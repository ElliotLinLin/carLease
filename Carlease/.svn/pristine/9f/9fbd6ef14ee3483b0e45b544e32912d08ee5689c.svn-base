package com.tools.sqlite.def;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 默认数据库Helper
 * 
 * @author LMC
 *
 */
public class SQLiteDefaultHelper extends SQLiteOpenHelper {
	
	// 数据库名称
	public static final String DB_NAME = "db_default.db";

	/**
	 * 应用版本1.0.0.1使用的SQLite版本是1
	 */
	public static final int DB_VERSION = 1;

	protected Context context = null;
	
	public SQLiteDefaultHelper(Context context, String name, int version) {
		super(context, name, null, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
