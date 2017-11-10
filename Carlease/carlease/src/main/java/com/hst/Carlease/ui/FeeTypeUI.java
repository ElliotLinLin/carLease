package com.hst.Carlease.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.adapter.CommonAdapter;
import com.hst.Carlease.adapter.CommonViewHolder;
import com.hst.Carlease.asynchttp.BaseCallBack;
import com.hst.Carlease.http.bean.FreeTypeBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.thirdperiodui.ThirdBaseUi;
import com.loopj.android.http.RequestParams;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 费用类型
 * 
 * @author lyq
 * 
 */
public class FeeTypeUI extends ThirdBaseUi {
	private final String TAG = FeeTypeUI.class.getSimpleName();
	private TitleBar titlebar;
	private ListView lv_myorder;// 我的订单的列表
	private TextView tv_myorder_nodata;// 没有数据
	private LinearLayout linea_sum;//
	ArrayList<String> list2 = new ArrayList<String>() {
//		{
//			add("首付");
//			add("租金");
//			add("尾付");
//			add("滞纳金");
//			add("逾期管理费");
//			add("收车费");
//			add("过户押金");
//			add("违章费用");
//			add("定金");
//		}
	};
	private CommonAdapter<String> adapter;
	private Intent intent;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.ui_myorder);
		setSlideFinishEnabled(false);
		super.onCreate(arg0);

		getDate();
	}

	private void getDate() {

		sendRequest(Http_Url.GetFeeList, new RequestParams(), FreeTypeBean.class, new BaseCallBack<FreeTypeBean>() {
			@Override
			public void onSuccess(FreeTypeBean freeTypeBean) {
				List<String> model = freeTypeBean.getModel();
				if (model != null && model.size() > 0 && adapter != null) {
					adapter.replaceAll(model);
				}
			}
		});
	}

	@Override
	protected void initControl() {
		titlebar = new TitleBar();
		lv_myorder = (ListView) findViewById(R.id.lv_myorder);
		linea_sum = (LinearLayout) findViewById(R.id.linea_sum);
		linea_sum.setVisibility(View.GONE);
		tv_myorder_nodata = (TextView) findViewById(R.id.tv_myorder_nodata);
		tv_myorder_nodata.setText("没有数据");
		adapter = new CommonAdapter<String>(context, list2, R.layout.ui_pop_select) {

			@Override
			public void fillItemData(CommonViewHolder holder, String data, int position) {
				TextView name = holder.getView(R.id.tv_pop);
				name.setText(data);
				ImageView checked=holder.getView(R.id.checked);
					checked.setVisibility(View.GONE);
			}
		};
		lv_myorder.setAdapter(adapter);
	}

	@Override
	protected void initControlEvent() {
		lv_myorder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				intent = getIntent();
				String feetype = (String) parent.getAdapter().getItem(position);
				intent.putExtra("feetype", feetype);
				intent.putExtra("UI", "FeeTypeUI");
				Log.i(TAG, "ui---"+intent.getStringExtra("UI"));
				AbsUI.startClearTopUI(context, PayUI.class, intent);
				finish();
			}
		});
	}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titleBar_myorder, titlebar);
	}

	@Override
	public void onAttachedToWindow() {
		titlebar.setTitle("费用类型");
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
