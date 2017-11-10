package com.tools.push;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hst.Carlease.R;
import com.tools.app.AbsUI2;
import com.tools.app.AppInfo;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.util.Log;

/**
 * 测试PushSocket界面
 * 
 * @authora
 *
 */
public class TestPushSocketUI extends AbsUI2 {

	private static final String TAG = TestPushSocketUI.class.getSimpleName();
	private static final boolean D = false; // 是否允许打印LOG
	private static final boolean S = false; // 是否允许模拟数据

	public static final short ITSMSG_LOGIN = 0x0001; // 客户端登录
	public static final int ITSMSG_LOGIN_RESP = 0x8001;// 客户端登录响应  // TODO 这个是ITS系统一直在用的
	public static final short ITSMSG_TEST = 0x1002; // 链路测试
	public static final int ITSMSG_TEST_RESP = 0x8002; // 链路测试响应

	// 命令ID
	public static final short ITSMSG_OBD_PUSH_MESSAGE = 0x1031; // OBD消息推送 4145
	public static final char ITSMSG_OBD_PUSH_MESSAGE_RESP = 0x8031; // OBD消息推送响应
	
	public static final int ITSMSG_HEADER_SIZE = 10; // 包头大小
	
	private String host = "carlife.wisdom-gps.com";
	private int port = 2295;
	
	private Button button_start = null;
	private Button button_close = null;
	private Button button_isInline = null;
	private Button button_send = null;

	private Button button_isConnected = null;
	private Button button_printSocket = null;
	private Button button_stopReadTask = null;

	private Button button_closeSocket = null;
	
	private Button button_wakeLock_acquire = null;
	private Button button_wakeLock_isHeld = null;
	private Button button_wakeLock_release = null;

