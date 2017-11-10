package com.hst.Carlease.ui;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.adapter.CommonAdapter;
import com.hst.Carlease.adapter.CommonViewHolder;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.GetOrderListBean;
import com.hst.Carlease.http.bean.RentCarsBeans;
import com.hst.Carlease.http.bean.RentCarsBeans.RentCars;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.util.Log;
import com.tools.widget.Prompt;

/**
 * 
 * @author lyq 已租车辆
 */

public class RentCarsUI extends AbsUI2 {
	private final String TAG = MyOrderUI.class.getSimpleName();
	private TitleBar titlebar;
	private ListView lv_myorder;// 我的订单的列表
	private TextView tv_myorder_nodata;// 没有数据
	private LinearLayout linea_sum;//
	private CommonAdapter<RentCars> adapter;
	private List<RentCars> list;

	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {

			finish();
			return;
		}
		setContentView(R.layout.ui_myorder);
		setSlideFinishEnabled(false);
		super.onCreate(arg0);
	}

	@Override
	protected void initControl() {
		titlebar = new TitleBar();
		lv_myorder = (ListView) findViewById(R.id.lv_myorder);
		linea_sum = (LinearLayout) findViewById(R.id.linea_sum);
		linea_sum.setVisibility(View.GONE);
		tv_myorder_nodata = (TextView) findViewById(R.id.tv_myorder_nodata);
		tv_myorder_nodata.setText("没有合同");
		GetMyBillList();
	}

	@Override
	protected void initControlEvent() {
		lv_myorder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				RentCars RentCars=(com.hst.Carlease.http.bean.RentCarsBeans.RentCars) arg0.getAdapter().getItem(arg2);
			Intent intent=new Intent();
			intent.putExtra("id", RentCars.getContractID());
			AbsUI.startUI(context, RentCarWebviewUI.class, intent);
			}
		});
	}

	/**
	 * 我的帐单
	 */
	protected void GetMyBillList() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		GetOrderListBean bean = new GetOrderListBean();
		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());

		try {
			AsyncHttpUtil.post(ui, Http_Url.GetContractList, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							lv_myorder) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
							lv_myorder.setVisibility(View.GONE);
							tv_myorder_nodata.setVisibility(View.VISIBLE);
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Bean bean = GJson.parseObject(arg2, Bean.class);
							Log.i(TAG, "result 截取" + bean.getD());
							if (bean.getD().equals("") == true) {
								return;
							}
							if (bean.getD() != null) {
								RentCarsBeans RentCarsBeans = GJson
										.parseObject(bean.getD(),
												RentCarsBeans.class);
								if (RentCarsBeans != null) {
									if (RentCarsBeans.getStatu() == 1) {
										list = RentCarsBeans.getModel();
										if (list.size() == 0) {
											lv_myorder.setVisibility(View.GONE);
											tv_myorder_nodata
													.setVisibility(View.VISIBLE);
											return;
										}
										adapter = new CommonAdapter<RentCars>(
												context, list,
												R.layout.ui_rentcars_item) {

											@Override
											public void fillItemData(
													CommonViewHolder holder,
													RentCars data, int position) {
												TextView NO = holder
														.getView(R.id.meliges);
												TextView CarNO = holder
														.getView(R.id.melige);
												TextView firs_pay = holder
														.getView(R.id.line_people);
												TextView month_pay = holder
														.getView(R.id.pailiang);
												NO.setText("合同编号："
														+ data.getSerialNumber()
														+ "");
												CarNO.setText(data
														.getPlateNumber() + "");
												firs_pay.setText(data
														.getFrtPay() + "元");
												month_pay.setText(data
														.getMonthRent() + "元");
											}
										};
										lv_myorder.setVisibility(View.VISIBLE);
										tv_myorder_nodata
												.setVisibility(View.GONE);
										lv_myorder.setAdapter(adapter);
									} else if (RentCarsBeans.getStatu() == -2) {
										ToastL.show(RentCarsBeans.getMsg());
										StringUtils.IsOUTOFtime(context,
												RentCarsUI.this.ui);
									} else {
										ToastL.show("无数据");
										lv_myorder.setVisibility(View.GONE);
										tv_myorder_nodata
												.setVisibility(View.VISIBLE);
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
		super.addFgm(R.id.titleBar_myorder, titlebar);
	}

	@Override
	protected void onStartLoader() {

	}

	@Override
	public void onAttachedToWindow() {
		titlebar.setTitle("租赁合同");
		titlebar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		super.onAttachedToWindow();
	}

	@Override
	protected byte[] doInBackgroundLoader() {
		return null;
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

	}

}
