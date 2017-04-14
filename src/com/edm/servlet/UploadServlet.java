package com.edm.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import com.edm.modules.utils.mapper.ObjectMappers;
import com.edm.modules.utils.web.Servlets;
import com.edm.utils.UserSession;
import com.edm.utils.file.TempUploadUtil;
import com.edm.utils.web.Webs;
import com.google.common.collect.Maps;

/**
 * KindEditor文件上传类.
 * 
 * @author xiaobo
 */
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = -5159158836335162112L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> map = Maps.newHashMap();
		if (UserSession.getUser() == null) {
			renderJson(response, map, "请登录系统。");
			return;
		}
		
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

	private void renderJson(HttpServletResponse response, Map<String, Object> map, String message) {
		map.put("error", 1);
		map.put("message", message);
		ObjectMappers.renderJson(response, Servlets.HTML_TYPE, map);
	}
	
	@SuppressWarnings("rawtypes")
	private void remote(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map)  throws ServletException, IOException {
		if (UserSession.getUser() == null) {
			renderJson(response, map, "请登录系统。");
			return;
		}
		
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

		// 最大文件大小
		long maxSize = 1 * 1024 * 1024; // 1M

		if (!ServletFileUpload.isMultipartContent(request)) {
			renderJson(response, map, "请选择文件。");
			return;
		}
		
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
	
		List items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			renderJson(response, map, "上传文件失败。");
			return;
		}
		
		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if (!extMap.containsKey(dirName)) {
			renderJson(response, map, "目录名不正确。");
			return;
		}
		
		Iterator itr = items.iterator();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			String fileName = item.getName();
			long fileSize = item.getSize();
			if (!item.isFormField()) {
				// 检查文件大小
				if (fileSize > maxSize) {
					renderJson(response, map, "上传文件大小超过限制。");
					return;
				}
				// 检查扩展名
				String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				if (!Arrays.<String> asList(extMap.get(dirName).split(",")).contains(fileExt)) {
					renderJson(response, map, "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
					return;
				}

				try {
					InputStream in = item.getInputStream();
					String requestUrl = Webs.fileServer() + "/kindEdit/upload" + "?dirName=" + dirName + "&corpId=" + UserSession.getCorpId();
					String data = TempUploadUtil.toUploadFile(in, fileName, "imgFile", requestUrl);
					map = ObjectMappers.readJson(data);
					String codeStr = map.get("code") + "";
					Integer code = 0;
					if(StringUtils.isNotBlank(codeStr)){
						code = Integer.valueOf(codeStr);
					}
					if(code == 0){
						renderJson(response, map, "上传文件失败。");
						return;
					}else if(code == 11){
						renderJson(response, map, "企业ID为空");
						return;
					}else if(code == 12){
						renderJson(response, map, "请选择文件。");
						return;
					}else if(code == 13){
						renderJson(response, map, "上传目录没有写权限");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					renderJson(response, map, "上传文件失败。");
					return;
				}

				ObjectMappers.renderJson(response, Servlets.TEXT_TYPE, map);
				return;
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void local(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map)  throws ServletException, IOException {
		if (UserSession.getUser() == null) {
			renderJson(response, map, "请登录系统。");
			return;
		}
		
		// 文件保存目录路径
		String savePath = request.getSession().getServletContext().getRealPath("") + "/static/res/attached/";
		// 文件保存目录URL
		String saveUrl = request.getContextPath() + "/static/res/attached/";

		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

		// 最大文件大小
		long maxSize = 1 * 1024 * 1024; // 1M

		if (!ServletFileUpload.isMultipartContent(request)) {
			renderJson(response, map, "请选择文件。");
			return;
		}
		
		// 检查目录
		File uploadDir = new File(savePath);
		if (!uploadDir.isDirectory()) {
			renderJson(response, map, "上传目录不存在。");
			return;
		}
		
		// 检查目录写权限
		if (!uploadDir.canWrite()) {
			renderJson(response, map, "上传目录没有写权限。");
			return;
		}

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if (!extMap.containsKey(dirName)) {
			renderJson(response, map, "目录名不正确。");
			return;
		}
		
		// 创建文件夹
		savePath += dirName + "/";
		saveUrl += dirName + "/";
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String ym = sdf.format(new Date());
		savePath += UserSession.getCorpId() + "/" + ym + "/";
		saveUrl += UserSession.getCorpId() + "/" + ym + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
	
		List items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			renderJson(response, map, "上传文件失败。");
			return;
		}
		
		Iterator itr = items.iterator();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			String fileName = item.getName();
			long fileSize = item.getSize();
			if (!item.isFormField()) {
				// 检查文件大小
				if (fileSize > maxSize) {
					renderJson(response, map, "上传文件大小超过限制。");
					return;
				}
				// 检查扩展名
				String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				if (!Arrays.<String> asList(extMap.get(dirName).split(",")).contains(fileExt)) {
					renderJson(response, map, "上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
					return;
				}

				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
				try {
					File uploadedFile = new File(savePath, newFileName);
					item.write(uploadedFile);
				} catch (Exception e) {
					renderJson(response, map, "上传文件失败。");
					return;
				}

				map.put("error", 0);
				map.put("url", saveUrl + newFileName);
				ObjectMappers.renderJson(response, Servlets.TEXT_TYPE, map);
				return;
			}
		}
	}
	
}
