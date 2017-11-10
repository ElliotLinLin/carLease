package com.hst.Carlease.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.adapter.CommonAdapter;
import com.hst.Carlease.adapter.CommonViewHolder;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.eventBean.MainStype;
import com.hst.Carlease.eventBean.Stype;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.GetOrderList2Bean;
import com.hst.Carlease.http.bean.GetOrderList2Bean.OrderList;
import com.hst.Carlease.http.bean.GetOrderListBean;
import com.hst.Carlease.http.bean.PeopleBean;
import com.hst.Carlease.http.bean.RegisterBean;
import com.hst.Carlease.http.bean.ZMCertBean;
import com.hst.Carlease.http.bean.ZMCertRequestBean;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.fragement.MainUI2;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.app.AlertDialog;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.util.Log;
import com.tools.widget.Prompt;
import com.zmxy.ZMCertification;
import com.zmxy.ZMCertificationListener;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 我的订单
 * 
 * @author lyq
 * 
 */
public class MyOrderUI extends AbsUI2 implements ZMCertificationListener {
	private final String TAG = MyOrderUI.class.getSimpleName();
	private TitleBar titlebar;
	private SwipeRefreshLayout swiprl_myorder;//下拉刷新
	private ListView lv_myorder;// 我的订单的列表
	private TextView tv_myorder_nodata;// 没有数据
	private CommonAdapter<OrderList> adapter;// 适配器
	private LinearLayout linea_sum;//
	private List<OrderList> list;
	private final int LIST_INFO = 1;
	private final int GET_DAT = 0;
	private final int GET_UP = 2;
	private ZMCertification zmCertification;
	private int tid;
	private boolean isface;
	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {

			finish();
			return;
		}
		setContentView(R.layout.ui_myorder);
		EventBus.getDefault().register(this);
		setSlideFinishEnabled(false);
		super.onCreate(arg0);

	}

	@Override
	protected void initControl() {
		titlebar = new TitleBar();
		zmCertification = ZMCertification.getInstance();
		lv_myorder = (ListView) findViewById(R.id.lv_myorder);
		linea_sum = (LinearLayout) findViewById(R.id.linea_sum);
		linea_sum.setVisibility(View.GONE);
		tv_myorder_nodata = (TextView) findViewById(R.id.tv_myorder_nodata);
		tv_myorder_nodata.setText("没有订单");
		swiprl_myorder=(SwipeRefreshLayout)findViewById(R.id.swiprl_myorder);

	}

	@Override
	protected void initControlEvent() {
		lv_myorder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				OrderList OrderList = (com.hst.Carlease.http.bean.GetOrderList2Bean.OrderList) parent
						.getAdapter().getItem(position);
				Intent intent = new Intent();
				intent.putExtra("ID", OrderList.getID());
				AbsUI.startUI(context, MyOrderDetailsUI.class, intent);

			}
		});
		swiprl_myorder.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		swiprl_myorder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				loadData();
			}
		});
