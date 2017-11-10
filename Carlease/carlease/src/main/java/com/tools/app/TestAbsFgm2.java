package com.tools.app;


import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hst.Carlease.R;
import com.tools.net.http.HttpConfig;
import com.tools.net.http.HttpTool;
import com.tools.task.LoaderConfig;
import com.tools.util.Log;

/**
 * 测试AbsFgm2界面
 * 
 * @author mosq
 *
 */
public class TestAbsFgm2 extends AbsFgm2 {

	private static final String TAG = TestAbsFgm2.class.getSimpleName();
	private static final boolean D = false; // 是否允许打印LOG
	private static final boolean S = false; // 是否允许模拟数据

	private LinearLayout ui_test_absfgm2_wait = null;

	private int loaderId = 2;

	private Button buttonOpenAbsDialogFgm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(TAG, "onCreateView(3)");
		View rootView = inflater.inflate(R.layout.tools_ui_test_absfgm2, container, false);
		return rootView;
	}

	@Override
	protected void initControl() {
		ui_test_absfgm2_wait = (LinearLayout) ui.findViewById(R.id.ui_test_absfgm2_wait);
		buttonOpenAbsDialogFgm = (Button) ui.findViewById(R.id.buttonOpenAbsDialogFgm);
	}

	@Override
	protected void initControlEvent() {

		buttonOpenAbsDialogFgm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TestAbsDialogFgm dialogFgm = new TestAbsDialogFgm();
				dialogFgm.show(fragmentManager, "TestAbsDialogFgm");
			}

		});

	}

	@Override
	protected void initMember() {

		// 创建Loader配置
		LoaderConfig loaderConfig = new LoaderConfig();
		loaderConfig.setDialogCloseable(true); // 是否可关闭 
		loaderConfig.setDialogShowable(false); // 是否显示对话框

		// 设置Loader配置
		super.setLoaderConfig(loaderConfig);
		
		super.startLoader( loaderId );

	}

	@Override
	protected void onStartLoader() {

		// 设置某个控件为圆形进度条，注意R.id.absui2_linearLayout_parent只能含有一个子控件
		super.setWaitViewId(R.id.ui_test_absfgm2_wait);

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
