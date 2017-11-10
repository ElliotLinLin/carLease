package com.hst.Carlease.ui;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.graphics.Color;
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
import com.hst.Carlease.http.bean.HeTongBean;
import com.hst.Carlease.http.bean.HeTongBean.HeTong;
import com.hst.Carlease.http.bean.TokenIDBean;
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

public class PayHeTongUI extends AbsUI2 {
	private final String TAG = PayHeTongUI.class.getSimpleName();
	private TitleBar titlebar;
	private ListView lv_myorder;// 我的订单的列表
	private TextView tv_myorder_nodata;// 没有数据
	private LinearLayout linea_sum;//
	private List<HeTong> HeTonglist;
	private CommonAdapter<HeTong> adapter;
	private Intent intent;

	@Override
	protected void onCreate(Bundle arg0) {
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
		tv_myorder_nodata.setText("没有数据");
		GetFeeType();
	}

	@Override
	protected void initControlEvent() {
		lv_myorder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				intent = getIntent();
				HeTong feetype = (HeTong) parent.getAdapter().getItem(position);
				intent.putExtra("hetong", feetype.getSerialNumber());
				intent.putExtra("UI", "PayHeTongUI");
				intent.putExtra("ContractID", feetype.getContractID());
				Log.i(TAG, "feetype.getSerialNumber()--"+feetype.getSerialNumber());
				AbsUI.startClearTopUI(context, PayUI.class, intent);
				finish();
			}
		});
	}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titleBar_myorder, titlebar);
	}

	/**
	 * 我的违章列表
	 */
	protected void GetFeeType() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		TokenIDBean bean = new TokenIDBean();
		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());
		try {
			AsyncHttpUtil.post(ui, Http_Url.GetFeeType, bean, "application/json",
					new AsyncCallBackHandler(ui, "", true, tv_myorder_nodata) {

						@Override
						public void myFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
							Log.e(TAG, "Http---error==" + arg0 + "---" + arg3);
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1, String arg2) {
							Log.i(TAG, "result==="+arg2);
							if (arg2==null) {
								return;
							}
							Bean bean = GJson.parseObject(arg2, Bean.class);
							Log.i(TAG, "result 截取" + bean.getD());
							if (bean.getD() != null) {
								HeTongBean HeTongBean = GJson.parseObject(bean.getD(), HeTongBean.class);
								if (HeTongBean.getStatu() == 1) {
									// 给适配器赋值
									HeTonglist = HeTongBean.getModel();
									if (HeTonglist != null) {
										adapter = new CommonAdapter<HeTongBean.HeTong>(context, HeTonglist, R.layout.text_item) {

											@Override
											public void fillItemData(CommonViewHolder holder, HeTong data, int position) {
												TextView name = holder.getView(R.id.tv_pop);//车牌号
												TextView carname = holder.getView(R.id.tv_carnum);//合同编号
												name.setText("车牌号："+data.getPlateNumber());
												carname.setText("合同编号："+data.getSerialNumber());
												carname.setVisibility(View.VISIBLE);
												name.setTextColor(Color.BLACK);
												carname.setTextColor(Color.BLACK);
											}
										};

										tv_myorder_nodata.setVisibility(View.INVISIBLE);
										lv_myorder.setVisibility(View.VISIBLE);
										lv_myorder.setAdapter(adapter);
									} else {

										tv_myorder_nodata.setVisibility(View.VISIBLE);
										lv_myorder.setVisibility(View.GONE);

									}
								} else if (HeTongBean.getStatu() == 2) {
									ToastL.show(HeTongBean.getMsg());
									StringUtils.IsOUTOFtime(context, PayHeTongUI.this.ui);
								} else {
									ToastL.show(HeTongBean.getMsg());
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
		titlebar.setTitle("付款合同");
		titlebar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
