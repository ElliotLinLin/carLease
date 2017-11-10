package com.hst.Carlease.widget.mywidget;

import com.hst.Carlease.R;
import com.hst.Carlease.util.PopupWindowHelper;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PopRegister implements OnClickListener {

	private Context context;
	private View rootView;
	private PopupWindowHelper helper;
	private View anchor;// 宿主
	private TextView weixinpay;//老用户绑定
	private TextView alipay;//跳过
	private TextView texttel;
	private String tishi;
	private String anString;

	public PopRegister(Context context, View anchor,String tishi,String anniu) {
		this.context = context;
		this.anchor = anchor;
		this.tishi=tishi;
		this.anString=anniu;
		init();
	}

	private void init() {
		rootView = rootView.inflate(context, R.layout.pop_register, null);
		helper = new PopupWindowHelper(rootView);
		texttel=(TextView) rootView.findViewById(R.id.texttel);
		weixinpay = (TextView) rootView.findViewById(R.id.tv_olduser);
		alipay = (TextView) rootView.findViewById(R.id.login);
		texttel.setText(tishi);
		alipay.setText(anString);
	}

	public PopRegister setweixinPay(OnClickListener listener) {
		if (rootView != null && listener != null) {
			rootView.findViewById(R.id.tv_olduser).setOnClickListener(listener);
		}
		return this;
	}

	public PopRegister setalipay(OnClickListener listener) {
		if (rootView != null && listener != null) {
			rootView.findViewById(R.id.login).setOnClickListener(listener);
		}

		return this;
	}

	public void show() {
		if (helper != null) {
			helper.showFillParent(anchor);
		}
	}

	public void close() {
		if (helper != null) {
			helper.dismiss();
		}
	}

	@Override
	public void onClick(View arg0) {

	}



}
