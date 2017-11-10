package com.tools.push;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PowerManager;
import android.util.Pair;

import com.hst.Carlease.service.PushService;
import com.tools.app.AppInfo;
import com.tools.app.Config;
import com.tools.content.pm.PermissionTool;
import com.tools.thread.AbsThread;
import com.tools.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 推送
 * 
 * TODO 经过几天的测试，还发现几个错误。 1）约每15分钟，socket.read()就会产生 java.net.SocketException:
 * The connection was reset
 * 2）socket.sendUrgentData(0xFB);发紧急数据包，服务端有时收到0xFF，有时收不到。
 * 3）有少许情况服务端没有收到手机端发的心跳包，而send()count>0和isInline()为true。
 * 4）handler收到消息严重延迟，将近27分钟，并且之间也没有startPrintTimerTask()的log 2014-08-15 18:38:18
 * E/PushSocket(24033): sendMessage() 2014-08-15 19:25:10 E/PushSocket(24033):
 * message == Msg_Socket_Error. 解决方案：（已验证，此方法能解决） 休眠
 * http://blog.csdn.net/dreamer0924/article/details/7705144 5)
 * readThread线程被暂停过9分钟，也有可能是整个应用被暂停，因startPrintTimerTask()没log 2014-08-15
 * 22:41:15 E/PushSocket(24033): 但是readCount<0 2014-08-15 22:50:00
 * E/PushSocket(24033): isInline() 解决方案：（已验证，此方法能解决） 休眠
 * http://blog.csdn.net/dreamer0924/article/details/7705144
 * 
 * TODO 测试结果： 条件socket.setSoTimeout( 0 ); && socket.setKeepAlive( true );
 * 每隔32分钟就会产生The connection was reset，原因不明。 加入了保持CPU运行功能，很耗电。还有，之前做了一些优化，
 * 就是无网络时，就停止服务，这是正确的做法。 解决方案（还没实践）： 1）试试socket.setKeepAlive( false );
 * 2）给socket.setSoTimeout( time )设置时间，不要为0，0是一直等待，直到有数据。
 * 
 * Socket详解 http://www.cnblogs.com/jerrychoi/archive/2010/04/15/1712931.html
 * 
 * 中文Java SDK http://www.cjsdn.net/Doc/JDK50/java/net/Socket.html
 * 
 * 超时 http://blog.csdn.net/sureyonder/article/details/5633647
 * 
 * TODO 不能采用Thread.sleep()做定时任务， 建议采用 java.util.Timer / java.util.TimerTask
 * 
 * 问：是否可以采用Thread.sleep()在后台做定时任务？ 答：不行。
 * 
 * 问：是否可以采用handler.postDelayed(...)在后台做定时任务？ 答：可以的。（不行，因CPU会休眠）
 * 
 * 问：是否可以采用java.util.Timer/java.util.TimerTask在后台做定时任务？ 答：可以的。（不行，因CPU会休眠）
 * 
 * 问：5分钟内没有心跳，则服务端会主动断开SOCKET，如果不发心跳数据，采用inLine替代心跳会如何？
 * 答：使用inLine，5分钟内还是会断开SOCKET，说明inLine替代不了心跳。
 * 并且服务端主动断开，手机端收到的异常是java.net.SocketException: The connection was reset
 * 
 * TODO 还有一个问题要解决 readByteBufferAll()里的read()能读到发出去的数据
 * 
 * 联通网络运营商，如果没有心跳，则5分钟会重置，抛出下述异常 java.net.SocketException: The connection was
 * reset
 * 
 * TODO 问题： 1）4.0系统 WF或者本网络的问题，每一分钟会产生，有时候3分钟 java.net.SocketException: recvfrom
 * failed: ETIMEDOUT (Connection timed out) 答：找了其它地方的WF网络，测试了30分钟左右，没有出现上述情况
 * ---------------------------- 初步判断是某些WF网络会有此问题。 2）联通3G网络 2.1系统 外网推送服务器
 * 设置socket.setKeepAlive( false ); 每10分钟会产生 java.net.SocketException: The
 * connection was reset 设置成socket.setKeepAlive( true ); 还是一样，每10分钟会产生异常The
 * connection was reset 每2分钟产生心跳还是一样会was reset
 * 
 * 测试一：从无线变成有线 2.2系统模拟器 有线网络 外网推送服务器，运行10分钟看看还有没有The connection was reset
 * 结果：还是每10分钟就会断一次，不过错误不是The connection was reset， 而是java.net.SocketException:
 * Broken pipe
 * 
 * 测试二：修改socket.setSoTimeout( 0 )设置为0，一直等待，直到有数据才返回。
 * ---------------------------- 测试通过，不过有时候还是会出现，只是时间不会每次都是10分钟。 2.1系统真机 3G无线网络
 * 外网推送服务器。 结果：第一次测试20分钟后就出现一次The connection was reset，
 * 而第二次30分钟了，长连接SOCKET还存活，没出现The connection was reset
 * 
 * 测试三：连接放在一个线程里，读和心跳放在另一个线程里 2.1系统真机 3G无线网络 外网推送服务器。
 * 
 * 测试四：连接、读、心跳都在一个线程里 2.1系统真机 3G无线网络 外网推送服务器。
 * 
 * 3）偶现 --- Connection reset by peer 导致连接重置 java.net.SocketException: recvfrom
 * failed: ECONNRESET (Connection reset by peer)
 * 网上解决方案：以上是我的猜想。当我及时干掉老线程的时候，发现这个错误就没有了。 Connection reset by peer原因分析
 * http://blog.csdn.net/shaovey/article/details/4678717
 * http://blog.csdn.net/smilingleo/article/details/133904
 * 
 * socket的读写是阻塞的，soTimeout是socket读写超时，而不是链接超时。 connection
 * reset是由于在一个半关闭的connection上读取数据导致，client已经关闭但是server端没有关闭。
 * http://xiaoz5919.iteye.com/blog/1685138
 * 
 * @author LMC
 */
public class PushSocket {

	private static final String TAG = PushSocket.class.getSimpleName();

	private static final boolean D = false;

	protected Socket socket = null;

	// 缓冲区最大长度
	// public static final int BUFFER_MAX_LEN = 1024;
	public static final int BUFFER_MAX_LEN = 128;

	// 第一次连接线程
	// protected AbsThread firstConnectThread = null;
	// 重连线程
	// protected AbsThread reconnectThread = null;
	// 读线程
	protected AbsThread readThread = null;
	// 心跳线程
	// protected AbsThread heartbeatThread = null;

	protected HandlerThread connectHandlerThread = null;

	// 读到有效的数据时，SLEEP一会
	private static final long scoket_read_available_sleep = 200L;

	// 让CPU保持运行状态
	protected PowerManager powerManager = null;
	protected PowerManager.WakeLock wakeLock = null;

	/*
	 * TODO 重连要队列模式，执行完第一个，等待10S，才到第二个，如：HandlerThread或者Handler 心跳要定时任务 打印要定时任务
	 * 读Socket要Thread
	 */

	// 消息
	protected static final int Msg_Connect = 1; // 正在连接
	protected static final int Msg_Heartbeat = 2; // 心跳
	protected static final int Msg_Socket_Read = 3; // 读Socket
	protected static final int Msg_Socket_Error = 4; // Socket错误
	protected static final int Msg_Socket_Close = 5; // 客户端主动关闭

	protected static final int Msg_Back_Runing = 6; // 测试，看看handler的消息是否长时间运行

	// 错误
	public static final int Error_Socket_Error = 1; // 网络出现问题
	public static final int Error_Socket_Read_Timeout = 2; // Socket读超时

	protected byte[] byteBuffer = new byte[BUFFER_MAX_LEN];

	// 已连接状态，跟Socket无关
	protected boolean isConnected = false;

	// 共计连接成功次数
	protected int connectedCount = 0;
	// 成功连接后，共计发送了多少次心跳
	protected int heartbeatCount = 0;

	// test
	protected int backCount = 0;
	protected long backTime = 13L * 1000L;

	// 心跳定时任务(必须为守护进程运行线程)
	protected Timer timerHeartbeat = null;

	// 打印定时任务(必须为守护进程运行线程)
	protected Timer timerPrint = null;

	// 事件接口
	public interface IEventListener {
		void onConnected(); // 连接成功

		void onReceived(int count, byte[] bytes); // 接收到数据包

		void onHeartbeat(); // 心跳

		void onError(int error); // 发生错误

		void onClosed(); // 客户端主动关闭 或者 服务器主动关闭
	}

	protected IEventListener eventListener = null;

	protected Context context = null;

	/*
	 * 版本 3.0.6 == ok
	 */
	private String version = "3.0.6";

	// 配置
	protected PushSocketConfig pushSocketConfig = null;

	private android.os.Handler handler = null;

	public PushSocket(Context context, PushSocketConfig pushSocketConfig) {
		init(context, pushSocketConfig);
	}

