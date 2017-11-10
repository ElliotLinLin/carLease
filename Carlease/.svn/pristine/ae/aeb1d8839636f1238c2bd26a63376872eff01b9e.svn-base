package com.hst.Carlease.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.HotCarDetails2Bean;
import com.hst.Carlease.http.bean.HotCarDetailsBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.widget.gallary.BGABanner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.widget.Prompt;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 车型的详细信息
 * 
 * @author lyq
 * 
 */
public class HotModelDetailsUI extends AbsUI2 implements
		BGABanner.Adapter<ImageView, String> {
	private TitleBar titlebar;// 标题栏
	private final String TAG = HotModelDetailsUI.class.getSimpleName();
	private Intent intent;
	private TextView btn_order;// 立即预定
	private BGABanner banner_main_accordion;
	private TextView carband_name, first_pay, month_pay, melige, line_people,
			pailiang, car_model, bags, oil_con, big_speed, passener;// 品牌名字，首付,月供,里程,在线人数,排量,车辆品牌
	private List<String> imageList;// 图片集合
	private RelativeLayout rv_layout;

	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {

			finish();
			return;
		}
		super.setContentView(R.layout.ui_hotmodeldetails);
		super.setSlideFinishEnabled(false);
		super.onCreate(arg0);
	}

	@Override
	protected void initControl() {
		titlebar = new TitleBar();
		intent = getIntent();
		btn_order = (TextView) findViewById(R.id.btn_order);
		banner_main_accordion = (BGABanner) findViewById(R.id.banner_main_accordion);
		carband_name = (TextView) findViewById(R.id.carband_name);
		first_pay = (TextView) findViewById(R.id.first_pay);
		month_pay = (TextView) findViewById(R.id.month_pay);
		melige = (TextView) findViewById(R.id.melige);
		line_people = (TextView) findViewById(R.id.line_people);
		pailiang = (TextView) findViewById(R.id.pailiang);
		car_model = (TextView) findViewById(R.id.car_model);
		bags = (TextView) findViewById(R.id.bags);
		oil_con = (TextView) findViewById(R.id.oil_con);
		big_speed = (TextView) findViewById(R.id.big_speed);
		passener = (TextView) findViewById(R.id.passener);
		rv_layout = (RelativeLayout) findViewById(R.id.rv_layout);
		GetListTask();
		ViewGroup.LayoutParams rl = (ViewGroup.LayoutParams) rv_layout
				.getLayoutParams();
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
		int screenWidth = mDisplayMetrics.widthPixels;
		rl.height = (int) (screenWidth * 0.75);
		rl.width = screenWidth;
		Log.i(TAG, "(int) (screenWidth*0.6)" + (int) (screenWidth * 0.6));
		rv_layout.setLayoutParams(rl);
	}

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

	@Override
	protected void initControlEvent() {
		btn_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.putExtra("UI", "HotModelDetailsUI");
				AbsUI2.startUI(context, OnlineBookingUI1.class, intent);
			}
		});
	}

	// 找回密码的任务
	private void GetListTask() {
		HotCarDetailsBean bean = new HotCarDetailsBean();
		int id = intent.getIntExtra("id", 0);
		bean.setHcm_Id(id);
		NetworkState state = new NetworkState(context);
		if (state.isConnected() == false) {
			Prompt.showWarning(context, "请检查您的网络");
			return;
		}
		try {
			AsyncHttpUtil.post(context, Http_Url.GetHirePurchaseCarModel, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							btn_order) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
							Log.e(TAG, "qqqq arg2" + arg2);
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Bean bean = GJson.parseObject(arg2, Bean.class);
							Log.i(TAG, "result" + bean.getD());
							if (bean != null) {
								HotCarDetails2Bean CarDetails = GJson
										.parseObject(bean.getD(),
												HotCarDetails2Bean.class);
								if (CarDetails != null) {

									carband_name.setText(CarDetails
											.getCarModerName());
									first_pay.setText("首付"
											+ CarDetails.getMpayment() + "元");
									month_pay.setText("24期月供"
											+ CarDetails.getTwoFMMpay()
											+ "元      " + "36期月供"
											+ CarDetails.getThreeFMMpay() + "元");
									melige.setText(CarDetails.getTankCapacity());// 里程
									line_people.setText(CarDetails.getCarYear());// 首次上映
									pailiang.setText(CarDetails
											.getDisplacement());// 排量
									car_model.setText(CarDetails
											.getCarModerName());
									bags.setText(CarDetails.getGearcase());
									oil_con.setText(CarDetails.getCombined() + "L");
									big_speed.setText(CarDetails.getTopvehicle() + "km/h");
									if (!isEmptyString(CarDetails
											.getSeatCount())) {
										passener.setText(CarDetails.getSeatCount() + "人");// 座位数
									} else {
										passener.setText("无数据");// 座位数
									}

									imageList = CarDetails.getCarModelUrl();
									show();

									intent.putExtra("CarMdoelID",
											CarDetails.getHcm_CarMdoelID());
									intent.putExtra("name",
											CarDetails.getCarModerName());

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
		super.addFgm(R.id.titlebar, titlebar);
	}

	@Override
	public void onAttachedToWindow() {
		titlebar.setTitle("车辆详情");
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
	protected void onDestroy() {
		banner_main_accordion.stopAutoPlay();
		super.onDestroy();
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
