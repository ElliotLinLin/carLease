package com.tools.task;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.Header;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.http.bean.UpgradeBean;
import com.tools.app.AppInfo;
import com.tools.content.pm.PermissionTool;
import com.tools.io.FileTool;
import com.tools.lang.reflect.ReflectTool;
import com.tools.net.http.HttpConfig;
import com.tools.net.http.HttpTool.Error;
import com.tools.os.Build;
import com.tools.os.storage.StorageTool;
import com.tools.util.Log;
import com.tools.widget.Prompt;


/**
 * 升级任务（实现下载功能）
 *
 * 问：加入升级模块，需要哪些文件？
 * 答：
 * 1）com.tools.task.UpgradeTask.java
 * 2）com.tools.bean.UpgradeTaskBean.java
 * 3）com.tools.bean.UpgradeNotificationBean.java
 * 4）com.aaaaaaaaaaa.oa.task.UpgradeTaskExt.java
 * 5）res/layout/ui_upgrade.xml
 * 6）com.aaaaaaaaaaa.oa.ui.UpgradeUI.java
 * 7）com.aaaaaaaaaaa.oa.http.bean.Upgrade1Bean.java
 *
 * 问：需要哪一些资源？
 * 答：
 * 1）res/layout/values/colors.xml
 *
 * 问：需要AndroidManifest.xml加入哪些？
 * 答：
 * 1）
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.ACCESS_DOWNLOAD_MANAGER" />
    <uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION"/>
    <uses-permission android:name="android.permission.VIBRATE"/>
 * 2）
 * <activity android:name=".ui.UpgradeUI"></activity>
 *
 *
 * 使用例子：
 * UpgradeTaskExt upgradeTaskExt = new UpgradeTaskExt(ui);
				upgradeTaskExt.check();
 *
 *
 * 注：
 * 1）当安装新版本时，share和sqlite是否会保持原数据？？？答：是的，保持原数据。
 * 2）不要网址编码，应由外部进行网址编码。
 *

 * 流程如下：
 * 检测远程网址 	---> 没发现新版本 ---> 提示用户，已是最新版本(通过接口回调显示toast)
 * 				---> 发现新版本 ---> 弹出UI[三个按钮,1)下载;2)取消;3)稍等;]（通过接口完成）
 * 							---> 用户选择"下载" ---> 进入下载过程
 * 							---> 用户选择"取消" ---> 关闭UI
 * 							---> 用户选择"稍等" ---> 不下载，在状态栏里显示一个通知Notification，当用户点击此Notification时，弹出此更新UI
 *
 * 禁止发出通知，既后台下载，如果要使用这一句必须声明一个权限
 * <uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION"/>
 *
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_DOWNLOAD_MANAGER" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION"/>
 *
 * 协议格式如下：
 *
 * {
    "upgrade": {
        "version": "1.2.21",
        "url": "http://202.103.190.97:81/Android/aaaaaaaaaaaaaa_Ver1.2.21_date2013-10-23.apk",
        "size": "2.40M",
        "releaseDate": "2013/10/31 0:00:00",
        "note": ""
    }
   }
 *
 * @author LMC
 *
 */
public class UpgradeTask {

	private static final String TAG = UpgradeTask.class.getSimpleName();

	protected android.support.v4.app.FragmentActivity ui = null;

	protected Context context = null;

	private static boolean isCheck = false; // 是否在检查升级或是在下载

	// 版本对象
	protected AppInfo appInfo = null;

	// +++ 远程信息
	// 请求网址
	protected String queryUri = null;
	// 内容编码
	//	protected String charset = "UTF-8";

	// 是否显示进度对话框
	protected boolean isShowDialog = false;

	// 对话框显示的内容
	protected String showText;

	// 得到下载服务
	protected android.app.DownloadManager downloadManager = null;

	// +++ 应用
	// 应用最新版本号
	protected String newVersion = null;
	//文件大小
	protected String size = null;
	// 应用下载网址
	protected String downloadRemoteUri = null;
	// 应用本地保存路径
	protected String downloadLocalPath = null;
	// 下载ID
	protected long downloadId = -1;

	// +++ 通知
	// 标题
	protected String title = null;

	// 广播接受
	protected DownloadCompletedReceiver receiver = new DownloadCompletedReceiver();

	public static final String ACTION_UPGRADE_OPERATE_COMPLETED = "ACTION_UPGRADE_OPERATE_COMPLETED";

