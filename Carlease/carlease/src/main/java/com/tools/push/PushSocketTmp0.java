package com.tools.push;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.CharBuffer;

import android.os.Handler;
import android.os.Message;

import com.tools.thread.AbsThread;
import com.tools.util.Log;

/**
 * 推送SocketChannel
 * 
 * Socket详解
 * http://www.cnblogs.com/jerrychoi/archive/2010/04/15/1712931.html
 * 
 * SocketChannel详解
 * http://www.cnblogs.com/likwo/archive/2010/06/29/1767814.html
 * 
 * 中文Java SDK
 * http://www.cjsdn.net/Doc/JDK50/java/nio/channels/SocketChannel.html
 * 
 * @author LMC
 *
 */
@Deprecated
public class PushSocketTmp0 {

	private static final String TAG = PushSocketTmp0.class.getSimpleName();

	protected String host;
	protected int port;

	protected Socket socket = null;

	// 读超时，等于0时，为没有超时
	protected int readTimeout = 3 * 1000;

	// 是否重连
	protected boolean isReconnect = true;

	// 重连的间隔时间
	protected long reconnectTime = 5 * 1000;

	// 心跳间隔时间
	protected long heartbeatTime = 5 * 60 * 1000; // 5分钟

	// 缓冲区最大长度
	protected static final int BUFFER_MAX_LEN = 1024;

	// 消息
	protected static final int MSG_HEARTBEAT = 1;
	protected static final int MSG_RECONNECT = 2;

	protected OutputStream out = null;
	protected InputStream in = null;

	// 读线程
	protected AbsThread readThread = null; 

	protected byte[] buffer = new byte[ BUFFER_MAX_LEN ];

	// 事件接口
	public interface IEventListener {
		void onConnected(); // 已连接事件
		void onReceived(int count, byte[] buffer); // 接收到数据包
		void onHeartbeat(); // 心跳事件
//		void onClosed(); // 关闭事件
	}

	protected IEventListener eventListener = null;

	public PushSocketTmp0(String host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 初始化 --- Socket
		initSocket();
		if ( isReconnect() ) {
			// 启动重连机制
			startReconnect();
		}
		// 启动心跳
		startHeartbeat();
	}

