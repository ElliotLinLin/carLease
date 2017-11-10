package com.hst.Carlease.ui.fragement;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.adapter.CommonAdapter;
import com.hst.Carlease.adapter.CommonViewHolder;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.CarBandBean;
import com.hst.Carlease.http.bean.HomePageBean;
import com.hst.Carlease.http.bean.SecondCarBean;
import com.hst.Carlease.http.bean.SecondCarBean.SecondCar;
import com.hst.Carlease.http.bean.SecondCarBean1;
import com.hst.Carlease.pulltorefresh.PullToRefreshLayout;
import com.hst.Carlease.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.MyMessageUI;
import com.hst.Carlease.ui.SecondCarDetailsUI;
import com.hst.Carlease.util.LocationChangedUtils;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.mylistview.SwipeMenuListView.IXListViewListener;
import com.hst.Carlease.widget.mywidget.MyListView;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tools.app.AbsFgm2;
import com.tools.app.AbsUI;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.util.Log;
import com.tools.widget.PopupWindowExt;
import com.tools.widget.Prompt;

public class OnLineBookingFgm extends AbsFgm2 implements IXListViewListener {
	private final String TAG = OnLineBookingFgm.class.getSimpleName();
	public View rootView; // 根布局
	private LinearLayout btn_right;// 右上角的消息按钮
	private TextView tv_pinpai, tv_price, tv_melige;// 品牌，价格，里程
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> list2 = new ArrayList<String>() {
		{
			add("价格");
			add("<10万");
			add("10--15万");
			add("15--20万");
			add("20--25万");
			add("25--35万");
			add(">35万");
		}
	};
	ArrayList<String> list3 = new ArrayList<String>() {
		{
			add("车龄");
			add("<1年");
			add("1-3年");
			add("3-5年");
			add("5-8年");
			add(">8年");
		}
	};
	private List<CarBandBean> lisBandBeans;
	private PopupWindow mAddFrindsPopup;
	private CommonAdapter<String> adapter2;
	private PullToRefreshLayout refresh_view;
	private TextView btn_left;// 定位
	private TextView no_data;
	int currpage = 1;// 当前页
	int oldpage = 1;// 上一页
	private LocationChangedUtils utils;// 定位
	boolean isfirst = true;// 是否刚加载
	boolean refresh = false;// 刷新
	boolean load_more = false;// 加载更多
	private MyListView lv_homepage;
	private CommonAdapter<SecondCarBean.SecondCar> adapter;
	private LinkedList<SecondCarBean.SecondCar> SecondCarlistdata = new LinkedList<SecondCarBean.SecondCar>();
	private final int SET_DATE = 0;
	private final int CAR_BAND = 1;
	private final int LIST_DATA = 2;
	private Intent intent;
	private String CheckString = "";
	private ImageView image1, image2, image3;
	private int carposition=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.ui_booking_fgm, null);
		return rootView;
	}

	@Override
	public void onResume() {
		utils.startOnce();
		super.onResume();
	}

	@Override
	public void onPause() {
		utils.stop();
		super.onPause();
	}

	@Override
	protected void initControl() {
		btn_right = (LinearLayout) rootView.findViewById(R.id.btn_right);
		tv_pinpai = (TextView) rootView.findViewById(R.id.tv_pinpai);
		tv_price = (TextView) rootView.findViewById(R.id.tv_price);
		tv_melige = (TextView) rootView.findViewById(R.id.tv_melige);
		lv_homepage = (MyListView) rootView.findViewById(R.id.lv_homepage);
		no_data = (TextView) rootView.findViewById(R.id.no_data);
		btn_left = (TextView) rootView.findViewById(R.id.tv_address);

		image1 = (ImageView) rootView.findViewById(R.id.image1);
		image2 = (ImageView) rootView.findViewById(R.id.image2);
		image3 = (ImageView) rootView.findViewById(R.id.image3);
		refresh_view = (PullToRefreshLayout) rootView
				.findViewById(R.id.refresh_view);
		utils = new LocationChangedUtils(context, ui);
		btn_left.setText(utils.cityname);
		intent = new Intent();
		hander.sendEmptyMessage(LIST_DATA);
		list.add("品牌");
		hander.sendEmptyMessage(CAR_BAND);
	}

	@Override
	protected void initControlEvent() {
		tv_pinpai.setOnClickListener(listener);
		tv_price.setOnClickListener(listener);
		tv_melige.setOnClickListener(listener);
		btn_right.setOnClickListener(listener);
		lv_homepage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SecondCar SecondCar = (com.hst.Carlease.http.bean.SecondCarBean.SecondCar) arg0
						.getAdapter().getItem(arg2);
				intent.putExtra("CarID", SecondCar.getUcID());
				AbsUI.startUI(context, SecondCarDetailsUI.class, intent);
			}
		});
		refresh_view.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				refresh = true;// 刷新
				load_more = false;
				currpage = 1;
				oldpage = currpage;
				hander.sendEmptyMessage(LIST_DATA);
			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				load_more = true;// 表示加载更多
				refresh = false;
				hander.sendEmptyMessage(LIST_DATA);

			}
		});
		no_data.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				hander.sendEmptyMessage(LIST_DATA);
			}
		});
	}

	View.OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.tv_pinpai:
				if (list.size() == 0) {
					return;
				}
				CheckString = tv_pinpai.getText().toString();
				ShowAddFrindsPopup(list, 0);
				setImageBg(image1, true);
				break;
			case R.id.tv_price:
				CheckString = tv_price.getText().toString();
				ShowAddFrindsPopup(list2, 1);
				setImageBg(image2, true);
				break;
			case R.id.tv_melige:
				CheckString = tv_melige.getText().toString();
				ShowAddFrindsPopup(list3, 2);
				setImageBg(image3, true);
				break;
			case R.id.btn_right:
				AbsUI.startUI(context, MyMessageUI.class);
				break;
			default:
				break;
			}

		}
	};

	Handler hander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LIST_DATA:
				GetPagingListTask();
				break;
			case SET_DATE:
				Log.i(TAG, "setAdapter");
				setAdapter();
				break;
			case CAR_BAND:
				GetCarBrand();
				break;
			}
		};
	};

	private void setAdapter() {

		adapter = new CommonAdapter<SecondCar>(context, SecondCarlistdata,
				R.layout.item_second_fgm) {

			@Override
			public void fillItemData(final CommonViewHolder holder,
					final SecondCar data, int position) {
				ImageView img_view = holder.getView(R.id.img_view);
				TextView carname = holder.getView(R.id.carname);
				TextView caraddress = holder.getView(R.id.caraddress);
				TextView carmodel = holder.getView(R.id.carmodel);
				TextView time = holder.getView(R.id.time);
				TextView price = holder.getView(R.id.price);
				TextView count = holder.getView(R.id.count);
				carname.setText(data.getBrandName());
				caraddress.setText(data.getRegionName());
				carmodel.setText(data.getCarModelName());
				time.setText(data.getCardDate() + "/" + data.getMileage()
						+ "公里");
				price.setText(data.getDisposable() + "万元");
				if (position == SecondCarlistdata.size() - 1) {
					count.setText("共有" + SecondCarlistdata.size() + "台车源信息");
					count.setVisibility(View.VISIBLE);
				} else {
					count.setVisibility(View.GONE);
				}

				String imgUrl = "";
				if (data.getCarUsedUrl().size() == 0) {
				} else {
					imgUrl = data.getCarUsedUrl().get(0);
				}
				ImageLoader.getInstance().displayImage(imgUrl, img_view);
			}

		};
		lv_homepage.setAdapter(adapter);
	}

	@Override
	protected void initMember() {
	}

	@Override
	protected void onStartLoader() {

	}

	public void onDestroyView() {
		super.onDestroyView();
	};

	public void onDestroy() {
		super.onDestroy();
	};

	@Override
	protected byte[] doInBackgroundLoader() {
		return null;
	}

	// 找回密码的任务
	private void GetPagingListTask() {
		NetworkState state = new NetworkState(context);
		if (state.isConnected() == false) {
			ToastL.show("请检查您的网络");
			if (refresh) {
				// 无网络刷新失败
				refresh_view.refreshFinish(PullToRefreshLayout.FAIL);
			} else if (load_more) {
				refresh_view.loadmoreFinish(PullToRefreshLayout.FAIL);
			}
			currpage = oldpage;

			return;
		}
		SecondCarBean1 bean = new SecondCarBean1();
		bean.setPageindex(currpage);
		bean.setPagesize(10);
		bean.setMaxPrice(0);
		bean.setMaxYear(0);
		bean.setMinPrice(0);
		bean.setMinYear(0);
		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());
		if (tv_pinpai.getText().toString().equals("品牌")) {
			bean.setBrandID(0);
		} else {
			for (int i = 0; i < lisBandBeans.size(); i++) {
				if (tv_pinpai.getText().toString()
						.equals(lisBandBeans.get(i).getC_DictName())) {
					bean.setBrandID(lisBandBeans.get(i).getC_DictID());
				}
			}
		}
		if (tv_price.getText().toString().contains("价格")) {
			bean.setMaxPrice(0.0);
			bean.setMinPrice(0.0);
		} else if (tv_price.getText().toString().contains("<")) {
			bean.setMaxPrice(100000);
			bean.setMinPrice(0.0);
		} else if (tv_price.getText().toString().contains(">")) {
			bean.setMaxPrice(0.0);
			bean.setMinPrice(350000);
		} else {
			String string = tv_price.getText().toString();
			String minprice = string.substring(0, 2);
			String maxprice = string.substring(4, string.length() - 1);
			double min = Double.parseDouble(minprice);
			double max = Double.parseDouble(maxprice);
			bean.setMinPrice(min * 10000);
			bean.setMaxPrice(max * 10000);
		}
		// // 车龄赋值
		if (tv_melige.getText().toString().contains("车龄")) {
			bean.setMaxYear(0);
			bean.setMinYear(0);
		} else if (tv_melige.getText().toString().contains("<")) {
			bean.setMaxYear(1);
			bean.setMinYear(0);
		} else if (tv_melige.getText().toString().contains(">")) {
			bean.setMaxYear(0);
			bean.setMinYear(8);
		} else if (tv_melige.getText().toString().contains("1-3年")) {
			bean.setMaxYear(3);
			bean.setMinYear(1);
		} else if (tv_melige.getText().toString().contains("3-5年")) {
			bean.setMaxYear(5);
			bean.setMinYear(3);
		} else {
			bean.setMaxYear(8);
			bean.setMinYear(5);
		}
		try {
			AsyncHttpUtil.post(context, Http_Url.GetCarUsedList, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							lv_homepage) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
							if (refresh) {
								// 无网络刷新失败
								refresh_view
										.refreshFinish(PullToRefreshLayout.FAIL);
							} else if (load_more) {
								refresh_view
										.loadmoreFinish(PullToRefreshLayout.FAIL);
							}
							currpage = oldpage;
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String result) {
							Bean bean = GJson.parseObject(result, Bean.class);
							Log.i(TAG, "result" + bean.getD());
							if (bean.getD() != null) {
								SecondCarBean beans = GJson.parseObject(
										bean.getD(), SecondCarBean.class);

								if (beans.getModel() == null && currpage == 1) {
									no_data.setVisibility(View.VISIBLE);
									lv_homepage.setVisibility(View.GONE);
									refresh_view.setVisibility(View.GONE);
									return;
								}
								if (beans.getModel() == null
										&& SecondCarlistdata.size() == 0
										&& currpage != 1) {
									no_data.setVisibility(View.VISIBLE);
									lv_homepage.setVisibility(View.GONE);
									refresh_view.setVisibility(View.GONE);
									return;
								}
								if (beans.getStatu() == 1) {
									if (beans.getModel() != null) {
										no_data.setVisibility(View.GONE);
										lv_homepage.setVisibility(View.VISIBLE);
										refresh_view
												.setVisibility(View.VISIBLE);
										if (currpage > 1) {
											if (refresh) {
												// 刷新成功
												refresh_view
														.refreshFinish(PullToRefreshLayout.SUCCEED);
											} else if (load_more) {
												// // 加载成功
												refresh_view
														.loadmoreFinish(PullToRefreshLayout.SUCCEED);
											}
											if (beans.getModel().size() > 0
													&& beans.getModel() != null) {
												oldpage = currpage;
												currpage++;
												SecondCarlistdata.addAll(beans
														.getModel());
											} else {
												ToastL.show("没有更多数据了");
											}
											adapter.notifyDataSetChanged();
										} else {
											if (refresh) {
												// 刷新成功
												refresh_view
														.refreshFinish(PullToRefreshLayout.SUCCEED);
											} else if (load_more) {
												// 加载成功
												refresh_view
														.loadmoreFinish(PullToRefreshLayout.SUCCEED);
											}
											oldpage = currpage;
											currpage++;
											SecondCarlistdata = beans
													.getModel();
											hander.sendEmptyMessage(SET_DATE);
										}
									} else {
										if (refresh) {
											// 刷新成功
											refresh_view
													.refreshFinish(PullToRefreshLayout.SUCCEED);
										} else if (load_more) {
											// 加载成功
											refresh_view
													.loadmoreFinish(PullToRefreshLayout.SUCCEED);
										}
									}
								} else if (beans.getStatu() == -2) {
									ToastL.show(beans.getMsg());
									StringUtils.IsOUTOFtime(context,
											OnLineBookingFgm.this.ui);
								} else {
									ToastL.show(beans.getMsg());
								}

							}
						}

					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private void ShowAddFrindsPopup(List<String> poplist, final int type) {
		View view = View.inflate(context, R.layout.pop_setect, null);
		mAddFrindsPopup = new PopupWindowExt(view,
				WindowManager.LayoutParams.FILL_PARENT, 800);
		mAddFrindsPopup
				.setOnDismissListener(new PopupWindow.OnDismissListener() {

					@Override
					public void onDismiss() {
						setImageBg(image1, false);
						setImageBg(image2, false);
						setImageBg(image3, false);
					}
				});                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
		// 控件
		ListView mTvAdd = (ListView) view.findViewById(R.id.pop_listview);
		// 添加好友
		adapter2 = new CommonAdapter<String>(context, poplist,
				R.layout.ui_pop_select) {
			@Override
			public void fillItemData(CommonViewHolder holder, String data,
					int position) {
				TextView textView = holder.getView(R.id.tv_pop);
				ImageView checked = holder.getView(R.id.checked);
				if (CheckString.equals(data)) {
					checked.setVisibility(View.VISIBLE);
				} else {
					checked.setVisibility(View.INVISIBLE);
				}
				textView.setText(data);
			}
		};
		mTvAdd.setAdapter(adapter2);
		if (type==0) {
		mTvAdd.setSelection(carposition);	
		}
		mTvAdd.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (type == 0) {
					tv_pinpai.setText(parent.getAdapter().getItem(position)
							.toString());
					setImageBg(image1, false);
					carposition=position;
				} else if (type == 1) {
					tv_price.setText(parent.getAdapter().getItem(position)
							.toString());
					setImageBg(image2, false);
				} else {
					tv_melige.setText(parent.getAdapter().getItem(position)
							.toString());
					setImageBg(image3, false);
				}
				CheckString = parent.getAdapter().getItem(position).toString();
				adapter2.notifyDataSetChanged();
				currpage = 1;
				hander.sendEmptyMessage(LIST_DATA);
				tv_melige.postDelayed(new Runnable() {
					@Override
					public void run() {
						closeAddFrindsPopup();
					}
				}, 300);

			}
		});
		showAddFrindsPopup();
	}

	private void setImageBg(ImageView view, boolean change) {
		if (change) {
			view.setBackgroundResource(R.drawable.expanded);
		} else {
			view.setBackgroundResource(R.drawable.collapsed);
		}
	}

	// 显示添加的window
	protected void showAddFrindsPopup() {
		if (mAddFrindsPopup != null) {
			if (!mAddFrindsPopup.isShowing()) {
				mAddFrindsPopup.showAsDropDown(tv_pinpai, 0, 0);
			} else {
				closeAddFrindsPopup();
			}
		}
	}

	// 关闭查找的window
	protected void closeAddFrindsPopup() {
		if (mAddFrindsPopup != null) {
			if (mAddFrindsPopup.isShowing()) {
				// 显示在titilebar的下面
				mAddFrindsPopup.dismiss();
			}
		}
	}

	// 获取品牌的任务
	private void GetCarBrand() {
		HomePageBean bean = new HomePageBean();
		NetworkState state = new NetworkState(context);
		if (state.isConnected() == false) {
			Prompt.showToast(context, "网络异常");
			return;
		}
		try {
			AsyncHttpUtil.post(context, Http_Url.GetDictList, bean,
					"application/json", new AsyncCallBackHandler(ui, "", false,
							btn_right) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {

						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Bean bean = GJson.parseObject(arg2, Bean.class);
							if (bean.getD() != null) {
								lisBandBeans = GJson.parseList(bean.getD(),
										CarBandBean.class);
								if (lisBandBeans != null) {
									for (int i = 0; i < lisBandBeans.size(); i++) {
										list.add(lisBandBeans.get(i)
												.getC_DictName());
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
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

	}

	@Override
	public void onRefresh() {
		currpage = 1;
		hander.sendEmptyMessage(LIST_DATA);
	}

	@Override
	public void onLoadMore() {
		currpage++;
		hander.sendEmptyMessage(LIST_DATA);
	}
}
