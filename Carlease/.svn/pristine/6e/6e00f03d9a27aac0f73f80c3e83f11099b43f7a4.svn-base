/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tools.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.tools.app.AppInfo;
import com.tools.app.Config;
import com.tools.app.NotificationUtil;
import com.tools.lang.StringUtil;
import com.tools.lang.reflect.ReflectTool;
import com.tools.net.NetworkState;


/**
 *  Log.e(TAG, "color"); // 深红
	Log.d(TAG, "color"); // 蓝
	Log.w(TAG, "color"); // 浅红
	Log.i(TAG, "color"); // 绿
	Log.v(TAG, "color"); // 黑
	android.util.Log.e(TAG, "ddddddddddddddddd");
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
/**
 * @author aaa
 *
 *@date 2013-10-25 下午5:17:59
 */
public class Log {

	public static final String TAG = Log.class.getSimpleName();

	// 是否在LogCat里显示日志
	public static boolean DEBUG = false;

	// 异常日志
	public static boolean DEBUGException = true;

	// 是否将状态日志保存到文件
	@Deprecated
	public static boolean SAVE_STATUS = true;

	private static final String STATUS_TAG = "status";

	// 通知ID
	private static int notificationID = 0;

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

	public static int v(String tag, String msg) {
		if (DEBUG) {
			return android.util.Log.v(tag, msg);
		}
		return -1;
	}

