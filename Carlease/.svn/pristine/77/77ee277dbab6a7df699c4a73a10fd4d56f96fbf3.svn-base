package com.tools.app;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hst.Carlease.R;
import com.tools.task.LoaderConfig;
import com.tools.util.Log;


/**
 * 测试AbsDialogFgm界面
 */
public class TestAbsDialogFgm extends AbsDialogFgm {

	public static final String TAG = TestAbsDialogFgm.class.getSimpleName();

	private LinearLayout ui_test_absdialogfgm_wait;
	
	private Button ui_test_absdialogfgm_buttonWait;
	
	private int loaderId = 3;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tools_ui_test_absdialogfgm, container, false);
		return view;
	}

	@Override
	protected void initControl() {
		ui_test_absdialogfgm_wait = (LinearLayout) this.getView().findViewById(R.id.ui_test_absdialogfgm_wait);
		ui_test_absdialogfgm_buttonWait = (Button) this.getView().findViewById(R.id.ui_test_absdialogfgm_buttonWait);
		
		if (ui_test_absdialogfgm_wait == null) {
			Log.e(TAG, "ui_test_absdialogfgm_wait == null");
		}else{
			Log.e(TAG, "ui_test_absdialogfgm_wait != null");
		}
		
	}

	@Override
	protected void initControlEvent() {
		
		ui_test_absdialogfgm_buttonWait.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startLoader( loaderId );
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

		// TODO 有错误
//		super.setWaitViewId(R.id.ui_test_absdialogfgm_wait);
		
	}

	@Override
	protected byte[] doInBackgroundLoader() {

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {


	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

}