	// http任务
	protected AsyncCallBackHandler callBackHandler;

	// 接口
	protected Adapter adapter = null;

	private static final int MSG_NO_FILE = 1; //文件不存在消息
	private static final int MSG_RESPONDECODE = 2; //url错误码返回消息

	// HTTP配置
	private HttpConfig httpConfig = null;

	// 使用此接口
	public interface Adapter {
		// 请求完成
		abstract void onQueryCompleted(String result);
		// http错误
		abstract void onHttpFailed(Error error, java.lang.Exception exception, int responseCode, byte[] out);
		// 获取最新版本号
		abstract String getVersionNO();
		//文件大小
		abstract String getSize();
		// 更新事件
		abstract void onUpgrade(boolean upgrade, String oldVersion, String newVersion, String downloadRemoteUri, String downloadLocalPath,String size);
		// 获取下载地址
		abstract String getDownloadRemoteUri();
		void onHttpFailed(java.lang.Error error, Exception exception,
				int responseCode, byte[] out);
	}

	/**
	 * 下载完成广播
	 *
	 * @author LMC
	 *
	 */
	public class DownloadCompletedReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.e(TAG, "onReceive --- action::"+action);
			if(action != null && action.equals( android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE )) {
				long id = intent.getLongExtra( android.app.DownloadManager.EXTRA_DOWNLOAD_ID, 0 );
				Log.e(TAG, "onReceive --- getLongExtra id:"+id);
				// 判断这个id与之前的id是否相等，如果相等说明是之前的那个要下载的文件
				Log.e(TAG, "onReceive():getDownloadId():"+getDownloadId());
				if (id == getDownloadId()) {
					// 移除广播
					setCheck(false);//下载完成
					unregisterBroadcast();
					Log.e(TAG, "onReceive():id == downloadId");
					Log.e(TAG, "onReceive():id:"+id);
					Log.e(TAG, "onReceive():getDownloadId():"+getDownloadId());
					Log.e(TAG, "onReceive():getDownloadLocalPath():"+getDownloadLocalPath());
					// 安装应用
					install();
				}
			}else if (action != null && action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
				Log.e(TAG, "onReceive():id ==ACTION_PACKAGE_REPLACED");
				//安装成功后，删掉下载任务
				if (downloadManager != null) {
					downloadManager.remove(getDownloadId());
				}
			}else if(action != null && action.equals(Intent.ACTION_PACKAGE_ADDED)){
				Log.e(TAG, "onReceive():id ==ACTION_PACKAGE_ADDED");
				//安装成功后，删掉下载任务
				if (downloadManager != null) {
					downloadManager.remove(getDownloadId());
				}
			}
		}

	}

	public UpgradeTask(android.support.v4.app.FragmentActivity ui) {
		init(ui);
	}

	/**
	 * 初始化
	 *
	 * @param ui
	 */
	private void init(android.support.v4.app.FragmentActivity ui) {
		if (ui == null) {
			Log.exception(TAG, new NullPointerException("ui == null"));
		}
		this.ui = ui;
		this.context = ui.getApplicationContext();
		appInfo = new AppInfo(context);
		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.INTERNET);
		PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		//		PermissionTool.checkThrow(context, "android.permission.ACCESS_DOWNLOAD_MANAGER");
		PermissionTool.checkThrow(context, android.Manifest.permission.VIBRATE);
		//		PermissionTool.checkThrow(context, android.Manifest.permission.INSTALL_LOCATION_PROVIDER);
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
	 * 得到标题
	 *
	 * @return
	 */
	public String getTitle() {
		Log.e(TAG, "getTitle():"+title);
		return title;
	}

	/**
	 * 设置标题
	 *
	 * @param title
	 */
	public void setTitle(String title) {
		Log.e(TAG, "setTitle():"+title);
		this.title = title;
	}

	/**
	 * 获取下载远程地址
	 *
	 * @return
	 */
	public String getDownloadRemoteUri() {
		Log.e(TAG, "getDownloadRemoteUri():"+downloadRemoteUri);
		return downloadRemoteUri;
	}

	/**
	 * 设置下载远程地址
	 *
	 * @param downloadRemoteUri
	 */
	public void setDownloadRemoteUri(String downloadRemoteUri) {
		Log.e(TAG, "setDownloadRemoteUri():"+downloadRemoteUri);
		this.downloadRemoteUri = downloadRemoteUri;
	}

	/**
	 * 获取下载保存路径
	 *
	 * @return
	 */
	public String getDownloadLocalPath() {
		Log.e(TAG, "getDownloadLocalPath():"+downloadLocalPath);
		return downloadLocalPath;
	}

	/**
	 * 设置下载保存路径
	 *
	 * @param downloadLocalPath
	 */
	public void setDownloadLocalPath(String downloadLocalPath) {
		Log.e(TAG, "setDownloadLocalPath():"+downloadLocalPath);
		this.downloadLocalPath = downloadLocalPath;
	}

	/**
	 * 获取新版本
	 *
	 * @return
	 */
	public String getNewVersion() {
		Log.e(TAG, "getNewVersion():"+newVersion);
		return newVersion;
	}

	/**
	 * 设置新版本
	 *
	 * @param newVersion
	 */
	public void setNewVersion(String newVersion) {
		Log.e(TAG, "setNewVersion():"+newVersion);
		this.newVersion = newVersion;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * 获取下载ID
	 *
	 * @return
	 */
	public long getDownloadId() {
		Log.e(TAG, "getDownloadId():"+downloadId);
		return downloadId;
	}

	/**
	 * 设置下载ID
	 *
	 * @param downloadId
	 */
	protected void setDownloadId(long id) {
		Log.e(TAG, "setDownloadId():"+id);
		downloadId = id;
	}

	/**
	 * 检查版本
	 *
	 * @param queryUri 远程请求网址
	 * @param charset http的内容编码
	 * @param listener 事件接口
	 * @param isShowDialog 是否弹出对话框
	 * @param showText 对话框内容
	 */
	public void check(String queryUri, boolean isShowDialog, String showText, Adapter listener) {
		if (isEmptyString(queryUri)) {
			Log.exception(TAG, "isEmptyString(queryUri) == true");
			return;
		}
		//		if (httpConfig == null) {
		//			Log.exception(TAG, "isEmptyString(httpConfig) == true");
		//			return;
		//		}
		if (showText == null) {
			Log.exception(TAG, "text = null == true");
		}
		this.queryUri = queryUri;
		this.adapter = listener;
		//		this.httpConfig = httpConfig;
		this.isShowDialog = isShowDialog;
		this.showText = showText;

		Log.e(TAG, "check():queryUri:"+queryUri);
		//		Log.e(TAG, "check():charset:"+charset);

		// 创建检查新版本任务
		createTask();
		
	}

	/**
	 * 创建检查新版本任务
	 */
	protected void createTask() {
		
		if (callBackHandler!=null) {//正在检测
			return;
		}
		
		UpgradeBean bean = new UpgradeBean();
        bean.setTypeID("1");	
		callBackHandler=new AsyncCallBackHandler(ui,showText,isShowDialog,null) {
			
			@Override
			public void mySuccess(int arg0, Header[] arg1, String result) {
				Log.e(TAG, "onPostExecute():result:" + result);
				// 调用接口
				if (adapter != null) {
					// 完成接口
					adapter.onQueryCompleted(result);
					// 设置新版本
					setNewVersion(adapter.getVersionNO());
					//设置文件大小
					setSize(adapter.getSize());
					// 远程下载地址
					setDownloadRemoteUri( adapter.getDownloadRemoteUri() );
					Log.i(TAG, "adapter.getVersionNO()"+adapter.getVersionNO());
					boolean bUpgrade = isUpgrade(appInfo.getVersionName()+"."+appInfo.getVersionCode(),adapter.getVersionNO());
					
					// 更新事件接口
					adapter.onUpgrade(bUpgrade, appInfo.getVersionName(),adapter.getVersionNO(), getDownloadRemoteUri(), getDownloadLocalPath(),getSize());
				}
				
			}
			
			@Override
			public void myFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Toast.makeText(ui, "网络异常", Toast.LENGTH_SHORT).show();
				
			}
		};
		
		try {
			AsyncHttpUtil.post(context, queryUri, bean, "application/json", callBackHandler);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		

	}

	

	/**
	 * 判断是否升级
	 *
	 * 测试例子：
	 * UpgradeTask task = new UpgradeTask(ui);
		task.isUpgrade("1.21", "1.21");
		task.isUpgrade("1.2", "1.85");
		task.isUpgrade("1.75", "1.1");
		task.isUpgrade("1.3.1", "1.3.1");
		task.isUpgrade("1.3.52", "1.3.1");
		task.isUpgrade("1.3.52", "1.3.145");
		task.isUpgrade("1.5.3.1", "1.5.3.1");
		task.isUpgrade("1.5.32.1", "1.5.3.1");
		task.isUpgrade("1.5.32.1", "1.6.3.1");
		task.isUpgrade("1.5.32.1", "1.5.67.1");
		task.isUpgrade("1.5.32.2", "1.5.67.1");
		task.isUpgrade("1.5.32.2", "1.5.67.8");
	 *
	 * @param oldVersion 手机客户端的版本号
	 * @param newVersion 最新的版本号
	 * @return
	 */
	public boolean isUpgrade(String oldVersion, String newVersion) {
		Log.e(TAG, "isUpgrade():oldVersion:"+oldVersion+",newVersion:"+newVersion);

		boolean bUpdate = false;

		// 旧
		int[] oldVersionArray = parseVersionArray(oldVersion);
		// 新
		int[] newVersionArray = parseVersionArray(newVersion);

		if (oldVersionArray != null && newVersionArray != null) {
			if (oldVersionArray.length > 0 && newVersionArray.length > 0) {
				// 长度不一样，则升级
				if (oldVersionArray.length != newVersionArray.length) {
					Log.e(TAG, "长度不一样。");
					bUpdate = true;
				}else{ // 长度一样，则新的要大于旧的，才升级
					Log.e(TAG, "长度一样。");
					int len = oldVersionArray.length;
					for (int n = 0; n < len; n++) {
						if (newVersionArray[n] == oldVersionArray[n]) { // 相等，则检查下一个
							continue;
						}else if (newVersionArray[n] > oldVersionArray[n]) { // 新的大于旧的，则升级
							Log.e(TAG, "新的大于旧的 --- newVersionArray[n]:"+newVersionArray[n]+",oldVersionArray[n]:"+oldVersionArray[n]);
							bUpdate = true;
							break;
						}else if (newVersionArray[n] < oldVersionArray[n]) { // 新的小于旧的，则不升级
							break;
						}
					}
				}
			}
		}

		Log.e(TAG, "isUpdate:"+bUpdate);
		if (bUpdate) {
			Log.e(TAG, "发现新版本。");
		}else{
			Log.e(TAG, "无新版本。");
		}

		return bUpdate;
	}

	/**
	 * 将字符串分解为int[]
	 *
	 * @param version
	 * @return
	 */
	protected int[] parseVersionArray(String version) {
		Log.e(TAG, "parseVersionArray():version:"+version);

		if (isEmptyString(version)) {
//			Log.exception(TAG, "isEmptyString(version) == true");
			return null;
		}

		// 一个.分隔不了，要使用\\.
		// 因split参数是正式表达式
		String[] versionStringArray = version.split("\\.");
		if (versionStringArray == null) {
			return null;
		}
		int len = versionStringArray.length;
		Log.e(TAG, "versionStringArray length:"+len);

		int[] versionArray = new int[ len ];

		try {
			for (int n = 0; n < len; n++) {
				versionArray[n] = Integer.valueOf(versionStringArray[n]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return versionArray;
	}

	/**
	 * 创建目录
	 */
	protected void makeDirs(File dir) {
		if (dir == null) {
			Log.exception(TAG, new NullPointerException("dir == null"));
			return;
		}
		// 判断文件是否存在
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 删除本地文件
	 */
	protected void deleteFile(String localPath) {
		if (isEmptyString(localPath)) {
			Log.exception(TAG, "isEmptyString(localPath) == true");
			return;
		}

		File file = new File(localPath);

		if (file.exists()) { // 判断是否存在
			// 删除
			file.delete();
			Log.e(TAG, "deleteFile():file.delete();");
		}
	}

	/**
	 * 下载
	 *
	 */
	public void download() {
		download(getDownloadRemoteUri(), getDownloadLocalPath());
	}

	/**
	 * 下载
	 *
	 * @param downloadRemoteUri
	 * @param downloadLocalPath
	 */
	public void download(final String downloadRemoteUri, final String downloadLocalPath) {
		Log.e(TAG, "download():downloadRemoteUri:"+downloadRemoteUri+",downloadLocalPath:"+downloadLocalPath);
		setCheck(false);//下载完成

		//这个线程用于检测url里要下载的文件是否存在
		//这次修改，是因为使用流量进行更新而文件又不存在服务器时会检测很久都没有反应的问题。
		new Thread(){
			public void run(){

				try {
					URL url = new URL(downloadRemoteUri);
					Log.i(TAG, "url 检测前");
					HttpURLConnection connection = (HttpURLConnection)url.openConnection();
					//设置连接超时；当使用移动网络而非WiFi网络时，如果不设置连接超时，而下载的文件又不存在时，就会一直连接很久。
					connection.setConnectTimeout(5*1000);
					Log.i(TAG, "connection=="+connection.getResponseCode());
					//发送消息，处理下载任务
					Message msg = Message.obtain();
					msg.arg1 = connection.getResponseCode();
					msg.what = MSG_RESPONDECODE;
					handler.sendMessage(msg);

				} catch (Exception e) {
					Log.i(TAG, "url 不可用=="+downloadRemoteUri);
					handler.sendEmptyMessage(MSG_NO_FILE);

				}
			}
		}.start();

	}

	/**
	 * 下载，使用2.3平台DownloadManager的类下载。
	 *
	 * @param downloadRemoteUri
	 * @param downloadLocalPath
	 */
	protected void downloadFromVer2_3(String downloadRemoteUri, String downloadLocalPath) {
		Log.e(TAG, "downloadFromVer2_3():downloadRemoteUri:"+downloadRemoteUri+",downloadLocalPath:"+downloadLocalPath);
		if (isEmptyString(downloadRemoteUri)) {
			Log.exception(TAG, "isEmptyString(downloadRemoteUri) == true");
			return;
		}

		if (isEmptyString(downloadLocalPath)) {
			Log.exception(TAG, "isEmptyString(downloadLocalPath) == true");
			return;
		}

		// TODO 如果文件保存，则会在此文件后面加处-1或者-2或者-3，所以在下载之前，最后是删除此文件。
		deleteFile(downloadLocalPath);

		// 取出父目录，用于创建目录
		makeDirs(new File(downloadLocalPath).getParentFile());

		// 得到下载服务
		downloadManager = (android.app.DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
		if (downloadManager == null) {
			return;
		}

		// 注册下载完成广播
		registerBroadcast();

		Log.i(TAG, "downloadRemoteUri--"+downloadRemoteUri);
		Log.i(TAG, "downloadLocalPath--"+downloadLocalPath);

		/**
		 * 检测url里的文件是否存在
		 */
		//		boolean isFileExists = isFileExists(downloadRemoteUri);
		//		if (!isFileExists) {
		//			return;
		//		}

		// 请求对象
		android.app.DownloadManager.Request downRequest = new android.app.DownloadManager.Request( Uri.parse(downloadRemoteUri) );
		// 设置允许使用的网络类型，这里是移动网络和wifi都可以
		downRequest.setAllowedNetworkTypes( android.app.DownloadManager.Request.NETWORK_MOBILE | android.app.DownloadManager.Request.NETWORK_WIFI );
		// 设定当设备处于漫游模式时是否要下载
		downRequest.setAllowedOverRoaming(true);
		// 设置保存本地文件路径
		downRequest.setDestinationUri(Uri.fromFile(new File(downloadLocalPath))); // ok
		// 显示在下载界面，即下载后的文件在下载管理里显示
		downRequest.setVisibleInDownloadsUi(true);

		//		downRequest.setShowRunningNotification(true);//此方法已经弃用，也不起作用了；setNotificationVisibility代替

		/**
		 * 使用反射来调用高版本的方法
		 */
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Ver2_3) {
			//android.app.DownloadManager$Request，这里必须是$符号，不能用. 因为这是内部类
			String classPath = "android.app.DownloadManager$Request";
			//下载完成后也在Notification里面显示完成通知
			String methodName = "setNotificationVisibility";
			String fieldName = "VISIBILITY_VISIBLE_NOTIFY_COMPLETED";

			Log.e(TAG, "setNotificationVisibility():fieldName:"+fieldName);
			// +++ 使用反射
			// 第一步：得到值
			int value = -1;
			Object object = ReflectTool.getStaticFieldValue(classPath, fieldName);
			if (object != null) {
				value = Integer.valueOf(object.toString());
			}
			// 第二步：得到方法
			java.lang.reflect.Method method = ReflectTool.getMethod(classPath, methodName, int.class);
			// 第三步：调用方法
			Log.i(TAG, "reflect method="+method+",value="+value);
			if (method != null && value != -1) {
				ReflectTool.invokeMethod(downRequest, method, value);
				Log.e(TAG, "setOverScrollEnabled():ReflectTool.invoke(...)");
			}

			//表示允许MediaScanner扫描到这个文件，默认不允许。
			//			String methodName2 = "allowScanningByMediaScanner";//表示允许MediaScanner扫描到这个文件
			//			java.lang.reflect.Method method2 = ReflectTool.getMethod(classPath, methodName2);
			//			// 第三步：调用方法
			//			Log.i(TAG, "reflect method="+method2+",value="+value);
			//			if (method2 != null && value != -1) {
			//				ReflectTool.invokeMethod(downRequest, method2);
			//			}
		}
		//下载完成后也在Notification里面显示完成通知，点击可以安装(3.0以上版本才可以 SDK11)
		//		downRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		//表示允许MediaScanner扫描到这个文件，默认不允许。(3.0以上版本才可以 SDK11)
		//		downRequest.allowScanningByMediaScanner();

		// 设置标题
		downRequest.setTitle( getTitle() );
		// 设置下载后文件存放的位置
		// localFile:/mnt/sdcard/***upgrade/a1.apk
		// file:///mnt/sdcard/Android/data/**********/files//mnt/sdcard/******/upgrade/a1.apk
		//		downRequest.setDestinationInExternalFilesDir(context, null, localFile);
		//		downRequest.setDescription(localFile); // 描述
		// 加入排列  --- 把id保存好，在接收者里面要用，最好保存在Preferences里面
		long downloadId = downloadManager.enqueue(downRequest);
		setDownloadId( downloadId );
		Log.e(TAG, "downloadFromVer2_3():getDownloadId():"+getDownloadId());
	}

	/**
	 * 由远程网址得到WebKit下载本地保存路径
	 *
	 * 一般情况为：
	 *  /mnt/sdcard/Download/remoteFileName.apk
	 *
	 * @param downloadRemoteUri
	 * @return
	 */
	protected String getWebKitDownloadPath(String downloadRemoteUri) {
		Log.e(TAG, "getWebKitDownloadPath():downloadRemoteUri:"+downloadRemoteUri);
		if (isEmptyString(downloadRemoteUri)) {
			Log.exception(TAG, "isEmptyString(downloadRemoteUri) == true");
			return null;
		}

		StorageTool storageTool = new StorageTool(context);
		String path = storageTool.getMountedPath();

		return path + "/Download" + "/" + getFileName(downloadRemoteUri);
	}

	/**
	 * 得到http中的文件名。 TODO 没完成
	 *
	 * @return
	 */
	protected String getFileName(String uri) {
		return null;
	}

	/**
	 * 下载，使用webkit打开网页的方式下载。
	 *
	 * @param downloadRemoteUri
	 * @param downloadLocalPath
	 */
	protected void downloadFromVer2_2(String downloadRemoteUri, String downloadLocalPath) {
		Log.e(TAG, "downloadFromVer2_2():downloadRemoteUri:"+downloadRemoteUri+",downloadLocalPath:"+downloadLocalPath);
		if (isEmptyString(downloadRemoteUri)) {
			Log.exception(TAG, "isEmptyString(downloadRemoteUri) == true");
			return;
		}

		/**
		 * 检测url里的文件是否存在
		 */
		//		boolean isFileExists = isFileExists(downloadRemoteUri);
		//		if (!isFileExists) {
		//			return;
		//		}

		// TODO 第一个方法是用浏览器打开网址；第二个方法是将数据插入到db里。
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadRemoteUri));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		// TODO 如果是2.2及以下的系统，最好是监听数据库变化来达到监听效果。
	}

	protected boolean isFileExists(String downloadRemoteUri) {
		/**
		 * 检测url里的文件是否存在
		 */
		URL url;

		try {
			url = new URL(downloadRemoteUri);
			Log.i(TAG, "url 检测前");
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(5*1000);
			Log.i(TAG, "connection=="+connection.getResponseCode());
			if(connection.getResponseCode()==200){

			}else {
				Prompt.showWarning(context,"文件不存在");
				setCheck(false);//检查升级完成
				return false;
			}
			//					InputStream in = url.openStream();
			Log.i(TAG, "url 可用=="+downloadRemoteUri);
		} catch (Exception e) {
			Log.i(TAG, "url 不可用=="+downloadRemoteUri);
			Prompt.showWarning(context,"文件不存在");
			setCheck(false);//检查升级完成
			return false;
		}
		return true;
	}

	/**
	 * 注册下载完成广播
	 */
	protected void registerBroadcast() {
		Log.e(TAG, "registerBroadcast()");
		IntentFilter filter = new IntentFilter( android.app.DownloadManager.ACTION_DOWNLOAD_COMPLETE );
		context.registerReceiver(receiver, filter);
		//应用替换安装成功
		IntentFilter filter4 = new IntentFilter(Intent.ACTION_PACKAGE_REPLACED);
		context.registerReceiver(receiver, filter4);
		//应用安装成功
		IntentFilter filter5 = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		context.registerReceiver(receiver, filter5);
	}

	/**
	 * 移除下载完成广播
	 */
	protected void unregisterBroadcast() {
		Log.e(TAG, "unregisterBroadcast()");
		if (receiver != null) {
			context.unregisterReceiver(receiver);
		}
	}

	/**
	 * 安装应用
	 */
	public void install() {
		Log.i(TAG, "downloadManager=="+downloadManager);
		install( context, getDownloadLocalPath() );
		//		if (downloadManager != null) {
		//			int id = downloadManager.remove(getDownloadId());//清掉状态栏
		//			Log.i(TAG, "install getDownloadId()=="+getDownloadId()+", id=="+id);
		////			downloadManager.remove(getDownloadId()-1);//如果存在上一个，也清掉（如果上一次下载了，但是没有安装，而现在又重新下载，则存在这种可能）
		//		}

	}

	/**
	 * 安装应用
	 */
	public void install(Context context, String filePath) {
		if (context == null) {
			return;
		}

		Log.e(TAG, "install():filePath:"+filePath);
		if (isEmptyString(filePath)) {
			Log.exception(TAG, "isEmptyString(filePath) == true");
			return;
		}
		NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancelAll();
		File file = new File(filePath);
		if (!FileTool.isExists(file)) {
			// 文件不存在
			Log.i(TAG, "getSize()==文件不存在");
			//在三星note3和另一款手机，不能断点续传，当断开网络再连接会直接删掉未完成的文件
			downloadManager.remove(getDownloadId());//清掉之前的
			download();//重新下载
			return;
		}
		Log.i(TAG, "getFileSize(filePath)=="+getFileSize(filePath));
		Log.i(TAG, "getSize()=="+getSize());
		//		if (getFileSize(filePath) == Integer.valueOf(getSize())) {
		//
		//		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 得到文件大小
	 * @param filePath 必须是文件，不能是文件夹
	 * @return
	 * 2014-7-3 下午4:47:23
	 * @author MoSQ
	 */
	public int getFileSize(String filePath){
		if (filePath == null) {
			return 0;
		}
		int bytes = 0;
		File file = new File(filePath);
		try {
			FileInputStream stream = new FileInputStream(file);
			if (stream != null) {
				bytes = stream.available();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bytes;
	}



	public static boolean isCheck() {
		return isCheck;
	}

	public static void setCheck(boolean isCheck) {
		UpgradeTask.isCheck = isCheck;
	}

	public HttpConfig getHttpConfig() {
		return httpConfig;
	}

	public void setHttpConfig(HttpConfig httpConfig) {
		this.httpConfig = httpConfig;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == MSG_NO_FILE) {//文件不存在时提示
				Prompt.showWarning(context,"文件不存在");
				setCheck(false);//检查升级完成
			}else if (msg.what == MSG_RESPONDECODE) {
				if(msg.arg1==200){
					// 判断系统版本2.3及以上使用DownloadManager，否则使用webkit打开网页的方式下载
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Ver2_3) {
						setCheck(true);//未完成下载
						downloadFromVer2_3(downloadRemoteUri, downloadLocalPath);
					}else{ // <= Ver2_2
						// 由远程网址得到WebKit下载本地保存路径
						String path = getWebKitDownloadPath(downloadRemoteUri);
						setDownloadLocalPath(path);
						downloadFromVer2_2(downloadRemoteUri, getDownloadLocalPath());
					}

					Log.i(TAG, "url 可用=="+downloadRemoteUri);
				}else {
					handler.sendEmptyMessage(MSG_NO_FILE);
				}
			}
		}

	};

}
