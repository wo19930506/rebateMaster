package com.edm.servlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.edm.modules.utils.mapper.ObjectMappers;
import com.edm.modules.utils.web.Servlets;
import com.edm.utils.UserSession;
import com.edm.utils.comparator.NameComparator;
import com.edm.utils.comparator.SizeComparator;
import com.edm.utils.comparator.TypeComparator;
import com.edm.utils.web.HttpClient;
import com.edm.utils.web.Webs;
import com.google.common.collect.Maps;

/**
 * KindEditor文件管理类.
 * 
 * @author xiaobo
 */
public class ManagerServlet extends HttpServlet {

	private static final long serialVersionUID = -991810927994959751L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (UserSession.getUser() == null) {
			ObjectMappers.renderText(response, "请登录系统。");
			return;
		}
		
		Map<String, Object> map = Maps.newHashMap();
		String fileServer = Webs.fileServer();
		if(StringUtils.isNotBlank(fileServer) && StringUtils.startsWith(fileServer, "http://")) {
			remote(request, response, map);
		}else{
			local(request, response, map);
		}
		return;

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private void remote(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map)  throws ServletException, IOException {
		
		String dirName = request.getParameter("dir");
		
		if (!Arrays.<String> asList(new String[] { "image", "flash", "media", "file" }).contains(dirName)) {
			ObjectMappers.renderText(response, "Invalid Directory name.");
			return;
		}
		
		// 根据path参数，设置各路径和URL
		String path = request.getParameter("path") != null ? request.getParameter("path") : "";
		// 不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			ObjectMappers.renderText(response, "Access is not allowed.");
			return;
		}
		// 最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			ObjectMappers.renderText(response, "Parameter is not valid.");
			return;
		}
		// 排序形式，name or size or type
		String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";
		
		String requestUrl = Webs.fileServer() + "/kindEdit/fileManager";
		StringBuffer params = new StringBuffer("");
		params.append("corpId=" + UserSession.getCorpId());
		params.append("&");
		params.append("dir=" + dirName);
		params.append("&");
		params.append("path=" + path);
		params.append("&");
		params.append("order=" + order);
		String data = "";
		try {
			data = HttpClient.sentPost(requestUrl + "/", params.toString());
			map = ObjectMappers.readJson(data);
			String codeStr = String.valueOf(map.get("code"));
			Integer code = 0;
			if(StringUtils.isNotBlank(codeStr)){
				code = Integer.valueOf(codeStr);
			}
			if(code == 14){
				ObjectMappers.renderText(response, "目录不存在或不是目录");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ObjectMappers.renderJson(response, Servlets.TEXT_TYPE, map);
		return;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void local(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map)  throws ServletException, IOException {
		
		// 根目录路径
		String rootPath = request.getSession().getServletContext().getRealPath("/") + "static/res/attached/";
		// 根目录URL
		String rootUrl = request.getContextPath() + "/static/res/attached/";
		// 图片扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };

		String dirName = request.getParameter("dir");
		if (dirName != null) {
			if (!Arrays.<String> asList(new String[] { "image", "flash", "media", "file" }).contains(dirName)) {
				ObjectMappers.renderText(response, "Invalid Directory name.");
				return;
			}
			rootPath += dirName + "/";
			rootUrl += dirName + "/";
			File saveDirFile = new File(rootPath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
		}
		// 根据path参数，设置各路径和URL
		String path = request.getParameter("path") != null ? request.getParameter("path") : "";
		
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0, currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
		}

		// 排序形式，name or size or type
		String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";

		// 不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			ObjectMappers.renderText(response, "Access is not allowed.");
			return;
		}
		// 最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			ObjectMappers.renderText(response, "Parameter is not valid.");
			return;
		}
		// 目录不存在或不是目录
		File currentPathFile = new File(currentPath);
		if (!currentPathFile.isDirectory()) {
			ObjectMappers.renderText(response, "Directory does not exist.");
			return;
		}
		
		// 遍历目录取的文件信息
		List<Hashtable> fileList = new ArrayList<Hashtable>();
		if (currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				String file_path = StringUtils.replace(file.getPath(), "\\", "/");
				String root_path = StringUtils.replace(rootPath + UserSession.getCorpId(), "\\", "/");
				if (StringUtils.startsWith(file_path, root_path)) {
					Hashtable<String, Object> hash = new Hashtable<String, Object>();
					String fileName = file.getName();
					if (file.isDirectory()) {
						hash.put("is_dir", true);
						hash.put("has_file", (file.listFiles() != null));
						hash.put("filesize", 0L);
						hash.put("is_photo", false);
						hash.put("filetype", "");
					} else if (file.isFile()) {
						String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
						hash.put("is_dir", false);
						hash.put("has_file", false);
						hash.put("filesize", file.length());
						hash.put("is_photo", Arrays.<String> asList(fileTypes).contains(fileExt));
						hash.put("filetype", fileExt);
					}
					hash.put("filename", fileName);
					hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
					fileList.add(hash);
				}
			}
		}

		if ("size".equals(order)) {
			Collections.sort(fileList, new SizeComparator());
		} else if ("type".equals(order)) {
			Collections.sort(fileList, new TypeComparator());
		} else {
			Collections.sort(fileList, new NameComparator());
		}
		
		map.put("moveup_dir_path", moveupDirPath);
		map.put("current_dir_path", currentDirPath);
		map.put("current_url", currentUrl);
		map.put("total_count", fileList.size());
		map.put("file_list", fileList);

		ObjectMappers.renderJson(response, Servlets.TEXT_TYPE, map);
		return;
	}
	
}
