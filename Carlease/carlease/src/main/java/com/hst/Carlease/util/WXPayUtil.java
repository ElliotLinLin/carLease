//package com.hst.Carlease.util;
//
//import org.apache.http.Header;
//
//import android.support.v4.app.FragmentActivity;
//import android.util.Log;
//import android.view.View;
//
//import com.hst.Carlease.asynchttp.AsyncHttpUtil;
//import com.hst.Carlease.widget.mywidget.ToastL;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.tencent.mm.sdk.modelbase.BaseResp;
//import com.tencent.mm.sdk.modelpay.PayReq;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tools.json.GJson;
////import com.wisdom.LuJLPv1.http.HttpCallback;
////import com.wisdom.LuJLPv1.http.HttpHelper;
////import com.wisdom.LuJLPv1.http.bean.WXPay2Bean;
////import com.wisdom.LuJLPv1.http.bean.WXPay2Bean.WXPayInfo;
////import com.wisdom.LuJLPv1.operate.UserOperate;
////import com.wisdom.LuJLPv1.ram.Http_Url;
//
///**
// * @author Ljj
// *
// * 描述：微信支付工具
// * 功能/说明：
// * 要注意的地方：
//1.确实有如官方说的，签名问题，确保生成的签名和填写在微信中的签名一致。
//2.注意配置文件，用第二种微信支付。
//3.注意三个必要参数的数据类型，特别是支付金额。
//4.错误本不在微信支付，而是自己的服务器端返回值的问题
//5.修改签名后的延时性，亲测，快的话估计5分钟生效，慢的话……你懂的
//6.模拟器和手机缓存，光卸载没用，必须用360这些工具清理下
// */
//public class WXPayUtil {
//	private WXPayUtil() {
//	};
//
//	private static IWXAPI msgApi = null;
//	private static WXPayUtil instance;
//	BaseResp resp;
//
//	public static WXPayUtil getInstance(FragmentActivity ui) {
//		if (instance == null) {
//			instance = new WXPayUtil();
//			instance.regist(ui);
//		}
//		return instance;
//	}
//
//	public void regist(FragmentActivity ui) {
//		if (msgApi == null) {
//			msgApi = WXAPIFactory.createWXAPI(ui, Constant_xxx.WX_APPID, false);
////			 将该app注册到微信
//			boolean registerApp = msgApi.registerApp(Constant_xxx.WX_APPID);
//			System.out.println("registerApp:" + registerApp + "-AppID-" + Constant_xxx.WX_APPID);
//		}
//	}
//
//	/**
//	 * @param orderID订单
//	 * @param price 价格
//	 * @param body 商品描述
//	 */
//	public void getPayParamers(String orderID, String price, String body, FragmentActivity ui, View view) {
//		// String url = HTTPURL.getGetWeiXinOrder();
//		String url = Http_Url.GetWeiXinParams;
//		AjaxParams params = new AjaxParams();
//		params.put("price", price);
//		params.put("body", body);
//		params.put("orderID", orderID);
//		AsyncHttpUtil.get("", params, new AsyncHttpResponseHandler() {
//			
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
//				Log.i("WXPayUtil", arg1);
////				WXPay2Bean payInfo = GJson.parseObject(t, WXPay2Bean.class);
////				if(payInfo!=null && payInfo.getPageData()!=null){
////					WXPayInfo pageData = payInfo.getPageData();
////					pay(pageData);
////					return;
////				}
//			}
//			
//			@Override
//			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//				
//			}
//		});
//
//	}
//
////	public void pay(WXPayInfo info) {
////		if (msgApi.isWXAppInstalled()) {
////			System.out.println("执行微信支付");
////			// wx907e93848e15da25
////			PayReq request = new PayReq();
////
//////			request.appId = Constant_xxx.WX_APPID;// 应用ID
////			request.partnerId = info.getPartnerid();// 商户号
////			request.prepayId = info.getPrepayid();// 预支付交易回话
////
////			request.packageValue = "Sign=WXPay";
////			request.nonceStr = info.getNoncestr();// 随机字符串
////			request.timeStamp = info.getTimestamp();// 时间戳
////			request.sign = info.getSign();// 签名
////			request.extData = "app data"; // optional
////
////			boolean checkArgs = request.checkArgs();
////			ToastL.show("开启微信中...");
////
////			System.out.println(info);
////			boolean sendReq = msgApi.sendReq(request);
////			System.out.println("sendReq:" + sendReq + "\n request:" + request + "\n checkArgs:" + checkArgs);
////		} else {
////			ToastL.show("您还没有安装微信，请先安装微信再支付!");
////		}
////	}
//}
