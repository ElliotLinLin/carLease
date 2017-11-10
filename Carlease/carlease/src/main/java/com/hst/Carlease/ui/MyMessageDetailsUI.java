package com.hst.Carlease.ui;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.MessageDetailsBean;
import com.hst.Carlease.http.bean.RequestBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.util.Log;
import com.tools.widget.Prompt;

public class MyMessageDetailsUI extends AbsUI {
	private final String TAG = MyMessageDetailsUI.class.getSimpleName();
	private TitleBar titleBar;
	private TextView title, tiem, content;
	private Intent intent;

	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {  
	        
	        finish();  
	        return;  
	    }  
		super.setContentView(R.layout.ui_messagedetails);
		super.setSlideFinishEnabled(false);
		super.onCreate(arg0);
	}

	@Override
	protected void initControl() {
		titleBar = new TitleBar();
		title = (TextView) findViewById(R.id.title);
		tiem = (TextView) findViewById(R.id.tiem);
		content = (TextView) findViewById(R.id.content);
		intent = getIntent();
		getMessageDetails();
	}

	@Override
	protected void initControlEvent() {

	}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titlebar, titleBar);
	}

	private void getMessageDetails() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		RequestBean bean = new RequestBean();
		bean.setId(intent.getStringExtra("id"));

		try {
			AsyncHttpUtil.post(ui, Http_Url.GetNewsDetailList, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,title) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Log.i(TAG, "result" + arg2);
							Bean bean = GJson.parseObject(arg2, Bean.class);
							Log.i(TAG, "result 截取" + bean.getD());
							if (bean.getD() != null) {
								MessageDetailsBean beans = GJson.parseObject(
										bean.getD(), MessageDetailsBean.class);
								if (beans.getModel() == null) {
									return;
								}
								if (beans.getStatu() == 1) {
									title.setText("标题："+beans.getModel().getM_Title());
									tiem.setText("时间："+beans.getModel()
											.getM_CreateDate());
									content.setText("内容："+beans.getModel()
											.getM_Content());
								} else {
									ToastL.show(beans.getMsg());
								}
							}

						}

					});

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		titleBar.setTitle("我的消息");
		titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

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
