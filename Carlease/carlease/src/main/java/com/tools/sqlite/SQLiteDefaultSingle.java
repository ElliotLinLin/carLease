package com.tools.sqlite;

/**
 * 默认数据库单例
 * 
 * @author LMC
 *
 */
public class SQLiteDefaultSingle {

	private static SQLiteManager mInstance = null;

	public static synchronized SQLiteManager getInstance() {
		if (mInstance == null) {
			mInstance = new SQLiteManager();
		}
		return mInstance;
	}

}