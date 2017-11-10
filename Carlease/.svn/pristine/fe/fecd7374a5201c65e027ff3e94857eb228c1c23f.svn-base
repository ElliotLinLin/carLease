package com.hst.Carlease.wxapi;

import org.greenrobot.eventbus.EventBus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.eventBean.Stype;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "WXPayEntryActivity";

	private IWXAPI api;

	private TextView mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.pay_result);
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, Constants.APPID);
		api.handleIntent(getIntent(), this);

		mTitle = (TextView) findViewById(R.id.tv_title);
		System.out.println("onCreate...........");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);

		System.out.println("onNewIntent.........");
	}

	@Override
	public void onReq(BaseReq req) {
		System.out.println("onReq.........");

	}

	@Override
	public void onResp(final BaseResp resp) {
		System.out.print("onPayFinish, errCode:" + resp.errCode + "---errStr:" + resp.errStr + resp.transaction);
		String msg="";
		switch (resp.errCode) {
		case 0:// 成功
			msg="支付成功";
			EventBus.getDefault().post(new Stype(10));
			break;
		case -1:// 失败
			msg="支付失败";
			
			break;
		case -2:// 取消
			msg="支付取消";
			break;
		default:
			break;
		}
		ToastL.show(msg);
//		ToastL.show(resp.errStr);
//		ToastL.show(resp.transaction);
		
		finish();
	}
}