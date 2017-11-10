package com.hst.Carlease.ui;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.eventBean.Stype;
import com.hst.Carlease.http.bean.RegisterBean;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.OldRegister;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.loopj.android.http.RequestParams;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;

/**
 * Created by lyq on 2016/9/23. 老用户绑定界面
 */
public class OldUserBingUI extends AbsUI2 {
	private final String TAG = OldUserBingUI.class.getSimpleName();
	private TitleBar titleBar;
	private EditText tel_phone;// 办理业务时候的手机号
	private EditText tv_htnum;// 合同编号
	private EditText code;// 验证码
	private TextView tv_getcode;// 获取验证码
	private EditText carnum;// 车牌号
	private TextView btn_login;// 确定按钮
	private CheckBox checkbox;// 同意协议
	// 写倒计时
	private static final int MSG_COUNT_DOWN = 1; // 倒计时消息
	private static final int delayMillis = 1 * 1000; // 1秒钟发送一次消息
	private int time = 60; // 60秒内只能获取一次
	private boolean isCanGetCode = true; // 是否可以点击获取验证码
	private int status = -100;
	private TextView xieyi;

	private String telphone;
	private String Code;
	private OldRegister OldRegister;

	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {

			finish();
			return;
		}
		super.setContentView(R.layout.ui_oldusers);
		super.setSlideFinishEnabled(false);
		super.onCreate(arg0);
	}

	@Override
	protected void initControl() {
		titleBar = new TitleBar();
		tel_phone = (EditText) findViewById(R.id.tel_phone);
		tv_htnum = (EditText) findViewById(R.id.tv_htnum);
		code = (EditText) findViewById(R.id.code);
		tv_getcode = (TextView) findViewById(R.id.tv_getcode);
		carnum = (EditText) findViewById(R.id.carnum);
		btn_login = (TextView) findViewById(R.id.btn_login);
		checkbox = (CheckBox) findViewById(R.id.checkbox);
		xieyi = (TextView) findViewById(R.id.xieyi);
		tv_getcode.setBackgroundResource(R.drawable.shape_btn_login);
	}

	@Override
	protected void initControlEvent() {
		tel_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				status = -100;
			}
		});
		xieyi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AbsUI.startUI(context, WebViewUI.class);
			}
		});
		tv_getcode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断手机号码是否正确
				telphone = tel_phone.getText().toString();
				if (telphone.isEmpty()) {
					ToastL.show("请输入手机号码");
					return;
				}
				if (StringUtils.isPhoneLegal(telphone) == false) {
					ToastL.show("请输入正确的手机号码 ");
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
		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断手机号码是否正确
				Code = code.getText().toString();
				telphone = tel_phone.getText().toString();
				if (tel_phone.getText().toString().isEmpty()) {
					ToastL.show("请输入手机号码");
					return;
				}
				if (StringUtils.isPhoneLegal(tel_phone.getText().toString()) == false) {
					ToastL.show("请输入正确的手机号码 ");
					return;
				}
				if (tv_htnum.getText().toString().isEmpty()) {
					ToastL.show("请输入您的姓名");
					return;
				}
				if (carnum.getText().toString().isEmpty()) {
					ToastL.show("请输入您的身份证号码");
					return;
				}
				if (carnum.getText().toString().length() != 18) {
					ToastL.show("您的身份证号码输入有误");
					return;
				}
				if (!telphone.equals(tel_phone.getText().toString())) {
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

				if (checkbox.isChecked() == false) {
					ToastL.show("请阅读并同意协议");
					return;
				}
				// 执行绑定的任务
				BingTask();
			}
		});
	}

	private void BingTask() {
		// openid 【微信用户标识】 phone 【办理业务的时候的手机号码】 contract 【合同编号】 code 【手机验证码】
		// carnum 【车牌号码】
		// / <param name="phone">手机号码</param>
		// / <param name="cusname">客户姓名</param>
		// / <param name="cardNum">身份证号</param>
		// / <param name="code">手机验证码</param>
		// / <param name="openid">微信用户标识</param>

		RequestParams param = new RequestParams();
		param.put("openid", "");
		param.put("phone", tel_phone.getText().toString());
		param.put("cusname", tv_htnum.getText().toString());
		param.put("code", code.getText().toString());
		param.put("cardNum", carnum.getText().toString());
		AsyncHttpUtil.post(Http_Url.oldUserBindNew, context, param,
				new AsyncCallBackHandler(this, "正在绑定...", true, btn_login) {
					@Override
					public void myFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// showToast("网络异常，请重试");
					}

					@Override
					public void mySuccess(int arg0, Header[] arg1, String result) {
						Log.i(TAG, "result==" + result);
						RegisterBean bean = GJson.parseObject(result,
								RegisterBean.class);
						if (bean == null) {
							return;
						}
						// 1：绑定成功，其他：绑定失败
						if (bean.getStatu() == 1) {
							SPUtils.put(ui, Constants.UserName, telphone);
							popWindowShow(bean.getModel());
						} else {
							ToastL.show(bean.getMsg());
						}

					}
				});
	}

	// 显示弹框
	private void popWindowShow(final String pass) {
		String toast = "账号：" + tel_phone.getText().toString() + ",密码：" + pass;
		OldRegister = new OldRegister(context, btn_login, toast);
		btn_login.postDelayed(new Runnable() {

			@Override
			public void run() {
				OldRegister.show();

				// 老用户绑定
				OldRegister.setweixinPay(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						OldRegister.close();
						AbsUI.startClearTopUI(ui, LoginUI.class);
						AbsUI.stopUI(ui);
						tel_phone.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								EventBus.getDefault().post(new Stype(Constant.update_username));
							}
						}, 300);
					}
				});
			}
		}, 300);
	}

	// 获取验证码的任务
	private void SendCodeTask() {
		RequestParams param = new RequestParams();
		param.put("phone", telphone);
		AsyncHttpUtil.post(Http_Url.sendCode, context, param,
				new AsyncCallBackHandler(this, "", true, tv_getcode) {
					@Override
					public void myFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						setCodeClick(false);
						isCanGetCode = true;
					}

					@Override
					public void mySuccess(int arg0, Header[] arg1, String result) {
						Log.i(TAG, "result==" + result);
						RegisterBean bean = GJson.parseObject(result,
								RegisterBean.class);
						// 1：发送成功，0：该手机号码已经注册，-1：手机号码格式有误
						if (bean == null) {
							return;
						}
						if (bean.getStatu() == 1) {
							status = bean.getStatu();
							mHandler.sendEmptyMessage(MSG_COUNT_DOWN);
							ToastL.show("发送成功");
						} else if (bean.getStatu() == 0) {
							isCanGetCode = true;
							ToastL.show(bean.getMsg());
						} else {
							isCanGetCode = true;
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
	protected void initMember() {
		super.addFgm(R.id.titleBar, titleBar);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		titleBar.setTitle("老用户绑定");
		titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_getcode.getText().toString().equals("获取验证码") == true) {
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
		if (tv_getcode.getText().toString().equals("获取验证码") == true) {
			finish();
		} else {
			ToastL.show("验证码倒计时中....");
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onStartLoader() {

	}

	@Override
	protected byte[] doInBackgroundLoader() {
		return new byte[0];
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

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
					tv_getcode
							.setBackgroundResource(R.drawable.shape_btn_login_grey);
					mHandler.sendMessageDelayed(msg2, delayMillis);
				} else {
					// 60秒后还原
					time = 60;
					isCanGetCode = true;
					tv_getcode.setText("获取验证码");
					tv_getcode
							.setBackgroundResource(R.drawable.shape_btn_login);
					setCodeClick(false);// btn还原
				}
				break;

			default:
				break;
			}

		}

	};
}
