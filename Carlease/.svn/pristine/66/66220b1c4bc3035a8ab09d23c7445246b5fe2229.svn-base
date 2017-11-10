package com.tools.app;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.tools.crash.AbsExceptionHandler;
import com.tools.crash.FileCrashHandler;
import com.tools.location.ConvertGps;
import com.tools.os.MemoryInfo;
import com.tools.os.SystemInfo;
import com.tools.os.storage.SDRam;
import com.tools.os.storage.StorageTool;
import com.tools.share.def.ShareDefaultKey;
import com.tools.share.def.ShareDefaultSingle;
import com.tools.sqlite.SQLiteDefaultSingle;
import com.tools.sqlite.SQLiteInfo;
import com.tools.sqlite.def.SQLiteDefaultHelper;
import com.tools.util.Log;

/**
 * 已经证明acvitiyt和service同一个Application是不同的空间， static方法和单例都是不同的。
 * 
 * push service和 remote service启动也会运行Application的onCreate()，所以多考虑一下。 local
 * serivce && local bind serivce && asyncqueue
 * service，则不会运行Application的onCreate() 主要是push service和 remote service是新创建了子进程。
 * 
 * 虽然创建子进程会运行Application的onCreate()，但是是新创建了一个Application对象，
 * 跟Activity的Application不是同一个对象。所以将Activity和Service同用的初始化init()数据，
 * 代码放在Application的onCreate()里，是最好不过了。
 * 
 * 当android:debuggable="false"时，LogCat还会有日志，只是不显示出应用名称，并且Devices里没有进程名。
 * 
 * AndroidManifest.xml文件解析（非常棒）
 * http://blog.csdn.net/xianming01/article/details/7526987
 * 
 * android:vmSafeMode="true"> 指定是否在允许该应用时禁止JIT编译优化 == true为关闭JIT
 * 
 * @author lmc 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public abstract class AbsApplication extends android.app.Application {

	private static final String TAG = AbsApplication.class.getSimpleName();

	protected Context context = null;

	protected ApplicationInfo appInfo = null;

	protected static AbsExceptionHandler exceptionHandler;

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()");
		context = getApplicationContext();
		// 初始化事件
		onInit(context);
		super.onCreate();
	}

	/**
	 * 初始化事件
	 * 
	 * @param context
	 */
	protected void onInit(Context context) {
		Log.d(TAG, "onInit()");

		// 初始化配置信息
		Config.init(context);
		// 打印配置信息
		Config.print();

		// 正式版本要判断debuggable
		if (Config.getVersionDevelop() == Config.VersionDevelop.Final) {
			/**
			 * 正式版时，debuggable设置为假，否则抛出异常
			 */
			checkDebuggable(context);
		}

		// 初始化百度定位库，里面有坐标转换方法
		ConvertGps.initLibrary("Location");

		// 得到App信息
		appInfo = this.getApplicationInfo();
		// 打印应用信息
		printAppInfo(this);

		// 初始化SystemProperty
		initSystemProperty(context);
		// 打印系统的Property
		// printSystemProperty();

		if (Config.isCrashEnable()) { // 是否允捕获crash信息
			// 没有SD卡，也new
			exceptionHandler = new FileCrashHandler(context, new File(
					SDRam.getCrashPath()));
		}

		/**
		 * 打开默认SharePres
		 */
		openSharePresDefault();

		/**
		 * 打开默认数据库
		 */
		openDefaultSQLite(context);

		// 初始化ImageLoader
		// initImageLoader(context);

	}

	/**
	 * 打开默认数据库
	 */
	protected void openDefaultSQLite(Context context) {
		SQLiteInfo sqliteInfo = new SQLiteInfo(context,
				SQLiteDefaultHelper.DB_NAME, SQLiteDefaultHelper.DB_VERSION);

		SQLiteDefaultHelper sqliteHelper = new SQLiteDefaultHelper(context,
				sqliteInfo.getDBName(), sqliteInfo.getVersion());

		SQLiteDefaultSingle.getInstance().init(context, sqliteInfo,
				sqliteHelper);
		boolean openSQLiteDefault = SQLiteDefaultSingle.getInstance().open();
		Log.d(TAG, "openSQLiteDefault:" + openSQLiteDefault);
	}

	/**
	 * 关闭默认数据库
	 */
	protected static void closeDefaultSQLite() {
		SQLiteDefaultSingle.getInstance().close();
	}

	/**
	 * 打开默认SharePres
	 */
	protected void openSharePresDefault() {
		ShareDefaultSingle.getInstance().init(context);
		boolean isOpenShare = ShareDefaultSingle.getInstance().open(
				ShareDefaultKey.getName(), Context.MODE_PRIVATE);
		Log.d(TAG, "isOpenShare:" + isOpenShare);
		if (isOpenShare) {

		}
	}

	/**
	 * 初始化SystemProperty
	 * 
	 * @param context
	 */
	protected static void initSystemProperty(Context context) {
		Log.d(TAG, "initSystemProperty()");
		if (com.tools.os.Build.VERSION.SDK_INT <= com.tools.os.Build.VERSION_CODES.Ver2_2_x) {
			// 模拟器推送服务会出现java.net.SocketException: Bad address family
			// 原因是Android2.2bug不能启用IPV6协议
			java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
			java.lang.System.setProperty("java.net.preferIPv6Addresses",
					"false");
		}
	}

	/**
	 * 初始化ImageLoader
	 * 
	 * @param context
	 */
	// public static void initImageLoader(Context context) {
	// if (context == null) {
	// return;
	// }
	//
	// // 创建图片选项
	// DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
	// // 设置图片在下载期间显示的图片
	// .showStubImage(R.drawable.image_default)
	// // 设置图片Uri为空或是错误的时候显示的图片
	// .showImageForEmptyUri(R.drawable.image_default)
	// // 设置图片加载/解码过程中错误时候显示的图片
	// .showImageOnFail(R.drawable.image_default)
	// // 设置图片在下载前是否重置，复位
	// .resetViewBeforeLoading(true)
	// // 设置下载的图片是否缓存在内存中
	// .cacheInMemory(false)
	// // 设置下载的图片是否缓存在SD卡中
	// .cacheOnDisc(true)
	// // 设置图片的解码类型
	// // .bitmapConfig(Bitmap.Config.RGB_565)
	// // 设置图片的解码配置
	// // .decodingOptions(android.graphics.BitmapFactory.Options
	// decodingOptions)
	// // 设置图片下载前的延迟
	// // .delayBeforeLoading(int delayInMillis)
	// // 设置额外的内容给ImageDownloader
	// // .extraForDownloader(Object extra)
	// // 设置图片加入缓存前，对bitmap进行设置
	// // .preProcessor(BitmapProcessor preProcessor)
	// // 设置显示前的图片，显示后这个图片一直保留在缓存中
	// // .postProcessor(BitmapProcessor postProcessor)
	// // 设置图片以如何的编码方式显示
	// // .imageScaleType(ImageScaleType imageScaleType)
	// // 设置图片显示方式：这里是圆角RoundedBitmapDisplayer
	// // .displayer(new RoundedBitmapDisplayer(20))
	// .build();
	//
	// ImageLoaderConfiguration config = null;
	// ImageLoaderConfiguration.Builder builder2 = new
	// ImageLoaderConfiguration.Builder(context);
	// // ImageLoaderConfiguration config = new
	// ImageLoaderConfiguration.Builder(context)
	// // 设置线程的优先级
	// builder2.threadPriority( Thread.NORM_PRIORITY - 2 )
	// // 线程池大小
	// .threadPoolSize( 5 )
	// // 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
	// .denyCacheImageMultipleSizesInMemory()
	// // 设置缓存文件的名字
	// .discCacheFileNameGenerator(new Md5FileNameGenerator())
	// // 设置图片下载和显示的工作队列排序
	// .tasksProcessingOrder(QueueProcessingType.LIFO)
	// // 设置显示图片选项
	// .defaultDisplayImageOptions( imageOptions );
	// // 设置硬盘缓冲目录
	// // .discCache(new UnlimitedDiscCache( new File(SD.getPath(true) +
	// SDRam.getImageCacheForNews()) )) // You can pass your own disc cache
	// implementation
	// // .build();
	//
	// //判断是否有存储卡（包括内置或是外置）
	// StorageTool tool = new StorageTool(context);
	// if (tool.getMountedPath() != null) {
	// //得到外置存储卡路径
	// String storage = tool.getExternalStoragePath();
	// //判断外置存储卡是否挂载
	// if (tool.isMounted(storage)) {
	// builder2.discCache(new UnlimitedDiscCache( new File(storage +
	// SDRam.getImageCacheForNews()) )); // You can pass your own disc cache
	// implementation
	// }else {
	// storage = tool.getInternalStoragePath();
	// builder2.discCache(new UnlimitedDiscCache( new File(storage +
	// SDRam.getImageCacheForNews()) )); // You can pass your own disc cache
	// implementation
	// }
	// Log.d(TAG, "image cache path:"+storage + SDRam.getImageCacheForNews());
	// }
	// config = builder2.build();
	//
	// // Log.d(TAG, "image cache path:"+SD.getPath(true) +
	// SDRam.getImageCacheForNews());
	//
	// // Initialize ImageLoader with configuration.
	// ImageLoader.getInstance().init(config);
	// }

	/**
	 * 打印应用信息
	 */
	protected void printAppInfo(Application app) {
		Log.d(TAG, "printAppInfo()");
		if (app == null) {
			return;
		}

		Context context = app.getApplicationContext();

		boolean debuggable = isDebuggable(context);

		android.util.Log.i(TAG, "------ 应用信息 start ------");
		AppInfo appInfo = new AppInfo(context);
		android.util.Log.i(TAG, "版本:" + appInfo.getVersion());
		android.util.Log.i(TAG, "debuggable:" + debuggable);
		android.util.Log.i(TAG, "------ 应用信息 end ------");
	}

	/**
	 * 打印系统Property
	 */
	protected void printSystemProperty() {
		Log.d(TAG, "printSystemProperty()");
		// java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
		// java.lang.System.setProperty("java.net.preferIPv6Addresses",
		// "false");
		android.util.Log.i(TAG, "------ 系统Property start ------");
		android.util.Log.i(
				TAG,
				"java.net.preferIPv4Stack:"
						+ java.lang.System
								.getProperty("java.net.preferIPv4Stack"));
		android.util.Log.i(TAG, "java.net.preferIPv6Addresses:"
				+ java.lang.System.getProperty("java.net.preferIPv6Addresses"));
		android.util.Log.i(TAG,
				"Java 运行时环境版本:" + java.lang.System.getProperty("java.version"));
		android.util.Log.i(TAG,
				"Java 运行时环境供应商:" + java.lang.System.getProperty("java.vendor"));
		android.util.Log.i(
				TAG,
				"Java 供应商的 URL:"
						+ java.lang.System.getProperty("java.vendor.url"));
		android.util.Log.i(TAG,
				"Java 安装目录:" + java.lang.System.getProperty("java.name"));
		android.util.Log.i(
				TAG,
				"Java 虚拟机实现版本:"
						+ java.lang.System.getProperty("java.vm.version"));
		android.util.Log.i(
				TAG,
				"Java 虚拟机实现供应商:"
						+ java.lang.System.getProperty("java.vm.vendor"));
		android.util.Log.i(TAG,
				"Java 虚拟机实现名称:" + java.lang.System.getProperty("java.vm.name"));
		android.util.Log.i(
				TAG,
				"Java 虚拟机规范版本:"
						+ java.lang.System
								.getProperty("java.vm.specification.version"));
		android.util.Log.i(
				TAG,
				"Java 虚拟机规范供应商:"
						+ java.lang.System
								.getProperty("java.vm.specification.vendor"));
		android.util.Log.i(
				TAG,
				"Java 虚拟机规范名称:"
						+ java.lang.System
								.getProperty("java.vm.specification.name"));
		android.util.Log.i(
				TAG,
				"Java 运行时环境规范版本："
						+ java.lang.System
								.getProperty("java.specification.version"));
		android.util.Log.i(
				TAG,
				"Java 运行时环境规范供应商："
						+ java.lang.System
								.getProperty("java.specification.vender"));
		android.util.Log.i(
				TAG,
				"Java 运行时环境规范名称："
						+ java.lang.System
								.getProperty("java.specification.name"));
		android.util.Log.i(TAG,
				"操作系统的名称:" + java.lang.System.getProperty("os.name"));
		android.util.Log.i(TAG,
				"操作系统的架构:" + java.lang.System.getProperty("os.arch"));
		android.util.Log.i(TAG,
				"操作系统的版本:" + java.lang.System.getProperty("os.version"));
		android.util.Log.i(
				TAG,
				"Java 类格式版本号:"
						+ java.lang.System.getProperty("java.class.version"));
		android.util.Log.i(TAG,
				"Java 类路径:" + java.lang.System.getProperty("java.class.path"));
		android.util.Log.i(
				TAG,
				"加载库时搜索的路径列表:"
						+ java.lang.System.getProperty("java.library.path"));
		android.util.Log.i(TAG,
				"默认的临时文件路径:" + java.lang.System.getProperty("java.io.tmpdir"));
		android.util.Log.i(
				TAG,
				"要使用的 JIT 编译器的名称:"
						+ java.lang.System.getProperty("java.compiler"));
		android.util.Log
				.i(TAG,
						"一个或多个扩展目录的路径:"
								+ java.lang.System.getProperty("java.ext.dirs"));
		android.util.Log.i(
				TAG,
				"文件分隔符（在 UNIX 系统中是\"/\"）:"
						+ java.lang.System.getProperty("file.separator"));
		android.util.Log.i(
				TAG,
				"路径分隔符（在 UNIX 系统中是\":\"）:"
						+ java.lang.System.getProperty("path.separator"));
		android.util.Log.i(
				TAG,
				"行分隔符（在 UNIX 系统中是\"/n\"）:"
						+ java.lang.System.getProperty("line.separator"));
		android.util.Log.i(TAG,
				"用户的账户名称:" + java.lang.System.getProperty("user.name"));
		android.util.Log.i(TAG,
				"用户的主目录:" + java.lang.System.getProperty("user.home"));
		android.util.Log.i(TAG,
				"用户的当前工作目录:" + java.lang.System.getProperty("user.dir"));
		android.util.Log.i(TAG,
				"文件编码:" + java.lang.System.getProperty("file.encoding"));
		android.util.Log.i(TAG, "------ 系统Property end ------");
	}

	/**
	 * 应用启动事件
	 * 
	 * @param context
	 */
	protected static void onLaunched(android.support.v4.app.FragmentActivity ui) {
		Log.d(TAG, "onLaunched()");

		Context context = ui.getApplicationContext();

		// 打印系统信息
		SystemInfo systemTool = new SystemInfo(ui);
		systemTool.print();

		// 初始化日志 MainApplication是应用全程共享的单例,也有被回收的情况
		logInit(context);
	}

	/**
	 * 退出应用事件
	 * 
	 * @param context
	 */
	protected static void onQuit(Context context) {
		Log.d(TAG, "onQuit(Context)");

		// close flowLog
		if (Config.isFlowLogEnable()) {
			// 关闭日志记录
			com.tools.util.LogcatHelper.getInstance().stop();
		}

		/**
		 * 关闭默认数据库
		 */
		closeDefaultSQLite();
	}

	/**
	 * 退出应用事件
	 * 
	 * @param ui
	 */
	protected static void onQuit(android.support.v4.app.FragmentActivity ui) {
		Log.d(TAG, "onQuit(FragmentActivity)");
		onQuit(ui.getApplicationContext());
	}

	/**
	 * 结束应用
	 */
	protected static void exit() {
		Log.d(TAG, "exit()");
		// 结束应用
		// android.os.Process.killProcess( android.os.Process.myPid() ); // 这个OK
		System.exit(0); // 这个OK
		/*
		 * 所以如果要关闭整个应用程序的话只需要运行以下两行代码就行： ActivityManager activityMgr=
		 * (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		 * activityMgr.restartPackage(getPackageName()); 最后还需要添加这个权限才行： <!--
		 * 关闭应用程序的权限 --> <uses-permission
		 * android:name="android.permission.RESTART_PACKAGES" />
		 */
	}

	@Override
	public void onTerminate() {
		Log.d(TAG, "onTerminate()");
		super.onTerminate();
	}

	@Override
	public void onLowMemory() {
		Log.d(TAG, "onLowMemory()");
		super.onLowMemory();
	}

	@Override
	protected void finalize() throws Throwable {
		Log.d(TAG, "finalize()");
		// 退出应用事件
		onQuit(context);
		super.finalize();
	}

	/**
	 * 初始化日志
	 * 
	 * @date 2013-10-29 下午4:10:15
	 * @author aaa
	 */
	protected static void logInit(Context context) {
		Log.d(TAG, "logInit()");

		// 获取SD目录
		String sdDir;
		// SD卡操作类
		StorageTool sTool = new StorageTool(context);
		if (sTool.getMountedPath() != null) {
			// 优先保存到SD卡中
			sdDir = sTool.getMountedPath();
		} else {
			// 如果内置和外置的SD卡都不存在，就不进行日志记录
			return;
		}
		// 流程日志目录
		String flowLogDir = sdDir + SDRam.getFlowLogPath();
		// 状态日志目录
		String statusLogDir = sdDir + SDRam.getStatusLogPath();
		// 创建日志目录
		File flowLogFile = new File(flowLogDir);
		File statusLogFile = new File(statusLogDir);
		if (!flowLogFile.exists()) {
			// 创建流程日志目录
			flowLogFile.mkdirs();
		}
		if (!statusLogFile.exists()) {
			// 创建状态日志目录
			statusLogFile.mkdirs();
		}
		if (Config.isFlowLogEnable()) {
			// 初始化流程日志,日志的保留时间、大小配置，可以getInstance()通过get/set获取/设置
			com.tools.util.LogcatHelper.getInstance().logcatHelperInit(context,
					flowLogDir);
			// 运行流程日志记录
			com.tools.util.LogcatHelper.getInstance().start(context);
		}
		if (Config.isStatusLogEnable()) {
			// 初始化状态记录, 日志的保留时间、大小配置，可以getInstance()通过get/set获取/设置
			com.tools.util.StatusLog.getInstance().init(context, statusLogDir);
		}
		// 打印应用信息
		AppInfo info = new AppInfo(context);
		Log.d(TAG, TAG + " start Log\n");
		info.print();

		// 打印内存信息
		MemoryInfo memoryInfo = new MemoryInfo(context);
		Log.d(TAG, "\n------------ 内存信息 start ------------\n");
		Log.d(TAG, "" + memoryInfo.toCrashString());
		Log.d(TAG, "\n------------ 内存信息 end ------------\n");
	}

	/**
	 * 返回App里的debuggable值
	 * 
	 * @param context
	 * @return
	 */
	protected boolean isDebuggable(Context context) {
		Log.d(TAG, "isDebuggable()");

		boolean debuggable = false;

		// 应用信息对象
		ApplicationInfo applicationInfo = null;

		applicationInfo = context.getApplicationInfo();
		if (applicationInfo != null) {
			if ((applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE) {
				debuggable = true;
			}
		}

		Log.d(TAG, "isDebuggable():" + debuggable);
		return debuggable;
	}

	/**
	 * 正式版时，debuggable设置为假，否则抛出异常
	 * 
	 * @param context
	 */
	protected void checkDebuggable(Context context) {
		Log.d(TAG, "checkDebuggable()");
		if (isDebuggable(context) == true) {
			Log.throwException("当正式版本时，AndroidManifest.xml里的debuggable要设置为false");
		}
	}

}
