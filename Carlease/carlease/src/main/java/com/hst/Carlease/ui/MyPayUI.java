package com.hst.Carlease.ui;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.eventBean.Stype;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.GetOrderListBean;
import com.hst.Carlease.http.bean.MypayBean;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.util.Log;
import com.tools.widget.Prompt;

/**
 * 我的账单
 * 
 * @author lyq
 * 
 */
public class MyPayUI extends AbsUI2 {
	private final String TAG = MyPayUI.class.getSimpleName();
	private TitleBar titlebar;
	private List<MypayBean.PayBean> PayBeanlist;// 列表
	private final int GET_DATA = 0;
	private final int SET_DATA = 1;
	private final int SET_CHILD_DATA = 2;
	private ExpandableListView expListViewHistoryOrder;
	private TextView tv_nodata;
	private com.hst.Carlease.adapter.MypayAdapter MypayAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {

			finish();
			return;
		}
		setContentView(R.layout.ui_mypay);
		EventBus.getDefault().register(this);
		super.setSlideFinishEnabled(false);
		super.onCreate(arg0);

	}

	@Override
	protected void initControl() {
		titlebar = new TitleBar();
		expListViewHistoryOrder = (ExpandableListView) findViewById(R.id.expListViewHistoryOrder);
		tv_nodata = (TextView) findViewById(R.id.tv_nodata);
		handler.sendEmptyMessage(GET_DATA);
	}

	@Override
	protected void initControlEvent() {
		expListViewHistoryOrder
				.setOnGroupClickListener(new OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						return true;
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DATA:// 获取数据
				GetMyBillList();
				break;
			case SET_DATA:
				setAdapter();
				break;
			case SET_CHILD_DATA:
				break;
			default:
				break;
			}
		};
	};

	private void setAdapter() {
		MypayAdapter = new com.hst.Carlease.adapter.MypayAdapter(context,
				PayBeanlist, PayBeanlist.get(0).getBillItem());
		expListViewHistoryOrder.setGroupIndicator(null);// 删除左边图标
		expListViewHistoryOrder.setCacheColorHint(0);
		expListViewHistoryOrder.setDivider(null); // 删除分割线
		expListViewHistoryOrder.setAdapter(MypayAdapter);
		for (int i = 0; i < PayBeanlist.size(); i++) {
			expListViewHistoryOrder.expandGroup(i);
		}
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
			AsyncHttpUtil.post(ui, Http_Url.GetPaymentList, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							expListViewHistoryOrder) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
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
								MypayBean beans = GJson.parseObject(
										bean.getD(), MypayBean.class);
								if (beans != null) {
									if (beans.getStatu() == 1) {
										// 成功
										if (beans.getModel() != null
												&& beans.getModel().size() != 0) {
											PayBeanlist = beans.getModel();
											// 数据为空
											tv_nodata.setVisibility(View.GONE);
											expListViewHistoryOrder
													.setVisibility(View.VISIBLE);
											handler.sendEmptyMessage(SET_DATA);
										} else {
											// 数据为空
											tv_nodata
													.setVisibility(View.VISIBLE);
											expListViewHistoryOrder
													.setVisibility(View.GONE);
										}

									} else if (beans.getStatu() == -1) {
										ToastL.show(beans.getMsg());
										StringUtils.IsOUTOFtime(context,
												MyPayUI.this.ui);
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
	protected void initMember() {
		super.addFgm(R.id.titleBar_myorder, titlebar);
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Subscribe
	public void onEventMainThread(Stype ev) {
		if (ev.getMsg() == Constant.ORDER_PAY3) {
			handler.sendEmptyMessage(GET_DATA);
		}
	}

	@Override
	public void onAttachedToWindow() {
		titlebar.setTitle("我的账单");
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
