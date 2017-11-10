package com.hst.Carlease.ui;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.adapter.CommonAdapter;
import com.hst.Carlease.adapter.CommonViewHolder;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.eventBean.Stype;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.GetOrderListBean;
import com.hst.Carlease.http.bean.MyVolitionBean;
import com.hst.Carlease.http.bean.MyVolitionBean.MyVolition.VehiIlegal;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.widget.Prompt;

/**
 * 我的违章
 * 
 * @author lyq
 * 
 */
public class MyViolationUI extends AbsUI2 {
	private final String TAG = MyViolationUI.class.getSimpleName();

	private TitleBar titlebar;
	private ListView lv_myorder;
	private TextView tv_myorder_nodata;
	private CommonAdapter<VehiIlegal> adapter;
	private List<VehiIlegal> list;
	private TextView volition_num, volition_mon, volition_score;
	private TextView btn_next;// 刷新
	private final int GET_DATA = 0;
	private final int SET_DATA = 1;

	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {

			finish();
			return;
		}
		setContentView(R.layout.ui_volition);
		EventBus.getDefault().register(this);
		super.setSlideFinishEnabled(false);
		super.onCreate(arg0);

	}

	@Override
	protected void initControl() {
		titlebar = new TitleBar();
		lv_myorder = (ListView) findViewById(R.id.lv_myorder);
		tv_myorder_nodata = (TextView) findViewById(R.id.tv_myorder_nodata);
		volition_num = (TextView) findViewById(R.id.volition_num);
		volition_mon = (TextView) findViewById(R.id.volition_mon);
		volition_score = (TextView) findViewById(R.id.volition_score);
		btn_next = (TextView) findViewById(R.id.btn_next);
		tv_myorder_nodata.setText("没有违章记录");
		hander.sendEmptyMessage(GET_DATA);

	}

	@Override
	protected void initControlEvent() {
		btn_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				hander.sendEmptyMessage(GET_DATA);
			}
		});
	}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titleBar_myorder, titlebar);

	}

	Handler hander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DATA:
				GetVehicleIllegalList();
				break;
			case SET_DATA:
				setAdapter();
				break;
			default:
				break;
			}
		};
	};

	@Subscribe
	public void onEventMainThread(Stype ev) {
		if (ev.getMsg() == Constant.ORDER_PAY4) {
			hander.sendEmptyMessage(GET_DATA);
		}
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	/**
	 * 我的违章列表
	 */
	protected void GetVehicleIllegalList() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			volition_num.setText("");
			volition_mon.setText("");
			volition_score.setText("");
			return;
		}
		GetOrderListBean bean = new GetOrderListBean();
		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());

		try {
			AsyncHttpUtil.post(ui, Http_Url.GetVehicleIllegalList, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							lv_myorder) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
							tv_myorder_nodata.setVisibility(View.VISIBLE);
							lv_myorder.setVisibility(View.GONE);
							btn_next.setVisibility(View.GONE);
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Bean bean = GJson.parseObject(arg2, Bean.class);
							if (bean.getD() != null) {
								MyVolitionBean beans = GJson.parseObject(
										bean.getD(), MyVolitionBean.class);
								if (beans.getStatu() == 1) {
									if (beans.getModel() == null) {
										tv_myorder_nodata
												.setVisibility(View.VISIBLE);
										lv_myorder.setVisibility(View.GONE);
										btn_next.setVisibility(View.GONE);
										return;
									}
									list = beans.getModel().getVehiIlegal();
									if (list.size() != 0) {
										volition_num.setText(list.size() + "条");
										volition_mon.setText(beans.getModel()
												.getTotalMoney()+"元");
										volition_score.setText(beans.getModel()
												.getTotalScore()+"分");
										hander.sendEmptyMessage(SET_DATA);
									}
								} else if (beans.getStatu() == -2) {
									ToastL.show(beans.getMsg());
									StringUtils.IsOUTOFtime(context,
											MyViolationUI.this.ui);
								} else {
									ToastL.show(beans.getMsg());
								}
							}

						}

					});

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

	}

	private void setAdapter() {
		adapter = new CommonAdapter<VehiIlegal>(ui, list,
				R.layout.item_myviolation) {

			@Override
			public void fillItemData(CommonViewHolder holder,
					final VehiIlegal data, int position) {
				TextView tv_myviolation_carNO = holder
						.getView(R.id.tv_myviolation_carNO);
				TextView tv_myviolation_vtime = holder
						.getView(R.id.tv_myviolation_vtime);
				TextView tv_myviolation_vplace = holder
						.getView(R.id.tv_myviolation_vplace);
				// TextView
				// tv_myviolation_vreason =
				// holder
				// .getView(R.id.tv_myviolation_vreason);//
				// 交警支队
				TextView tv_myviolation_status = holder
						.getView(R.id.tv_myviolation_status);// 违章行为
				TextView tv_myviolation_way = holder
						.getView(R.id.tv_myviolation_way);// 违章处罚
				TextView tv_totalmoney = holder.getView(R.id.tv_totalmoney);
				TextView paystatus = holder.getView(R.id.paystatus);
				tv_myviolation_carNO.setText(data.getC_PlateNumber());
				tv_myviolation_vtime.setText(data.getVVR_crt_dte());
				tv_myviolation_vplace.setText(data.getVVR_Place());
				tv_myviolation_status.setText(data.getVVR_Reason());
				tv_myviolation_way.setText(data.getIllegalPunish());
				tv_totalmoney.setText(Html
						.fromHtml("   总罚金：<font color=\"#FFA90C\"> "
								+ data.getVVR_TotalFee() + "</font>元"));
				if (data.getVVR_IsPaid() == 0) {// 未代办
					paystatus.setText("付款代办");
					paystatus.setTextColor(Color.parseColor("#FFFFFF"));
					paystatus
							.setBackgroundResource(R.drawable.shape_order_orange);
				} else {
					paystatus.setText(data.getVVR_Status());
					paystatus.setTextColor(Color.parseColor("#FFAC0E"));
					paystatus.setBackgroundColor(Color.parseColor("#FFFFFF"));
				}
				paystatus.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (data.getVVR_IsPaid() == 0) {
							// 开启到支付界面
							Intent intent = new Intent();
							intent.putExtra("orderNo", data.getVVR_ID() + "");
							intent.putExtra("dataFrom", Constant.ORDER_PAY4);
							intent.putExtra("body", "违章费用");
							intent.putExtra("contactid", data.getC_ContractID());
							intent.putExtra("money", data.getVVR_TotalFee()
									+ "");
//							intent.putExtra("money", "0.01");
							intent.putExtra("subject", data.getVVR_Reason());
							AbsUI.startUI(context, OrderPayUI.class, intent);
						}
					}
				});
			}
		};
		lv_myorder.setAdapter(adapter);
		tv_myorder_nodata.setVisibility(View.GONE);
	}

	@Override
	public void onAttachedToWindow() {
		titlebar.setTitle("违章处理");
		titlebar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		super.onAttachedToWindow();
	}

	@Override
	protected void onStartLoader() {

	}

	@Override
	protected byte[] doInBackgroundLoader() {
		return null;
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

	}

}
