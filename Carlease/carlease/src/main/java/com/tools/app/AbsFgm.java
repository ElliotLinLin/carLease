package com.tools.app;

import android.content.Context;
import android.widget.Toast;

import com.tools.net.NetworkState;



/**
 * 在线android系统源码
 * http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.4_r1/android/os/Environment.java
 * 
 * 弃用		@Deprecated
 * 
 */
public abstract class AbsFgm extends AbsFgm2 {

	private static final String TAG = AbsFgm.class.getSimpleName();
	private static final boolean DEBUG = false;
	
	
	/**
	 * 判断是否有网络
	 * @param context
	 * @return
	 * HL
	 */
	public static boolean netWorkIsConn(Context context){
		NetworkState nState = new NetworkState(context);
		
		if (nState.isConnected()) {
			return true;
		}else {
			Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

}
