package com.hst.Carlease.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.Loader;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.asynchttp.BaseCallBack;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.eventBean.Stype;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.CommonBean;
import com.hst.Carlease.http.bean.NewInsertOrderBean;
import com.hst.Carlease.http.bean.OnlyIntModelBean;
import com.hst.Carlease.http.bean.PurchaseParamsBean;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.thirdperiodui.ThirdBaseUi;
import com.hst.Carlease.util.BitmapUtils;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.util.Log;
import com.tools.util.UIUtils;
import com.tools.widget.PopZx;
import com.tools.widget.Prompt;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 完善资料
 * 
 * @author wzy
 * 
 */
public class OnlineBookingUI3Old extends ThirdBaseUi {
	private static final String TAG = OnlineBookingUI3Old.class.getSimpleName();
	private TitleBar titleBar = null;// 标题
	private Button btn_perfectdata_submit;
	private Intent intent;
	private static final int request_pic1 = 1; // 图片1请求码
	private static final int request_pic2 = 2; // 图片2请求码
	private static final int request_pic3 = 3; // 图片3请求码
	private static final int request_pic4 = 4; // 图片4请求码
	private static final int request_pic5 = 5; // 图片5请求码

	private ImageView mActPic1; // 图片1
	private ImageView mActPic2; // 图片2
	private ImageView mActPic3; // 图片3
	private ImageView mActPic4; // 图片4
	private ImageView mActPic5; // 图片5
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;

	public static String ImgPath1 = ""; // 图片路径
	public static String ImgPath2 = "";
	public static String ImgPath3 = "";
	public static String ImgPath4 = "";
	public static String ImgPath5 = "";
	private TextView mTvPic1;// 图片1上传状态
	private TextView mTvPic2;// 图片2
	private TextView mTvPic3;// 图片3
	private TextView mTvPic4;// 图片3
	private TextView mTvPic5;// 图片3
	private PopZx pop;// 征信弹框
	private CheckBox checked;
	private boolean isToast = false;
	private boolean isCansubmit=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {

			finish();
			return;
		}
		setContentView(R.layout.ui_onlinebooking3_old);
		EventBus.getDefault().register(this);
		super.setSlideFinishEnabled(false);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		titleBar = new TitleBar();
		intent = getIntent();

