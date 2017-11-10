package com.hst.Carlease.ram;

/**
 * Http信息
 */
public class HttpRam {

	// Http的URL字符集
	private static final String urlCharset = com.tools.os.Charset.UTF_8;
	// Http的内容字符集
	private static final String textCharset = com.tools.os.Charset.UTF_8;
	// 转成本地使用的字符集
	private static final String localCharset = com.tools.os.Charset.UTF_8;
	
	// ContentType
	private static final String contentType = com.tools.net.http.HttpContentType.Application_JSON;

	// 连接超时
	private static int connectTimeout = 20 * 1000;
	// 读超时
	private static int readTimeout = 20 * 1000;
	// 会话
	private static String sessionId = "";

	public static String getUrlcharset() {
		return urlCharset;
	}

	public static String getTextcharset() {
		return textCharset;
	}

	public static String getLocalcharset() {
		return localCharset;
	}

	public static int getConnectTimeout() {
		return connectTimeout;
	}

	public static int getReadTimeout() {
		return readTimeout;
	}

	public static String getContenttype() {
		return contentType;
	}

	public static String getSessionId() {
		return sessionId;
	}

	public static void setSessionId(String sessionId) {
		HttpRam.sessionId = sessionId;
	}

}