	/**
	 * 初始化
	 */
	private void init(Context context, PushSocketConfig pushSocketConfig) {

		setPushSocketConfig(pushSocketConfig);

		this.context = context;
		this.isConnected = false;
		this.connectedCount = 0;
		this.heartbeatCount = 0;
		Log.e(TAG, "version:" + version);
		alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// WAKE_LOCK && DEVICE_POWER
		PermissionTool.checkThrow(context,
				android.Manifest.permission.WAKE_LOCK);
		// PermissionTool.checkThrow(context,
		// android.Manifest.permission.DEVICE_POWER); // 这是系统的权限，所以不要。

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

	/**
	 * 发送消息
	 * 
	 * @param what
	 */
	private void sendMessage(int what) {
		if (D)
			Log.e(TAG, "sendMessage():what:" + what);
		if (handler == null) {
			return;
		}
		handler.sendEmptyMessage(what);
	}

	/**
	 * 发送延迟消息
	 * 
	 * @param what
	 */
	private void sendMessageDelayed(int what, long delayMillis) {
		if (D)
			Log.e(TAG, "sendMessageDelayed():what:" + what + ",delayMillis:"
					+ delayMillis);
		if (handler == null) {
			return;
		}
		Message msg = handler.obtainMessage(what);
		handler.sendMessageDelayed(msg, delayMillis);
	}

	/**
	 * 移除消息
	 * 
	 * @param what
	 */
	private void removeMessage(int what) {
		if (D)
			Log.e(TAG, "removeMessage():what:" + what);
		if (handler == null) {
			return;
		}
		handler.removeMessages(what);
	}

	public PushSocketConfig getPushSocketConfig() {
		return this.pushSocketConfig;
	}

	public void setPushSocketConfig(PushSocketConfig pushSocketConfig) {
		this.pushSocketConfig = pushSocketConfig;
	}

	/**
	 * 得到Scoket实例
	 * 
	 * @return
	 */
	public Socket getSocket() {
		return this.socket;
	}

	/**
	 * 设置Socket属性
	 */
	private void setSocket(Socket socket) {
		if (D)
			Log.e(TAG, "setSocket()");
		if (socket == null) {
			return;
		}

		// 设置超时
		// Sets this socket's read timeout in milliseconds.
		try {
			// 当通过Socket的输入流读数据时，如果还没有数据，就会等待。
			// Socket类的SO_TIMEOUT选项用于设定接收数据的等待超时时间，单位为毫秒，它的默认值为0，表示会无限等待，永远不会超时。
			// Socket的setSoTimeout()方法必须在接收数据之前执行才有效。
			// 此外，当输入流的read()方法抛出SocketTimeoutException后，Socket仍然是连接的，可以尝试再次读取数据。

			// 一定要设置为0，否则每10分钟就会出现java.net.SocketException: The connection was
			// reset
			// socket.setSoTimeout( 0 );
			socket.setSoTimeout(getPushSocketConfig().getReadTimeout()); // 这个读超时是有效的，已检证通过

			// 当SO_KEEPALIVE选项为true，表示底层的TCP实现会监视该连接是否有效。
			// SO_KEEPALIVE选项的默认值为false，表示TCP不会监视连接是否有效，不活动的客户端可能会永久存在下去，而不会注意到服务器已经崩溃。
			// socket.setKeepAlive( true );
			socket.setKeepAlive(false);

			/*
			 * TODO 有时会出现 java.net.SocketException: recvfrom failed: ECONNRESET
			 * (Connection reset by peer) Google给出一个解决方案 if
			 * (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO)
			 * { System.setProperty("http.keepAlive", "false"); }} 所以这里尝试一下
			 * socket.setKeepAlive(false);
			 */

			// TCP_NODEALY的默认值为false，表示采用Negale算法。
			// 如果调用setTcpNoDelay(true)方法，就会关闭Socket的缓冲，确保数据及时发送：
			// socket关闭时，立即释放资源
			socket.setTcpNoDelay(true);

			// 执行Socket的close方法，该方法不会立即返回，而进入阻塞状态，同时，底层的Socket也会尝试发送剩余的数据
			// 当底层的Socket已经发送完所有的剩余数据或者close()方法的阻塞时间超过3秒，也会返回。
			// socket.setSoLinger(true, 0);
			socket.setSoLinger(true, 3); // TODO 尝试设置时间，看看是否能解决Connection reset
											// by peer ，还是解决不了

			// IP规定了四种服务类型，用来定性的描述服务的质量
			socket.setTrafficClass(0x04 | 0x10); // 高可靠性和最小延迟传输
			/*
			 * l 低成本：0x02 （二进制的倒数第二位为1） l 高可靠性：0x04（二进制的倒数第三位为1） l
			 * 最高吞吐量：0x08（二进制的倒数第四位为1） l 最小延迟：0x10（二进制的倒数第五位为1）
			 */

			// connectionTime 表示用最少时间建立连接
			// latency 表示最小延迟
			// bandwidth 表示最高带宽
			// 就表示最高带宽最重要，其次是最少连接时间，最后是最小延迟。
			socket.setPerformancePreferences(1, 2, 3);

			// 打开OOB选项 TODO 还不知道是true还是false才能使用sendUrgentData()
			socket.setOOBInline(false); // 默认是false

		} catch (SocketException e) {
			e.printStackTrace();
		}

		printSocket(socket);

	}

	/**
	 * 启动重连机制
	 */
	// protected void startReconnect() {
	// if (DEBUG) Log.e(TAG, "startReconnect()");
	//
	// reconnectThread = new AbsThread(context) {
	//
	// @Override
	// public void run() {
	//
	// if (DEBUG) Log.e(TAG, "startReconnect()...开始while...");
	//
	// while( super.isCanceled() == false ) {
	//
	// // if (DEBUG) Log.e(TAG, "startReconnect()...while入口...");
	//
	// if (isConnected() == false) {
	// // 连接
	// connect();
	// if (isConnected()) {
	//
	// // 成功连接时，heartbeatCount清零
	// heartbeatCount = 0;
	// // 共计连接成功次数
	// connectedCount++;
	//
	// // 设置Socket属性
	// setSocket(socket);
	// // 连接事件接口
	// if (eventListener != null) {
	// eventListener.onConnected();
	// }
	//
	// // 开始心跳任务
	// startHeartbeatTask();
	//
	// // 发消息，启动socket读线程
	// sendMessage(Msg_Socket_Read);
	// }
	// }
	//
	// if (DEBUG) {
	// Log.e(TAG, "连接成功次数:"+connectedCount+",心跳次数:"+heartbeatCount);
	// }
	//
	// // if (DEBUG) Log.e(TAG, "startReconnect():isConnected:"+isConnected());
	//
	// // if (DEBUG) {
	// // // 打印网络状态
	// // NetworkState networkStateCC = new NetworkState(context);
	// // networkStateCC.print( networkStateCC.getActiveNetworkInfo() );
	// // }
	//
	// if ( sleepExt( getPushSocketConfig().getReconnectTime() ) == false ) {
	// break;
	// }
	//
	// // if (DEBUG) Log.e(TAG, "startReconnect()...while出口...");
	//
	// } // end while
	//
	// if (DEBUG) Log.e(TAG, "startReconnect()...结束while...");
	//
	// }
	//
	// };
	//
	// reconnectThread.start();
	// }

	/**
	 * 停止重连机制
	 */
	// protected void stopReconnect() {
	// if (DEBUG) Log.e(TAG, "stopReconnect()");
	// if(this.reconnectThread != null) {
	// reconnectThread.cancel();
	// }
	// }

	/**
	 * 开始第一次连接线程
	 */
	// protected void startFirstConnectTask() {
	//
	// firstConnectThread = new AbsThread(context) {
	//
	// @Override
	// public void run() {
	//
	// /**
	// * 执行连接任务
	// */
	// executeConnect();
	//
	// // 等待第一次连接执行完毕，不管是否连接成功，都要 - 开始重连定时任务
	//
	// /**
	// * 开始重连定时任务
	// */
	// startReconnectTimerTask();
	//
	// } // end run
	//
	// };
	//
	// firstConnectThread.start();
	// }

	/**
	 * 停止第一次连接线程
	 */
	// protected void stopFirstConnectTask() {
	// if (firstConnectThread != null) {
	// firstConnectThread.cancel();
	// }
	// }

	/**
	 * 开始重连定时任务
	 */
	// protected void startReconnectTimerTask() {
	// if (DEBUG) Log.e(TAG, "startReconnectTimerTask()");
	//
	// // stopHeartbeatTask(); 还没有用，则不能停止
	//
	// if (timerReconnect == null) {
	// return;
	// }
	//
	// try {
	// timerReconnect.schedule(new TimerTask() {
	//
	// @Override
	// public void run() {
	// // 执行连接任务
	// executeConnect();
	// }
	//
	// }, 0, getPushSocketConfig().getReconnectTime());
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 开始连接
	 */
	private void startConnect() {
		if (D)
			Log.e(TAG, "startConnect()");
		// 发消息
		sendMessage(Msg_Connect);
	}

	/**
	 * 执行连接任务
	 */
	private void executeConnect() {
		if (D)
			Log.e(TAG, "executeConnect()");

		if (isConnected() == false) {
			// 连接
			if (connect()) {
				if (D)
					Log.e(TAG, "连接成功.");
				// 成功连接时，heartbeatCount清零
				heartbeatCount = 0;
				// 共计连接成功次数
				connectedCount++;

				// 设置Socket属性
				setSocket(socket);
				// 连接事件接口
				if (eventListener != null) {
					eventListener.onConnected();
				}

				/**
				 * 开始心跳定时任务
				 */
				startHeartbeatTimerTask();

				// 发消息，启动socket读线程
				sendMessage(Msg_Socket_Read);
			} else {
				if (D)
					Log.e(TAG, "连接失败.");
			}
		} else {

		}

	}

	/**
	 * 停止连接
	 */
	private void stopConnect() {
		if (D)
			Log.e(TAG, "stopConnect()");
		// 移除消息
		removeMessage(Msg_Connect);
	}

	/**
	 * 停止重连定时任务
	 */
	// protected void stopReconnectTimerTask() {
	// if (DEBUG) Log.e(TAG, "stopReconnectTimerTask()");
	// if (timerReconnect != null) {
	// timerReconnect.cancel();
	// }
	// }
	private AlarmManager alarmManager;

	private void startHearbeatAlarmManger() {
		if (alarmManager == null) {
			return;
		}
		Intent intent = new Intent(context, PushService.class);

		// 会替代旧的
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		long nowTimeMillis = System.currentTimeMillis();
		alarmManager.setRepeating(AlarmManager.RTC, nowTimeMillis,
				1 * 60 * 1000, pendingIntent);
	}

	/**
	 * 停止心跳定时任务
	 */
	private void stopHearbeatAlarmManger(Intent intent) {
		if (alarmManager == null) {
			return;
		}

		if (intent == null) {
			return;
		}
		if (D)
		Log.d(TAG, "cancel():intent.getAction():" + intent.getAction());
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_NO_CREATE);
		if (pendingIntent != null) {
			if (D)
			Log.d(TAG, "cancel():pendingIntent != null");
			// 取消正在执行的服务
			pendingIntent.cancel();
			alarmManager.cancel(pendingIntent);
		} else {
			if (D)
			Log.d(TAG, "cancel():pendingIntent == null");
		}
	}

