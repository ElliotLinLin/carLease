package com.hst.Carlease.ui.fragement;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.util.Log;
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
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.CarBandBean;
import com.hst.Carlease.http.bean.HomePageBean;
import com.hst.Carlease.http.bean.HotCarBean;
import com.hst.Carlease.pulltorefresh.PullToRefreshLayout;
import com.hst.Carlease.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.HotModelDetailsUI;
import com.hst.Carlease.ui.MyMessageUI;
import com.hst.Carlease.widget.mylistview.SwipeMenuListView.IXListViewListener;
import com.hst.Carlease.widget.mywidget.MyListView;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tools.app.AbsFgm2;
import com.tools.app.AbsUI;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.widget.PopupWindowExt;
import com.tools.widget.Prompt;

public class HotModelFgm extends AbsFgm2 implements IXListViewListener {
	private final String TAG = HotModelFgm.class.getSimpleName();
	public View rootView; // 根布局
	private TextView tv_pinpai, tv_price, tv_melige;// 品牌，价格，里程
	private LinearLayout btn_right;// 消息
	private CommonAdapter<HotCarBean.PageDetails> adapter;
	private ArrayList<HotCarBean.PageDetails> PageDetails;// 数据集合
	private MyListView lv_homepage;
	private SimpleDateFormat format;//
	private PullToRefreshLayout refresh_view;
	int currpage = 1;// 当前页
	int oldpage = 1;// 上一页
	private final int SET_DATE = 0;
	private final int CAR_BAND = 1;
	private final int LIST_DATA = 2;
	boolean isfirst = true;// 是否刚加载
	boolean refresh = false;// 刷新
	boolean load_more = false;// 加载更多
	private List<CarBandBean> lisBandBeans;
	private TextView no_data;// 没有数据
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<String> list2 = new ArrayList<String>() {
		{
			add("价格");
			add("<20万");
			add("20—50万");
			add("50—80万");
			add("80—100万");
			add(">100万");
		}
	};
	ArrayList<String> list3 = new ArrayList<String>() {
		{
			add("里程(公里)");
			add("<2万");
			add("2—5万");
			add("5—10万");
			add("10—20万");
			add(">20万");
		}
	};
	private PopupWindow mAddFrindsPopup;
	private CommonAdapter<String> adapter2;
	private String CheckString = "";
	private ImageView image1, image2, image3;
	private int carposition=0;
	

