package com.hst.Carlease.ui;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.CommonBean;
import com.hst.Carlease.http.bean.OnlineBookingBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.util.LocationChangedUtils;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.widget.Prompt;

public class OnlineOrderUI extends AbsUI2 {
	private TitleBar titlebar;
	private EditText username, phone;
	private TextView order_order;
	private LocationChangedUtils utils;// 定位

	@Override
	protected void onCreate(Bundle arg0) {
		super.setContentView(R.layout.ui_online_order);
		super.onCreate(arg0);
	}

	@Override
	protected void initControl() {
		titlebar = new TitleBar();
		utils=new LocationChangedUtils(context, ui);
		username = (EditText) findViewById(R.id.username);
		phone = (EditText) findViewById(R.id.phone);
		order_order = (TextView) findViewById(R.id.order_order);
		phone.setText(SPUtils.get(context, Constants.UserName, "").toString());
	}

	@Override
	protected void initControlEvent() {
		order_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (username.getText().toString().trim().isEmpty()) {
					ToastL.show("请输入姓名");
					return;
				}
				if (phone.getText().toString().isEmpty()) {
					ToastL.show("请输入手机号码");
					return;
				}
				if (StringUtils.isPhoneLegal(phone.getText().toString()) == false) {
					ToastL.show("请输入合法的手机号码");
					return;
				}
				AppointmentCarTask();
			}
		});
	}

	// 找回密码的任务
	private void AppointmentCarTask() {
		OnlineBookingBean bean = new OnlineBookingBean();
		bean.setMobile(phone.getText().toString());
		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());
		bean.setUserName(username.getText().toString());
		bean.setAddress(utils.cityname);
		NetworkState state = new NetworkState(context);
		if (state.isConnected() == false) {
			Prompt.showWarning(context, "请检查您的网络");
			return;
		}
		try {
			AsyncHttpUtil.post(context, Http_Url.AppointmentCar, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							phone) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
//							Log.e(TAG, "qqqq arg2" + arg2);
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Bean bean = GJson.parseObject(arg2, Bean.class);
//							Log.i(TAG, "result" + bean.getD());
							if (bean != null) {
								CommonBean CommonBean = GJson.parseObject(
										bean.getD(), CommonBean.class);
								if (CommonBean!=null) {
									if (CommonBean.getStatu() == 1) {// 成功
										ToastL.show(CommonBean.getMsg());
										finish();
										stopUI(ui);
									} else if (CommonBean.getStatu() ==-2) {// 获取用户标识失败
										ToastL.show(CommonBean.getMsg());
										StringUtils.IsOUTOFtime(context,
												OnlineOrderUI.this.ui);
									} else {
										ToastL.show(CommonBean.getMsg());
									}
								}
							}
						}

					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}
	@Override
	protected void onResume() {
		utils.startOnce();
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		utils.stop();
		super.onDestroy();
	}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titlebar, titlebar);
	}

	@Override
	public void onAttachedToWindow() {
		titlebar.setTitle("预约看车");
		titlebar.getLeftView(1).setVisibility(View.VISIBLE);
		titlebar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

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
