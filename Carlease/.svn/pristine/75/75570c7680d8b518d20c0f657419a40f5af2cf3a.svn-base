package com.tools.sqlite;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

import com.tools.util.Log;

/**
 * 数据库监听
 * 
 * Settings.Secure.getUriFor(Settings.Secure.BLUETOOTH_ON);
 * toString:content://settings/secure/bluetooth_on
 * 
 * 一般情况下：
 * settings为数据名
 * secure为表名
 * bluetooth_on为列名
 * 
 * 
 * http://blog.csdn.net/jiahui524/article/details/7016430
 * http://www.cnblogs.com/devinzhang/archive/2012/01/20/2327863.html
 * 
 * 
 * 想使用
 * Settings.Secure.putString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED, "gps");
 * 修改gps关开，但是失败
 * 加入下述二个权限也一样失败
 *  <uses-permission android:name="android.permission.WRITE_SETTINGS" />
    <uses-permission android:name="android.permission.WRITE_SECURE_SETTINGS" />

 * 
 * 使用例子：
 * 
 * SQLiteObserver sqliteObserver = new SQLiteObserver(context);

		// 监听蓝牙开关
		sqliteObserver.registerSystem(android.provider.Settings.Secure.BLUETOOTH_ON, new SQLiteObserver.IObserverListener() {

			@Override
			public void onChange(Object value) {
				Log.e(TAG, "onChange():value:"+value);
			}

		});

		// 监听GPS开关
		sqliteObserver.registerSystem(android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED, new SQLiteObserver.IObserverListener() {

			@Override
			public void onChange(Object value) {
				// 开 onChange():value:none,network,gps
				// 关 onChange():value:none,network
				Log.e(TAG, "onChange():value:"+value);
			}

		});
		
		// 监听Settings.Secure.WIFI_ON开关
		
		// 监听Settings.System.AIRPLANE_MODE_ON飞行模式开关

 * 
 * TODO 设置监听之前如何得到其值呢？如下：
 * String location_allowed = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
 * Log.e(TAG, "location_allowed:"+location_allowed); // location_allowed:network,gps
 * 
 * 
 * @author LMC
 *
 */
public class SQLiteObserver {

	private static final String TAG = SQLiteObserver.class.getSimpleName();

	protected Context context = null;

	protected IObserverListener iObserverListener = null;

	protected Uri uri = null;

	protected MyContentObserver myContentObserver = null;

	// 事件接口
	public interface IObserverListener {
		void onChange(Object value);
	}

	protected class MyContentObserver extends ContentObserver {

		public MyContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			//			Log.e(TAG, "onChange "+selfChange);
			if (iObserverListener != null) {

				//				int sssss = 999;
				//				try {
				//					sssss = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.BLUETOOTH_ON);
				//				} catch (SettingNotFoundException e) {
				//					e.printStackTrace();
				//				}
				//	            Log.i(TAG, " BLUETOOTH_ON -----> " +sssss);
				//	            
				//	            String isAirplaneOpen = Settings.Secure.getString(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON);
				//	            Log.i(TAG, " isAirplaneOpen -----> " +isAirplaneOpen);
				//				
				//				iObserverListener.onChange(isAirplaneOpen);

				// 从Uri里得到name
				String systemName = getSystemNameFromUri(uri);

				// 由系统名字得到该值
				String value = getValueFromSystemName(systemName);

				// 调用
				iObserverListener.onChange(value);

				//				if (isEmptyString(systemName) == false) {
				//					String value = Settings.Secure.getString(context.getContentResolver(), systemName);
				//					if (isEmptyString(value)) {
				//						value = Settings.System.getString(context.getContentResolver(), systemName);
				//					}
				//					Log.i(TAG, " value -----> " +value);
				//					iObserverListener.onChange(value);
				//				}

			}
			super.onChange(selfChange);
		}

