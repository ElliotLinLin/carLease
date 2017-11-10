package com.tools.app;

import java.io.File;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

import com.tools.util.Log;

/**
 * 应用信息
 * 
 * 应用安装目录默认有五个目录，分别为：
 * cache、files、databases、lib、shared_prefs
 * 其中：
 * databases、lib、shared_prefs三个目录不能拷贝至SD卡里，
 * 只有cache、files二个目录可以拷贝至SD卡，
 * 所以新建的databases和shared_prefs文件都会保存在手机内部存储里，不是SD卡里。
 * 
 * @author LMC
 *
 */
public class AppInfo {

	private static final String TAG = AppInfo.class.getSimpleName();

	protected Context context = null;

	protected PackageInfo info = null;

	private static final String Data_data = "/data/data";
	private static final String Android_data = "/Android/data";

	public static final String Cache = "cache";
	public static final String Files = "files";
	public static final String Databases = "databases";
	public static final String Lib = "lib";
	public static final String Shared_prefs = "shared_prefs";

	private static final String Underline = "/";

	public AppInfo(Context context) {
		init(context);
	}

	private void init(Context context) {
		if (context == null) {
			// 不要throw
			return;
		}
		this.context = context;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 得到包名
	 * 
	 * @return
	 */
	public String getPackageName() {
		if (context == null) {
			return "";
		}
		return context.getPackageName();
	}

	/**
	 * 应用名称
	 * 
	 * @return
	 */
	public String getName() {
		if (context == null || info == null) {
			return "";
		}
		CharSequence cs = info.applicationInfo.loadLabel(context.getPackageManager());
		if (cs == null) {
			return "";
		}
		return cs.toString();
	}

	/**
	 * 得到版本
	 * 
	 * @return
	 */
	public String getVersion() {
		if (info == null) {
			return "";
		}
		return getVersionName()+"."+getVersionCode();
	}

	/**
	 * 得到版本名称
	 * 
	 * @return
	 */
	public String getVersionName() {
		return info.versionName;
	}

	/**
	 * 得到版本代码
	 * 
	 * @return
	 */
	public int getVersionCode() {
		return info.versionCode;
	}

	/**
	 * 判断应用是否安装在存储卡里
	 * 
	 * flags字段： FLAG_SYSTEM　系统应用程序
	 * FLAG_EXTERNAL_STORAGE　表示该应用安装在sdcard中
	 * 
	 * @return
	 */
	public boolean isInstallOnStorage() {

		boolean isInstallOnStorage = false;

		PackageManager pm = context.getPackageManager();
		ApplicationInfo appInfo = null;
		try {
			appInfo = pm.getApplicationInfo(getPackageName(), 0);
			if ((appInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
				isInstallOnStorage = true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Log.i(TAG, "isInstallOnStorage:"+isInstallOnStorage);
		return isInstallOnStorage;
	}

	/**
	 * 获取内部存储上的cache路径
	 * /data/data/com.company.prject/cache
	 * 
	 * @return
	 */
	public File getInternalCacheDir() {
		return context.getCacheDir();
	}

	/**
	 * 获取内部存储上的files路径
	 * /data/data/com.company.prject/files
	 * 
	 * @return
	 */
	public File getInternalFilesDir() {
		return context.getFilesDir();
	}

	/**
	 * 获取内部存储上的databases路径
	 * /data/data/com.company.prject/databases
	 * 
	 * @return
	 */
	public File getInternalDatabasesDir() {
		return new File(Data_data+Underline+getPackageName()+Underline+Databases);
	}

	/**
	 * 通过文件名得到跟数据库文件的内部存储绝对路径
	 * 
	 * @return
	 */
	public File getInternalDatabasePath(String name) {
		if (isEmptyString(name)) {
			return null;
		}

		return context.getDatabasePath( name );
	}

	/**
	 * 获取内部存储上的lib路径
	 * /data/data/com.company.prject/lib
	 * 
	 * @return
	 */
	public File getInternalLibDir() {
		return new File(Data_data+Underline+getPackageName()+Underline+Lib);
	}

	/**
	 * 获取内部存储上的shared_prefs路径
	 * /data/data/com.company.prject/shared_prefs
	 * 
	 * @return
	 */
	public File getInternalSharedPrefsDir() {
		return new File(Data_data+Underline+getPackageName()+Underline+Shared_prefs);
	}

	/**
	 * 获取内部存储上的应用路径
	 * /data/data/com.company.prject
	 * 
	 * @return
	 */
	public File getInternalDir() {
		return new File(Data_data+Underline+getPackageName());
	}

	/**
	 * 获取存储卡上的cache路径
	 * /mnt/sdcard/Android/data/com.company.prject/cache
	 * 
	 * @return
	 */
	public File getExternalCacheDir() {
		if (com.tools.os.Build.VERSION.SDK_INT < com.tools.os.Build.VERSION_CODES.Level18) {
			return null;
		}
		return context.getExternalCacheDir(); // level8及以上才可用。
	}

	/**
	 * 获取存储卡上的files路径
	 * /mnt/sdcard/Android/data/com.company.prject/files
	 * 
	 * @return
	 */
	public File getExternalFilesDir() {
		if (com.tools.os.Build.VERSION.SDK_INT < com.tools.os.Build.VERSION_CODES.Level18) {
			return null;
		}
		return context.getExternalFilesDir(null); // level8及以上才可用。
	}

	/**
	 * 获取存储卡上的应用路径
	 * /mnt/sdcard/Android/data/com.company.prject
	 * 
	 * @return
	 */
	public File getExternalDir() {
		return new File(Environment.getExternalStorageDirectory()+Android_data+Underline+getPackageName());
	}

	/**
	 * 自动判断cache目录所在
	 * 
	 * @return
	 */
	public File getCacheDir() {
		if (isInstallOnStorage()) {
			return getExternalCacheDir();
		}
		return getInternalCacheDir();
	}

	/**
	 * 自动判断files目录所在
	 * 
	 * @return
	 */
	public File getFilesDir() {
		if (isInstallOnStorage()) {
			return getExternalFilesDir();
		}
		return getInternalFilesDir();
	}

	/**
	 * 自动判断app根目录所在
	 * 
	 * @return
	 */
	public File getDir() {
		if (isInstallOnStorage()) {
			return getExternalDir();
		}
		return getInternalDir();
	}

	/**
	 * 02-10 09:47:26.452: E/AppInfo(1940): ------------------ start AppInfo ------------------
02-10 09:47:26.452: E/AppInfo(1940): getCacheDir:/data/data/com.company.prject/cache
02-10 09:47:26.454: E/AppInfo(1940): getFilesDir:/data/data/com.company.prject/files
02-10 09:47:26.454: E/AppInfo(1641): getDatabasePath:/data/data/com.company.prject/databases/abc.db
02-10 09:47:26.455: E/AppInfo(1940): getPackageResourcePath:/data/app/com.company.prject-1.apk
02-10 09:47:26.456: E/AppInfo(1940): getPackageCodePath:/data/app/com.company.prject-1.apk
02-10 09:47:26.457: E/AppInfo(1940): getExternalCacheDir:/mnt/sdcard/Android/data/com.company.prject/cache
02-10 09:47:26.458: E/AppInfo(1940): getExternalFilesDir(null):/mnt/sdcard/Android/data/com.company.prject/files
02-10 09:47:26.459: E/AppInfo(1940): getExternalFilesDir(Environment.DIRECTORY_ALARMS):/mnt/sdcard/Android/data/com.company.prject/files/Alarms
02-10 09:47:26.459: E/AppInfo(1940): getExternalFilesDir(Environment.DIRECTORY_DCIM):/mnt/sdcard/Android/data/com.company.prject/files/DCIM
02-10 09:47:26.460: E/AppInfo(1940): getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS):/mnt/sdcard/Android/data/com.company.prject/files/Download
02-10 09:47:26.461: E/AppInfo(1940): getExternalFilesDir(Environment.DIRECTORY_MOVIES):/mnt/sdcard/Android/data/com.company.prject/files/Movies
02-10 09:47:26.462: E/AppInfo(1940): getExternalFilesDir(Environment.DIRECTORY_MUSIC):/mnt/sdcard/Android/data/com.company.prject/files/Music
02-10 09:47:26.462: E/AppInfo(1940): getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS):/mnt/sdcard/Android/data/com.company.prject/files/Notifications
02-10 09:47:26.463: E/AppInfo(1940): getExternalFilesDir(Environment.DIRECTORY_PICTURES):/mnt/sdcard/Android/data/com.company.prject/files/Pictures
02-10 09:47:26.464: E/AppInfo(1940): getExternalFilesDir(Environment.DIRECTORY_PODCASTS):/mnt/sdcard/Android/data/com.company.prject/files/Podcasts
02-10 09:47:26.464: E/AppInfo(1940): getExternalFilesDir(Environment.DIRECTORY_RINGTONES):/mnt/sdcard/Android/data/com.company.prject/files/Ringtones
02-10 09:47:26.465: E/AppInfo(1940): ------------------ end AppInfo ------------------
	 */
	public void printContextInfo() {
		Log.e(TAG, "------------------ start AppInfo ContextInfo ------------------");

		Log.e(TAG, "getCacheDir:"+context.getCacheDir());
		Log.e(TAG, "getFilesDir:"+context.getFilesDir());

		//		Log.e(TAG, "getDatabasePath:"+context.getDatabasePath(null)); // 出错
		Log.e(TAG, "getDatabasePath:"+context.getDatabasePath("abc.db"));
		//		Log.e(TAG, "getDatabasePath:"+context.getDatabasePath(""));  // 出错

		Log.e(TAG, "getPackageResourcePath:"+context.getPackageResourcePath());
		Log.e(TAG, "getPackageCodePath:"+context.getPackageCodePath());

		Log.e(TAG, "getExternalCacheDir:"+context.getExternalCacheDir());
		Log.e(TAG, "getExternalFilesDir(null):"+context.getExternalFilesDir(null));
		Log.e(TAG, "getExternalFilesDir(Environment.DIRECTORY_ALARMS):"+context.getExternalFilesDir(Environment.DIRECTORY_ALARMS));
		Log.e(TAG, "getExternalFilesDir(Environment.DIRECTORY_DCIM):"+context.getExternalFilesDir(Environment.DIRECTORY_DCIM));
		Log.e(TAG, "getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS):"+context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
		Log.e(TAG, "getExternalFilesDir(Environment.DIRECTORY_MOVIES):"+context.getExternalFilesDir(Environment.DIRECTORY_MOVIES));
		Log.e(TAG, "getExternalFilesDir(Environment.DIRECTORY_MUSIC):"+context.getExternalFilesDir(Environment.DIRECTORY_MUSIC));
		Log.e(TAG, "getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS):"+context.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS));
		Log.e(TAG, "getExternalFilesDir(Environment.DIRECTORY_PICTURES):"+context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
		Log.e(TAG, "getExternalFilesDir(Environment.DIRECTORY_PODCASTS):"+context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS));
		Log.e(TAG, "getExternalFilesDir(Environment.DIRECTORY_RINGTONES):"+context.getExternalFilesDir(Environment.DIRECTORY_RINGTONES));

		Log.e(TAG, "------------------ end AppInfo ContextInfo ------------------");
	}

	public void print() {
		Log.e(TAG, "------------------ start AppInfo ------------------");

		Log.e(TAG, "isInstallOnStorage:"+isInstallOnStorage());

		Log.e(TAG, "getInternalCacheDir:"+getInternalCacheDir());
		Log.e(TAG, "getInternalFilesDir:"+getInternalFilesDir());
		Log.e(TAG, "getInternalDatabasesDir:"+getInternalDatabasesDir());
		Log.e(TAG, "getInternalDatabasePath:"+getInternalDatabasePath("aaa.db"));
		Log.e(TAG, "getInternalLibDir:"+getInternalLibDir());
		Log.e(TAG, "getInternalSharedPrefsDir:"+getInternalSharedPrefsDir());
		Log.e(TAG, "getInternalDir:"+getInternalDir());

		Log.e(TAG, "getExternalCacheDir:"+getExternalCacheDir());
		Log.e(TAG, "getExternalFilesDir:"+getExternalFilesDir());
		Log.e(TAG, "getExternalDir:"+getExternalDir());

		Log.e(TAG, "getCacheDir:"+getCacheDir());
		Log.e(TAG, "getFilesDir:"+getFilesDir());
		Log.e(TAG, "getDir:"+getDir());

		Log.e(TAG, "------------------ end AppInfo ------------------");
	}

	/*
	 * 打印（包信息+app信息）
	 *
	 * android.content.pm
	 * 
	 * ActivityInfo
	 * ApplicationInfo
	 * ComponentInfo
	 * ConfigurationInfo
	 * FeatureInfo
	 * PackageInfo
	 * PackageItemInfo
	 * ProviderInfo
	 * ResolveInfo
	 * ServiceInfo
	 *
	 */
	public String toCrashString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("包名:");
		buffer.append( getPackageName() );
		buffer.append("\n");
		buffer.append("应用名称:");
		buffer.append( getName() );
		buffer.append("\n");
		buffer.append("版本:");
		buffer.append( getVersion() );
		return buffer.toString();
	}

}
