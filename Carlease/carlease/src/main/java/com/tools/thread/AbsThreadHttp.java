package com.tools.thread;

import android.content.Context;
import android.os.Handler;

import com.tools.net.NetworkState;
import com.tools.net.http.HttpTool;
import com.tools.util.Log;


/**
 * http异步线程，将http封装起来，可判断网络是否有效。
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public abstract class AbsThreadHttp extends AbsThread {

	private static final String TAG = AbsThreadHttp.class.getSimpleName();

	// http
	protected HttpTool http = null;

	// 网络状态
	protected NetworkState networkState = null;

	// 处理消息
	private Handler absThreadHttpHandler = new Handler();

	public AbsThreadHttp(Context context) {
		super();
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {

		http = new HttpTool(context);
		
		networkState = new NetworkState(context);

		if (networkState == null || networkState.isConnected() == false) {
			// 处理消息
			absThreadHttpHandler.post(new Runnable() {

				@Override
				public void run() {
					// 反转
					onHttpFailed(com.tools.net.http.HttpTool.Error.NotNetwork, new java.lang.Exception("not network"), -1, null);
				}

			});
			// 这里不要取消线程cancel()，让其经过onPostExecute()
			return;
		}

		// 监听http事件
		http.setOnCompletedListener(new HttpTool.OnCompletedListener() {

			@Override
			public void onSuccessful(byte[] out) {

			}

			@Override
			public void onFailed(final com.tools.net.http.HttpTool.Error error, final java.lang.Exception exception, final int responseCode, final byte[] out) {
				Log.e(TAG, "onFailed()");
				// 处理消息
				absThreadHttpHandler.post(new Runnable() {

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

	// 抽象方法，用于反转。
	protected abstract void onHttpFailed(com.tools.net.http.HttpTool.Error error, java.lang.Exception exception, int responseCode, byte[] out);

}
