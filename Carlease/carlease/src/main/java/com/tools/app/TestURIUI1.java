package com.tools.app;


import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;

import com.hst.Carlease.R;
import com.tools.util.Log;

/**
 *
 */
public class TestURIUI1 extends AbsUI {

	private static final String TAG = TestURIUI1.class.getSimpleName();
	private static final boolean D = false; // 是否允许打印LOG
	private static final boolean S = false; // 是否允许模拟数据

	private TitleBar titleBar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.tools_ui_test_uri1);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		titleBar = new TitleBar();
	}

	@Override
	protected void initControlEvent() {
		
	}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titleBar, titleBar);
		
		String s = super.getExtras().getString("key1");
		Log.e(TAG, "ssssssss:"+s);
		
		Log.printIntent(TAG, this.getIntent());
	}

	@Override
	public void onAttachedToWindow() {
		
		titleBar.setTitle("URI 1");
		Log.e(TAG, "URI 1");

		super.setViewVisibility(titleBar.getLeftView(1), true);
		titleBar.getLeftView(1).setBackgroundResource(R.drawable.tools_btn_back_selector);
		titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
		super.onAttachedToWindow();
	}

	@Override
	protected void onStartLoader() {

	}

	@Override
	protected byte[] doInBackgroundLoader() {
		return null;
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

	}

}
