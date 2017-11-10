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
import com.hst.Carlease.http.bean.MyMessageBean;
import com.hst.Carlease.http.bean.MyMessageBean.MessageBean;
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
 * 我的消息
 * @author lyq
 *
 */

public class MyMessageUI extends AbsUI2{
	private final String TAG=MyMessageUI.class.getSimpleName();

	private TitleBar titlebar;
	private ListView lv_myorder;//我的消息列表
	private TextView tv_myorder_nodata;//没有数据
	private LinearLayout linea_sum;//
	private CommonAdapter<MyMessageBean.MessageBean> adapter;
	private List<MyMessageBean.MessageBean> list;
	

	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {  
	        
	        finish();  
	        return;  
	    }  
		setContentView(R.layout.ui_myorder);
		super.setSlideFinishEnabled(false);
		super.onCreate(arg0);

	}

	@Override
	protected void initControl() {
		titlebar = new TitleBar();
		lv_myorder=(ListView) findViewById(R.id.lv_myorder);
		tv_myorder_nodata=(TextView) findViewById(R.id.tv_myorder_nodata);
		linea_sum=(LinearLayout) findViewById(R.id.linea_sum);
		linea_sum.setVisibility(View.GONE);
		tv_myorder_nodata.setText("没有消息");
		 GetVehicleIllegalList();
	}

	@Override
	protected void initControlEvent() {
		lv_myorder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MessageBean MessageBean=(com.hst.Carlease.http.bean.MyMessageBean.MessageBean) parent.getAdapter().getItem(position);
				Intent intent=new Intent();
				intent.putExtra("id",MessageBean.getM_id()+ "");
				AbsUI.startUI(context, MyMessageDetailsUI.class,intent);
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
	protected void GetVehicleIllegalList() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		 GetOrderListBean bean=new GetOrderListBean();
				 bean.setTokenID(SPUtils.get(context, Constants.tokenID,
				 "").toString());

		try {
			AsyncHttpUtil.post(ui, Http_Url.GetNewsList, bean,"application/json", new AsyncCallBackHandler(ui, "",
							true,lv_myorder) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
							tv_myorder_nodata.setVisibility(View.VISIBLE);
							lv_myorder.setVisibility(View.GONE);
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Log.i(TAG, "result" + arg2);
							Bean bean = GJson.parseObject(arg2, Bean.class);
							Log.i(TAG, "result 截取" + bean.getD());
							if (bean.getD() != null) {
								MyMessageBean beans = GJson.parseObject(
										bean.getD(), MyMessageBean.class);
                                     if (beans!=null) {
                                    	 if (beans.getStatu() ==1) {
         									list=beans.getModel();
         									if (list.size()==0) {
         										tv_myorder_nodata.setVisibility(View.VISIBLE);
         										lv_myorder.setVisibility(View.GONE);
         										return;
         									}
         									if (list!=null) {
         										adapter=new CommonAdapter<MyMessageBean.MessageBean>(ui, list, R.layout.ui_item_mymessage) {

         											@Override
         											public void fillItemData(CommonViewHolder holder, MessageBean data,
         													int position) {
         												TextView title=holder.getView(R.id.title);
         												TextView time=holder.getView(R.id.time);
         												title.setText("标题："+data.getM_Title());
         												time.setText(data.getM_CreateDate());
         											}
         										};
         										lv_myorder.setAdapter(adapter);
         										tv_myorder_nodata.setVisibility(View.GONE);
         									}else {
         										tv_myorder_nodata.setVisibility(View.VISIBLE);
         										lv_myorder.setVisibility(View.GONE);
         									}
         								}else if(beans.getStatu()==-2){
         									ToastL.show(beans.getMsg());
         									StringUtils.IsOUTOFtime(context,MyMessageUI.this.ui);
         								}  else {
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
		titlebar.setTitle("我的消息");
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
