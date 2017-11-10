package com.hst.Carlease.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.http.bean.MypayBean;
import com.hst.Carlease.http.bean.MypayBean.PayBean;
import com.hst.Carlease.http.bean.MypayBean.PayBean.BillItem;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.ui.OrderPayUI;
import com.tools.app.AbsUI;
import com.tools.util.Log;

public class MypayAdapter extends BaseExpandableListAdapter{
	private List<MypayBean.PayBean> groupList;// 列表
	private List<MypayBean.PayBean.BillItem> childList;
	private Context context;
	public MypayAdapter(Context context,List<MypayBean.PayBean> groupList,List<MypayBean.PayBean.BillItem> childList) {
		this.groupList = groupList;
		this.childList=childList;
		this.context=context;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (childList == null) {
			return null;
		}
		return childList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolderChild holder=null;
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_mypay_child, null);
			holder=new ViewHolderChild();
			holder.datatime= (TextView) convertView.findViewById(R.id.datatime);
			holder.paytype = (TextView) convertView.findViewById(R.id.paytype);
			holder.money = (TextView) convertView.findViewById(R.id.money);
			holder.paystatus = (TextView) convertView.findViewById(R.id.paystatus);
			holder.tv_pay=(LinearLayout) convertView.findViewById(R.id.tv_pay);
			holder.view=(TextView) convertView.findViewById(R.id.view);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolderChild) convertView.getTag();
		}
		final BillItem data=groupList.get(groupPosition).getBillItem().get(childPosition);
		holder.datatime.setText(data.getPlyear() + "."
				+ data.getPlmonth() + "." + data.getPlday());
		holder.paytype.setText(data.getDnt_name());
		holder.money.setText(data.getPl_amount() + "元");
		
		if (data.getPl_payStatus() == 0) {
			holder.paystatus.setText(data.getPl_status());
			holder.paystatus.setTextColor(Color.parseColor("#666666"));
			holder.paystatus.setBackgroundColor(Color.parseColor("#FFFFFF"));
		} else {
			holder.tv_pay.setVisibility(View.VISIBLE);
			holder.paystatus.setText("去付款");
			holder.paystatus.setTextColor(Color.parseColor("#ffffff"));
			holder.paystatus.setBackgroundResource(R.drawable.shape_order_orange);
		}
		if (childPosition==groupList.get(groupPosition).getBillItem().size()-1) {
			holder.view.setVisibility(View.VISIBLE);
		}else {
			holder.view.setVisibility(View.GONE);
		}
		holder.tv_pay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (data.getPl_payStatus() == 0) {
					return;
				}
				Intent intent =new Intent();
				intent.putExtra("orderNo",data.getPlid()+"");
				intent.putExtra("dataFrom",Constant.ORDER_PAY3);
				intent.putExtra("money", data.getPl_amount()+"");
				intent.putExtra("body",groupList.get(groupPosition).getBillItem().get(childPosition).getDnt_name());
				intent.putExtra("contactid", groupList.get(groupPosition).getContractID());
				Log.i("mypayAdapter", "单号==Adapter==="+groupList.get(groupPosition).getSerialNumber());
				Log.i("mypayAdapter", "body==Adapter==="+groupList.get(groupPosition).getBillItem().get(childPosition).getDnt_name());
				AbsUI.startUI(context, OrderPayUI.class,intent);
			}
		});
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupList.get(groupPosition).getBillItem() == null) {
			return 0;
		}
		return groupList.get(groupPosition).getBillItem().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (groupList == null) {
			return null;
		}
		return groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		if (groupList == null) {
			return 0;
		}
		return groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.ui_mypay_item, null);
			holder=new ViewHolder();
			holder.orderno = (TextView) convertView.findViewById(R.id.orderno);
			holder.carname = (TextView) convertView.findViewById(R.id.carname);
			holder.line=convertView.findViewById(R.id.line);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		PayBean data=groupList.get(groupPosition);
		childList=data.getBillItem();
		holder.orderno.setText(data.getSerialNumber());
		holder.carname.setText(data.getPlateNumber());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	public class ViewHolder{
		TextView orderno;
		TextView carname;
		View line;
	}
	public class ViewHolderChild{
		TextView datatime ;//
		TextView paytype  ;// 
		TextView money  ;//
		TextView paystatus  ;//
		LinearLayout tv_pay  ;//
		TextView view;
	}
}
