package com.tools.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.tools.sqlite.SQLiteSingle;
import com.tools.util.Log;

/**
 * 服务简单框架
 * 
 * startService生命周期：onCreate(一次) -> onStartCommand(多次) -> onStart(多次) -> ... -> onDestroy(一次)
 * 如果被杀掉,重新启动时,会执行onCreate();
 * 
 * 在AndroidManifest.xml文件里加入，三样：
 * 
 * 权限：
 * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * 
 * 广播：
 * 
 *         <receiver
            android:name=".receiver.BootReceiver"
            android:permission="android.permission.RECEIVE_BOOT_COMPLETED" >
            <intent-filter >
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>
        <receiver android:name=".receiver.ConnectionReceiver" >
            <intent-filter >
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
                <action android:name="android.net.wifi.WIFI_STATE_CHANGED" />
            </intent-filter>
        </receiver>

       服务：
        <service
            android:name=".service.PushService"
            android:process=":push" >
            <!-- 多了一个android:priority属性，并且依次减小。这个属性的范围在-1000到1000，数值越大，优先级越高。 -->
            <intent-filter android:priority="1000" >
                <action android:name="com.service.PushService" />
            </intent-filter>
        </service>
 * 
 * 使用例子：
 * 
 *  // 启动
 *  context.startService(new Intent(PushService.Action));
 *  
 *  // 调用并且传参
 *  Intent intent = new Intent(PushService.Action);
	intent.putExtra("cmd", 88);
	context.startService(intent);
 *  
 *  // 在service的onStartCommand()里接受参数值
 *  if (DEBUG) Log.e(TAG, "onStartCommand():cmd:"+intent.getIntExtra("cmd", 0));
 *  
 *  // 停止
 *  context.stopService(new Intent(PushService.Action));
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class SimpleService extends Service {

	private static final String TAG = SimpleService.class.getSimpleName();
	private static final boolean D = false;

	protected Context context = null;

	// 激活名称
	public static final String Action = SimpleService.class.getCanonicalName();

	// 命令列表
	public static final String Key_Command = "Command";
	public static final int Command_Start = 1; // 启动
	public static final int Command_Send = 2; // 发送

	// intent扩展信息
	public static final String Extra_Send = "send";

	// 判断是否正在运行中。。。。。
	private boolean isRunning = false;

	// 消息
	protected static final int Msg_Read_Queue = 1;
	protected static final int Msg_Read_Queue2 = 2;

	public class MyServiceImpl extends IMyService.Stub {

		@Override
		public String getValue() throws RemoteException {
			return "AndroidServiceImpl...";
		}

		@Override
		public int max(int a, int b) throws RemoteException {
			return (a>=b)?a:b;
		}

		//		@Override
		//		public Beauty getBeauty() throws RemoteException {
		//			return null;
		//		}

	}

	@Override
	public IBinder onBind(Intent arg0) {
		if (D) Log.e(TAG, "onBind()");
		// 返回
		return new MyServiceImpl();
	}

	@Override
	public void onCreate() {

		this.context = this.getApplicationContext();

		if (D) Log.e(TAG, "onCreate()");

		// 保证单例数据库是可用的
		if (D) Log.e(TAG, "onCreate():SQLiteSingle.getInstance().isOpen():"+SQLiteSingle.getInstance().isOpen());
		if ( SQLiteSingle.getInstance().isOpen() == false ) {
			SQLiteSingle.getInstance().open();
		}
		if (D) Log.e(TAG, "onCreate():SQLiteSingle.getInstance().isOpen():"+SQLiteSingle.getInstance().isOpen());

		// 启动常驻服务
		DurableService durableService = new DurableService(context);
		// 创建一个
		Intent intent = new Intent(context, SimpleService.class);
		intent.putExtra(SimpleService.Key_Command, SimpleService.Command_Start);
		// 添加
		durableService.add(intent, 1L * 60L * 1000L); // 1分钟
		//		durableService.add(intent, 1L * 10L * 1000L); // 10秒
		// 开始
		durableService.start();

		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (D) Log.e(TAG, "onStartCommand():flags:"+flags+",startId:"+startId);
		if (intent != null) {
			// 一个独特的整数表示此特定请求开始
			// 可以通过onStartCommand有几个
			int cmd = intent.getIntExtra(Key_Command, -1);
			if (D) Log.e(TAG, "onStartCommand():cmd:"+cmd);
			if (D) Log.e(TAG, "onStartCommand():isRunning:"+isRunning);

			if (cmd == Command_Start) {
				if (isRunning == false) {
					isRunning = true;
					startExecute();
				}
			}else if (cmd == Command_Send) {

			}
			if (D) Log.e(TAG, "onStartCommand():isRunning:"+isRunning);
		}
		//		return super.onStartCommand(intent, flags, startId); 
		return Service.START_STICKY;
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

	// 消息
	protected Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Msg_Read_Queue:
				Log.d(TAG, "what == Msg_Read_Queue");
				
				break;
			case Msg_Read_Queue2:
				Log.d(TAG, "what == Msg_Read_Queue2");

				break;
			}
			super.handleMessage(msg);
		}

	};

	/**
	 * 发送消息
	 * 
	 * @param what
	 */
	protected void __sendMessage(int what) {
		if (handler == null) {
			return;
		}
		if (handler.hasMessages(what)) {
			// 如果此消息在消息队列里，则不重复加入消息队列
			Log.e(TAG, "消息队列里已存在:"+what);
			//			Log.status(context, new Throwable(), "消息队列里已存在", "消息ID__what:"+what);
			//			return; // 消息队列是同步的，所以可重复加入。
		}
		handler.sendEmptyMessage( what );
	}

	/**
	 * 发延迟消息
	 * 
	 * @param what
	 * @param delayMillis
	 */
	protected void __sendMessageDelayed(int what, long delayMillis) {
		if (handler == null) {
			return;
		}
		if (handler.hasMessages(what)) {
			// 如果此消息在消息队列里，则不重复加入消息队列
			Log.e(TAG, "消息队列里已存在:"+what);
			//			Log.status(context, new Throwable(), "消息队列里已存在", "消息ID__what:"+what);
			//			return; // 消息队列是同步的，所以可重复加入。
		}
		handler.sendEmptyMessageDelayed(what, delayMillis);
	}
	
	/**
	 * 移除消息
	 * 
	 * @param what
	 */
	protected void clearMessage(int what) {
		if (handler == null) {
			return;
		}
		handler.removeMessages( what );
	}

	/**
	 * 开始执行
	 */
	protected void startExecute() {

	}

	/**
	 * 停止执行
	 */
	protected void stopExecute() {
		clearMessage( Msg_Read_Queue );
		clearMessage( Msg_Read_Queue2 );
		
	}

	/**
	 * 启动
	 */
	public static void start(Context context) {
		if (D) Log.e(TAG, "start()");
		Intent intent = new Intent(Action);
		context.startService(intent);
	}

	/**
	 * 停止
	 */
	public static void stop(Context context) {
		if (D) Log.e(TAG, "stop()");
		context.stopService(new Intent(Action));
	}

	/**
	 * 发送命令
	 */
	public static void sendCommand(Context context, int command, Intent intent) {
		if (D) Log.e(TAG, "sendCommand:"+command);
		if (intent == null) {
			intent = new Intent(Action);
		} else {
			intent.setAction(Action);
		}

		intent.putExtra(Key_Command, command);
		context.startService(intent);
	}

	/**
	 * 发送命令
	 */
	public static void sendCommand(Context context, int command) {
		sendCommand(context, command, null);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (D) Log.e(TAG, "onStart()");
		super.onStart(intent, startId);
	}

	@Override
	public void onRebind(Intent intent) {
		if (D) Log.e(TAG, "onRebind()");
		super.onRebind(intent);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		if (D) Log.e(TAG, "onUnbind()");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		if (D) Log.e(TAG, "onDestroy()");

		// +++ 停止服务时，停止常驻服务，为了省电。
		// 停止常驻服务
		DurableService durableService = new DurableService(context);
		// 创建一个
		Intent intent = new Intent(context, SimpleService.class);
		intent.putExtra(SimpleService.Key_Command, SimpleService.Command_Start);
		// 停止
		durableService.cancel(intent);

		// 停止执行
		stopExecute();

		// 停止sqlite
		SQLiteSingle.getInstance().close();

		isRunning = false;

		super.onDestroy();
	}

	@Override
	protected void finalize() throws Throwable {
		if (D) Log.e(TAG, "finalize()");
		super.finalize();
	}

}
