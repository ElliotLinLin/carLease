package com.hst.Carlease.app;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.hst.Carlease.R;
import com.hst.Carlease.ram.SQLiteRam;
import com.hst.Carlease.ram.ShareSet;
import com.hst.Carlease.share.ShareSingle;
import com.hst.Carlease.sqlite.SQLiteHelper;
import com.hst.Carlease.util.MyImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.crashreport.CrashReport;
import com.tools.sqlite.SQLiteInfo;
import com.tools.sqlite.SQLiteSingle;
import com.tools.util.Log;

/**
 * MainApplication
 * 
 * @author lmc 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class MainApplication extends com.tools.app.AbsApplication {

	private static final String TAG = MainApplication.class.getSimpleName();
	public static String appName = ""; // 应用名称
	private float density;

	private static int screenWidth = 720;

	private static int screenHeight = 1280;
	private static MainApplication instance;
	private static Context mContext;

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()");
		super.onCreate();
		// 初始化
		instance = this;
		init(context);
		mContext = this;
		
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		Log.d(TAG, "init()");
		appName = getString(R.string.app_name);
		// sharepres
		ShareSingle.getInstance().init(context);
		// open
		boolean bOpenShare = ShareSingle.getInstance().open(ShareSet.getName(),
				Context.MODE_PRIVATE);
		Log.d(TAG, "init():bOpenShare:" + bOpenShare);
		if (bOpenShare) {
			// 初始化数据key

		}
		CrashReport.initCrashReport(context, "bdaaeb64ad", true);
		// sqlite
		SQLiteInfo sqliteInfo = new SQLiteInfo(context, SQLiteRam.getName(),
				SQLiteRam.getVersion());
		SQLiteHelper sqliteHelper = new SQLiteHelper(context,
				sqliteInfo.getDBName(), sqliteInfo.getVersion());
		SQLiteSingle.getInstance().init(context, sqliteInfo, sqliteHelper);
		boolean bOpenSQLite = SQLiteSingle.getInstance().open();
		Log.d(TAG, "init():bOpenSQLite:" + bOpenSQLite);
		// 初始化ImageLoader
		instance.initScreenSize();
		initImageLoad(context);
	}

	private void initScreenSize() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
		density = mDisplayMetrics.density;
		screenWidth = mDisplayMetrics.widthPixels;
		screenHeight = mDisplayMetrics.heightPixels;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public static MainApplication getInstance() {
		return instance;
	}

	public float getDensity() {
		return density;
	}

	/**
	 * 应用启动
	 * 
	 * @param context
	 */
	public static void launch(android.support.v4.app.FragmentActivity ui) {
		Log.d(TAG, "launch()");
		// 调父类
		onLaunched(ui);
		// ...
	}

	public static Context getmContext() {
		return mContext;
	}

	public static void setmContext(Context mContext) {
		MainApplication.mContext = mContext;
	}

	/**
	 * 退出应用
	 * 
	 * @param ui
	 */
	public static void quit(android.support.v4.app.FragmentActivity ui) {
		Log.d(TAG, "quit(FragmentActivity)");
		// 调父类
		onQuit(ui);
		// close sqlite
		SQLiteSingle.getInstance().close();
		// 结束应用
		exit();
	}

	/**
	 * 初始化图片加载保存类 void
	 * 
	 */
	public void initImageLoad(Context context) {
		ImageLoaderConfiguration config = MyImageLoader
				.initImageLoader(context);

		ImageLoader.getInstance().init(config);
	}
}
