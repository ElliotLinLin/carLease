package com.tools.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;

import com.hst.Carlease.R;
import com.tools.app.AbsUI2;

public class TestDurableServiceUI extends AbsUI2 {

	protected Button button_add;
	protected Button button_remove;
	protected Button button_isExists;

	protected Button button_start;
	protected Button button_cancel;
	protected Button button_stop;

	DurableService durableService;

	private Intent myIntent = null; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.tools_ui_test_durable_service);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		button_add = (Button) findViewById(R.id.button_add);
		button_remove = (Button) findViewById(R.id.button_remove);
		button_isExists = (Button) findViewById(R.id.button_isExists);
		button_start = (Button) findViewById(R.id.button_start);
		button_cancel = (Button) findViewById(R.id.button_cancel);
		button_stop = (Button) findViewById(R.id.button_stop);

		durableService = new DurableService(context);
		
//		myIntent = new Intent(context, PushService.class);
//		myIntent.putExtra(PushService.Key_Command, PushService.Command_Push);
		
		//		// 创建一个
		//		Intent intent = new Intent(context, PushService.class);
		//		intent.putExtra(PushService.Key_Command, PushService.Command_Push);
		//		// 添加
		//		durableService.add(intent, 1L * 60L * 1000L); // 1分钟
		//		// 开始
		//		durableService.start();
	}

	@Override
	protected void initControlEvent() {
		button_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(context, PushService.class);
//				intent.putExtra(PushService.Key_Command, PushService.Command_Push);
//				
//				myIntent = intent;
					
				// 添加
				durableService.add(myIntent, 1L * 10L * 1000L); // 1分钟
			}
		});
		button_remove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(context, AsyncQueueService.class);
//				durableService.remove(myIntent);
			}
		});
		button_isExists.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				durableService.isExists(myIntent);
			}
		});
		button_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 开始
				durableService.start();
			}
		});
		button_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				durableService.cancel(myIntent);
			}
		});
		button_stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				durableService.stop();
			}
		});
	}

	@Override
	protected void initMember() {


	}

	@Override
	protected void onStartLoader() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected byte[] doInBackgroundLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

}
