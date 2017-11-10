package com.tools.sqlite;


import android.content.Context;
import android.provider.Settings;
import android.test.AndroidTestCase;

import com.tools.util.Log;


public class SQLiteObserverTestCase extends AndroidTestCase {

	private static final String TAG = SQLiteObserverTestCase.class.getSimpleName();

	/*
	 * 初始化资源
	 */
	@Override
	protected void setUp() throws Exception {
		//		Log.e(TAG, "setUp");
		super.setUp();
	}

	@Override
	protected void runTest() throws Throwable {
		//		Log.e(TAG, "runTest");
		super.runTest();
	}

	/*
	 * 垃圾清理与资源回收
	 */
	@Override
	protected void tearDown() throws Exception {
		//		Log.e(TAG, "tearDown");
		super.tearDown();
	}

	public void testRun(Context context) {
		
		SQLiteObserver sqliteObserver = new SQLiteObserver(context);
		
		// 监听蓝牙开关
		sqliteObserver.registerSystem(Settings.Secure.BLUETOOTH_ON, new SQLiteObserver.IObserverListener() {

			@Override
			public void onChange(Object value) {
				Log.e(TAG, "onChange():value:"+value);
			}

		});

		// 监听GPS开关
		sqliteObserver.registerSystem(Settings.Secure.LOCATION_PROVIDERS_ALLOWED, new SQLiteObserver.IObserverListener() {

			@Override
			public void onChange(Object value) {
				// 开 onChange():value:none,network,gps
				// 关 onChange():value:none,network
				Log.e(TAG, "onChange():value:"+value);
			}

		});
		
	}

}
