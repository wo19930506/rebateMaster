package com.edm.modules.utils.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.edm.modules.utils.web.Servlets;

/**
 * ObjectMapper工具类.
 * 
 * 实现获取Request/Response/Session与绕过jsp/freemaker直接输出文本的简化函数.
 */
public abstract class ObjectMappers {

	//-- header 常量定义 --//
	private static final String HEADER_ENCODING = "encoding";
	private static final String HEADER_NOCACHE = "no-cache";
	private static final String DEFAULT_ENCODING = "UTF-8";
	private static final boolean DEFAULT_NOCACHE = true;

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * 写json.
	 */
	public static String writeJson(final Object object) {
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(object);
			return jsonString;
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 读json.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> readJson(final String jsonString) {
		Map<String, Object> jsonMap = null;
		try {
			jsonMap = mapper.readValue(jsonString, Map.class);
			return jsonMap;
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> readJson(InputStream input, String charset) {
		Map<String, Object> jsonMap = null;
		try {
			jsonMap = mapper.readValue(new InputStreamReader(input, charset), Map.class);
			return jsonMap;
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	//-- 绕过jsp/freemaker直接输出文本的函数 --//
	/**
	 * 直接输出内容的简便函数.

	 * eg.
	 * render("text/plain", "hello", "encoding:GBK");
	 * render("text/plain", "hello", "no-cache:false");
	 * render("text/plain", "hello", "encoding:GBK", "no-cache:false");
	 * 
	 * @param headers 可变的header数组,目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
	 */
	public static void render(final HttpServletResponse response, final String contentType, final String content, final String... headers) {
		initResponseHeader(response, contentType, headers);
		try {
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 直接输出文本.
	 * @see #render(String, String, String...)
	 */
	public static void renderText(final HttpServletResponse response, final String text, final String... headers) {
		render(response, Servlets.TEXT_TYPE, text, headers);
	}

	/**
	 * 直接输出HTML.
	 * @see #render(String, String, String...)
	 */
	public static void renderHtml(final HttpServletResponse response, final String html, final String... headers) {
		render(response, Servlets.HTML_TYPE, html, headers);
	}

	/**
	 * 直接输出XML.
	 * @see #render(String, String, String...)
	 */
	public static void renderXml(final HttpServletResponse response, final String xml, final String... headers) {
		render(response, Servlets.XML_TYPE, xml, headers);
	}

	/**
	 * 直接输出JSON.
	 * 
	 * @param jsonString json字符串.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(final HttpServletResponse response, final String jsonString, final String... headers) {
		render(response, Servlets.JSON_TYPE, jsonString, headers);
	}
	
	/**
	 * 直接输出JSON,使用Jackson转换Java对象.
	 * 
	 * @param data 可以是List<POJO>, POJO[], POJO, 也可以Map名值对.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(final HttpServletResponse response, final Object data, final String... headers) {
		initResponseHeader(response, Servlets.JSON_TYPE, headers);
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 直接输出JSON,使用Jackson转换Java对象.
	 * 
	 * @param data 可以是List<POJO>, POJO[], POJO, 也可以Map名值对.
	 * @see #render(String, String, String...)
	 */
	public static void renderJson(final HttpServletResponse response, final String contentType, final Object data, final String... headers) {
		initResponseHeader(response, contentType, headers);
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * 直接输出支持跨域Mashup的JSONP.
	 * 
	 * @param callbackName callback函数名.
	 * @param object Java对象,可以是List<POJO>, POJO[], POJO ,也可以Map名值对, 将被转化为json字符串.
	 */
	public static void renderJsonp(final HttpServletResponse response, final String callbackName, final Object object, final String... headers) {
		String result = new StringBuilder().append(callbackName).append("(").append(writeJson(object)).append(");").toString();

		//渲染Content-Type为javascript的返回内容,输出结果为javascript语句, 如callback197("{html:'Hello World!!!'}");
		render(response, Servlets.JS_TYPE, result, headers);
	}
	
	/**
	 * 分析并设置contentType与headers.
	 */
	private static void initResponseHeader(final HttpServletResponse response, final String contentType, final String... headers) {
		//分析headers参数
		String encoding = DEFAULT_ENCODING;
		boolean noCache = DEFAULT_NOCACHE;
		for (String header : headers) {
			String headerName = StringUtils.substringBefore(header, ":");
			String headerValue = StringUtils.substringAfter(header, ":");

			if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
				encoding = headerValue;
			} else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
				noCache = Boolean.parseBoolean(headerValue);
			} else {
				throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
			}
		}

		//设置headers参数
		String fullContentType = contentType + ";charset=" + encoding;
		response.setContentType(fullContentType);
		if (noCache) {
			Servlets.setDisableCacheHeader(response);
		}
	}
}
