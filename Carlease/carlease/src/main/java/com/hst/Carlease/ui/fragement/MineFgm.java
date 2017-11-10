package com.hst.Carlease.ui.fragement;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.eventBean.MainStype;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.ui.FeedBacktUI;
import com.hst.Carlease.ui.LoginUI;
import com.hst.Carlease.ui.MyMessageUI;
import com.hst.Carlease.ui.MyOrderUI;
import com.hst.Carlease.ui.MyPayUI;
import com.hst.Carlease.ui.MyViolationUI;
import com.hst.Carlease.ui.PayUI;
import com.hst.Carlease.ui.RentCarsUI;
import com.hst.Carlease.ui.TransactionHistoryUI;
import com.hst.Carlease.ui.UpdatePassUI;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.widget.mywidget.MyCallPop;
import com.tools.app.AbsFgm2;
import com.tools.app.AbsUI;
import com.tools.app.AlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 我的界面
 * 
 * @author lyq
 * 
 */
public class MineFgm extends AbsFgm2 {
	public View rootView; // 根布局
	private RelativeLayout rv_myorder, rv_mypay, rv_myvolition, rv_mymessage,
			rv_logout, update_pass, rent_cars, pople, rv_pay, call_paone;// 我的订单，我的账单，我的违章，我的信息
	private TextView username, telphone;
	private Button btn_login_Login;
	private String tel;
	private MyCallPop popwindow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.ui_mine_fgm, null);
		EventBus.getDefault().register(MineFgm.this);
		return rootView;
	}

	@Override
	protected void initControl() {
		rv_myorder = (RelativeLayout) rootView.findViewById(R.id.rv_myorder);
		rv_pay = (RelativeLayout) rootView.findViewById(R.id.rv_pay);
		rv_mypay = (RelativeLayout) rootView.findViewById(R.id.rv_mypay);
		rv_myvolition = (RelativeLayout) rootView
				.findViewById(R.id.rv_myvolition);
		rv_mymessage = (RelativeLayout) rootView
				.findViewById(R.id.rv_mymessage);
		call_paone = (RelativeLayout) rootView.findViewById(R.id.call_paone);
		telphone = (TextView) rootView.findViewById(R.id.telphone);
		rv_logout = (RelativeLayout) rootView.findViewById(R.id.rv_logout);
		update_pass = (RelativeLayout) rootView.findViewById(R.id.update_pass);
		rent_cars = (RelativeLayout) rootView.findViewById(R.id.rent_cars);
		pople = (RelativeLayout) rootView.findViewById(R.id.pople);
		username = (TextView) rootView.findViewById(R.id.username);
		btn_login_Login = (Button) rootView.findViewById(R.id.btn_login_Login);
		username.setText(SPUtils.get(context, Constants.UserName, "用户名")
				.toString());
		tel = telphone.getText().toString();
	}

	// 显示弹框
	private void popWindowShow() {
		popwindow = new MyCallPop(context, telphone);
		telphone.postDelayed(new Runnable() {

			@Override
			public void run() {
				popwindow.show();
				// 老用户绑定
				popwindow.setalipay(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						popwindow.close();
						Intent intent = new Intent(Intent.ACTION_CALL);
						intent.setData(Uri.parse("tel:" + tel));
						startActivity(intent);
					}
				});
				popwindow.setweixinPay(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						popwindow.close();
					}
				});
			}
		}, 300);
	}

	@Override
	protected void initControlEvent() {
		rv_myorder.setOnClickListener(listener);
		rv_mypay.setOnClickListener(listener);
		rv_myvolition.setOnClickListener(listener);
		rv_mymessage.setOnClickListener(listener);
		rv_logout.setOnClickListener(listener);
		update_pass.setOnClickListener(listener);
		rent_cars.setOnClickListener(listener);
		pople.setOnClickListener(listener);
		rv_pay.setOnClickListener(listener);
		btn_login_Login.setOnClickListener(listener);
		call_paone.setOnClickListener(listener);
	}

	View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rv_mymessage:// 我的消息
				AbsUI.startUI(context, MyMessageUI.class);
				break;
			case R.id.rv_myvolition:// 我的违章
				AbsUI.startUI(context, MyViolationUI.class);
				break;
			case R.id.rv_mypay:// 我的账单
				AbsUI.startUI(context, MyPayUI.class);
				break;
			case R.id.rv_myorder:// 我的订单
				AbsUI.startUI(context, MyOrderUI.class);
				break;
			case R.id.rv_logout:// 交易记录
				AbsUI.startUI(context, TransactionHistoryUI.class);
				break;
			case R.id.update_pass:// 修改密码
				AbsUI.startUI(context, UpdatePassUI.class);
				break;
			case R.id.rent_cars:// 已租车辆
				AbsUI.startUI(context, RentCarsUI.class);
				break;
			case R.id.rv_pay:// 我的消息
				Intent intent = new Intent();
				intent.putExtra("UI", "MineFgm");
				AbsUI.startUI(context, PayUI.class, intent);
				break;
			case R.id.pople:// 我的消息
				AbsUI.startUI(context, FeedBacktUI.class);
				break;
			case R.id.btn_login_Login:// 注销登录
				AlertDialog.Builder builder = new AlertDialog.Builder(ui);
				builder.setTitle(" ");
				builder.setMessage("您确定注销登录吗？");

				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								SPUtils.put(context, Constants.ForgetCode,
										"false");
								AbsUI.startClearTopUI(context, LoginUI.class);
//								PushServiceOperate.stop(getActivity());
								AbsUI.stopUI(ui);
							}

						})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}

								}).create().show();
				break;
			case R.id.call_paone:
				popWindowShow();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void initMember() {

	}

	public void close() {
		if (popwindow != null) {
			popwindow.close();
		}
	}

	@Subscribe
	public void onEventMainThread(MainStype ev) {
		if (ev.getMsg() == Constant.me_pop) {
			close();
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(MineFgm.this);
		super.onDestroy();
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
