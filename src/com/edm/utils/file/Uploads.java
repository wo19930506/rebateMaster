package com.edm.utils.file;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import com.edm.modules.utils.Property;
import com.edm.utils.consts.Config;
import com.edm.utils.except.Errors;

public class Uploads {
	

	public static void maxSize(MultipartFile file) {
		if (file.isEmpty()) {
			throw new Errors("文件内容不能为空值");
		}
		long maxSize = Property.getInt(Config.UPLOAD_SIZE);
		if (file.getSize() > maxSize) {
			throw new Errors("文件大小不能大于" + Files.unit(maxSize));
		}
	}
	
	public static String upload(int corpId, String charset, MultipartFile file, String savePath, String filePath) {
		try {
			Files.valid(savePath + filePath);
			
			DateTime now = new DateTime();
			String dir = Files.dir(corpId, now);
			String name = Files.name(now, "." + StringUtils.substringAfterLast(file.getOriginalFilename(), "."));
			
			Files.make(savePath + filePath + dir);
			
			String output = savePath + filePath + dir + name;
			String suff = Files.suffix(name);
			if (suff.equals(Suffix.TXT)) {
				Writers.txt(charset, "UTF-8", file.getInputStream(), new FileOutputStream(output));
			} else if (suff.equals(Suffix.CSV)) {
				Writers.csv(charset, "UTF-8", file.getInputStream(), new FileOutputStream(output));
			} else if (suff.equals(Suffix.XLS) || suff.equals(Suffix.XLSX)) {
				Writers.excel(suff, file.getInputStream(), new FileOutputStream(output));
			} else {
				throw new Errors("文件后缀不是合法值");
			}
			
			return filePath + dir + name;
		} catch (Errors e) {
			throw new Errors(e.getMessage(), e);
		} catch (Exception e) {
			throw new Errors("文件上传失败", e);
		}
	}
	
	public static String upload(int corpId, String content, String savePath, String filePath) {
		try {
			File path = new File(savePath + filePath);
			if(!path.exists()) Files.make(savePath + filePath);
			Files.valid(savePath + filePath);
			
			DateTime now = new DateTime();
			String dir = Files.dir(corpId, now);
			String name = Files.name(now, ".txt");
			
			Files.make(savePath + filePath + dir);
			
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + filePath + dir + name), "UTF-8"));
			writer.print(content);
			writer.flush();
			writer.close();
			return filePath + dir + name;
		} catch (Errors e) {
			throw new Errors(e.getMessage(), e);
		} catch (Exception e) {
			throw new Errors("文件上传失败", e);
		} 
	}
	
	private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
	private static final String CHARSET = "UTF-8"; // 设置编码
	
	public static String toUploadFile(File file, String fileKey, String requestURL) {
		String result = null;

		try {
			URL url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// conn.setConnectTimeout(connectTimeout);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = null;
			String params = "";

			if(file != null && !file.getPath().equals("")) {
				sb = new StringBuffer();
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
				if(file.getName().contains(".jpg") || file.getName().contains(".png")) {
					sb.append("Content-Disposition:form-data; name=\"" + fileKey + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
				} else {
					sb.append("Content-Disposition:form-data; name=\"" + fileKey + "\"; filename=\"" + file.getName() + ".jpg" + "\"" + LINE_END);
				}
				// 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
				sb.append("Content-Type:multipart/form-data" + LINE_END); // 这里配置的Content-type很重要的，用于服务器端辨别文件的类型的
				sb.append(LINE_END);
				params = sb.toString();
				sb = null;
				dos.write(params.getBytes(CHARSET));
				/** 上传文件 */
				// 从本地存储设备上将文件读入输入流中
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[100];
				int len = 0;
				while((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes(CHARSET));
				// 上传文件结束，加上结束边界
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes(CHARSET);
				dos.write(end_data);
			} else {
				byte[] end_data = (PREFIX + BOUNDARY).getBytes(CHARSET);
				dos.write(end_data);
			}
			// 将参数及图片数据上传至服务器
			dos.flush();
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = conn.getResponseCode();
			if(res == 200) {
				InputStream input = conn.getInputStream();
				result = "";
				byte[] buffer = new byte[2048];
				while(input.read(buffer) != -1) {
					result += new String(buffer, Charset.forName(CHARSET)).trim();
				}
				return result;
			} else {
				return "";
			}
		} catch(MalformedURLException e) {
			e.printStackTrace();
			return "";
		} catch(IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
