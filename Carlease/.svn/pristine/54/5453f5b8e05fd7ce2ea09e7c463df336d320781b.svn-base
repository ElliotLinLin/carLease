package com.hst.Carlease.ui;

import java.util.List;

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
import com.hst.Carlease.http.bean.BrandOffice;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;

/**
 * 选择门店
 * 
 * @author lyq
 * 
 */
public class StoreUI extends AbsUI {
	private TitleBar titlebar;
	private ListView lv_myorder;// 我的订单的列表
	private TextView tv_myorder_nodata;// 没有数据
	private CommonAdapter<BrandOffice> adapter;// 适配器
	private LinearLayout linea_sum;//
	private List<BrandOffice> list;
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
		intent = getIntent();
		intent.putExtra("UI", "BranchOfficeUI");
		tv_myorder_nodata = (TextView) findViewById(R.id.tv_myorder_nodata);
		tv_myorder_nodata.setVisibility(View.GONE);
		list = (List<BrandOffice>) intent.getSerializableExtra("list");
		adapter = new CommonAdapter<BrandOffice>(ui, list, R.layout.text_item) {

			@Override
			public void fillItemData(CommonViewHolder holder,
					final BrandOffice data, int position) {
				TextView name = holder.getView(R.id.tv_pop);
				name.setText(data.getCompanyName());
			}
		};
		lv_myorder.setAdapter(adapter);
	}

	@Override
	protected void initControlEvent() {
		lv_myorder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BrandOffice BrandOffice = (BrandOffice) parent.getAdapter()
						.getItem(position);
				intent.putExtra("CompanyID", BrandOffice.getCompanyID());
				if (position==0) {
					intent.putExtra("storeName", "");
				}else {
					intent.putExtra("storeName", BrandOffice.getCompanyName());
				}
				
				AbsUI.startClearTopUI(context, OnlineBookingUI1.class, intent);

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
		titlebar.setTitle("选择门店");
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
