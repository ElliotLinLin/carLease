package com.hst.Carlease.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.PeopleBean;
import com.hst.Carlease.http.bean.RegisterBean;
import com.hst.Carlease.http.bean.ZMCertBean;
import com.hst.Carlease.http.bean.ZMCertRequestBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.util.Log;
import com.tools.widget.Prompt;
import com.zmxy.ZMCertification;
import com.zmxy.ZMCertificationListener;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public class FeedBacktUI extends AbsUI implements ZMCertificationListener {
	private static final String TAG = FeedBacktUI.class.getSimpleName();
	private TitleBar titlebar;
	private EditText username, phone;
	private TextView order_order, number;
	private ZMCertification zmCertification;

	@Override
	protected void onCreate(Bundle arg0) {
		super.setContentView(R.layout.ui_peopl_zmcert);
		super.onCreate(arg0);
	}

	@Override
	public void onFinish(boolean isCanceled, boolean isPassed, int errorCode) {
		zmCertification.setZMCertificationListener(null);
		Log.i(TAG, "错误原因：" + errorCode);
		ReturnFaceCertification();
		if (isCanceled)
			ToastL.show("芝麻验证失败,");
		else {
			if (isPassed)
				ToastL.show("芝麻验证成功!");
			else if (errorCode == 1)
				ToastL.show(" 芝麻验证失败,用户人脸与数据库中的人脸比对分数较低");
			else if (errorCode == 2)
				ToastL.show(" 芝麻验证失败,手机在不支持列表里");
			else if (errorCode == 3)
				ToastL.show(" 芝麻验证失败,缺少手机权限 ");
			else if (errorCode == 4)
				ToastL.show(" 芝麻验证失败,没有联网权限 ");
			else if (errorCode == 5)
				ToastL.show(" 芝麻验证失败,没有打开相机的权限 ");
			else if (errorCode == 6)
				ToastL.show(" 芝麻验证失败,无法读取运动数据的权限 ");
			else if (errorCode == 7)
				ToastL.show(" 芝麻验证失败,人脸采集算法初始化失败 ");
			else if (errorCode == 8)
				ToastL.show(" 芝麻验证失败,发生网络错误");
			else if (errorCode == 9)
				ToastL.show(" 芝麻验证失败,传入的bizNO 有误");
			else if (errorCode == 10)
				ToastL.show(" 芝麻验证失败,此APP的bundle_id在系统的黑名单库里");
			else if (errorCode == 11)
				ToastL.show(" 芝麻验证失败,数据源错误");
			else if (errorCode == 12)
				ToastL.show(" 芝麻验证失败,服务发生内部错误");
			else if (errorCode == 13)
				ToastL.show(" 芝麻验证失败,bizNO和merchantID不匹配");
			else if (errorCode == 14)
				ToastL.show(" 芝麻验证失败,SDK版本过旧");
			else if (errorCode == 15)
				ToastL.show(" 芝麻验证失败,身份证号和姓名的格式不正确");
			else if (errorCode == 16)
				ToastL.show(" 芝麻验证失败,身份证号和姓名在一天内使用次数过多");
			else
				ToastL.show(" 芝麻验证失败," + errorCode);
		}
	}

	@Override
	protected void initControl() {
		titlebar = new TitleBar();
		zmCertification = ZMCertification.getInstance();
		username = (EditText) findViewById(R.id.username);
		phone = (EditText) findViewById(R.id.phone);
		order_order = (TextView) findViewById(R.id.order_order);
		number = (TextView) findViewById(R.id.number);
	}

	@Override
	protected void initControlEvent() {
		order_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (username.getText().toString().isEmpty()) {
//					ToastL.show("请输入姓名");
//					return;
//				}
//				if (phone.getText().toString().isEmpty()) {
//					ToastL.show("请输入身份证号码");
//					return;
//				}
//				ReturnFaceCertification();
				GetBiznoList();
			}
		});
	}

	/**
	 * 我的违章列表
	 */
	protected void GetBiznoList() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		ZMCertBean bean = new ZMCertBean();
		bean.setCertno(phone.getText().toString().trim());
		bean.setName(username.getText().toString().trim());
		String url = "http://172.16.0.199:8056/Interface/Zmxy.asmx/Bizno";// Http_Url.Bizno
		try {
			AsyncHttpUtil.post(ui, url, bean, "application/json",
					new AsyncCallBackHandler(ui, "", true, order_order) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							if (arg2 == null) {
								return;
							}
							Bean bean = GJson.parseObject(arg2, Bean.class);
							if (bean != null) {
								Log.i(TAG, "result===" + bean.getD());
								PeopleBean PayBean2 = GJson.parseObject(
										bean.getD(), PeopleBean.class);
								if (PayBean2 != null) {
									if (PayBean2.getStatu() == 1) {
										if (PayBean2.getModel() == null) {
											return;
										}
										zmCertification
												.setZMCertificationListener(FeedBacktUI.this);
										zmCertification.startCertification(
												FeedBacktUI.this,PayBean2.getModel().getBizno(), PayBean2
														.getModel()
														.getMerchant_id(),
												
												null);
									} else {
										ToastL.show(PayBean2.getMsg());
										return;
									}

								}

							}

						}

					});

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

	}
	/**
	 * 我的违章列表
	 */
	protected void ReturnFaceCertification() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		ZMCertRequestBean bean = new ZMCertRequestBean();
		bean.setIspass(false);
		bean.setTid(1);
		bean.setTokenID((String) SPUtils.get(FeedBacktUI.this, Constants.tokenID,""));
		try {
			AsyncHttpUtil.post(ui, Http_Url.ReturnFaceCertification, bean, "application/json",
					new AsyncCallBackHandler(ui, "", true, order_order) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							if (arg2 == null) {
								return;
							}
							Bean bean = GJson.parseObject(arg2, Bean.class);
							if (bean != null) {
								Log.i(TAG, "result===" + bean.getD());
								RegisterBean PayBean2 = GJson.parseObject(
										bean.getD(), RegisterBean.class);
								if (PayBean2 != null) {
									if (PayBean2.getStatu() == 1) {
										ToastL.show(PayBean2.getMsg());
									} else {
										ToastL.show(PayBean2.getMsg());
										return;
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
	protected void initMember() {
		super.addFgm(R.id.titlebar, titlebar);
	}

	@Override
	public void onAttachedToWindow() {
		titlebar.setTitle("人脸识别");
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
	protected void onFinishedLoader(
			android.support.v4.content.Loader<byte[]> loader, byte[] bytes) {

	}

}