		@Override
		public boolean deliverSelfNotifications() {
			//			Log.e(TAG, "deliverSelfNotification()");
			//			if (iObserverListener != null) {
			//				iObserverListener.onChange("onChange22222222222");
			//			}
			return super.deliverSelfNotifications();
		}

	};

	public SQLiteObserver(Context context) {
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

	/**
	 * 从Uri里得到name
	 * 
	 * @param uri
	 * @return
	 */
	protected String getSystemNameFromUri(Uri uri) {
		if (uri == null) {
			return null;
		}
		Log.i(TAG, "getSystemNameFromUri:" + uri.getLastPathSegment());
		return uri.getLastPathSegment();
	}

	/**
	 * 系统的名称转为Uri
	 * 
	 * @param systemName
	 * @return
	 */
	protected Uri systemName2Uri(String systemName) {
		if (isEmptyString(systemName)) {
			return null;
		}

		Uri uri = null;
		uri = Settings.Secure.getUriFor(systemName);
		if (uri == null) {
			Log.i(TAG, "systemName2Uri URI == NULL");
			uri = Settings.System.getUriFor(systemName);
		}
		if (uri != null) {
			Log.i(TAG, "systemName2Uri:"+uri.toString());
		}
		return uri;
	}

	/**
	 * 由系统名字得到该值
	 * 
	 * @param systemName
	 * @return
	 */
	protected String getValueFromSystemName(String systemName) {
		if (isEmptyString(systemName)) {
			return null;
		}

		String value = null;

		value = Settings.Secure.getString(context.getContentResolver(), systemName);
		if (isEmptyString(value)) {
			Log.i(TAG, "Settings.System.getString(...) ............");
			value = Settings.System.getString(context.getContentResolver(), systemName);
		}
		Log.i(TAG, "getValueFromSystemName:" + value);
		return value;
	}

	/**
	 * 注册
	 * 
	 * @param uri
	 * @param iObserverListener
	 */
	public void register(Uri uri, IObserverListener iObserverListener) {
		Log.i(TAG, "register()");

		if (uri == null || iObserverListener == null) {
			return;
		}

		//		Log.printUri(TAG, uri);

		this.uri = uri;

		// 移除
		unregister( iObserverListener );

		// 触发器分为表触发器、行触发器
		myContentObserver = new MyContentObserver( new Handler() );

		ContentResolver resolver = context.getContentResolver();
		// 第二个参数，为false表示精确匹配，即只匹配该Uri
		// 第二个参数，为true表示可以同时匹配其派生的Uri
		resolver.registerContentObserver(uri, false, myContentObserver);
		Log.i(TAG, "register() --- OK");

		this.iObserverListener = iObserverListener;
	}

	/**
	 * 注册系统自带的
	 * 
	 * @param systemName 系统自带名称
	 * @param iObserverListener
	 */
	public void registerSystem(String systemName, IObserverListener iObserverListener) {
		Log.i(TAG, "registerSystem()");
		// 系统的名称转为Uri
		Uri uri = systemName2Uri(systemName);
		// 注册
		register(uri, iObserverListener);
		Log.i(TAG, "registerSystem() --- OK");
	}

	/**
	 * 移除
	 * 
	 * @param iObserverListener
	 */
	public void unregister(IObserverListener iObserverListener) {
		Log.i(TAG, "unregister()");

		ContentResolver resolver = context.getContentResolver();

		if ( myContentObserver != null) {
			resolver.unregisterContentObserver( myContentObserver );
			myContentObserver = null;
		}

		// 触发器分为表触发器、行触发器
		this.iObserverListener = null;
	}

	//	public void register(String pageageName, String databaseName, String tableName, IObserverListener iObserverListener) {
	//		Log.e(TAG, "register()");
	//
	//		//		uri = Uri.parse("content://com.aaaaaaaaaaaaaaaa.bbbbb/aaa1/UserInfo");
	//		//		uri = Uri.parse("content://aaa1/UserInfo/#");
	//		uri = Settings.Secure.getUriFor(Settings.Secure.BLUETOOTH_ON);
	//
	//		//		uri = Settings.Secure.getUriFor(Settings.Secure.BLUETOOTH_ON); // ok
	//		Log.printUri(TAG, uri);
	//
	//		ContentResolver resolver = context.getContentResolver();
	//
	//		if ( myContentObserver != null) {
	//			resolver.unregisterContentObserver( myContentObserver );
	//		}
	//
	//		// 触发器分为表触发器、行触发器
	//		myContentObserver = new MyContentObserver( new Handler() );
	//		resolver.registerContentObserver(uri, true, myContentObserver);
	//
	//		this.iObserverListener = iObserverListener;
	//	}

}
