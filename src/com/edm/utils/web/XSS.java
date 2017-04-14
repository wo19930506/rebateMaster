package com.edm.utils.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;

import com.edm.modules.utils.Property;
import com.edm.utils.Asserts;
import com.edm.utils.Converts;
import com.edm.utils.consts.Config;

public class XSS {
    
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }

        text = text.replace("&", "&amp;");
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        text = text.replace("\"", "&quot;");
        text = text.replace("'", "&apos;");
        
        return text;
    }
    
    public static String clean(String str) {
        StringBuilder sbff = new StringBuilder();
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Clean.index(c) && !Clean.extension(c)) {
                sbff.append(c);
            }
        }
        
        /********** 限制 **********/
        // 1) : #http:,https:
        if (StringUtils.contains(str, ":")) {
            if (StringUtils.countMatches(str, ":") != (StringUtils.countMatches(str, "http:") + StringUtils.countMatches(str, "https:"))) {
                sbff.append(":");
            }
        }

        return sbff.toString();
    }
    
    public static Document safety(Document doc) {
        String title = doc.select("title").text();
        String style = doc.body().attr("style");
        
        doc.select("title").remove();
        doc.select("meta").remove();
        
        Document safety = new Cleaner(Safety.whitelist()).clean(doc);
        safety.head().appendElement("meta");
        safety.head().select("meta").attr("http-equiv", "Content-Type");
        safety.head().select("meta").attr("content", "text/html; charset=utf-8");
        if (StringUtils.isNotBlank(title)) safety.title(title);
        if (StringUtils.isNotBlank(style)) safety.body().attr("style", style);

        Safety.clean(safety);
        
        return safety;
    }
    
    public static Document safety4(Document doc) {
        String resourceUrls = Property.getStr(Config.RESOURCE_URLS);
        
        String[] urls = null;
        if (StringUtils.isNotBlank(resourceUrls)) {
            urls = Converts._toStrings(resourceUrls);
            if (urls.length == 0) {
                urls = null;
            }
        }
        
        String title = doc.select("title").text();
        String style = doc.body().attr("style");
        
        List<Element> links = doc.select("link");
        List<Element> scripts = doc.select("script");
        
        doc.select("title").remove();
        doc.select("meta").remove();
        doc.select("link").remove();
        doc.select("script").remove();
        
        Document safety4 = new Cleaner(Safety4.whitelist()).clean(doc);
        safety4.head().appendElement("meta");
        safety4.head().select("meta").attr("http-equiv", "Content-Type");
        safety4.head().select("meta").attr("content", "text/html; charset=utf-8");
        
        if (StringUtils.isNotBlank(title)) safety4.title(title);
        if (StringUtils.isNotBlank(style)) safety4.body().attr("style", style);
        if (!Asserts.empty(links)) {
            for (Element e : links) {
                if (urls != null) {
                    for (String url : urls)
                        if (!StringUtils.startsWith(e.attr("href"), url)) continue;
                }
                if (!StringUtils.isBlank(e.html())) continue;
                safety4.head().appendChild(e);
            }
        }
        if (!Asserts.empty(scripts)) {
            for (Element e : scripts) {
                if (urls != null) {
                    for (String url : urls)
                        if (!StringUtils.startsWith(e.attr("href"), url)) continue;
                }
                if (!StringUtils.isBlank(e.html())) continue;
                safety4.body().lastElementSibling().appendChild(e);
            }
        }
        Safety.clean(safety4);
        
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        safety4.prependChild(new DocumentType("html", publicId, systemId, ""));
        safety4.child(0).attr("xmlns", "http://www.w3.org/1999/xhtml");
        
        return safety4;
    }
}
