package com.tools.app;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.hst.Carlease.R;
import com.tools.content.res.ResManager;
import com.tools.json.GJson;
import com.tools.lang.reflect.ReflectTool;
import com.tools.net.http.HttpConfig;
import com.tools.net.http.HttpTool;
import com.tools.net.http.HttpTool.Error;
import com.tools.os.Build;
import com.tools.os.Charset;
import com.tools.task.AbsTaskLoaderHttpWait;
import com.tools.task.LoaderConfig;
import com.tools.util.Log;

/**
 * 注意： 不要引用其他的Activity，会发生泄漏 ，可以引用Context，因为Context是全局的
 * 
 * 弃用
 * 
 * @Deprecated
 * 
 *             在线android系统源码
 *             http://grepcode.com/file/repository.grepcode.com/java
 *             /ext/com.google
 *             .android/android/2.3.4_r1/android/os/Environment.java
 * 
 * 
 *             默认值，由系统判断状态自动切换
 *             super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
 *             );
 * 
 *             强制设置为横屏
 *             super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
 *             );
 * 
 *             强制设置为竖屏
 *             super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
 *             );
 * 
 *             弱引用环境例子：
 * 
 *             // 加入 VersionA version = new VersionA();
 *             version.setName("vvvvv"); addWeakContext("version", version);
 *             version = null; // 释放全局引用
 * 
 *             // 取出key对应的值 VersionA version =
 *             (VersionA)getWeakContext("version"); if (D) Log.d(TAG,
 *             "name:"+version.getName());
 * 
 * @author lmc 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public abstract class AbsUI2 extends android.support.v4.app.FragmentActivity {

	private static final String TAG = AbsUI2.class.getSimpleName();
	private static final boolean D = true;

	protected android.support.v4.app.FragmentActivity ui = null;

	protected static Context context = null;

	protected android.support.v4.app.FragmentManager fragmentManager = null;

	// 装载器
	protected android.support.v4.app.LoaderManager loaderManager = null;

	protected float mLastx = 0;
	protected float x = 0;
	protected int width;// //屏幕宽度
	protected int height;// //屏幕宽度

	// 是否滑动关闭窗口
	protected boolean slideFinishEnabled = false;
	//
	protected boolean backAnimationEnabled = true;

	// TODO 这个不要全局变量
	// 因会出错 Caused by: java.lang.IllegalStateException: commit already called
	// protected android.support.v4.app.FragmentTransaction fragmentTransaction
	// = null;

	// 软引用环境
	private HashMap<String, SoftReference<Object>> softMap = new HashMap<String, SoftReference<Object>>();

	// 弱引用环境
	private HashMap<String, WeakReference<Object>> weakMap = new HashMap<String, WeakReference<Object>>();

	// 软引用环境
	private HashMap<String, WeakReference<Object>> activityMap = new HashMap<String, WeakReference<Object>>();

	// 加载器Id
	private int loaderId = 0;
	// 加载器回调
	private LoaderCallbacks<byte[]> loaderCallbacks = null;
	// 任务加载器
	private AbsTaskLoaderHttpWait<byte[]> taskLoader = null;

	// 等待控件
	protected WaitFgm waitFgm = null;
	private int waitViewId = 0;

	private LoaderConfig loaderConfig = new LoaderConfig();
	public WindowManager mWm;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		init(this);
		
	}

	/**
	 * 初始化
	 * 
	 * @param ui
	 */
	private void init(final android.support.v4.app.FragmentActivity ui) {
		this.ui = ui;
		context = ui.getApplicationContext();

		// 将UI加入UI管理器
		UIManager.getInstance().addUI(this);
		// 打印
		UIManager.getInstance().print();

		if (fragmentManager == null) {
			fragmentManager = this.getSupportFragmentManager();
		}
		if (loaderManager == null) {
			loaderManager = this.getSupportLoaderManager();
		}

		mWm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		width = mWm.getDefaultDisplay().getWidth();// 屏幕宽度
		height = mWm.getDefaultDisplay().getHeight();// 屏幕高度

		/**
		 * 默认不让软键盘弹出来
		 */
		setNoSoftKeyboard();

		// if (fragmentTransaction == null) {
		// fragmentTransaction = fragmentManager.beginTransaction();
		// }

		initControl();
		initControlEvent();
		initMember();

	}
	
	/**
	 * 根据屏幕宽度转换为dp单位
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public int dp2px(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}

	@Override
	public void onAttachedToWindow() {
		if (D)
			Log.d(TAG, "onAttachedToWindow()");
		// if (D) Log.d(TAG,
		// "onAttachedToWindow():bDestroyLoader:"+bDestroyLoader);
		// if (bDestroyLoader == false) {
		// 开始加载器
		// startLoader();
		// }
		super.onAttachedToWindow();
	}

	// 初始化
	protected abstract void initControl();

	protected abstract void initControlEvent();

	protected abstract void initMember();

	// 加载器
	protected  void onStartLoader(){};

	protected  byte[] doInBackgroundLoader(){return null;};

	protected  void onFinishedLoader(Loader<byte[]> loader, byte[] bytes){};

	// 新增 - 查询数据
	// protected abstract void onQueryStart();
	// protected abstract byte[] onQueryInBackground(HttpTool http);
	// protected abstract void onQueryFinished(int count, byte[] bytes, HttpTool
	// http);

	// 新增 - 提交数据
	// protected abstract void onCommitStart();
	// protected abstract byte[] onCommitInBackground(HttpTool http);
	// protected abstract void onCommitFinished(int count, byte[] bytes,
	// HttpTool http);

	protected android.support.v4.app.FragmentTransaction getTransaction() {
		return fragmentManager.beginTransaction();
	}

	protected void addFgm(int containerViewId,
			android.support.v4.app.Fragment fragment, String tag) {
		if (fragment == null) {
			return;
		}
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (isEmptyString(tag)) {
			fragmentTransaction.add(containerViewId, fragment);
		} else {
			if (containerViewId <= 0) {
				fragmentTransaction.add(fragment, tag);
			} else {
				fragmentTransaction.add(containerViewId, fragment, tag);
			}
		}
		fragmentTransaction.commit();
	}

	protected void addFgm(int containerViewId,
			android.support.v4.app.Fragment fragment) {
		addFgm(containerViewId, fragment, null);
	}

	protected void removeFgm(android.support.v4.app.Fragment fragment) {
		if (fragment == null) {
			return;
		}
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.remove(fragment);
		fragmentTransaction.commit();
	}

	protected void hideFgm(android.support.v4.app.Fragment fragment) {
		if (fragment == null) {
			return;
		}
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.hide(fragment);
		fragmentTransaction.commit();
	}

	protected void showFgm(android.support.v4.app.Fragment fragment) {
		if (fragment == null) {
			return;
		}
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.show(fragment);
		fragmentTransaction.commit();
	}

	protected void replaceFgm(int containerViewId,
			android.support.v4.app.Fragment fragment, String tag) {
		if (containerViewId <= 0 || fragment == null) {
			return;
		}
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (isEmptyString(tag)) {
			fragmentTransaction.replace(containerViewId, fragment);
		} else {
			fragmentTransaction.replace(containerViewId, fragment, tag);
		}
		fragmentTransaction.commit();
	}

	protected void replaceFgm(int containerViewId,
			android.support.v4.app.Fragment fragment) {
		replaceFgm(containerViewId, fragment, null);
	}

	/**
	 * 获取扩展包
	 * 
	 * @return
	 */
	protected Bundle getExtras() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			return extras;
		}
		return null;
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
	 * 设置无标题 一定要放在setContentView()之前 立刻无标题，此过程没有动画
	 */
	protected void setNoTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 设置全屏 可以不放在setContentView()之前 不是立刻全屏，有一个动画的过程
	 */
	protected void setFullScreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * 设置可见性
	 * 
	 * @param view
	 * @param enable
	 */
	public void setViewVisibility(View view, boolean enable) {
		if (view == null) {
			Log.e(TAG, "setViewVisibility() view == null");
			// new NullPointerException("view == null").printStackTrace();
			return;
		}
		if (enable) {
			// 显示
			view.setVisibility(View.VISIBLE);
		} else {
			// 隐藏
			this.setViewVisibilityGONE(view); // 不占位
		}
		// 刷新
		view.invalidate();
	}

	/**
	 * 设置控件的可见性（占位）
	 * 
	 * @param view
	 */
	public void setViewVisibilityIN(View view) {
		if (view == null) {
			Log.e(TAG, "setViewVisibilityIN() view == null");
			// new NullPointerException("view == null").printStackTrace();
			return;
		}
		// 隐藏
		view.setVisibility(View.INVISIBLE); // 占位
		// 刷新
		view.invalidate();
	}

	/**
	 * 设置控件的可见性（不占位）
	 * 
	 * @param view
	 */
	public void setViewVisibilityGONE(View view) {
		if (view == null) {
			Log.e(TAG, "setViewVisibilityGONE() view == null");
			// new NullPointerException("view == null").printStackTrace();
			return;
		}
		// 隐藏
		view.setVisibility(View.GONE); // 不占位
		// 刷新
		view.invalidate();
	}

	/**
	 * Android2.3系统的overscroll效果 android:overScrollMode="never"
	 * setOverScrollMode(OVER_SCROLL_NEVER)
	 * 
	 * @param enabled
	 */
	public void setOverScrollEnabled(View view, boolean enabled) {
		if (view == null) {
			return;
		}
		if (D)
			Log.d(TAG, "setOverScrollEnabled()");
		// View.OVER_SCROLL_NEVER == 2 // 2.3(9)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Ver2_3) {
			String classPath = "android.view.View";
			String methodName = "setOverScrollMode";
			String fieldName = null;
			if (enabled) {
				// public static final int OVER_SCROLL_ALWAYS
				// view.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
				// public void setOverScrollMode (int overScrollMode)
				fieldName = "OVER_SCROLL_ALWAYS";
			} else {
				// public static final int OVER_SCROLL_NEVER
				// view.setOverScrollMode(View.OVER_SCROLL_NEVER); // ok
				fieldName = "OVER_SCROLL_NEVER";
			}
			if (D)
				Log.d(TAG, "setOverScrollEnabled():fieldName:" + fieldName);
			// +++ 使用反射
			// 第一步：得到值
			int value = -1;
			Object object = ReflectTool.getStaticFieldValue(classPath,
					fieldName);
			if (object != null) {
				value = Integer.valueOf(object.toString());
			}
			// 第二步：得到方法
			java.lang.reflect.Method method = ReflectTool.getMethod(classPath,
					methodName, int.class);
			// 第三步：调用方法
			if (method != null && value != -1) {
				ReflectTool.invokeMethod(view, method, value);
				if (D)
					Log.d(TAG, "setOverScrollEnabled():ReflectTool.invoke(...)");
			}
		}
	}

	/**
	 * 中文字体加粗（适用TextView和Button）
	 * 
	 * @param tv
	 * @param text
	 */
	public void setBoldText(TextView tv, boolean fakeBoldText, String text) {
		if (tv == null || isEmptyString(text)) {
			return;
		}
		// set bold
		setBoldText(tv, fakeBoldText);
		// set text
		tv.setText(text);
	}

	/**
	 * 中文字体加粗（适用TextView和Button）
	 * 
	 * @param tv
	 */
	public void setBoldText(TextView tv, boolean fakeBoldText) {
		if (tv == null) {
			return;
		}
		// set bold
		TextPaint tp = tv.getPaint();
		tp.setFakeBoldText(fakeBoldText);
	}

	/**
	 * 给文本加入下划线
	 * 
	 * @param tv
	 */
	public void setUnderlienText(TextView tv) {
		if (tv == null) {
			return;
		}
		TextPaint tp = tv.getPaint();
		tp.setUnderlineText(true);
	}

	/**
	 * 默认不让软键盘弹出来
	 */
	protected void setNoSoftKeyboard() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	/**
	 * 打印WaitFgm信息
	 */
	private void printWaitFgm() {
		Log.d(TAG, "------ printWaitFgm() start ------");
		if (waitFgm != null) {
			Log.d(TAG, "isAdded():" + waitFgm.isAdded()); // 是否添加到父UI
			Log.d(TAG, "isDetached():" + waitFgm.isDetached()); // 是否已从父UI删除
			Log.d(TAG, "isHidden():" + waitFgm.isHidden());
			Log.d(TAG, "isInLayout():" + waitFgm.isInLayout());
			Log.d(TAG, "isRemoving():" + waitFgm.isRemoving());
			Log.d(TAG, "isResumed():" + waitFgm.isResumed());
			Log.d(TAG, "isVisible():" + waitFgm.isVisible()); // 是否可见
		}
		Log.d(TAG, "------ printWaitFgm() end ------");
	}

	/**
	 * 设置等待容器控件ID 容器控件有：ViewGroup、FrameLayout、LinearLayout、RelativeLayout
	 * 不知道支不支持ListView、GridView、ScrollView
	 * 
	 * TODO 不知道ListView和View及Button支不支持
	 * 
	 * @param viewId
	 */
	protected void setWaitViewId(int viewId) {
		Log.d(TAG, "setWaitViewId()");
		this.waitViewId = viewId;

		if (getWaitViewId() > 0) {
			// 添加Wait控件
			addWaitFgm();
			// 显示Wait控件
			showWaitFgm();
		} else {
			hideWaitFgm();
		}
	}

	/**
	 * 得到WaitViewId
	 * 
	 * @return
	 */
	protected int getWaitViewId() {
		return this.waitViewId;
	}

	/**
	 * 添加Wait控件
	 */
	private void addWaitFgm() {
		Log.d(TAG, "addWaitFgm()");

		if (getWaitViewId() <= 0) {
			return;
		}

		if (waitFgm == null) {
			waitFgm = new WaitFgm();
			// 设置回调接口，用于WaitFgm.java点击刷新
			waitFgm.setOnCallbackListener(new WaitFgm.OnCallbackListener() {

				@Override
				public void onCallback() {
					startLoader(loaderId);
				}

			});
		}

		if (waitFgm == null) {
			return;
		}

		printWaitFgm();

		if (waitFgm.isAdded() == false) {
			// 添加fgm
			addFgm(waitViewId, waitFgm);
		}
	}

	/**
	 * 显示Wait控件
	 */
	private void showWaitFgm() {
		Log.d(TAG, "showWaitFgm()");

		if (getWaitViewId() <= 0) {
			return;
		}

		if (waitFgm == null) {
			return;
		}

		printWaitFgm();

		if (waitFgm.isAdded()) {
			waitFgm.setListShown(false); // 显示圆形进度条
			if (waitFgm.isHidden()) {
				showFgm(waitFgm);
			}
		}

		// 将父控件下的子控件全部隐藏
		hideChildView(waitViewId);

		if (waitFgm.isVisible() == false) { // 如果WaitFgm不显示
			// 显示
			setViewVisibility(waitFgm.getView(), true);
		}
	}

	/**
	 * 隐藏Wait控件
	 */
	private void hideWaitFgm() {
		Log.d(TAG, "hideWaitFgm()");
		showChildView(waitViewId);
		// hideFgm(waitFgm); // TODO 会出错
		// 显示
		if (waitFgm != null) {
			setViewVisibilityGONE(waitFgm.getView());
		}
	}

	/**
	 * 移除Wait控件
	 */
	private void removeWaitFgm() {
		Log.d(TAG, "removeWaitFgm()");
		removeFgm(waitFgm);
	}

	/**
	 * 设置WaitFgm文本内容(注意:在Loader运行结束之后使用)
	 * 
	 * @param text
	 */
	protected void setWaitText(String text) {
		Log.d(TAG, "setWaitText()");

		if (waitFgm == null) {
			return;
		}

		if (text == null) {
			text = "";
		}

		// waitFgm采用ListFgm源代码

		// setListShown(false);时，就会显示一个圆形进度条。

		// setListShown(true)设置为true时，才能显示文字。
		// setEmptyText("正在获取数据......");
		printWaitFgm();

		if (waitFgm.isAdded() == false) {
			return;
		}

		// 显示
		showWaitFgm();

		waitFgm.setListShown(true);
		waitFgm.setEmptyText(text);

	}

	/**
	 * 如果Object等null或者数组长度等于0，则设置显示文本 TODO 没完成
	 * 当int.class、Integer.class等于0时，则设置显示文本 当short.class、Short.class等于0时，则设置显示文本
	 * 当long.class、Long.class等于0时，则设置显示文本 当float.class、Float.class等于0.0时，则设置显示文本
	 * 当double.class、Double.class等于0.0时，则设置显示文本
	 * 当string.class、String.class等于null或者""时，则设置显示文本 当Object等于null时，则设置显示文本
	 * 当[]等于null或者[]长度等于0时，则设置显示文本
	 * 
	 * @param object
	 * @param text
	 */
	protected void setWaitTextIfEmpty(Object object, String text) {

		if (object == null) {
			setWaitText(text);
		} else {
			// 判断类型，并取出值。

		}

	}

	/**
	 * 
	 * @param object
	 */
	protected void setWaitTextIfEmpty(Object object) {
		setWaitTextIfEmpty(object, "无数据");
	}

	/**
	 * 得到setContentView()的控件
	 * 
	 * @return
	 */
	private ViewGroup getContentView(int containerViewId) {
		Log.d(TAG, "getContentView()");
		if (containerViewId <= 0) {
			return null;
		}

		Window window = this.getWindow();
		if (window == null) {
			return null;
		}
		// 得到setContentView()设置的布局
		View view = window.getDecorView();
		if (view == null) {
			return null;
		}
		// 得到父控件
		ViewGroup parentView = (ViewGroup) view.findViewById(containerViewId);
		return parentView;
	}

	/**
	 * 显示子控件
	 * 
	 * @param parentViewId
	 */
	private void showChildView(int parentViewId) {
		Log.d(TAG, "showChildView()");
		ViewGroup parentView = getContentView(parentViewId);
		if (parentView == null) {
			return;
		}

		int len = parentView.getChildCount();
		Log.d(TAG, "showChildView():len:" + len);
		for (int n = 0; n < len; n++) {
			setViewVisibility(parentView.getChildAt(n), true);
		}
	}

	/**
	 * 判断是否为android.support.v4.app.NoSaveStateFrameLayout类
	 * 
	 * @param v
	 * @return
	 */
	private boolean isNoSaveStateFrameLayout(View v) {
		if (v == null) {
			return false;
		}

		Class<?> clazz = v.getClass();
		if (clazz == null) {
			return false;
		}

		String classPath = clazz.getCanonicalName();
		if (classPath == null) {
			return false;
		}

		if ("android.support.v4.app.NoSaveStateFrameLayout"
				.equalsIgnoreCase(classPath)) {
			return true;
		}

		return false;
	}

	/**
	 * 将父控件下的子控件全部隐藏 TODO 要将原来的信息保存，因为不显示之前隐藏的。
	 * 
	 * @param parentViewId
	 */
	private void hideChildView(int parentViewId) {
		Log.d(TAG, "hideChildView()");
		ViewGroup parentView = getContentView(parentViewId);
		if (parentView == null) {
			return;
		}

		int childCount = parentView.getChildCount();
		Log.d(TAG, "hideChildView():childCount:" + childCount);

		// 因使用优化工具回收内存时，UI也跟着回收了，再次启行APP，setWaitViewId()里的ID布局会自动新增
		// android.support.v4.app.NoSaveStateFrameLayout，所以数量要排除它。
		int len = 0;
		for (int n = 0; n < childCount; n++) {
			// 判断是否为android.support.v4.app.NoSaveStateFrameLayout类
			if (isNoSaveStateFrameLayout(parentView.getChildAt(n)) == false) {
				// 不是NoSaveStateFrameLayout类，才算是View
				++len;
			}
		}

		Log.d(TAG, "hideChildView():len:" + len);

		if (!(len >= 1 && len <= 2)) {
			Log.throwException("setWaitViewId()设置的控件只能含有一个子控件");
		}

		for (int n = 0; n < childCount; n++) {
			// 不占位隐藏
			setViewVisibilityGONE(parentView.getChildAt(n));
		}
	}

	/**
	 * 设置Loader配置
	 * 
	 * @param config
	 */
	protected void setLoaderConfig(LoaderConfig config) {
		this.loaderConfig = config;
	}

	/**
	 * 得到TaskLoader
	 * 
	 * @return
	 */
	protected AbsTaskLoaderHttpWait<byte[]> getTaskLoader() {
		return taskLoader;
	}

	/**
	 * 在AbsUI.java加入Loader的功能哪一些？ 答： 1)提供了Loader的几个常用方法: startLoader()
	 * restartLoader() destroyLoader(); 默认不运行Loader 2)有错误的详细内容提示：
	 * 当遇到"无网络"，"非JSON格式"，"非XML格式","HTTP错误的状态码" "连接失败","连接超时","HTTP请求成功，但无数据"
	 * 3)保持Loader与UI的关系，即与UI的生命周期有关系。 4)可弹出等待对话框，详见：AbsTaskLoaderHttpWait.java
	 * 5)点击可刷新。 6) 7) 8)
	 * */

	/**
	 * 创建加载器
	 */
	private void createLoader() {
		Log.d(TAG, "createLoader()");
		if (this.loaderCallbacks != null) {
			Log.e(TAG, "createLoader() loaderCallbacks != null");
			return;
		}
		Log.e(TAG, "createLoader() loaderCallbacks == null");

		// 创建LoaderCallbacks
		this.loaderCallbacks = new LoaderCallbacks<byte[]>() {

			@Override
			public Loader<byte[]> onCreateLoader(int arg0, Bundle arg1) {
				// 实例化并返回指定ID的loader
				if (D)
					Log.d(TAG, "onCreateLoader()");

				// 创建Loader
				taskLoader = new AbsTaskLoaderHttpWait<byte[]>(ui,
						R.string.loading) {

					@Override
					protected void onStartLoading() {
						if (D)
							Log.d(TAG, "onStartLoading()");
						// abs回调
						onStartLoader();
						super.onStartLoading();
					}

					@Override
					public byte[] loadInBackground() {
						if (D)
							Log.d(TAG, "loadInBackground()");
						// abs回调
						byte[] bytes = doInBackgroundLoader();
						return bytes;
					}

					@Override
					protected void onHttpFailed(Error error,
							java.lang.Exception exception, int responseCode,
							byte[] out) {
						if (D)
							Log.d(TAG, "onHttpFailed():error:" + error
									+ ",responseCode:" + responseCode);

						String errorMessage = null;

						if (error == HttpTool.Error.ResponseCodeError) {
							// 状态码为500,不是session超时
							ResManager res = new ResManager(context);
							String responseCodeText = res.parseString(
									R.array.tools_http_status_code,
									responseCode,
									R.array.tools_http_status_caption);

							// 显示错误
							String showErrorText = "( "
									+ Charset.bytes2String(out,
											new HttpConfig.Header()
													.getCharset()) + " )";

							errorMessage = String.format(
									"HTTP状态码错误\n%d - %s\n%s\n点击重试",
									responseCode, responseCodeText,
									showErrorText);

						} else if (error == HttpTool.Error.NotNetwork) {
							errorMessage = String.format("无网络\n点击重试");
						} else if (error == HttpTool.Error.ConnectException) {
							errorMessage = String.format("连接失败\n点击重试");
						} else if (error == HttpTool.Error.ConnectTimeoutException) {
							errorMessage = String.format("连接超时\n点击重试");
						} else if (error == HttpTool.Error.IOException) {
							errorMessage = String.format("连接失败\n点击重试");
						} else if (error == HttpTool.Error.InterruptedIOException) {
							errorMessage = String.format("连接失败\n点击重试");
						} else if (error == HttpTool.Error.SocketException) {
							errorMessage = String.format("获取失败\n点击重试");
						} else if (error == HttpTool.Error.SocketTimeoutException) {
							errorMessage = String.format("获取超时\n点击重试");
						} else if (error == HttpTool.Error.FileNotFoundException) {
							errorMessage = String.format("网址找不到\n点击重试");
						} else if (error == HttpTool.Error.OtherException) {
							errorMessage = String.format("其它失败\n点击重试");
						}

						// 显示错误文本
						setWaitText(errorMessage);

						// 按HOME键让APP到后台，再返回前台，刷新时，点击无响应。
						// 原因是isStarted()为true
						// 所以，出现错误时，则reset();
						reset();

					}

				};

				if (loaderConfig == null) {
					loaderConfig = new LoaderConfig();
				}

				// taskLoader.setDialogCloseable(true); // 可以关闭
				// taskLoader.setDialogShowable(true); // 不显示对话框
				Log.i(TAG,
						"loaderConfig.getDialogCloseable():"
								+ loaderConfig.getDialogCloseable());
				Log.i(TAG,
						"loaderConfig.getDialogShowable():"
								+ loaderConfig.getDialogShowable());
				taskLoader
						.setDialogCloseable(loaderConfig.getDialogCloseable()); // 可以关闭
				taskLoader.setDialogShowable(loaderConfig.getDialogShowable()); // 不显示对话框

				return taskLoader;
			}

			@Override
			public void onLoadFinished(Loader<byte[]> arg0, byte[] arg1) {

				if (D)
					Log.d(TAG, "------ onLoadFinished() start ------");

				// loader加载完成时调用
				if (D)
					Log.d(TAG, "onLoadFinished():arg0:" + arg0);
				if (D)
					Log.d(TAG, "onLoadFinished():arg1:" + arg1);

				if (arg0 != null) {
					// 停止loader
					arg0.stopLoading();
					// 关闭loader，防止onStart()再次运行。
					// ui.getSupportLoaderManager().destroyLoader( arg0.getId()
					// );
					// 销毁加载器
					// destroyLoader();
				}

				boolean isShowWaitFgm = false; // 是否显示WaitFgm控件

				// 得到HTTP对象
				HttpTool http = getTaskLoader().getHttp();

				// 如果http错误，则不要显示子控件
				HttpTool.Error error = http.getError();
				if (D)
					Log.d(TAG, "onLoadFinished():error:" + error.name());

				if (error == HttpTool.Error.Successful) { // 请求成功
					// 字节数组转成字符串
					String resultText = Charset.convertString(arg1, http
							.getConfig().getHeader().getCharset(),
							Charset.defaultCharset());
					if (D)
						Log.d(TAG, "onLoadFinished():resultText:" + resultText);
					if (isEmptyString(resultText) == false) { // 字符串 > 0
						if (D)
							Log.d(TAG, "onLoadFinished():resultText字符串长度>0");
						// 这里有可能是JSON格式，也有可能是XML格式。
						// 取第一个字节，如果是{或者[，则为JSON格式，如果是<，则为XML格式
						boolean isVerifyJSON = false;
						boolean isVerifyXML = false;

						int resultTextLen = resultText.length();
						// 取第一个
						char firstChar = resultText.charAt(0);
						// 取最后一个
						char endChar = resultText.charAt(resultTextLen - 1);
						// Log.e(TAG, "endChar:"+String.valueOf(endChar));

						if (firstChar == '{' && endChar == '}') {
							isVerifyJSON = true;
						} else if (firstChar == '[' && endChar == ']') {
							isVerifyJSON = true;
						} else if (firstChar == '<' && endChar == '>') {
							isVerifyXML = true;
						}

						if (D)
							Log.d(TAG, "onLoadFinished():isVerifyJSON:"
									+ isVerifyJSON);
						if (D)
							Log.d(TAG, "onLoadFinished():isVerifyXML:"
									+ isVerifyXML);

						// TODO 强制支持JSON格式
						// isJSONString = true;
						isVerifyXML = true;

						// super.setTextType() //
						// 设置内容类型。是纯文件，还是JSON(默认)，还是XML？？？
						// 还可能还是对象，或者是十六进制fff
						// 或者从http里判断
						if (isVerifyJSON == true) {
							// 有内容，但是"不是JSON标准格式"，则显示错误文本。
							boolean isJson = GJson.isJson(resultText);
							if (isJson == false) {
								isShowWaitFgm = true; // 是否显示WaitFgm控件
								if (isFinished() == false) {
									setWaitText("非JSON标准格式\n点击重试");
								}
							}
						} else if (isVerifyXML == true) {
							// if (isFinished() == false) {
							// setWaitText("非XML标准格式\n点击重试");
							// }
							isShowWaitFgm = false;
						} else if (isVerifyJSON == false
								&& isVerifyXML == false) {
							isShowWaitFgm = true; // 是否显示WaitFgm控件
							if (isFinished() == false) {
								setWaitText("非JSON、XML格式\n点击重试");
							}
						}
					} else { // 字符串 <=0
						if (D)
							Log.d(TAG, "onLoadFinished():resultText字符串长度<=0");
						isShowWaitFgm = true; // 是否显示WaitFgm控件
						if (isFinished() == false) {
							setWaitText("请求成功，但无数据\n点击重试");
						}
					}
				} else { // 请求失败
					isShowWaitFgm = true; // 是否显示WaitFgm控件
				}

				if (D)
					Log.d(TAG, "onLoadFinished():isShowWaitFgm:"
							+ isShowWaitFgm);

				Log.d(TAG, "onLoadFinished()--->isFinished():" + isFinished());
				if (isFinished() == false) {
					if (isShowWaitFgm == false) { // 是否显示WaitFgm控件
						hideWaitFgm();
					}

					// 不在这里处理HTTP SESSION 超时问题，因解析文本会耗电等因素。

					// abs回调
					onFinishedLoader(arg0, arg1);

				}

				if (D)
					Log.d(TAG, "------ onLoadFinished() end ------");
			}

			@Override
			public void onLoaderReset(Loader<byte[]> arg0) {
				// loader被重置时调用，以设置数据无效
				if (D)
					Log.d(TAG, "onLoaderReset()");
			}

		}; // end loader call

	}

	/**
	 * 启动加载器(如果加载器已启动，则不会再次加载)
	 */
	// @Deprecated
	// protected void startLoader() {
	// startLoader( 0 );
	// }

	/**
	 * 启动加载器(如果加载器已启动，则不会再次加载)
	 */
	protected void startLoader(int loaderId) {
		Log.d(TAG, "startLoader():loaderId:" + loaderId);

		this.loaderId = loaderId;

		boolean isStarted = false;

		if (taskLoader != null) {
			taskLoader.print();
			isStarted = taskLoader.isStarted();
		}

		Log.d(TAG, "startLoader():isStarted:" + isStarted);

		if (isStarted == false) {
			restartLoader(this.loaderId);
		}
	}

	/**
	 * 重新启动加载器(不管加载器是否已启动，会再次加载)
	 */
	// @Deprecated
	// protected void restartLoader() {
	// restartLoader( 0 );
	// }

	/**
	 * 重新启动加载器(不管加载器是否已启动，会再次加载)
	 */
	protected void restartLoader(int loaderId) {
		Log.d(TAG, "restartLoader():loaderId:" + loaderId);

		this.loaderId = loaderId;

		// 创建加载器
		createLoader();

		ui.getSupportLoaderManager().restartLoader(loaderId, null,
				loaderCallbacks);
	}

	/**
	 * 销毁加载器
	 */
	protected void destroyLoader() {
		Log.d(TAG, "destroyLoader()");

		if (taskLoader != null) {
			taskLoader.print();

			boolean isStarted = taskLoader.isStarted();
			Log.d(TAG, "destroyLoader():isStarted:" + isStarted);
			if (isStarted) {
				// 如果Loader已经开始，则恢复控件
				hideWaitFgm();
			}
		}

		// 如果Loader已经开始，则恢复控件
		// hideWaitFgm(); // 不要这句，因onFinishedLoader()可以使用destroyLoader()

		ui.getSupportLoaderManager().destroyLoader(this.loaderId);
	}

	/**
	 * 判断是否结束
	 * 
	 * @return
	 */
	protected boolean isFinished() {
		return super.isFinishing();
	}

	/**
	 * 提示Toast
	 * 
	 * @param resId
	 */
	protected void showToast(int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 提示Toast
	 * 
	 * @param resId
	 */
	protected void showToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		
	}

	/**
	 * 得到系统状态栏高度
	 * 
	 * @return
	 */
	protected int getWindowStatusBarHeight() {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			if (c != null) {
				obj = c.newInstance();
				if (obj != null) {
					field = c.getField("status_bar_height");
					if (field != null) {
						x = Integer.parseInt(field.get(obj).toString());
						statusBarHeight = getResources().getDimensionPixelSize(
								x);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (D)
			Log.d(TAG, "getWindowsStatusBarHeight:" + statusBarHeight);
		return statusBarHeight;
	}

	/**
	 * 软引用环境，OutOfMemory发生之前释放 例子： http://bif (D)
	 * Log.csdn.net/chuchu521/article/details/7925702
	 */
	protected void addSoftContext(String key, Object value) {
		if (isEmptyString(key)) {
			return;
		}
		if (value != null) {
			softMap.put(key, new SoftReference<Object>(value));
		}
	}

	/**
	 * 得到软引用的对象
	 * 
	 * @param key
	 * @return
	 */
	protected Object getSoftContext(String key) {
		return softMap.get(key).get();
	}

	/**
	 * 得到软引用环境
	 * 
	 * @return
	 */
	protected HashMap<String, SoftReference<Object>> getSoftContext() {
		return softMap;
	}

	/**
	 * 主动释放软引用
	 */
	protected void releaseSoftReferences() {
	}

	/**
	 * 弱引用环境，GC前释放（不能释放全局引用/强引用）（普通对象测试通过，但Bitmap和Drawable对象没有测试通过）
	 */
	protected void addWeakContext(String key, Object value) {
		if (isEmptyString(key)) {
			return;
		}
		if (value != null) {
			weakMap.put(key, new WeakReference<Object>(value));
		}
	}

	/**
	 * 得到弱引用的对象（测试通过）
	 * 
	 * @param key
	 * @return
	 */
	protected Object getWeakContext(String key) {
		return weakMap.get(key).get();
	}

	/**
	 * 得到弱引用环境（测试通过）
	 * 
	 * @return
	 */
	protected HashMap<String, WeakReference<Object>> getWeakContext() {
		return weakMap;
	}

	/**
	 * 释放弱引用
	 */
	protected void releaseWeakReferences() {
	}

	/**
	 * 执行GC
	 */
	protected void releaseGC() {
	}

	/**
	 * Activity引用环境，与Activity的生命周期一样 如果是图片，则3.0及以上版本，不要使用，因recycle方法没用了
	 */
	protected void addActivityContext(String key, Object value) {

	}

	/**
	 * 打开Action
	 * 
	 * @param context
	 * @param action
	 * @param uri
	 * @param mimeType
	 */
	public static void startAction(final Context context, final String action,
			final Uri uri, final String mimeType, Intent i) {
		if (context != null && isEmptyString(action) == false) {
			Intent intent = new Intent();
			;
			if (i != null) {
				intent = i;
			}
			intent.setAction(action);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (uri != null) {
				if (isEmptyString(mimeType) == false) {
					intent.setDataAndType(uri, mimeType);
				} else {
					intent.setData(uri);
				}
			}
			context.startActivity(intent);
		}
	}

	public static void startUri(final Context context, final Uri uri,
			final String mimeType, Intent intent) {
		startAction(context, Intent.ACTION_VIEW, uri, mimeType, intent);
	}

	/**
	 * 以android.intent.action.VIEW动作方式打开Uri
	 * 
	 * @param context
	 * @param uri
	 * @param mimeType
	 */
	public static void startUri(final Context context, final Uri uri,
			final String mimeType) {
		startAction(context, Intent.ACTION_VIEW, uri, mimeType, null);
	}

	public static void startUri(final Context context, final Uri uri,
			Intent intent) {
		startAction(context, Intent.ACTION_VIEW, uri, null, intent);
	}

	/**
	 * 以android.intent.action.VIEW动作方式打开Uri
	 * 
	 * @param context
	 * @param uri
	 */
	public static void startUri(final Context context, final Uri uri) {
		startAction(context, Intent.ACTION_VIEW, uri, null, null);
	}

	/*
	 * 系统低内存时，不是应用低内存
	 */
	@Override
	public void onLowMemory() {
		if (D)
			Log.d(TAG, "onLowMemory()");
		super.onLowMemory();
	}

	/**
	 * 打开Activity
	 * 
	 * @param context
	 * @param intent
	 */
	public static void startUI(final Context context, final Intent intent) {
		if (context != null && intent != null) {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	/**
	 * 打开Activity
	 * 
	 * (已验证通过)
	 * 
	 * @param context
	 * @param cls
	 */
	public static void startUI(final Context context, final Class<?> cls) {
		// if (context != null && cls != null) {
		// Intent intent = new Intent(context, cls);
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startActivity(intent);
		// }
		startUI(context, cls, null);
	}

	/**
	 * 启动cls，保留intent数据
	 * 
	 * (已验证通过)
	 * 
	 * @param context
	 * @param cls
	 * @param intent
	 */
	public static void startUI(final Context context, final Class<?> cls,
			Intent intent) {
		if (context != null && cls != null) {
			if (intent == null) {
				intent = new Intent(context, cls);
			} else {
				intent.setClass(context, cls);
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	public static void startClearTopUI(final Context context,
			final Class<?> cls, Intent intent) {
		if (context != null && cls != null) {
			if (intent == null) {
				intent = new Intent(context, cls);
			} else {
				intent.setClass(context, cls);
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
	}

	/**
	 * Intent.FLAG_ACTIVITY_CLEAR_TOP
	 * 
	 * 打开ActivityA(根)--->ActivityB--->ActivityC--->ActivityD--->ActivityB(
	 * FLAG_ACTIVITY_CLEAR_TOP) 此时在ActivityB之上的ActivityC和ActivityD会被清除。
	 * 
	 * (已验证通过)
	 * 
	 * @param context
	 * @param cls
	 */
	public static void startClearTopUI(final Context context, final Class<?> cls) {
		if (context != null && cls != null) {
			Log.d(TAG, "startClearTopUI():class:" + cls.getCanonicalName());
			Intent intent = new Intent(context, cls);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		}
	}

	public static void startClearTaskUI(final Context context,
			final Class<?> cls, Intent intent) {
		if (context != null && cls != null) {
			if (intent == null) {
				intent = new Intent(context, cls);
			} else {
				intent.setClass(context, cls);
			}

			if (context != null && cls != null) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Ver3_0_x) {
					Log.d(TAG, "startClearTaskUI():3.0及以上.");
					// 3.0及以上
					// Intent intent = new Intent(context, cls);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| android.support.v4.content.IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
					context.startActivity(intent);
				} else {
					Log.d(TAG, "startClearTaskUI():3.0以下.");
					UIManager.getInstance().finish(cls); // 结束cls
					startUI(context, cls); // 打开cls
					UIManager.getInstance().finishAll(cls); // 结束全部，不包括cls
					// 3.0以下(已测试，以下方法无效)
					// ComponentName componentName = new ComponentName(context,
					// cls);
					// Intent intent =
					// android.support.v4.content.IntentCompat.makeRestartActivityTask(componentName);
					// context.startActivity(intent);
				}
			}

		}
	}

	/**
	 * Intent.FLAG_ACTIVITY_CLEAR_TASK
	 * 
	 * 打开ActivityA(根)--->ActivityB--->ActivityC(FLAG_ACTIVITY_CLEAR_TASK)
	 * 此时ActivityC变成了根Activity，其它的Activity全部被清除。
	 * 
	 * 4.0验证通过，2.3验证通过(采用非makeRestartActivityTask方式)
	 * 
	 * 2.3系统上采用 android.support.v4.content.IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
	 * 或者 android.support.v4.content.IntentCompat.makeRestartActivityTask(
	 * componentName); 都无效。
	 * 
	 * @param context
	 * @param cls
	 */
	public static void startClearTaskUI(final Context context,
			final Class<?> cls) {
		Log.d(TAG, "startClearTaskUI():class:" + cls.getCanonicalName());
		Log.d(TAG, "startClearTaskUI():SDK_INT:" + Build.VERSION.SDK_INT);
		if (context != null && cls != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Ver3_0_x) {
				Log.d(TAG, "startClearTaskUI():3.0及以上.");
				// 3.0及以上
				Intent intent = new Intent(context, cls);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| android.support.v4.content.IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(intent);
			} else {
				Log.d(TAG, "startClearTaskUI():3.0以下.");
				UIManager.getInstance().finish(cls); // 结束cls
				startUI(context, cls); // 打开cls
				UIManager.getInstance().finishAll(cls); // 结束全部，不包括cls
				// 3.0以下(已测试，以下方法无效)
				// ComponentName componentName = new ComponentName(context,
				// cls);
				// Intent intent =
				// android.support.v4.content.IntentCompat.makeRestartActivityTask(componentName);
				// context.startActivity(intent);
			}
		}
	}

	/**
	 * Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
	 * 
	 * 打开ActivityA--->ActivityB(FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
	 * 按home键让应用到后台，从Launcher桌面或者桌面应用列表打开应用，ActivityB被清除。 按home键让应用到后台，从Back
	 * Stack打开应用，则ActivityB还在。
	 * 
	 * (已验证通过)
	 * 
	 * @param context
	 * @param cls
	 */
	public static void startClearWhenTaskResetUI(final Context context,
			final Class<?> cls) {
		if (context != null && cls != null) {
			Log.d(TAG,
					"startClearWhenTaskResetUI():class:"
							+ cls.getCanonicalName());
			Intent intent = new Intent(context, cls);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			context.startActivity(intent);
		}
	}

	/**
	 * Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
	 * 
	 * 没验证过
	 * 
	 * @param context
	 * @param cls
	 */
	public static void startResetTaskIfNeededUI(final Context context,
			final Class<?> cls) {
		if (context != null && cls != null) {
			Log.d(TAG,
					"startResetTaskIfNeededUI():class:"
							+ cls.getCanonicalName());
			Intent intent = new Intent(context, cls);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			context.startActivity(intent);
		}
	}

	/**
	 * android.support.v4.content.IntentCompat.FLAG_ACTIVITY_TASK_ON_HOME
	 * 
	 * 没验证过
	 * 
	 * level11才有android.support.v4.content.IntentCompat.
	 * FLAG_ACTIVITY_TASK_ON_HOME
	 * 
	 * @param context
	 * @param cls
	 */
	public static void startTaskOnHomeUI(final Context context,
			final Class<?> cls) {
		if (context != null && cls != null) {
			Log.d(TAG, "startTaskOnHomeUI():class:" + cls.getCanonicalName());
			Intent intent = new Intent(context, cls);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| android.support.v4.content.IntentCompat.FLAG_ACTIVITY_TASK_ON_HOME);
			context.startActivity(intent);
		}
	}

	/**
	 * 关闭ui
	 * 
	 * @param ui
	 */
	public static void stopUI(final android.support.v4.app.FragmentActivity ui) {
		UIManager.getInstance().finish(ui);
	}

	/**
	 * 设置是否滑动关闭窗口
	 * 
	 * @param slideFinishEnabled
	 */
	protected void setSlideFinishEnabled(boolean slideFinishEnabled) {
		this.slideFinishEnabled = slideFinishEnabled;
	}

	/**
	 * 获取是否滑动关闭窗口
	 * 
	 * @return
	 */
	protected boolean isSlideFinishEnabled() {
		return slideFinishEnabled;
	}

	/**
	 * 是否返回键设置动画
	 * 
	 * @param BackAnimationEnabled
	 *            void 2013-12-20
	 */
	protected void setBackAnimationEnabled(boolean backAnimationEnabled) {
		this.backAnimationEnabled = backAnimationEnabled;
	}

	/**
	 * 获取是否返回键设置动画
	 * 
	 * @return boolean 2013-12-20
	 */
	protected boolean isBackAnimationEnabled() {
		return backAnimationEnabled;
	}

	@Override
	public void onBackPressed() {
		if (D)
			Log.d(TAG, "onBackPressed()");
		super.onBackPressed();
		if (isBackAnimationEnabled()) {
			overridePendingTransition(0, R.anim.tools_slip_out_right);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (isSlideFinishEnabled()) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				x = event.getX();
				mLastx = event.getX();
				// if (D) Log.d(TAG, "x---"+x);
				break;
			case MotionEvent.ACTION_MOVE:
				mLastx = event.getX();
				// if (D) Log.d(TAG, "mLastx---"+mLastx);
				break;
			case MotionEvent.ACTION_UP:
				// if (D) Log.d(TAG, "mLastx - x---"+(mLastx - x));
				// if (D) Log.d(TAG, "mLastx - x---"+(mLastx -
				// x)+"width/2---"+width/2);
				if (mLastx - x > width / 3) {
					if (ui != null) {
						ui.finish();
						overridePendingTransition(0,
								R.anim.tools_slip_out_right);
					}
				}
				break;
			}
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, R.anim.tools_slip_out_right);
		View view = getWindow().peekDecorView();
		if (view != null) {
		InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/*
	 * 销毁
	 */
	@Override
	protected void onDestroy() {
		if (D)
			Log.d(TAG, "onDestroy()");
		// 销毁加载器
		destroyLoader();
		// 从UI管理吕的列队移除并结束UI
		UIManager.getInstance().finish(this);
		View view = getWindow().peekDecorView();
		if (view != null) {
		InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		super.onDestroy();
	}

	/*
	 * 在垃圾收集器将对象从内存中清除出去之前做必要的清理工作
	 */
	@Override
	protected void finalize() throws Throwable {
		if (D)
			Log.d(TAG, "finalize()");
		super.finalize();
	}

}