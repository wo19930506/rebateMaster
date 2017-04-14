package com.edm.entity;

/**
 * 地域统计.
 * 
 * @author yjli
 */
public class Region {

	private Integer corpId;
	private Integer taskId;
	private Integer templateId;
	private Integer regionId;
	private Integer readCount;
	private Integer readUserCount;
	private Integer clickCount;
	private Integer clickUserCount;
	private Integer unsubscribeCount;
	private Integer forwardCount;

	private String regionName;
	private String provinceName;
	private String cityName;

	public Integer getCorpId() {
		return corpId;
	}

	public void setCorpId(Integer corpId) {
		this.corpId = corpId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public Integer getReadCount() {
		return readCount;
	}

	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}

	public Integer getReadUserCount() {
		return readUserCount;
	}

	public void setReadUserCount(Integer readUserCount) {
		this.readUserCount = readUserCount;
	}

	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public Integer getClickUserCount() {
		return clickUserCount;
	}

	public void setClickUserCount(Integer clickUserCount) {
		this.clickUserCount = clickUserCount;
	}

	public Integer getUnsubscribeCount() {
		return unsubscribeCount;
	}

	public void setUnsubscribeCount(Integer unsubscribeCount) {
		this.unsubscribeCount = unsubscribeCount;
	}

	public Integer getForwardCount() {
		return forwardCount;
	}

	public void setForwardCount(Integer forwardCount) {
		this.forwardCount = forwardCount;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
