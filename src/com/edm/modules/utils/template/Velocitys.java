package com.edm.modules.utils.template;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/**
 * 使用Velocity渲染模板内容的工具类.
 */
public abstract class Velocitys {

	static {
		Velocity.init();
	}

	/**
	 * 渲染模板内容.
	 * 
	 * @param templateContent 模板内容.
	 * @param context 变量Map.
	 */
	public static String renderTemplateContent(String templateContent, Map<String, ?> context) {
		VelocityContext velocityContext = new VelocityContext(context);

		StringWriter result = new StringWriter();
		Velocity.evaluate(velocityContext, result, "", templateContent);
		return result.toString();
	}

	/**
	 * 渲染模板文件.
	 * 
	 * @param velocityEngine velocityEngine, 需经过VelocityEngineFactory处理, 绑定Spring的ResourceLoader.
	 * @param templateContent 模板文件名, loader会自动在前面加上velocityEngine的resourceLoaderPath.
	 * @param context 变量Map.
	 */
	public static String renderFile(String templateFileName, VelocityEngine velocityEngine, String encoding, Map<String, ?> context) {
		VelocityContext velocityContext = new VelocityContext(context);

		StringWriter result = new StringWriter();
		velocityEngine.mergeTemplate(templateFileName, encoding, velocityContext, result);
		return result.toString();
	}
}
