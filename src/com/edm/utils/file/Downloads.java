package com.edm.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.joda.time.DateTime;

import com.edm.modules.orm.MapBean;
import com.edm.modules.utils.Encodes;
import com.edm.utils.except.Errors;
import com.edm.utils.web.Webs;

public class Downloads {

	public static void download(HttpServletResponse response, String savePath, String filePath, String fileName) {
		Webs.setHeader(response, Encodes.urlEncode(fileName));

		File file = new File(savePath + filePath);
		if (!file.exists()) {
			file = new File(Webs.webRoot() + filePath);
			if(!file.exists()) throw new Errors("文件不存在");
		}

		try {
			OutputStream output = response.getOutputStream();
			String suff = Files.suffix(fileName);
			
			if (suff.equals(Suffix.TXT)) {
				Writers.txt("UTF-8", "GBK", new FileInputStream(file), output);
			} else if (suff.equals(Suffix.CSV)) {
				Writers.csv("UTF-8", "GBK", new FileInputStream(file), output);
			} else if (suff.equals(Suffix.XLS) || suff.equals(Suffix.XLSX)) {
				Writers.excel(suff, new FileInputStream(file), output);
			} else if (suff.equals(Suffix.DOCX)) {
                IOUtils.copy(new FileInputStream(file), output);
			} else {
			    throw new Errors("文件后缀不是合法值");
			}
			
			output.flush();
			output.close();
		} catch (Exception e) {
			throw new Errors("文件下载失败", e);
		}
	}
	
	public static void download(HttpServletResponse response, String savePath, List<MapBean> fileMap) {
		try {
			String fileName = StringUtils.replace(Encodes.urlEncode(new DateTime().toString("yyyy/MM/dd HH:mm") + ".zip"), "+", "%20");
			Webs.setHeader(response, fileName);

			ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
			out.setEncoding("gbk");
			for (MapBean mb : fileMap) {
				String filePath = savePath + mb.getString("path");
				String name = mb.getString("name");
				String suff = StringUtils.lowerCase(mb.getString("suff"));
				File file = new File(filePath);
				if (!file.exists()) {
					continue;
				}
				out.putNextEntry(new ZipEntry(name + "." + suff));
				
				FileInputStream fis = new FileInputStream(file);
				byte[] buff = new byte[1024];
				int len = 0;
				while ((len = fis.read(buff)) != -1) {
					out.write(buff, 0, len);
				}
				fis.close();
			}

			out.flush();
			out.close();
		} catch (Exception e) {
			throw new Errors("文件下载失败", e);
		}
	}
}
