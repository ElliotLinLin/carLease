package com.hst.Carlease.widget;

import com.hst.Carlease.R;
import com.hst.Carlease.util.PopupWindowHelper;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OldRegister implements OnClickListener{


	private Context context;
	private View rootView;
	private PopupWindowHelper helper;
	private View anchor;// 宿主
	private TextView texttel;
	private TextView weixinpay;//老用户绑定
	private String toast;

	public OldRegister(Context context, View anchor,String toast) {
		this.context = context;
		this.anchor = anchor;
		this.toast=toast;
		init();
	}

	private void init() {
		rootView = rootView.inflate(context, R.layout.oldpop_register, null);
		helper = new PopupWindowHelper(rootView);
		weixinpay = (TextView) rootView.findViewById(R.id.tv_olduser);
		texttel=(TextView) rootView.findViewById(R.id.texttel);
		texttel.setText(toast);
	}

	public OldRegister setweixinPay(OnClickListener listener) {
		if (rootView != null && listener != null) {
			rootView.findViewById(R.id.tv_olduser).setOnClickListener(listener);
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