		mActPic1 = (ImageView) findViewById(R.id.image1);
		mActPic2 = (ImageView) findViewById(R.id.image2);
		mActPic3 = (ImageView) findViewById(R.id.image3);
		mActPic4 = (ImageView) findViewById(R.id.image4);
		mActPic5 = (ImageView) findViewById(R.id.image5);
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn4 = (Button) findViewById(R.id.btn4);
		btn5 = (Button) findViewById(R.id.btn5);
		mTvPic1 = (TextView) findViewById(R.id.tv1);
		mTvPic2 = (TextView) findViewById(R.id.tv2);
		mTvPic3 = (TextView) findViewById(R.id.tv3);
		mTvPic4 = (TextView) findViewById(R.id.tv4);
		mTvPic5 = (TextView) findViewById(R.id.tv5);
		checked = (CheckBox) findViewById(R.id.checked);
		btn1.setOnClickListener(listener);
		btn2.setOnClickListener(listener);
		btn3.setOnClickListener(listener);
		btn4.setOnClickListener(listener);
		btn5.setOnClickListener(listener);
		btn_perfectdata_submit = (Button) findViewById(R.id.btn_register);
		btn_perfectdata_submit.setClickable(false);



	}

	@Override
	protected void onDestroy() {
		ImageLoader.getInstance().clearMemoryCache();
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Subscribe
	public void onEventMainThread(Stype ev) {
		if (ev.getMsg() == 1) {
			isToast = true;
			btn_perfectdata_submit
					.setBackgroundResource(R.drawable.shape_btn_login);
		}
	}

	/**
	 * 选择图片 图片相册
	 */
	public void startActionPickCrop() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		ui.startActivityForResult(intent, Constant.REQUEST_IMAGE_BY_SDCARD);
	}

	@Override
	protected void initControlEvent() {
		checked.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					btn_perfectdata_submit.postDelayed(new Runnable() {
						@Override
						public void run() {
							pop = new PopZx(context, btn5);
							pop.show();
						}
					}, 100);
				}
			}
		});
		btn_perfectdata_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if (isEmptyString(ImgPath1)) {
				// ToastL.show("请上传身份证正面照");
				// return;
				// }
				// if (isEmptyString(ImgPath2)) {
				// ToastL.show("请上传您的身份证背面照");
				// return;
				// }
				// if (isEmptyString(ImgPath3)) {
				// ToastL.show("请上传您的驾驶证");
				// return;
				// }
				// if (isEmptyString(ImgPath4)) {
				// ToastL.show("请上传银行流水图片");
				// return;
				// }
				// if (isEmptyString(ImgPath5)) {
				// ToastL.show("请上传您的最近三个月的通话记录");
				// return;
				// }
				if (checked.isChecked() == false && isToast) {
					ToastL.show("请同意授权个人征信查询");
					return;
				} else if (checked.isChecked() == false && isToast == false) {
					return;
				}

				if (isCansubmit) {
					isCansubmit=false;
					GetHirePurchaseDetailsHDID();
//					GetHirePurchaseDetails();
				}

			}
		});

	}

	private void GetHirePurchaseDetails() {

		PurchaseParamsBean purchaseParamsBean = new PurchaseParamsBean();
		purchaseParamsBean.setHcmID(intent.getIntExtra("id", 0));
		purchaseParamsBean.setCarModelID(intent.getIntExtra("CarMdoelID", 0));
		purchaseParamsBean.setCarModelName(intent.getStringExtra("name"));
		purchaseParamsBean.setCustomerName(intent.getStringExtra("carname"));
		purchaseParamsBean.setCredentialsNum(intent.getStringExtra("cardno"));
		purchaseParamsBean.setMobilePhone(intent.getStringExtra("mine_phone"));
		purchaseParamsBean.setCompanyId(intent.getIntExtra("CompanyID", 0));
		purchaseParamsBean.setComAddress(intent.getStringExtra("comAddress"));
		purchaseParamsBean.setLiveAreaID(intent.getIntExtra("liveAreaID", 0));
		purchaseParamsBean.setLiveAddress(intent.getStringExtra("liveAddress"));
		purchaseParamsBean.setMaritalStatus(intent.getIntExtra("spinner", 1));
		purchaseParamsBean.setEmergyName(intent.getStringExtra("emergyName"));
		purchaseParamsBean.setEmergMobile(intent.getStringExtra("emergyName"));
		purchaseParamsBean.setEmeryRelation(intent.getIntExtra("emeryRelation", 0));
		purchaseParamsBean.setImmFamName(intent.getStringExtra("family"));
		purchaseParamsBean.setImmFamMobile(intent.getStringExtra("fa_phone"));
		purchaseParamsBean.setImmFamRelation(intent.getStringExtra("contanct"));
		purchaseParamsBean.setBankId(intent.getIntExtra("bankId", 0));
		purchaseParamsBean.setAccountName(intent.getStringExtra("AccountName"));
		purchaseParamsBean.setBankNum(intent.getStringExtra("bankNum"));
		purchaseParamsBean.setBranchName(intent.getStringExtra("branchName"));
		purchaseParamsBean.setDriveFirstDate(intent.getStringExtra("DriveFirstDate"));


		RequestParams requestParams = new RequestParams();
		requestParams.add("Source",2+"");
		requestParams.add("tokenID",SPUtils.get(context, Constants.tokenID, "").toString());
		requestParams.add("OpenId","");
		String value = new Gson().toJson(purchaseParamsBean);
		requestParams.add("PurchaseParams", value);
		requestParams.add("file1",getBase64String4Loader(mActPic1));
		requestParams.add("file2",getBase64String4Loader(mActPic2));
		requestParams.add("driveAttach",getBase64String4Loader(mActPic3));
		requestParams.add("SixBankAttach",getBase64String4Loader(mActPic4));
		requestParams.add("ThreePhoneAttach",getBase64String4Loader(mActPic5));



		sendRequest(Http_Url.INSERTPURCHSE, requestParams, OnlyIntModelBean.class, new BaseCallBack<OnlyIntModelBean>() {
			@Override
			public void onSuccess(OnlyIntModelBean onlyIntModelBean) {
				showToast(onlyIntModelBean.getMsg());
			}

			@Override
			public void onFailure(String arg2, Throwable arg3) {
				super.onFailure(arg2, arg3);
				showToast("请求失败");
			}
		});


	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn1) {
				BitmapUtils.getPic(OnlineBookingUI3Old.this, request_pic1,
						mActPic1);
			} else if (v.getId() == R.id.btn2) {
				BitmapUtils.getPic(OnlineBookingUI3Old.this, request_pic2,
						mActPic2);
			} else if (v.getId() == R.id.btn3) {
				BitmapUtils.getPic(OnlineBookingUI3Old.this, request_pic3,
						mActPic3);
			} else if (v.getId() == R.id.btn4) {
				BitmapUtils.getPic(OnlineBookingUI3Old.this, request_pic4,
						mActPic4);
			} else if (v.getId() == R.id.btn5) {
				BitmapUtils.getPic(OnlineBookingUI3Old.this, request_pic5,
						mActPic5);
			}
		}
	};

	/**
	 * 创建图片数组
	 *
	 * @return
	 */
	private String getBase64String(Bitmap bitmap) {
		// 将path转成byte[]
		byte[] bytes = BitmapUtils.Bitmap2Bytes(bitmap);
		Log.e(TAG, "bitmap-SIZE:" + bytes.length);
		// 转成Base64编码
		return Base64.encodeToString(bytes, 0);
	}

	private void GetHirePurchaseDetailsHDID() {
		NetworkState state = new NetworkState(context);
		if (state.isConnected() == false) {
			Prompt.showWarning(context, "请检查您的网络");
			return;
		}
        PurchaseParamsBean purchaseParamsBean = new PurchaseParamsBean();
        purchaseParamsBean.setHcmID(intent.getIntExtra("id", 0));
        purchaseParamsBean.setCarModelID(intent.getIntExtra("CarMdoelID", 0));
        purchaseParamsBean.setCarModelName(intent.getStringExtra("name"));
        purchaseParamsBean.setCustomerName(intent.getStringExtra("carname"));
        purchaseParamsBean.setCredentialsNum(intent.getStringExtra("cardno"));
        purchaseParamsBean.setMobilePhone(intent.getStringExtra("mine_phone"));
        purchaseParamsBean.setCompanyId(intent.getIntExtra("CompanyID", 0));
        purchaseParamsBean.setComAddress(intent.getStringExtra("comAddress"));
        purchaseParamsBean.setLiveAreaID(intent.getIntExtra("liveAreaID", 0));
        purchaseParamsBean.setLiveAddress(intent.getStringExtra("liveAddress"));
        purchaseParamsBean.setMaritalStatus(intent.getIntExtra("spinner", 0));
        purchaseParamsBean.setEmergyName(intent.getStringExtra("emergyName"));
        purchaseParamsBean.setEmergMobile(intent.getStringExtra("emergMobile"));
        purchaseParamsBean.setEmeryRelation(intent.getIntExtra("emeryRelation", 0));
        purchaseParamsBean.setImmFamName(intent.getStringExtra("family"));
        purchaseParamsBean.setImmFamMobile(intent.getStringExtra("fa_phone"));
        purchaseParamsBean.setImmFamRelation(intent.getStringExtra("contanct"));
        purchaseParamsBean.setBankId(intent.getIntExtra("bankId", 0));
        purchaseParamsBean.setAccountName(intent.getStringExtra("AccountName"));
        purchaseParamsBean.setBankNum(intent.getStringExtra("bankNum"));
        purchaseParamsBean.setBranchName(intent.getStringExtra("branchName"));
        purchaseParamsBean.setDriveFirstDate(intent.getStringExtra("DriveFirstDate"));



        NewInsertOrderBean bean = new NewInsertOrderBean();

//		InsertOrderBean bean = new InsertOrderBean();
//		bean.setCarModelID(intent.getIntExtra("CarMdoelID", 0) + "");
//		bean.setCarModelName(intent.getStringExtra("name").trim());
//		bean.setCredentialsNum(intent.getStringExtra("cardno"));
//		bean.setCustName(intent.getStringExtra("carname"));
//		bean.setImmFamMobile(intent.getStringExtra("fa_phone"));
//		bean.setImmFamName(intent.getStringExtra("family"));
//		bean.setImmFamRelation(intent.getStringExtra("contanct"));
//		bean.setMobile(intent.getStringExtra("mine_phone"));
//		bean.setOpenId("");
//		bean.setSource(2 + "");
//		bean.setBusName(intent.getStringExtra("jingbanren"));
//		bean.setCompanyid(intent.getIntExtra("CompanyID", -1) + "");
//		bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());
//		bean.setMaritalStatus(intent.getStringExtra("spinner"));
//		bean.setCarArea(intent.getStringExtra("getcar"));
//		bean.setOrderArea(intent.getStringExtra("mine_address"));
//
        bean.setSource(2);
        bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());
        bean.setOpenId("");
        String value = new Gson().toJson(purchaseParamsBean);
        bean.setPurchaseParams(value);
		bean.setFile1(getBase64String4Loader(mActPic1));
		bean.setFile2(getBase64String4Loader(mActPic2));
		bean.setDriveAttach(getBase64String4Loader(mActPic3));
		bean.setSixBankAttach(getBase64String4Loader(mActPic4));
		bean.setThreePhoneAttach(getBase64String4Loader(mActPic5));
