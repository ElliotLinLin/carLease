package com.hst.Carlease.ui.fragement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.adapter.CommonAdapter;
import com.hst.Carlease.adapter.CommonViewHolder;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.eventBean.MainStype;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.HomePage2Bean;
import com.hst.Carlease.http.bean.HomePage2Bean.HomePageInfo.Carousel;
import com.hst.Carlease.http.bean.HomePage2Bean.HomePageInfo.newArrival;
import com.hst.Carlease.http.bean.HomePageBean;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.HomePageDetailsUI;
import com.hst.Carlease.ui.HotModelDetailsUI;
import com.hst.Carlease.ui.TmWebViewUI;
import com.hst.Carlease.widget.MyGridView;
import com.hst.Carlease.widget.gallary.BGABanner;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tools.app.AbsFgm2;
import com.tools.app.AbsUI;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.widget.Prompt;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * 
 * @author lyq
 * 
 */
public class HomePageFgm extends AbsFgm2 implements
		BGABanner.Delegate<ImageView, String>,
		BGABanner.Adapter<ImageView, String> {
	private final String TAG = HomePageFgm.class.getSimpleName();
	public View rootView; // 根布局
	private List<newArrival> ListData;
	private MyGridView lv_homepage;
	private CommonAdapter<newArrival> adapter;
	private TextView no_data;// 暂无数据
	private final int GET_DATA = 0;
	private final int CHECK_VERSION = 1;
	private final int SET_DATA = 2;
	private final int SET_ImageDATA = 3;
	private List<Carousel> imagelist;
	private LinearLayout ap_buycar, tian_mao;
	private LinearLayout rv_layout;
	private ImageView imageView;
	private TextView textid;
	private BGABanner banner_main_accordion;
	List<String> imgs = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.ui_home_fgm, null);
		return rootView;
	}

	@Override
	protected void initControl() {
		lv_homepage = (MyGridView) rootView.findViewById(R.id.lv_homepage);
		ap_buycar = (LinearLayout) rootView.findViewById(R.id.ap_buycar);
		tian_mao = (LinearLayout) rootView.findViewById(R.id.tian_mao);
		no_data = (TextView) rootView.findViewById(R.id.no_data);
		imageView = (ImageView) rootView.findViewById(R.id.imageView);
		rv_layout = (LinearLayout) rootView.findViewById(R.id.rv_layout);
		banner_main_accordion = (BGABanner) rootView
				.findViewById(R.id.banner_main_accordion);
		handler.sendEmptyMessage(CHECK_VERSION);
		textid = (TextView) rootView.findViewById(R.id.textid);
		ViewGroup.LayoutParams rl = (ViewGroup.LayoutParams) rv_layout
				.getLayoutParams();
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
		int screenWidth = mDisplayMetrics.widthPixels;
		rl.height = (int) (screenWidth * 0.6);
		Log.i(TAG, "(int) (screenWidth*0.6)" + (int) (screenWidth * 0.6));
		rv_layout.setLayoutParams(rl);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_DATA:
				GetListTask();
				break;
			case CHECK_VERSION:
				// checkVersion();
				break;
			case SET_DATA:
				setAdapter();
				break;
			case SET_ImageDATA:
				show();
			default:
				break;
			}
		};
	};

	// 图片轮播
	private void show() {
		if (imgs.size() != 0) {
			imgs.clear();
		}
		for (int i = 0; i < imagelist.size(); i++) {
			String url = imagelist.get(i).getUrl();
			imgs.add(url);
		}
		banner_main_accordion.setAdapter(this);
		banner_main_accordion.setAutoPlayInterval(5000);
		if (imgs != null && imgs.size() == 1) {
			banner_main_accordion.setAllowUserScrollable(false);
		} else {
			banner_main_accordion.setAutoPlayAble(true);
		}
		banner_main_accordion.setData(imgs, null);
		banner_main_accordion.setDelegate(this);
	}

	@Override
	protected void initControlEvent() {
		textid.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// EventBus.getDefault().post(new
				// MainStype(Constant.Main_mine));
//				AbsUI.startUI(context, ThirdMainActivity.class);
			}
		});
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(
						new MainStype(Constant.Main_changeSecond));
			}
		});
		ap_buycar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(new MainStype(Constant.Main_change));
			}
		});
		tian_mao.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AbsUI.startUI(context, TmWebViewUI.class);
			}
		});
		// mAbSlidingPlayView.
		// 跳到信息界面
		lv_homepage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HomePage2Bean.HomePageInfo.newArrival data = (HomePage2Bean.HomePageInfo.newArrival) parent
						.getAdapter().getItem(position);
				Intent intent = new Intent();
				intent.putExtra("id", data.getHcm_ID());
				// intent.putExtra("time", data.getCreateTime());
				AbsUI.startUI(context, HotModelDetailsUI.class, intent);
			}
		});
	}


	// 获取列表的任务
	private void GetListTask() {
		HomePageBean bean = new HomePageBean();
		// bean.setPageSize(10);
		// bean.setPageIndex(currpage);
		NetworkState state = new NetworkState(context);
		if (state.isConnected() == false) {
			Prompt.showWarning(context, "请检查您的网络");
			return;
		}
		try {
			AsyncHttpUtil.post(context, Http_Url.GetIndexAdvertiseInfo, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							banner_main_accordion) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
							Log.e(TAG, "qqqq arg2" + arg2);
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Log.i(TAG, "qqqq" + arg2);
							Bean bean = GJson.parseObject(arg2, Bean.class);
							Log.i(TAG, "@@@" + bean.getD());
							if (bean != null) {
								HomePage2Bean HomePage2Bean = GJson
										.parseObject(bean.getD(),
												HomePage2Bean.class);
								if (HomePage2Bean.getModel() == null) {
									no_data.setVisibility(View.VISIBLE);
									lv_homepage.setVisibility(View.GONE);
									return;
								}
								if (HomePage2Bean.getStatu() == 1) {
									if (HomePage2Bean.getModel()
											.getNewArrival() != null
											&& HomePage2Bean.getModel()
													.getNewArrival().size() > 0) {
										no_data.setVisibility(View.GONE);
										lv_homepage.setVisibility(View.VISIBLE);
										ListData = HomePage2Bean.getModel()
												.getNewArrival();
										handler.sendEmptyMessage(SET_DATA);
									}
									if (HomePage2Bean.getModel().getCarousel() != null
											&& HomePage2Bean.getModel()
													.getCarousel().size() > 0) {
										imagelist = HomePage2Bean.getModel()
												.getCarousel();
										handler.sendEmptyMessage(SET_ImageDATA);
									}
								} else {
									ToastL.show(HomePage2Bean.getMsg());
								}

							}
						}

					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	// 给adaper
	private void setAdapter() {
		adapter = new CommonAdapter<newArrival>(context, ListData,
				R.layout.ui_item_homeindex) {
			@Override
			public void fillItemData(CommonViewHolder holder, newArrival data,
					int position) {
				ImageView iv = holder.getView(R.id.iv_adver);
				TextView model_name = holder.getView(R.id.model_name);
				TextView first_pay = holder.getView(R.id.first_pay);
				TextView month_pay = holder.getView(R.id.month_pay);
				ImageLoader.getInstance().displayImage(
						data.getCarModelUrl().get(0), iv);
				model_name.setText(data.getCarModerName()
						+ "                      ");
				first_pay.setText(Html.fromHtml("首付：<font color=\"#FFA90C\"> "
						+ data.getMpayment() + "</font>元"));
				month_pay.setText("月供：" + data.getTwoFMMpay() + "元");
			}
		};
		lv_homepage.setAdapter(adapter);
		// 解决ScrollView中嵌套 GridView 导致 ScrollView默认不停留在顶部的问题
		lv_homepage.setFocusable(false);
	}

	public void onDestroyView() {
		super.onDestroyView();
	};

	@Override
	public void onResume() {
		if (this.isHidden()==false) {
			handler.sendEmptyMessage(GET_DATA);
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.i(TAG, "onPause()");
		super.onPause();
	}

	@Override
	public void onDestroy() {
		banner_main_accordion.stopAutoPlay();
		super.onDestroy();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	protected void initMember() {
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
		itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		ImageLoader.getInstance().displayImage(model, itemView);
	}

	@Override
	public void onBannerItemClick(BGABanner banner, ImageView itemView,
			String model, int position) {
		Intent intent = new Intent();
		intent.putExtra("id", imagelist.get(position).getId());
		AbsUI.startUI(context, HomePageDetailsUI.class, intent);
	}

}