//		swiprl_myorder.setRefreshing(false);
	}

	private void loadData(){
		isFromFresh=true;
		hand.sendEmptyMessage(GET_DAT);
	}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titleBar_myorder, titlebar);
	}

	@Override
	public void onAttachedToWindow() {
		titlebar.setTitle("我的订单");
		titlebar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AbsUI2.startClearTopUI(context, MainUI2.class);
				lv_myorder.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						EventBus.getDefault().post(new MainStype(Constant.Main_mine));
					}
				}, 100);
			}
		});
		super.onAttachedToWindow();
	}

	/**
	 * 我的订单列表
	 */
	boolean isFromFresh=false;
	protected void GetOrderList() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		GetOrderListBean bean = new GetOrderListBean();
		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());
		if (isFromFresh){
			swiprl_myorder.setRefreshing(false);
		}
		try {
			AsyncHttpUtil.post(ui, Http_Url.GetOrderList, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							tv_myorder_nodata) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
							// tv_myorder_nodata.setVisibility(View.VISIBLE);
							// lv_myorder.setVisibility(View.GONE);
							isFromFresh=false;
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							isFromFresh=false;
							Bean bean = GJson.parseObject(arg2, Bean.class);
							Log.i(TAG, "result 截取" + bean.getD());
							if (bean.getD() != null) {
								GetOrderList2Bean beans = GJson.parseObject(
										bean.getD(), GetOrderList2Bean.class);
								if (beans != null) {
									if (beans.getStatu() == 1) {
										list = beans.getModel();
										if (list != null) {
											hand.sendEmptyMessage(LIST_INFO);  //如果有数据  就去填充数据
										} else {
											tv_myorder_nodata
													.setVisibility(View.VISIBLE);
											lv_myorder.setVisibility(View.GONE);
										}
									} else if (beans.getStatu() == -2) {
										ToastL.show(beans.getMsg());
										StringUtils.IsOUTOFtime(context,
												MyOrderUI.this.ui);
									} else {
										ToastL.show(beans.getMsg());
									}
								}

							}

						}

					});

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LIST_INFO:
				setAdapter();
				break;
			case GET_DAT:
				GetOrderList();//重新请求数据
				break;
			case GET_UP:
				ReturnFaceCertification();//返回认证结果
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 获取人脸识别参数
	 */
	protected void GetBiznoList(String name, String certionNo, TextView tv) {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		ZMCertBean bean = new ZMCertBean();
		bean.setCertno(certionNo);
		bean.setName(name);
//		String url = "http://172.16.0.199:8056/Interface/Zmxy.asmx/Bizno";// Http_Url.Bizno
		try {
			AsyncHttpUtil.post(ui, Http_Url.Bizno, bean, "application/json",
					new AsyncCallBackHandler(ui, "", true, tv) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							if (arg2 == null) {
								return;
							}
							Bean bean = GJson.parseObject(arg2, Bean.class);
							if (bean != null) {
								Log.i(TAG, "result===" + bean.getD());
								PeopleBean PayBean2 = GJson.parseObject(
										bean.getD(), PeopleBean.class);
								if (PayBean2 != null) {
									if (PayBean2.getStatu() == 1) {
										if (PayBean2.getModel() == null) {
											return;
										}
										zmCertification
												.setZMCertificationListener(MyOrderUI.this);
										zmCertification.startCertification(
												MyOrderUI.this, PayBean2
														.getModel().getBizno(),
												PayBean2.getModel()
														.getMerchant_id(),

												null);
									} else {
										ToastL.show(PayBean2.getMsg());
										return;
									}

								}

							}

						}

					});

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

	}

	// 取消订单的任务
	private void CancelOrder(int hpid, final TextView cacle_order,
			final View line) {
		GetOrderListBean bean = new GetOrderListBean();
		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());
		bean.setHpid(hpid);
		try {
			AsyncHttpUtil.post(ui, Http_Url.CancelOrder, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							cacle_order) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Bean bean = GJson.parseObject(arg2, Bean.class);
							Log.i(TAG, "result 截取" + bean.getD());
							if (bean.getD() != null) {
								RegisterBean benaBean = GJson.parseObject(
										bean.getD(), RegisterBean.class);
								if (benaBean == null) {
									return;
								}
								if (benaBean.getStatu() == 1) {
									ToastL.show("订单已取消");
									hand.sendEmptyMessage(GET_DAT);
									cacle_order.setVisibility(View.GONE);
									line.setVisibility(View.GONE);
								} else if (benaBean.getStatu() == -2) {
									ToastL.show(benaBean.getMsg());
									StringUtils.IsOUTOFtime(context,
											MyOrderUI.this.ui);
								} else {
									cacle_order.setVisibility(View.VISIBLE);
									line.setVisibility(View.VISIBLE);
									ToastL.show("订单取消失败");
								}
							}

						}

					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 给listview设置数据
	 */

	private void setAdapter() {
		adapter = new CommonAdapter<OrderList>(ui, list, R.layout.ui_order_item) {

			@Override
			public void fillItemData(CommonViewHolder holder,
					final OrderList data, int position) {
				ImageView iView = holder.getView(R.id.iamge);
				TextView name = holder.getView(R.id.name);
				TextView username = holder.getView(R.id.username);
				TextView tv_orderno = holder.getView(R.id.tv_orderno);
				TextView order_status = holder.getView(R.id.order_status);
				TextView tv_firstpay = holder.getView(R.id.tv_firstpay);
				TextView zhidao = holder.getView(R.id.zhidao);
				final View line = holder.getView(R.id.line);
				final TextView cacle_order = holder.getView(R.id.cacle_order);
				TextView order_order = holder.getView(R.id.order_order);// 去付款
				String imgUrl = "";
				if (data.getCarModelUrl() != null
						&& data.getCarModelUrl().size() != 0) {
					imgUrl = data.getCarModelUrl().get(0);
				}
				ImageLoader.getInstance().displayImage(imgUrl, iView);
				zhidao.setText("指导价：" + data.getMarketPrice() + "元");
				if (data.getOrderStatus().equals("预订")
						|| data.getOrderStatus().equals("待审批")
						|| data.getOrderStatus().equals("审批通过")
						|| data.getOrderStatus().equals("批复中")
						|| data.getOrderStatus().equals("待生成合同")) {
					cacle_order.setVisibility(View.VISIBLE);
					line.setVisibility(View.VISIBLE);
				} else {
					cacle_order.setVisibility(View.GONE);
					line.setVisibility(View.GONE);
				}
				if (data.getOrderStatus().equals("待审批")
						|| data.getOrderStatus().equals("审批通过")
						|| data.getOrderStatus().equals("批复中")
						|| data.getOrderStatus().equals("待生成合同")) {
					order_order.setVisibility(View.VISIBLE);
				} else {
					order_order.setVisibility(View.GONE);
				}
				if (data.isNeedFace1() || data.isNeedFace2()) {
					holder.getView(R.id.tv_order_face).setVisibility(View.VISIBLE);
//					cacle_order.setText("人脸识别认证");
					line.setVisibility(View.VISIBLE);
				} else {
					holder.getView(R.id.tv_order_face).setVisibility(View.GONE);
				}
				order_status.setText(data.getOrderStatus());
				tv_orderno.setText("订单号:" + data.getOrderNo());
				name.setText(data.getBrandName());
				username.setText("车型：" + data.getCarModelName().trim());// +data.getBrandName()
				tv_firstpay.setText(Html
						.fromHtml("首付：<font color=\"#FFA90C\"> "
								+ data.getFrtPay() + "</font>元"));
				order_order.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("orderNo", data.getOrderNo());
						intent.putExtra("dataFrom", Constant.ORDER_PAY2);
						intent.putExtra("body", "订单支付");
						intent.putExtra("contactid", 0);
						AbsUI.startUI(context, OrderPayUI.class, intent);
					}
				});
				holder.getView(R.id.tv_order_face).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (data.isNeedFace1() || data.isNeedFace2()) {
							if (data.getCredentialsNum() != null && data.getCst_name() != null) {
								tid = data.getID();
								GetBiznoList(data.getCst_name(),
										data.getCredentialsNum(), cacle_order);
							} else {
								ToastL.show("姓名或身份证号码为空");
								return;
							}
							if (data.isNeedFace1()) {
								MyOrderUI.this.isface = true;
							} else {
								MyOrderUI.this.isface = false;
							}
						}
					}
				});

				cacle_order.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

							AlertDialog.Builder builder = new AlertDialog.Builder(
									ui);
							builder.setTitle(" ");
							builder.setMessage("您确定取消订单吗？");

							builder.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											CancelOrder(data.getID(),
													cacle_order, line);
										}

									})
									.setNegativeButton(
											"取消",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
												}

											}).create().show();

					}
				});
			}
		};
		lv_myorder.setAdapter(adapter);
		tv_myorder_nodata.setVisibility(View.GONE);

	}





	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Subscribe
	public void onEventMainThread(Stype ev) {
		if (ev.getMsg() == Constant.ORDER_PAY2) {
			hand.sendEmptyMessage(GET_DAT);
		}
		
	}
	@Override
	protected void onResume() {
		hand.sendEmptyMessage(GET_DAT);
		super.onResume();
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
	public void onFinish(boolean isCanceled, boolean isPassed, int errorCode) {
		zmCertification.setZMCertificationListener(null);
		Log.i(TAG, "错误原因：" + errorCode);
		if (isCanceled)
		Log.i(TAG, "芝麻验证失败," + errorCode);
		else {
			if (isPassed) { //认证完成
				hand.sendEmptyMessage(GET_UP);
				hand.sendEmptyMessage(GET_DAT); //把数据告诉后台
			} 
//			else if (errorCode == 1)
//				ToastL.show(" 芝麻验证失败,用户人脸与数据库中的人脸比对分数较低");
//			else if (errorCode == 2)
//				ToastL.show(" 芝麻验证失败,手机在不支持列表里");
//			else if (errorCode == 3)
//				ToastL.show(" 芝麻验证失败,缺少手机权限 ");
//			else if (errorCode == 4)
//				ToastL.show(" 芝麻验证失败,没有联网权限 ");
//			else if (errorCode == 5)
//				ToastL.show(" 芝麻验证失败,没有打开相机的权限 ");
//			else if (errorCode == 6)
//				ToastL.show(" 芝麻验证失败,无法读取运动数据的权限 ");
//			else if (errorCode == 7)
//				ToastL.show(" 芝麻验证失败,人脸采集算法初始化失败 ");
//			else if (errorCode == 8)
//				ToastL.show(" 芝麻验证失败,发生网络错误");
//			else if (errorCode == 9)
//				ToastL.show(" 芝麻验证失败,传入的bizNO 有误");
//			else if (errorCode == 10)
//				ToastL.show(" 芝麻验证失败,此APP的bundle_id在系统的黑名单库里");
//			else if (errorCode == 11)
//				ToastL.show(" 芝麻验证失败,数据源错误");
//			else if (errorCode == 12)
//				ToastL.show(" 芝麻验证失败,服务发生内部错误");
//			else if (errorCode == 13)
//				ToastL.show(" 芝麻验证失败,bizNO和merchantID不匹配");
//			else if (errorCode == 14)
//				ToastL.show(" 芝麻验证失败,SDK版本过旧");
//			else if (errorCode == 15)
//				ToastL.show(" 芝麻验证失败,身份证号和姓名的格式不正确");
//			else if (errorCode == 16)
//				ToastL.show(" 芝麻验证失败,身份证号和姓名在一天内使用次数过多");
//			else
//				ToastL.show(" 芝麻验证失败," + errorCode);
		}
	}

	/**
	 * 把认证结果告诉后台
	 */
	protected void ReturnFaceCertification() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "无网络连接");
			return;
		}
		ZMCertRequestBean bean = new ZMCertRequestBean();
		bean.setIspass(true);
		bean.setTid(tid);
		bean.setTokenID((String) SPUtils.get(MyOrderUI.this, Constants.tokenID,""));
		try {
			String url;

			if (isface) {
				url = Http_Url.RETURNFACECERTIFICATIONFIRST;
			} else {
				url = Http_Url.RETURNFACECERTIFICATIONSECOND;
			}

			AsyncHttpUtil.post(ui, url, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							lv_myorder) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							if (arg2 == null) {
								return;
							}
							Bean bean = GJson.parseObject(arg2, Bean.class);
							if (bean != null) {
								Log.i(TAG, "result===" + bean.getD());
								RegisterBean PayBean2 = GJson.parseObject(
										bean.getD(), RegisterBean.class);
								if (PayBean2 != null) {
									if (PayBean2.getStatu() == 1) {
//										ToastL.show(PayBean2.getMsg());
										hand.sendEmptyMessage(GET_DAT);
									} else {
										ToastL.show(PayBean2.getMsg());
										return;
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
	public void onBackPressed() {
		super.onBackPressed();
		AbsUI2.startClearTopUI(context, MainUI2.class);
		lv_myorder.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				EventBus.getDefault().post(new MainStype(Constant.Main_mine));
			}
		}, 100);
		
	}
}
