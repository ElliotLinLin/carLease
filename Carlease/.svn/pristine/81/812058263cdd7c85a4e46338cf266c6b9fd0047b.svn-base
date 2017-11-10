package com.tools.app;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.hst.Carlease.R;
import com.tools.content.res.ResManager;
import com.tools.json.GJson;
import com.tools.lang.reflect.ReflectTool;
import com.tools.net.http.HttpTool;
import com.tools.net.http.HttpTool.Error;
import com.tools.os.Build;
import com.tools.os.Charset;
import com.tools.task.AbsTaskLoaderHttpWait;
import com.tools.task.LoaderConfig;
import com.tools.util.Log;


/**
 * 在线android系统源码
 * http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.4_r1/android/os/Environment.java
 *
 * 弃用		@Deprecated
 *
 */
public abstract class AbsFgm2 extends android.support.v4.app.Fragment {

	private static final String TAG = AbsFgm2.class.getSimpleName();
	private static final boolean D = false;

	protected android.support.v4.app.FragmentActivity ui = null;

	protected Context context = null;

	protected android.support.v4.app.FragmentManager fragmentManager = null;

	// 装载器
	protected android.support.v4.app.LoaderManager loaderManager = null;

	// 加载器Id
	private int loaderId = 0; // 不知道会不会跟UI里的loader冲突？
	// 加载器回调
	private LoaderCallbacks<byte[]> loaderCallbacks = null;
	// 任务加载器
	private AbsTaskLoaderHttpWait<byte[]> taskLoader = null;

	// 等待控件
	protected WaitFgm waitFgm = null;
	private int waitViewId = 0;

	private LoaderConfig loaderConfig = new LoaderConfig();

	/**
	 * 初始化
	 */
	private void init() {
		if (D) Log.e(TAG, "init()");
		initControl();
		initControlEvent();
		initMember();
	}

	protected abstract void initControl();
	protected abstract void initControlEvent();
	protected abstract void initMember();

	// 加载器
	protected abstract void onStartLoader();
	protected abstract byte[] doInBackgroundLoader();
	protected abstract void onFinishedLoader(Loader<byte[]> loader, byte[] bytes);

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

	protected android.support.v4.app.FragmentTransaction getTransaction() {
		return fragmentManager.beginTransaction();
	}

	protected void addFgm(int containerViewId, android.support.v4.app.Fragment fragment, String tag) {
		if (fragment == null) {
			return;
		}
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (isEmptyString(tag)) {
			fragmentTransaction.add(containerViewId, fragment);
		}else{
			if (containerViewId <= 0) {
				fragmentTransaction.add(fragment, tag);
			}else{
				fragmentTransaction.add(containerViewId, fragment, tag);
			}
		}
		fragmentTransaction.commit();
	}

	protected void addFgm(int containerViewId, android.support.v4.app.Fragment fragment) {
		addFgm(containerViewId, fragment, null);
	}

	protected void removeFgm(android.support.v4.app.Fragment fragment) {
		if (fragment == null) {
			return;
		}
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(fragment);
		fragmentTransaction.commit();
	}

	protected void hideFgm(android.support.v4.app.Fragment fragment) {
		if (fragment == null) {
			return;
		}
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.hide(fragment);
		fragmentTransaction.commit();
	}

	protected void showFgm(android.support.v4.app.Fragment fragment) {
		if (fragment == null) {
			return;
		}
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.show(fragment);
		fragmentTransaction.commit();
	}

	protected void replaceFgm(int containerViewId, android.support.v4.app.Fragment fragment, String tag) {
		if (containerViewId <= 0 || fragment == null) {
			return;
		}
		android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (isEmptyString(tag)) {
			fragmentTransaction.replace(containerViewId, fragment);
		}else{
			fragmentTransaction.replace(containerViewId, fragment, tag);
		}
		fragmentTransaction.commit();
	}

