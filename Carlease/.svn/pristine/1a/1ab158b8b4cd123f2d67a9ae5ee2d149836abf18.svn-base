package com.tools.os.storage;


/**
 * SD信息
 * 
 * 例子：
 * /sdcard/company/app/crash
 * /sdcard/company/app/log
 * /sdcard/company/app/log/flowlog
 * /sdcard/company/app/log/statuslog
 * /sdcard/company/app/log/pushlog
 * /sdcard/company/app/upgrade
 * /sdcard/company/app/videoRecord
 * /sdcard/company/app/imageCache/news
 * 
 */
public class SDRam {

	// 公司名称
	private static final String company = "/hst";
	
	// 应用名称
	private static final String app = "/hiringcar";

	// crash文件夹
	private static final String crash = "/crash";

	// log文件夹
	private static final String log = "/log";
	
	// 流程日志文件夹
	private static final String flowLog = "/flowlog";
	//推送日志目录
	private static final String pushLog="/pushlog";
	//状态日志目录
	private static final String statusLog = "/statuslog";
	
	// 升级文件夹
	private static final String upgrade = "/upgrade";

	// 系统下载目录   /mnt/sdcard/Download
	private static final String systemDownload = "/Download";

	// 图片缓存文件夹
	private static final String imageCache = "/imageCache";
	
	// 保存图片
	private static final String image = "/image";
	
	// 新闻文件夹
	private static final String news = "/news";
	
	// 视频文件夹
	private static final String videoRecord = "/videoRecord";
	
	// 应用文件名
	private static final String apkName = "/HiringCar.apk";
	
	public static String getApkname() {
		return apkName;
	}

	/**
	 * 公司目录（主目录）
	 */
	public static String getCompanyPath() {
		return company;
	}

	/**
	 * 应用目录
	 */
	public static String getAppPath() {
		return getCompanyPath() + app;
	}

	/**
	 * crash目录
	 */
	public static String getCrashPath() {
		return getAppPath() + crash;
	}

	/**
	 * log目录
	 */
	public static String getLogPath() {
		return getAppPath() + log;
	}

	/**
	 * 流程日志目录
	 * @return
	 */
	public static String getFlowLogPath() {
		return getLogPath() + flowLog;
	}
	
	/**
	 * 推送日志目录
	 * @return
	 */
	public static String getPushLogPath(){
		return getLogPath()+pushLog;
	}
	
	/**
	 * 状态日志目录
	 * @return
	 */
	public static String getStatusLogPath() {
		return getLogPath() + statusLog;
	}
	
	/**
	 * 升级目录
	 */
	public static String getUpgradePath() {
		return getAppPath() + upgrade;
	}

	/**
	 * 系统下载目录
	 */
	public static String getSystemDownloadPath() {
		return systemDownload;
	}
	
	/**
	 * 图片缓冲目录
	 */
	public static String getImageCacheDir() {
		return getAppPath() + imageCache;
	}
	
	/**
	 * 图片缓冲目录
	 */
	public static String getImageDir() {
		return getAppPath() + image;
	}
	
	/**
	 * 新闻目录
	 */
	public static String getImageCacheForNews() {
		return getImageCacheDir() + news;
	}
	
	/**
	 * 视频目录
	 */
	public static String getVdeoRecordPath() {
		return getAppPath() + videoRecord;
	}

}
