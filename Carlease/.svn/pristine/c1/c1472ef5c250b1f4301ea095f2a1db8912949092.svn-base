package com.tools.push;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;

import com.tools.content.pm.PermissionTool;
import com.tools.net.NetworkState;
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
 * IP没有开端口的情况是：
 * SocketChannel.open会等待。。。，等待多久？？？？
 * 
 * 
 * 连接超时的时间是多少？ --- 默认90秒
 * 读数据超时的时间是多少？
 * 
 * 
 * 使用例子：
 * 
 * 
 * 	private void pushService() {
		//						pushSocketChannel = new PushSocketChannel(PushSocketInfo.getHost(), PushSocketInfo.getPort());
		pushSocketChannel = new PushSocketChannel("192.168.80.33", 60000);
		if (pushSocketChannel != null) {
			pushSocketChannel.setReadTimeout(1 * 1000); // 设置为无超时
			pushSocketChannel.setReconnectTime(10 * 1000); // 重连间隔时间
			// push.setHeartbeatTime(5 * 60 * 1000); // 心跳时间
			pushSocketChannel.setHeartbeatTime(15 * 1000); // 心跳时间
			// 事件接口
			pushSocketChannel.setEventListener(new PushSocketChannel.IEventListener() {

				@Override
				public void onConnected() {
					Log.e(TAG, "onConnected():连接成功后，发送登陆包.");
					Log.e(TAG, "pushSocketChannel.byteOrder:"+pushSocketChannel.getByteOrder().toString());
					String msgBody = "{\"username\":\"minz\"}";
					Log.e(TAG, "msgBody len A:"+msgBody.length());
					Log.e(TAG, "msgBody len getBytes B:"+msgBody.getBytes().length);
					int capacity = PushSocketInfo.ITSMSG_HEADER_SIZE + msgBody.getBytes().length;
					Log.e(TAG, "onConnected():capacity:"+capacity);
					ByteBuffer buffer = ByteBuffer.allocate(capacity);
					// 长度
					buffer.putInt(capacity);
					// 命令
					buffer.putShort(PushSocketInfo.ITSMSG_LOGIN);
					// SeqNo
					buffer.putInt(0x00000000);
					// body
					buffer.put(msgBody.getBytes()); // 追加方式
					// 通过buffer.flip();这个语句，就能把buffer的当前位置更改为buffer缓冲区的第一个位置。 
					buffer.flip();
					Log.e(TAG, "onConnected():发送的数据:"+PushSocketChannel.bytes2HexString(buffer.array()));
					int count = pushSocketChannel.send(buffer);
					Log.e(TAG, "onConnected():发送数据成功的数量.count():"+count);
				}

				@Override 
				public void onReceived(int count, java.nio.ByteBuffer buffer) {
					Log.e(TAG, "onReceive():接收到的数量...count:"+count);
					if (count > 0 && buffer != null) {
						Log.e(TAG, "onReceive():接收到的数据:"+PushSocketChannel.bytes2HexString(buffer.array()));
						// 返回的是10个字节加上json字符串
						int CmdLen = buffer.getInt();
						short CmdID = buffer.getShort();
						int SeqNo = buffer.getInt();
						Log.e(TAG, "CmdLen:"+CmdLen+",CmdID:"+CmdID+",SeqNo:"+SeqNo);
						// 这里还要调试
						byte[] msgBodyBuffer = new byte[ CmdLen - PushSocketInfo.ITSMSG_HEADER_SIZE ];
						Log.e(TAG, "msgBodyBuffer len:"+msgBodyBuffer.length);
						buffer.get(msgBodyBuffer, 0, msgBodyBuffer.length);
						String msgBody = null;
						msgBody = new String(msgBodyBuffer);
						Log.e(TAG, "msgBody result AAA:"+msgBody);
						// 分析协议
						JSONObject json = JSON.parseObject(msgBody);
						if (json != null) {
							PushMessage pushBean = json.getObject(PushMessage.KEY, PushMessage.class);
							if (pushBean != null) {
								Log.e(TAG, "getAlert:"+pushBean.getAlert());
								Log.e(TAG, "getSound:"+pushBean.getSound());
								Log.e(TAG, "getBadge:"+pushBean.getBadge());
								// 分析字符串是哪个大类型
							}
						}else {
							Log.exception(TAG, new NullPointerException("json == null"));
						}
					} // end if
				}

				@Override
				public void onHeartbeat() {
					Log.e(TAG, "onHeartbeat():心跳事件......");
					int capacity = PushSocketInfo.ITSMSG_HEADER_SIZE;
					Log.e(TAG, "onHeartbeat():capacity:"+capacity);
					ByteBuffer buffer = ByteBuffer.allocate(capacity);
					// 长度
					buffer.putInt(capacity);
					// 命令
					buffer.putShort(PushSocketInfo.ITSMSG_TEST);
					// SeqNo
					buffer.putInt(0x00000000);
					// 通过buffer.flip();这个语句，就能把buffer的当前位置更改为buffer缓冲区的第一个位置。 
					buffer.flip();
					Log.e(TAG, "onHeartbeat():发送的数据:"+PushSocketChannel.bytes2HexString(buffer.array()));
					int count = pushSocketChannel.send(buffer); 
					Log.e(TAG, "onHeartbeat():发送数据成功的数量.count():"+count);
				}

			});

			// 连接
			pushSocketChannel.start();
			Log.e(TAG, "pushSocketChannel.start();");

		} // end push

	}
 * 
 * 
 * @author LMC
 *
 */
