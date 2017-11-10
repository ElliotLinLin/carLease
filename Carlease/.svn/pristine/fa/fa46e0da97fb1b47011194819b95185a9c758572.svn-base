package com.tools.crash;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import com.tools.util.Log;


/**
 * 这是一个抽象类，需要要实现二个方法
 * throwText
 * throwThrowable
 * 
 * 使用方法：
 * 第一步：
 * 新建一个类继承Application，并且在onCreate()里中入
 * AbsExceptionHandler exceptionHandler = new FileCrashHandler(context, url);
 * 第二步：
 * 在AndroidManifest.xml里的application加入name内容，如下：
 * android:name="com.app.MainApplication"（就是第一步新建立的类）
 * 第三步：
 * 加入权限
 * 如果是保存在本地文件里，请加入
 *  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public abstract class AbsExceptionHandler implements UncaughtExceptionHandler {

	private static final String TAG = AbsExceptionHandler.class.getSimpleName();
	private static final boolean DEBUG = true;

	protected java.lang.Thread.UncaughtExceptionHandler defaultHandler = null;

	public AbsExceptionHandler() {
		// 获取系统默认的uncaughtException处理器
		this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置AbsExceptionHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 获取错误的信息
	 * @param ex
	 * @return
	 */
	protected String getStackTrace(Throwable ex) {
		if (ex == null) {
			return null;
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		printWriter.close();
		String error = writer.toString();
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return error;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (DEBUG) Log.i(TAG, "uncaughtException()");

		if (thread != null) {
			if (DEBUG) Log.i(TAG, "thread != null");
		}else{
			if (DEBUG) Log.e(TAG, "thread == null");
		}

		throwThrowable(thread, ex);

		if (ex != null) {
			Log.i(TAG, "throwable != null");
			String stackTraceText = getStackTrace(ex);
			throwText(thread, stackTraceText);
		}else{
			if (DEBUG) Log.e(TAG, "throwable == null");
		}

		if (this.defaultHandler != null) {
			// 交给系统处理
			defaultHandler.uncaughtException(thread, ex);
		}
	}

	protected abstract void throwText(Thread thread, String stackTraceText);
	protected abstract void throwThrowable(Thread thread, Throwable ex);

}
