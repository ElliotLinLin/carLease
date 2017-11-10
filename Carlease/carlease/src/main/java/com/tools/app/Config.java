package com.tools.app;

import android.content.Context;

/**
 * 配置信息
 * 
 * @author lmc
 * 
 */
public class Config {

	private static final String TAG = Config.class.getSimpleName();

	// 打印日志Log开关
	private static boolean logEnabled = true;

	// 是否使用crash功能
	private static boolean crashEnabled = true;

	// 流程日志开关,只用于判断是否要初始化流程日志记录线程
	private static boolean flowLogEnabled = true;

	// 推送日志开关
	private static boolean pushLogEnabled = true;

	// 状态日志开关
	private static boolean statusLogEnabled = true;

	// 是否可模拟数据调试 TODO 弃用
	// private static boolean debugEnabled = true;

	// 是否支持monkey测试（可通过sd里的xml文件来配置，只有测试版才可配置）
	private static boolean monkeyEnabled = false;

	// 内网或者外网 == true为外网；==false为内网
	private static boolean outerNet = false;

	// 版本开发阶段
	private static VersionDevelop versionDevelop = VersionDevelop.Alphal;

	// 版本类型
	private static VersionType versionType = VersionType.Free;

	// 合作项目类型
	private static ProjectType projectType = ProjectType.Default;

	// 地图
	private static MapType mapType = MapType.Baidu;

	/**
	 * 版本开发阶段
	 * 
	 * @author LMC
	 * 
	 */
	public enum VersionDevelop {
		Alphal, // 内部测试版 (Alpha版本的产品仍然需要完整的功能测试，而其功能亦未完善，但是可以满足一般需求。)
		Beta, // 外部测试版(是由公众参与的测试阶段。)
		Gamma, // 相当成熟的测试版，与即将发行的正式版相差无几。
		RC, // 候选版本，发布倒计时，处于Gamma阶段，
		// 该版本已经完成全部功能并清除大部分的BUG。到了这个阶段只会除BUG，不会对软件做任何大的更改。
		Final, // 正式版。
		SR // 修正版或更新版，修正了正式版推出后发现的Bug。
	}

	/**
	 * 版本类型
	 * 
	 * @author LMC
	 * 
	 */
	public enum VersionType {
		Free, // 自由版
		Demo, // 演示版
		Trial, // 试用版
		Enhance, // 增强版或者加强版 属于正式版
		Full, // 完全版 属于正式版
		Shareware, // 共享版
		Upgrade, // 升级版
		Retail, // 零售版
		Plus, // 属增强版，不过这种大部分是在程序界面及多媒体功能上增强。
		Preview, // 预览版
		Standard, // 标准版
		Professional, // 专业版
		Mini, // 迷你版也叫精简版只有最基本的功能
		Express, // 特别版
		Deluxe // 豪华版
	}

	/**
	 * 合作项目名称
	 * 
	 * @author LMC
	 * 
	 */
	public enum ProjectType {
		Default, // 默认为本项目
		ChinaPnr, // 汇付天下
	}

	/**
	 * 地图
	 * 
	 * @author LMC
	 * 
	 */
	public enum MapType {
		Baidu, // 百度地图（默认）
		AMap, // 高德地图
		QQSOSO, // 腾讯搜搜地图
		MapBar, // 图吧地图
		Google // 谷歌地图
	}

	/**
	 * 初始化---配置信息
	 */
	public static void init(Context context) {

		// 配置是否可模拟数据调试
		// setDebugEnabled( false );

		// 配置是否支持Monkey测试
		setMonkeyEnabled(false);

		// 配置内网或者外网( == true为外网；==false为内网 )
		setOuterNet(false); // 内网
		// setOuterNet( true ); // 外网

		// 配置开发阶段版本
//		setVersionDevelop(VersionDevelop.Alphal); // 内网+测试版（默认）
		 setVersionDevelop( VersionDevelop.Beta ); // 外网+测试版/
		/* 正式版本要修改三处：
		 * 1)Config.java设置为VersionDevelop.Final
		 * 2)AndroidManifest.xml里的android:debuggable="false"
		 * 3)混淆project.properties加入proguard.config=proguard.cfg
		 * */
//		 setVersionDevelop( VersionDevelop.Final ); // 外网+正式版
		// 设置合作项目类型
		setProjectType(ProjectType.Default); // 本项目（默认）
		// setProjectType( ProjectType.ChinaPnr ); // 汇付天下

		// 设置地图
		setMapType(MapType.Baidu); // 百度地图（默认）
		// setMapType( MapType.AMap ); // 高德地图

		// 执行配置信息
		execute(context);

	}