//		bean.setHcmID(intent.getIntExtra("id", 0) + "");
		Log.e(TAG, "HcmID=====" + intent.getIntExtra("id", 0));
		try {
			AsyncHttpUtil.post(ui, Http_Url.INSERTPURCHSE, bean,
					"application/json", new AsyncCallBackHandler(ui, "正在提交...",
							true, btn_perfectdata_submit) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
							Log.e(TAG, "arg3" + arg3);
							Log.e(TAG, "arg2" + arg2);
							isCansubmit=true;
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String result) {
							Bean bean = GJson.parseObject(result, Bean.class);
							if (bean.getD() != null) {
								Log.i(TAG, "result==" + bean.getD());
								CommonBean CommonBean = GJson.parseObject(
										bean.getD(), CommonBean.class);
								// 1：注册成功，其他：注册失败
								if (CommonBean != null) {
									if (CommonBean.getStatu() == 1) {
										ToastL.show("预订成功");
										AbsUI.startUI(context, MyOrderUI.class);
										stopUI(ui);
									} else if (CommonBean.getStatu() == -2) {
										ToastL.show(CommonBean.getMsg());
										StringUtils.IsOUTOFtime(context,
												OnlineBookingUI3Old.this.ui);
									} else {
										isCansubmit=true;
										ToastL.show(CommonBean.getMsg());
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
	protected void initMember() {
		super.addFgm(R.id.titlebar, titleBar);
		super.setSlideFinishEnabled(false); // 设置不可左右滑动退出
	}

	@Override
	public void onAttachedToWindow() {
		titleBar.setTitle("在线预订");
		titleBar.getLeftView(1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		super.onAttachedToWindow();
	}

	/**
	 * 设置控件背景图
	 * 
	 * @param image
	 * @param bm
	 */
	private boolean setImageBackground(ImageView image, Bitmap bm) {
		boolean result = false;
		if (image != null && bm != null) {
			image.setImageBitmap(bm);
			result = true;
		}
		Log.e(TAG, "setImageBackground():result:" + result);
		return result;
	}

	/**
	 * 创建图片数组---通过ImageLoader
	 * 
	 * @return
	 */
	private String getBase64String4Loader(ImageView image) {
		Bitmap bitmap = null;
		Object tag = image.getTag();
		if (tag == null) {
			return "";
		}
		if (tag instanceof Uri) {// 取出本地uri
			Uri uri = (Uri) tag;
			Log.e(TAG, "uri:" + uri.toString());
			bitmap = getimage(BitmapUtils.getImageAbsolutePath(
					OnlineBookingUI3Old.this, uri));
		} else {
			// 取出绝对路径（从路径里解析出文件类型）
			String path = (String) tag;
			Log.e(TAG, "path:" + path);
			bitmap = getimage(path);
		}
		byte[] bytes = compressImage(bitmap);
		Log.e(TAG, bytes.length + "");
		String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
		// 回收
		bitmap.recycle();
		Log.e(TAG, "getBase64String4Loader回收了bitmap");
		return base64;
	}

	/*
	 * 图片比例压缩
	 */
	private Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		int degree = BitmapUtils.readPictureDegree(srcPath);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		bitmap = BitmapUtils.rotaingImageView(degree, bitmap);
		// 压缩好比例大小后再进行质量压缩
		return bitmap;// 压缩好比例大小后再进行质量压缩
	}

	/*
	 * 将图片进行质量压缩的方法
	 */
	private byte[] compressImage(Bitmap image) {
		if (image == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 60, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}

		return baos.toByteArray();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 选择图片
		if (resultCode == RESULT_OK)// -1
		{
			String path = "";
			Uri uri = data.getData();
			path = BitmapUtils.getImageAbsolutePath(OnlineBookingUI3Old.this, uri);

			Log.e(TAG, "uri:" + uri);

			if (uri == null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					Bitmap photo = (Bitmap) bundle.get("data"); // get
					path = UIUtils.getFilesPath(ui) + "/"
							+ System.currentTimeMillis() + ".jpg";
					// spath :生成图片取个名字和路径包含类型
					BitmapUtils.saveImage(photo, path);
				} else {
					ToastL.show("找不到图片");
					return;
				}
			}

			switch (requestCode) {
			case request_pic1:// 图片1
				mTvPic1.setVisibility(View.GONE);
				getAndUpdatePic(mActPic1, mTvPic1, path, true, 0);
				ImgPath1 = path;
				break;
			case request_pic2:// 图片2
				mTvPic2.setVisibility(View.GONE);
				getAndUpdatePic(mActPic2, mTvPic2, path, true, 1);
				ImgPath2 = path;
				break;
			case request_pic3:// 图片3
				mTvPic3.setVisibility(View.GONE);
				getAndUpdatePic(mActPic3, mTvPic3, path, true, 2);
				ImgPath3 = path;
				break;
			case request_pic4:// 图片4
				mTvPic4.setVisibility(View.GONE);
				getAndUpdatePic(mActPic4, mTvPic4, path, true, 3);
				ImgPath4 = path;
				break;
			case request_pic5:// 图片5
				mTvPic5.setVisibility(View.GONE);
				getAndUpdatePic(mActPic5, mTvPic5, path, true, 4);
				ImgPath5 = path;
				break;
			default:
				break;
			}
		}
	}

	// 获取-保存-上传图片
	private void getAndUpdatePic(ImageView picView, TextView stateView,
			String path, boolean isLocatFile, int index) {
		picView.setTag(path);
		Bitmap bitmap = BitmapUtils.getImageThumbnail(path,
				picView.getMeasuredWidth(), picView.getMeasuredHeight());
		picView.setImageBitmap(bitmap);
	}

	/*
	 * 将Bitmap转换为byte【】数组
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		if (bm == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] array = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			baos = null;
			e.printStackTrace();
		}
		return array;
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
