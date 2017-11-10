package com.hst.Carlease.share;

import com.tools.share.SharePres;


/**
 * 单例模式
 * 
 * 使用synchronized同步方法
 * 
 * 例子：
 * 		Log.e(TAG, "--- SharePresSingle start ---");
		// init
		SharePresSingle.getInstance().init(context);
		// open
		SharePresSingle.getInstance().open("set", Context.MODE_PRIVATE);
		// use
		SharePresSingle.getInstance().putString("key_string", "valuse-_sfe");
		SharePresSingle.getInstance().putInteger("key_int", 2323);
		SharePresSingle.getInstance().putLong("key_long", 332);
		SharePresSingle.getInstance().putBoolean("key_bool", true);
		SharePresSingle.getInstance().putFloat("key_flaot", 3.34F);
		// print
		Log.e(TAG, SharePresSingle.getInstance().getString("key_string", ""));
		Log.e(TAG, "--- SharePresSingle end ---");
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class ShareSingle {

	// 实例
	private static SharePres instance = null;

	/**
	 * 修饰符为private
	 */
	private ShareSingle() {

	}

	/**
	 * 得到实例
	 * 
	 * @return
	 */
	public synchronized static SharePres getInstance() {
		if (instance == null) {
			instance = new SharePres();
		}
		return instance;
	}

}