public class PushSocketChannel {

	private static final String TAG = PushSocketChannel.class.getSimpleName();
	private static final boolean DEBUG = true;

	// 缓冲区最大长度 --- 不限制大小
	public static final int BUFFER_MAX_LEN = 1024;

	protected String host;
	protected int port;

	//	protected Socket socket = null;

	// 读超时，等于0时，为没有超时
	protected int readTimeout = 3 * 1000;

	// 是否重连
	protected boolean isReconnect = true;

	// 重连的间隔时间
	protected long reconnectTime = 10 * 1000;

	// 心跳间隔时间
	protected long heartbeatTime = 5 * 60 * 1000; // 5分钟

	// 消息
	protected static final int MSG_HEARTBEAT = 1;
	protected static final int MSG_RECONNECT = 2;
	protected static final int MSG_READ = 3;

	protected OutputStream out = null;
	protected InputStream in = null;

	// 重连线程
	protected AbsThread reconnectThread = null;
	// 读线程
	protected AbsThread readThread = null;
	// 心跳线程
	protected AbsThread heartbeatThread = null;

	//	protected byte[] buffer = new byte[ BUFFER_MAX_LEN ];
	//	protected java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate( BUFFER_MAX_LEN );

	// 字节顺序
	protected java.nio.ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;

	// 信道选择器
	protected java.nio.channels.Selector selector = null;

	// 与服务器通信的信道
	protected java.nio.channels.SocketChannel socketChannel = null;

	// 是否已连接
	protected boolean isConnected = false;

	// 是否停止
	protected boolean isClosed = true;

	protected IEventListener eventListener = null;

	protected Context context = null;

	// 事件接口
	public interface IEventListener {
		void onConnected(); // 已连接事件
		void onReceived(int count, java.nio.ByteBuffer buffer); // 接收到数据包
		void onHeartbeat(); // 心跳事件
	}

	public PushSocketChannel(Context context, String host, int port) {
		init(context, host, port);
	}

