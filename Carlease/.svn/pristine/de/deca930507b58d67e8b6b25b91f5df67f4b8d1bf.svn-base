package com.hst.Carlease.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hst.Carlease.operate.PushServiceOperate;
import com.tools.util.Log;


/**
 * 开机广播
 * 
 * 注：
 * ConnectionReceiver网络广播，在开机时，不一定会有，所以要加入启动广播。
 * 
 * 在AndroidManifest.xml加入权限：
 * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 * 
        <receiver
            android:name=".receiver.BootReceiver"
            android:permission="android.permission.RECEIVE_BOOT_COMPLETED" >
            <intent-filter >
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = BootReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			// 开启服务
			Log.i(TAG, "BootReceiver():onReceive()-->startService()");
			// 尝试开始
			PushServiceOperate.tryStart(context);
			//启动常驻状态栏
			
		}
	}
	
}