	/**
	 * 开始心跳定时任务
	 */
	private void startHeartbeatTimerTask() {

		// startHearbeatAlarmManger();
		// executeHeartbeat();

		// stopHeartbeatTask(); 还没有用，则不能停止

		// 已有，则停止
		stopHeartbeatTimerTask();

		if (D)
			Log.e(TAG, "startHeartbeatTimerTask()");

		// 在Java中有两类线程：用户线程 (User Thread)、守护线程 (Daemon Thread)。
		// 主线程退出了，守护线程也跟着退出，而用户线程不会退出。
		// 有一个现象，APP里创建并且运行一个线程，当APP退出了，线程还在运行中，
		// 说明此线程是用户线程。

		// 没有，则创建
		timerHeartbeat = new Timer("push_socket_heartbeat", true);

		// 如果第二个参数为0，则会马上执行一次
		// 在delay秒之后,绝对每隔period秒执行TimerTask的任务一次
		long delay = getPushSocketConfig().getHeartbeatTime();
		long period = getPushSocketConfig().getHeartbeatTime();

		try {
			timerHeartbeat.schedule(new TimerTask() {

				@Override
				public void run() {
					// 执行心跳任务
					executeHeartbeat();
				}

			}, delay, period);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 执行心跳任务
	 */
	private void executeHeartbeat() {
		if (D)
			Log.e(TAG, "executeHeartbeat()");
		if (isConnected() && eventListener != null) {
			eventListener.onHeartbeat();
			heartbeatCount++;
		}
		// 发消息
		sendMessage(Msg_Heartbeat);
	}

	/**
	 * 停止心跳定时任务
	 */
	private void stopHeartbeatTimerTask() {
		if (D)
			Log.e(TAG, "stopHeartbeatTimerTask()");
		if (timerHeartbeat != null) {
			timerHeartbeat.cancel();
		}
		heartbeatCount = 0;
	}

	/**
	 * 开始打印定时任务
	 */
	private void startPrintTimerTask() {
		// stopHeartbeatTask(); 还没有用，则不能停止

		// 已有，则停止
		stopPrintTimerTask();

		if (D)
			Log.e(TAG, "startPrintTimerTask()");

		// 在Java中有两类线程：用户线程 (User Thread)、守护线程 (Daemon Thread)。
		// 主线程退出了，守护线程也跟着退出，而用户线程不会退出。
		// 有一个现象，APP里创建并且运行一个线程，当APP退出了，线程还在运行中，
		// 说明此线程是用户线程。

		// 没有，则创建
		timerPrint = new Timer("push_socket_print", true);

		// 如果第二个参数为0，则会马上执行一次
		// 在delay秒之后,绝对每隔period秒执行TimerTask的任务一次
		long delay = 10L * 1000L; // 10s
		long period = 10L * 1000L; // 10s

		try {
			timerPrint.schedule(new TimerTask() {

				@Override
				public void run() {
					// 执行打印任务
					executePrint();
				}

			}, delay, period);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行打印任务
	 */
	private void executePrint() {
		if (D)
			Log.e(TAG, "executePrint()");
		if (D) {
			String error = String.format("连接状态:" + isConnected() + ",连接成功次数:"
					+ connectedCount + ",心跳次数:" + heartbeatCount);
			Log.e(TAG, error);
		}
	}

	/**
	 * 停止打印定时任务
	 */
	private void stopPrintTimerTask() {
		if (D)
			Log.e(TAG, "stopPrintTimerTask()");
		if (timerPrint != null) {
			timerPrint.cancel();
		}
	}

	/**
	 * 启动心跳机制
	 */
	// protected void startHeartbeat() {
	// if (DEBUG) Log.e(TAG, "startHeartbeat()");
	//
	// heartbeatThread = new AbsThread(context) {
	//
	// @Override
	// public void run() {
	//
	// if (DEBUG) Log.e(TAG, "startHeartbeat()...开始while...");
	//
	// while (super.isCanceled() == false) {
	//
	// if (DEBUG) Log.e(TAG, "startHeartbeat()...入口while...");
	//
	// if ( sleepExt( getPushSocketConfig().getHeartbeatTime() ) == false ) {
	// break;
	// }
	//
	// if (isConnected() && eventListener != null) {
	// if (DEBUG) Log.e(TAG, "startHeartbeat():isConnected()"+isConnected());
	// eventListener.onHeartbeat();
	// heartbeatCount++;
	// }
	//
	// if (DEBUG) Log.e(TAG, "startHeartbeat()...出口while...");
	//
	// } // end while
	//
	// if (DEBUG) Log.e(TAG, "startHeartbeat()...结束while...");
	//
	// } // end run
	//
	// };
	//
	// heartbeatThread.start();
	// }

	/**
	 * 停止心跳机制
	 */
	// protected void stopHeartbeat() {
	// if (DEBUG) Log.e(TAG, "stopHeartbeat()");
	// if (heartbeatThread != null) {
	// heartbeatThread.cancel();
	// }
	// }

	/**
	 * 开始读数据线程(读全部)
	 */
	private void startReadTask() {
		if (D)
			Log.e(TAG, "startReadTask()");

		readThread = new AbsThread() {

			@Override
			public void run() {

				if (D)
					Log.e(TAG, "...start...startReadTask()...");

				while (readThread.isCanceled() == false && isConnected()) {

					if (D)
						Log.e(TAG, "...start...startReadTask()...while...");

					int readCount = -1;

					PacketInfo resultData = null;

					try {
						/*
						 * 如果输入缓冲队列RecvQ中没有数据，read操作会一直阻塞而挂起线程，
						 * 直到有新的数据到来或者有异常产生。调用setSoTimeout(int timeout)
						 * 可以设置超时时间，
						 * 如果到了超时时间仍没有数据，read会抛出一个SocketTimeoutException，
						 * 程序需要捕获这个异常，但是当前的socket连接仍然是有效的。
						 * 如果对方进程崩溃、对方机器突然重启、网络断开，本端的read会一直阻塞下去，这时设置超时时间是非常重要的，
						 * 否则调用read的线程会一直挂起。
						 */
						// readCount = socket.getInputStream().read(byteBuffer);
						// // 读单次

						resultData = readByteBufferAll(socket);

						// Pair<Integer, java.nio.ByteBuffer> pair =
						// readByteBufferAll(socket);
						// if (pair != null) {
						// readCount = pair.first;
						// if (DEBUG) Log.e(TAG, "pair.first:"+pair.first);
						// if (DEBUG) Log.e(TAG,
						// "pair.first readCount:"+readCount);

						// 读不到，则为-1 ---- 如果readCount == -1，说明已经跟服务器断开了。
						// if (readCount <= -1) {
						// // 关闭读线程startReadTask()
						// if (DEBUG) Log.e(TAG, "关闭读线程startReadTask()");
						// // 网络出现问题，发消息
						// sendMessage( Msg_Network_Error );
						// }

					} catch (SocketTimeoutException e) {
						if (D)
							Log.e(TAG,
									"读...发生了socket超时SocketTimeoutException...");
						e.printStackTrace();
						// set count == 0
						readCount = 0;
						// 调接口
						if (eventListener != null) {
							eventListener.onError(Error_Socket_Read_Timeout);
						}
						if (D)
							Log.e(TAG, "isInline():" + isInline());
					} catch (InterruptedException e) {
						e.printStackTrace();
						// 线程发生了中断
						readCount = 0;
						if (D)
							Log.e(TAG, "读...发生了中断异常...interrupt()");
					} catch (Exception e) {
						if (D)
							Log.e(TAG, "读...发生了其它异常...");
						e.printStackTrace();

						/*
						 * TODO 在WF网络下，每一分钟总是发生 java.net.SocketException:
						 * recvfrom failed: ETIMEDOUT (Connection timed out)
						 * 而在3G网络下没有此异常。 初步判断是本网络或者WF的问题。 --- 已找到答案，是因为网络真的断开了。
						 */

						readCount = -1;

						if (D)
							Log.e(TAG, "isInline():" + isInline());
						// if (D) Log.e(TAG, "isInline2():"+isInline2());
						// byte[] testB = new byte[3];
						// testB[0] = 0x01;
						// testB[1] = 0x02;
						// testB[2] = 0x03;
						// int c = send(testB);
						// if (D) Log.e(TAG, "发生了其它异常，看看是否能send():count:"+c);
						if (D)
						Log.printException(TAG, e);
						if (e != null) {
							if (D)
							Log.printThrowable(TAG, e.getCause());
						}

						// 状态日志
						if (e != null) {
							// Log.status(context, new Throwable(),
							// "socket出现异常:" + e.getMessage());
						}
					}

					if (resultData != null) {
						readCount = resultData.getCount();
						if (readCount > 0 && eventListener != null) {
							if (D)
								Log.e(TAG,
										"execute...........onReceived()--------- start ----------");
							eventListener.onReceived(readCount,
									resultData.getBytes());
							if (D)
								Log.e(TAG,
										"execute...........onReceived()--------- end ----------");
						}
					}

					if (D)
						Log.e(TAG, "startReadTask()....readCount:" + readCount);

					if (readCount <= -1) {
						if (D)
							Log.e(TAG, "但是readCount<0");
						if (isInline() == false) {
							// 网络出现问题，发消息
							sendMessage(Msg_Socket_Error);
						}
					}

					if (D)
						Log.e(TAG, "...end...startReadTask()...while...");
				} // end while
					// super.run();

				if (D)
					Log.e(TAG, "...end...startReadTask()...");
			}

		};

		readThread.start();
	}

	/**
	 * 停止读线程 TODO 停止线程后，里面socket不会产生中断异常。
	 */
	private void stopReadTask() {
		if (D)
			Log.e(TAG, "stopReadThread()");
		if (readThread != null) {
			readThread.cancel();
		}
	}

	/**
	 * 开始读数据线程(读全部)
	 */
	private void __tmp__startReadTask() {
		if (D)
			Log.e(TAG, "startReadTask()");

		readThread = new AbsThread() {

			@Override
			public void run() {

				if (D)
					Log.e(TAG, "startReadTask()...开始while...");

				while (readThread.isCanceled() == false && isConnected()) {

					if (D)
						Log.e(TAG, "startReadTask()...入口while...");

					int readCount = -1;

					PacketInfo resultData = null;

					try {
						/*
						 * 如果输入缓冲队列RecvQ中没有数据，read操作会一直阻塞而挂起线程，
						 * 直到有新的数据到来或者有异常产生。调用setSoTimeout(int timeout)
						 * 可以设置超时时间，
						 * 如果到了超时时间仍没有数据，read会抛出一个SocketTimeoutException，
						 * 程序需要捕获这个异常，但是当前的socket连接仍然是有效的。
						 * 如果对方进程崩溃、对方机器突然重启、网络断开，本端的read会一直阻塞下去，这时设置超时时间是非常重要的，
						 * 否则调用read的线程会一直挂起。
						 */
						// readCount = socket.getInputStream().read(byteBuffer);
						// // 读单次

						if (D)
							Log.e(TAG, "... 开始 readByteBufferAll ....");
						resultData = readByteBufferAll(socket);

						// Pair<Integer, java.nio.ByteBuffer> pair =
						// readByteBufferAll(socket);
						// if (pair != null) {
						// readCount = pair.first;
						// if (DEBUG) Log.e(TAG, "pair.first:"+pair.first);
						// if (DEBUG) Log.e(TAG,
						// "pair.first readCount:"+readCount);

						// 读不到，则为-1 ---- 如果readCount == -1，说明已经跟服务器断开了。
						// if (readCount <= -1) {
						// // 关闭读线程startReadTask()
						// if (DEBUG) Log.e(TAG, "关闭读线程startReadTask()");
						// // 网络出现问题，发消息
						// sendMessage( Msg_Network_Error );
						// }

					} catch (SocketTimeoutException e) {
						if (D)
							Log.e(TAG, "读......发生了超时......");
						// set count == 0
						readCount = 0;
						// 调接口
						if (eventListener != null) {
							eventListener.onError(Error_Socket_Read_Timeout);
						}
						e.printStackTrace();
						if (D)
							Log.e(TAG, "isInline():" + isInline());

						// Log.e(TAG, "PushSocket.java 发送心跳...start.....");
						// __test__sendHeartbeat();

					} catch (Exception e) {
						if (D)
							Log.e(TAG, "读......发生了其它异常......");

						/*
						 * TODO 在WF网络下，每一分钟总是发生 java.net.SocketException:
						 * recvfrom failed: ETIMEDOUT (Connection timed out)
						 * 而在3G网络下没有此异常。 初步判断是本网络或者WF的问题。
						 */

						readCount = -1;
						e.printStackTrace();
						if (D)
							Log.e(TAG, "isInline():" + isInline());

						Log.printException(TAG, e);
						if (e != null) {
							Log.printThrowable(TAG, e.getCause());
						}

						// 状态日志
						if (e != null) {
							// Log.status(context, new Throwable(),
							// "socket出现异常:" + e.getMessage());
						}

					}

					if (resultData != null) {
						readCount = resultData.getCount();
						if (readCount > 0 && eventListener != null) {
							if (D)
								Log.e(TAG,
										"execute...........onReceived()--------- start ----------");
							eventListener.onReceived(readCount,
									resultData.getBytes());
							if (D)
								Log.e(TAG,
										"execute...........onReceived()--------- end ----------");
						}
					}

					if (D)
						Log.e(TAG, "startReadTask()....readCount:" + readCount);

					if (readCount <= -1) {
						if (D)
							Log.e(TAG, "但是readCount<0");
						if (isInline() == false) {
							// 网络出现问题，发消息
							sendMessage(Msg_Socket_Error);
						}
					}

					if (D)
						Log.e(TAG, "startReadTask()...出口while...");
				}
				// super.run();
				if (D)
					Log.e(TAG, "startReadTask()...结束while...");
			}

		};
		//
		readThread.start();
	}

	/**
	 * 启动心跳机制
	 */
	// protected void startHeartbeat() {
	// if (DEBUG) Log.e(TAG, "startHeartbeat()");
	//
	// heartbeatThread = new AbsThread(context) {
	//
	// @Override
	// public void run() {
	//
	// if (DEBUG) Log.e(TAG, "startHeartbeat()...开始while...");
	//
	// while (super.isCanceled() == false) {
	//
	// if (DEBUG) Log.e(TAG, "startHeartbeat()...入口while...");
	//
	// if ( sleepExt( getPushSocketConfig().getHeartbeatTime() ) == false ) {
	// break;
	// }
	//
	// if (isConnected() && eventListener != null) {
	// if (DEBUG) Log.e(TAG, "startHeartbeat():isConnected()"+isConnected());
	// eventListener.onHeartbeat();
	// heartbeatCount++;
	// }
	//
	// if (DEBUG) Log.e(TAG, "startHeartbeat()...出口while...");
	//
	// } // end while
	//
	// if (DEBUG) Log.e(TAG, "startHeartbeat()...结束while...");
	//
	// } // end run
	//
	// };
	//
	// heartbeatThread.start();
	// }

	/**
	 * 停止心跳机制
	 */
	// protected void stopHeartbeat() {
	// if (DEBUG) Log.e(TAG, "stopHeartbeat()");
	// if (heartbeatThread != null) {
	// heartbeatThread.cancel();
	// }
	// }

	/**
	 * 开始读数据线程(单次读)
	 */
	private void __test__startReadTask() {
		if (D)
			Log.e(TAG, "startReadTask()");

		readThread = new AbsThread() {

			@Override
			public void run() {

				if (D)
					Log.e(TAG, "startReadTask()...开始while...");

				while (readThread.isCanceled() == false && isConnected()) {

					if (D)
						Log.e(TAG, "startReadTask()...入口while...");

					int readCount = -1;

					try {
						if (D)
							Log.e(TAG, "开始读.......");
						/*
						 * 如果输入缓冲队列RecvQ中没有数据，read操作会一直阻塞而挂起线程，
						 * 直到有新的数据到来或者有异常产生。调用setSoTimeout(int timeout)
						 * 可以设置超时时间，
						 * 如果到了超时时间仍没有数据，read会抛出一个SocketTimeoutException，
						 * 程序需要捕获这个异常，但是当前的socket连接仍然是有效的。
						 * 如果对方进程崩溃、对方机器突然重启、网络断开，本端的read会一直阻塞下去，这时设置超时时间是非常重要的，
						 * 否则调用read的线程会一直挂起。
						 */
						if (D)
							Log.e(TAG,
									"socket.getInputStream().available()有效的:"
											+ socket.getInputStream()
													.available());
						readCount = socket.getInputStream().read(byteBuffer); // 读单次
						if (D)
							Log.e(TAG, "readCount:" + readCount);
					} catch (SocketTimeoutException e) {
						if (D)
							Log.e(TAG, "读----发生了超时.....");
						// set count == 0
						readCount = 0;
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (D)
						Log.e(TAG, "结束读.......");
					if (D)
						Log.e(TAG, "readCount222:" + readCount);

					if (readCount > 0) {
						if (eventListener != null) {
							// 接收事件
							eventListener.onReceived(readCount, byteBuffer);
						}
					} else if (readCount <= -1) {
						// 网络出现问题，发消息
						sendMessage(Msg_Socket_Error);
					}

					if (D)
						Log.e(TAG, "startReadTask()...出口while...");
				}
				// super.run();
				if (D)
					Log.e(TAG, "startReadTask()...结束while...");
			}

		};
		//
		readThread.start();
	}

	// 数据包信息
	public class PacketInfo {
		private int count = 0; // 数量
		private byte[] bytes = null;
		private java.nio.ByteOrder byteOrder = null;
		private java.nio.ByteBuffer byteBuffer = null;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public byte[] getBytes() {
			return bytes;
		}

		public void setBytes(byte[] bytes) {
			this.bytes = bytes;
		}

		public java.nio.ByteOrder getByteOrder() {
			return byteOrder;
		}

		public void setByteOrder(java.nio.ByteOrder byteOrder) {
			this.byteOrder = byteOrder;
		}

		public java.nio.ByteBuffer getByteBuffer() {
			return byteBuffer;
		}

		public void setByteBuffer(java.nio.ByteBuffer byteBuffer) {
			this.byteBuffer = byteBuffer;
		}
	}

	/**
	 * 读缓冲区全部字节
	 * 
	 * 02-27 16:28:59.353: E/PushSocket(29371):
	 * socket.getInputStream().available()有效的222:4796 02-27 16:28:59.373:
	 * E/PushSocket(29371): socket.getInputStream().available()有效的222:3772 02-27
	 * 16:28:59.393: E/PushSocket(29371):
	 * socket.getInputStream().available()有效的222:2748 02-27 16:28:59.493:
	 * E/PushSocket(29371): socket.getInputStream().available()有效的222:8664 02-27
	 * 16:28:59.513: E/PushSocket(29371):
	 * socket.getInputStream().available()有效的222:7640 02-27 16:28:59.563:
	 * E/PushSocket(29371): socket.getInputStream().available()有效的222:6616 02-27
	 * 16:28:59.593: E/PushSocket(29371):
	 * socket.getInputStream().available()有效的222:7382 02-27 16:28:59.633:
	 * E/PushSocket(29371): socket.getInputStream().available()有效的222:6358 02-27
	 * 16:28:59.663: E/PushSocket(29371):
	 * socket.getInputStream().available()有效的222:5334 02-27 16:28:59.683:
	 * E/PushSocket(29371): socket.getInputStream().available()有效的222:4310 02-27
	 * 16:28:59.703: E/PushSocket(29371):
	 * socket.getInputStream().available()有效的222:3286 02-27 16:28:59.723:
	 * E/PushSocket(29371): socket.getInputStream().available()有效的222:2262 02-27
	 * 16:28:59.753: E/PushSocket(29371):
	 * socket.getInputStream().available()有效的222:1238
	 * 
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private PacketInfo readByteBufferAll(Socket socket) throws Exception {
		if (D)
			Log.e(TAG, "--- readByteBufferAll() start ---");

		if (socket == null) {
			return null;
		}

		if (D)
			Log.e(TAG, "socket.isInputShutdown():" + socket.isInputShutdown());
		if (D)
			Log.e(TAG, "socket.isOutputShutdown():" + socket.isOutputShutdown());

		// 缓冲区 TODO 会出错，还是close关闭
		// BufferedInputStream bufferedInput = new BufferedInputStream(
		// socket.getInputStream() );

		// Byte数组 ByteArrayOutputStream会自动增长
		ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();

		if (D)
			Log.e(TAG, "...start...socket.read()...");

		// 读到的大小
		int readCount = 0;

		// 共计，将读的全部累加起来
		int totalCount = 0;

		PacketInfo packetInfo = new PacketInfo();

		byte[] bytes = new byte[BUFFER_MAX_LEN];
		// byte[] byteBuffer = new byte[ 178 ];

		// readCount = bufferedInput.read(byteBuffer);
		readCount = socket.getInputStream().read(bytes);

		if (readCount > 0) {
			if (D)
				Log.e(TAG, "readCount > 0");

			totalCount += readCount;
			byteArrayOutput.write(bytes, 0, readCount);
			byteArrayOutput.flush();
			if (D)
				Log.e(TAG, "...第一次socket.read()...readCount:" + readCount);
			if (D)
				Log.e(TAG,
						"bytes2HexString:" + bytes2HexString(readCount, bytes));

			// if (DEBUG) Log.e(TAG, "...readCount...:"+readCount);
			// if (DEBUG) Log.e(TAG, "...totalCount...:"+totalCount);

			// 在数据包被拆分的情况下，如果available()太快，可能读不能第二个包
			Thread.sleep(scoket_read_available_sleep);

			if (D)
				Log.e(TAG, "...下面通过available来判断是否还有数据...start...");
			// 有效数据，要使用read()读到数据后，才能使用。
			int available = 0;
			while ((available = socket.getInputStream().available()) > 0) {
				// available()方法是有效的，只不过数据包会拆分成多个包发送。
				// 如服务端发了50条，则被分成二个包
				if (D)
					Log.e(TAG, "......进入while.......");
				if (D)
					Log.e(TAG, "...available...:" + available);

				// byte[] tmpByteBuffer = new byte[ available ];
				byte[] tmpBytes = new byte[BUFFER_MAX_LEN];
				// readCount = bufferedInput.read(tmpByteBuffer);
				readCount = socket.getInputStream().read(tmpBytes);
				if (D)
					Log.e(TAG, "...readCount...:" + readCount);
				if (readCount > 0) {
					totalCount += readCount;
					byteArrayOutput.write(tmpBytes, 0, readCount);
					byteArrayOutput.flush();
					if (D)
						Log.e(TAG, "...第二次socket.read()...readCount:"
								+ readCount);
					if (D)
						Log.e(TAG,
								"bytes2HexString:"
										+ bytes2HexString(readCount, tmpBytes));
				}

				// 在数据包被拆分的情况下，如果available()太快，可能读不能第二个包
				Thread.sleep(scoket_read_available_sleep);

				if (D)
					Log.e(TAG, "......出去while.......");
			}
			if (D)
				Log.e(TAG, "...下面通过available来判断是否还有数据...end...");

			if (D)
				Log.e(TAG, "...totalCount...:" + totalCount);

			// java nio byteBuffer
			int capacity = byteArrayOutput.size();
			if (D)
				Log.e(TAG, "...capacity...:" + capacity);

			java.nio.ByteBuffer resultByteBuffer = java.nio.ByteBuffer
					.allocate(capacity);
			resultByteBuffer.order(pushSocketConfig.getByteOrder()); // 字节顺序
			resultByteBuffer.put(byteArrayOutput.toByteArray());

			// ResultData resultData = new ResultData();
			packetInfo.setCount(capacity);
			packetInfo.setBytes(resultByteBuffer.array());
			packetInfo.setByteOrder(pushSocketConfig.getByteOrder());
			packetInfo.setByteBuffer(resultByteBuffer);

		} else if (readCount == 0) {
			if (D)
				Log.e(TAG, "readCount == 0");
			if (D)
				Log.e(TAG, "...readCount...:" + readCount);
			// int capacity = 0;
			// ResultData resultData = new ResultData();
			packetInfo.setCount(0);
			packetInfo.setBytes(null);
			packetInfo.setByteOrder(pushSocketConfig.getByteOrder());
			packetInfo.setByteBuffer(null);
		} else { // < 0 (包括-1)
			if (D)
				Log.e(TAG, "readCount < 0");
			if (D)
				Log.e(TAG, "...readCount...:" + readCount);
			// int capacity = 0;
			// ResultData resultData = new ResultData();
			packetInfo.setCount(readCount);
			packetInfo.setBytes(null);
			packetInfo.setByteOrder(pushSocketConfig.getByteOrder());
			packetInfo.setByteBuffer(null);
		}

		if (D)
			Log.e(TAG, "...totalCount...:" + totalCount);

		if (packetInfo.getCount() > 0) {
			if (D)
				Log.e(TAG,
						"最后得到bytes2HexString:"
								+ bytes2HexString(packetInfo.getBytes()));
		}

		// close
		// bufferedInput.close();
		byteArrayOutput.close();

		if (D)
			Log.e(TAG, "--- readByteBufferAll() end ---");
		return packetInfo;
	}

	private Pair<Integer, java.nio.ByteBuffer> __test__2_readByteBufferAll(
			Socket socket) throws Exception {

		if (D)
			Log.e(TAG, "readByteBufferAll()");

		if (socket == null) {
			return null;
		}

		// 缓冲区
		BufferedInputStream bufferedInput = new BufferedInputStream(
				socket.getInputStream());

		// Byte数组 ByteArrayOutputStream会自动增长
		ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();

		if (D)
			Log.e(TAG, "...end...socket.read()...");
		// read阻塞
		int c = bufferedInput.read(); // 读取bis流中的下一个字节
		if (D)
			Log.e(TAG, "read111->c:" + c);

		if (D)
			Log.e(TAG, "...start...while.......");
		while (c != -1) {
			byteArrayOutput.write(c);
			if (D)
				Log.e(TAG, "read第二次...");
			c = bufferedInput.read(); // 阻塞
			if (D)
				Log.e(TAG, "read222->c:" + c);
		}
		if (D)
			Log.e(TAG, "...end...while.......");

		bufferedInput.close();

		byte[] resultByteArray = byteArrayOutput.toByteArray();
		if (resultByteArray != null) {
			if (D)
				Log.e(TAG, "resultByteArray size:" + resultByteArray.length);
		} else {
			if (D)
				Log.e(TAG, "resultByteArray size == null");
		}

		return null;
	}

	private Pair<Integer, java.nio.ByteBuffer> __test__1_readByteBufferAll(
			Socket socket) throws Exception {

		if (D)
			Log.e(TAG, "readByteBufferAll()");
		if (socket == null) {
			return null;
		}

		// 列表缓冲区
		List<java.nio.ByteBuffer> listByteBuffer = new ArrayList<java.nio.ByteBuffer>();

		// 读缓冲区
		// java.nio.ByteBuffer readBuffer = java.nio.ByteBuffer.allocate(
		// BUFFER_MAX_LEN );
		// readBuffer.order( pushSocketConfig.getByteOrder() ); // 字节顺序
		// readBuffer.flip();
		// readBuffer.clear();
		// byte[] byteBuffer = new byte[ BUFFER_MAX_LEN ];

		// 读到的大小
		int readCount = 0;

		// 共计，将读的全部累加起来
		int totalCount = 0;

		// BufferedInputStream buffered = new BufferedInputStream(
		// socket.getInputStream() );

		// try {

		// if (DEBUG) Log.e(TAG,
		// "socket.getInputStream().available()有效的:"+socket.getInputStream().available());

		// 不受阻塞地从此输入流读取的字节数数量
		int available = 0;
		// available = socket.getInputStream().available();
		// if (available <= 0) {
		// // 无数据可读
		//
		// }

		// BufferedReader in = new BufferedReader(new
		// InputStreamReader(socket.getInputStream()));
		// PrintWriter out = new PrintWriter(socket.getOutputStream());
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(System.in));

		// for (int n = 0; n < 10; n++) {
		// available = socket.getInputStream().available();
		// if (DEBUG) Log.e(TAG,
		// "socket.getInputStream().available():"+available);
		// readCount = socket.getInputStream().read(byteBuffer);
		// if (DEBUG) Log.e(TAG,
		// "socket.getInputStream().readCount():"+readCount);
		// }

		int n = 0;
		// while ( (readCount = socket.getInputStream().read(byteBuffer)) > 0 )
		// {

		available = socket.getInputStream().available();
		if (D)
			Log.e(TAG, "socket.getInputStream().available():" + available);

		if (available >= 0) {

			// 没可读数据时，执行read阻塞，等待数据
			readCount = socket.getInputStream().read(byteBuffer);
			if (D)
				Log.e(TAG, "socket.getInputStream().readCount():" + readCount);

			// add to list
			java.nio.ByteBuffer tmpByteBuffer = java.nio.ByteBuffer
					.allocate(readCount);
			tmpByteBuffer.order(pushSocketConfig.getByteOrder()); // 字节顺序
			tmpByteBuffer.clear();
			// put buffer
			tmpByteBuffer.put(byteBuffer, 0, readCount);
			// 将buffer加入list列表
			listByteBuffer.add(tmpByteBuffer);

			// 累加
			totalCount += readCount;
			if (D)
				Log.e(TAG, "readByteBufferAll():totalCount:" + totalCount);

			// while ( (readCount = socket.getInputStream().read(byteBuffer)) >
			// 0 ) {
			// 发现第一个可读数据后，while( available > 0 )
			while ((available = socket.getInputStream().available()) > 0) {
				if (D)
					Log.e(TAG, "readByteBufferAll()...入口while...");

				if (D)
					Log.e(TAG, "socket.getInputStream().available():"
							+ available);

				byte[] tmpBytes = new byte[available];
				readCount = socket.getInputStream().read(tmpBytes);
				if (D)
					Log.e(TAG, "socket.getInputStream().readCount():"
							+ readCount);

				n++;
				if (D)
					Log.e(TAG, "readByteBufferAll():n++");

				// 累加
				totalCount += readCount;
				if (D)
					Log.e(TAG, "readByteBufferAll():totalCount:" + totalCount);

				// 放在list列表里的buffer
				tmpByteBuffer = java.nio.ByteBuffer.allocate(readCount);
				tmpByteBuffer.order(pushSocketConfig.getByteOrder()); // 字节顺序
				tmpByteBuffer.clear();
				// put buffer
				tmpByteBuffer.put(tmpBytes, 0, readCount);
				// 将buffer加入list列表
				listByteBuffer.add(tmpByteBuffer);

				if (D)
					Log.e(TAG, "readByteBufferAll(while):listBuffer len:"
							+ listByteBuffer.size());

				// if (DEBUG) Log.e(TAG,
				// "readByteBufferAll(while):readCount:"+readCount);
				if (D)
					Log.e(TAG, "readByteBufferAll(while):totalCount:"
							+ totalCount);
				// if (DEBUG) Log.e(TAG,
				// "readByteBufferAll(while):接收到的数据readBuffer:"+PushSocketChannel.bytes2HexString(byteBuffer));
				// if (DEBUG) Log.e(TAG,
				// "readByteBufferAll(while):接收到的数据appendBuffer:"+PushSocketChannel.bytes2HexString(tmpByteBuffer.array()));

				readCount = 0;
				// readBuffer.flip(); // 设置到第一个位置
				// readBuffer.clear();
			} // while end

		} // if end

		if (D)
			Log.e(TAG, "readByteBufferAll(while Out break..):n:" + n);
		if (D)
			Log.e(TAG, "readByteBufferAll(while Out break..):totalCount:"
					+ totalCount);
		if (D)
			Log.e(TAG, "readByteBufferAll(while Out break..):readCount:"
					+ readCount); // == 0
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		Pair<Integer, java.nio.ByteBuffer> pair = null;
		// pair = new Pair<Integer, java.nio.ByteBuffer>(totalCount,
		// resultBuffer);

		if (readCount <= -1) {
			// 出错，则将出错码传过去
			java.nio.ByteBuffer resultErrorBuffer = java.nio.ByteBuffer
					.allocate(0);
			// resultBuffer.order( getByteOrder() ); // 字节顺序
			pair = new Pair<Integer, java.nio.ByteBuffer>(readCount,
					resultErrorBuffer);
		} else {
			// 返回的缓冲区 totalCount <= -1 会出错 == 0 或者 > 0 则不会出错
			java.nio.ByteBuffer resultBuffer = java.nio.ByteBuffer
					.allocate(totalCount);
			resultBuffer.order(pushSocketConfig.getByteOrder()); // 字节顺序

			if (D)
				Log.e(TAG, "readByteBufferAll(END):listBuffer len:"
						+ listByteBuffer.size());
			// 将list缓冲区转成一个单独的buffer
			for (java.nio.ByteBuffer tmp : listByteBuffer) {
				if (tmp != null) {
					if (D)
						Log.e(TAG, "readByteBufferAll(END): put tmp...");
					if (D)
						Log.e(TAG,
								"readByteBufferAll(END): put tmp 内容:"
										+ PushSocketChannel.bytes2HexString(tmp
												.array()));
					resultBuffer.put(tmp.array());
					if (D)
						Log.e(TAG,
								"readByteBufferAll(END):接收到的数据 show...resultBuffer:"
										+ PushSocketChannel
												.bytes2HexString(resultBuffer
														.array()));
				}
			}

			if (D)
				Log.e(TAG, "readByteBufferAll(END):totalCount:" + totalCount);
			if (D)
				Log.e(TAG,
						"readByteBufferAll(END):resultBuffer.array().length:"
								+ resultBuffer.array().length);
			if (D)
				Log.e(TAG,
						"readByteBufferAll(END):接收到的数据resultBuffer:"
								+ PushSocketChannel
										.bytes2HexString(resultBuffer.array()));

			// 创建一对
			// (pair第一个为读的参数) 结果可能有三种：1）-1；2）为0；3）共读取数据的累加
			// == -1 时，表示跟服务器断开
			// Pair<Integer, java.nio.ByteBuffer> pair = new Pair<Integer,
			// java.nio.ByteBuffer>(totalCount, resultBuffer);
			pair = new Pair<Integer, java.nio.ByteBuffer>(totalCount,
					resultBuffer);
		}

		return pair;
	}

	/**
	 * 创建HandlerThread
	 */
	private void createHandlerThread() {
		if (D)
			Log.e(TAG, "createHandlerThread()");

		if (connectHandlerThread != null) {
			connectHandlerThread.quit();
		}

		// android.os.Process.THREAD_PRIORITY_BACKGROUND比默认的权重要低，所以资源会优先分配给其他线程(UI线程)
		connectHandlerThread = new HandlerThread("push_socket_handler_thread",
				android.os.Process.THREAD_PRIORITY_DEFAULT);

		connectHandlerThread.start(); // 创建HandlerThread后一定要记得start();

		handler = new Handler(connectHandlerThread.getLooper()) {

			public void handleMessage(Message msg) {

				switch (msg.what) {
				case Msg_Connect: // 连接
					if (D)
						Log.e(TAG, "message == Msg_Connect.");
					/**
					 * 执行连接任务
					 */
					executeConnect();
					// 连接失败，则重来
					if (isConnected() == false) {
						sendMessageDelayed(handler.obtainMessage(Msg_Connect),
								pushSocketConfig.getReconnectTime());
					}
					break;
				case Msg_Heartbeat: // 心跳
					if (D)
						Log.e(TAG, "message == Msg_Heartbeat.");
					break;
				case Msg_Socket_Read: // 读
					if (D)
						Log.e(TAG, "message == Msg_Socket_Read.");
					startReadTask();
					break;
				case Msg_Socket_Error:
					if (D)
						Log.e(TAG, "message == Msg_Socket_Error.");

					// TODO 问题：当产生异常java.net.SocketException: The connection was
					// reset时
					// 再执行closeSocket();，服务端会收到logout表示手机端主动登出。
					// 所以在closeSocket();之前，检查一下网络。

					// java.net.SocketException: The operation timed out
					// java.net.SocketException: The socket argument is not a
					// valid file descriptor
					// java.net.SocketException: Socket is closed
					// Resolution of the FileDescriptor to socket failed

					// close socket
					closeSocket();

					// 停止心跳定时任务
					stopHeartbeatTimerTask();
					// 停止读
					stopReadTask();
					// 调接口
					if (eventListener != null) {
						eventListener.onError(Error_Socket_Error);
					}

					// Log.status(context, new Throwable(), "socket出现异常:" +
					// "socket error");

					// 发消息来连接重连
					sendMessageDelayed(handler.obtainMessage(Msg_Connect),
							pushSocketConfig.getReconnectTime());
					break;
				case Msg_Socket_Close:
					if (eventListener != null) {
						eventListener.onClosed();
					}
					break;
				case Msg_Back_Runing:
					if (D)
						Log.e(TAG, "message == Msg_Back_Runing.");
					backCount++;
					if (D)
						Log.e(TAG,
								"产生了handler后台消息Msg_Back_Runing--->backCount:"
										+ backCount);
					sendMessageDelayed(handler.obtainMessage(Msg_Back_Runing),
							backTime); // 13s
					break;
				}

				super.handleMessage(msg);
			}
		};

	}

	/**
	 * 保持CPU运行
	 * 
	 * Flag Value CPU Screen Keyboard PARTIAL_WAKE_LOCK On* Off Off
	 * SCREEN_DIM_WAKE_LOCK On Dim Off SCREEN_BRIGHT_WAKE_LOCK On Bright Off
	 * FULL_WAKE_LOCK On Bright Bright
	 * 
	 * WakeLock 类型以及说明： PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
	 * SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
	 * SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯 FULL_WAKE_LOCK：保持CPU
	 * 运转，保持屏幕高亮显示，键盘灯也保持亮度 ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
	 * ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
	 */
	private void acquireCPU() {
		if (D)
			Log.e(TAG, "...start...acquireCPU()...");
		if (wakeLock == null && context != null) {
			powerManager = (PowerManager) context
					.getSystemService(Context.POWER_SERVICE);
			if (powerManager != null) {
				// wakeLock =
				// powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				// this.getClass().getCanonicalName());
				wakeLock = powerManager.newWakeLock(
						PowerManager.PARTIAL_WAKE_LOCK,
						new AppInfo(context).getPackageName());
			}
		}
		if (wakeLock != null) {
			wakeLock.acquire();
			if (D)
				Log.e(TAG, "execute...wakeLock.acquire();");
		}
		if (D)
			Log.e(TAG, "...end...acquireCPU()...");
	}

	/**
	 * 释放CPU保持运行
	 */
	private void releaseCPU() {
		if (D)
			Log.e(TAG, "...start...releaseCPU()...");
		if (wakeLock != null && wakeLock.isHeld()) {
			wakeLock.release();
			if (D)
				Log.e(TAG, "execute...wakeLock.release();");
			wakeLock = null;
		}
		if (D)
			Log.e(TAG, "...end...releaseCPU()...");
	}

	/**
	 * 开始
	 * 
	 * @return
	 */
	public void start() {
		if (D)
			Log.e(TAG, "start()");
		/**
		 * 保持CPU运行
		 */
		acquireCPU();
		/**
		 * 创建HandlerThread
		 */
		createHandlerThread();
		/**
		 * 开始连接
		 */
		startConnect();
		//
		if (Config.getVersionDevelop() != Config.VersionDevelop.Final) {
			// 非正式版
			/**
			 * 开始打印定时任务
			 */
			startPrintTimerTask();
			// 打印LOG
			backCount = 0;
			sendMessageDelayed(Msg_Back_Runing, backTime);
		}
	}

	/**
	 * 连接
	 * 
	 * @return
	 */
	private boolean connect() {
		if (D)
			Log.e(TAG, "connect()");

		try {
			// close socket
			closeSocket();
			// 创建socket对象，指定服务器端地址和端口号

			getPushSocketConfig().print();

			socket = null;
			socket = new Socket();
			// 采用代理
			// String proxyIp = "10.0.0.172";
			// int proxyPort = 80;
			// InetSocketAddress socketAddress = new InetSocketAddress(proxyIp,
			// proxyPort);
			// java.net.Proxy proxy = new
			// java.net.Proxy(java.net.Proxy.Type.SOCKS, socketAddress);
			// socket = new Socket(proxy);
			SocketAddress remoteAddr = new InetSocketAddress(
					getPushSocketConfig().getHost(), getPushSocketConfig()
							.getPort());

			// 使用下面的，也会出现java.net.SocketException: recvfrom failed: ETIMEDOUT
			// (Connection timed out)
			// socket = new Socket(getPushSocketConfig().getHost(),
			// getPushSocketConfig().getPort()); // 执行这个，就自动连接了
			if (socket != null) {
				if (D)
					Log.e(TAG, "connect()...开始...");
				// 第二个参数不是连接超时，默认为90s
				// socket.connect(remoteAddr,
				// getPushSocketConfig().getConnectTimeout());
				socket.connect(remoteAddr);
				if (D)
					Log.e(TAG, "connect()...结束...");
				// 设置已连接的状态
				setConnected(true);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (java.net.ConnectException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (java.lang.AssertionError e) {
			e.printStackTrace();
			if (D)
				Log.e(TAG,
						"connect():printf java.lang.AssertionError.. ------ ....");
		}

		return isConnected();
	}

	/**
	 * 通过Socket发送一个数据，判断网络远端是否断开连接。
	 * 
	 * 注意：在使用setOOBInline方法打开SO_OOBINLINE选项时要注意是必须在客户端
	 * 和服务端程序同时使用setOOBInline方法打开这个选项，否则无法命名用 sendUrgentData来发送数据。
	 * 
	 * 测试通过
	 * 
	 * SO_OOBINLINE BOOL 在正常的数据流中接收带外数据 FALSE
	 * 
	 * 设置 OOBINLINE 选项时，在套接字上接收的所有 TCP 紧急数据都将通过套接字输入流接收。 禁用该选项时（默认），将悄悄丢弃紧急数据。
	 * 
	 * SO_OOBINLINE为false时，服务端会丢弃紧急数据。（一般设置为false）
	 * SO_OOBINLINE为true时，服务端可read()到紧急数据。
	 * 
	 * @return
	 */
	public boolean isInline() {
		if (D)
			Log.e(TAG, "isInline()");

		boolean isInline = false;

		if (socket != null) {
			// TODO 或者通过getLocalSocketAddress()是否产生异常来判断socket的存在性。
			try {
				// TODO 当网络关闭后，执行socket.getOOBInline()这条，
				// 会产生Socket is closed异常
				// 当getOOBInline()为false时，才能使用sendUrgentData()发送数据
				Log.e(TAG, "socket.sendUrgentData(0xFF)");
				socket.sendUrgentData(0xFF);
				isInline = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Log.e(TAG, "isInline():" + isInline);
		return isInline;
	}

	/**
	 * 第二次方式判断网络是否畅通（还在测试，不知道是否可行） 答，此方法不行 因。isInline()为false，但isInline2()为true
	 * 
	 * @return
	 */
	public boolean isInline2() {
		Log.e(TAG, "...start...isInline2()...");

		boolean isInline2 = true;

		try {
			SocketAddress localSocketAddress = socket.getLocalSocketAddress();
			if (localSocketAddress != null) {
				if (D)
					Log.e(TAG, "localSocketAddress.toString():"
							+ localSocketAddress.toString());
			}
		} catch (Exception e) {
			Log.e(TAG, "isInline2() --- 第1个异常.");
			e.printStackTrace();
			isInline2 = false;
		}

		try {
			InetAddress localInetAddress = socket.getLocalAddress();
			if (localInetAddress != null) {
				if (D)
					Log.e(TAG, "localInetAddress.getHostName():"
							+ localInetAddress.getHostName());
				if (D)
					Log.e(TAG, "localInetAddress.getHostAddress():"
							+ localInetAddress.getHostAddress());
				if (D)
					Log.e(TAG, "localInetAddress.getCanonicalHostName():"
							+ localInetAddress.getCanonicalHostName());
			}
		} catch (Exception e) {
			Log.e(TAG, "isInline2() --- 第2个异常.");
			e.printStackTrace();
			isInline2 = false;
		}

		// socket产生异常，下面还能正常获取。
		// try {
		// if (D) Log.e(TAG, "socket.getLocalPort():"+socket.getLocalPort());
		// } catch (Exception e) {
		// Log.e(TAG, "isInline2() --- 第3个异常.");
		// e.printStackTrace();
		// isInline2 = false;
		// }

		Log.e(TAG, "...end...isInline2()...");
		Log.e(TAG, "isInline2():" + isInline2);
		return isInline2;
	}

	/**
	 * 发送
	 * 
	 * @param bytes
	 * @return <= -1 表示错误 == 0 表示条件不足，不发送， > 0 发送成功
	 */
	public int send(byte[] bytes) {
		if (D)
			Log.e(TAG, "send()");

		// this.isInline();
		// this.isInline2();

		int count = -1;

		if (socket == null || isConnected() == false) {
			return 0;
		}

		if (bytes == null || bytes.length <= 0) {
			return 0;
		}

		try {
			OutputStream outputStream = socket.getOutputStream();
			if (outputStream != null) {
				outputStream.write(bytes);
				outputStream.flush();
				count = bytes.length;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (D)
			Log.e(TAG, "send():count:" + count);

		// this.isInline();
		// this.isInline2();

		if (count <= -1) {
			// 发送失败，表示Socket出现问题，需要重建Socket
			// 网络出现问题，发消息
			sendMessage(Msg_Socket_Error);
		}

		return count;
	}

	/**
	 * 发送
	 * 
	 * @param buffer
	 * @return
	 */
	public int send(java.nio.ByteBuffer buffer) {
		if (buffer == null) {
			return 0;
		}
		return send(buffer.array());
	}

	/**
	 * 设置接收事件接口
	 * 
	 * @param listener
	 */
	public void setEventListener(IEventListener listener) {
		this.eventListener = listener;
	}

	/**
	 * 判断是否已连接
	 * 
	 * @return
	 */
	// public boolean isConnected() {
	// boolean isConnected = false;
	// if (socket == null) {
	// return false;
	// }
	// if (DEBUG) Log.e(TAG,
	// "isConnected():socket.isConnected():"+socket.isConnected());
	// if (DEBUG) Log.e(TAG,
	// "isConnected():socket.isClosed():"+socket.isClosed());
	// isConnected = socket.isConnected() && !socket.isClosed() &&
	// this.isConnected;
	// if (DEBUG) Log.e(TAG, "isConnected():"+isConnected);
	// return isConnected;
	// }

	/**
	 * 判断是否已连接
	 * 
	 * @return
	 */
	public boolean isConnected() {
		if (D)
			Log.e(TAG, "isConnected():" + this.isConnected);
		return this.isConnected;
	}

	/**
	 * 设置已连接状态
	 * 
	 * @param isConnected
	 */
	private void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	/**
	 * 关闭Socket TODO 如果SOCKET已经在阻塞read了，此方法closeSocket不会产生异常。
	 * 
	 */
	private void closeSocket(Socket socket) {
		if (D)
			Log.e(TAG, "closeSocket(socket)");
		if (socket != null) {
			try {
				// 调用Socket的close()方法将同时终止两个方向（输入和输出）的数据流.
				socket.close();
				if (D)
					Log.e(TAG, "closeSocket(socket)--->socket.close();");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 关闭Socket
	 */
	private void closeSocket() {
		if (D)
			Log.e(TAG, "closeSocket()");
		closeSocket(this.getSocket());
		this.socket = null;
		// 关闭SOCKET了，连接状态也要跟着改变
		setConnected(false);
		isConnected();
	}

	/**
	 * 关闭
	 * 
	 * @return
	 */
	public void close() {
		if (D)
			Log.e(TAG, "close()");

		/**
		 * 释放CPU保持运行
		 */
		releaseCPU();

		if (handler != null) {
			removeMessage(Msg_Connect);
			removeMessage(Msg_Heartbeat);
			removeMessage(Msg_Socket_Read);
			removeMessage(Msg_Socket_Error);
			removeMessage(Msg_Socket_Close);
			removeMessage(Msg_Back_Runing); // TODO test
		}

		if (connectHandlerThread != null) {
			connectHandlerThread.quit();
		}

		stopConnect();
		stopHeartbeatTimerTask();
		stopPrintTimerTask();

		closeSocket();

		stopReadTask();

		// 关闭事件接口
		// sendMessage( Msg_Socket_Close ); // 不发消息，因handler都没了
		if (eventListener != null) {
			eventListener.onClosed();
		}

		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }

	}

	/**
	 * 打印状态
	 * 
	 * 08-12 09:30:29.138: E/PushSocket(29763): ------ printSocket() start
	 * ------ 08-12 09:30:29.148: E/PushSocket(29763): ------ 远程域名和端口 ------
	 * 08-12 09:30:29.148: E/PushSocket(29763):
	 * socketAddress.toString():carlife.wisdom-gps.com/202.103.191.62:2295 08-12
	 * 09:30:29.158: E/PushSocket(29763):
	 * inetAddress.getHostName():carlife.wisdom-gps.com 08-12 09:30:29.168:
	 * E/PushSocket(29763): inetAddress.getHostAddress():202.103.191.62 08-12
	 * 09:30:29.328: E/PushSocket(29763):
	 * inetAddress.getCanonicalHostName():202.103.191.62 08-12 09:30:29.328:
	 * E/PushSocket(29763): socket.getPort():2295 08-12 09:30:29.338:
	 * E/PushSocket(29763): ------ 本地域名和端口 ------ 08-12 09:30:29.408:
	 * E/PushSocket(29763):
	 * localSocketAddress.toString():bogon/10.107.44.65:41482 08-12
	 * 09:30:29.418: E/PushSocket(29763): localInetAddress.getHostName():bogon
	 * 08-12 09:30:29.428: E/PushSocket(29763):
	 * localInetAddress.getHostAddress():10.107.44.65 08-12 09:30:29.438:
	 * E/PushSocket(29763): localInetAddress.getCanonicalHostName():bogon 08-12
	 * 09:30:29.438: E/PushSocket(29763): socket.getLocalPort():41482 08-12
	 * 09:30:29.448: E/PushSocket(29763): socket.getSoTimeout()读超时:0 08-12
	 * 09:30:29.458: E/PushSocket(29763): socket.getSoLinger():3 08-12
	 * 09:30:29.468: E/PushSocket(29763): socket.getReceiveBufferSize():87380
	 * 08-12 09:30:29.498: E/PushSocket(29763): socket.getSendBufferSize():16384
	 * 08-12 09:30:29.528: E/PushSocket(29763): socket.getTrafficClass():20
	 * 08-12 09:30:29.548: E/PushSocket(29763): socket.getKeepAlive():false
	 * 08-12 09:30:29.598: E/PushSocket(29763): socket.getOOBInline():false
	 * 08-12 09:30:29.678: E/PushSocket(29763): socket.getReuseAddress():false
	 * 08-12 09:30:29.808: E/PushSocket(29763): socket.getTcpNoDelay():true
	 * 08-12 09:30:29.878: E/PushSocket(29763): socket.isBound():true 08-12
	 * 09:30:29.988: E/PushSocket(29763): socket.isClosed()网络断开了，则无法判断:false
	 * 08-12 09:30:30.058: E/PushSocket(29763):
	 * socket.isConnected()网络断开了，则无法判断:true 08-12 09:30:30.108:
	 * E/PushSocket(29763): socket.isInputShutdown():false 08-12 09:30:30.198:
	 * E/PushSocket(29763): socket.isOutputShutdown():false 08-12 09:30:30.258:
	 * E/PushSocket(29763): ------ printSocket() end ------
	 */
	public void printSocket(Socket socket) {
		if (D)
			Log.e(TAG, "------ printSocket() start ------");
		if (socket != null) {
			try {
				// 远程域名和端口
				if (D)
					Log.e(TAG, "------ 远程域名和端口 ------");
				SocketAddress socketAddress = socket.getRemoteSocketAddress();
				if (socketAddress != null) {
					if (D)
						Log.e(TAG,
								"socketAddress.toString():"
										+ socketAddress.toString());
				}
				InetAddress inetAddress = socket.getInetAddress();
				if (inetAddress != null) {
					if (D)
						Log.e(TAG,
								"inetAddress.getHostName():"
										+ inetAddress.getHostName());
					if (D)
						Log.e(TAG, "inetAddress.getHostAddress():"
								+ inetAddress.getHostAddress());
					if (D)
						Log.e(TAG, "inetAddress.getCanonicalHostName():"
								+ inetAddress.getCanonicalHostName());
				}
				if (D)
					Log.e(TAG, "socket.getPort():" + socket.getPort());
				// 本地域名和端口
				if (D)
					Log.e(TAG, "------ 本地域名和端口 ------");
				SocketAddress localSocketAddress = socket
						.getLocalSocketAddress();
				if (localSocketAddress != null) {
					if (D)
						Log.e(TAG, "localSocketAddress.toString():"
								+ localSocketAddress.toString());
				}
				InetAddress localInetAddress = socket.getLocalAddress();
				if (localInetAddress != null) {
					if (D)
						Log.e(TAG, "localInetAddress.getHostName():"
								+ localInetAddress.getHostName());
					if (D)
						Log.e(TAG, "localInetAddress.getHostAddress():"
								+ localInetAddress.getHostAddress());
					if (D)
						Log.e(TAG, "localInetAddress.getCanonicalHostName():"
								+ localInetAddress.getCanonicalHostName());
				}
				if (D)
					Log.e(TAG, "socket.getLocalPort():" + socket.getLocalPort());
				//
				if (D)
					Log.e(TAG,
							"socket.getSoTimeout()读超时:" + socket.getSoTimeout());
				if (D)
					Log.e(TAG, "socket.getSoLinger():" + socket.getSoLinger());
				if (D)
					Log.e(TAG,
							"socket.getReceiveBufferSize():"
									+ socket.getReceiveBufferSize());
				if (D)
					Log.e(TAG,
							"socket.getSendBufferSize():"
									+ socket.getSendBufferSize());
				if (D)
					Log.e(TAG,
							"socket.getTrafficClass():"
									+ socket.getTrafficClass());
				if (D)
					Log.e(TAG, "socket.getKeepAlive():" + socket.getKeepAlive());
				if (D)
					Log.e(TAG, "socket.getOOBInline():" + socket.getOOBInline());
				if (D)
					Log.e(TAG,
							"socket.getReuseAddress():"
									+ socket.getReuseAddress());
				if (D)
					Log.e(TAG,
							"socket.getTcpNoDelay():" + socket.getTcpNoDelay());
				if (D)
					Log.e(TAG, "socket.isBound():" + socket.isBound());
				if (D)
					Log.e(TAG,
							"socket.isClosed()网络断开了，则无法判断:" + socket.isClosed());
				if (D)
					Log.e(TAG,
							"socket.isConnected()网络断开了，则无法判断:"
									+ socket.isConnected());
				if (D)
					Log.e(TAG,
							"socket.isInputShutdown():"
									+ socket.isInputShutdown());
				if (D)
					Log.e(TAG,
							"socket.isOutputShutdown():"
									+ socket.isOutputShutdown());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (D)
			Log.e(TAG, "------ printSocket() end ------");
	}

	/**
	 * 打印
	 */
	public void printSocket() {
		printSocket(this.getSocket());
	}

	/**
	 * ByteBuffer转byte[]
	 * 
	 * @return
	 */
	public static byte[] byteBuffer2Bytes(java.nio.ByteBuffer buffer) {
		if (buffer == null) {
			return null;
		}
		return buffer.array();
	}

	/**
	 * ByteBuffer转HexString
	 * 
	 * @param buffer
	 * @return
	 */
	public static String byteBuffer2HexString(java.nio.ByteBuffer buffer) {
		return bytes2HexString(byteBuffer2Bytes(buffer));
	}

	/**
	 * byte[]转ByteBuffer
	 * 
	 * @return
	 */
	public static java.nio.ByteBuffer bytes2ByteBuffer(byte[] bytes,
			ByteOrder byteOrder) {
		if (byteOrder == null || bytes == null) {
			return null;
		}

		int capacity = bytes.length;
		if (capacity <= 0) {
			return null;
		}

		// java.nio.ByteBuffer byteBuffer =
		// java.nio.ByteBuffer.allocate(capacity);
		java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(bytes);
		if (byteBuffer == null) {
			return null;
		}

		// 设置字节排序
		byteBuffer.order(byteOrder);

		// byteBuffer.put(bytes);

		return byteBuffer;
	}

	/**
	 * 将字节数组转成设定的字节顺序字节数组
	 * 
	 * @param bytes
	 * @param byteOrder
	 * @return
	 */
	public static byte[] bytes2Bytes(byte[] bytes, ByteOrder byteOrder) {
		if (bytes == null) {
			return null;
		}

		if (byteOrder == null) {
			return null;
		}

		java.nio.ByteBuffer byteBuffer = bytes2ByteBuffer(bytes, byteOrder);
		byteBuffer.order(byteOrder);
		return byteBuffer2Bytes(byteBuffer);
	}

	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	/**
	 * 将字节转成十六进制的字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2HexString(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		return bytes2HexString(bytes.length, bytes);
	}

	/**
	 * 将字节转成十六进制的字符串
	 * 
	 * @param count
	 * @param bytes
	 * @return
	 */
	public static String bytes2HexString(int count, byte[] bytes) {
		if (count <= 0) {
			return null;
		}

		if (bytes == null) {
			return null;
		}

		int len = bytes.length;
		if (count > len) {
			return null;
		}

		byte[] hex = "0123456789ABCDEF".getBytes();
		byte[] buffer = new byte[2 * count];
		for (int i = 0; i < count; i++) {
			buffer[2 * i] = hex[(bytes[i] >> 4) & 0x0f];
			buffer[2 * i + 1] = hex[bytes[i] & 0x0f];
		}
		return new String(buffer);
	}

	/**
	 * 从十六进制字符串到字节数组转换
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] hexString2Bytes(String hexString) {
		if (isEmptyString(hexString)) {
			return null;
		}

		byte[] bytes = new byte[hexString.length() / 2];
		int j = 0;
		for (int i = 0; i < bytes.length; i++) {
			char c0 = hexString.charAt(j++);
			char c1 = hexString.charAt(j++);
			bytes[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return bytes;
	}

}