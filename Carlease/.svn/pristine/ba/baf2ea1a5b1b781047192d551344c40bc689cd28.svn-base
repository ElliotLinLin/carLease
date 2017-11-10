package com.tools.net;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.tools.content.pm.PermissionTool;
import com.tools.lang.StringUtil;
import com.tools.lang.reflect.ReflectTool;
import com.tools.util.Log;

/**
 * 网络管理，如：打开、关闭网络
 * 
 * 支持2.2及以上，因ConnectivityManager类从2.2开始都有这两个方法
 * getMobileDataEnabled 不需要权限
 * setMobileDataEnabled 需要权限
 * 
 * 权限
 * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
 * 
 * 在线源码
 * http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android-apps/4.1.2_r1/com/android/phone/MobileNetworkSettings.java#MobileNetworkSettings.0mButtonDataEnabled
 * http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.2_r1.1/android/net/ConnectivityManager.java#ConnectivityManager.setMobileDataEnabled%28boolean%29
 * 
 * @author LMC
 *
 */
public class NetworkManager {

	private static final String TAG = NetworkManager.class.getSimpleName();

	private static final String Class_ConnectivityManager = "android.net.ConnectivityManager";
	private static final String Method_getMobileDataEnabled = "getMobileDataEnabled";
	private static final String Method_setMobileDataEnabled = "setMobileDataEnabled";

	// Level17以下采用Settings.System.AIRPLANE_MODE_ON
	// Level17及以上采用Settings.Global.AIRPLANE_MODE_ON
	private static final String AIRPLANE_MODE_ON = "airplane_mode_on";

	private Context context = null;
	
	protected ConnectivityManager connectivityManager = null;

	public NetworkManager(Context context) {
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 判断移动网络是否打开(验证通过)
	 * 
	 * android.net.ConnectivityManager类里有两个方法是Hide
	 * public boolean getMobileDataEnabled()
	 * public void setMobileDataEnabled(boolean)
	 * 
	 * 不需要权限
	 * 
	 * @return
	 */
	public boolean isMobileDataEnabled() {

		// 不需要检查权限
		//		PermissionTool.checkThrow(context, android.Manifest.permission.CHANGE_NETWORK_STATE);

		String classPath = Class_ConnectivityManager;
		String methodName = Method_getMobileDataEnabled;

		boolean isExists = ReflectTool.isMethodExists(classPath, methodName);
		Log.i(TAG, "isMobileDataEnabled():isExists:"+isExists);
		if (isExists == false) {
			return false;
		}

		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}

		Method method = ReflectTool.getMethod(classPath, methodName);
		Object object = ReflectTool.invokeMethod(connectivityManager, method);
		if (object == null) {
			return false;
		}

		boolean isMobileDataEnabled = false;
		isMobileDataEnabled = StringUtil.Object2Boolean(object);
		Log.i(TAG, "isMobileDataEnabled():"+isMobileDataEnabled);

		return isMobileDataEnabled;
	}

	/**
	 * 设置移动网络(验证通过)
	 * 
	 * 注意：
	 * 通过setMobileDataEnabled(true)打开移动网络时，不能马上通过isMobileDataEnabled()获取状态。
	 * 可能需要时间吧。
	 * 
	 * 需要权限
	 * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
	 * 
	 * @param enabled
	 */
	public void setMobileDataEnabled(boolean enabled) {

		// 权限检查
		PermissionTool.checkThrow(context, android.Manifest.permission.CHANGE_NETWORK_STATE);

		String classPath = Class_ConnectivityManager;
		String methodName = Method_setMobileDataEnabled;

		boolean isExists = ReflectTool.isMethodExists(classPath, methodName);
		Log.i(TAG, "setMobileDataEnabled():isExists:"+isExists);
		if (isExists == false) {
			return;
		}

		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return;
		}

		Method method = ReflectTool.getMethod(classPath, methodName, boolean.class);
		ReflectTool.invokeMethod(connectivityManager, method, enabled);
	}

	/**
	 * 获取飞行模式开关状态(验证通过) TODO level17及以上没有完成
	 * 
	 * @return
	 */
	public boolean isAirplaneMode() {
		String name = null;
		if (com.tools.os.Build.VERSION.SDK_INT < com.tools.os.Build.VERSION_CODES.Level17) {
			name = Settings.System.AIRPLANE_MODE_ON;
			return (Settings.System.getInt(context.getContentResolver(), name, 0) == 1 ? true : false);
		} else {
			// Settings.Global.AIRPLANE_MODE_ON
			name = AIRPLANE_MODE_ON;
			// Settings.Global.getInt(...)
			return (Settings.System.getInt(context.getContentResolver(), name, 0) == 1 ? true : false);
		}
		//		return (Settings.System.getInt(context.getContentResolver(), name, 0) == 1 ? true : false);
	}

	/**
	 * 设置飞行模式开关(验证通过) TODO level17及以上没有完成
	 * 
	 * @param context
	 * @param enabling
	 */
	public void setAirplaneMode(boolean enabled) {
		String name = null;
		if (com.tools.os.Build.VERSION.SDK_INT < com.tools.os.Build.VERSION_CODES.Level17) {
			name = Settings.System.AIRPLANE_MODE_ON;
			Settings.System.putInt(context.getContentResolver(), name, enabled ? 1 : 0);
		} else {
			// Settings.Global.AIRPLANE_MODE_ON
			name = AIRPLANE_MODE_ON;
			//			Settings.Global.putInt(context.getContentResolver(), name, enabled ? 1 : 0);
			Settings.System.putInt(context.getContentResolver(), name, enabled ? 1 : 0);
		}
		//		Settings.System.putInt(context.getContentResolver(), name, enabled ? 1 : 0);
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", enabled);
		// 发广播
		context.sendBroadcast(intent);
	}
	
	public String toCrashString() {
		if (connectivityManager == null) {
			return "";
		}
		
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return "";
		}
		
		StringBuilder buffer = new StringBuilder();
		buffer.append("getDetailedState:");
		buffer.append( networkInfo.getDetailedState().name() );
		buffer.append("\n");
		buffer.append("getExtraInf:");
		buffer.append( networkInfo.getExtraInfo() );
		buffer.append("\n");
		buffer.append("getReason:");
		buffer.append( networkInfo.getReason() );
		buffer.append("\n");
		buffer.append("getState:");
		buffer.append( networkInfo.getState().name() );
		buffer.append("\n");
		buffer.append("getSubtype:");
		buffer.append( networkInfo.getSubtype() );
		buffer.append("\n");
		buffer.append("getSubtypeName:");
		buffer.append( networkInfo.getSubtypeName() );
		buffer.append("\n");
		buffer.append("getType:");
		buffer.append( networkInfo.getType() );
		buffer.append("\n");
		buffer.append("getTypeName:");
		buffer.append( networkInfo.getTypeName() );
		buffer.append("\n");
		buffer.append("isAvailable:");
		buffer.append( networkInfo.isAvailable() );
		buffer.append("\n");
		buffer.append("isConnected:");
		buffer.append( networkInfo.isConnected() );
		buffer.append("\n");
		buffer.append("isConnectedOrConnecting:");
		buffer.append( networkInfo.isConnectedOrConnecting() );
		buffer.append("\n");
		buffer.append("isFailover:");
		buffer.append( networkInfo.isFailover() );
		buffer.append("\n");
		buffer.append("isRoaming:");
		buffer.append( networkInfo.isRoaming() );
		return buffer.toString();
	}


}
