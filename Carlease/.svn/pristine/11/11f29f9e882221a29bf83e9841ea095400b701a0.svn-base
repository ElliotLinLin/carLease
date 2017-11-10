package com.hst.Carlease.operate;

import android.content.Context;
import android.content.Intent;

import com.hst.Carlease.app.MainApplication;
import com.hst.Carlease.service.PushService;
import com.tools.net.NetworkState;
import com.tools.service.DurableService;
import com.tools.util.Log;


public class PushServiceOperate {

	private static final String TAG = PushServiceOperate.class.getSimpleName();

	/**
	 * 尝试开始
	 * 
	 * @param context
	 */
	public static void tryStart(Context context) {
		Log.e(TAG, "tryStart()");
		// 成功登陆标识
		boolean isLoginSuccesssed = LoginOperate.isLoginSuccesssed();
		Log.e(TAG, "tryStart():isLoginSuccesssed:"+isLoginSuccesssed);

		// 网络是否开启
		boolean isNetworkConnected = false;
		NetworkState state = new NetworkState(context);
		if (state != null) {
			isNetworkConnected = state.isConnected();
		}
		Log.e(TAG, "tryStart():isNetworkConnected:"+isNetworkConnected);

		// 是否启动
		boolean isStartService = true;
		Log.e(TAG, "tryStart():isStartService:"+isStartService);

		// 是否开启服务
		if ( isStartService ) {
			// 真正的启动服务
//			start(context);
			// 在开启
			DurableService durableService = new DurableService(context);
			// 创建一个
			Intent intents = new Intent(MainApplication.getmContext(), PushService.class);
			intents.putExtra(PushService.Key_Command, PushService.Command_Push);

			// 添加
			durableService.add(intents, 2L * 60L * 1000L); // 2分钟
			// durableService.add(intents,3* 1000L); // 2分钟
			// 开始
			durableService.start();
		} else {
			Log.i(TAG, "tryStart()-->不启动");
		}
	}

	/**
	 * 开始
	 * 
	 * @param context
	 */
	public static void start(Context context) {
		Log.i(TAG, "start()-->开始");
		// 发送命令
		PushService.sendCommand(context, PushService.Command_Start);
	}

	/**
	 * 发送
	 * 
	 * @param context
	 */
	public static void send(Context context, byte[] bytes) {
		Log.i(TAG, "send()--->发送");
		// 发送命令
		Intent intent = new Intent();
		intent.putExtra(PushService.Extra_Send, bytes);
		PushService.sendCommand(context, PushService.Command_Send, intent);
	}

	/**
	 * IsInline
	 * 
	 * @param context
	 */
	public static void isInline(Context context) {
		Log.i(TAG, "isInline()--->测试");
		// 发送命令
		PushService.sendCommand(context, PushService.Command_IsInline);
	}

	/**
	 * 发送命令
	 * 
	 * @param context
	 * @param command
	 */
	public static void sendCommand(Context context, int command) {
		// 发送命令
		PushService.sendCommand(context, command);
	}

	/**
	 * 发送命令
	 * 
	 * @param context
	 * @param command
	 * @param intent
	 */
	public static void sendCommand(Context context, int command, Intent intent) {
		// 发送命令
		PushService.sendCommand(context, command, intent);
	}

	/**
	 * 停止
	 * 
	 * @param context
	 */
	public static void stop(Context context) {
		Log.i(TAG, "stop()--->停止");
		// 发送命令
		PushService.stop(context);
	}

}
