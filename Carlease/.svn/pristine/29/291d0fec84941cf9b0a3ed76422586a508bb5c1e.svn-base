package com.tools.push;

import java.nio.ByteOrder;

import com.tools.util.Log;

/**
 * PushSocket配置
 * 
 * 记录
 * 从2014-02-27 18:59:11到2014-02-28 08:20:27历经12个小时，共计发生了message == Msg_Network_Error八次
 * 
 * 发生
 * 2014-02-27 19:07:56  W/System.err(28359): java.net.SocketException: recvfrom failed: ETIMEDOUT (Connection timed out)
 * 紧跟着发生
 * 2014-02-27 19:07:57  W/System.err(28359): Caused by: libcore.io.ErrnoException: recvfrom failed: ETIMEDOUT (Connection timed out)
 * 上述原因是：是因为网络断开，跟msgBody--->string:{"username":"pabc"}次数是一样的
 * 
 * 
 * 问题一：
 * 02-28 08:59:36.583: E/PushService(28359): onReceive():接收到的数量...count:10
 * 02-28 08:59:36.583: E/PushService(28359): onReceive():接收到的数据:0A000000028000000000
 * 频率跟心跳一样，说明available() and read()可以读到发出去的数据
 * 而且，发出去的是0A000000021000000000，而read()读到的是0A000000028000000000
 * 
 * 
 * 问题二：
 * 先使用read()再使用available()
 * 所以读取read()后再使用available()，已经可以很好的保证通信正常。 
 * http://www.iteye.com/problems/81029
 * 
 * 
 * 学习三:
 * 将网络流变成对象流进行传输ObjectInputStream
 * http://blog.csdn.net/iamkila/article/details/7302962
 *
 * 正常使用Java I/O输出和读入数据 
 * http://blog.csdn.net/shendl/article/details/1542126
 * 
 * 
 * 读数据
 * http://www.cnblogs.com/MyFavorite/archive/2010/10/19/1855758.html
 * 
 * ByteArrayInputStream
 *   我的个人理解是ByteArrayOutputStream是用来缓存数据的（数据写入的目标（output stream原义）），
 *   向它的内部缓冲区写入数据，缓冲区自动增长，当写入完成时可以从中提取数据。由于这个原因，
 *   ByteArrayOutputStream常用于存储数据以用于一次写入
 *   
 *   
 * BufferedInputStream缓冲区
 * BufferedReader
 * SequenceInputStream顺序:把多个InputStream合并为一个InputStream
 * PushbackInputStream推回
 * 
 * socket学习
 * http://guji.is-programmer.com/posts/25181.html
 * 
 * @author LMC
 *
 */
public class PushSocketConfig {

	private static final String TAG = PushSocketConfig.class.getSimpleName();

	private String host = null;

	private int port;

	// 字节顺序
	private java.nio.ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

	// 连接超时
	private int connectTimeout = 3 * 1000;
	
	// 读超时，等于0时，为没有超时
	private int readTimeout = 3 * 1000;

	// 重连的间隔时间
	private long reconnectTime = 5 * 1000;

	// 心跳间隔时间
	private long heartbeatTime = 5 * 60 * 1000; // 5分钟

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public java.nio.ByteOrder getByteOrder() {
		return byteOrder;
	}

	public void setByteOrder(java.nio.ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public long getReconnectTime() {
		return reconnectTime;
	}

	public void setReconnectTime(long reconnectTime) {
		this.reconnectTime = reconnectTime;
	}

	public long getHeartbeatTime() {
		return heartbeatTime;
	}

	public void setHeartbeatTime(long heartbeatTime) {
		this.heartbeatTime = heartbeatTime;
	}

	public void print() {
		Log.e(TAG, "------------------ PushSocketConfig() start ------------------");
		Log.e(TAG, "getHost:"+getHost());
		Log.e(TAG, "getPort:"+getPort());
		Log.e(TAG, "getByteOrder:"+getByteOrder().toString());
		Log.e(TAG, "getConnectTimeout():"+getConnectTimeout());
		Log.e(TAG, "getReadTimeout:"+getReadTimeout());
		Log.e(TAG, "getReconnectTime:"+getReconnectTime());
		Log.e(TAG, "getHeartbeatTime():"+getHeartbeatTime());
		Log.e(TAG, "------------------ PushSocketConfig() end ------------------");
	}

}
