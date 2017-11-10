package com.weixin.paydemo;

import java.io.UnsupportedEncodingException;

import net.sourceforge.simcpux.ConstantsXX;

import org.apache.http.Header;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.WeiXinPayBean;
import com.hst.Carlease.http.bean.WeixinPayBean2;
import com.hst.Carlease.http.bean.WeixinPayBean2.WeixinPay;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.OrderPayUI;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tools.json.GJson;

public class WXPay {
	private WXPay() {
	};

	private static IWXAPI msgApi = null;
	private static WXPay instance;
	BaseResp resp;

	public static WXPay getInstance(FragmentActivity ui) {
		if (instance == null) {
			instance = new WXPay();
			instance.regist(ui);
		}
		return instance;
	}

	public void regist(FragmentActivity ui) {
		if (msgApi == null) {
			msgApi = WXAPIFactory.createWXAPI(ui, ConstantsXX.APP_ID, false);
			// 将该app注册到微信
			boolean registerApp = msgApi.registerApp(ConstantsXX.APP_ID);
			System.out.println("registerApp:" + registerApp + "-AppID-" + ConstantsXX.APP_ID);
		}
	}

	/**
	 * @param orderID订单
	 * @param price
	 *            价格
	 * @param body
	 *            商品描述
	 */
	public void getPayParamers(int orderID, String price, String body, FragmentActivity ui, View view, String subject,String orderNo,String dataFrom)
			throws UnsupportedEncodingException {
		String url = Http_Url.GenerateWBImpl;
		WeiXinPayBean bean = new WeiXinPayBean();
		bean.setBody(body);
		bean.setContract(orderID);
		bean.setIp("192.168.70.5");
		bean.setSubject(subject);
		bean.setToken(SPUtils.get(ui, Constants.tokenID, "").toString());
		bean.setTotal_fee(price);
		bean.setOrderNo(orderNo);
		bean.setDataFrom(dataFrom);
		AsyncHttpUtil.post(ui, url, bean, "application/json", new AsyncCallBackHandler(ui, true, view) {

			@Override
			public void mySuccess(int arg0, Header[] arg1, String arg2) {
				Log.i("WXPay", "result==" + arg2);
				if (arg2 == null) {
					return;
				}
				Bean bean = GJson.parseObject(arg2, Bean.class);
				if (bean != null) {
                WeixinPayBean2 weixinPayBean2=GJson.parseObject(bean.getD(), WeixinPayBean2.class);
               
                if (weixinPayBean2.getStatu()==1) {
                	 WeixinPay info=weixinPayBean2.getModel();
                	 pay(info);
				} else if(weixinPayBean2.getStatu()==-2){
					ToastL.show(weixinPayBean2.getMsg());
					StringUtils.IsOUTOFtime(ui,
							ui);
				}
                else {
					ToastL.show(weixinPayBean2.getMsg());
				}
				}

			}

			@Override
			public void myFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			}
		});
	}

	public void pay(WeixinPay payinfo) {
		if (msgApi.isWXAppInstalled()) {
			System.out.println("执行微信支付");
			PayReq request = new PayReq();
			request.appId = ConstantsXX.APP_ID;// 应用ID
			request.partnerId = payinfo.getPartnerid();// 商户号
			request.prepayId = payinfo.getPrepayid();// 预支付交易回话
			request.packageValue ="Sign=WXPay";
			request.nonceStr =payinfo.getNoncestr();// 随机字符串
			request.timeStamp = payinfo.getTimestamp();// 时间戳
			request.sign = payinfo.getSign();// 签名
			request.extData = "app data"; // optional

			boolean checkArgs = request.checkArgs();
			ToastL.show("开启微信中...");

			boolean sendReq = msgApi.sendReq(request);
			System.out.println("sendReq:" + sendReq + "\n request:" + request + "\n checkArgs:" + checkArgs);
		} else {
			ToastL.show("您还没有安装微信，请先安装微信再支付!");
		}
	}
}
