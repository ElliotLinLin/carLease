package com.hst.Carlease.widget.mywidget;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.util.PopupWindowHelper;

public class MyCallPop implements OnClickListener {

	private Context context;
	private View rootView;
	private PopupWindowHelper helper;
	private View anchor;// 宿主
	private TextView weixinpay;//老用户绑定
	private TextView alipay;//跳过
	private TextView texttel;
	private LinearLayout lear;

	public MyCallPop(Context context, View anchor) {
		this.context = context;
		this.anchor = anchor;
		init();
	}

	private void init() {
		rootView = rootView.inflate(context, R.layout.call_pop, null);
		helper = new PopupWindowHelper(rootView);
		weixinpay = (TextView) rootView.findViewById(R.id.tv_olduser);
		alipay = (TextView) rootView.findViewById(R.id.login);
		texttel=(TextView) rootView.findViewById(R.id.texttel);
		lear=(LinearLayout) rootView.findViewById(R.id.lear);
	}

	public MyCallPop setweixinPay(OnClickListener listener) {
		if (rootView != null && listener != null) {
			rootView.findViewById(R.id.tv_olduser).setOnClickListener(listener);
		}
		return this;
	}

	public MyCallPop setalipay(OnClickListener listener) {
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
