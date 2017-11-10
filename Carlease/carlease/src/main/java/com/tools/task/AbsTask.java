package com.tools.task;

import android.content.Context;
import android.os.AsyncTask;

import com.tools.util.Log;

/**
 * 异步任务，封装系统的android.os.AsyncTask<Params, Progress, Result>
 * 
 * 权限：
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public abstract class AbsTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	private static final String TAG = AbsTask.class.getSimpleName();

	protected Context context = null;

	public AbsTask(Context context) {
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {
		if (context == null) {
			Log.exception(TAG, new NullPointerException("context == null"));
		}
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

	@Override
	protected void onPreExecute() {
		//		Log.e(TAG, "onPreExecute");
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Result result) {
		//		Log.e(TAG, "onPostExecute()");
		super.onPostExecute(result);
	}

	/**
	 * 关闭
	 */
	public void cancel() {
		Log.e(TAG, "cancel()");
		// 关闭AsyncTask
		super.cancel(true);
	}

}