	/**
	 * 初始化
	 */
	private void init(Context context, String host, int port) {
		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.INTERNET);
		this.context = context;
		this.host = host;
		this.port = port;
		this.isClosed = false;
		setByteOrder( ByteOrder.LITTLE_ENDIAN ); // 默认
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
	 * 设置Socket属性
	 */
	protected void setSocket(Socket socket) {
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
			//			if (DEBUG) Log.e(TAG, "readTimeout:"+socket.getSoTimeout());
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
		if (DEBUG) Log.e(TAG, "isReconnect:"+isReconnect);
		if (DEBUG) Log.e(TAG, "reconnectTime:"+reconnectTime);
		if (DEBUG) Log.e(TAG, "heartbeatTime:"+heartbeatTime);
		if (DEBUG) Log.e(TAG, "isInputShutdown:"+socket.isInputShutdown());
		if (DEBUG) Log.e(TAG, "isOutputShutdown:"+socket.isOutputShutdown());
		if (DEBUG) Log.e(TAG, "close:"+socket.isClosed()); // false
		if (DEBUG) Log.e(TAG, "isConnected:"+socket.isConnected()); // true
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_HEARTBEAT: // 心跳
				//				Log.e(TAG, "handler == MSG_HEARTBEAT.");
				//				if (connected == true && eventListener != null) {
				//					handler.post(heartbeatRunnable);
				//				}
				//				startHeartbeat();
				//				break;
				//			case MSG_RECONNECT: // 重连
				//				Log.e(TAG, "handler == MSG_RECONNECT.");
				//				//				startReconnect();
				//				break;
			case MSG_READ: // 读
				if (DEBUG) Log.e(TAG, "handler == MSG_READ.");
				startReadThread();
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 启动重连机制
	 */
	protected void startReconnect() {
		if (DEBUG) Log.e(TAG, "startReconnect()");

		reconnectThread = new AbsThread() {

			@Override
			public void run() {

				while( super.isCanceled() == false ) {
					if (isConnected == false) {
						// 连接
						isConnected = connect();
						if (isConnected) {
							// 设置Socket属性
							setSocket(socketChannel.socket());
							// 连接事件接口
							if (eventListener != null) {
								eventListener.onConnected();
							}
							// handler
							handler.sendEmptyMessage(MSG_READ);
						}
					}

					if (DEBUG) Log.e(TAG, "startReconnect():connectd:"+isConnected);

					if (DEBUG) {
						// 打印网络状态
						NetworkState networkStateCC = new NetworkState(context);
						networkStateCC.print( networkStateCC.getActiveNetworkInfo() );
					}

					if (DEBUG) Log.e(TAG, "startReconnect():start reconnectTime:"+reconnectTime);
					if ( sleepMayInterrupt(reconnectTime) == false ) {
						break;
					}

					if (DEBUG) {
						// 打印网络状态
						NetworkState networkStateCC = new NetworkState(context);
						networkStateCC.print( networkStateCC.getActiveNetworkInfo() );
					}

					if (DEBUG) Log.e(TAG, "startReconnect():end reconnectTime:"+reconnectTime);

				} // end while
			}

		};

		reconnectThread.start();
	}

	/**
	 * 停止重连机制
	 */
	protected void stopReconnect() {
		if(this.reconnectThread != null) {
			reconnectThread.cancel();
		}
	}

	/**
	 * 开始读数据线程
	 */
	//	protected void ___333_____startReadThread() {
	//		if (DEBUG) Log.e(TAG, "startReadThread()");
	//
	//		readThread = new AbsThread(context) {
	//
	//			@Override
	//			public void run() {
	//				// isCancel() == false
	//				if (DEBUG) Log.e(TAG, "start while");
	//
	//				Iterator<SelectionKey> iterator;
	//				SelectionKey key;
	//
	//				while (isCancel() == false) {
	//
	//					//					Iterator<SelectionKey> iterator;
	//					//					SelectionKey key;
	//					//					while (true) {
	//					// ...
	//
	//					iterator = selector.selectedKeys().iterator();
	//					if (iterator != null) {
	//						//						Log.e(TAG, "Take action...hasNext:"+iterator.hasNext());
	//					}
	//					while (iterator.hasNext()) {
	//						key = iterator.next();
	//						iterator.remove();
	//						Log.e(TAG, "Take action...hasNext:"+iterator.hasNext());
	//						// ...
	//						if (key.isValid() && key.isReadable()) {
	//							// Take action...
	//							Log.e(TAG, "Take action..hasNext .//.//");
	//							SocketChannel sc = (SocketChannel) key.channel();
	//							// ByteBuffer buffer = ByteBuffer.allocate(1024)
	//							buffer.clear(); // 清空数据
	//							int readCount = -1;
	//							try {
	//								readCount = sc.read(buffer);
	//							} catch (IOException e) {
	//								e.printStackTrace();
	//							} // 读不到，则为-1
	//							if (DEBUG) Log.e(TAG, "buffer readCount:"+readCount);
	//						}else{
	//							Log.e(TAG, "Take action...//.//  == false");
	//						}
	//					}
	//					//					}
	//					//					
	//					//					if (DEBUG) Log.e(TAG, "aaaaaaaaaaaaaaaaaaa");
	//					//					try {
	//					//						if (DEBUG) Log.e(TAG, "bbbbbbbbbbbbbbbbbb");
	//					//						if (selector.select() > 0) { // 这里可以会阻塞
	//					//							if (DEBUG) Log.e(TAG, "selector.select() > 0");
	//					////							printSocketChannel(socketChannel, selector);
	//					//							// 遍历每个有可用IO操作Channel对应的SelectionKey
	//					//							for (SelectionKey sk : selector.selectedKeys()) {
	//					//								// 如果该SelectionKey对应的Channel中有可读的数据
	//					////								printSelectionKey(sk);
	//					//								if (sk.isReadable()) {
	//					//									// 使用NIO读取Channel中的数据
	//					//									SocketChannel sc = (SocketChannel) sk.channel();
	//					//									// ByteBuffer buffer = ByteBuffer.allocate(1024)
	//					//									buffer.clear(); // 清空数据
	//					//									int readCount = sc.read(buffer); // 读不到，则为-1
	//					//									if (DEBUG) Log.e(TAG, "buffer readCount:"+readCount);
	//					//									Log.e(TAG, "buffer readCount:"+readCount);
	//					//									// 通过buffer.flip();这个语句，就能把buffer的当前位置更改为buffer缓冲区的第一个位置。 
	//					//									buffer.flip();
	//					//									if (readCount > 0 && eventListener != null) {
	//					//										eventListener.onReceived(readCount, buffer);
	//					//									}
	//					//									// 将字节转化为为UTF-16的字符串
	//					//									//									String receivedString = Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
	//					//									// 控制台打印出来
	//					//									//									if (DEBUG) Log.e(TAG, "接收到来自服务器"+sc.socket().getRemoteSocketAddress()+"的信息:"+receivedString);
	//					//									// 为下一次读取作准备
	//					////									sk.interestOps(SelectionKey.OP_READ);
	//					//								}
	//					//								// 删除正在处理的SelectionKey
	//					//								selector.selectedKeys().remove(sk);
	//					//							} // end for
	//					//						}else {
	//					//							if (DEBUG) Log.e(TAG, "selector.select() <= 0");
	//					//							printSocketChannel(socketChannel, selector);
	//					//						} // end if
	//					//						if (DEBUG) Log.e(TAG, "cccccccccccccccccc");
	//					//					} catch (IOException e) {
	//					//						e.printStackTrace();
	//					//					}
	//					// Thread.sleep.....
	//					//					if (DEBUG) Log.e(TAG, "start sleepExt");
	//					//					if(sleepExt(100) == false) {
	//					//						break;
	//					//					}
	//					//					if (DEBUG) Log.e(TAG, "end sleepExt");
	//				} // end while
	//				if (DEBUG) Log.e(TAG, "end while");
	//			}
	//
	//		};
	//		//
	//		readThread.start();
	//	}

	/**
	 * 开始启动心跳线程
	 */
	protected void startHeartbeat() {
		if (DEBUG) Log.e(TAG, "startHeartbeat()");
		//		handler.sendEmptyMessageDelayed(MSG_HEARTBEAT, heartbeatTime);
	
		heartbeatThread = new AbsThread() {
	
			@Override
			public void run() {
	
				while (super.isCanceled() == false) {
					if ( sleepMayInterrupt(heartbeatTime) == false ) {
						break;
					}
					if (isConnected == true && eventListener != null) {
						eventListener.onHeartbeat();
					}
				} // end while
				if (DEBUG) Log.e(TAG, "startHeartbeat():end while");
	
			} // end run
	
		};
	
		heartbeatThread.start();
	
	}

	/**
	 * 停止心跳
	 */
	protected void stopHeartbeat() {
		//		handler.removeMessages(MSG_HEARTBEAT);
		if (heartbeatThread != null) {
			heartbeatThread.cancel();
		}
	}

	/**
	 * 开始启动读线程
	 */
	protected void startReadThread() {
		if (DEBUG) Log.e(TAG, "startReadThread()");

		readThread = new AbsThread() {

			@Override
			public void run() {

				if (DEBUG) Log.e(TAG, "startReadThread():start while");
				if (DEBUG) Log.e(TAG, "startReadThread():super.isCanceled():"+super.isCanceled());

				while (super.isCanceled() == false) {
					if (DEBUG) Log.e(TAG, "while() --- start ----");
					try {
						//						if (DEBUG) Log.e(TAG, "bbbbbbbbbbbbbbbbbb");
						//						int countSelect = 0;
						//						if (selector != null) {
						//							countSelect = selector.select();
						//						}
						//						if (DEBUG) Log.e(TAG, "countSelect:"+countSelect);
						if (selector != null) {
							int countSelect = 0;

							if (DEBUG) Log.e(TAG, "here -1-1-1-1-1-1-1-1-1-1-1-1-1-1.");

							if (DEBUG) {
								// 打印网络状态
								NetworkState networkStateCC = new NetworkState(context);
								networkStateCC.print( networkStateCC.getActiveNetworkInfo() );
							}

							if (DEBUG) Log.e(TAG, "开始执行。。。。select.........");
							countSelect = selector.select(); // 这里可以会阻塞
							if (DEBUG) Log.e(TAG, "select执行完毕。。。。select.........");
							if (DEBUG) Log.e(TAG, "selector.select():countSelect:"+countSelect);

							if (DEBUG) Log.e(TAG, "here 00000000000000000000000000000.");

							if (DEBUG) {
								// 打印网络状态
								NetworkState networkState = new NetworkState(context);
								networkState.print( networkState.getActiveNetworkInfo() );
							}

							printSocketChannel(socketChannel);
							printSelector(selector);
							if (socketChannel != null) {
								printSocket(socketChannel.socket());
							}

							if (countSelect > 0) {
								if (DEBUG) Log.e(TAG, "selector.select() > 0 countSelect:"+countSelect);

								if (DEBUG) {
									// 打印网络状态
									NetworkState networkState2 = new NetworkState(context);
									networkState2.print( networkState2.getActiveNetworkInfo() );
								}

								//								if (DEBUG) Log.e(TAG, "selector.select() > 0 __ count:"+selector.select());
								//								Selector sel = selector.wakeup();
								//								if (DEBUG) Log.e(TAG, "selector.select() > 0:isOpen:"+sel.isOpen());

								if (DEBUG) Log.e(TAG, "here 111111111111111111111111111.");

								printSocketChannel(socketChannel);
								printSelector(selector);
								if (socketChannel != null) {
									printSocket(socketChannel.socket());
								}

								// 遍历每个有可用IO操作Channel对应的SelectionKey
								for (SelectionKey sk : selector.selectedKeys()) {
									if (DEBUG) Log.e(TAG, "for for for for for selector.selectedKeys()");
									// 如果该SelectionKey对应的Channel中有可读的数据
									
									printSelectionKey(sk);
									if (sk.isReadable()) {
										// 使用NIO读取Channel中的数据
										SocketChannel sc = (SocketChannel) sk.channel();

										//										ByteBuffer buffer = ByteBuffer.allocate( BUFFER_MAX_LEN );
										//										buffer.order( getByteOrder() );
										//										buffer.clear(); // 清空数据

										if (DEBUG) Log.e(TAG, "here 2222222222222222222222222222222222.");

										if (DEBUG) {
											// 打印网络状态
											NetworkState networkState3 = new NetworkState(context);
											networkState3.print( networkState3.getActiveNetworkInfo() );
										}

										//										int readCount = sc.read(buffer);
										//										ByteBuffer[] buffer = null;
										//										int readCount = sc.read(buffer);
										//										Log.e(TAG, "buffer readCount:"+readCount);
										//										if (readCount == BUFFER_MAX_LEN) {
										//											Log.e(TAG, "readCount == BUFFER_MAX_LEN");
										//											readCount = 0;
										//											buffer.flip();
										//											readCount = sc.read(buffer);
										//											Log.e(TAG, "buffer readCount 222:"+readCount);
										//											if (readCount == BUFFER_MAX_LEN) {
										//												Log.e(TAG, "readCount == BUFFER_MAX_LEN");
										//												readCount = 0;
										//												buffer.flip();
										//												readCount = sc.read(buffer);
										//												Log.e(TAG, "buffer readCount 333:"+readCount);
										//											} else {
										//												Log.e(TAG, "readCount != BUFFER_MAX_LEN 333");
										//											}
										//										} else {
										//											Log.e(TAG, "readCount != BUFFER_MAX_LEN 222");
										//										}

										int readCount = 0;
										// 读缓冲区全部字节
										Pair<Integer, java.nio.ByteBuffer> pair = readByteBufferAll(sc);
										if (pair != null) {

											readCount = pair.first;
											if (DEBUG) Log.e(TAG, "pair.first:"+pair.first);
											if (DEBUG) Log.e(TAG, "pair.first readCount:"+readCount);

											// TODO 读不到，则为-1  ---- 如果readCount == -1，说明已经跟服务器断开了。
											if (readCount <= -1) {
												if (DEBUG) {
													// 打印网络状态
													NetworkState networkState4 = new NetworkState(context);
													networkState4.print( networkState4.getActiveNetworkInfo() );
												}
												// 关闭读线程startReadThread()
												Log.e(TAG, "关闭读线程startReadThread()");
												isConnected = false;
												super.cancel();
											}

											// 通过buffer.flip();这个语句，就能把buffer的当前位置更改为buffer缓冲区的第一个位置。
											// TODO flip()是必须加上的
											//	buffer.flip();
											//	if (DEBUG) Log.e(TAG, "................buffer.flip();");

											if (readCount > 0 && eventListener != null) {
												if (DEBUG) Log.e(TAG, "execute...........onReceived()--------- start ----------");
												eventListener.onReceived(readCount, pair.second);
												if (DEBUG) Log.e(TAG, "execute...........onReceived()--------- end ----------");
											}

										}

										// 将字节转化为为UTF-16的字符串
										//									String receivedString = Charset.forName("UTF-8").newDecoder().decode(buffer).toString();
										// 控制台打印出来
										//									if (DEBUG) Log.e(TAG, "接收到来自服务器"+sc.socket().getRemoteSocketAddress()+"的信息:"+receivedString);
										// 为下一次读取作准备
										if (DEBUG) Log.e(TAG, "interestOps (SelectionKey.OP_READ) ............");
										sk.interestOps(SelectionKey.OP_READ);
									} // end if
									// 删除正在处理的SelectionKey
									if (DEBUG) Log.e(TAG, "selectedKeys().remove(sk); ............");
									selector.selectedKeys().remove(sk);
								} // end for

								if (DEBUG) Log.e(TAG, "here 333333333333333333333333333333333333333.");

								if (DEBUG) {
									// 打印网络状态
									NetworkState networkStateRrr = new NetworkState(context);
									networkStateRrr.print( networkStateRrr.getActiveNetworkInfo() );
								}

							}else {
								if (DEBUG) Log.e(TAG, "selector.select() <= 0");

								printSocketChannel(socketChannel);
								printSelector(selector);
								if (socketChannel != null) {
									printSocket(socketChannel.socket());
								}

								// set connect
								isConnected = false;
								if (DEBUG) Log.e(TAG, "selector.select() <= 0 connected:"+isConnected);
								// 出来
								break;
								/*
------ printSocketChannel() start ------
printSocketChannel():socketChannel-->isBlocking:false
printSocketChannel():socketChannel-->isConnected:true
printSocketChannel():socketChannel-->isConnectionPending:false
printSocketChannel():socketChannel-->isOpen:true
printSocketChannel():socketChannel-->isRegistered:true
printSocketChannel():selector--->isOpen:true
------ printSocketChannel() end ------ */
							} // end if
						}
						//						if (DEBUG) Log.e(TAG, "cccccccccccccccccc");
					} catch (Exception e) {
						e.printStackTrace();
						if (DEBUG) Log.e(TAG, "here thorws Exception 44444444444444444444444444444444444.");

						if (DEBUG) {
							// 打印网络状态
							NetworkState networkState = new NetworkState(context);
							networkState.print( networkState.getActiveNetworkInfo() );
						}

						// 关闭读线程startReadThread()
						Log.e(TAG, "关闭读线程startReadThread()");
						isConnected = false;
						super.cancel();

					}
					// Thread.sleep.....
					//					if (DEBUG) Log.e(TAG, "start sleepExt");
					//					if(sleepExt(200) == false) {
					//						break;
					//					}
					//					if (DEBUG) Log.e(TAG, "end sleepExt");

					if (DEBUG) Log.e(TAG, "here 55555555555555555555555555555555555.");

					if (DEBUG) {
						// 打印网络状态
						NetworkState networkState = new NetworkState(context);
						networkState.print( networkState.getActiveNetworkInfo() );
					}

					if (DEBUG) Log.e(TAG, "while() --- end ----");
				} // end while
				if (DEBUG) Log.e(TAG, "startReadThread():end while");
			}

		};

		readThread.start();
	}

	/**
	 * 停止读线程
	 */
	protected void stopReadThread() {
		if (readThread != null) {
			readThread.cancel();
		}
	}

	/**
	 * 读缓冲区全部字节
	 * 
	 * @param sc
	 * @return
	 * @throws IOException 
	 */
	protected Pair<Integer, java.nio.ByteBuffer> readByteBufferAll(SocketChannel sc) throws Exception {

		if (DEBUG) Log.e(TAG, "readBufferByteAll()");
		if (sc == null) {
			return null;
		}

		// 列表缓冲区
		List<java.nio.ByteBuffer> listBuffer = new ArrayList<java.nio.ByteBuffer>();

		// 读缓冲区
		java.nio.ByteBuffer readBuffer = java.nio.ByteBuffer.allocate( BUFFER_MAX_LEN );
		readBuffer.order( getByteOrder() ); // 字节顺序
		readBuffer.flip();
		readBuffer.clear();

		// 读到的大小
		int readCount = 0;

		// 共计，将读的全部累加起来
		int totalCount = 0;

		//		try {
		
		if (DEBUG) Log.e(TAG, "readBufferByteAll()1111");
		printSocketChannel(sc);
		if (DEBUG) Log.e(TAG, "readBufferByteAll()2222");
		
		int n = 0;
		while ( (readCount = sc.read(readBuffer)) > 0 ) {

			n++;
			
			// 累加
			totalCount += readCount;

			// 放在list列表里的buffer
			java.nio.ByteBuffer appendBuffer = java.nio.ByteBuffer.allocate( readCount );
			appendBuffer.order( getByteOrder() ); // 字节顺序
			appendBuffer.clear();
			// put buffer
			appendBuffer.put(readBuffer.array(), 0, readCount);
			// 将buffer加入list列表
			listBuffer.add(appendBuffer);

			if (DEBUG) Log.e(TAG, "readBufferByteAll(while):listBuffer len:"+listBuffer.size());

			if (DEBUG) Log.e(TAG, "readBufferByteAll(while):readCount:"+readCount);
			if (DEBUG) Log.e(TAG, "readBufferByteAll(while):totalCount:"+totalCount);
			if (DEBUG) Log.e(TAG, "readBufferByteAll(while):接收到的数据readBuffer:"+PushSocketChannel.bytes2HexString(readBuffer.array()));
			if (DEBUG) Log.e(TAG, "readBufferByteAll(while):接收到的数据appendBuffer:"+PushSocketChannel.bytes2HexString(appendBuffer.array()));

			readCount = 0;
			readBuffer.flip(); // 设置到第一个位置
			readBuffer.clear();
		}

		if (DEBUG) Log.e(TAG, "readBufferByteAll(while Out break..):n:"+n);
		if (DEBUG) Log.e(TAG, "readBufferByteAll(while Out break..):totalCount:"+totalCount);
		if (DEBUG) Log.e(TAG, "readBufferByteAll(while Out break..):readCount:"+readCount); // == 0
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}

		Pair<Integer, java.nio.ByteBuffer> pair = null;

		if (readCount <= -1) {
			// 出错，则将出错码传过去
			java.nio.ByteBuffer resultBuffer = java.nio.ByteBuffer.allocate( 0 );
			//			resultBuffer.order( getByteOrder() ); // 字节顺序
			pair = new Pair<Integer, java.nio.ByteBuffer>(readCount, resultBuffer);
		} else {

			// 返回的缓冲区  totalCount <= -1 会出错  == 0 或者  > 0 则不会出错
			java.nio.ByteBuffer resultBuffer = java.nio.ByteBuffer.allocate( totalCount );
			resultBuffer.order( getByteOrder() ); // 字节顺序

			if (DEBUG) Log.e(TAG, "readBufferByteAll(END):listBuffer len:"+listBuffer.size());
			// 将list缓冲区转成一个单独的buffer
			for ( java.nio.ByteBuffer tmp : listBuffer ) {
				if (tmp != null) {
					if (DEBUG) Log.e(TAG, "readBufferByteAll(END): put tmp...");
					if (DEBUG) Log.e(TAG, "readBufferByteAll(END): put tmp 内容:"+PushSocketChannel.bytes2HexString(tmp.array()));
					resultBuffer.put( tmp.array() );
					if (DEBUG) Log.e(TAG, "readBufferByteAll(END):接收到的数据 show ...  resultBuffer:"+PushSocketChannel.bytes2HexString(resultBuffer.array()));
				}
			}

			if (DEBUG) Log.e(TAG, "readBufferByteAll(END):totalCount:"+totalCount);
			if (DEBUG) Log.e(TAG, "readBufferByteAll(END):resultBuffer.array().length:"+resultBuffer.array().length);
			if (DEBUG) Log.e(TAG, "readBufferByteAll(END):接收到的数据resultBuffer:"+PushSocketChannel.bytes2HexString(resultBuffer.array()));

			// 创建一对
			// (pair第一个为读的参数) 结果可能有三种：1）-1；2）为0；3）共读取数据的累加
			// == -1 时，表示跟服务器断开
			//			Pair<Integer, java.nio.ByteBuffer> pair = new Pair<Integer, java.nio.ByteBuffer>(totalCount, resultBuffer);
			pair = new Pair<Integer, java.nio.ByteBuffer>(totalCount, resultBuffer);
		}

		return pair;
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
	 * 设置字节网络顺序
	 */
	public void setByteOrder(java.nio.ByteOrder order) {
		//		if (order != null && buffer != null) {
		//			buffer.order(order);
		//		}
		this.byteOrder = order;
	}

	/**
	 * 获取字节网络顺序
	 * 
	 * @return
	 */
	public java.nio.ByteOrder getByteOrder() {
		//		return buffer.order();
		return this.byteOrder;
	}

	/**
	 * 设置是否重连
	 * 
	 * @return
	 */
	public void setReconnectTime(int reconnectTime) {
		this.reconnectTime = reconnectTime;
	}

	/**
	 * 测试发送
	 */
	public static void sendTest() {
		//		Log.e(TAG, "sendTest():连接成功后，发送登陆包.");
		//		String msgBody = "{\"username\":\"minz\"}";
		//		Log.e(TAG, "msgBody len A:"+msgBody.length());
		//		Log.e(TAG, "msgBody len getBytes B:"+msgBody.getBytes().length);
		//		int capacity = PushSocketInfo.ITSMSG_HEADER_SIZE + msgBody.getBytes().length;
		//		Log.e(TAG, "capacity:"+capacity);
		//		ByteBuffer buffer = ByteBuffer.allocate(capacity + 2);
		//		// 长度
		//		buffer.putInt(capacity);
		//		// 命令
		//		buffer.putShort(PushSocketInfo.ITSMSG_LOGIN);
		//		// SeqNo
		//		buffer.putInt(0x00000000);
		//		Log.e(TAG, "string hex 1111:"+bytes2HexString(buffer.array()));
		//		// body
		//		buffer.put(msgBody.getBytes()); // 追加方式
		//		//		ByteBuffer bufferBody = ByteBuffer.wrap(msgBody.getBytes());
		//		//		buffer.put(bufferBody);
		//		buffer.flip(); // 要加上的
		//		//		buffer.putInt(0x00009999); // 追加方式
		//		Log.e(TAG, "sendTest():发送的数据:"+bytes2HexString(buffer.array()));
		//		int count = -1;
		//		try {
		//			count = socketChannel.write(buffer);
		//			//			socketChannel.socket().getOutputStream().flush();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}
		//		Log.e(TAG, "sendTest():发送数据成功的数量.count():"+count);
	}

	/**
	 * 开始
	 * 
	 * @return
	 */
	public void start() {
		if ( isReconnect() ) {
			// 启动重连机制
			startReconnect();
		}
		// 启动心跳
		startHeartbeat();
	}

	/**
	 * 连接
	 * 
	 * @return
	 */
	private boolean connect() {
		// 初始化 --- Socket
		this.isConnected = false;
		try {
			if (DEBUG) Log.e(TAG, "connect():111111111111111111");
			// 打开监听信道
			// 会阻塞主线程
			// TODO 关闭旧的socketChannel，否则会产生java.net.SocketException: Too many open files
			// 关闭SocketChannel
			closeSocketChannel( socketChannel );
			// 关闭Selector
			closeSelector( selector );
			// 创建
			if (DEBUG) Log.e(TAG, "connect():host:"+host+",port:"+port);
			socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
			if (DEBUG) Log.e(TAG, "connect():22222222222222222222");
			if (socketChannel != null) {
				
				socketChannel.finishConnect();
				
				// 设置为非阻塞模式
				socketChannel.configureBlocking(false);
				// 打开选择器
				selector = Selector.open();
				// 注册选择器到信道
				socketChannel.register(selector, SelectionKey.OP_READ);
				if (isConnected()) {
					isConnected = true;
				}
			}
		} catch (java.net.SocketTimeoutException e) {
			e.printStackTrace();
			if (DEBUG) Log.e(TAG, "connect():printf SocketTimeoutException.. ------ ....");
		} catch (IOException e) {
			e.printStackTrace();
			if (DEBUG) Log.e(TAG, "connect():printf IOException.. ------ ....");
		} catch (Exception e) {
			e.printStackTrace();
			if (DEBUG) Log.e(TAG, "connect():printf Exception.. ------ ....");
		} catch (java.lang.AssertionError e) {
			e.printStackTrace();
			if (DEBUG) Log.e(TAG, "connect():printf java.lang.AssertionError.. ------ ....");
		}
		return isConnected;
	}

	/**
	 * TODO 一定要在连接之后，向服务端发一个打招呼消息
	 * 
	 * @return
	 */
	private boolean sayHello(SocketChannel socketChannel) {
		boolean sayHello = false;
		if (socketChannel != null) {
			// say hello
			try {
				ByteBuffer ss = ByteBuffer.allocate(10);
				ss.putInt(0);
				int count = socketChannel.write(ss);
				//								socketChannel.socket().getOutputStream().flush();
				Log.e(TAG, "sayHello():write___count:"+count);
				if (count > 0) {
					sayHello = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				sayHello = false;
			}
		}
		Log.e(TAG, "sayHello():sayHello:"+sayHello);
		return sayHello;
	}

	/**
	 * 判断是否连接
	 * 
	 * @return
	 */
	public boolean isConnected() {
		boolean connect = false;
		if (socketChannel != null) {
			connect = socketChannel.isConnected();
		}
		if (DEBUG) Log.e(TAG, "isConnected():connect:"+connect);
		return connect;
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
	 * 发送字节
	 * 
	 * @param bytes
	 * @return
	 */
	public int send(byte[] bytes) {
		if (bytes == null) {
			return -1;
		}
		return send(java.nio.ByteBuffer.wrap(bytes));
	}

	/**
	 * 发送
	 * 
	 * @param buffer
	 * @return
	 */
	public int send(java.nio.ByteBuffer buffer) {
		int count = -1;
		if (socketChannel != null && buffer != null) {
			try {
				count = socketChannel.write(buffer);
				//				socketChannel.socket().getOutputStream().flush();
				if (DEBUG) Log.e(TAG, "send:write___:count:"+count);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return count;
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
	 * push是否停止或者关闭，不是socket是否停止
	 * 
	 * @return
	 */
	public boolean isClosed() {
		//		boolean closed = false;
		//		if (socketChannel != null) {
		//			closed = socketChannel.socket().isClosed();
		//		}
		//		if (DEBUG) Log.e(TAG, "isClosed():isClosed:"+closed);
		//		return closed;
		return this.isClosed;
	}

	/**
	 * 关闭socket
	 * 
	 * @return
	 */
	public void close() {
		Log.e(TAG, "close()");
		stopHeartbeat();
		stopReconnect();
		stopReadThread();
		// 关闭Selector
		closeSelector( selector );
		// 关闭SocketChannel
		closeSocketChannel( socketChannel );
		isClosed = true;
	}

	/**
	 * 关闭SocketChannel
	 * 
	 * @param socketChannel
	 */
	protected void closeSocketChannel(SocketChannel socketChannel) {
		Log.e(TAG, "closeSocketChannel()");
		if (socketChannel == null) {
			return;
		}
		try {
//			socketChannel.finishConnect();
			socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭Selector
	 * 
	 * @param selector
	 */
	protected void closeSelector(Selector selector) {
		Log.e(TAG, "closeSelector()");
		if (selector == null) {
			return;
		}
		
		try {
			// for close
			for (SelectionKey sk : selector.selectedKeys()) {
				if (sk != null) {
					Log.e(TAG, "closeSelector()--->sk.cancel()");
					sk.cancel();
				}
			}
			// selector close
			selector.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

		byte[] hex = "0123456789ABCDEF".getBytes();
		byte[] buffer = new byte[2 * bytes.length];
		for (int i = 0; i < bytes.length; i++) {
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

	/**
	 * 打印状态
	 */
	public void printSocketChannel(java.nio.channels.SocketChannel socketChannel) {
		if (DEBUG) Log.e(TAG, "------ printSocketChannel() start ------");
		if (socketChannel != null) {
			if (DEBUG) Log.e(TAG, "printSocketChannel():socketChannel--->isBlocking:"+socketChannel.isBlocking());
			if (DEBUG) Log.e(TAG, "printSocketChannel():socketChannel--->isConnected:"+socketChannel.isConnected());
			if (DEBUG) Log.e(TAG, "printSocketChannel():socketChannel--->isConnectionPending:"+socketChannel.isConnectionPending());
			if (DEBUG) Log.e(TAG, "printSocketChannel():socketChannel--->isOpen:"+socketChannel.isOpen());
			if (DEBUG) Log.e(TAG, "printSocketChannel():socketChannel--->isRegistered:"+socketChannel.isRegistered());
		}
		if (DEBUG) Log.e(TAG, "------ printSocketChannel() end ------");
	}

	/**
	 * 打印状态
	 */
	public void printSelector(java.nio.channels.Selector selector) {
		if (DEBUG) Log.e(TAG, "------ printSelector() start ------");
		if (selector != null) {
			if (DEBUG) Log.e(TAG, "printSelector():selector--->isOpen:"+selector.isOpen());
		}
		if (DEBUG) Log.e(TAG, "------ printSelector() end ------");
	}

	/**
	 * 打印状态
	 */
	public void printSelectionKey(java.nio.channels.SelectionKey selectionKey) {
		if (DEBUG) Log.e(TAG, "------ printSelectionKey() start ------");
		if (selectionKey != null) {
			if (DEBUG) Log.e(TAG, "printSelectionKey():selectionKey--->isAcceptable:"+selectionKey.isAcceptable());
			if (DEBUG) Log.e(TAG, "printSelectionKey():selectionKey--->isConnectable:"+selectionKey.isConnectable());
			if (DEBUG) Log.e(TAG, "printSelectionKey():selectionKey--->isReadable:"+selectionKey.isReadable());
			if (DEBUG) Log.e(TAG, "printSelectionKey():selectionKey--->isValid:"+selectionKey.isValid());
			if (DEBUG) Log.e(TAG, "printSelectionKey():selectionKey--->isWritable:"+selectionKey.isWritable());
		}
		if (DEBUG) Log.e(TAG, "------ printSelectionKey() end ------");
	}

	/**
	 * 打印状态
	 */
	public void printSocket(Socket socket) {
		if (DEBUG) Log.e(TAG, "------ printSocket() start ------");
		if (socket != null) {
			try {
				if (DEBUG) Log.e(TAG, "printSocket():socket--->getSoTimeout:"+socket.getSoTimeout());
				if (DEBUG) Log.e(TAG, "printSocket():socket--->getKeepAlive:"+socket.getKeepAlive());
				if (DEBUG) Log.e(TAG, "printSocket():socket--->getLocalPort:"+socket.getLocalPort());
				if (DEBUG) Log.e(TAG, "printSocket():socket--->getPort:"+socket.getPort());
				if (DEBUG) Log.e(TAG, "printSocket():socket--->getSoLinger:"+socket.getSoLinger());
				if (DEBUG) Log.e(TAG, "printSocket():socket--->getOOBInline:"+socket.getOOBInline());
				if (DEBUG) Log.e(TAG, "printSocket():socket--->getTcpNoDelay:"+socket.getTcpNoDelay());
				if (DEBUG) Log.e(TAG, "printSocket():socket--->isBound:"+socket.isBound());
				if (DEBUG) Log.e(TAG, "printSocket():socket--->isClosed:"+socket.isClosed());
				if (DEBUG) Log.e(TAG, "printSocket():socket--->isConnected:"+socket.isConnected());
				if (DEBUG) Log.e(TAG, "printSocket():socket--->isInputShutdown:"+socket.isInputShutdown());
				if (DEBUG) Log.e(TAG, "printSocket():socket--->isOutputShutdown:"+socket.isOutputShutdown());
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		if (DEBUG) Log.e(TAG, "------ printSocket() end ------");
	}

}