	protected void replaceFgm(int containerViewId, android.support.v4.app.Fragment fragment) {
		replaceFgm(containerViewId, fragment, null);
	}

	protected void switchFgm(int containerViewIdHide, String tagHide, int containerViewIdShow, String tagShow) {
//		replaceFgm(containerViewId, fragment, null);
	}
	
	/**
	 * 换切Fgm，隐藏一个，显示一个
	 * 
	 * 逻辑如下：当一个Fgm不存在，即没有add时，则创建新的Fgm并且添加上去。
	 * 
	 * @param containerViewIdHide
	 * @param fragmentHide
	 * @param containerViewIdShow
	 * @param tagShow
	 */
	protected void switchFgm(int containerViewIdHide, android.support.v4.app.Fragment fragmentHide, int containerViewIdShow, String tagShow) {
//		replaceFgm(containerViewId, fragment, null);
	}
	
	/**
	 * 获取扩展包
	 * 
	 * @return
	 */
	protected Bundle getExtras() {
		Bundle extras = getArguments();
		return extras;
	}
	
	/**
	 * 设置控件的可见性
	 *
	 * @param view
	 * @param enable
	 */
	public void setViewVisibility(View view, boolean enable) {
		if (view == null) {
			new NullPointerException("view == null").printStackTrace();
			return;
		}
		if (enable) {
			// 显示
			view.setVisibility(View.VISIBLE);
		}else{
			// 隐藏
			this.setViewVisibilityGONE(view); // 不占位
		}
		// 刷新
		view.invalidate();
	}

	/**
	 * 设置控件不可见（占位）
	 *
	 * @param view
	 * @param enable
	 */
	public void setViewVisibilityIN(View view) {
		if (view == null) {
			new NullPointerException("view == null").printStackTrace();
			return;
		}
		// 隐藏
		view.setVisibility(View.INVISIBLE); // 占位
		// 刷新
		view.invalidate();
	}

	/**
	 * 设置控件不可见（不占位）
	 *
	 * @param view
	 * @param enable
	 */
	public void setViewVisibilityGONE(View view) {
		if (view == null) {
			new NullPointerException("view == null").printStackTrace();
			return;
		}
		// 隐藏
		view.setVisibility(View.GONE); // 不占位
		// 刷新
		view.invalidate();
	}

