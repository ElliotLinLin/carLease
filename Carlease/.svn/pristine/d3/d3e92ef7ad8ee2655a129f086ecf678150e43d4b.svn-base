package com.tools.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.test.AndroidTestCase;

public class NetworkStateTestCase extends AndroidTestCase {

	private static final String TAG = NetworkStateTestCase.class.getCanonicalName();

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
	}

	public void testRun() {
		NetworkState network = new NetworkState(this.getContext());
//		Log.e(TAG, "type:"+network.getType());
//		Log.e(TAG, "apn:"+network.getAPN());
//		Log.e(TAG, "getState:"+network.getState());
//		Log.e(TAG, "TYPE_MOBILE:"+network.isAvailable( ConnectivityManager.TYPE_MOBILE ));
//		Log.e(TAG, "isConnected:"+network.isConnected());
//		network.print(network.getActiveNetworkInfo());
		network.print( ConnectivityManager.TYPE_MOBILE );
	}

}
