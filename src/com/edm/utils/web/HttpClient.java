package com.edm.utils.web;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP Client
 * @author xiaobo
 * @see http的get和post请求
 */
public class HttpClient {

	private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url 发送请求的URL
	 * @param params 请求参数，请求参数应该是name1=value1&name2=value2的形式
	 * @return URL 远程资源的响应
	 * @throws IOException
	 */
	public static String sentGet(String urlStr, String params) throws IOException {
		URL url = null;
		byte[] result = null;
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try{
			// 构造URL对象
			url = new URL(urlStr + "?" + params);
			//打开连接
			urlConn = (HttpURLConnection) url.openConnection();
			//获取响应码，判断请求是否成功
			if (urlConn.getResponseCode() == 200) {
				inputStream = urlConn.getInputStream();
				result = read(inputStream);
			}
			// 关闭InputStreamReader
			inputStream.close();
			// 关闭http连接
			urlConn.disconnect();
		}catch(Exception e) {
			logger.error(e.getMessage());
		}finally {
			
		}
		return new String(result, "UTF-8");
	}

	/**
	 * 向指定URL发送POST方法的请求
	 * 
	 * @param urlStr 发送请求的URL
	 * @param params 请求参数，请求参数应该是name1=value1&name2=value2的形式
	 * @return URL 远程资源的响应
	 */
	public static String sentPost(String urlStr, String params) throws Exception {
		byte[] result = null;
		BufferedReader reader = null;
		URL url = null;
		url = new URL(urlStr);
		logger.info("请求URL：" + urlStr);
		logger.info("请求参数：" + params);
		DataOutputStream out = null;
		try {
			// 使用HttpURLConnection打开连接
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			// 因为这个是post请求,设置为true
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setConnectTimeout(10*1000);
			// 设置以POST方式
			urlConn.setRequestMethod("POST");
			// Post 请求不能使用缓存
			urlConn.setUseCaches(false);
			urlConn.setInstanceFollowRedirects(true);
			urlConn.setRequestProperty("accept", "*/*");
//			urlConn.setRequestProperty("Accept-Language", "zh-CN");
			urlConn.setRequestProperty("Charset", "UTF-8");
			// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConn.connect();
			out = new DataOutputStream(urlConn.getOutputStream());
			out.write(params.getBytes());
			logger.info("请求返回码：" + urlConn.getResponseCode());
			InputStream inputStream = urlConn.getInputStream();
			result = read(inputStream);
			// 关闭输入流
			inputStream.close();
			// 关闭http连接
			urlConn.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} finally {
			if (reader != null) {
				reader.close();
			}
			if(out != null){
				out.flush();
				out.close();
			}
		}
		return new String(result, "UTF-8");
	}
	
	private static byte[] read(InputStream inputStream) throws IOException {
		byte[] data = new byte[1024];
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len = 0;
		while ((len = inputStream.read(data)) > 0) {
			outputStream.write(data, 0, len);
		}
		inputStream.close();
		return outputStream.toByteArray();
	}
	
}