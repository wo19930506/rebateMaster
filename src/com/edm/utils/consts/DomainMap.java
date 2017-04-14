package com.edm.utils.consts;

/**
 * 邮箱域名.
 * 
 * @author SuperScott@Yeah.Net
 */
public enum DomainMap {

	RICHINFO("139邮箱", "139.com"),
	NETEASE	("网易邮箱", "126.com", "163.com", "188.com", "vip.126.com", "vip.163.com", "yeah.net"),
	GOOGLE	("Gmail邮箱", "gmail.com", "google.com"), 
	TENCENT	("QQ邮箱", "foxmail.com", "qq.com", "vip.qq.com"),
	SINA	("新浪邮箱", "sina.cn", "sina.com", "sina.com.cn", "vip.sina.com", "vip.sina.com.cn"), 
	SOHU	("搜狐邮箱", "sohu.com", "sohu.net", "vip.sohu.com");

	private final String name;
	private final String[] domains;

	private DomainMap(String name, String... domains) {
		this.name = name;
		this.domains = domains;
	}

	public String getName() {
		return name;
	}

	public String[] getDomains() {
		return domains;
	}

	public static String[] getDomains(String name) {
		for (DomainMap mapper : DomainMap.values()) {
			if (mapper.getName().equals(name)) {
				return mapper.getDomains();
			}
		}
		return null;
	}
}
