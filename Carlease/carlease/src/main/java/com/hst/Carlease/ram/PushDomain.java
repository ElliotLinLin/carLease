package com.hst.Carlease.ram;

import android.content.Context;

import com.tools.app.AppInfo;


/**
 * push域名和端口配置
 * 
 * @author LMC
 *
 */
public class PushDomain {


	private static final String __host__ = "carlife.wisdom-gps.com";


	private static String host ="";

	private static int port = 2295;

	// PUSH协议版本
	public static String version = "1.1";

	public static String getHost() {

		host = "202.103.191.20"; //内网
		return host;
				
	}

	public static int getPort() {
		
		port = 2295;
		
		return port;
	}

	public static String getVersion() {
		return version;
	}

	/**
	 * 得到应用版本号
	 * 
	 * @param c
	 * @return
	 */
	public static String getAppVersion(Context c) {
		return new AppInfo(c).getVersion();
	}

}
