package com.hst.Carlease.ui;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.SecondCarDetailsBean2;
import com.hst.Carlease.http.bean.SecondCarDetaislBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.gallary.BGABanner;
import com.hst.Carlease.widget.mywidget.MyCallPop;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.util.Log;

public class SecondCarDetailsUI extends AbsUI2 implements
		BGABanner.Adapter<ImageView, String> {
	private static final String TAG = SecondCarDetailsUI.class.getSimpleName();
	private TitleBar titleBar;
	private final int GET_DATA = 0;
	private final int SET_DATA = 1;
	private final int SHOW_FALLERY = 2;
	private Intent intent;
	private SecondCarDetailsBean2.SecondCarDetails SecondCar;
	private TextView carband_name, first_pay, price, tv_first_pay, tv_last_pay,
			pay_tween, pay_three, melige, line_people, pailiang, weihu,
			biaozhun, usecar, souce, jian, paizhao, seat, pinpai, oil, address;
	private BGABanner banner_main_accordion;
	private List<String> imageList;
	private LinearLayout call;
	private TextView order_order;// 预约看车
	private RelativeLayout rv_layout;
	private MyCallPop popwindow;

	// private TextView tv1, tv2, tv3, tv4;

	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.ui_sencond_car_details);
		super.setSlideFinishEnabled(false);
		super.onCreate(arg0);
	}

	@Override
	protected void initControl() {
		titleBar = new TitleBar();
		carband_name = (TextView) findViewById(R.id.carband_name);
		intent = getIntent();
		banner_main_accordion = (BGABanner) findViewById(R.id.banner_main_accordion);
		first_pay = (TextView) findViewById(R.id.first_pay);
		price = (TextView) findViewById(R.id.price);
		tv_first_pay = (TextView) findViewById(R.id.tv_first_pay);
		tv_last_pay = (TextView) findViewById(R.id.tv_last_pay);
		pay_tween = (TextView) findViewById(R.id.pay_tween);
		pay_three = (TextView) findViewById(R.id.pay_three);
		melige = (TextView) findViewById(R.id.melige);
		line_people = (TextView) findViewById(R.id.line_people);
		pailiang = (TextView) findViewById(R.id.pailiang);
		weihu = (TextView) findViewById(R.id.weihu);
		biaozhun = (TextView) findViewById(R.id.biaozhun);
		usecar = (TextView) findViewById(R.id.usecar);
		souce = (TextView) findViewById(R.id.souce);
		jian = (TextView) findViewById(R.id.jian);
		paizhao = (TextView) findViewById(R.id.paizhao);
		seat = (TextView) findViewById(R.id.seat);
		pinpai = (TextView) findViewById(R.id.pinpai);
		oil = (TextView) findViewById(R.id.oil);
		call = (LinearLayout) findViewById(R.id.call);
		order_order = (TextView) findViewById(R.id.order_order);
		address = (TextView) findViewById(R.id.address);
		rv_layout = (RelativeLayout) findViewById(R.id.rv_layout);
		// tv1 = (TextView) findViewById(R.id.tv1);
		// tv2 = (TextView) findViewById(R.id.tv2);
		// tv3 = (TextView) findViewById(R.id.tv3);
		// tv4 = (TextView) findViewById(R.id.tv4);
		ViewGroup.LayoutParams rl = (ViewGroup.LayoutParams) rv_layout
				.getLayoutParams();
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
		int screenWidth = mDisplayMetrics.widthPixels;
		rl.width = screenWidth;
		rl.height = (int) (screenWidth * 0.75);
		Log.i(TAG, "(int) (screenWidth*0.6)" + (int) (screenWidth * 0.6));
		rv_layout.setLayoutParams(rl);
		hand.sendEmptyMessage(GET_DATA);
	}

	@Override
	protected void initControlEvent() {
		order_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AbsUI.startUI(context, OnlineOrderUI.class);
			}
		});
		call.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				popWindowShow();
			}
		});
	}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titleBar, titleBar);
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DATA:
				// 获取数据
				GetPagingListTask();
				break;
			case SET_DATA:
				setData();
				break;
			case SHOW_FALLERY:
				show();
				break;
			default:
				break;
			}
		};
	};

	// 图片轮播
	private void show() {
		if (imageList == null) {
			return;
		}
		banner_main_accordion.setAdapter(this);
		if (imageList != null && imageList.size() == 1) {
			banner_main_accordion.setAllowUserScrollable(false);
			banner_main_accordion.setAutoPlayAble(false);
		} else {
			banner_main_accordion.setAutoPlayAble(true);
			banner_main_accordion.setAutoPlayInterval(5000);
		}
		banner_main_accordion.setData(imageList, null);
	}

	private void setData() {
		if (SecondCar != null) {
			imageList = SecondCar.getCarUsedUrl();
			if (imageList.size() != 0) {
				hand.sendEmptyMessage(SHOW_FALLERY);
			}
			carband_name.setText(SecondCar.getCarModelName());
			first_pay.setText(SecondCar.getStrCardDate() + "/"
					+ SecondCar.getMileage() + "公里");
			price.setText(SecondCar.getDisposable() + "万元");
			tv_first_pay.setText(Html.fromHtml("首付:<font color=\"#FFA90C\">"
					+ SecondCar.getShouPayment() + "</font>万元"));
			tv_last_pay.setText(Html.fromHtml("尾付:<font color=\"#FFA90C\">"
					+ SecondCar.getWeiPayment() + "</font>万元"));
			pay_tween.setText(Html.fromHtml("24期月供:<font color=\"#FFA90C\">"
					+ SecondCar.getTwoMonthly() + "</font>元"));
			pay_three.setText(Html.fromHtml("36期月供:<font color=\"#FFA90C\">"
					+ SecondCar.getThreeMonthly() + "</font>元"));
			melige.setText(SecondCar.getMileage() + "公里");
			line_people.setText(SecondCar.getStrCardDate());
			pailiang.setText(SecondCar.getDisplacement());
			weihu.setText(SecondCar.getMaintenance());
			biaozhun.setText(SecondCar.getDischarge());
			usecar.setText(SecondCar.getNatureName());
			souce.setText(SecondCar.getSource());
			jian.setText(SecondCar.getAnnualInspection() + "到期");
			paizhao.setText(SecondCar.getShortPlateNum());
			if (SecondCar.getSeatCount() == 0) {
				seat.setText("无数据");
			} else {
				seat.setText(SecondCar.getSeatCount() + "人");
			}

			pinpai.setText(SecondCar.getBrandName());
			if (SecondCar.getOilTypeName().equals("")) {
				oil.setText("无数据");
			} else {
				oil.setText(SecondCar.getOilTypeName());
			}
			address.setText(SecondCar.getRegionName());

			// tv1.setText(SecondCar.getLetherSeat());
			// tv2.setText(SecondCar.getReverseImage());
			// tv3.setText(SecondCar.getGPSnavi());
			// tv4.setText(SecondCar.getSafe());
		}
	}

	// 找回密码的任务
	private void GetPagingListTask() {
		NetworkState state = new NetworkState(context);
		if (state.isConnected() == false) {
			ToastL.show("请检查您的网络");
			return;
		}
		SecondCarDetaislBean bean = new SecondCarDetaislBean();
		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());
		bean.setCarusedid(intent.getIntExtra("CarID", 0));
		try {
			AsyncHttpUtil.post(context, Http_Url.GetCarUsedDetail, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							carband_name) {
						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String result) {
							Bean bean = GJson.parseObject(result, Bean.class);
							Log.i(TAG, "result" + bean.getD());
							if (bean.getD() != null) {
								SecondCarDetailsBean2 SecondCarDetails = GJson
										.parseObject(bean.getD(),
												SecondCarDetailsBean2.class);
								if (SecondCarDetails.getModel() != null) {
									if (SecondCarDetails.getStatu() == 1) {
										// 赋值
										SecondCar = SecondCarDetails.getModel();
										hand.sendEmptyMessage(SET_DATA);
									} else if (SecondCarDetails.getStatu() == -2) {
										ToastL.show(SecondCarDetails.getMsg());
										StringUtils.IsOUTOFtime(context,
												SecondCarDetailsUI.this.ui);
									} else {
										ToastL.show(SecondCarDetails.getMsg());
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
	protected void onDestroy() {
		banner_main_accordion.stopAutoPlay();
		super.onDestroy();
	}

	// 显示弹框
	private void popWindowShow() {
		if (SecondCar != null && SecondCar.getPhone() != null) {
		} else {
			ToastL.show("该商家没有联系电话");
			return;
		}
		popwindow = new MyCallPop(context, call);
		call.postDelayed(new Runnable() {

			@Override
			public void run() {
				popwindow.show();
				// 老用户绑定
				popwindow.setalipay(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						popwindow.close();
						Intent intent = new Intent(Intent.ACTION_CALL);
						intent.setData(Uri.parse("tel:" + SecondCar.getPhone()));
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
	public void onAttachedToWindow() {
		titleBar.setTitle("二手车详情");
		titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		super.onAttachedToWindow();
	}

	@Override
	public void onBackPressed() {
		if (popwindow != null) {
			popwindow.close();
		}
		super.onBackPressed();
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

	@Override
	public void fillBannerItem(BGABanner banner, ImageView itemView,
			String model, int position) {
		ImageLoader.getInstance().displayImage(model, itemView);

	}

}
