package com.hst.Carlease.ui;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.widget.Prompt;

public class OnlineBookingUI2 extends AbsUI2 {
	private TitleBar titlebar;
	private EditText company_name, fa_name, fa_idcard, fa_phone, register_no,
			jing_name, jing_carno, jing_phone;// 企业客户名称，法人姓名，法人身份证号码，企业紧急电话，注册号，经办人姓名，经办人身份证号，经办人紧急电话
	private TextView btn_register;//按钮点击

	@Override
	protected void onCreate(Bundle arg0) {
		super.setContentView(R.layout.ui_onlinebooking2);
		super.onCreate(arg0);
	}

	@Override
	protected void initControl() {
		titlebar = new TitleBar();
		company_name = (EditText) findViewById(R.id.company_name);
		fa_name = (EditText) findViewById(R.id.fa_name);
		fa_idcard = (EditText) findViewById(R.id.fa_idcard);
		fa_phone = (EditText) findViewById(R.id.fa_phone);
		register_no = (EditText) findViewById(R.id.register_no);
		jing_name = (EditText) findViewById(R.id.jing_name);
		jing_carno = (EditText) findViewById(R.id.jing_carno);
		jing_phone = (EditText) findViewById(R.id.jing_phone);
		btn_register = (TextView) findViewById(R.id.btn_register);
	}

	@Override
	protected void initControlEvent() {
		btn_register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckPram();
				AbsUI2.startUI(context, OnlineBookingUI3.class);
			}
		});
	}
	// 检查参数
		private void CheckPram() {
			if (company_name.getText().toString().isEmpty()) {
				Prompt.showToast(context, "请输入企业客户名称");
				return;
			}
			if (fa_name.getText().toString().isEmpty()) {
				Prompt.showToast(context, "请输入法人的名字");
				return;
			}
			if (fa_idcard.getText().toString().isEmpty()) {
				Prompt.showToast(context, "请输入法人的身份证号码");
				return;
			}
			if (fa_phone.getText().toString().isEmpty()) {
				Prompt.showToast(context, "请输入法人的联系电话");
				return;
			}
			if (register_no.getText().toString().isEmpty()) {
				Prompt.showToast(context, "请输入企业注册号");
				return;
			}
			if (jing_name.getText().toString().isEmpty()) {
				Prompt.showToast(context, "请输入经办人姓名");
				return;
			}
			 if (jing_carno.getText().toString().isEmpty()) {
			 Prompt.showToast(context, "请输入经办人的身份证号码");
			 return;
			 }
			 if (jing_phone.getText().toString().isEmpty()) {
			 Prompt.showToast(context, "请输入经办人的紧急电话");
			 return;
			 }
		}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titlebar, titlebar);
	}

	@Override
	public void onAttachedToWindow() {
		titlebar.setTitle("在线预订");
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
