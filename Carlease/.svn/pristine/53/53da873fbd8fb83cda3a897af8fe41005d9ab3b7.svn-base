package com.tools.os;

import android.content.Context;
import android.os.Debug;


/**
 * 内存信息
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class MemoryInfo {

	protected Context context = null;

	public MemoryInfo(Context context) {
		if (context == null) {
			// 不要throw
			return;
		}
		this.context = context;
	}

	/**
	 * 转为KB单位
	 * 
	 * @param size
	 * @return
	 */
	public static double toKB(long size) {
		return size / 1024.0D;
	}

	/**
	 * 转为M单位
	 * 
	 * @param size
	 * @return
	 */
	public static double toM(long size) {
		return toKB(size) / 1024.0D;
	}

	/**
	 * 转为G单位
	 * 
	 * @param size
	 * @return
	 */
	public static double toG(long size) {
		return toM(size) / 1024.0D;
	}

	public String toCrashString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("最大可分配的内存:");
		long max = Runtime.getRuntime().maxMemory();
		buffer.append( toM( max ) + "M" );
		buffer.append("\n");
		buffer.append("已使用的内存:");
		long use = Debug.getNativeHeapSize() + Runtime.getRuntime().totalMemory();
		buffer.append( toM( use ) + "M" );
		buffer.append("\n");
		buffer.append("未使用的内存:");
		buffer.append( toM( max - use ) + "M" );
		return buffer.toString();
	}

}
