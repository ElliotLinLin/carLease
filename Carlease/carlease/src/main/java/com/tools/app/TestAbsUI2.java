package com.tools.app;


import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.LinearLayout;

import com.hst.Carlease.R;
import com.tools.net.http.HttpConfig;
import com.tools.net.http.HttpTool;
import com.tools.task.LoaderConfig;
import com.tools.util.Log;

/**
 * 测试AbsUI2界面
 * 
 * @author mosq
 *
 */
public class TestAbsUI2 extends AbsUI2 {

	private static final String TAG = TestAbsUI2.class.getSimpleName();
	private static final boolean D = false; // 是否允许打印LOG
	private static final boolean S = false; // 是否允许模拟数据

	private TitleBar titleBar = null;

	private LinearLayout ui_test_absui2_wait;

	private int loaderId = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.tools_ui_test_absui2);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		titleBar = new TitleBar();
		ui_test_absui2_wait = (LinearLayout) findViewById(R.id.ui_test_absui2_wait);
	}

	@Override
	protected void initControlEvent() {
		//		buttonStart.setOnClickListener(new View.OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//
		//			}
		//
		//		});
		//
		//		buttonStop.setOnClickListener(new View.OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//
		//			}
		//
		//		});
	}

	@Override
	protected void initMember() {

		super.addFgm(R.id.titleBar, titleBar);

		super.addFgm(R.id.ui_test_absui2_wait, new TestAbsFgm2());

		// 创建Loader配置
		LoaderConfig loaderConfig = new LoaderConfig();
		loaderConfig.setDialogCloseable(true); // 是否可关闭 
		loaderConfig.setDialogShowable(false); // 是否显示对话框

		// 设置Loader配置
		super.setLoaderConfig(loaderConfig);

	}

	@Override
	public void onAttachedToWindow() {

		titleBar.setTitle("AbsUI2.java(测试界面)");

		super.setViewVisibility(titleBar.getLeftView(1), true);
		titleBar.getLeftView(1).setBackgroundResource(R.drawable.tools_btn_back_selector);
		titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startLoader( loaderId );
			}

		});

		super.setViewVisibility(titleBar.getRightView(2), true);
		titleBar.getRightView(2).setBackgroundResource(R.drawable.tools_btn_back_selector);
		titleBar.getRightView(2).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				restartLoader( loaderId );
			}

		});

		super.setViewVisibility(titleBar.getRightView(1), true);
		titleBar.getRightView(1).setBackgroundResource(R.drawable.tools_btn_back_selector);
		titleBar.getRightView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				destroyLoader();
			}

		});

		super.onAttachedToWindow();
	}

	@Override
	protected void onStartLoader() {

		// 设置某个控件为圆形进度条，注意R.id.absui2_linearLayout_parent只能含有一个子控件
		super.setWaitViewId(R.id.ui_test_absui2_wait);

		// super.setTextType() // 设置内容类型。是纯文件，还是JSON(默认)，还是XML？？？
		// 还可能还是对象，或者是十六进制
		// 或者从http里判断

	}

	@Override
	protected byte[] doInBackgroundLoader() {

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 创建HTTP头配置
		HttpConfig.Header httpHeaderConfig = new HttpConfig.Header();
		//		httpHeaderConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		httpHeaderConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型

		// 创建HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( httpHeaderConfig ); // 设置HTTP头配置

		// 得到HTTP对象
		HttpTool http = super.getTaskLoader().getHttp();

		// 设置HTTP配置
		http.setConfig(httpConfig);

		String url = "http://rental.wisdom-gps.com/mobile.asmx/LoginNew?";
		Log.i(TAG, "url:"+url);

		// HTTP请求
		return http.doGet(url, null);
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

		// 得到HTTP对象
		HttpTool http = super.getTaskLoader().getHttp();

		// 将字节数组转成本地字符集
//		String resultText = Charset.convertString( bytes, HttpRam.getTextcharset(), Charset.UTF_8 );
//		Log.i(TAG, "resultText:"+resultText);

		super.showToast("完成。。。。");

		//		destroyLoader(); // 关闭加载器，与UI生命周期分离
	}

}
