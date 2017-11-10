package com.hst.Carlease.ui;


import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.view.View;
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
import com.hst.Carlease.http.bean.TransactionHistoryBean;
import com.hst.Carlease.http.bean.TransactionHistoryBean.TransactionHistory;
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
 * 交易记录
 * @author lyq
 *
 */
public class TransactionHistoryUI extends AbsUI2 {
	private final String TAG = TransactionHistoryUI.class.getSimpleName();
	private TitleBar titlebar;
	private ListView lv_myorder;// 我的订单的列表
	private TextView tv_myorder_nodata;// 没有数据
	private LinearLayout linea_sum;//
	private final int LIST_INFO = 1;
	private final int GET_DAT = 0;
	private final int GET_UP = 2;
	private List<TransactionHistory> list;
	private CommonAdapter<TransactionHistoryBean.TransactionHistory> adapter;

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
		hand.sendEmptyMessage(GET_DAT);
		tv_myorder_nodata.setText("未产生交易记录");
	}
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LIST_INFO:
				setAdapter();
				break;
			case GET_DAT:
				GetOrderList();
				break;
			default:
				break;
			}
		}

		
	};
	private void GetOrderList() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		GetOrderListBean bean = new GetOrderListBean();
		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());

		try {
			AsyncHttpUtil.post(ui, Http_Url.GetTransactionFlowList, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							tv_myorder_nodata) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
							// tv_myorder_nodata.setVisibility(View.VISIBLE);
							// lv_myorder.setVisibility(View.GONE);
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Bean bean = GJson.parseObject(arg2, Bean.class);
							Log.i(TAG, "result 截取" + bean.getD());
							if (bean.getD() != null) {
								TransactionHistoryBean beans = GJson.parseObject(
										bean.getD(), TransactionHistoryBean.class);
								if (beans != null) {
									if (beans.getStatu() == 1) {
										list = beans.getModel();
										if (list != null) {
											hand.sendEmptyMessage(LIST_INFO);
										} else {
											tv_myorder_nodata
													.setVisibility(View.VISIBLE);
											lv_myorder.setVisibility(View.GONE);
										}
									} else if (beans.getStatu() == -1) {
										ToastL.show(beans.getMsg());
										StringUtils.IsOUTOFtime(context,
												TransactionHistoryUI.this.ui);
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
	};
	@Override
	protected void initControlEvent() {

	}

	protected void setAdapter() {
		adapter=new CommonAdapter<TransactionHistoryBean.TransactionHistory>(context,list,R.layout.ui_transaction_item) {

			@Override
			public void fillItemData(CommonViewHolder holder,
					TransactionHistory data, int position) {
				TextView free_type=holder.getView(R.id.free_type);
				TextView free_time=holder.getView(R.id.free_time);
				TextView free=holder.getView(R.id.free);
				free_type.setText(data.getTrade_type());
				free_time.setText(data.getTrade_date());
				free.setText("-"+data.getTotal_fee()+"元");
			}
		};
		lv_myorder.setAdapter(adapter);
	}
	@Override
	protected void initMember() {
		super.addFgm(R.id.titleBar_myorder, titlebar);
	}
	@Override
	public void onAttachedToWindow() {
		titlebar.setTitle("交易记录");
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
