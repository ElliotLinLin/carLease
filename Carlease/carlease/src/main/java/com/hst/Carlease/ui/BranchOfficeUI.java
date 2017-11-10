package com.hst.Carlease.ui;

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
import com.hst.Carlease.http.bean.BrandOffice;
import com.hst.Carlease.http.bean.BrandOfficeBean;
import com.hst.Carlease.http.bean.GetOrderListBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.util.Log;
import com.tools.widget.Prompt;

import org.apache.http.Header;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
/**
 * 分公司
 * @author lyq
 *
 */
public class BranchOfficeUI extends AbsUI2{
	private final String TAG = BranchOfficeUI.class.getSimpleName();
	private TitleBar titlebar;
	private ListView lv_myorder;// 我的订单的列表
	private TextView tv_myorder_nodata;// 没有数据
	private CommonAdapter<BrandOffice> adapter;// 适配器
	private LinearLayout linea_sum;//
	private List<BrandOffice> list;
	private Intent intent;

	@Override
	protected void onCreate(Bundle arg0) {
//		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {  
//	        
//	        finish();  
//	        return;  
//	    }  
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
		intent=getIntent();
		intent.putExtra("UI", "BranchOfficeUI");
		tv_myorder_nodata = (TextView) findViewById(R.id.tv_myorder_nodata);
		tv_myorder_nodata.setText("没有数据");
		GetBranchCompanyList();
	}

	@Override
	protected void initControlEvent() {
		lv_myorder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BrandOffice BrandOffice=(BrandOffice) parent.getAdapter().getItem(position);
				intent.putExtra("CompanyName", BrandOffice.getCompanyName());
			    GetStoreList(BrandOffice.getCompanyID(),BrandOffice.getCompanyName());
			}
		});
	}
	/**
	 * 获取门店列表
	 */
	protected void GetStoreList(final int id,final String name) {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		GetOrderListBean bean = new GetOrderListBean();
		bean.setParentid(id);
		try {
			AsyncHttpUtil.post(ui, Http_Url.GetStoreList, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,lv_myorder) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Bean bean = GJson.parseObject(arg2, Bean.class);
							if (bean.getD() != null) {
								BrandOfficeBean beans = GJson.parseObject(
										bean.getD(), BrandOfficeBean.class);
								if (beans==null) {
									return;
								}
								if (beans.getStatu() == 1) {
									list = beans.getModel();
									if (list != null) {
										BrandOffice beanBrandOffice= new BrandOffice();
										beanBrandOffice.setCompanyID(id);
										beanBrandOffice.setCompanyName(name);
										list.add(0,beanBrandOffice);
										intent.putExtra("list", (Serializable)list);
										intent.putExtra("CompanyID", id);
										AbsUI.startUI(context, StoreUI.class, intent);
									} else {
										intent.putExtra("CompanyID", id);
										Log.i(TAG, "name==id="+id);
										intent.putExtra("storeName", "");
										AbsUI.startClearTopUI(context, OnlineBookingUI1.class, intent);
										finish();
									}
								}else {
									ToastL.show(beans.getMsg());
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
		titlebar.setTitle("选择分公司");
		titlebar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		super.onAttachedToWindow();
	}
	/**
	 * 我的违章列表
	 */
	protected void GetBranchCompanyList() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		GetOrderListBean bean = new GetOrderListBean();
		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());
		try {
			AsyncHttpUtil.post(ui, Http_Url.GetBranchCompanyList, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,lv_myorder) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Bean bean = GJson.parseObject(arg2, Bean.class);
							Log.i(TAG, "result 截取" + bean.getD());
							if (bean.getD() != null) {
								BrandOfficeBean beans = GJson.parseObject(
										bean.getD(), BrandOfficeBean.class);
								if (beans==null) {
									return;
								}
								if (beans.getStatu() == 1) {
									list = beans.getModel();
									if (list != null) {
										adapter = new CommonAdapter<BrandOffice>(
												ui, list,
												R.layout.text_item) {

											@Override
											public void fillItemData(
													CommonViewHolder holder,
													final BrandOffice data,
													int position) {
												TextView name = holder
														.getView(R.id.tv_pop);
												name.setText(data.getCompanyName());
											}
										};
										lv_myorder.setAdapter(adapter);
										tv_myorder_nodata
												.setVisibility(View.GONE);
									} else {
										tv_myorder_nodata
												.setVisibility(View.VISIBLE);
										lv_myorder.setVisibility(View.GONE);
									}
								}else {
									ToastL.show(beans.getMsg());
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
	protected byte[] doInBackgroundLoader() {
		return null;
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {
		
	}

}
