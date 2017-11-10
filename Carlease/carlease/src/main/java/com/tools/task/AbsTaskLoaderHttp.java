package com.tools.task;

import android.content.Context;
import android.os.Handler;

import com.tools.net.NetworkState;
import com.tools.net.http.HttpTool;
import com.tools.util.Log;

/**
 * http的Loader任务，将http封装起来，可判断网络是否有效。
 * 
 * 权限：
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * 
 * @author LMC
 *
 * @param <D>
 */
public abstract class AbsTaskLoaderHttp<D> extends AbsTaskLoader<D> {

	private static final String TAG = AbsTaskLoaderHttp.class.getSimpleName();
	private static final boolean D = false;

	// http
	protected HttpTool http = null;

	// 网络状态
	protected NetworkState networkState = null;

	// 处理消息
	private Handler absTaskLoaderHttpHandler = new Handler(); 

	public AbsTaskLoaderHttp(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {

		http = new HttpTool(context);

		networkState = new NetworkState(context);

		// TODO 暂时不要，判断太快了，有些地方不适用
		//		if (networkState == null || networkState.isConnected() == false) {
		//			// 处理消息
		//			absTaskLoaderHttpHandler.post(new Runnable() {
		//
		//				@Override
		//				public void run() {
		//					// 反转
		//					onHttpFailed(com.tools.net.http.HttpTool.Error.NotNetwork, new java.lang.Exception("not network"), -1, null);
		//				}
		//
		//			});
		//			// 这里不要取消线程abandon()，让其经过onLoadFinished()
		//			return;
		//		}

		// 监听http事件
		http.setOnCompletedListener(new HttpTool.OnCompletedListener() {

			@Override
			public void onSuccessful(byte[] out) {

			}

			@Override
			public void onFailed(final com.tools.net.http.HttpTool.Error error, final java.lang.Exception exception, final int responseCode, final byte[] out) {
				if (D) Log.e(TAG, "onFailed()");
				// 处理消息
				absTaskLoaderHttpHandler.post(new Runnable() {

					@Override
					public void run() {
						// 反转
						onHttpFailed(error, exception, responseCode, out);
					}

				});
			}

		});

	}

	/**
	 * 得到网络状态对象
	 * 
	 * @return
	 */
	public NetworkState getNetworkState() {
		return this.networkState;
	}

	/**
	 * 得到HTTP
	 * 
	 * @return
	 */
	public HttpTool getHttp() {
		return this.http;
	}

	@Override
	protected void onStopLoading() {
		if (D) Log.e(TAG, "onStopLoading()");
		// 关闭或者取消HTTP
		cancelHttp();
		super.onStopLoading();
	}

	@Override
	public void onCanceled(D data) {
		if (D) Log.e(TAG, "onCanceled(D data)");
		// 关闭或者取消HTTP
		cancelHttp();
		super.onCanceled(data);
	}

	@Override
	protected void onAbandon() {
		if (D) Log.e(TAG, "onAbandon()");
		// 关闭或者取消HTTP
		cancelHttp();
		super.onAbandon();
	}

	@Override
	protected void onReset() {
		if (D) Log.e(TAG, "onReset()");
		// 关闭或者取消HTTP
		cancelHttp();
		super.onReset();
	}

	/**
	 * 关闭或者取消HTTP
	 */
	protected void cancelHttp() {
		if (D) Log.e(TAG, "cancelHttp()");
		if (http != null) {
			http.disconnect();
		}
	}

	// 抽象方法，用于反转。
	protected abstract void onHttpFailed(com.tools.net.http.HttpTool.Error error, java.lang.Exception exception, int responseCode, byte[] out);

}
