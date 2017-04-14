package com.edm.utils.web;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

import com.edm.utils.Asserts;

public class Safety {

	private static Whitelist WHITELIST = new Whitelist();

	static {
		WHITELIST.addTags(
				"a",
				"b", "blockquote", "br",
				"caption", "center", "cite", "code", "col", "colgroup",
				"dd", "div", "dl", "dt",
				"em",
				"font",
				"h1", "h2", "h3", "h4", "h5", "h6", "hr",
				"i", "img",
				"li",
				"meta",
				"ol",
				"p", "pre",
				"q",
				"small", "span", "strike", "strong", "sub", "sup",
				"table", "tbody", "td", "tfoot", "th", "thead", "title", "tr",
				"u", "ul")
		    .addAttributes("a", "href", "title", "target")
		    .addAttributes("img", "align", "border", "height", "src", "title", "width")
		    .addAttributes("meta", "http-equiv", "content")
		    .addAttributes("table", "align", "bgcolor", "border", "cellpadding", "cellspacing", "width")
		    .addAttributes("tbody", "align", "valign")
		    .addAttributes("td", "align", "bgcolor", "colspan", "height", "rowspan", "valign", "width")
		    .addAttributes("th", "align", "bgcolor", "colspan", "height", "rowspan", "valign", "width")
		    .addAttributes("tfoot", "align", "valign")
		    .addAttributes(":all", "class", "id", "name", "style")

		    .addProtocols("a", "href", "ftp", "http", "https", "mailto")
		    .addProtocols("img", "src", "http", "https");
	}
	
	public static Whitelist whitelist() {
		return WHITELIST;
	}
	
	public static void clean(Document doc) {
		for (Element ele : doc.select("*")) {
			for (Attribute attr : ele.attributes()) {
				String key = StringUtils.lowerCase(attr.getKey());
				String value = StringUtils.lowerCase(attr.getValue());
				if (key.equalsIgnoreCase("style") && !clean(value)) {
					attr.setValue("");
				}
			}
		}
	}
	
	private static boolean clean(String value) {
		if (Asserts.containsAny(value, new String[] { ":expression(" })) {
			return false;
		}

		return true;
	}
}
