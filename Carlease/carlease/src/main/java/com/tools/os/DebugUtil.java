package com.tools.os;

import com.tools.util.Log;


/**
 * 调试
 * 
 * DebugUtil.startMemory("decode bitmap stream");
 *	// ...
 * DebugUtil.endMemory();
 * 
 * @author lmc
 * 
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 * 
 */
public class DebugUtil {

	private static final String TAG = DebugUtil.class.getSimpleName();
	private static final boolean DEBUG = true;

	protected static String Tag = null;

	protected static long startTime = 0L;
	protected static long endTime = 0L;
	protected static long useTime = 0L;
	
	protected static long startMemory = 0L;
	protected static long endMemory = 0L;
	protected static long useMemory = 0L;

	/**
	 * 统计使用时间
	 * 
	 * @param tag
	 */
	public static void startTime(String tag) {
		Tag = tag;
		if (DEBUG) Log.e(TAG, "tag:"+tag+",start time.");
		startTime = System.currentTimeMillis();
	}

	/**
	 * 统计使用时间
	 * 
	 * @return
	 */
	public static double endTime() {
		endTime = System.currentTimeMillis();
		useTime = (endTime-startTime);
		if (DEBUG) Log.e(TAG, "tag:"+Tag+",use time:"+useTime*0.001+"s");
		return useTime*0.001D;
	}

}