	/**
	 * 初始化 --- Socket
	 */
	private void initSocket() {
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
			socket.setSoTimeout(this.readTimeout);
			Log.e(TAG, "readTimeout:"+socket.getSoTimeout());
			// 当SO_KEEPALIVE选项为true，表示底层的TCP实现会监视该连接是否有效。
			// SO_KEEPALIVE选项的默认值为false，表示TCP不会监视连接是否有效，不活动的客户端可能会永久存在下去，而不会注意到服务器已经崩溃。
			socket.setKeepAlive(true); // == true 数据不作缓冲，立即发送
			// TCP_NODEALY的默认值为false，表示采用Negale算法。
			// 如果调用setTcpNoDelay(true)方法，就会关闭Socket的缓冲，确保数据及时发送：
			socket.setTcpNoDelay(true);
			// socket关闭时，立即释放资源
			socket.setSoLinger(true, 0);
			// IP规定了四种服务类型，用来定性的描述服务的质量
			socket.setTrafficClass(0x04 | 0x10); // 高可靠性和最小延迟传输
			// connectionTime 表示用最少时间建立连接 
			// latency 表示最小延迟 
			// bandwidth 表示最高带宽
			// 就表示最高带宽最重要，其次是最少连接时间，最后是最小延迟。
			socket.setPerformancePreferences(1,2,3);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		Log.e(TAG, "isReconnect:"+isReconnect);
		Log.e(TAG, "reconnectTime:"+reconnectTime);
		Log.e(TAG, "heartbeatTime:"+heartbeatTime);
		Log.e(TAG, "isInputShutdown:"+socket.isInputShutdown());
		Log.e(TAG, "isOutputShutdown:"+socket.isOutputShutdown());
		Log.e(TAG, "close:"+socket.isClosed()); // false
		Log.e(TAG, "isConnected:"+socket.isConnected()); // true
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_HEARTBEAT: // 心跳
				if (eventListener != null) {
					eventListener.onHeartbeat();
				}
				startHeartbeat();
				break;
			case MSG_RECONNECT: // 重连
				startReconnect();
				break;
			}
			super.handleMessage(msg);
		}
	};

	//	protected TimerTask task = new TimerTask() {
	//		public void run() {  
	//			Message message = new Message();
	//			message.what = 1;      
	//			handler.sendMessage(message);    
	//		}
	//	};

	//	protected Timer timer;

	/**
	 * 启动心跳
	 */
	protected void startHeartbeat() {
		//		timer = new Timer(true);
		//		timer.schedule(task, 1000, 1000); //延时1000ms后执行，1000ms执行一次
		//timer.cancel(); //退出计时器
		handler.sendEmptyMessageDelayed(MSG_HEARTBEAT, heartbeatTime);
	}

	/**
	 * 停止心跳
	 */
	protected void stopHeartbeat() {
		handler.removeMessages(MSG_HEARTBEAT);
	}

	/**
	 * 启动重连机制
	 */
	protected void startReconnect() {

	}

	/**
	 * 停止重连机制
	 */
	protected void stopReconnect() {
		handler.removeMessages(MSG_RECONNECT);
	}

	/**
	 * 开始读数据线程
	 */
	protected void startReadThread() {

		readThread = new AbsThread() {

			@Override
			public void run() {
				//				byte[] buffer = new byte[ BUFFER_LEN ];
				Log.e(TAG, "in.read(buffer); --- start...");
				int realReadCount = 0;
				//			while ((realRead = in.read(buffer)) != -1) {
				while ( readThread.isCanceled() == false ) {
					try {
						realReadCount = in.read(buffer);
						Log.e(TAG, "realReadCount:"+realReadCount);
					} catch (IOException e) {
						e.printStackTrace();
						realReadCount = -1;
					}
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					Log.e(TAG, "realReadCount:"+realReadCount);
					Log.e(TAG, "isInputShutdown:"+socket.isInputShutdown());
					Log.e(TAG, "isOutputShutdown:"+socket.isOutputShutdown());
					Log.e(TAG, "close:"+socket.isClosed()); // false
					Log.e(TAG, "isConnected:"+socket.isConnected()); // true
					
					if (realReadCount != -1) {
						if (eventListener != null) {
							Log.e(TAG, "onReceived start...");
							Log.e(TAG, "realRead String:"+new String(buffer));
							eventListener.onReceived(realReadCount, buffer);
							Log.e(TAG, "onReceived end...");
						}
					}
				}
				//				super.run();
			}

		};
		//
		readThread.start();
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public long getReconnectTime() {
		return reconnectTime;
	}

	public long getHeartbeatTime() {
		return heartbeatTime;
	}

	public void setHeartbeatTime(int heartbeatTime) {
		this.heartbeatTime = heartbeatTime;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * 设置读超时
	 * 
	 * @param timeout
	 */
	public void setReadTimeout(int timeout) {
		this.readTimeout = timeout;
	}

	/**
	 * 设置是否重连
	 * 
	 * @return
	 */
	public void setReconnect(boolean reconnect, int reconnectTime) {
		this.isReconnect = reconnect;
		this.reconnectTime = reconnectTime;
	}

	/**
	 * 连接
	 * 
	 * @return
	 */
	public boolean connect() {
		boolean connect = false;
		try {
			// 创建socket对象，指定服务器端地址和端口号
			socket = new Socket(host, port); // 自动连接了
			// 初始化
			init();
			if (socket != null) {
				//
				out = socket.getOutputStream(); // send
				in = socket.getInputStream(); // read
				// set true
				connect = true;
				// 连接事件接口
				if (this.eventListener != null) {
					eventListener.onConnected();
				}
				// 开始读数据线程
				startReadThread();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.e(TAG, "connect:"+connect);
		return connect;
	}

	/**
	 * 连接
	 * 
	 * @return
	 */
	protected boolean __connect() {

		try {
			// 创建socket对象，指定服务器端地址和端口号
			socket = new Socket(host, port); // 自动连接了
			Log.e(TAG, "vvvvvvvvvvvvvvv");
			Log.e(TAG, "close:"+socket.isClosed()); // false
			Log.e(TAG, "isConnected:"+socket.isConnected()); // true

			// 设置超时
			// Sets this socket's read timeout in milliseconds.
			Log.e(TAG, "getSoTimeout:"+socket.getSoTimeout());
			socket.setSoTimeout(this.readTimeout);
			Log.e(TAG, "getSoTimeout:"+socket.getSoTimeout());

			// 获取 Client 端的输出流
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);

			//			OutputStream ou = socket.getOutputStream();
			//			ou.w
			//			out.w

			// 填充信息
			//			out.println("AAABBB");

			String body = "{\"username\":\"minz\"}";
			Log.e(TAG, "body len A:"+body.length());
			Log.e(TAG, "body len B:"+body.toCharArray().length);

			char[] header = new char[5];
			header[0] = 0x01;

			header[1] = 0x00;
			header[2] = (char) (body.toCharArray().length + 5); // 0x18 == d24
			Log.e(TAG, "len:"+(body.toCharArray().length + 5));

			header[3] = 0x00;
			header[4] = 0x00;

			CharBuffer ccc = CharBuffer.allocate(100);
			ccc.put(header);
			ccc.append(body);

			Log.e(TAG, "ccc len:"+ccc.length());
			Log.e(TAG, "ccc string:"+ccc.toString());
			Log.e(TAG, "ccc string hex:"+bytes2HexString(new String(ccc.array()).getBytes()));

			out.write(ccc.array());
			out.flush();

			System.out.println("send ..msg");
			byte[] buffer = new byte[1024];

			socket.getInputStream().read(buffer);
			Log.e(TAG, "client get text:"+new String(buffer).toString());
			Log.e(TAG, "client get text byte:"+bytes2HexString(buffer));
			System.out.println("send ..msg ---  get end.");
			// 关闭
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			//			try {
			//				if (socket != null) {
			//					socket.close();
			//				}
			//			} catch (IOException e) {
			//				e.printStackTrace();
			//			}
		}

		return true;
	}

	/**
	 * 判断是否连接
	 * 
	 * @return
	 */
	public boolean isConnected() {
		if (socket == null) {
			return false;
		}
		return socket.isConnected() && !socket.isClosed();
	}

	/**
	 * 判断是否重连
	 * 
	 * @return
	 */
	public boolean isReconnect() {
		return isReconnect;
	}

	/**
	 * 发送
	 * 
	 * @return
	 */
	public boolean send(byte[] buffer) {
		boolean send = false;
		if (out != null && buffer != null) {
			try {
				out.write(buffer);
				out.flush();
				send = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return send;
	}
	
	public boolean send(java.nio.ByteBuffer buffer) {
		if (buffer == null) {
			return false;
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
	 * 是否关闭
	 * 
	 * @return
	 */
	public boolean isClosed() {
		if (socket == null) {
			return true;
		}
		return socket.isClosed();
	}

	/**
	 * 关闭socket
	 * 
	 * @return
	 */
	public void close() {
		//
		stopHeartbeat();
		stopReconnect();
		//
		if (readThread != null) {
			readThread.cancel();
		}
		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (socket != null) { // 和上面分开
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 关闭事件接口
//		if (this.eventListener != null) {
//			eventListener.onClosed();
//		}
	}

	public static String bytes2HexString(byte[] b) {
		byte[] hex = "0123456789ABCDEF".getBytes();
		byte[] buff = new byte[3 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buff);
	}

}