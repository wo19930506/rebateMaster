package com.edm.utils.consts;

import org.apache.commons.lang.StringUtils;

/**
 * LICENSE URL 验证.
 * 
 * @author SuperScott@Yeah.Net
 */
public enum UrlMap {

	CODE				("code"),
	ROBOT				("robot"),
	EXPIRE				("expire"),
	SEND1				("send1"),
	SEND2				("send2"),
	
	ACCOUNT				("account", "/account/history", "/account/password", "/account/profile", "/account/sender"),
	ACCOUNT_USER		("account.user", "/account/user"),
	DATASOURCE			("datasource", "/datasource/category", "/datasource/export", "/datasource/filter", "/datasource/form", "/datasource/prop", "/datasource/recipient", "/datasource/selection", "/datasource/tag"),
	MAILING_TEMPLATE	("mailing.template", "/mailing/category", "/mailing/template"),
    MAILING_CAMPAIGN    ("mailing.campaign", "/mailing/campaign"),
    MAILING_TASK        ("mailing.task", "/mailing/task"),
	MAILING_AUDIT		("mailing.audit", "/mailing/audit"),
	REPORT_CAMPAIGN		("report.campaign", "/report/campaign"),
	REPORT_TASK         ("report.task", "/report/task"),
	REPORT_TRIGER		("report.triger", "/report/triger"),
	REPORT_DETAIL		("report.detail", "/report/detail"),
	REPORT_EXPORT		("report.export", "/report/export"),
	STEP				("step", "/step");
	
	private final String action;
	private final String[] links;

	private UrlMap(String action, String... links) {
		this.action = action;
		this.links = links;
	}

	public String getAction() {
		return action;
	}

	public String[] getLinks() {
		return links;
	}

	public static final String getAction(String link) {
		for (UrlMap mapping : UrlMap.values()) {
			if (mapping.getLinks() == null || mapping.getLinks().length == 0) {
				continue;
			}
			for (String action : mapping.getLinks()) {
				if (StringUtils.startsWith(link, action)) {
					return mapping.getAction();
				}
			}
		}

		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getAction("/mailing/category"));
	}
}
