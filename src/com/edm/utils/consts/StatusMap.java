package com.edm.utils.consts;

/**
 * 任务状态.
 * 
 * @author SuperScott@Yeah.Net
 */
public enum StatusMap {

	DRAFTED		(0, "草稿"),
	PROOFING	(11, "审核中"),
	UNAPPROVED	(12, "审核不通过"),
	REVIEWING	(13, "审核中"),
	RETURN		(14, "审核不通过"),
	TEST		(21, "测试发送"),
	WAITED		(22, "待发送"),
	QUEUEING	(23, "发送中"),
	PROCESSING	(24, "发送中"),
	PAUSED		(25, "已暂停"),
	CANCELLED	(26, "已取消"),
	COMPLETED	(27, "已发送"),
	NOTFIND	    (28, "文件失效");
	private final int id;
	private final String name;

	private StatusMap(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static final String getName(int id) {
		for (StatusMap mapper : StatusMap.values()) {
			if (mapper.getId() == id) {
				return mapper.getName();
			}
		}
		return null;
	}
}
