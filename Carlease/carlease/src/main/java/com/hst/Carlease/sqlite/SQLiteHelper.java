package com.hst.Carlease.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hst.Carlease.sqlite.bean.PushSeqNo;
import com.hst.Carlease.sqlite.bean.UserInforDb;
import com.tools.sqlite.SQLiteTable;
import com.tools.util.Log;

/**
 * 数据库初始化
 *
 * @author lmc 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHelper.class.getSimpleName();

	public static final String DB_NAME = "databases.db";

	/**
	 * 应用版本1.4.0.19使用的SQLite版本是1
	 */
	public static final int DB_VERSION = 1;

	protected Context context = null;

	public SQLiteHelper(Context context, String name, int version) {
		super(context, name, null, version);
		this.context = context;
	}

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 *
	 * @param src
	 * @return
	 */
	protected static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}

	/* 创建数据库 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate() SQLiteDatabase db");

		SQLiteTable.create(db, UserInforDb.class);//创建用户表
		SQLiteTable.create(db, PushSeqNo.class);//创建关键字历史记录表


	}

	/**
	 * 新增PushSeqNo表
	 *
	 * @param db
	 */
	private void creatTablePushSeqNo(SQLiteDatabase db) {
		SQLiteTable.create(db, PushSeqNo.class);
	}

	/* 打开数据库 */
	@Override
	public void onOpen(SQLiteDatabase db) {
		// Log.d(TAG, "onOpen");
		super.onOpen(db);
	}

	/*
	 * 升级数据库 安装新的，不会经过onUpgrade()
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade()->oldVersion:" + oldVersion + ",newVersion:"
				+ newVersion);

	}

}
