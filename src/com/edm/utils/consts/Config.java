package com.edm.utils.consts;
/**
 * 
 * @author xiaobo
 * @see app.peoperties 中对应的字段
 *
 */
public interface Config {

	public static final String APP_URL = "app.url";
	public static final String APP_URLS = "app.urls";
    public static final String WEBSITE_URL = "website.url";
    public static final String RESOURCE_URLS = "resource.urls";
    
	public static final String SMTP_HOST = "smtp.host";
	public static final String SMTP_PORT = "smtp.port";
	public static final String SMTP_FROM = "smtp.from";

	public static final String MONGO_HOST = "mongo.host";
	public static final String MONGO_PORT = "mongo.port";
	public static final String MONGO_POOL_SIZE = "mongo.pool.size";
	public static final String MONGO_COLL_SIZE = "mongo.coll.size";
	public static final String MONGO_RECIPIENT_SIZE = "mongo.recipient.size";
	public static final String MONGO_TAG_SIZE = "mongo.tag.size";
	
	public static final String UNIQUE_CODE = "unique.code";
	public static final String CONCURRENCY_COUNT = "concurrency.count";
	public static final String UPLOAD_SIZE = "upload.size";
	/**
	 * 生成任务文件路径
	 * <prev>如：E:/test </prev>
	 */
	public static final String ROOT_PATH = "root.path";
	/**
	 * 上传文件服务器地址
	 * <prev>如：http://127.0.0.1:8888/showpic </prev>
	 */
	public static final String FILE_SERVER = "file.server";
	/** 每次扫描任务的基础文件个数 */
	public static final String SCAN_SIZE = "scan.size";
	/** 针对某个版本的菜单显示配置，是否显示菜单。值为：false|true，不设置则默认为true*/
	public static final String HR_CATALOG_SHOW = "hr.catalog.show";
	
}