	// private com.hst.Carlease.widget.mywidget.MySelectPop MySelectPop;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.ui_hotcar_fgm, null);
		return rootView;
	}

	@Override
	protected void initControl() {
		tv_pinpai = (TextView) rootView.findViewById(R.id.tv_pinpai);
		tv_price = (TextView) rootView.findViewById(R.id.tv_price);
		tv_melige = (TextView) rootView.findViewById(R.id.tv_melige);
		btn_right = (LinearLayout) rootView.findViewById(R.id.btn_right);
		lv_homepage = (MyListView) rootView.findViewById(R.id.lv_homepage);
		refresh_view = (PullToRefreshLayout) rootView
				.findViewById(R.id.refresh_view);
		no_data = (TextView) rootView.findViewById(R.id.no_data);
		image1 = (ImageView) rootView.findViewById(R.id.image1);
		image2 = (ImageView) rootView.findViewById(R.id.image2);
		image3 = (ImageView) rootView.findViewById(R.id.image3);
		format = new SimpleDateFormat("yyyy-MM-dd");
		hander.sendEmptyMessage(LIST_DATA);
		list.add("品牌");
		hander.sendEmptyMessage(CAR_BAND);
		no_data.setText("没有找到车型套餐");
	}

	@Override
	protected void initControlEvent() {
		tv_pinpai.setOnClickListener(listener);
		tv_price.setOnClickListener(listener);
		tv_melige.setOnClickListener(listener);
		btn_right.setOnClickListener(listener);
		lv_homepage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HotCarBean.PageDetails PageDetails = (HotCarBean.PageDetails) parent
						.getAdapter().getItem(position);
				Intent intent = new Intent();
				intent.putExtra("id", PageDetails.getHcm_ID());
				AbsUI.startUI(context, HotModelDetailsUI.class, intent);
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

	Handler hander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LIST_DATA:
				GetPagingListTask();
				break;
			case SET_DATE:
				setAdapter();
				break;
			case CAR_BAND:
				GetCarBrand();
				break;
			}
		};
	};

	private void setImageBg(ImageView view, boolean change) {
		if (change) {
			view.setBackgroundResource(R.drawable.expanded);
		} else {
			view.setBackgroundResource(R.drawable.collapsed);
		}
	}

	// 找回密码的任务
	private void GetPagingListTask() {
		NetworkState state = new NetworkState(context);
		if (state.isConnected() == false) {
			Prompt.showWarning(context, "请检查您的网络");
			if (refresh) {
				// 无网络刷新失败
				refresh_view.refreshFinish(PullToRefreshLayout.FAIL);
			} else if (load_more) {
				refresh_view.loadmoreFinish(PullToRefreshLayout.FAIL);
			}
			currpage = oldpage;

			return;
		}
		HomePageBean bean = new HomePageBean();
		bean.setPageSize(10);
		bean.setPageIndex(currpage);
		if (tv_pinpai.getText().toString().equals("品牌")) {
			bean.setDicID(0);
		} else {
			for (int i = 0; i < lisBandBeans.size(); i++) {
				if (tv_pinpai.getText().toString()
						.equals(lisBandBeans.get(i).getC_DictName())) {
					bean.setDicID(lisBandBeans.get(i).getC_DictID());
				}
			}
		}
		if (tv_price.getText().toString().contains("价格")) {
			bean.setMaxPrice(0.0);
			bean.setMinPrice(0.0);
		} else if (tv_price.getText().toString().contains("<")) {
			bean.setMaxPrice(200000);
			bean.setMinPrice(0.0);
		} else if (tv_price.getText().toString().contains(">")) {
			bean.setMaxPrice(0.0);
			bean.setMinPrice(1000000);
		} else {
			String string = tv_price.getText().toString();
			String minprice = string.substring(0, 2);
			String maxprice = string.substring(3, string.length() - 1);
			double min = Double.parseDouble(minprice);
			double max = Double.parseDouble(maxprice);
			bean.setMinPrice(min * 10000);
			bean.setMaxPrice(max * 10000);
			Log.i(TAG, "price--max--" + max);
			Log.i(TAG, "price--min--" + min);
		}
		// 里程赋值
		if (tv_melige.getText().toString().contains("里程")) {
			bean.setMaxMile(0);
			bean.setMinMile(0);
		} else if (tv_melige.getText().toString().contains("<")) {
			bean.setMaxMile(20000);
			bean.setMinMile(0);
		} else if (tv_melige.getText().toString().contains(">")) {
			bean.setMaxMile(0);
			bean.setMinMile(200000);
		} else if (tv_melige.getText().toString().contains("10—20万")) {
			bean.setMaxMile(200000);
			bean.setMinMile(100000);
		} else if (tv_melige.getText().toString().contains("2—5万")) {
			bean.setMaxMile(50000);
			bean.setMinMile(20000);
		} else {
			bean.setMaxMile(50000);
			bean.setMinMile(100000);
		}
		try {
			AsyncHttpUtil.post(context, Http_Url.GetPagingLists, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							btn_right) {

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
							Log.i(TAG, "qqqq" + result);
							Bean bean = GJson.parseObject(result, Bean.class);
							if (bean.getD() != null) {
								HotCarBean beans = GJson.parseObject(
										bean.getD(), HotCarBean.class);
								if ((beans == null || beans.getDataList()
										.size() == 0) && currpage == 1) {
									no_data.setVisibility(View.VISIBLE);
									lv_homepage.setVisibility(View.GONE);
									refresh_view.setVisibility(View.GONE);
									return;
								}
								if (beans.getDataList() != null) {
									no_data.setVisibility(View.GONE);
									lv_homepage.setVisibility(View.VISIBLE);
									refresh_view.setVisibility(View.VISIBLE);
									if (currpage > 1) {
										if (refresh) {
											// 刷新成功
											refresh_view
													.refreshFinish(PullToRefreshLayout.SUCCEED);
										} else if (load_more) {
											// 加载成功
											refresh_view
													.loadmoreFinish(PullToRefreshLayout.SUCCEED);
										}
										if (beans.getDataList().size() > 0
												&& beans.getDataList() != null) {
											oldpage = currpage;
											currpage++;
											PageDetails.addAll(beans
													.getDataList());
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
										PageDetails = beans.getDataList();
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

							}
						}

					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	// 给适配器赋值
	private void setAdapter() {
		adapter = new CommonAdapter<HotCarBean.PageDetails>(context,
				PageDetails, R.layout.ui_item_hotcar) {

			@Override
			public void fillItemData(CommonViewHolder holder,
					com.hst.Carlease.http.bean.HotCarBean.PageDetails data,
					int position) {
				ImageView image = holder.getView(R.id.imge);
				TextView name = holder.getView(R.id.name);
				TextView time = holder.getView(R.id.time);
				TextView address = holder.getView(R.id.address);
				TextView pay = holder.getView(R.id.pay);
				name.setText(data.getCarModerName());
				time.setText(string2string(data.getHcm_Crt_Date()));
				address.setText(data.getMaddress());
				pay.setText("首付" + data.getMpayment() + "元");
				String imgUrl = "";
				if (data.getCarModelUrl().size() == 0) {
				} else {
					imgUrl = data.getCarModelUrl().get(0);

				}
				ImageLoader.getInstance().displayImage(imgUrl, image);
			}
		};
		lv_homepage.setAdapter(adapter);
	}

	@Override
	protected void initMember() {
	}

	private String string2string(String string) {
		String result = "";
		Date date = null;
		try {
			date = format.parse(string);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		result = sdf1.format(date);
		return result;
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
							Log.i(TAG, "result===" + bean.getD());
							Log.i(TAG, "qqq" + arg2);
							if (bean.getD() != null) {
								lisBandBeans = GJson.parseList(bean.getD(),
										CarBandBean.class);
								for (int i = 0; i < lisBandBeans.size(); i++) {
									list.add(lisBandBeans.get(i)
											.getC_DictName());
								}
							}
						}

					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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

	// 显示弹框
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
				textView.setText(data);
				ImageView checked = holder.getView(R.id.checked);
				if (CheckString.contains("里程")) {
					CheckString = "里程(公里)";
				}
				if (CheckString.equals(data)) {
					position=position;
					checked.setVisibility(View.VISIBLE);
				} else {
					checked.setVisibility(View.INVISIBLE);
				}
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
		Log.i(TAG, "closeAddFrindsPopup");
		if (mAddFrindsPopup != null) {
			if (mAddFrindsPopup.isShowing()) {
				// 显示在titilebar的下面
				mAddFrindsPopup.dismiss();
			}
		}
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
