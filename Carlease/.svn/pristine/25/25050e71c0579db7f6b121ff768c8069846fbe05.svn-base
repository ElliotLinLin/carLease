package com.hst.Carlease.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.alibaba.fastjson.JSONObject;
import com.hst.Carlease.R;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.ram.PushDomain;
import com.hst.Carlease.ram.PushRam;
import com.hst.Carlease.ram.SQLiteRam;
import com.hst.Carlease.ram.ShareSet;
import com.hst.Carlease.share.ShareSingle;
import com.hst.Carlease.sqlite.SQLiteHelper;
import com.hst.Carlease.sqlite.bean.PushSeqNo;
import com.hst.Carlease.ui.LauncherPassUI;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.tools.app.Config;
import com.tools.app.NotificationUtil;
import com.tools.json.GJson;
import com.tools.lang.StringUtil;
import com.tools.net.NetworkState;
import com.tools.os.storage.StorageTool;
import com.tools.push.PushSocket;
import com.tools.push.PushSocketChannel;
import com.tools.push.PushSocketConfig;
import com.tools.ram.SDRam;
import com.tools.service.DurableService;
import com.tools.sqlite.SQLiteInfo;
import com.tools.sqlite.SQLiteManager;
import com.tools.sqlite.SQLiteSingle;
import com.tools.util.Log;
import com.tools.util.LogcatHelper;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**onStartCommand()
 * 后台服务
 * <p>
 * startService生命周期：onCreate(一次) -> onStartCommand(多次) -> onStart(多次) -> ... ->
 * onDestroy(一次) 如果被杀掉,重新启动时,会执行onCreate();
 * <p>
 * 在AndroidManifest.xml文件里加入，三样：
 * <p>
 * 权限： <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"
 * /> <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <p>
 * 广播： <receiver android:name="com.service.BootReceiver" android:process=":push"
 * > <intent-filter > <action
 * android:name="android.intent.action.BOOT_COMPLETED" /> </intent-filter>
 * </receiver>
 * <p>
 * <receiver android:name="com.service.ConnectionReceiver"
 * android:process=":push" > <intent-filter > <action
 * android:name="android.net.conn.CONNECTIVITY_CHANGE" /> </intent-filter>
 * </receiver>
 * <p>
 * 服务： <service android:name="com.service.PushService" android:process=":push" >
 * <!-- 多了一个android:priority属性，并且依次减小。这个属性的范围在-1000到1000，数值越大，优先级越高。 -->
 * <intent-filter android:priority="1000" > <action
 * android:name="com.service.PushService" /> </intent-filter> </service>
 * <p>
 * 使用例子：
 * <p>
 * // 启动 context.startService(new Intent(PushService.Action));
 * <p>
 * // 调用并且传参 Intent intent = new Intent(PushService.Action);
 * intent.putExtra("cmd", 88); context.startService(intent);
 * <p>
 * // 在service的onStartCommand()里接受参数值 if (DEBUG) Log.e(TAG,
 * "onStartCommand():cmd:"+intent.getIntExtra("cmd", 0));
 * <p>
 * // 停止 context.stopService(new Intent(PushService.Action));
 *
 * @author lmc 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class PushService extends Service {
    private static final String TAG = PushService.class.getSimpleName();
    private static final boolean D = false;

    protected Context context = null;

    // 激活名称
    public static final String Action = PushService.class.getCanonicalName();

    protected NotificationUtil notification = null;
    protected int notifID = 0;

    /*
     * 版本 3.0.6 == ok
     */
    private String version = "3.0.6";

    // 命令列表
    public static final String Key_Command = "Command";
    public static final int Command_Start = 1; // 启动
    public static final int Command_Push = 2; // 开始启动推送
    public static final int Command_Send = 3; // 发送
    public static final int Command_IsInline = 4; // IsInline
    public static final int Command_Close = 5;
    // intent扩展信息
    public static final String Extra_Send = "send";

    // 判断是否正在运行中。。。。。
    private boolean isRunning = false;

    // 消息
    protected static final int Msg_Socket_Read_Queue = 1; // Socket读_消费队列
    protected static final int Msg_FeedBack_Queue = 2; // 回馈_消费队列(收到消息后，返回一个消息给服务器)

    protected SQLiteManager sqlite = null;

    protected static PushSocket pushSocket = null;

    // 共计连接成功次数
    protected int connectedCount = 0;
    // 成功连接后，共计发送了多少次心跳
    protected int heartbeatCount = 0;

    // 消息数量
    protected int messageCount = 0;

    // 接受到的数据 LinkedBlockingQueue 是有界的，默认大小为2^31－1，所以换成ConcurrentLinkedQueue
    protected ConcurrentLinkedQueue<java.lang.Byte> queueReceived = new ConcurrentLinkedQueue<java.lang.Byte>();

    // 推送日志记录器
    protected LogcatHelper logger = null;

    public class MyServiceImpl extends IMyService.Stub {

        @Override
        public String getValue() throws RemoteException {
            return "AndroidServiceImpl...";
        }

        @Override
        public int max(int a, int b) throws RemoteException {
            return (a >= b) ? a : b;
        }

    }

    @Override
    public IBinder onBind(Intent arg0) {
        if (D)
            Log.e(TAG, "onBind()");
        // 返回
        return new MyServiceImpl();
    }

    @Override
    public void onCreate() {

        this.context = this.getApplicationContext();

        if (D)
            Log.e(TAG, "onCreate()");
        if (D)
            Log.e(TAG, "version:" + version);

        SQLiteInfo sqliteInfo = new SQLiteInfo(context, SQLiteRam.getName(),
                SQLiteRam.getVersion());
        SQLiteHelper sqliteHelper = new SQLiteHelper(context,
                sqliteInfo.getDBName(), sqliteInfo.getVersion());
        sqlite = new SQLiteManager(context, sqliteInfo, sqliteHelper);
        if (D)
            Log.e(TAG, "onCreate():openSQLite:" + sqlite.open());

        // 保证单例数据库是可用的
        if (D)
            Log.e(TAG, "onCreate():SQLiteSingle.getInstance().isOpen():"
                    + SQLiteSingle.getInstance().isOpen());
        if (SQLiteSingle.getInstance().isOpen() == false) {
            SQLiteSingle.getInstance().open();
        }
        if (D)
            Log.e(TAG, "onCreate():SQLiteSingle.getInstance().isOpen():"
                    + SQLiteSingle.getInstance().isOpen());

        // 初始化化notifiedID
        notifID = ShareSingle.getInstance().getInteger(ShareSet.keyNotifedID(),
                0);

        notification = new NotificationUtil(this.context);

        // 开启日志
        if (Config.isPushLogEnable()) {
            logger = pushlogInit(context);
            if (logger != null) {
                // 运行流程日志记录
                Log.e(TAG, "start push logger");
                logger.start(context);
            }
        }

        super.onCreate();
    }

    /**
     * 初始化日志
     *
     * @date 2013-10-29 下午4:10:15
     * @author luman
     */
    private LogcatHelper pushlogInit(Context context) {
        if (D)
            Log.e(TAG, "---pushlogInit  logInit() ---");
        if (context == null) {
            return null;
        }
        // 获取SD目录
        String sdDir;
        // SD卡操作类
        StorageTool sTool = new StorageTool(context);
        if (sTool.getMountedPath() != null) {
            // 优先保存到SD卡中
            sdDir = sTool.getMountedPath();
        } else {
            // 如果内置和外置的SD卡都不存在，就不进行日志记录
            return null;
        }
        // 流程日志目录
        String pushLogDir = sdDir + SDRam.getPushLogPath();

        // 创建日志目录
        File pushLogFile = new File(pushLogDir);

        if (!pushLogFile.exists()) {
            // 创建流程日志目录
            pushLogFile.mkdirs();
        }

        // 初始化流程日志,日志的保留时间、大小配置，可以getInstance()通过get/set获取/设置
        LogcatHelper logcat = LogcatHelper.getInstance();
        if (logcat != null) {
            logcat.logcatHelperInit(context, pushLogDir);
            logcat.setLOG_FILE_MAX_SIZE(100 * 1024 * 1024);// 推送日志文件设置大一点100M
            logcat.setLOG_FILE_MONITOR_INTERVAL(60 * 60 * 1000);// 检测文件大小时间间隔设置一小时
        }
        return logcat;
    }

    /**
     * while(true)执行任务
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (D)
            Log.e(TAG, "onStartCommand():flags:" + flags + ",startId:"
                    + startId);
        if (intent != null) {
            // 一个独特的整数表示此特定请求开始
            // 可以通过onStartCommand有几个
            int cmd = intent.getIntExtra(Key_Command, -1);
            if (D)
                Log.e(TAG, "onStartCommand():cmd:" + cmd);
            if (D)
                Log.e(TAG, "onStartCommand():isRunning:" + isRunning);

            if (cmd == Command_Start) {
                if (isRunning == false) {
                    isRunning = true;
                    if (pushSocket == null) {
                        startPush();
                    }
                }
            } else if (cmd == Command_Push) {
                if (isRunning == false) {
                    isRunning = true;
                    if (pushSocket == null) {
                        startPush();
                    }
                }
            } else if (cmd == Command_Send) {
                if (pushSocket != null) {
                    byte[] bytes = intent
                            .getByteArrayExtra(PushService.Extra_Send);
                    int count = pushSocket.send(bytes);
                    if (D)
                        Log.e(TAG, "send count:" + count);
                }
            } else if (cmd == Command_IsInline) {
                if (pushSocket != null) {
                    boolean isInline = pushSocket.isInline();
                    if (D)
                        Log.e(TAG, "pushSocket.isInline():" + isInline);
                }
            } else if (cmd == Command_Close) {
                if (pushSocket != null) {
                    pushSocket.close();
                }
            }

            if (D)
                Log.e(TAG, "onStartCommand():isRunning:" + isRunning);
        }
        return Service.START_NOT_STICKY;
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
     * 测试Socket读消费队列
     */

    /**
     * 开始启动推送
     */
    protected void startPush() {
        if (D)
            Log.e(TAG, "startPush()");

        // TTS 输入资源的名字 输出资源的名字
        // TTS.initIsResV4(context, TTSRam.getResInName(),
        // TTSRam.getResOutName());
        // 初始化
        // TTS.init();
        // 开始
        // TTS.start();

		/*
         * http://www.chengxuyuans.com/Android/58774.html 心跳检测步骤： 1
		 * 客户端每隔一个时间间隔发生一个探测包给服务器 2 客户端发包时启动一个超时定时器 3 服务器端接收到检测包，应该回应一个包 4
		 * 如果客户机收到服务器的应答包，则说明服务器正常，删除超时定时器 5 如果客户端的超时定时器超时，依然没有收到应答包，则说明服务器挂了
		 */// TODO 这个没有做
        PushSocketConfig pushSocketConfig = new PushSocketConfig();
        pushSocketConfig.setHost(PushDomain.getHost()); // 域名
        pushSocketConfig.setPort(PushDomain.getPort()); // 端口
        pushSocketConfig.setConnectTimeout(10 * 1000); // 设置连接超时 （TODO 没验证）
        // pushSocketConfig.setReadTimeout( 2 * 60 * 1000 ); // 设置读超时间(要小于心跳时间)
        // --- 这个有BUG，建议设置为0
        // pushSocketConfig.setReadTimeout( 0 ); // 设置读超时间(要小于心跳时间)
        pushSocketConfig.setReadTimeout(60 * 60 * 1000); // 设置读超时间(要小于心跳时间)
        pushSocketConfig.setReconnectTime(10 * 1000); // 重连间隔时间
        // 要比服务端时间少些，微信为5分钟，这里设置为2分钟
        // pushSocketConfig.setHeartbeatTime( (5 * 60 * 1000) - (60 * 1000)); //
        // 心跳间隔时间
        pushSocketConfig.setHeartbeatTime((2 * 60 * 1000)); // 心跳间隔时间
        // pushSocketConfig.setHeartbeatTime( (30 * 1000) ); // 心跳间隔时间
        // pushSocketConfig.setHeartbeatTime( (1 * 20 * 1000) ); // 心跳间隔时间
        pushSocketConfig.setByteOrder(ByteOrder.LITTLE_ENDIAN); // 设置字节顺序

        pushSocket = new PushSocket(context, pushSocketConfig);
        if (pushSocket != null) {

            // 事件接口
            pushSocket.setEventListener(new PushSocket.IEventListener() {

                @Override
                public void onConnected() {
                    if (D)
                        Log.e(TAG, "onConnected():...start...");
                    if (D)
                        Log.e(TAG, "onConnected():已连接成功，下面发送登陆包。");
                    if (D)
                        Log.e(TAG, "pushSocketChannel.byteOrder:"
                                + pushSocket.getPushSocketConfig()
                                .getByteOrder().toString());
                    JSONObject json = new JSONObject();
                    // 登录推送服务器，加入密码认证。
                    String password = SPUtils.get(context, Constants.Pwd, "")
                            .toString();
                    String userName = SPUtils.get(context, Constants.DeviceNO,
                            "").toString();
                    if (password == null || userName == null) {
                        return;
                    }
                    if (userName == null) {
                        userName = StringUtils.getIMEI(context);
                    }
                    System.out.println("data  userName 用户控制类：" + userName);
                    System.out.println("data  password 用户控制类：" + password);

                    json.put("username", userName);
                    json.put("password", password);
                    // 加入网络类型，是3G还是WF
                    json.put("NetworkType",
                            new NetworkState(context).getTypeString());
                    json.put("version", PushDomain.getAppVersion(context));
                    String msgBody = json.toJSONString();
                    if (D)
                        Log.e(TAG, "msgBody--->string:" + msgBody);
                    if (D)
                        Log.e(TAG, "msgBody.length:" + msgBody.length());
                    if (D)
                        Log.e(TAG,
                                "msgBody len getBytes B:"
                                        + msgBody.getBytes().length);
                    int capacity = PushRam.ITSMSG_HEADER_SIZE
                            + msgBody.getBytes().length;
                    if (D)
                        Log.e(TAG, "onConnected():capacity:" + capacity);
                    ByteBuffer buffer = ByteBuffer.allocate(capacity);
                    buffer.order(pushSocket.getPushSocketConfig()
                            .getByteOrder()); // 小字节顺序==高低位对调
                    // 长度
                    buffer.putInt(capacity);
                    // 命令
                    buffer.putShort(PushRam.ITSMSG_LOGIN);
                    // SeqNo
                    buffer.putInt(0x00000000);
                    // body
                    buffer.put(msgBody.getBytes()); // 追加方式
                    // 通过buffer.flip();这个语句，就能把buffer的当前位置更改为buffer缓冲区的第一个位置。
                    buffer.flip();
                    if (D)
                        Log.e(TAG,
                                "onConnected():发送的数据:"
                                        + PushSocketChannel
                                        .bytes2HexString(buffer.array()));
                    int count = pushSocket.send(buffer);
                    if (D)
                        Log.e(TAG, "onConnected():成功发送的数量...count():" + count);

                    // set == 0
                    connectedCount++;
                    heartbeatCount = 0;
                    if (D)
                        Log.e(TAG, "onConnected():...end...");

                    // 发消息，执行消费回馈队列(连接成为后，如果队列有要回馈的数据，则消费)
                    sendMessage(Msg_FeedBack_Queue);
                }

                @Override
                public void onReceived(int count, byte[] bytes) {
                    if (D)
                        Log.e(TAG, "onReceive():...start...");
                    if (D)
                        Log.e(TAG, "onReceive():接收到的数量...count:" + count);

                    // PushScket.java将数据拷贝上来，而解析数据包在这里进行，注意：数据包可能是被拆分的，所以一条消息也可能是截断的。

                    // 将得到的数据包放入一个buffer区，用一个类似消费者线程去取（数据包头一个字节表示长度 ）

                    // 将字节数组转成设定的字节顺序字节数组
                    if (D)
                        Log.e(TAG,
                                "onReceive():bytes2HexString:"
                                        + PushSocket.bytes2HexString(bytes));
                    if (D)
                        Log.e(TAG, "count:" + count);

                    // 将拷贝上来的数据加入列表中
                    for (int n = 0; n < count; n++) {
                        queueReceived.add(new Byte(bytes[n]));
                    }

                    // 发消息，进行消费队列
                    sendMessage(Msg_Socket_Read_Queue);

                    if (D)
                        Log.e(TAG, "onReceive():...end...");
                }

                @Override
                public void onHeartbeat() {
                    if (D)
                        Log.e(TAG, "onHeartbeat():...start...");
                    if (D)
                        Log.e(TAG, "onHeartbeat():心跳事件。");

                    int capacity = PushRam.ITSMSG_HEADER_SIZE;
                    if (D)
                        Log.e(TAG, "onHeartbeat():capacity:" + capacity);
                    ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
                    byteBuffer.order(pushSocket.getPushSocketConfig()
                            .getByteOrder()); // 小字节顺序==高低位对调
                    // 长度
                    byteBuffer.putInt(capacity);
                    // 命令
                    byteBuffer.putShort(PushRam.ITSMSG_TEST);
                    // SeqNo
                    byteBuffer.putInt(0x00000000);
                    // 通过buffer.flip();这个语句，就能把buffer的当前位置更改为buffer缓冲区的第一个位置。
                    byteBuffer.flip();
                    if (D)
                        Log.e(TAG,
                                "onHeartbeat():发送的数据:"
                                        + PushSocketChannel
                                        .bytes2HexString(byteBuffer
                                                .array()));
                    int count = pushSocket.send(byteBuffer);
                    if (D)
                        Log.e(TAG, "onHeartbeat():成功发送的数量...count():" + count);
                    if (D)
                        Log.e(TAG, "onHeartbeat():连接成功次数:" + connectedCount
                                + ",一共发送心跳的次数:" + (++heartbeatCount));
                    if (D)
                        Log.e(TAG, "onHeartbeat():...end...");
                }

                @Override
                public void onError(int error) {
                    if (D)
                        Log.e(TAG, "onError():error:" + error);
                    if (error == PushSocket.Error_Socket_Error) {
                        if (D)
                            Log.e(TAG, "onError()--->Error_Socket_Error");
                        // 如果发生错误
                        // 停止消费
                        clearMessage(Msg_Socket_Read_Queue);
                        // 清空
                        queueReceived.clear();
                    } else if (error == PushSocket.Error_Socket_Read_Timeout) {
                        if (D)
                            Log.e(TAG, "onError()--->Error_Socket_Read_Timeout");
                    }
                }

                @Override
                public void onClosed() {
                    if (D)
                        Log.e(TAG, "onClosed()");
                }

            });

            // 连接
            pushSocket.start();
            if (D)
                Log.e(TAG, "pushSocket.start();");

        } // end push
    }

    // 消息
    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Msg_Socket_Read_Queue:
                    if (D)
                        Log.i(TAG, "what == Msg_Socket_Read_Queue");
                    if (isSocketReadConsumeEnable(queueReceived)) { // 判断是否可以消费
                        // 消费队列
                        boolean isConsume = consumeSocketReadQueue(queueReceived);
                        if (isConsume) {
                            // 消费成功后，再继续
                            handler.sendEmptyMessage(Msg_Socket_Read_Queue);
                        }
                    }
                    break;
                case Msg_FeedBack_Queue:
                    if (D)
                        Log.i(TAG, "what == Msg_FeedBack_Queue");
                    // 判断数据库是否可以消费
                    if (isFeedbackConsumeEnable(PushSeqNo.class)) { // 判断是否可以消费
                        // 消费回馈队列
                        boolean isConsume = consumeFeedbackQueue();
                        if (isConsume) {
                            // 消费成功后，再继续
                            handler.sendEmptyMessage(Msg_FeedBack_Queue);
                        }
                    }
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
    protected void sendMessage(int what) {
        if (handler == null) {
            return;
        }
        if (handler.hasMessages(what)) {
            // 如果此消息在消息队列里，则不重复加入消息队列
            if (D)
                Log.e(TAG, "消息队列里已存在:" + what);
        }
        handler.sendEmptyMessage(what);
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
        handler.removeMessages(what);
    }

    /**
     * 判断是否可以消费
     *
     * @return
     */
    protected boolean isSocketReadConsumeEnable(
            ConcurrentLinkedQueue<java.lang.Byte> queue) {

        if (D)
            Log.e(TAG, "--- isConsumeEnable() start ---");

        boolean isConsumeEnable = false;

        if (queue != null) {

            int queueSize = queue.size();
            if (D)
                Log.e(TAG, "queueSize:" + queueSize);
            if (queueSize >= PushRam.ITSMSG_HEADER_SIZE) { // 队列大小要不小于包头大小10个字节
                if (D)
                    Log.e(TAG, "queueSize >= PushRam.ITSMSG_HEADER_SIZE");

                int headerCmdLenSize = 4; // 包头中的CmdLen大小(int是4个字节)
                java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer
                        .allocate(headerCmdLenSize);
                byteBuffer.order(pushSocket.getPushSocketConfig()
                        .getByteOrder());

                // 取前面4个
                Iterator<Byte> ff = queue.iterator();
                for (int n = 0; n < headerCmdLenSize; n++) {
                    if (ff.hasNext()) {
                        Byte bb = ff.next();
                        if (D)
                            Log.e(TAG, "byteBuffer put n:" + n);
                        byteBuffer.put(n, bb.byteValue());
                    }
                }

                if (D)
                    Log.e(TAG,
                            "byteBuffer.bytes2HexString():"
                                    + PushSocket.bytes2HexString(byteBuffer.array()));
                if (D)
                    Log.e(TAG, "queue.size():" + queue.size());

                int cmdLen = byteBuffer.getInt(0);
                if (D)
                    Log.e(TAG, "cmdLen:" + cmdLen);

                // 比较,queue里的大小要超过cmdLen，并且长度不超过1024
                if (queueSize >= cmdLen && cmdLen <= (1024 * 5)) {
                    // 可以消费
                    isConsumeEnable = true;
                } else {
                    if (D)
                        Log.e(TAG, "queueSize < cmdLen --- 不处理");
                }
            }
        }
        if (D)
            Log.e(TAG, "isConsumeEnable():isConsumeEnable:" + isConsumeEnable);
        if (D)
            Log.e(TAG, "--- isConsumeEnable() end ---");

        return isConsumeEnable;
    }

    /**
     * 消费队列中的一个
     */
    protected boolean consumeSocketReadQueue(
            ConcurrentLinkedQueue<java.lang.Byte> queue) {
        if (D)
            Log.e(TAG, "--- consumeSocketReadQueue() start ---");

        boolean isConsume = false;

        // 可以消费

        int queueSize = queue.size();
        if (D)
            Log.e(TAG, "queueSize:" + queueSize);
        if (queueSize >= PushRam.ITSMSG_HEADER_SIZE) { // 队列大小要不小于包头大小10个字节
            if (D)
                Log.e(TAG, "queueSize >= PushRam.ITSMSG_HEADER_SIZE");

            int headerCmdLenSize = 4; // 包头中的CmdLen大小(int是4个字节)
            java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer
                    .allocate(headerCmdLenSize);
            byteBuffer.order(pushSocket.getPushSocketConfig().getByteOrder());

            // 取前面4个
            Iterator<Byte> ff = queue.iterator();
            for (int n = 0; n < headerCmdLenSize; n++) {
                if (ff.hasNext()) {
                    Byte bb = ff.next();
                    if (D)
                        Log.e(TAG, "byteBuffer put n:" + n);
                    byteBuffer.put(n, bb.byteValue());
                }
            }
            if (D)
                Log.e(TAG,
                        "byteBuffer.bytes2HexString():"
                                + PushSocket.bytes2HexString(byteBuffer.array()));
            if (D)
                Log.e(TAG, "queue.size():" + queue.size());

            int cmdLen = byteBuffer.getInt(0);
            if (D)
                Log.e(TAG, "cmdLen:" + cmdLen);

            // 比较,queue里的大小要超过cmdLen，并且长度不超过1024
            if (queueSize >= cmdLen && cmdLen <= (1024 * 5)) {
                // 真正的取出来
                if (D)
                    Log.e(TAG, "queueSize >= cmdLen");

                byte[] tmpBytes = new byte[cmdLen];
                for (int n = 0; n < cmdLen; n++) {
                    tmpBytes[n] = queue.poll().byteValue();
                }

                PushPacket pushPacket = bytes2PushPacket(cmdLen, tmpBytes,
                        pushSocket.getPushSocketConfig().getByteOrder());
                if (pushPacket != null) {
                    pushPacket.print();
                }

                // 将SeqNo信息保存到数据库里
                putSQLiteForPushSeqNo(pushPacket);
                // 发消息，执行消费回馈队列
                sendMessage(Msg_FeedBack_Queue);

                // 将PushPacket转成PushMessage
                PushMessage pushMessage = pushPacket2PushMessage(pushPacket);
                if (pushMessage != null) {
                    pushMessage.print();
                }

                // 发出PushMessage通知
                showPushMessage(pushMessage);
                isConsume = true;
            } else {
                if (D)
                    Log.e(TAG, "queueSize < cmdLen --- 不处理");
            }
        }
        if (D)
            Log.e(TAG, "--- consumeSocketReadQueue() end ---");

        return isConsume;
    }

    /**
     * 判断数据包是否含有此命令ID
     *
     * @return
     */
    protected boolean isCmdID(PushPacket pushPacket, short cmdID) {

        boolean isCmdID = false;

        if (pushPacket != null) {
            // 判断数据包是否含有此命令
            if (D)
                Log.e(TAG, "isCmdID():cmdID:" + cmdID);
            if (D)
                Log.e(TAG,
                        "isCmdID():pushPacket.getCmdID():" + pushPacket.getCmdID());
            if (pushPacket.getCmdID() == cmdID) {
                isCmdID = true;
            }
        }
        if (D)
            Log.e(TAG, "isCmdID():" + isCmdID);
        return isCmdID;
    }

    /**
     * 将信息保存到数据库里
     */
    protected void putSQLiteForPushSeqNo(PushPacket pushPacket) {
        if (D)
            Log.i(TAG, "putSQLiteForPushSeqNo()");
        if (pushPacket == null) {
            return;
        }

        int SeqNo = pushPacket.getSeqNo();
        if (SeqNo <= 0) {
            return;
        }

        // 判断数据包是否含有此命令ID (判断命令ID是不是消息推送命令ID)
        if (isCmdID(pushPacket, PushRam.ITSMSG_OBD_PUSH_MESSAGE)) {
            PushSeqNo entity = new PushSeqNo();
            entity.setSeqNo(SeqNo);
            SQLiteSingle.getInstance().insert(entity);
            SQLiteSingle.getInstance().printTable(PushSeqNo.class);
        }
    }

    /**
     * 判断回馈队列是否可以消费
     *
     * @param clazz
     * @return
     */
    protected boolean isFeedbackConsumeEnable(Class<?> clazz) {
        if (D)
            Log.e(TAG, "--- isFeedbackConsumeEnable() start ---");

        if (clazz == null) {
            return false;
        }

        int count = SQLiteSingle.getInstance().getRowCount(clazz);
        if (count > 0) {
            return true;
        }
        if (D)
            Log.e(TAG, "--- isFeedbackConsumeEnable() end ---");

        return false;
    }

    /**
     * 根据seqNo创建数据包
     *
     * @param seqNo
     * @return
     */
    protected byte[] createPushMessageResp(int seqNo) {
        if (D)
            Log.e(TAG, "--- createPushMessageResp() start ---");

        // 数据
        JSONObject json = new JSONObject();
        json.put("respSeqNo", seqNo);

        String dataString = json.toJSONString();
        if (D)
            Log.e(TAG, "dataString:" + dataString);
        if (isEmptyString(dataString)) {
            return null;
        }

        byte[] dataBytes = dataString.getBytes();
        if (dataBytes == null) {
            return null;
        }

        int dataCount = dataBytes.length;
        if (D)
            Log.e(TAG, "dataCount:" + dataCount);
        if (dataCount <= 0) {
            return null;
        }
        if (D)
            Log.e(TAG, "数据HEX:" + PushSocket.bytes2HexString(dataBytes));

        // 将包头+数据加一起
        int CmdLen = PushRam.ITSMSG_HEADER_SIZE + dataCount;
        if (D)
            Log.e(TAG, "CmdLen:" + CmdLen);
        ByteBuffer sendByteBuffer = ByteBuffer.allocate(CmdLen);
        sendByteBuffer.order(pushSocket.getPushSocketConfig().getByteOrder()); // 小字节顺序==高低位对调

        // 打包头
        sendByteBuffer.putInt(CmdLen);

        // short放不下，用char
        sendByteBuffer.putChar(PushRam.ITSMSG_OBD_PUSH_MESSAGE_RESP);

        sendByteBuffer.putInt(0x00000000);
        if (D)
            Log.e(TAG, "包头HEX:" + PushSocket.byteBuffer2HexString(sendByteBuffer));

        // 打包数据
        sendByteBuffer.put(dataBytes);
        sendByteBuffer.flip();
        if (D)
            Log.e(TAG, "发送HEX:" + PushSocket.byteBuffer2HexString(sendByteBuffer));
        if (D)
            Log.e(TAG, "--- createPushMessageResp() end ---");

        return sendByteBuffer.array();
    }

    /**
     * 消费回馈队列
     *
     * @return
     */
    protected boolean consumeFeedbackQueue() {
        if (D)
            Log.e(TAG, "--- feedbackConsumeQueue() start ---");

        // 读出一条数据，并且打包成数据包，发送给服务端
        List<PushSeqNo> list = SQLiteSingle.getInstance().query(
                PushSeqNo.class, 1);
        if (list != null) {
            if (D)
                Log.i(TAG, "list size:" + list.size());

            PushSeqNo pushSeqNo = list.get(0); // 取第一个
            if (pushSeqNo != null) {

                // 根据seqNo创建数据包
                byte[] sendData = createPushMessageResp(pushSeqNo.getSeqNo());

                if (D)
                    Log.e(TAG, "feedbackConsumeQueue():发送的数据:"
                            + PushSocketChannel.bytes2HexString(sendData));
                int count = pushSocket.send(sendData);
                if (D)
                    Log.e(TAG, "feedbackConsumeQueue():成功发送的数量...count():"
                            + count);
                if (D)
                    Log.e(TAG, "feedbackConsumeQueue():...end...");

                if (count > 0) {
                    // 消费成功
                    if (D)
                        Log.e(TAG, "PushSeqNo数据库消费成功");
                    SQLiteSingle.getInstance().printTable(PushSeqNo.class);
                    if (D)
                        Log.e(TAG,
                                "PushSeqNo数据库消费成功---删除SeqNo:"
                                        + pushSeqNo.getSeqNo());

                    // 删除数据
                    String where = String.format("SeqNo = %d",
                            pushSeqNo.getSeqNo());
                    SQLiteSingle.getInstance().delete(PushSeqNo.class, where);

                    SQLiteSingle.getInstance().printTable(PushSeqNo.class);

                    return true;
                }
            }
        }

        // 发送不失败，则删除数据
        if (D)
            Log.e(TAG, "--- feedbackConsumeQueue() end ---");
        return false;
    }

    /**
     * 推送数据包(包头+数据)
     *
     * @author LMC
     */
    class PushPacket {
        private int CmdLen = 0; // 长度
        private short CmdID = 0;
        private int SeqNo = 0;
        private String data = null; // 数据

        public int getCmdLen() {
            return CmdLen;
        }

        public void setCmdLen(int cmdLen) {
            CmdLen = cmdLen;
        }

        public short getCmdID() {
            return CmdID;
        }

        public void setCmdID(short cmdID) {
            CmdID = cmdID;
        }

        public int getSeqNo() {
            return SeqNo;
        }

        public void setSeqNo(int seqNo) {
            SeqNo = seqNo;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public void print() {
            if (D) {
                Log.e(TAG, "--- PushPacket() start ---");
                Log.e(TAG, "CmdLen:" + CmdLen);
                Log.e(TAG, "CmdID:" + CmdID);
                Log.e(TAG, "SeqNo:" + SeqNo);
                Log.e(TAG, "data:" + data);
                Log.e(TAG, "--- PushPacket() end ---");
            }
        }
    }

    /**
     * 安卓新的推送JSON 推送数据(无包头+数据)
     *
     * @author LMC
     */
    class PushMessage {
        private int type; // 表示父分类：资讯+保养+报警+优惠券+违章
        private int id;
        private String title; // 标题
        private String alert; // 内容

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void print() {
            if (D) {
                Log.e(TAG, "--- PushMessage() start ---");
                Log.e(TAG, "type:" + type);
                Log.e(TAG, "id:" + id);
                Log.e(TAG, "title:" + title);
                Log.e(TAG, "--- PushMessage() end ---");
            }
        }
    }

    /**
     * 将bytes转成PushPacket
     *
     * @param count
     * @param bytes
     * @return
     */
    protected PushPacket bytes2PushPacket(int count, byte[] bytes,
                                          java.nio.ByteOrder byteOrder) {
        if (count <= 0) {
            if (D)
                Log.e(TAG, "bytes2PushPacket():count <= 0");
            return null;
        }

        if (bytes == null) {
            if (D)
                Log.e(TAG, "bytes2PushPacket():bytes == null");
            return null;
        }

        if (byteOrder == null) {
            if (D)
                Log.e(TAG, "bytes2PushPacket():byteOrder == null");
            return null;
        }

        int len = bytes.length;
        if (len <= 0) {
            return null;
        }

        if (count < PushRam.ITSMSG_HEADER_SIZE) {
            return null;
        }

        if (D)
            Log.e(TAG, "bytes2PushPacket():count:" + count);

        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(count);
        byteBuffer.order(byteOrder);
        byteBuffer.put(bytes);
        byteBuffer.flip(); // pos返加到第一个
        if (D)
            Log.e(TAG, "byteBuffer.order().toString():"
                    + byteBuffer.order().toString());

        PushPacket pushPacket = new PushPacket();
        if (D)
            Log.e(TAG, "bytes2PushPacket():bytes2HexString():"
                    + PushSocketChannel.bytes2HexString(bytes));
        if (D) {
            Log.e(TAG, "buffer.position(0):" + byteBuffer.position());

            Log.e(TAG, "buffer.position(1开始的第一个位置):" + byteBuffer.position());
            Log.e(TAG, "buffer.remaining:" + byteBuffer.remaining());
            Log.e(TAG, "buffer.hasRemaining:" + byteBuffer.hasRemaining());
        }

        // 返回的是10个字节加上json字符串
        int CmdLen = byteBuffer.getInt();

        pushPacket.setCmdLen(CmdLen);
        if (D) {
            Log.e(TAG, "buffer.remaining:" + byteBuffer.remaining());
            Log.e(TAG, "buffer.hasRemaining:" + byteBuffer.hasRemaining());

        }

        short CmdID = byteBuffer.getShort();
        pushPacket.setCmdID(CmdID);
        if (D)
            Log.e(TAG, "buffer.remaining:" + byteBuffer.remaining());
        if (D)
            Log.e(TAG, "buffer.hasRemaining:" + byteBuffer.hasRemaining());

        int SeqNo = byteBuffer.getInt();
        pushPacket.setSeqNo(SeqNo);
        if (D)
            Log.e(TAG, "buffer.remaining:" + byteBuffer.remaining());
        if (D)
            Log.e(TAG, "buffer.hasRemaining:" + byteBuffer.hasRemaining());

        if (D)
            Log.e(TAG, "CmdLen:" + CmdLen + ",CmdID:" + CmdID + ",SeqNo:"
                    + SeqNo);
        if (D) {
            Log.e(TAG, "buffer.position(2得完包头):" + byteBuffer.position());

            Log.e(TAG, "buffer.remaining:" + byteBuffer.remaining());
            Log.e(TAG, "buffer.hasRemaining:" + byteBuffer.hasRemaining());
        }

        // 这里还要调试
        // 这里不用调字节顺序
        byte[] msgBodyBuffer = new byte[CmdLen - PushRam.ITSMSG_HEADER_SIZE];
        if (D)
            Log.e(TAG, "msgBodyBuffer.length:" + msgBodyBuffer.length);
        byteBuffer.get(msgBodyBuffer, 0, msgBodyBuffer.length);

        String msgBody = new String(msgBodyBuffer);
        pushPacket.setData(msgBody);
        if (D)
            Log.e(TAG, "msgBody result:" + msgBody);
        if (D)
            Log.e(TAG, "buffer.position(3得完内容):" + byteBuffer.position());

        return pushPacket;
    }

    /**
     * 将PushPacket转成PushMessage
     *
     * @param pushPacket
     * @return
     */
    protected PushMessage pushPacket2PushMessage(PushPacket pushPacket) {
        String msgBody = pushPacket.getData();
        if (D)
            Log.e(TAG, "pushPacket2PushMessage():msgBody:" + msgBody);
        if (isEmptyString(msgBody)) {
            return null;
        }

        try {
            if (D)
                Log.e(TAG, "pushPacket2PushMessage():msgBody:" + msgBody);
            // PushMessage pushMessage = GJson.parseObject(msgBody,
            // PushMessage.KeyName, PushMessage.class);// TODO
            // 混淆后转换出问题,需加-keep
            PushMessage pushMessage = GJson.parseObject(msgBody,
                    PushMessage.class); // class
            // com.tools.json.**
            // {*;}
            if (pushMessage != null) {
                return pushMessage;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 发出PushMessage通知
     *
     * @param pushMessage
     */
    protected void showPushMessage(PushMessage pushMessage) {
        if (pushMessage == null) {
            return;
        }
        // 类型
        int type = pushMessage.getType();
        if (D)
            Log.e(TAG, "showPushMessage():type:" + type);

        Intent intent = new Intent(context, LauncherPassUI.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (D)
            Log.e(TAG, "showNogification");
        showNotification(pushMessage.getTitle(), pushMessage.getTitle(),
                pushMessage.getAlert(), intent);

        messageCount++;
        if (D)
            Log.e(TAG, "共计收到消息数量messageCount:" + messageCount);

    }

    /**
     * 停止推送
     */
    protected void stopPush() {
        if (D)
            Log.e(TAG, "stopPush()");
        if (pushSocket != null) {
            pushSocket.close();
            pushSocket.setEventListener(null);
            pushSocket = null;
        }
        // +++ 停止服务时，停止常驻服务，为了省电。
        // 停止常驻服务
        DurableService durableService = new DurableService(context);
        // 创建一个
        Intent intent = new Intent(context, PushService.class);
        intent.putExtra(PushService.Key_Command, PushService.Command_Push);
        // 停止
        durableService.cancel(intent);

        // 停止服务
        context.stopService(new Intent(Action));
    }

    /**
     * 判断某个车牌是否设防
     *
     * @return
     */
    protected boolean isFortify(String carID) {
        if (D)
            Log.e(TAG, "isFortify(carID)");
        boolean bFortify = false;
        String sql = String.format(
                "select states from proof where objectId = %s;", carID);
        Object obj = sqlite.queryRow(sql, "states");
        if (obj != null) {
            int n = StringUtil.Object2Integer(obj);
            if (n == 1) {
                bFortify = true;
            }
        }
        if (D)
            Log.e(TAG, "isFortify(carID):carID:" + carID + ",bFortify:"
                    + bFortify);
        return bFortify;
    }

    /**
     * 显示通知
     *
     * @param tickerText
     * @param contentTitle
     * @param contentText
     * @param intent
     */
    protected void showNotification(String tickerText, String contentTitle,
                                    String contentText, Intent intent) {

        if (D)
            Log.e(TAG, "------ showNotification() start ------");

        if (isEmptyString(tickerText)) {
            tickerText = "";
        }
        if (isEmptyString(contentTitle)) {
            contentTitle = "";
        }
        if (isEmptyString(contentText)) {
            contentText = "";
        }
        if (D) {
            Log.e(TAG, "showNotification():notifID:" + notifID);
            Log.e(TAG, "showNotification():tickerText:" + tickerText);
            Log.e(TAG, "showNotification():contentTitle:" + contentTitle);
            Log.e(TAG, "showNotification():contentText:" + contentText);

        }

        int icon = R.drawable.ic_lanucher;

        // 不要音
        notification.setUseSound(false);
        // 显示
        notification.sendNotification(++notifID, icon, tickerText,
                contentTitle, contentText, intent);
        // 保存notifID
        if (notifID < Integer.MAX_VALUE) {
            ShareSingle.getInstance().putInteger(ShareSet.keyNotifedID(),
                    notifID);
        } else {
            notifID = 0;
        }

        if (D)
            Log.e(TAG, "------ showNotification() end ------");
    }

    /**
     * 启动
     */
    public static void start(Context context) {
        if (D)
            Log.e(TAG, "start()");
        Intent intent = new Intent(Action);
        context.startService(intent);
    }

    /**
     * 停止
     */
    public static void stop(Context context) {
        if (D)
            Log.e(TAG, "stop()");

        // +++ 停止服务时，停止常驻服务，为了省电。
        // 停止常驻服务
        DurableService durableService = new DurableService(context);
        // 创建一个
        Intent intent = new Intent(context, PushService.class);
        intent.putExtra(PushService.Key_Command, PushService.Command_Start);
        // 停止
        durableService.cancel(intent);

        // 停止服务
        context.stopService(new Intent(Action));
    }

    /**
     * 发送命令
     */
    public static void sendCommand(Context context, int command, Intent intent) {
        if (D)
            Log.e(TAG, "sendCommand:" + command);
        if (intent == null) {
            intent = new Intent(Action);
        } else {
            intent.setAction(Action);
        }
        intent.putExtra(Key_Command, command);
    }

    /**
     * 发送命令
     */
    public static void sendCommand(Context context, int command) {
        sendCommand(context, command, null);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if (D)
            Log.e(TAG, "onStart()");
        super.onStart(intent, startId);
    }

    @Override
    public void onRebind(Intent intent) {
        if (D)
            Log.e(TAG, "onRebind()");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (D)
            Log.e(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if (D)
            Log.e(TAG, "onDestroy()");

        // 停止语音
        // TTS.stop();
        // 停止服务
        stopPush();

        // 停止日志记录
        if (Config.isPushLogEnable()) {
            if (logger != null) {
                // 运行流程日志记录
                if (D)
                    Log.e(TAG, "stop push logger");
                logger.stop();
            }
        }
        // 停止sqlite
        SQLiteSingle.getInstance().close();
        if (sqlite != null) {
            sqlite.close();
        }

        isRunning = false;

        super.onDestroy();
    }

    @Override
    protected void finalize() throws Throwable {
        if (D)
            Log.e(TAG, "finalize()");
        super.finalize();
    }

}