	/**
	 * 执行配置
	 * 
	 * @param context
	 */
	public static void execute(Context context) {

		VersionDevelop versionDevelop = getVersionDevelop();

		if (versionDevelop == VersionDevelop.Alphal) {
			// 内部测试版
			setOuterNet(false);
		} else if (versionDevelop == VersionDevelop.Beta) {
			// 外部测试版
			setOuterNet(true);
		} else if (versionDevelop == VersionDevelop.Gamma) {
			// 相当成熟的测试版

		} else if (versionDevelop == VersionDevelop.RC) {
			// 候选版本
			setOuterNet(true);
		} else if (versionDevelop == VersionDevelop.Final || versionDevelop == VersionDevelop.SR) {
			// 正式版 || 正式版之后的修正版或更新版

			// 配置内网或者外网( == true为外网；==false为内网 )
			setOuterNet(true);
			// 配置是否可打印日志
			setLogEnabled(false);
			// 配置是否可模拟数据调试
			// setDebugEnabled( false );
			// 配置是否能产生crash日志
			setCrashEnabled(true);
			// 配置是否保存流程日志
			setFlowLogEnable(false);
			// 配置是否显示和保存状态日志
			setStatusLogEnable(true);
			// 配置是否保存推送日志
			setPushLogEnable(false);
		}
		setCrashEnabled(true);
		setStatusLogEnable(true);
		// 设置
		com.tools.util.Log.DEBUG = isLogEnable();
		com.tools.util.Log.SAVE_STATUS = isStatusLogEnable();

	}

	public static boolean isOuterNet() {
		return outerNet;
	}

	public static void setOuterNet(boolean outerNet) {
		Config.outerNet = outerNet;
	}

	public static MapType getMapType() {
		return mapType;
	}

	public static void setMapType(MapType mapType) {
		Config.mapType = mapType;
	}

	public static VersionDevelop getVersionDevelop() {
		return versionDevelop;
	}

	public static void setVersionDevelop(VersionDevelop versionDevelop) {
		Config.versionDevelop = versionDevelop;
	}

	public static VersionType getVersionType() {
		return versionType;
	}

	public static void setVersionType(VersionType versionType) {
		Config.versionType = versionType;
	}

	public static ProjectType getProjectType() {
		return projectType;
	}

	public static void setProjectType(ProjectType projectType) {
		Config.projectType = projectType;
	}

	public static void setFlowLogEnable(boolean flowLogEnable) {
		Config.flowLogEnabled = flowLogEnable;
	}

	public static boolean isFlowLogEnable() {
		return flowLogEnabled;
	}

	public static void setPushLogEnable(boolean flowLogEnable) {
		Config.pushLogEnabled = flowLogEnable;
	}

	public static boolean isPushLogEnable() {
		return pushLogEnabled;
	}

	public static void setStatusLogEnable(boolean statusLogEnable) {
		Config.statusLogEnabled = statusLogEnable;
	}

	public static boolean isStatusLogEnable() {
		return statusLogEnabled;
	}

	public static void setLogEnabled(boolean enable) {
		Config.logEnabled = enable;
	}

	public static boolean isLogEnable() {
		return logEnabled;
	}

	public static void setCrashEnabled(boolean enable) {
		Config.crashEnabled = enable;
	}

	public static boolean isCrashEnable() {
		return crashEnabled;
	}

	public static void setMonkeyEnabled(boolean monkeyEnabled) {
		Config.monkeyEnabled = monkeyEnabled;
	}

	public static boolean isMonkeyEnabled() {
		return monkeyEnabled;
	}

	/**
	 * 打印信息
	 */
	public static void print() {
		android.util.Log.i(TAG, "------ 配置信息 start ------");
		android.util.Log.i(TAG, "是否允许打印日志:" + isLogEnable());
		android.util.Log.i(TAG, "是否允许捕获Crash信息:" + isCrashEnable());
		android.util.Log.i(TAG, "是否允许保存流程日志:" + isFlowLogEnable());
		android.util.Log.i(TAG, "是否允许保存状态日志:" + isStatusLogEnable());
		android.util.Log.i(TAG, "是否允许Monkey测试:" + isMonkeyEnabled());
		android.util.Log.i(TAG, "是否为外网:" + isOuterNet());
		android.util.Log.i(TAG, "版本开发阶段:" + getVersionDevelop().name());
		android.util.Log.i(TAG, "------ 配置信息 end ------");
	}

}
