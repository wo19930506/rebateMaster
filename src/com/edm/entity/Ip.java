package com.edm.entity;

/**
 * Ip地域映射.
 * 
 * @author xiaobo
 */
public class Ip {

	private Long nip1;
	private Long nip2;
	private String sip1;
	private String sip2;
	private String region;
	private String isp;

	public Long getNip1() {
		return nip1;
	}

	public void setNip1(Long nip1) {
		this.nip1 = nip1;
	}

	public Long getNip2() {
		return nip2;
	}

	public void setNip2(Long nip2) {
		this.nip2 = nip2;
	}

	public String getSip1() {
		return sip1;
	}

	public void setSip1(String sip1) {
		this.sip1 = sip1;
	}

	public String getSip2() {
		return sip2;
	}

	public void setSip2(String sip2) {
		this.sip2 = sip2;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

}
