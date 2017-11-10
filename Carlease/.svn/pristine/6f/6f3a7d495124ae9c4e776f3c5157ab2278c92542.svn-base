package com.hst.Carlease.ui;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.GetOrderListBean;
import com.hst.Carlease.http.bean.OrderDetailsBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.util.Log;
import com.tools.widget.Prompt;

/**
 * 我的订单详情
 * 
 * @author lyq
 * 
 */
public class MyOrderDetailsUI extends AbsUI {
	private final String TAG = MyOrderDetailsUI.class.getSimpleName();

	private TitleBar titleBar;
	private TextView order_status, order_num, order_time, name, username,
			rent_money, month_money, last_pay,price,pailiang;// 订单状态,订单号,下单时间，车辆品牌
	private ImageView iamge;// 图片
	private Intent intent;//

	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {  
	        
	        finish();  
	        return;  
	    }  
		setContentView(R.layout.ui_orderdetails);
		super.setSlideFinishEnabled(false);
		super.onCreate(arg0);
	}

	@Override
	protected void initControl() {
		titleBar = new TitleBar();
		intent = getIntent();
		order_status = (TextView) findViewById(R.id.order_status);
		order_num = (TextView) findViewById(R.id.order_num);
		order_time = (TextView) findViewById(R.id.order_time);
		name = (TextView) findViewById(R.id.name);
		iamge = (ImageView) findViewById(R.id.iamge);
		username = (TextView) findViewById(R.id.username);
		rent_money = (TextView) findViewById(R.id.rent_money);
		month_money = (TextView) findViewById(R.id.month_money);
		last_pay = (TextView) findViewById(R.id.last_pay);
		pailiang=(TextView) findViewById(R.id.pailiang);
		price=(TextView) findViewById(R.id.price);
		GetOrderDetail();
	}

	@Override
	protected void initControlEvent() {

	}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titleBar_orderdetails, titleBar);
	}

	@Override
	protected void onStartLoader() {

	}

	/**
	 * 我的违章列表
	 */
	protected void GetOrderDetail() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		GetOrderListBean bean = new GetOrderListBean();
		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());
		bean.setHpid(intent.getIntExtra("ID", 1));
		try {
			AsyncHttpUtil.post(ui, Http_Url.GetOrderDetail, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,order_status) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Bean bean = GJson.parseObject(arg2, Bean.class);
							Log.i(TAG, "result " + bean.getD());
							if (bean.getD() != null) {
								OrderDetailsBean beans = GJson.parseObject(
										bean.getD(), OrderDetailsBean.class);
								if (beans!=null) {
									if (beans.getStatu() == 1) {
										if (beans.getModel() == null) {
											return;
										}
										order_status
												.setText(beans.getModel()
																.getOrderStatus());
										order_num.setText("订单编号："
												+ beans.getModel().getOrderNo()
												+ "");
										order_time.setText("下单时间："
												+ beans.getModel().getOrderDate());
										name.setText( beans.getModel().getBrandName());
										username.setText("车型："
												+ beans.getModel().getCarModelName().trim());
										rent_money.setText(Html
												.fromHtml("<font color=\"#FFA90C\"> "
														+ beans.getModel().getFrtPay()+ "</font>元"));
										month_money.setText(Html
												.fromHtml("<font color=\"#FFA90C\"> "
												+ beans.getModel().getMthRent()+ "</font>元"));
										last_pay.setText(Html
												.fromHtml("<font color=\"#FFA90C\"> "
												+ beans.getModel().getLastPay()+ "</font>元"));
										price.setText("指导价："+beans.getModel().getMarketPrice()+"元");
										pailiang.setText("座位数/排量："+beans.getModel().getSeatCount()+"人/"+beans.getModel().getDisplacement()+"L");
										if (beans.getModel().getCarModelUrl() != null
												&& beans.getModel()
														.getCarModelUrl().size() != 0) {
											ImageLoader.getInstance().displayImage(
													beans.getModel()
															.getCarModelUrl()
															.get(0), iamge);
										}else {
											ImageLoader.getInstance().displayImage(
													"", iamge);
										}

										if (beans.getModel().getOrderStatus()
												.equals("预订")
												|| beans.getModel()
														.getOrderStatus()
														.equals("待审批")) {
										} else if (beans.getModel()
												.getOrderStatus().equals("审批通过")) {
										} else {
											
										}

									} else if (beans.getStatu() == -2) {
										ToastL.show(beans.getMsg());
										StringUtils.IsOUTOFtime(context,
												MyOrderDetailsUI.this.ui);
									} else {
										ToastL.show(beans.getMsg());
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
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		titleBar.setTitle("订单详情");
		titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected byte[] doInBackgroundLoader() {
		return null;
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

	}

}
