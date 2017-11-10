package com.hst.Carlease.ui;

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
import com.hst.Carlease.http.bean.RegisterBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.util.LocationChangedUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.mywidget.PopRegister;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.loopj.android.http.RequestParams;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;

import org.apache.http.Header;

/**
 * 注册 Created by lyq on 2016/9/23.
 */
public class RegisterUI extends AbsUI2 {
	private final String TAG = RegisterUI.class.getSimpleName();
	private TitleBar titleBar;
	private TextView btn_register;// 注册
	private EditText tel_phone, pass, code;// 手机号，密码，验证码
	private TextView tv_code;// 获取验证码
	// 写倒计时
	private static final int MSG_COUNT_DOWN = 1; // 倒计时消息
	private static final int delayMillis = 1 * 1000; // 1秒钟发送一次消息
	private int time = 60; // 60秒内只能获取一次
	private boolean isCanGetCode = true; // 是否可以点击获取验证码
	private CheckBox checked;// 同意用户协议
	private PopRegister popwindow;// 注册界面的 popw
	private EditText sure_pass;// 确认密码
	private TextView old_userbind;// 老用户绑定
	private int status = -100;// -100
	private TextView xieyi;
	private LocationChangedUtils utils;
	private String telphone;
	private String passpwd;
	private String surepasspwd;
	private String Code;
	private CheckBox no;
	private CheckBox yes;

	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;
		}
		super.setContentView(R.layout.ui_register);
		super.setSlideFinishEnabled(false);
		super.onCreate(arg0);
	}

	@Override
	protected void initControl() {
		titleBar = new TitleBar();
		utils = new LocationChangedUtils(context, ui);
		btn_register = (TextView) findViewById(R.id.btn_register);
		tel_phone = (EditText) findViewById(R.id.tel_phone);
		pass = (EditText) findViewById(R.id.pass);
		code = (EditText) findViewById(R.id.code);
		tv_code = (TextView) findViewById(R.id.tv_code);
		checked = (CheckBox) findViewById(R.id.checked);
		sure_pass = (EditText) findViewById(R.id.sure_pass);
		old_userbind = (TextView) findViewById(R.id.old_userbind);
		xieyi = (TextView) findViewById(R.id.xieyi);
		tv_code.setBackgroundResource(R.drawable.shape_btn_login);
		no = (CheckBox) findViewById(R.id.no);
		yes = (CheckBox) findViewById(R.id.yes);
	}

	@Override
	protected void initControlEvent() {
		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				yes.setChecked(true);
				no.setChecked(false);
			}
		});
		no.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				no.setChecked(true);
				yes.setChecked(false);
			}
		});
		// yes.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// yes.setChecked(true);
		// no.setChecked(false);
		// }
		// });
		// no.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean
		// isChecked) {
		// no.setChecked(true);
		// yes.setChecked(false);
		// }
		// });
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
		old_userbind.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AbsUI.startUI(context, OldUserBingUI.class);
			}
		});
		btn_register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				passpwd = pass.getText().toString();
				surepasspwd = sure_pass.getText().toString();
				Code = code.getText().toString();
				telphone = tel_phone.getText().toString();
				if (tel_phone.getText().toString().isEmpty()) {
					ToastL.show("请输入手机号码");
					return;
				}
				if (StringUtils.isPhoneLegal(tel_phone.getText().toString()) == false) {
					ToastL.show("请输入正确的手机号码");
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
				if (passpwd.isEmpty()) {
					ToastL.show("请输入密码");
					return;
				}
				if (passpwd.length() < 6 || passpwd.length() > 20) {
					ToastL.show("请输入6-20位的密码");
					return;
				}
				if (StringUtils.isContainChinese((passpwd))) {
					ToastL.show("密码不能包含汉字");
					return;
				}
				if (surepasspwd.isEmpty()) {
					ToastL.show("请输入确认密码");
					return;
				}
				if (!passpwd.equals(surepasspwd)) {
					ToastL.show("两次密码输入不一致");
					return;
				}

				if (checked.isChecked() == false) {
					ToastL.show("请阅读并同意协议");
					return;
				}
				registerTask();
				// popWindowShow();
			}
		});
		tv_code.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				telphone = tel_phone.getText().toString();
				if (telphone.isEmpty()) {
					ToastL.show("请输入手机号码");
					return;
				}
				if (telphone.length() != 11) {
					ToastL.show("请输入正确的手机号码");
					return;
				}
				if (StringUtils.isPhoneLegal(telphone) == false) {
					ToastL.show("请输入正确的手机号码");
					return;
				}
				// 获取验证码的任务
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

	// 注册的任务
	private void registerTask() {
		RequestParams param = new RequestParams();
		param.put("phone", telphone);
		param.put("openid", "");
		param.put("password", passpwd);
		param.put("code", Code);
		param.put("registAddr", utils.cityname);
		if (no.isChecked()) {
			param.put("IsAgent", 0);
		} else {
			param.put("IsAgent", 1);
		}
		AsyncHttpUtil.post(Http_Url.registerNew, context, param,
				new AsyncCallBackHandler(this, "", true, btn_register) {

					@Override
					public void myFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						setCodeClick(false);
					}

					@Override
					public void mySuccess(int arg0, Header[] arg1, String arg2) {

						Log.i(TAG, "result==" + arg2);
						RegisterBean bean = GJson.parseObject(arg2,
								RegisterBean.class);
						if (bean == null) {
							return;
						}
						// 1：注册成功，其他：注册失败
						if (bean.getStatu() == 1) {
							// 弹出来选择界面
							ToastL.show(bean.getMsg());
							AbsUI.startClearTopUI(context, LoginUI.class);
							finish();
						} else if (bean.getStatu() == 0) {
							popWindowShow();
						} else {
							ToastL.show(bean.getMsg());
						}

					}
				});

	}

	// 显示弹框
	private void popWindowShow() {
		popwindow = new PopRegister(context, btn_register,"请进行老用户绑定","关闭");
		btn_register.postDelayed(new Runnable() {

			@Override
			public void run() {
				popwindow.show();
				// 老用户绑定
				popwindow.setalipay(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						popwindow.close();
					}
				});
				popwindow.setweixinPay(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						popwindow.close();
						AbsUI2.startUI(context, OldUserBingUI.class);
					}
				});
			}
		}, 300);
	}

	// 获取验证码的任务
	private void SendCodeTask() {
		RequestParams param = new RequestParams();
		param.put("phone", tel_phone.getText().toString());
		AsyncHttpUtil.post(Http_Url.sendCode, context, param,
				new AsyncCallBackHandler(this, "", true, tv_code) {

					@Override
					public void myFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						setCodeClick(false);
						isCanGetCode = true;
					}

					@Override
					public void mySuccess(int arg0, Header[] arg1, String arg2) {
						setCodeClick(false);
						Log.i(TAG, "result==" + arg2);
						RegisterBean bean = GJson.parseObject(arg2,
								RegisterBean.class);
						// 1：发送成功，0：该手机号码已经注册，-1：手机号码格式有误
						if (bean == null) {
							return;
						}
						if (bean.getStatu() == 1) {
							status = bean.getStatu();
							mHandler.sendEmptyMessage(MSG_COUNT_DOWN);
							ToastL.show("发送成功");            
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
		tv_code.setSelected(click ? true : false);
		tv_code.setClickable(click ? false : true);
	}

	@Override
	public void onResume() {
		utils.startOnce();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		utils.stop();
		super.onDestroy();
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		titleBar.setTitle("新用户注册");
		titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tv_code.getText().toString().equals("获取验证码")) {
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
		if (tv_code.getText().toString().equals("获取验证码")) {
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
					tv_code.setText(time + "秒后重获");
					tv_code.setBackgroundResource(R.drawable.shape_btn_login_grey);
					mHandler.sendMessageDelayed(msg2, delayMillis);
				} else {
					// 60秒后还原
					time = 60;
					isCanGetCode = true;
					tv_code.setText("获取验证码");
					tv_code.setBackgroundResource(R.drawable.shape_btn_login);
					setCodeClick(false);// btn还原
				}
				break;

			default:
				break;
			}

		}

	};
}
