package com.hst.Carlease.pay;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

public class Alipay2 {
	// 商户PID
	private static final int SDK_PAY_FLAG = 1;
	private WeakReference<Activity> mActivity;

	private OnAlipayListener mListener;
	private FragmentActivity ui;
	private String sn;

	public Alipay2(Activity activity, FragmentActivity ui, String string) {
		mActivity = new WeakReference<Activity>(activity);
		this.ui = ui;
		this.sn = string;
	}

	public void setListener(OnAlipayListener l) {
		mListener = l;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == SDK_PAY_FLAG) {
				PayResult payResult = new PayResult((HashMap<String, String>) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				// String resultInfo = payResult.getResult();
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					if (mListener != null)
						mListener.onSuccess();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
					// 最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						if (mListener != null)
							mListener.onWait();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						if (mListener != null)
							mListener.onCancel();
					}
				}
			}
		}
	};

	/**
	 * 加载支付类型
	 * 
	 * @param token
	 */
	public void initPayType() {
		pay(sn);
	}

	/**
	 * 支付
	 * 
	 * @param title
	 *            标题 不能为空或者“”
	 * @param desc
	 *            描述 不能为空或者“”
	 * @param price
	 *            价格 不能为空或者“”
	 * @param sn
	 *            商品唯一货号 不能为空或者“”
	 * @param url
	 *            服务器回调url 不能为空或者“”
	 */
	public void pay(final String sn) {
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				Activity activity = mActivity.get();
				if (activity == null)
					return;
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				Map<String, String> result = alipay.payV2(sn, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/**
	 * 支付回调接口
	 * 
	 * @author lenovo
	 * 
	 */
	public static class OnAlipayListener {
		/**
		 * 支付成功
		 */
		public void onSuccess() {
		}

		/**
		 * 支付取消
		 */
		public void onCancel() {
		}

		/**
		 * 等待确认
		 */
		public void onWait() {
		}
	}


}
