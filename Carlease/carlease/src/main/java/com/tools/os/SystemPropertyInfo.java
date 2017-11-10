package com.tools.os;

import android.content.Context;

/**
 * Java SystemProperty 信息
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class SystemPropertyInfo {

	protected Context context;

	public SystemPropertyInfo(Context context) {
		if (context == null) {
			// 不要throw
			return;
		}
		this.context = context;
	}

	public String toCrashString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("java.net.preferIPv4Stack:");
		buffer.append( java.lang.System.getProperty("java.net.preferIPv4Stack") );
		buffer.append("\n");
		buffer.append("java.net.preferIPv6Addresses:");
		buffer.append( java.lang.System.getProperty("java.net.preferIPv6Addresses") );
		return buffer.toString();
	}

}
