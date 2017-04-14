package com.edm.utils.helper;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.edm.utils.consts.Value;
import com.edm.utils.file.Files;

public class Charts {

	public static String cost(String webRoot, String path) {
		Document doc = Jsoup.parse(Files.get(webRoot, path));
		
		Elements charts = doc.select(".chart_index");
		
		if (charts.isEmpty()) {
			return Value.S;
		}
		
		for (Element chart : charts) {
			int y = 0;
			for (Element td : chart.select("tr")) {
				td.children().empty();
				td.children().first().text("#$Y#" + y + "#/$Y#");
				y++;
			}
		}
		charts.removeClass("chart_index");

		Elements items = doc.select(".chart_item");
		for (Element chart : items) {
			for (Element cost : chart.select("span.chart_param")) {
				cost.text("#$COST#" + cost.text() + "#/$COST#");
				cost.removeClass("chart_param");
			}
			int x = 0;
			for (Element height : chart.select("td.chart_param")) {
				height.attr("height", "#$X#" + x + "#/$X#");
				height.removeClass("chart_param");
				x++;
			}
		}

		items.removeClass("chart_item");
		String content = doc.select(".chart_table").html();

		content = StringUtils.replace(content, "#$Y#", "<$Y>");
		content = StringUtils.replace(content, "#/$Y#", "</$Y>");
		content = StringUtils.replace(content, "#$X#", "<$X>");
		content = StringUtils.replace(content, "#/$X#", "</$X>");
		content = StringUtils.replace(content, "#$COST#", "<$COST>");
		content = StringUtils.replace(content, "#/$COST#", "</$COST>");

		return content;
	}
	
	public static void filter(Document doc) {
		Elements elements = doc.select(".chart_table");
		if (!elements.isEmpty()) {
			elements.get(0).text("#$CHART##/$CHART#");
		}
	}
}
