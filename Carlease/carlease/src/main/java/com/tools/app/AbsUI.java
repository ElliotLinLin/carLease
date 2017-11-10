package com.tools.app;

import android.content.Context;
import android.widget.Toast;

import com.hst.Carlease.ui.LoginUI;
import com.tools.net.NetworkState;



/**
 * 注意：
 * 不要引用其他的Activity，会发生泄漏 ，可以引用Context，因为Context是全局的
 *
 *	弃用	
 * 		@Deprecated
 * 
 * 在线android系统源码
 * http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.4_r1/android/os/Environment.java
 * 
 * 
 * 默认值，由系统判断状态自动切换
 * super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
 * 
 * 强制设置为横屏
 * super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
 * 
 * 强制设置为竖屏
 * super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
 * 
 * 弱引用环境例子：
 * 
 * 	// 加入
 * 	VersionA version = new VersionA();
	version.setName("vvvvv");
	addWeakContext("version", version);
	version = null; // 释放全局引用

	// 取出key对应的值
	VersionA version = (VersionA)getWeakContext("version");
	Log.e(TAG, "name:"+version.getName());
 *
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public abstract class AbsUI extends AbsUI2 {

	private static final String TAG = AbsUI.class.getSimpleName();
	private static final boolean D = true;

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}
	
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
	
	@Override
	public void onLowMemory() {
//		LoginUI.islogin=false;
		super.onLowMemory();
	}

	
	
}