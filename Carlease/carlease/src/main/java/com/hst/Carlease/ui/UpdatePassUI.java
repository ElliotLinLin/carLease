package com.hst.Carlease.ui;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.app.MainApplication;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.eventBean.MainStype;
import com.hst.Carlease.http.bean.RegisterBean;
import com.hst.Carlease.operate.PushServiceOperate;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.service.PushService;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.loopj.android.http.RequestParams;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;
import com.tools.json.GJson;

/**
 * 
 * @author lyq 修改密码
 */
public class UpdatePassUI extends AbsUI {
	private final String TAG = FindPassUI.class.getSimpleName();
	private TitleBar titleBar;
	private TextView btn_next;// 下一步
	private EditText phone, code;// 电话号码
	private TextView tv_getcode;// 获取验证码
	// 写倒计时
	private static final int MSG_COUNT_DOWN = 1; // 倒计时消息
	private static final int delayMillis = 1 * 1000; // 1秒钟发送一次消息
	private int time = 60; // 60秒内只能获取一次
	private boolean isCanGetCode = true; // 是否可以点击获取验证码
	private EditText pass;// 密码
	private EditText pass_sure;// 确认密码
	private int status = -100;

	private String telphone;
	private String passpwd;
	private String surepasspwd;
	private String Code;
	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {

			finish();
			return;
		}
		super.setContentView(R.layout.ui_findpassword);
		super.setSlideFinishEnabled(false);
		super.onCreate(arg0);
	}

	@Override
	protected void initControl() {
		titleBar = new TitleBar();
		btn_next = (TextView) findViewById(R.id.btn_next);
		phone = (EditText) findViewById(R.id.phone);
		code = (EditText) findViewById(R.id.code);
		tv_getcode = (TextView) findViewById(R.id.tv_getcode);
		pass = (EditText) findViewById(R.id.pass);
		pass_sure = (EditText) findViewById(R.id.pass_sure);
		phone.setText(SPUtils.get(context, Constants.UserName, "").toString());
		phone.setFocusable(false);
		tv_getcode.setBackgroundResource(R.drawable.shape_btn_login);
		telphone=SPUtils.get(context, Constants.UserName, "").toString();
	}

	@Override
	protected void initControlEvent() {
		btn_next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				passpwd=pass.getText().toString();
				surepasspwd=pass_sure.getText().toString();
				Code=code.getText().toString();
				if (phone.getText().toString().isEmpty()) {
					ToastL.show("请输入手机号码");
					return;
				}
				if (StringUtils.isPhoneLegal(phone.getText().toString()) == false) {
					ToastL.show("请输入正确的手机号码");
					return;
				}
				if (!telphone.equals(phone.getText().toString())) {
					ToastL.show("手机号码与验证码不匹配");
					return;
				}
				if (status == -100) {
					ToastL.show("请先获取验证码");
					return;
				}
				if (Code.isEmpty()) {
					ToastL.show("请输入验证码");
					return;
				}
				if (passpwd.isEmpty()) {
					ToastL.show("请输入密码");
					return;
				}
				if (passpwd.length() < 6 || passpwd.length() > 20) {
					ToastL.show("请输入6-20位的密码");
					return;
				}
				String username = SPUtils.get(context, Constants.Pwd, "")
						.toString();
				if (passpwd.equals(username)) {
					ToastL.show("新密码和旧密码一致");
					return;
				}
				if (surepasspwd.isEmpty()) {
					ToastL.show("请输入确认密码");
					return;
				}
				if (!passpwd
						.equals(surepasspwd)) {
					ToastL.show("两次密码输入不一致");
					return;
				}
				FindPassTask();
			}
		});
		tv_getcode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (telphone.isEmpty()) {
					ToastL.show("请输入手机号码");
					return;
				}
				if (StringUtils.isPhoneLegal(telphone) == false) {
					ToastL.show("请输入正确的手机号码");
					return;
				}
				// 发送验证码的任务
				if (isCanGetCode) {
					isCanGetCode = false;
					SendCodeTask();
				} else {
					ToastL.show("验证码已发送，请耐心等候");
					return;
				}
			}
		});
	}

	// 找回密码的任务
	private void FindPassTask() {
		// • phone 【手机号码】 code 【手机验证码】
		RequestParams param = new RequestParams();
		param.put("phone", telphone);
		param.put("code", Code);
		param.put("password",passpwd);
		AsyncHttpUtil.post(Http_Url.findPass, context, param,
				new AsyncCallBackHandler(ui, "", true, btn_next) {

					@Override
					public void myFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}

					@Override
					public void mySuccess(int arg0, Header[] arg1, String result) {
						Log.i(TAG, "result==" + result);
						RegisterBean bean = GJson.parseObject(result,
								RegisterBean.class);
						if (bean != null) {
							// 1：验证成功，其他：验证失败
							if (bean.getStatu() == 1) {
								ToastL.show(bean.getMsg());
								SPUtils.put(context, Constants.ForgetCode,
										"false");
								EventBus.getDefault().post(new MainStype(Constant.mai_Urage));
								AbsUI.startClearTopUI(context, LoginUI.class);
								AbsUI.stopUI(ui);
							} else {
								ToastL.show(bean.getMsg());
							}
						}
					}
				});
	}

	// 获取验证码的任务
	private void SendCodeTask() {
		RequestParams param = new RequestParams();
		param.put("phone", telphone);
		AsyncHttpUtil.post(Http_Url.sendFindCode, context, param,
				new AsyncCallBackHandler(ui,"",true,tv_getcode) {
			@Override
			public void myFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				setCodeClick(false);
				isCanGetCode=true;
			}

			@Override
			public void mySuccess(int arg0, Header[] arg1, String result) {
				setCodeClick(false);
				Log.i(TAG, "result==" + result);
				RegisterBean bean = GJson.parseObject(result,
						RegisterBean.class);
				if (bean == null) {
					return;
				}
				// 1：发送成功，0：该手机号码已经注册，-1：手机号码格式有误
				if (bean.getStatu() == 1) {
					status = bean.getStatu();
					mHandler.sendEmptyMessage(MSG_COUNT_DOWN);
					ToastL.show("发送成功");
				} else {
					isCanGetCode=true;
					ToastL.show(bean.getMsg());
				}
				
			}

					
				});
	}

	/**
	 * 设置“获取验证码”按钮
	 * 
	 */
	private void setCodeClick(boolean click) {
		tv_getcode.setSelected(click ? true : false);
		tv_getcode.setClickable(click ? false : true);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		titleBar.setTitle("修改密码");
		titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_getcode.getText().toString().equals("获取验证码")) {
					finish();
				} else {
					ToastL.show("验证码倒计时中....");
					return;
				}

			}
		});
	}

	@Override
	public void onBackPressed() {
		if (tv_getcode.getText().toString().equals("获取验证码")) {
			finish();
		} else {
			ToastL.show("验证码倒计时中....");
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titleBar, titleBar);
	}

	@Override
	protected void onStartLoader() {

	}

	@Override
	protected byte[] doInBackgroundLoader() {
		return new byte[0];
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			// 倒计时
			case MSG_COUNT_DOWN:
				Log.i(TAG, "time==" + time);
				if (time > 0) {
					// 60秒内倒计时
					isCanGetCode = false;
					Message msg2 = Message.obtain();
					msg2.arg1 = time--;
					msg2.what = MSG_COUNT_DOWN;
					tv_getcode.setText(time + "秒后重获");
					tv_getcode.setBackgroundResource(R.drawable.shape_btn_login_grey);
					mHandler.sendMessageDelayed(msg2, delayMillis);
				} else {
					// 60秒后还原
					time = 60;
					isCanGetCode = true;
					tv_getcode.setText("获取验证码");
					tv_getcode.setBackgroundResource(R.drawable.shape_btn_login);
					setCodeClick(false);// btn还原
				}
				break;

			default:
				break;
			}

		}

	};

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

	}
}
