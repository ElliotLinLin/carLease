package com.tools.lang.reflect;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;

import com.hst.Carlease.R;
import com.tools.app.AbsUI2;
import com.tools.util.Log;

public class ReflectUI extends AbsUI2 {

	private static final String TAG = ReflectUI.class.getSimpleName();

	private Button buttonisClassExists;
	private Button buttonisMethodExists;
	private Button buttonisFieldExists;
	private Button buttongetMethod;
	private Button buttoninvokeMethod;
	private Button buttongetObject;
	private Button buttonprintClass;
	private Button buttoncreateObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		setContentView(R.layout.tools_ui_reflect);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		buttonisClassExists = (Button) findViewById(R.id.buttonisClassExists);
		buttonisMethodExists = (Button) findViewById(R.id.buttonisMethodExists);
		buttonisFieldExists = (Button) findViewById(R.id.buttonisFieldExists);
		buttongetMethod = (Button) findViewById(R.id.buttongetMethod);
		buttoninvokeMethod = (Button) findViewById(R.id.buttoninvokeMethod);
		buttongetObject = (Button) findViewById(R.id.buttongetObject);
		buttonprintClass = (Button) findViewById(R.id.buttonprintClass);
		buttoncreateObject = (Button) findViewById(R.id.buttoncreateObject);
	}

	@Override
	protected void initControlEvent() {
		buttonisClassExists.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TestReflectTool.isClassExists();
			}

		});
		buttonisMethodExists.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TestReflectTool.isMethodExists();
			}

		});
		buttonisFieldExists.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TestReflectTool.isFieldExists();
			}

		});
		buttongetMethod.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TestReflectTool.getMethod();
			}

		});
		buttoninvokeMethod.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TestReflectTool.invokeMethod();
			}

		});
		buttongetObject.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TestReflectTool.getFieldObject();
			}

		});
		buttonprintClass.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TestReflectTool.printClass();
			}

		});
		buttoncreateObject.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TestReflectTool.createObject();
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