	protected PushSocket pushSocket = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.tools_ui_test_push_socket);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		button_start = (Button) findViewById(R.id.button_start);
		button_close = (Button) findViewById(R.id.button_close);
		button_isInline = (Button) findViewById(R.id.button_isInline);
		button_send = (Button) findViewById(R.id.button_send);

		button_isConnected = (Button) findViewById(R.id.button_isConnected);
		button_printSocket = (Button) findViewById(R.id.button_printSocket);
		button_stopReadTask = (Button) findViewById(R.id.button_stopReadTask);

		button_closeSocket = (Button) findViewById(R.id.button_closeSocket);
		
		button_wakeLock_acquire = (Button) findViewById(R.id.button_wakeLock_acquire);
		button_wakeLock_isHeld = (Button) findViewById(R.id.button_wakeLock_isHeld);
		button_wakeLock_release = (Button) findViewById(R.id.button_wakeLock_release);
	}

	@Override
	protected void initControlEvent() {
		button_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pushSocket == null) {
					initPushSocket();
				}
				// 连接
				pushSocket.start();
			}

		});

		button_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pushSocket != null) {
					pushSocket.close();
				}
			}

		});

		button_isInline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pushSocket != null) {
					pushSocket.isInline();
				}
			}

		});

		button_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pushSocket != null) {
					byte[] bytes = new byte[10];
					bytes[0] = 0x01;
					bytes[1] = 0x02;
					bytes[2] = 0x03;
					pushSocket.send(bytes);
				}
			}

		});

		button_isConnected.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pushSocket != null) {
					pushSocket.isConnected();
				}
			}

		});

		button_printSocket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pushSocket != null) {
					pushSocket.printSocket( pushSocket.getSocket() );
				}
			}

		});

		button_stopReadTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pushSocket != null) {
//					pushSocket.stopReadTask();
				}
			}

		});

		button_closeSocket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pushSocket != null) {
//					pushSocket.closeSocket(pushSocket.getSocket());
				}
			}

		});
		
		button_wakeLock_acquire.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}

		});
		
		button_wakeLock_isHeld.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}

		});
		
		button_wakeLock_release.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}

		});

	}

	@Override
	protected void initMember() {

	}

	@Override
	public void onAttachedToWindow() {

		super.onAttachedToWindow();
	}

	private void initPushSocket() {

		PushSocketConfig pushSocketConfig = new PushSocketConfig();
		pushSocketConfig.setHost(host); // 域名
		pushSocketConfig.setPort(port); // 端口
		pushSocketConfig.setConnectTimeout( 10 * 1000 ); // 设置连接超时  （TODO 没验证）
		//		pushSocketConfig.setReadTimeout( 2 * 60 * 1000 ); // 设置读超时间(要小于心跳时间) --- 这个有BUG，建议设置为0
		pushSocketConfig.setReadTimeout( 0 ); // 设置读超时间(要小于心跳时间)
		pushSocketConfig.setReconnectTime( 10 * 1000 ); // 重连间隔时间
		// 要比服务端时间少些，微信为5分钟，这里设置为2分钟
		//		pushSocketConfig.setHeartbeatTime( (5 * 60 * 1000) - (60 * 1000)); // 心跳间隔时间
		pushSocketConfig.setHeartbeatTime( (2 * 60 * 1000) ); // 心跳间隔时间
		//		pushSocketConfig.setHeartbeatTime( (30 * 1000) ); // 心跳间隔时间
		//		pushSocketConfig.setHeartbeatTime( (1 * 20 * 1000) ); // 心跳间隔时间
		pushSocketConfig.setByteOrder( ByteOrder.LITTLE_ENDIAN ); // 设置字节顺序

		pushSocket = new PushSocket(context, pushSocketConfig);
		if (pushSocket != null) {

			// 事件接口
			pushSocket.setEventListener(new PushSocket.IEventListener() {

				@Override
				public void onConnected() {
					if (D) Log.e(TAG, "onConnected():...start...");
					if (D) Log.e(TAG, "onConnected():已连接成功，下面发送登陆包。");
					if (D) Log.e(TAG, "pushSocketChannel.byteOrder:"+pushSocket.getPushSocketConfig().getByteOrder().toString());
					//					if (DEBUG) Log.e(TAG, "SQLiteSingle.getInstance().isOpen:"+SQLiteSingle.getInstance().isOpen());
					//					SQLiteSingle.getInstance().printTable(LoginInfo.class);
					com.google.gson.JsonObject json = new com.google.gson.JsonObject();
					String userName = "pabc";
					// 登录推送服务器，加入密码认证。
					String password = "pabc";
					if (D) Log.e(TAG, "userName:"+userName);
					if (D) Log.e(TAG, "password:"+password);
					json.addProperty("username", userName);
					json.addProperty("password", password);
					// 加入网络类型，是3G还是WF
					json.addProperty("NetworkType", new NetworkState(context).getTypeString());
					json.addProperty("version", new AppInfo(context).getVersion());
					//					json.put("version", "1.1");
					//					json.put("version", "1.1");
					//					String msgBody = "{\"username\":\"minz\","version":"1.1"}";
					String msgBody = GJson.toJsonString(json);
					if (D) Log.e(TAG, "msgBody--->string:"+msgBody);
					if (D) Log.e(TAG, "msgBody.length:"+msgBody.length());
					if (D) Log.e(TAG, "msgBody len getBytes B:"+msgBody.getBytes().length);
					int capacity = ITSMSG_HEADER_SIZE + msgBody.getBytes().length;
					if (D) Log.e(TAG, "onConnected():capacity:"+capacity);
					ByteBuffer buffer = ByteBuffer.allocate(capacity);
					buffer.order( pushSocket.getPushSocketConfig().getByteOrder() ); // 小字节顺序==高低位对调
					// 长度
					buffer.putInt(capacity);
					// 命令
					buffer.putShort(ITSMSG_LOGIN);
					// SeqNo
					buffer.putInt(0x00000000);
					// body
					buffer.put(msgBody.getBytes()); // 追加方式
					// 通过buffer.flip();这个语句，就能把buffer的当前位置更改为buffer缓冲区的第一个位置。 
					buffer.flip();
					if (D) Log.e(TAG, "onConnected():发送的数据:"+PushSocketChannel.bytes2HexString(buffer.array()));
					int count = pushSocket.send(buffer);
					if (D) Log.e(TAG, "onConnected():成功发送的数量...count():"+count);

					if (D) Log.e(TAG, "onConnected():...end...");

					// 测试Socket读消费队列
					//					__test__Msg_Socket_Read_Queue();

					// 发消息，执行消费回馈队列(连接成为后，如果队列有要回馈的数据，则消费)
					//					sendMessage( Msg_FeedBack_Queue );
				}

				@Override
				public void onReceived(int count, byte[] bytes) {
					if (D) Log.e(TAG, "onReceive():...start...");
					if (D) Log.e(TAG, "onReceive():接收到的数量...count:"+count);
					// 将字节数组转成设定的字节顺序字节数组
					Log.e(TAG, "onReceive():bytes2HexString:"+PushSocket.bytes2HexString(bytes));
					if (D) Log.e(TAG, "onReceive():...end...");
				}

				@Override
				public void onHeartbeat() {
					if (D) Log.e(TAG, "onHeartbeat():...start...");
					if (D) Log.e(TAG, "onHeartbeat():心跳事件。");

					int capacity = ITSMSG_HEADER_SIZE;
					if (D) Log.e(TAG, "onHeartbeat():capacity:"+capacity);
					ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
					byteBuffer.order(pushSocket.getPushSocketConfig().getByteOrder()); // 小字节顺序==高低位对调
					// 长度
					byteBuffer.putInt(capacity);
					// 命令
					byteBuffer.putShort(ITSMSG_TEST);
					// SeqNo
					byteBuffer.putInt(0x00000000);
					// 通过buffer.flip();这个语句，就能把buffer的当前位置更改为buffer缓冲区的第一个位置。 
					byteBuffer.flip();
					if (D) Log.e(TAG, "onHeartbeat():发送的数据:"+PushSocketChannel.bytes2HexString(byteBuffer.array()));
					int count = pushSocket.send(byteBuffer);
					if (D) Log.e(TAG, "onHeartbeat():成功发送的数量...count():"+count);
					if (D) Log.e(TAG, "onHeartbeat():...end...");
				}

				@Override
				public void onError(int error) {
					if (D) Log.e(TAG, "onError():error:"+error);
					if (error == PushSocket.Error_Socket_Error) {
						if (D) Log.e(TAG, "onError()--->Error_Socket_Error");
					} else if (error == PushSocket.Error_Socket_Read_Timeout) {
						if (D) Log.e(TAG, "onError()--->Error_Socket_Read_Timeout");
					}
				}

				@Override
				public void onClosed() {
					if (D) Log.e(TAG, "onClosed()");

				}

			});

		}

	}

	@Override
	protected void onStartLoader() {

	}

	@Override
	protected byte[] doInBackgroundLoader() {
		return null;
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

	}

}
