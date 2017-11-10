package com.tools.share;


import android.content.Context;
import android.content.SharedPreferences;

import com.tools.util.Log;


/**
 * TODO
 * 注意：
 * 只能用于进程间共享数据，而RemoteService和远程进程无法共享数据。
 * 第一次能读到，但内容改变后，再次读到的内容是旧内容，
 * 无法读到已改变的新内容。
 * 
 * 例子：
 * 
 * Log.e(TAG, "--- SharePres start ---");
		// 对象
		SharePres sharePres = new SharePres(context);
		// 打开
		sharePres.open("set", Context.MODE_PRIVATE);
		// 注册事件
		sharePres.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener(){
			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				Log.e(TAG, "onSharedPreferenceChanged:key:"+key);
			}
		});
		// 提交
		sharePres.putString("name", "zh_CN");
		sharePres.putString("age", "25");
		sharePres.putString("size", "32");
		// 得到
		Log.e(TAG, "name:"+sharePres.getString("name", ""));
		Log.e(TAG, "age:"+sharePres.getString("age", ""));
		Log.e(TAG, "size:"+sharePres.getString("size", ""));
		// 事务
		Log.e(TAG, "isTransaction:"+sharePres.isTransaction());
		sharePres.beginTransaction();
		Log.e(TAG, "isTransaction:"+sharePres.isTransaction());
		sharePres.putString("key1", "value1");
		sharePres.putLong("key2", 111111);
		sharePres.putLong("key3", 22222);
		sharePres.commitTransaction();
		Log.e(TAG, "isTransaction:"+sharePres.isTransaction());
		Log.e(TAG, "getFileName:"+sharePres.getFileName());
		Log.e(TAG, "--- SharePres end ---");
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class SharePres {

	private static final String TAG = SharePres.class.getSimpleName();
	private static final boolean DEBUG = true;

	protected Context context;
	protected SharedPreferences share;
	protected SharedPreferences.Editor editor;
	protected SharedPreferences.OnSharedPreferenceChangeListener listener;
	protected String fileName;
	protected int mode = 0;

	protected boolean autoCommit = true;

	// Context.MODE_PRIVATE Constant Value: 0 (0x00000000)
	// Context.MODE_WORLD_READABLE Constant Value: 1 (0x00000001)
	// Context.MODE_WORLD_WRITEABLE Constant Value: 2 (0x00000002)

	public SharePres() {
		
	}

	public SharePres(Context context) {
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 * @param fileName
	 * @param mode
	 */
	public void init(Context context) {
		if (context == null) {
			// 抛出异常
			throw new NullPointerException("context == null");
		}
		this.context = context;
	}

	/**
	 * 打开
	 */
	public boolean open(String fileName, int mode) {
		if (isEmptyString(fileName)) {
			Log.exception(TAG, new NullPointerException("isEmptyString(fileName) == true"));
			return false;
		}
		this.fileName = fileName;
		this.mode = mode;

		if (context == null) {
			return false;
		}
		share = context.getApplicationContext().getSharedPreferences(fileName, mode);

		if (share == null) {
			return false;	
		}
		editor = share.edit();
		
		return true;
	}

	/**
	 * 得到文件名
	 * 
	 * @return
	 */
	public String getFileName() {
		return this.fileName;
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
	 * 得到SharedPreferences.Editor对象
	 * 
	 * @return
	 */
	public SharedPreferences.Editor getEditor() {
		return this.editor;
	}

	/**
	 * 注册事回调
	 * 
	 * @param listener
	 */
	public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
		this.listener = listener;
		if (this.share != null) this.share.registerOnSharedPreferenceChangeListener(listener);
	}

	public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
		this.listener = null;
		if (this.share != null) this.share.unregisterOnSharedPreferenceChangeListener(listener);
	}

	public void putBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		if(autoCommit) {
			commit();
		}
	}

	public void putFloat(String key, float value) {
		editor.putFloat(key, value);
		if(autoCommit) {
			commit();
		}
	}

	public void putInteger(String key, int value) {
		editor.putInt(key, value);
		if(autoCommit) {
			commit();
		}
	}

	public void putLong(String key, long value) {
		editor.putLong(key, value);
		if(autoCommit) {
			commit();
		}
	}

	public void putString(String key, String value) {
		editor.putString(key, value);
		if(autoCommit) {
			commit();
		}
	}

	public boolean getBoolean(String key, boolean defValue) {
		return share.getBoolean(key, defValue);
	}

	public float getFloat(String key, float defValue) {
		return share.getFloat(key, defValue);
	}

	public int getInteger(String key, int defValue) {
		return share.getInt(key, defValue);
	}

	public long getLong(String key, long defValue) {
		return share.getLong(key, defValue);
	}

	public String getString(String key, String defValue) {
		return share.getString(key, defValue);
	}

	/**
	 * 开始事务（验证通过）
	 */
	public void beginTransaction() {
		autoCommit = false;
	}

	/**
	 * 事务是否打开
	 * 
	 * @return
	 */
	public boolean isTransaction() {
		return !autoCommit;
	}

	/**
	 * 结束事务（验证通过）
	 */
	public void commitTransaction() {
		autoCommit = true;
		// 提交
		commit();
	}

	/**
	 * 提交
	 */
	public void commit() {
		this.editor.commit();
	}

	/**
	 * 判断键是否存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean isKeyExists(String key) {
		return share.contains(key);
	}

	/**
	 * 移除（已验证）
	 * 
	 * @param key
	 */
	public void remove(String key) {
		editor.remove(key);
		commit();
	}

	/**
	 * 清除全部（已验证）
	 */
	public void clear() {
		editor.clear();
		commit();
	}

}