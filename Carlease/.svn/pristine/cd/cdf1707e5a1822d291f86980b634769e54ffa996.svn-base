package com.hst.Carlease.task;

import org.apache.http.Header;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.http.bean.LoginBean;
import com.hst.Carlease.operate.PushServiceOperate;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.LoginUI;
import com.hst.Carlease.ui.OldUserBingUI;
import com.hst.Carlease.ui.fragement.MainUI2;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.widget.mywidget.PopRegister;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.json.GJson;
import com.tools.net.http.HttpTool;

public class LoginTask {
	private static final String TAG = LoginTask.class.getSimpleName();
	static FragmentActivity ui;
	static Context Context;

	public LoginTask(FragmentActivity ui) {
		this.ui = ui;
	}

	public LoginTask(Context Context) {
		this.Context = Context;
	}

	public static void Login(Context context, final String phone,
			final String pass, String devicesNO, String cityname,final boolean remember) {
		RequestParams param = new RequestParams();
		// • phone 【手机号码】 password 【登陆密码】
		param.put("phone", phone);
		param.put("password", pass);
		param.put("deviceNo", devicesNO);
		param.put("LoginAddr", cityname);
		param.put("client", "1");// 客户端标识 1安卓 2IOS

		AsyncHttpUtil.post(Http_Url.userLogin, ui, param,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
						String result = HttpTool.bytes2String(arg2);
						Log.i(TAG, "result==" + result);
						LoginBean bean = GJson.parseObject(result,
								LoginBean.class);
						SPUtils.put(ui, Constants.UserName, phone);
						if (bean == null) {
							return;
						}
						// 1：登陆成功，-1：登陆失败
						if (bean.getStatu() == 1) {
							SPUtils.put(ui, Constants.ForgetCode, remember + "");
							SPUtils.put(ui, Constants.Pwd, pass);
							SPUtils.put(ui, Constants.tokenID, bean.getModel()
									.getGuid());
							if (bean.getModel().getIsHire() == 1) {
								SPUtils.put(ui, Constants.hasOrder, true);
							}else {
								SPUtils.put(ui, Constants.hasOrder, false);
							}
							PushServiceOperate.tryStart(ui);
							AbsUI.startClearTopUI(ui, MainUI2.class);
							AbsUI.stopUI(ui);
						}else {
							ToastL.show(bean.getMsg());
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						AbsUI.startUI(ui, LoginUI.class);
						AbsUI.stopUI(ui);
					}
				});
	}
	

}
