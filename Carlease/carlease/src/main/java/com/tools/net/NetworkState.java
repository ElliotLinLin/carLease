package com.tools.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tools.content.pm.PermissionTool;
import com.tools.util.Log;

/**
 * networkInfo.isAvailable() 表示是否可用（跟开关有关系）
 * networkInfo.isConnected() 表示是否已连接，判断是否能上网
 * 
 * 权限：
 *  <uses-permission android:name="android.permission.INTERNET"/>
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * 
 * Android网络连接判断与处理
 * http://www.cnblogs.com/qingblog/archive/2012/07/19/2598983.html
 * 
 * 1、cmwap的特点
    cmwap接入方式的限制很多，cmwap接入方式只开放80，8080，9201端口，仅支持http和wap协议。也就是说，你的移动设备将无法使用基于POP3、IMAP等协议的软件。

 * 2、cmnet的特点
    cmnet就像我们使用电脑连接互联网，除了伟大的墙，你用移动设备连接互联网不会有任何限制。

 * 
 * 其实，CMWAP 和 CMNET 只是中国移动人为划分的两个GPRS接入方式。前者是为手机WAP上网而设立的，
 * 后者则主要是为PC、笔记本电脑、PDA等利用GPRS上网服务。它们在实现方式上并没有任何差别，但因为定位不同，
 * 所以和CMNET相比，CMWAP便有了部分限制，资费上也存在差别。
 * 
 * 
 * 我使用Android HttpClient 下载视频文件, 在W_I_F_I或cmnet网络环境下没问题.但切换到cmwap下的时候http返回码为413.
 * http://www.oschina.net/question/265107_63721
 * 
 * android网络连接Wifi和cmnet及cmwap的问题
		// 如何 现在很完美了，程序可以在cmnet，cmwap，以及wifi下完美运行。???
		// 答: http://hfutxf.iteye.com/blog/1219379
		// 困扰了我很久的，android ，http client无法直接使用cmwap，使用某些wifi会出错的问题，
 * 
 * 
 * android 连接CMWAP代理(HttpURLConnection)
 * http://lynn0708.blog.163.com/blog/static/79587212010873712881/
 * 
 * cmwap/ctwap代理访问
 * http://www.eoeandroid.com/forum.php?mod=viewthread&tid=17974&highlight=
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class NetworkState {

	private static final String TAG = NetworkState.class.getSimpleName();

	protected Context context = null;

	protected ConnectivityManager connectivityManager = null;

	public enum APN
	{
		// 不知道
		UNKOWN,
		// 移动
		CMNET,
		CMWAP,
		// 联通
		_3GNET,
		_3GWAP,
		// 电信
		CTNET,
		CTWAP,
		// 其它的
		OTHER
	}

	public NetworkState(Context context) {
		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.INTERNET);
		PermissionTool.checkThrow(context, android.Manifest.permission.ACCESS_NETWORK_STATE);
		
		this.context = context;
		connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * 得到当前激活网络NetworkInfo
	 * 
	 * @return
	 */
	public android.net.NetworkInfo getActiveNetworkInfo() {
		if (connectivityManager == null) {
			new NullPointerException("connectivityManager == null").printStackTrace();
			return null;
		}
		android.net.NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			Log.e(TAG, "getActiveNetworkInfo():networkInfo != null");
		} else {
			Log.e(TAG, "getActiveNetworkInfo():networkInfo == null");
		}
		return networkInfo;
	}

	/**
	 * 得到指定网络类型的网络信息
	 * 如：手机
	 * ConnectivityManager.TYPE_MOBILE
	 * 
	 * @param networkType
	 * @return
	 */
	public android.net.NetworkInfo getNetworkInfo(int networkType) {
		if (connectivityManager == null) {
			new NullPointerException("connectivityManager == null").printStackTrace();
			return null;
		}

		Log.e(TAG, "getNetworkInfo():networkType:"+networkType);

		android.net.NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networkType);
		if (networkInfo != null) {
			Log.e(TAG, "getNetworkInfo():networkInfo != null");
		} else {
			Log.e(TAG, "getNetworkInfo():networkInfo == null");
		}
		return networkInfo;
	}

	/**
	 * 判断某个网络类型是否可用
	 * 
	 * isAvailable( ConnectivityManager.TYPE_MOBILE );
	 * isAvailable( ConnectivityManager.TYPE_MAX );
	 * 
	 * @return
	 */
	public boolean isAvailable(int networkType) {
		NetworkInfo networkInfo = getNetworkInfo(networkType);
		if (networkInfo == null) {
			new NullPointerException("networkInfo == null").printStackTrace();
			return false;
		}
		boolean isAvailable = networkInfo.isAvailable();
		Log.e(TAG, "isAvailable(int networkType):isAvailable:"+isAvailable);
		return isAvailable;
	}

	/**
	 * 判断当前网络类型是否有效，可用
	 * 
	 * @return
	 */
	public boolean isAvailable() {
		NetworkInfo networkInfo = getActiveNetworkInfo();
		if (networkInfo == null) {
			new NullPointerException("networkInfo == null").printStackTrace();
			return false;
		}
		boolean isAvailable = networkInfo.isAvailable();
		Log.e(TAG, "isAvailable():isAvailable:"+isAvailable);
		return isAvailable;
	}

	/**
	 * 判断某个网络类型是否已连接
	 * isConnected( ConnectivityManager.TYPE_MOBILE );
	 * 
	 * @return
	 */
	public boolean isConnected(int networkType) {
		NetworkInfo networkInfo = getNetworkInfo(networkType);
		if (networkInfo == null) {
			new NullPointerException("networkInfo == null").printStackTrace();
			return false;
		}
		boolean isConnected = networkInfo.isConnected();
		Log.e(TAG, "isConnected(int networkType):isConnected:"+isConnected);
		return isConnected;
	}

	/**
	 * 判断当前激活网络是否已连接Connected
	 * 不管是手机网络，还是无线网络，已连接上了，就为真
	 * 
	 * @return
	 */
	public boolean isConnected() {
		NetworkInfo networkInfo = getActiveNetworkInfo();
		if (networkInfo == null) {
			new NullPointerException("networkInfo == null").printStackTrace();
			return false;
		}
		boolean isConnected = networkInfo.isConnected();
		Log.e(TAG, "isConnected():isConnected:"+isConnected);
		return isConnected;
	}

	/**
	 * 得到当前激活网络类型，是mobile还是无线
	 * 	ConnectivityManager.TYPE_MOBILE
	 * 	ConnectivityManager.TYPE_WIMAX
	 *
	 * @return
	 */
	public int getType() {
		NetworkInfo networkInfo = getActiveNetworkInfo();
		if(networkInfo == null) {
			new NullPointerException("networkInfo == null").printStackTrace();
			return -1;
		}
		int type = networkInfo.getType();
		Log.e(TAG, "getType():type:"+type);
		return type;
	}

	public static final String TYPE_MOBILE = "mobile";
	public static final String TYPE_MOBILE_DUN = "mobile_dun";
	public static final String TYPE_MOBILE_HIPRI = "mobile_hipri";
	public static final String TYPE_MOBILE_MMS = "mobile_mms";
	public static final String TYPE_MOBILE_SUPL = "mobile_supl";
	public static final String TYPE_WIFI = "wifi";
	public static final String TYPE_WIMAX = "wimax";
	public static final String TYPE_UNKNOWN = "unknown"; // 未知

	/**
	 * 网络类型int转成字符串
	 *
	 * int 	TYPE_MOBILE 	The Default Mobile data connection.
int 	TYPE_MOBILE_DUN 	A DUN-specific Mobile data connection. Since: API Level 8 
int 	TYPE_MOBILE_HIPRI 	A High Priority Mobile data connection. Since: API Level 8 
int 	TYPE_MOBILE_MMS 	An MMS-specific Mobile data connection. Since: API Level 8 
int 	TYPE_MOBILE_SUPL 	A SUPL-specific Mobile data connection. Since: API Level 8 
int 	TYPE_WIFI 	The Default WIFI data connection.
int 	TYPE_WIMAX 	The Default WiMAX data connection. Since: API Level 8 

	 * @param type
	 * @return
	 */
	public String getTypeString() {

		int type = getType();

		// 判断类型是否支持
		if ( ConnectivityManager.isNetworkTypeValid(type) == false ) {
			return TYPE_UNKNOWN;
		}

		if (com.tools.os.Build.VERSION.SDK_INT < com.tools.os.Build.VERSION_CODES.Level18) {
			// 只有两种 TYPE_MOBILE TYPE_WIFI
			if ( type == ConnectivityManager.TYPE_MOBILE ) {
				return TYPE_MOBILE;
			} else if ( type == ConnectivityManager.TYPE_WIFI ) {
				return TYPE_WIFI;
			}
		} else {
			if ( type == ConnectivityManager.TYPE_MOBILE ) {
				return TYPE_MOBILE;
			} else if ( type == ConnectivityManager.TYPE_MOBILE_DUN ) {
				return TYPE_MOBILE_DUN;
			} else if ( type == ConnectivityManager.TYPE_MOBILE_HIPRI ) {
				return TYPE_MOBILE_HIPRI;
			} else if ( type == ConnectivityManager.TYPE_MOBILE_MMS ) {
				return TYPE_MOBILE_MMS;
			} else if ( type == ConnectivityManager.TYPE_MOBILE_SUPL ) {
				return TYPE_MOBILE_SUPL;
			} else if ( type == ConnectivityManager.TYPE_WIFI ) {
				return TYPE_WIFI;
			} else if ( type == ConnectivityManager.TYPE_WIMAX ) {
				return TYPE_WIMAX;
			}
		}

		return TYPE_UNKNOWN;
	}

	/**
	 * 得到当前激活网络状态
NetworkInfo.State  	CONNECTED  	 
NetworkInfo.State  	CONNECTING  	 
NetworkInfo.State  	DISCONNECTED  	 
NetworkInfo.State  	DISCONNECTING  	 
NetworkInfo.State  	SUSPENDED  	 
NetworkInfo.State  	UNKNOWN 
	 * 
	 * @return
	 */
	public android.net.NetworkInfo.State getState() {
		NetworkInfo networkInfo = getActiveNetworkInfo();
		if (networkInfo == null) {
			new NullPointerException("networkInfo == null").printStackTrace();
			return android.net.NetworkInfo.State.UNKNOWN;
		}
		NetworkInfo.State state = networkInfo.getState();
		Log.e(TAG, "getState():state:"+state.name());
		return state;
	}

	/**
	 * 获取当前激活网络的的APN（接入点）名称，如：cmnet、cmwap
	 * TODO 不一定可以得到想要的字符串，因为此方法是得到在“设置”里设置的字符串。
	 * 
	 * @return
	 */ 
	public APN getAPN() {

		APN apn = APN.UNKOWN;
		NetworkInfo networkInfo = getActiveNetworkInfo();
		if(networkInfo == null) {
			new NullPointerException("networkInfo == null").printStackTrace();
			return apn;
		}

		String extraInfo = networkInfo.getExtraInfo();
		// extraInfo 是在设置里的值
		Log.e(TAG, "getAPN():"+extraInfo);
		if (extraInfo != null) {
			if(extraInfo.equalsIgnoreCase("cmnet")) {
				apn = APN.CMNET;
			} else if(extraInfo.equalsIgnoreCase("cmwap")) {
				apn = APN.CMWAP;
			} else if(extraInfo.equalsIgnoreCase("ctnet")) {
				apn = APN.CTNET;
			} else if(extraInfo.equalsIgnoreCase("ctwap")) {
				apn = APN.CTWAP;
			} else {
				apn = APN.OTHER;
			}
		}

		return apn;
	}

	/**
	 * 打印
	 * 
	 * NetworkState networkState = new NetworkState(context);
	 * networkState.print( networkState.getActiveNetworkInfo() );
	 * 
	 * @param networkInfo
	 */
	public void print(android.net.NetworkInfo networkInfo) {
		Log.e(TAG, "------------------ start print( android.net.NetworkInfo ) ------------------");
		if (networkInfo != null) {
			// 更详细的状态
			Log.e(TAG, "getDetailedState:"+networkInfo.getDetailedState());
			Log.e(TAG, "getExtraInfo:"+networkInfo.getExtraInfo());
			Log.e(TAG, "getReason:"+networkInfo.getReason());
			Log.e(TAG, "getState:"+networkInfo.getState());
			Log.e(TAG, "getType:"+networkInfo.getType());
			Log.e(TAG, "getTypeName:"+networkInfo.getTypeName());
			Log.e(TAG, "getSubtype:"+networkInfo.getSubtype());
			Log.e(TAG, "getSubtypeName:"+networkInfo.getSubtypeName());
			Log.e(TAG, "isAvailable:"+networkInfo.isAvailable());
			Log.e(TAG, "isConnected:"+networkInfo.isConnected());
			Log.e(TAG, "isConnectedOrConnecting:"+networkInfo.isConnectedOrConnecting());
			Log.e(TAG, "isFailover:"+networkInfo.isFailover());
			Log.e(TAG, "isRoaming:"+networkInfo.isRoaming());
		}
		Log.e(TAG, "------------------ end print( android.net.NetworkInfo ) ------------------");

		/*
	05-01 16:34:08.217: E/Network(6880): getDetailedState:DISCONNECTED
	05-01 16:16:23.767: E/Network(6015): getExtraInfo:3g123
	05-01 16:16:23.767: E/Network(6015): getReason:dataEnabled
	05-01 16:16:23.767: E/Network(6015): getState:CONNECTED
	05-01 16:16:23.767: E/Network(6015): getSubtype:8
	05-01 16:16:23.767: E/Network(6015): getSubtypeName:HSDPA
	05-01 16:16:23.767: E/Network(6015): getType:0
	05-01 16:16:23.767: E/Network(6015): getTypeName:MOBILE
	05-01 16:16:23.767: E/Network(6015): isAvailable:true
	05-01 16:16:23.767: E/Network(6015): isConnected:true
	05-01 16:16:23.767: E/Network(6015): isConnectedOrConnecting:true
	05-01 16:16:23.767: E/Network(6015): isFailover:false
	05-01 16:16:23.767: E/Network(6015): isRoaming:false

	05-01 16:34:08.217: E/Network(6880): getDetailedState:DISCONNECTED
	05-01 16:27:46.967: E/Network(6645): getExtraInfo:3g123
	05-01 16:27:46.967: E/Network(6645): getReason:dataDisabled
	05-01 16:27:46.967: E/Network(6645): getState:DISCONNECTED
	05-01 16:27:46.967: E/Network(6645): getSubtype:8
	05-01 16:27:46.967: E/Network(6645): getSubtypeName:HSDPA
	05-01 16:27:46.967: E/Network(6645): getType:0
	05-01 16:27:46.967: E/Network(6645): getTypeName:MOBILE
	05-01 16:27:46.977: E/Network(6645): isAvailable:true
	05-01 16:27:46.977: E/Network(6645): isConnected:false
	05-01 16:27:46.977: E/Network(6645): isConnectedOrConnecting:false
	05-01 16:27:46.977: E/Network(6645): isFailover:true
	05-01 16:27:46.977: E/Network(6645): isRoaming:false
		 * */
	}

	/**
	 * 打印
	 * 
	 * 例子：
	 * NetworkState networkState = new NetworkState(context);
	 * networkState.print(ConnectivityManager.TYPE_MOBILE);
	 * 
	 * @param networkInfo
	 */
	public void print(int networkType) {
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networkType);
		print(networkInfo);
	}

}
