package com.tools.task;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.tools.util.Log;

/**
 * Loader异步任务，封装系统的android.support.v4.content.AsyncTaskLoader<D>。
 * 
 * 权限：
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * 
 * @author LMC
 *
 * @param <D>
 */
public abstract class AbsTaskLoader<D> extends AsyncTaskLoader<D> {

	private static final String TAG = AbsTaskLoader.class.getSimpleName();
	private static final boolean D = false;

	protected Context context = null;

	public AbsTaskLoader(Context context) {
		super(context);
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

	@Override
	public void onCanceled(D data) {
		if (D) Log.e(TAG, "onCanceled(D data)");
		super.onCanceled(data);
	}

	@Override
	protected void onForceLoad() {
		if (D) Log.e(TAG, "onForceLoad()");
		super.onForceLoad();
	}

	@Override
	protected D onLoadInBackground() {
		if (D) Log.e(TAG, "onLoadInBackground()");
		return super.onLoadInBackground();
	}

	@Override
	protected void onAbandon() {
		if (D) Log.e(TAG, "onAbandon()");
		super.onAbandon();
	}

	@Override
	protected void onReset() {
		if (D) Log.e(TAG, "onReset()");
		super.onReset();
	}

	@Override
	protected void onStartLoading() {
		if (D) Log.e(TAG, "onStartLoading()");
		// 强制装载
		super.forceLoad();
		super.onStartLoading();
	}

	@Override
	protected void onStopLoading() {
		if (D) Log.e(TAG, "onStopLoading()");
		super.onStopLoading();
	}

	/**
	 * 打印状态
	 */
	public void print() {
		print(this);
	}

	/**
	 * 打印状态
	 * 使用：
	 * AbsTaskLoader.print(loader);
	 */
	public static void print(Loader<?> loader) {
		if (loader == null) {
			return;
		}
		Log.i(TAG, "------ print() start ------");
		Log.i(TAG, "getId():"+loader.getId());
		Log.i(TAG, "isAbandoned():"+loader.isAbandoned());
		Log.i(TAG, "isReset():"+loader.isReset());
		Log.i(TAG, "isStarted():"+loader.isStarted());
		Log.i(TAG, "takeContentChanged():"+loader.takeContentChanged());
		Log.i(TAG, "------ print() end ------");
	}

}