	/**
	 * Android2.3系统的overscroll效果
	 * android:overScrollMode="never"
	 * setOverScrollMode(OVER_SCROLL_NEVER)
	 *
	 * @param enabled
	 */
	public void setOverScrollEnabled(View view, boolean enabled) {
		if (view == null) {
			return;
		}
		Log.e(TAG, "setOverScrollEnabled()");
		// View.OVER_SCROLL_NEVER == 2  // 2.3(9)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Ver2_3) {
			String classPath = "android.view.View";
			String methodName = "setOverScrollMode";
			String fieldName = null;
			if (enabled) {
				// public static final int OVER_SCROLL_ALWAYS
				// view.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
				// public void setOverScrollMode (int overScrollMode)
				fieldName = "OVER_SCROLL_ALWAYS";
			}else{
				// public static final int OVER_SCROLL_NEVER
				// view.setOverScrollMode(View.OVER_SCROLL_NEVER); // ok
				fieldName = "OVER_SCROLL_NEVER";
			}
			Log.e(TAG, "setOverScrollEnabled():fieldName:"+fieldName);
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
			if (method != null && value != -1) {
				ReflectTool.invokeMethod(view, method, value);
				Log.e(TAG, "setOverScrollEnabled():ReflectTool.invoke(...)");
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
	 * 打印WaitFgm信息
	 */
	private void printWaitFgm() {
		Log.d(TAG, "------ printWaitFgm() start ------");
		if (waitFgm != null) {
			Log.d(TAG, "isAdded():"+waitFgm.isAdded()); // 是否添加到父UI
			Log.d(TAG, "isDetached():"+waitFgm.isDetached()); // 是否已从父UI删除
			Log.d(TAG, "isHidden():"+waitFgm.isHidden());
			Log.d(TAG, "isInLayout():"+waitFgm.isInLayout());
			Log.d(TAG, "isRemoving():"+waitFgm.isRemoving());
			Log.d(TAG, "isResumed():"+waitFgm.isResumed());
			Log.d(TAG, "isVisible():"+waitFgm.isVisible()); // 是否可见
		}
		Log.d(TAG, "------ printWaitFgm() end ------");
	}

	/**
	 * 设置等待容器控件ID
	 * 容器控件有：ViewGroup、FrameLayout、LinearLayout、RelativeLayout
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
		}else{
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
					startLoader( loaderId );
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
			waitFgm.setListShown( false ); // 显示圆形进度条
			if(waitFgm.isHidden()) {
				showFgm(waitFgm);
			}
		}

		// 将父控件下的子控件全部隐藏
		hideChildView(waitViewId);

		if (waitFgm.isVisible() == false) { // 如果WaitFgm不显示
			// 显示
			setViewVisibility( waitFgm.getView(), true );
		}
	}

	/**
	 * 隐藏Wait控件
	 */
	private void hideWaitFgm() {
		Log.d(TAG, "hideWaitFgm()");
		showChildView(waitViewId);
		//		hideFgm(waitFgm); // TODO 会出错
		// 显示
		if (waitFgm != null) {
			setViewVisibilityGONE( waitFgm.getView() );
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

		if ( text == null ) {
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

		waitFgm.setListShown( true );
		waitFgm.setEmptyText( text );

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

		Window window = ui.getWindow();
		if (window == null) {
			return null;
		}
		// 得到setContentView()设置的布局
		View view = window.getDecorView();
		if (view == null) {
			return null;
		}
		// 得到父控件
		ViewGroup parentView = (ViewGroup)view.findViewById( containerViewId );
		return parentView;
	}

	/**
	 * 显示子控件
	 *
	 * @param parentViewId
	 */
	private void showChildView(int parentViewId) {
		Log.d(TAG, "showChildView()");
		ViewGroup parentView = getContentView( parentViewId );
		if (parentView == null) {
			return;
		}

		int len = parentView.getChildCount();
		Log.d(TAG, "showChildView():len:"+len);
		for (int n = 0; n < len; n++) {
			setViewVisibility( parentView.getChildAt(n), true );
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

		if ("android.support.v4.app.NoSaveStateFrameLayout".equalsIgnoreCase( classPath )) {
			return true;
		}

		return false;
	}

	/**
	 * 将父控件下的子控件全部隐藏  TODO 要将原来的信息保存，因为不显示之前隐藏的。
	 *
	 * @param parentViewId
	 */
	private void hideChildView(int parentViewId) {
		Log.d(TAG, "hideChildView()");
		ViewGroup parentView = getContentView( parentViewId );
		if (parentView == null) {
			return;
		}

		int childCount = parentView.getChildCount();
		Log.d(TAG, "hideChildView():childCount:"+childCount);

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

		Log.d(TAG, "hideChildView():len:"+len);
		
		if ( !(len >= 1 && len <= 2) ) {
			Log.throwException("setWaitViewId()设置的控件只能含有一个子控件");
		}

		for (int n = 0; n < childCount; n++) {
			// 不占位隐藏
			setViewVisibilityGONE( parentView.getChildAt(n) );
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
	 * 在AbsUI.java加入Loader的功能哪一些？
	 * 答：
	 * 1)提供了Loader的几个常用方法:
	 * 		startLoader() restartLoader() destroyLoader();
	 * 		默认不运行Loader
	 * 2)有错误的详细内容提示：
	 * 		当遇到"无网络"，"非JSON格式"，"非XML格式","HTTP错误的状态码"
	 * 		"连接失败","连接超时","HTTP请求成功，但无数据"
	 * 3)保持Loader与UI的关系，即与UI的生命周期有关系。
	 * 4)可弹出等待对话框，详见：AbsTaskLoaderHttpWait.java
	 * 5)点击可刷新。
	 * 6)
	 * 7)
	 * 8)
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
				if (D) Log.d(TAG, "onCreateLoader()");

				// 创建Loader
				taskLoader = new AbsTaskLoaderHttpWait<byte[]>(ui, R.string.loading) {

					@Override
					protected void onStartLoading() {
						if (D) Log.d(TAG, "onStartLoading()");
						// abs回调
						onStartLoader();
						super.onStartLoading();
					}

					@Override
					public byte[] loadInBackground() {
						if (D) Log.d(TAG, "loadInBackground()");
						// abs回调
						byte[] bytes = doInBackgroundLoader();
						return bytes;
					}

					@Override
					protected void onHttpFailed(Error error, java.lang.Exception exception, int responseCode, byte[] out) {
						if (D) Log.d(TAG, "onHttpFailed():error:"+error+",responseCode:"+responseCode);

						String errorText = null;

						if (error == HttpTool.Error.ResponseCodeError) {
							// 状态码为500,不是session超时
							ResManager res = new ResManager(context);
							String responseCodeText = res.parseString(R.array.tools_http_status_code, responseCode, R.array.tools_http_status_caption);
							errorText = String.format("HTTP状态码错误\n%d - %s\n点击重试", responseCode, responseCodeText);
						}else if (error == HttpTool.Error.NotNetwork) {
							errorText = String.format("无网络\n点击重试");
						}else if (error == HttpTool.Error.ConnectException) {
							errorText = String.format("连接失败\n点击重试");
						}else if (error == HttpTool.Error.ConnectTimeoutException) {
							errorText = String.format("连接超时\n点击重试");
						}else if (error == HttpTool.Error.IOException) {
							errorText = String.format("连接失败\n点击重试");
						}else if (error == HttpTool.Error.InterruptedIOException) {
							errorText = String.format("连接失败\n点击重试");
						}else if (error == HttpTool.Error.SocketException) {
							errorText = String.format("获取失败\n点击重试");
						}else if (error == HttpTool.Error.SocketTimeoutException) {
							errorText = String.format("获取超时\n点击重试");
						}else if (error == HttpTool.Error.OtherException) {
							errorText = String.format("其它失败\n点击重试");
						}

						// 显示错误文本
						setWaitText( errorText );

						// 按HOME键让APP到后台，再返回前台，刷新时，点击无响应。
						// 原因是isStarted()为true
						// 所以，出现错误时，则reset();
						reset();
						
					}

				};

				if (loaderConfig == null) {
					loaderConfig = new LoaderConfig();
				}

				//				taskLoader.setDialogCloseable(true); // 可以关闭
				//				taskLoader.setDialogShowable(true); // 不显示对话框
				Log.i(TAG, "loaderConfig.getDialogCloseable():"+loaderConfig.getDialogCloseable());
				Log.i(TAG, "loaderConfig.getDialogShowable():"+loaderConfig.getDialogShowable());
				taskLoader.setDialogCloseable( loaderConfig.getDialogCloseable() ); // 可以关闭
				taskLoader.setDialogShowable( loaderConfig.getDialogShowable() ); // 不显示对话框

				return taskLoader;
			}

			@Override
			public void onLoadFinished(Loader<byte[]> arg0, byte[] arg1) {

				if (D) Log.d(TAG, "------ onLoadFinished() start ------");

				// loader加载完成时调用
				if (D) Log.d(TAG, "onLoadFinished():arg0:"+arg0);
				if (D) Log.d(TAG, "onLoadFinished():arg1:"+arg1);

				if (arg0 != null) {
					// 停止loader
					arg0.stopLoading();
					// 关闭loader，防止onStart()再次运行。
					//					ui.getSupportLoaderManager().destroyLoader( arg0.getId() );
					// 销毁加载器
					//					destroyLoader();
				}

				boolean isShowWaitFgm = false; // 是否显示WaitFgm控件

				// 得到HTTP对象
				HttpTool http = getTaskLoader().getHttp();

				// 如果http错误，则不要显示子控件
				HttpTool.Error error = http.getError();
				if (D) Log.d(TAG, "onLoadFinished():error:"+error.name());

				if (error == HttpTool.Error.Successful) { // 请求成功
					// 字节数组转成字符串
					String resultText = Charset.convertString( arg1, http.getConfig().getHeader().getCharset(), Charset.defaultCharset() );
					if (D) Log.d(TAG, "onLoadFinished():resultText:"+resultText);
					if ( isEmptyString(resultText) == false ) { // 字符串  > 0
						if (D) Log.d(TAG, "onLoadFinished():resultText字符串长度>0");
						// 这里有可能是JSON格式，也有可能是XML格式。
						// 取第一个字节，如果是{或者[，则为JSON格式，如果是<，则为XML格式
						boolean isVerifyJSON = false;
						boolean isVerifyXML = false;

						int resultTextLen = resultText.length();
						// 取第一个
						char firstChar = resultText.charAt(0);
						// 取最后一个
						char endChar = resultText.charAt( resultTextLen-1 );
						//						Log.e(TAG, "endChar:"+String.valueOf(endChar));

						if (firstChar == '{' && endChar == '}') {
							isVerifyJSON = true;
						} else if (firstChar == '[' && endChar == ']') {
							isVerifyJSON = true;
						} else if (firstChar == '<' && endChar == '>') {
							isVerifyXML = true;
						}

						if (D) Log.d(TAG, "onLoadFinished():isVerifyJSON:"+isVerifyJSON);
						if (D) Log.d(TAG, "onLoadFinished():isVerifyXML:"+isVerifyXML);

						// TODO 强制支持JSON格式
						// isJSONString = true;
						// isVerifyXML = true;

						// super.setTextType() // 设置内容类型。是纯文件，还是JSON(默认)，还是XML？？？
						// 还可能还是对象，或者是十六进制fff
						// 或者从http里判断
						if (isVerifyJSON == true) {
							// 有内容，但是"不是JSON标准格式"，则显示错误文本。
							boolean isJson = GJson.isJson( resultText );
							if (isJson == false) {
								isShowWaitFgm = true; // 是否显示WaitFgm控件
								if (isFinished() == false) {
									setWaitText("非JSON标准格式\n点击重试");
								}
							}
						} else if (isVerifyXML == true) {
							//						if (isFinished() == false) {
							//							setWaitText("非XML标准格式\n点击重试");
							//						}
							isShowWaitFgm = false;
						} else if (isVerifyJSON == false && isVerifyXML == false) {
							isShowWaitFgm = true; // 是否显示WaitFgm控件
							if (isFinished() == false) {
								setWaitText("非JSON、XML格式\n点击重试");
							}
						}
					} else { // 字符串 <=0
						if (D) Log.d(TAG, "onLoadFinished():resultText字符串长度<=0");
						isShowWaitFgm = true; // 是否显示WaitFgm控件
						if (isFinished() == false) {
							setWaitText("HTTP请求成功，但无数据\n点击重试");
						}
					}
				} else { // 请求失败
					isShowWaitFgm = true; // 是否显示WaitFgm控件
				}

				if (D) Log.d(TAG, "onLoadFinished():isShowWaitFgm:"+isShowWaitFgm);

				Log.d(TAG, "onLoadFinished()--->isFinished():"+isFinished());
				if (isFinished() == false) {
					if (isShowWaitFgm == false) { // 是否显示WaitFgm控件
						hideWaitFgm();
					}

					// 不在这里处理HTTP SESSION 超时问题，因解析文本会耗电等因素。

					// abs回调
					onFinishedLoader(arg0, arg1);
				}

				if (D) Log.d(TAG, "------ onLoadFinished() end ------");
			}

			@Override
			public void onLoaderReset(Loader<byte[]> arg0) {
				// loader被重置时调用，以设置数据无效
				if (D) Log.d(TAG, "onLoaderReset()");
			}

		}; // end loader call

	}

	/**
	 * 启动加载器(如果加载器已启动，则不会再次加载)
	 */
	//	@Deprecated
	//	protected void startLoader() {
	//		startLoader( 0 );
	//	}

	/**
	 * 启动加载器(如果加载器已启动，则不会再次加载)
	 */
	protected void startLoader(int loaderId) {
		Log.d(TAG, "startLoader():loaderId:"+loaderId);

		this.loaderId = loaderId;

		boolean isStarted = false;

		if (taskLoader != null) {
			taskLoader.print();
			isStarted = taskLoader.isStarted();
		}

		Log.d(TAG, "startLoader():isStarted:"+isStarted);

		if (isStarted == false) {
			restartLoader( this.loaderId );
		}
	}

	/**
	 * 重新启动加载器(不管加载器是否已启动，会再次加载)
	 */
	//	@Deprecated
	//	protected void restartLoader() {
	//		restartLoader( 0 );
	//	}

	/**
	 * 重新启动加载器(不管加载器是否已启动，会再次加载)
	 */
	protected void restartLoader(int loaderId) {
		Log.d(TAG, "restartLoader():loaderId:"+loaderId);

		this.loaderId = loaderId;

		// 创建加载器
		createLoader();

		ui.getSupportLoaderManager().restartLoader(loaderId, null, loaderCallbacks);
	}

	/**
	 * 销毁加载器
	 */
	protected void destroyLoader() {
		Log.d(TAG, "destroyLoader()");

		if (taskLoader != null) {
			taskLoader.print();

			boolean isStarted = taskLoader.isStarted();
			Log.d(TAG, "destroyLoader():isStarted:"+isStarted);
			if ( isStarted ) {
				// 如果Loader已经开始，则恢复控件
				hideWaitFgm();
			}
		}

		// 如果Loader已经开始，则恢复控件
		//		hideWaitFgm(); // 不要这句，因onFinishedLoader()可以使用destroyLoader()

		ui.getSupportLoaderManager().destroyLoader(this.loaderId);
	}

	/**
	 * 判断是否结束
	 *
	 * @return
	 */
	protected boolean isFinished() {
		return !super.isAdded();
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

	@Override
	public void onAttach(Activity activity) {
		if (D) Log.e(TAG, "onAttach(第一个)");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (D) Log.e(TAG, "onCreate(第二个)");

		this.ui = this.getActivity();
		context = ui.getApplicationContext();

		if (fragmentManager == null) {
			fragmentManager = ui.getSupportFragmentManager();
		}
		if (loaderManager == null) {
			loaderManager = ui.getSupportLoaderManager();
		}

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (D) Log.e(TAG, "onCreateView(第三个)[被继承类重写]");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if (D) Log.e(TAG, "onViewCreated(第四个)");
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if (D) Log.e(TAG, "onActivityCreated(第五个)");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		if (D) Log.e(TAG, "onStart()");
		super.onStart();
	}

	@Override
	public void onResume() {
		if (D) Log.e(TAG, "onResume()");
		super.onResume();
	}

	@Override
	public void onPause() {
		if (D) Log.e(TAG, "onPause()");
		super.onPause();
	}

	@Override
	public void onStop() {
		if (D) Log.e(TAG, "onStop()");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		if (D) Log.e(TAG, "onDestroyView(倒数第三个)");
		destroyLoader();
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		if (D) Log.e(TAG, "onDestroy(倒数第二个)");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		if (D) Log.e(TAG, "onDetach(最后一个)");
		super.onDetach();
	}

}
