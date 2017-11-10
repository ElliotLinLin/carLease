package com.tools.crash;

import java.io.File;
import java.io.IOException;

import android.content.Context;

import com.tools.app.AppInfo;
import com.tools.content.pm.PermissionTool;
import com.tools.net.NetworkManager;
import com.tools.os.MemoryInfo;
import com.tools.os.SystemInfo;
import com.tools.os.SystemPropertyInfo;
import com.tools.os.storage.StorageTool;
import com.tools.telephony.TelephonyInfo;
import com.tools.util.DatetimeUtil;
import com.tools.util.Log;


/**
 * 继承AbsExceptionHandler抽象类，实现二个方法
 * 这个类是将异常日志保存到本地文件里。
 *
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class FileCrashHandler extends AbsExceptionHandler {

	private static final String TAG = FileCrashHandler.class.getSimpleName();

	protected Context context = null;

	protected AppInfo appInfo;
	protected SystemInfo systemInfo;
	protected MemoryInfo memoryInfo;
	protected NetworkManager networkManager;
	protected TelephonyInfo telephonyInfo;
	protected SystemPropertyInfo systemPropertyInfo;

	protected File dir = null; // 目录路径

	protected File file = null;
	protected java.io.FileOutputStream fileOutputStream = null;

	/**
	 * @param context
	 * @param dir 目录路径
	 */
	public FileCrashHandler(Context context, File dir) {
		super();

		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

		this.context = context;
		this.dir = dir;

		this.appInfo = new AppInfo(context);
		this.systemInfo = new SystemInfo(context);
		this.memoryInfo = new MemoryInfo(context);
		this.networkManager = new NetworkManager(context);
		this.telephonyInfo = new TelephonyInfo(context);
		this.systemPropertyInfo = new SystemPropertyInfo(context);

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

	@Override
	public void throwText(Thread thread, String stackTraceText) {
		Log.e(TAG, "throwText() start");

		// 创建日志保存路径
		String curDatetime = getCurrentDatetime();
		String logPath = createLogPath(this.dir, curDatetime);
		if (!isEmptyString(logPath)) {
			// 打开
			boolean bOpen = open(new File(logPath));
			if (bOpen) {
				// 写入
				write("\n------------------------ start ------------------------\n");

				write("崩溃时的手机时间:"+curDatetime+"\n");

				write("\n------------ 应用信息 start ------------\n");
				write(appInfo.toCrashString());
				write("\n------------ 应用信息 end ------------\n");

				write("\n------------ 堆栈信息 start ------------\n");
				write(stackTraceText);
				write("\n------------ 堆栈信息 end ------------\n");

				write("\n------------ 内存信息 start ------------\n");
				write(memoryInfo.toCrashString());
				write("\n------------ 内存信息 end ------------\n");

				write("\n------------ 系统信息 start ------------\n");
				write(systemInfo.toCrashString());
				write("\n------------ 系统信息 end ------------\n");

				write("\n------------ 网络信息 start ------------\n");
				write(networkManager.toCrashString());
				write("\n------------ 网络信息 end ------------\n");

				write("\n------------ 电话信息 start ------------\n");
				write(telephonyInfo.toCrashString());
				write("\n------------ 电话信息 end ------------\n");

				write("\n------------ 虚拟机系统的Property信息 start ------------\n");
				write(systemPropertyInfo.toCrashString());
				write("\n------------ 虚拟机系统的Property信息 end ------------\n");

				write("\n------------------------ end ------------------------\n");
				// 关闭文件
				close();
			}
		}

		Log.e(TAG, "throwText() end");
	}

	@Override
	public void throwThrowable(Thread thread, Throwable ex) {
		Log.e(TAG, "throwThrowable(..) start");
		if (thread != null) {
			Log.e(TAG, "throwThrowable() thread != null");
		}else{
			Log.e(TAG, "throwThrowable() thread == null");
		}
		Log.e(TAG, "throwThrowable(..) end");
	}

	/**
	 * 得到当前时间
	 *
	 * @return
	 */
	protected String getCurrentDatetime() {
		return DatetimeUtil.getCurrent("yyyy-MM-dd HH.mm.ss.SSS");
	}

	/**
	 * 创建目录
	 */
	protected void makeDirs(File dir) {
		if (dir == null) {
			return;
		}
		// 判断文件是否存在
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 创建日志保存路径
	 */
	protected String createLogPath(File dir, String time) {
		if (dir == null) {
			return null;
		}

		if (isEmptyString(time)) {
			return null;
		}

		String path = null;

		// 如果外置存储卡不存在，则保存在内置存储卡，如果内置存储卡有的话。
		StorageTool storageTool = new StorageTool(context);
		path = storageTool.getMountedPath();

		Log.i(TAG, "storageTool.getMountedPath():"+storageTool.getMountedPath());
		Log.i(TAG, "storageTool.getPrimaryPath():"+storageTool.getPrimaryPath());
		Log.i(TAG, "storageTool.isMounted( getPrimaryPath() ):"+storageTool.isMounted(storageTool.getPrimaryPath()));
		Log.i(TAG, "storageTool.isMounted( getMountedPath() ):"+storageTool.isMounted(storageTool.getMountedPath()));
		//		if (storageTool.isMounted(storageTool.getPrimaryPath()) == false) {
		//			// 没有存储卡不存在
		//			return null;
		//		}

		if (isEmptyString(path)) {
			return null;
		}

		// 创建文件，以日期和时间来创建
		String logPath = String.format("%s%s/%s.log", path, dir.getAbsolutePath(), time);
		Log.i(TAG, "logPath:"+logPath);

		return logPath;
	}

	/**
	 * 打开文件
	 *
	 * @param file
	 * @return
	 */
	protected boolean open(File file) {
		if (file == null) {
			return false;
		}

		this.file = file;

		// 取出父目录，用于创建目录
		makeDirs(file.getParentFile());

		try {
			if (fileOutputStream == null) {
				fileOutputStream = new java.io.FileOutputStream(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 写入文件
	 *
	 * @param text
	 * @return
	 */
	protected boolean write(String text) {
		if (text == null) {
			return false;
		}

		try {
			fileOutputStream.write(text.getBytes());
			fileOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 关闭文件
	 */
	protected void close() {
		try {
			if (fileOutputStream != null) {
				fileOutputStream.close();
				fileOutputStream = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