	public static int v(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			return android.util.Log.v(tag, msg, tr);
		}
		return -1;
	}

	public static int d(String tag, String msg) {
		if (DEBUG) {
			return android.util.Log.d(tag, msg);
		}
		return -1;
	}

	public static int d(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			return android.util.Log.d(tag, msg, tr);
		}
		return -1;
	}

	public static int i(String tag, String msg) {
		if (DEBUG) {
			return android.util.Log.i(tag, msg);
		}
		return -1;
	}

	public static int i(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			return android.util.Log.i(tag, msg, tr);
		}
		return -1;
	}

	public static int w(String tag, String msg) {
		if (DEBUG) {
			return android.util.Log.w(tag, msg);
		}
		return -1;
	}

	public static int w(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			return android.util.Log.w(tag, msg, tr);
		}
		return -1;
	}

	public static int e(String tag, String msg) {
		if (DEBUG) {
			return android.util.Log.e(tag, msg);
		}
		return -1;
	}

	public static int e(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			return android.util.Log.e(tag, msg, tr);
		}
		return -1;
	}

	/**
	 * 写状态日志标题
	 * 
	 * 注：已测试过，不管在Thread还是在AsyncTask或者是在AsyncTaskLoader，都运行正常。
	 * 
	 * 例子：
	 * 
	 * // 状态日志标题
				Log.stateTitle(context, MainUI.class, "测试状态日志标题");
				// 状态日志内容
				Log.stateText(MainUI.class, "测试状态日志内容标题", "测试状态日志内容");

	 * @param context
	 * @param clazz
	 * @param title
	 * @return
	 */
	//	@Deprecated
	//	public static int stateTitle(Context context, Class<?> clazz, String title) {
	//		if (DEBUG == false && SAVE_STATUS == false) {
	//			return -1;
	//		}
	//
	//		if (context == null || clazz == null) {
	//			return -1;
	//		}
	//		String msg = "\n============ "+ clazz.getCanonicalName() + " " + title + " ============";
	//
	//		if (DEBUG) {
	//			android.util.Log.e(clazz.getSimpleName(), msg);
	//		}
	//
	//		// 根据状态写日志
	//		if(SAVE_STATUS) {
	//			if(StatusLog.getFileLogger()==null){//日志记录器被回收则关闭写日志功能
	//				Config.setStatusLogEnable(false);
	//			}else{
	//				StatusLog.getFileLogger().log(Level.INFO, msg);
	//				// 在任务栏生成一个通知
	//				NotificationUtil notification = new NotificationUtil(context);
	//				notification.setSoundDefault();
	//				notification.setUseSound(true);
	//				notification.setUseVibrate(true);
	//				notification.setUseFlashlight(true);
	//				// android.R.drawable.ic_dialog_info 17301659
	//				// 如果填写0，则高版本系统有sound，但状态栏无图标。
	//				//			int icon = android.R.drawable.ic_dialog_info;
	//				//			int icon = android.R.drawable.ic_dialog_alert;
	//				int icon = android.R.drawable.ic_dialog_alert;
	//				notification.sendNotification(notificationID++, icon, "发现一条状态日志", "状态日志", title);
	//			}
	//
	//		}
	//		return -1;
	//	}

	/**
	 * 写状态日志内容
	 * 
	 * 注：已测试过，不管在Thread还是在AsyncTask或者是在AsyncTaskLoader，都运行正常。
	 * 
	 * 例子：
	 * 
	 * // 状态日志标题
				Log.stateTitle(context, MainUI.class, "测试状态日志标题");
				// 状态日志内容
				Log.stateText(MainUI.class, "测试状态日志内容标题", "测试状态日志内容");

	 * @param clazz
	 * @param textHeader
	 * @param text
	 * @return
	 */
	//	@Deprecated
	//	public static int stateText(Class<?> clazz, String textHeader, String text) {
	//		if (DEBUG == false && SAVE_STATUS == false) {
	//			return -1;
	//		}
	//
	//		if (clazz == null) {
	//			return -1;
	//		}
	//
	//		text = DatetimeUtil.getCurrentDatetime() + " " + clazz.getCanonicalName() + " " + textHeader+" -> " + text;
	//
	//		if (DEBUG) {
	//			android.util.Log.e(clazz.getSimpleName(), text);
	//		}
	//
	//		/**
	//		 * msg为null时，只记录标题,否则格式化记录 标题和内容
	//		 * 2013-10-10 12:12:12___TAG:AboutFUI___title:这是数组a的内容___msg:数组内容
	//		 */
	//		//根据状态写日志
	//		if(SAVE_STATUS) {
	//			StatusLog.getFileLogger().log(Level.INFO, text);
	//		}
	//		return -1;
	//	}

	/**
	 * 写状态日志标题
	 * 
	 * 注：已测试过，不管在Thread还是在AsyncTask或者是在AsyncTaskLoader，都运行正常。
	 * 
	 * 例子：
	 * 
		String[] msg = new String[10];
		msg[0] = "测试状态日志内容11111";
		msg[1] = "测试状态日志内容22222";
		msg[2] = "测试状态日志内容33333";
		Log.status(context, new Throwable(), "测试状态日志标题", msg);

	 * @param context
	 * @param clazz
	 * @param title
	 * @return
	 */
	public static int status(Context context, Throwable throwable, String title, String... msg) {
		Log.d(TAG, "...status...");
		if (DEBUG == false && SAVE_STATUS == false) {
			return -1;
		}

		if (context == null) {
			return -1;
		}

		if (throwable == null) {
			return -1;
		}

		// 标题头
		String titleStart = "\n============ "+ title + " start ============";
		if (DEBUG) {
			android.util.Log.e(STATUS_TAG, titleStart);
		}

		// 时间:2014-01-16 09:04:14.562
		String datetime = String.format("时间:%s", DatetimeUtil.getCurrent("yyyy-MM-dd HH:mm:ss.SSS"));
		if (DEBUG) {
			android.util.Log.e(STATUS_TAG, datetime);
		}

		// 应用信息
		String appInfo = new AppInfo(context).toString();
		if (DEBUG) {
			android.util.Log.e(STATUS_TAG, appInfo);
		}

		// 代码行数：at android.widget.AbsListView.obtainView(AbsListView.java:2088)
		String stacksInfo = getStackLineNumber(throwable);
		if (DEBUG) {
			android.util.Log.e(STATUS_TAG, stacksInfo);
		}

		// 内容
		StringBuilder content = new StringBuilder();

		if (msg != null && msg.length > 0) {
			int len = msg.length;
			for (int n = 0; n < len; n++) {
				content.append(msg[n]);
				content.append("\n");
			}
		}
		if (DEBUG) {
			android.util.Log.e(STATUS_TAG, content.toString());
		}

		// 标题尾
		String titleEnd = "============ "+ title + " end ============";
		if (DEBUG) {
			android.util.Log.e(STATUS_TAG, titleEnd);
		}

		// 根据状态写日志
		if(SAVE_STATUS) {
			if(StatusLog.getFileLogger()==null){//日志记录器被回收则关闭写日志功能
				Config.setStatusLogEnable(false);
			} else {

				// 将标题头写入文件
				StatusLog.getFileLogger().log(Level.INFO, titleStart);
				// 将时间写入文件
				StatusLog.getFileLogger().log(Level.INFO, datetime);
				// 将应用信息写入文件
				StatusLog.getFileLogger().log(Level.INFO, appInfo);
				// 将代码行数写入文件
				StatusLog.getFileLogger().log(Level.INFO, stacksInfo);
				// 将内容写入文件
				StatusLog.getFileLogger().log(Level.INFO, content.toString());
				// 将标题尾写入文件
				StatusLog.getFileLogger().log(Level.INFO, titleEnd);

				// 在任务栏生成一个通知
				NotificationUtil notification = new NotificationUtil(context);
				notification.setSoundDefault();
				notification.setUseSound(true);
				notification.setUseVibrate(true);
				notification.setUseFlashlight(true);
				// android.R.drawable.ic_dialog_info 17301659
				// 如果填写0，则高版本系统有sound，但状态栏无图标。
				//			int icon = android.R.drawable.ic_dialog_info;
				//			int icon = android.R.drawable.ic_dialog_alert;
				int icon = android.R.drawable.ic_dialog_alert;
				notification.sendNotification(notificationID++, icon, "发现一条状态日志", "状态日志", title);
			}
		}
		return -1;
	}

	/**
	 * 得到Stack发生的行数
	 * 
	 * at android.widget.AbsListView.obtainView(AbsListView.java:2088)
	 * 
	 * @return
	 */
	public static String getStackLineNumber(Throwable throwable) {

		if (throwable == null) {
			return null;
		}

		StackTraceElement[] stacks = throwable.getStackTrace();
		if (stacks == null) {
			return null;
		}

		int stacksLen = stacks.length;   
		if (stacksLen < 1) {
			return null;
		}

		String stacksText = String.format("at %s.%s(%s:%d)", stacks[0].getClassName(), stacks[0].getMethodName(), stacks[0].getFileName(), stacks[0].getLineNumber());
		return stacksText;
	}

	/**
	 * 打印异常
	 * 
	 * @param tag
	 * @param detailMessage
	 */
	public static void exception(String tag, String detailMessage) {
		if (!DEBUGException || detailMessage == null) {
			return;
		}
		new Exception(detailMessage).printStackTrace();
	}

	/**
	 * 打印异常
	 * 
	 * 例子：
	 * Log.exception(TAG, new NullPointerException("context == null"));
	 * 
	 * @param tag
	 * @param detailMessage
	 */
	public static void exception(String tag, Exception exception) {
		if (!DEBUGException || exception == null) {
			return;
		}
		exception.printStackTrace();
	}

	/**
	 * 主动抛出异常
	 * throw new RuntimeException("run failed.");
	 * throw new NullPointerException("context == null");
	 * 
	 * 例子：
	 * Log.throwException(new RuntimeException("run failed."));
	 */
	public static void throwException(RuntimeException exception) {
		if (exception == null) {
			return;
		}
		throw exception;
	}

	/**
	 * 主动抛出异常
	 * throw new RuntimeException("run failed.");
	 * throw new NullPointerException("context == null");
	 * 
	 * 例子：
	 * Log.throwException("context == null");
	 */
	public static void throwException(String msg) {
		throwException(new RuntimeException(msg));
	}

	/**
	 * 打印android.net.Uri
	 * 
	 * @param tag
	 * @param uri
	 */
	public static void printUri(String tag, android.net.Uri uri) {
		if (uri == null) {
			return;
		}
		Log.e(tag, "------------------ start android.net.Uri ------------------");
		Log.e(tag, "getAuthority:"+uri.getAuthority());
		Log.e(tag, "getEncodedAuthority:"+uri.getEncodedAuthority());
		Log.e(tag, "getEncodedFragment:"+uri.getEncodedFragment());
		Log.e(tag, "getEncodedPath:"+uri.getEncodedPath());
		Log.e(tag, "getEncodedQuery:"+uri.getEncodedQuery());
		Log.e(tag, "getEncodedSchemeSpecificPart:"+uri.getEncodedSchemeSpecificPart());
		Log.e(tag, "getEncodedUserInfo:"+uri.getEncodedUserInfo());
		Log.e(tag, "getFragment:"+uri.getFragment());
		Log.e(tag, "getLastPathSegment:"+uri.getLastPathSegment());
		Log.e(tag, "getHost:"+uri.getHost());
		Log.e(tag, "getPath:"+uri.getPath());
		Log.e(tag, "getPort:"+uri.getPort());
		Log.e(tag, "getQuery:"+uri.getQuery());

		List<String> pathSegments = uri.getPathSegments();
		if (pathSegments != null) {
			int len = pathSegments.size();
			for (int n = 0; n < len; n++) {
				Log.e(tag, "getPathSegments["+n+"]"+":"+pathSegments.get(n).toString());
			}
		}

		Log.e(tag, "getScheme:"+uri.getScheme());
		Log.e(tag, "getSchemeSpecificPart:"+uri.getSchemeSpecificPart());
		Log.e(tag, "getUserInfo:"+uri.getUserInfo());
		Log.e(tag, "isAbsolute:"+uri.isAbsolute());
		Log.e(tag, "isHierarchical:"+uri.isHierarchical());
		Log.e(tag, "isOpaque:"+uri.isOpaque());
		Log.e(tag, "isRelative:"+uri.isRelative());
		Log.e(tag, "toString:"+uri.toString());
		Log.e(tag, "------------------ end android.net.Uri ------------------");
	}

	/**
	 * 打印URI
	 * 
	 * @param tag
	 * @param uri
	 */
	public static void printURI(String tag, java.net.URI uri) {
		if (uri == null) {
			return;
		}
		Log.e(tag, "------------------ start URI ------------------");
		Log.e(tag, "getAuthority:"+uri.getAuthority());
		Log.e(tag, "getFragment:"+uri.getFragment());
		Log.e(tag, "getHost:"+uri.getHost());
		Log.e(tag, "getPath:"+uri.getPath());
		Log.e(tag, "getPort:"+uri.getPort());
		Log.e(tag, "getQuery:"+uri.getQuery());
		Log.e(tag, "getRawAuthority:"+uri.getRawAuthority());
		Log.e(tag, "getRawFragment:"+uri.getRawFragment());
		Log.e(tag, "getRawPath:"+uri.getRawPath());
		Log.e(tag, "getRawQuery:"+uri.getRawQuery());
		Log.e(tag, "getRawSchemeSpecificPart:"+uri.getRawSchemeSpecificPart());
		Log.e(tag, "getRawUserInfo:"+uri.getRawUserInfo());
		Log.e(tag, "getScheme:"+uri.getScheme());
		Log.e(tag, "getSchemeSpecificPart:"+uri.getSchemeSpecificPart());
		Log.e(tag, "getUserInfo:"+uri.getUserInfo());
		Log.e(tag, "isAbsolute:"+uri.isAbsolute());
		Log.e(tag, "isOpaque:"+uri.isOpaque());
		Log.e(tag, "toASCIIString:"+uri.toASCIIString());
		Log.e(tag, "toString:"+uri.toString());
		try {
			Log.e(tag, "toURL:"+uri.toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Log.e(tag, "------------------ end URI ------------------");
	}

	/**
	 * 打印URL
	 * 
	 * @param tag
	 * @param url
	 */
	public static void printURL(String tag, java.net.URL url) {
		if (url == null) {
			return;
		}
		Log.e(tag, "------------------ start URL ------------------");
		Log.e(tag, "getAuthority:"+url.getAuthority());
		Log.e(tag, "getDefaultPort:"+url.getDefaultPort());
		Log.e(tag, "getFile:"+url.getFile());
		Log.e(tag, "getHost:"+url.getHost());
		Log.e(tag, "getPath:"+url.getPath());
		Log.e(tag, "getPort:"+url.getPort());
		Log.e(tag, "getProtocol:"+url.getProtocol());
		Log.e(tag, "getQuery:"+url.getQuery());
		Log.e(tag, "getRef:"+url.getRef());
		Log.e(tag, "getUserInfo:"+url.getUserInfo());
		Log.e(tag, "toExternalForm:"+url.toExternalForm());
		Log.e(tag, "toString:"+url.toString());
		try {
			Log.e(tag, "toURI:"+url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Log.e(tag, "------------------ end URL ------------------");
	}

	/**
	 * 打印Rect
	 * 
	 * @param tag
	 * @param rect
	 */
	public static void printRect(String tag, android.graphics.Rect rect) {
		if (rect == null) {
			return;
		}
		Log.e(tag, "------------------ start Rect ------------------");
		Log.e(tag, "left:"+rect.left);
		Log.e(tag, "top:"+rect.top);
		Log.e(tag, "right:"+rect.right);
		Log.e(tag, "bottom:"+rect.bottom);
		Log.e(tag, "centerX:"+rect.centerX());
		Log.e(tag, "centerY:"+rect.centerY());
		Log.e(tag, "height:"+rect.height());
		Log.e(tag, "width:"+rect.width());
		Log.e(tag, "exactCenterX:"+rect.exactCenterX());
		Log.e(tag, "exactCenterY:"+rect.exactCenterY());
		// 左上角的坐标是（150,75），右下角的坐标是（260,120）
		Log.e(tag, "原点坐标是屏幕的左上角，向右和向下为正方向");
		String format = String.format("矩形左上角的坐标是（left:%d,top:%d），矩形右下角的坐标是（rigth:%d,buttom:%d）", rect.left, rect.top, rect.right, rect.bottom);
		Log.e(tag, format);
		Log.e(tag, "------------------ end Rect ------------------");
	}

	/**
	 * 打印RectF
	 * 
	 * @param tag
	 * @param rectF
	 */
	public static void printRectF(String tag, android.graphics.RectF rectF) {
		if (rectF == null) {
			return;
		}
		Log.e(tag, "------------------ start RectF ------------------");
		Log.e(tag, "left:"+rectF.left);
		Log.e(tag, "top:"+rectF.top);
		Log.e(tag, "right:"+rectF.right);
		Log.e(tag, "bottom:"+rectF.bottom);
		Log.e(tag, "centerX:"+rectF.centerX());
		Log.e(tag, "centerY:"+rectF.centerY());
		Log.e(tag, "height:"+rectF.height());
		Log.e(tag, "width:"+rectF.width());
		// 左上角的坐标是（150,75），右下角的坐标是（260,120）
		Log.e(tag, "原点坐标是屏幕的左上角，向右和向下为正方向");
		String format = String.format("矩形左上角的坐标是（left:%f,top:%f），矩形右下角的坐标是（rigth:%f,buttom:%f）", rectF.left, rectF.top, rectF.right, rectF.bottom);
		Log.e(tag, format);
		Log.e(tag, "------------------ end RectF ------------------");
	}

	/**
	 * 打印DisplayMetrics
	 * 
	 * @param tag
	 * @param metrics
	 */
	public static void printDisplayMetrics(String tag, DisplayMetrics metrics) {
		if (metrics == null) {
			return;
		}
		Log.e(tag, "------------------ start DisplayMetrics ------------------");
		Log.e(tag, "density屏幕密度:"+metrics.density); // 屏幕密度（0.75 / 1.0 / 1.5）
		Log.e(tag, "densityDpi屏幕密度DPI:"+metrics.densityDpi); // 屏幕密度DPI（120 / 160 / 240）
		Log.e(tag, "heightPixels屏幕高度（像素）:"+metrics.heightPixels); // 屏幕高度（像素）
		Log.e(tag, "widthPixels屏幕宽度（像素）:"+metrics.widthPixels); // 屏幕宽度（像素）
		Log.e(tag, "scaledDensity:"+metrics.scaledDensity);
		Log.e(tag, "xdpi:"+metrics.xdpi);
		Log.e(tag, "ydpi:"+metrics.ydpi);
		Log.e(tag, "------------------ end DisplayMetrics ------------------");
	}

	public static void printDate(String tag, java.util.Date date) {
		if (date == null) {
			return;
		}
		Log.e(tag, "------------------ start java.util.Date ------------------");
		Log.e(tag, "日期getDate:"+date.getDate());
		Log.e(tag, "getDay:"+date.getDay());
		Log.e(tag, "小时getHours:"+date.getHours());
		Log.e(tag, "分钟getMinutes:"+date.getMinutes());
		Log.e(tag, "月(+1)getMonth:"+date.getMonth());
		Log.e(tag, "秒getSeconds:"+date.getSeconds());
		Log.e(tag, "1970年到现在的秒数getTime:"+date.getTime());
		Log.e(tag, "getTimezoneOffset:"+date.getTimezoneOffset());
		Log.e(tag, "年(+1900)getYear:"+date.getYear());
		Log.e(tag, "------------------ end java.util.Date ------------------");
	}

	public static void printIntent(String tag, Intent intent) {
		if (intent == null) {
			return;
		}
		Log.e(tag, "------------------ start android.content.Intent ------------------");
		Log.e(tag, "getAction:"+intent.getAction());
		Log.e(tag, "getDataString:"+intent.getDataString());
		Log.e(tag, "getFlags:"+intent.getFlags());
		Log.e(tag, "getPackage:"+intent.getPackage());
		Log.e(tag, "getScheme:"+intent.getScheme());
		Log.e(tag, "getType:"+intent.getType());
		Log.e(tag, "hasCategory(android.intent.category.DEFAULT):"+intent.hasCategory( Intent.CATEGORY_DEFAULT )); // android.intent.category.DEFAULT
		Log.e(tag, "hasCategory(android.intent.category.HOME):"+intent.hasCategory( Intent.CATEGORY_HOME )); // android.intent.category.HOME
		Log.e(tag, "hasCategory(android.intent.category.INFO):"+intent.hasCategory( Intent.CATEGORY_INFO )); // android.intent.category.INFO
		Log.e(tag, "hasCategory(android.intent.category.LAUNCHER):"+intent.hasCategory( Intent.CATEGORY_LAUNCHER )); // android.intent.category.LAUNCHER
		Log.e(tag, "hasCategory(android.intent.category.MONKEY):"+intent.hasCategory( Intent.CATEGORY_MONKEY)); // android.intent.category.MONKEY
		Log.e(tag, "hasCategory(android.intent.category.TEST):"+intent.hasCategory( Intent.CATEGORY_TEST )); // android.intent.category.TEST
		android.content.ComponentName componentName = intent.getComponent();
		if (componentName != null) {
			Log.e(tag, "componentName.getPackageName:"+componentName.getPackageName());
			Log.e(tag, "componentName.getClassName:"+componentName.getClassName());
			Log.e(tag, "componentName.getShortClassName:"+componentName.getShortClassName());
			Log.e(tag, "componentName.toShortString:"+componentName.toShortString());
			Log.e(tag, "componentName.toString:"+componentName.toString());
		}
		printUri(tag, intent.getData());
		Log.e(tag, "------------------ end android.content.Intent ------------------");
	}

	/**
	 * 打印广播ConnectionReceiver中的Intent
	 * 
	 * @param context
	 * @param tag
	 * @param intent
	 */
	public static void printIntentFromConnectivityManager(String tag, Context context, Intent intent) {
		if (intent == null) {
			return;
		}
		Log.e(tag, "------------------ start android.net.ConnectivityManager中的Intent ------------------");
		printIntent(tag, intent);

		boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		Log.e(tag, "noConnectivity:"+noConnectivity); // == false时，网络打开

		String extraInfo = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO);
		Log.e(tag, "extraInfo:"+extraInfo);

		boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
		Log.e(tag, "isFailover:"+isFailover);

		NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
		NetworkState networkState = new NetworkState(context);
		networkState.print(networkInfo);

		NetworkInfo otherNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
		NetworkState networkState2 = new NetworkState(context);
		networkState2.print(otherNetworkInfo);

		String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
		Log.e(tag, "reason:"+reason);
		Log.e(tag, "------------------ end android.net.ConnectivityManager中的Intent ------------------");
	}

	public static void printStackTraceElement(String tag, StackTraceElement[] stacks) {
		if (stacks == null) {
			return;
		}
		Log.e(tag, "------------------ start java.lang.StackTraceElement ------------------");
		int len = stacks.length;
		for (int n = 0; n < len; n++) {
			Log.e(tag, "n:"+n);
			Log.e(tag, "getClassName:"+stacks[n].getClassName());
			Log.e(tag, "getFileName:"+stacks[n].getFileName());
			Log.e(tag, "getMethodName:"+stacks[n].getMethodName());
			Log.e(tag, "getLineNumber:"+stacks[n].getLineNumber());
		}
		Log.e(tag, "------------------ end java.lang.StackTraceElement ------------------");
	}

	/**
	 * 04-30 16:15:07.030: E/HttpTool(19575): ------------------ start java.lang.Throwable ------------------
04-30 16:15:07.030: E/HttpTool(19575): getLocalizedMessage:connect failed: ENETUNREACH (Network is unreachable)
04-30 16:15:07.040: E/HttpTool(19575): getMessage:connect failed: ENETUNREACH (Network is unreachable)
04-30 16:15:07.040: E/HttpTool(19575): toString:libcore.io.ErrnoException: connect failed: ENETUNREACH (Network is unreachable)
04-30 16:15:07.040: E/HttpTool(19575): ------------------ end java.lang.Throwable ------------------

	 * @param tag
	 * @param stacks
	 */
	public static void printThrowable(String tag, Throwable t) {
		Log.e(tag, "------------------ start java.lang.Throwable ------------------");
		if (t != null) {
			Log.e(tag, "getLocalizedMessage:"+t.getLocalizedMessage());
			Log.e(tag, "getMessage:"+t.getMessage());
			Log.e(tag, "toString:"+t.toString());
		}
		Log.e(tag, "------------------ end java.lang.Throwable ------------------");
	}

	/**
	 * 
08-12 11:08:08.211: E/PushSocket(31301): ------------------ start java.lang.Exception ------------------
08-12 11:08:08.241: E/PushSocket(31301): getLocalizedMessage:The connection was reset
08-12 11:08:08.281: E/PushSocket(31301): getMessage:The connection was reset
08-12 11:08:08.311: E/PushSocket(31301): toString:java.net.SocketException: The connection was reset
08-12 11:08:08.351: E/PushSocket(31301): ------------------ end java.lang.Exception ------------------
	 * @param tag
	 * @param e
	 */
	public static void printException(String tag, Exception e) {
		Log.e(tag, "------------------ start java.lang.Exception ------------------");
		if (e != null) {
			Log.e(tag, "getLocalizedMessage:"+e.getLocalizedMessage());
			Log.e(tag, "getMessage:"+e.getMessage());
			Log.e(tag, "toString:"+e.toString());
		}
		Log.e(tag, "------------------ end java.lang.Exception ------------------");
	}

	/**
	 * 打印java.io.File
	 * 
	 * 例子：
	 * Log.printFile(new File("/mnt/sdcard/download/aaa.jpg"));
	 * 
	 * 02-21 15:49:12.651: E/Log(22529): ------------------ start java.io.File ------------------
02-21 15:49:12.651: E/Log(22529): getAbsolutePath:/mnt/sdcard/download/abc.jpg
02-21 15:49:12.661: E/Log(22529): getCanonicalPath:/mnt/sdcard/download/abc.jpg
02-21 15:49:12.661: E/Log(22529): getParent:/mnt/sdcard/download
02-21 15:49:12.661: E/Log(22529): getAbsoluteFile:/mnt/sdcard/download/abc.jpg
02-21 15:49:12.661: E/Log(22529): getCanonicalFile:/mnt/sdcard/download/abc.jpg
02-21 15:49:12.661: E/Log(22529): getParentFile:/mnt/sdcard/download
02-21 15:49:12.661: E/Log(22529): getPath:/mnt/sdcard/download/abc.jpg
02-21 15:49:12.661: E/Log(22529): getName:abc.jpg
02-21 15:49:12.661: E/Log(22529): exists:false
02-21 15:49:12.661: E/Log(22529): isAbsolute:true
02-21 15:49:12.661: E/Log(22529): isDirectory:false
02-21 15:49:12.661: E/Log(22529): isFile:false
02-21 15:49:12.661: E/Log(22529): isHidden:false
02-21 15:49:12.661: E/Log(22529): lastModified:0
02-21 15:49:12.671: E/Log(22529): length:0
02-21 15:49:12.671: E/Log(22529): ------------------ end java.io.File ------------------

	 * @param file
	 */
	public static void printFile(File file) {
		Log.e(TAG, "------------------ start java.io.File ------------------");

		if (file == null) {
			return;
		}

		try {

			Log.e(TAG, "getAbsolutePath:"+file.getAbsolutePath());
			Log.e(TAG, "getCanonicalPath:"+file.getCanonicalPath());
			//			Log.e(TAG, "getFreeSpace:"+file.getFreeSpace());
			Log.e(TAG, "getParent:"+file.getParent());
			//			Log.e(TAG, "getTotalSpace:"+file.getTotalSpace());
			//			Log.e(TAG, "getUsableSpace:"+file.getUsableSpace());
			Log.e(TAG, "getAbsoluteFile:"+file.getAbsoluteFile());
			Log.e(TAG, "getCanonicalFile:"+file.getCanonicalFile());
			Log.e(TAG, "getParentFile:"+file.getParentFile());

			Log.e(TAG, "getPath:"+file.getPath());
			Log.e(TAG, "getName:"+file.getName());

			//			Log.e(TAG, "canExecute:"+file.canExecute());
			//			Log.e(TAG, "canRead:"+file.canRead());
			//			Log.e(TAG, "canWrite:"+file.canWrite());
			Log.e(TAG, "exists:"+file.exists());
			Log.e(TAG, "isAbsolute:"+file.isAbsolute());
			Log.e(TAG, "isDirectory:"+file.isDirectory());
			Log.e(TAG, "isFile:"+file.isFile());
			Log.e(TAG, "isHidden:"+file.isHidden());
			Log.e(TAG, "lastModified:"+file.lastModified());
			Log.e(TAG, "length:"+file.length());

		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.e(TAG, "------------------ end java.io.File ------------------");
	}

	/**
	 * 02-10 09:47:26.445: E/AppInfo(1940): ------------------ start android.os.Environment ------------------
02-10 09:47:26.445: E/AppInfo(1940): getDataDirectory:/data
02-10 09:47:26.445: E/AppInfo(1940): getDownloadCacheDirectory:/cache
02-10 09:47:26.446: E/AppInfo(1940): getExternalStorageDirectory:/mnt/sdcard
02-10 09:47:26.446: E/AppInfo(1940): getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS):/mnt/sdcard/Alarms
02-10 09:47:26.446: E/AppInfo(1940): getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM):/mnt/sdcard/DCIM
02-10 09:47:26.447: E/AppInfo(1940): getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS):/mnt/sdcard/Download
02-10 09:47:26.447: E/AppInfo(1940): getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES):/mnt/sdcard/Movies
02-10 09:47:26.447: E/AppInfo(1940): getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC):/mnt/sdcard/Music
02-10 09:47:26.448: E/AppInfo(1940): getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS):/mnt/sdcard/Notifications
02-10 09:47:26.448: E/AppInfo(1940): getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES):/mnt/sdcard/Pictures
02-10 09:47:26.448: E/AppInfo(1940): getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS):/mnt/sdcard/Podcasts
02-10 09:47:26.449: E/AppInfo(1940): getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES):/mnt/sdcard/Ringtones
02-10 09:47:26.450: E/AppInfo(1940): getExternalStorageState:mounted
02-10 09:47:26.450: E/AppInfo(1940): getRootDirectory:/system
02-10 09:47:26.451: E/AppInfo(1940): ------------------ end android.os.Environment ------------------
	 */
	public static void printEnvironment() {
		Log.e(TAG, "------------------ start android.os.Environment ------------------");
		Log.e(TAG, "getDataDirectory:"+Environment.getDataDirectory());
		Log.e(TAG, "getDownloadCacheDirectory:"+Environment.getDownloadCacheDirectory());
		Log.e(TAG, "getExternalStorageDirectory:"+Environment.getExternalStorageDirectory());

		Log.e(TAG, "getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS):"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS));
		Log.e(TAG, "getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM):"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
		Log.e(TAG, "getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS):"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
		Log.e(TAG, "getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES):"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
		Log.e(TAG, "getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC):"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));
		Log.e(TAG, "getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS):"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS));
		Log.e(TAG, "getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES):"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
		Log.e(TAG, "getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS):"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS));
		Log.e(TAG, "getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES):"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES));

		Log.e(TAG, "getExternalStorageState:"+Environment.getExternalStorageState());
		Log.e(TAG, "getRootDirectory:"+Environment.getRootDirectory());

		Log.e(TAG, "Build.VERSION.SDK_INT:"+com.tools.os.Build.VERSION.SDK_INT);

		if (com.tools.os.Build.VERSION.SDK_INT >= com.tools.os.Build.VERSION_CODES.Level9) { // >= 2.3
			String classPath = "android.os.Environment";
			String methodName = "isExternalStorageRemovable";
			java.lang.reflect.Method method = ReflectTool.getMethod(classPath, methodName); 
			Object object = ReflectTool.invokeStaticMethod( method );
			if (object != null) {
				Log.e(TAG, "isExternalStorageRemovable:"+StringUtil.Object2Boolean(object));
			}
		}

		Log.e(TAG, "------------------ end android.os.Environment ------------------");
	}

}
