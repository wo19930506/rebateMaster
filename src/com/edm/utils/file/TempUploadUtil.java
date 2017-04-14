package com.edm.utils.file;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * 上传工具类
 * @author xiaobo
 *
 */
public class TempUploadUtil {

	private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

	private TempUploadUtil() {
		
	}

	private static final int readTimeOut = 10 * 1000; // 读取超时

	private static final String CHARSET = "UTF-8"; // 设置编码

	/***
	 * 上传成功
	 */
	public static final int UPLOAD_SUCCESS_CODE = 1;
	/**
	 * 服务器出错
	 */
	public static final int UPLOAD_SERVER_ERROR_CODE = 3;

	/**
	 * 上传文件到服务器
	 * 
	 * @param in
	 *            需要上传的文件流
	 * @param fileName
	 *            上传的文件名
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param requestURL
	 *            请求的URL
	 */
	public static String toUploadFile(InputStream in, String fileName, String fileKey, String requestURL) {
		String result = "{\"code\": 0}";

		try {
			URL url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(readTimeOut);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
//			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = null;
			String params = null;

			if(in != null) {
				sb = new StringBuffer();
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
				sb.append("Content-Disposition:form-data; name=\"" + fileKey + "\"; filename=\"" + fileName + "\"" + LINE_END);
				// sb.append("Content-Type:image/pjpeg" + LINE_END); //
				// 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
				sb.append("Content-Type:multipart/form-data" + LINE_END); // 这里配置的Content-type很重要的，用于服务器端辨别文件的类型的
				sb.append(LINE_END);
				params = sb.toString();
				sb = null;
				dos.write(params.getBytes(CHARSET));
				/** 上传文件 */
				// 从本地存储设备上将文件读入输入流中
				byte[] bytes = new byte[100];
				int len = 0;
				while((len = in.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				in.close();
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
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				input.close();
				conn.disconnect();
			}
		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}