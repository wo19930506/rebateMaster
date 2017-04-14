package com.edm.utils.web;

import org.springframework.ui.ModelMap;

import com.edm.modules.orm.MapBean;
import com.edm.modules.utils.Property;
import com.edm.utils.consts.Config;
import com.edm.utils.consts.Value;

public class Views {

	public static String _404() {
		return redirect("404");
	}

	public static String redirect(String result) {
		result = Property.getStr(Config.APP_URL) + "/" + result;
		return "redirect:" + result;
	}
	
	public static void map(ModelMap map, String m, String action, String operate, String message, String name, Object value) {
		map(map, m, action, operate, message, null, name, value);
	}
	
	public static void map(ModelMap map, String m, String action, String operate, String message, String subMessage, String name, Object value) {
		map.put("m", m);
		map.put("action", action);
		map.put("operate", operate);
		map.put("message", message);
		map.put("subMessage", subMessage);
		map.put("name", name);
		map.put("value", value);
	}
	
	public static void ok(MapBean mb, String message) {
		mb.put("code", Value.T);
		mb.put("message", message);
	}

	public static void error(MapBean mb, String message) {
		mb.put("code", Value.F);
		mb.put("message", message);
	}
}
