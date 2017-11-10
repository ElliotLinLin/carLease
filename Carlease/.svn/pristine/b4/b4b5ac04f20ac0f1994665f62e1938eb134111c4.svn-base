package com.hst.Carlease.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.BaseCallBack;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.eventBean.Stype;
import com.hst.Carlease.http.bean.OnlyIntModelBean;
import com.hst.Carlease.http.bean.PurchaseParamsBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.thirdperiodui.ThirdBaseUi;
import com.hst.Carlease.util.BitmapUtils;
import com.hst.Carlease.util.CommonRecyclerAdapter;
import com.hst.Carlease.util.CommonViewHolder;
import com.hst.Carlease.util.CompressUtil;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;
import com.tools.util.Log;
import com.tools.widget.CreateLoadingView;
import com.tools.widget.PopZx;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumListener;
import com.yanzhenjie.album.api.widget.Widget;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 完善资料
 *
 * @author wzy
 */
public class OnlineBookingUI3 extends ThirdBaseUi implements OnClickListener {
    private static final String TAG = OnlineBookingUI3.class.getSimpleName();
    private TitleBar titleBar = null;// 标题
    private Button btn_perfectdata_submit;
    private Intent intent;
    private PopZx pop;// 征信弹框
    private CheckBox checked;
    private boolean isToast = false;
    private boolean isCansubmit = true;
    private RecyclerView mRv;
    private LinkedList<AlbumFile> datas = new LinkedList<>();
    private CommonRecyclerAdapter<AlbumFile> mAdapter;
    private ProgressDialog progressDialog;
    private LinearLayout ll_check;
    private TextView tv_prompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.ui_onlinebooking3);
        EventBus.getDefault().register(this);
        super.setSlideFinishEnabled(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initControl() {
        titleBar = new TitleBar();
        intent = getIntent();
        checked = (CheckBox) findViewById(R.id.checked);
        btn_perfectdata_submit = (Button) findViewById(R.id.btn_register);
        btn_perfectdata_submit.setEnabled(false);
        mRv = (RecyclerView) findViewById(R.id.rv);
        ll_check = (LinearLayout) findViewById(R.id.ll_check);
        tv_prompt = (TextView) findViewById(R.id.tv_prompt);
        ll_check.setOnClickListener(this);
        initRv();
    }

    /**
     * 初始化recycleview 设置适配器 已经条目点击事件
     */
    private void initRv() {
        datas.add(new AlbumFile());

        mAdapter = new CommonRecyclerAdapter<AlbumFile>(this, datas, R.layout.book_rv_item) {
            @Override
            public void convert(CommonViewHolder holder, AlbumFile albumFile, int i, boolean b) {
                ImageView iv_icon = holder.getView(R.id.iv_icon);
                ViewGroup.LayoutParams layoutParams = iv_icon.getLayoutParams();
                layoutParams.width = mWm.getDefaultDisplay().getWidth() / 4 - 8;
                layoutParams.height = mWm.getDefaultDisplay().getWidth() / 4 - 8;
                iv_icon.setLayoutParams(layoutParams);


                if (TextUtils.isEmpty(albumFile.getPath())) {
                    holder.getView(R.id.iv_dele).setVisibility(View.GONE);
                    iv_icon.setScaleType(ImageView.ScaleType.FIT_XY);
                    (iv_icon).setImageResource(R.drawable.camera);
                } else {
                    iv_icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if (TextUtils.isEmpty(albumFile.getThumbPath())) {
                        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(albumFile.getPath()), iv_icon);
                    } else {
                        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(albumFile.getThumbPath()), iv_icon);
                    }
//                    Album.getAlbumConfig().
//                            getAlbumLoader().loadAlbumFile(iv_icon,albumFile,iv_icon.getWidth(),iv_icon.getWidth());
                    holder.getView(R.id.iv_dele).setVisibility(View.VISIBLE);
                }
                holder.getView(R.id.iv_dele).setTag(i);
                holder.getView(R.id.iv_dele).setOnClickListener(OnlineBookingUI3.this);

            }
        };

        mRv.setLayoutManager(new GridLayoutManager(this, 4));
        mRv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClickListener(CommonViewHolder holder, int i) {
                if (TextUtils.isEmpty(mAdapter.getDatas().get(i).getPath())) {
                    Album.image(OnlineBookingUI3.this) // 选择图片。
                            .multipleChoice()
                            .requestCode(200 )
                            .camera(false)
                            .columnCount(3)
                            .widget(getWight())
                            .selectCount(20-mAdapter.getDatas().size() +1)
                            .listener(new AlbumListener<ArrayList<AlbumFile>>() {
                                @Override
                                public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                                    for (AlbumFile albumFile : result) {
                                        Log.w(TAG, albumFile.getPath() + "");
                                        if (mAdapter.getDatas().size() < 20) {
                                            mAdapter.addAt(mAdapter.getDatas().size() - 1, albumFile);
                                            mAdapter.notifyItemInserted(mAdapter.getDatas().size() - 2);
                                        } else if (mAdapter.getDatas().size() == 20) {
                                            mAdapter.addAt(mAdapter.getDatas().size() - 1, albumFile);
                                            mAdapter.notifyItemInserted(mAdapter.getDatas().size() - 2);
                                            for (AlbumFile file : mAdapter.getDatas()) {
                                                if (TextUtils.isEmpty(file.getPath())) {
                                                    mAdapter.remove(file);
                                                }
                                            }
                                        } else {
                                            showToast("最多可以添加20张照片");
                                            for (AlbumFile file : mAdapter.getDatas()) {
                                                if (TextUtils.isEmpty(file.getPath())) {
                                                    mAdapter.remove(file);
                                                }
                                            }
                                            break;

                                        }
                                    }
                                    showHideText();
                                }

                                @Override
                                public void onAlbumCancel(int requestCode) {
                                }
                            })
                            .start();
                }
            }
        });
    }

    /**
     * 更改图片选择器的title
     * @return
     */
    public Widget getWight() {
        return Widget.newDarkBuilder(context)
                .statusBarColor(ContextCompat.getColor(context, com.yanzhenjie.album.R.color.album_ColorPrimaryDark))
                .toolBarColor(ContextCompat.getColor(context, com.yanzhenjie.album.R.color.album_ColorPrimary))
                .navigationBarColor(ContextCompat.getColor(context, com.yanzhenjie.album.R.color.album_ColorPrimaryBlack))
                .title("照片列表")
                .mediaItemCheckSelector(ContextCompat.getColor(context, com.yanzhenjie.album.R.color.album_WhiteGray),
                        ContextCompat.getColor(context, com.yanzhenjie.album.R.color.album_ColorPrimary))
                .bucketItemCheckSelector(ContextCompat.getColor(context, com.yanzhenjie.album.R.color.album_WhiteGray),
                        ContextCompat.getColor(context, com.yanzhenjie.album.R.color.album_ColorPrimary))
                .buttonStyle(
                        Widget.ButtonStyle.newDarkBuilder(context)
                                .setButtonSelector(ContextCompat.getColor(context, com.yanzhenjie.album.R.color.album_ColorPrimary),
                                        ContextCompat.getColor(context, com.yanzhenjie.album.R.color.album_ColorPrimaryDark))
                                .build()
                )
                .build();

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
            btn_perfectdata_submit.setEnabled(true);
        }
    }

    /**
     * 初始化征询按钮
     */
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
                            pop = new PopZx(context, checked);
                            pop.show();
                        }
                    }, 100);
                }
            }
        });
        btn_perfectdata_submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (checked.isChecked() == false && isToast) {
                    ToastL.show("请同意授权个人征信查询");
                    return;
                } else if (checked.isChecked() == false && isToast == false) {
                    return;
                }

//                if (isCansubmit) {
//                    isCansubmit = false;
                GetHirePurchaseDetails();
//                }

            }
        });

    }

    /**
     * 进行网络请求
     */
    private void GetHirePurchaseDetails() {
        showProgressDiag();
        new MyTask().execute();
    }


    /**
     * 关闭对话框
     */
    protected void closeDialog() {
        if (progressDialog != null && progressDialog.isShowing() && ui != null
                && !ui.isFinishing()) {
            progressDialog.dismiss();
        }
    }

    public void showProgressDiag() {
        // 进度
        progressDialog = new ProgressDialog(ui);

        // 是否允许关闭
        progressDialog.setCancelable(true);

        // 点击允许点击外部关闭对话框？
        progressDialog.setCanceledOnTouchOutside(false);


        // 显示对话框
        progressDialog.show();
        progressDialog.setContentView(CreateLoadingView.createView(ui, ""));
    }

    /**
     * 创建图片数组---通过ImageLoader
     *
     * @return
     */
    private String getBase64String4Loader(Object tag) {
        Bitmap bitmap = null;
        if (tag instanceof Uri) {// 取出本地uri
            Uri uri = (Uri) tag;
            Log.e(TAG, "uri:" + uri.toString());
            bitmap = CompressUtil.getimage(BitmapUtils.getImageAbsolutePath(
                    OnlineBookingUI3.this, uri));
        } else {
            // 取出绝对路径（从路径里解析出文件类型）
            String path = (String) tag;
            Log.e(TAG, "path:" + path);
            bitmap = CompressUtil.getimage(path);
        }
        byte[] bytes = CompressUtil.compressImage(bitmap);
        Log.e(TAG, bytes.length + "");
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        // 回收
        bitmap.recycle();
        Log.e(TAG, "getBase64String4Loader回收了bitmap");
        return base64;
    }

    @Override
    protected void initMember() {
        super.addFgm(R.id.titlebar, titleBar);
        super.setSlideFinishEnabled(false); // 设置不可左右滑动退出
    }

    @Override
    public void onAttachedToWindow() {
        titleBar.setTitle("在线预订");
        titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        super.onAttachedToWindow();
    }

    /**
     * 将所有图片转为base64集合
     *
     * @return
     */
    public ArrayList<String> getAllBase64Img() {
        ArrayList<String> arr = new ArrayList<>();
        for (AlbumFile file : mAdapter.getDatas()) {
            if (!TextUtils.isEmpty(file.getPath())) {
                arr.add(getBase64String4Loader(file.getPath()));
            }
        }
        if (arr.size() == 0) {
            return null;
        } else {
            return arr;
//            return new Gson().toJson(arr);
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

    public boolean containAddImag() {
        for (AlbumFile file : mAdapter.getDatas()) {
            if (TextUtils.isEmpty(file.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dele:
                int tag = (int) v.getTag();
                AlbumFile file = mAdapter.getDatas().get(tag);
                if (!TextUtils.isEmpty(file.getPath())) {
                    mAdapter.removeWithOutNotify(file);
                    mAdapter.notifyItemRemoved(tag);
                    mAdapter.notifyItemRangeChanged(tag, mAdapter.getDatas().size() - 1);
                }
                if (mAdapter.getDatas().size() < 20 && !containAddImag()) {
                    mAdapter.add(new AlbumFile());
                }
                showHideText();
                break;
            case R.id.ll_check:
                checked.setChecked(!checked.isChecked());
                break;
        }
    }

    /**
     * 动态设置背景提示textview的显示隐藏
     */
    public void showHideText() {
        tv_prompt.setVisibility(mAdapter.getDatas().size() > 1 ? View.GONE : View.VISIBLE);
    }

    /**
     * 将处理数据和网络请求放在不同线程
     */
    private class MyTask extends AsyncTask<Object, Object, RequestParams> {

        @Override
        protected RequestParams doInBackground(Object... params) {

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
            purchaseParamsBean.setCredentialsAddr(intent.getStringExtra("cardloc"));
//            purchaseParamsBean.setPurchaseFiles(getAllBase64Img());


            RequestParams requestParams = new RequestParams();
            requestParams.add("Source", 2 + "");
            requestParams.add("tokenID", SPUtils.get(context, Constants.tokenID, "").toString());
            requestParams.add("OpenId", "");
            String value = new Gson().toJson(purchaseParamsBean);
            requestParams.add("PurchaseParams", value);
            for (int i = 0; i < mAdapter.getDatas().size(); i++) {
                try {
                    String path = mAdapter.getDatas().get(i).getPath();
                    if (!TextUtils.isEmpty(path)) {
                        requestParams.put("files" + i, CompressUtil.getCompressPic(path, getCacheDir().getPath()));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            return requestParams;
        }

        @Override
        protected void onPostExecute(RequestParams requestParams) {
            sendRequest(Http_Url.InsertHirePurchase, requestParams, OnlyIntModelBean.class, "", false, btn_perfectdata_submit, new BaseCallBack<OnlyIntModelBean>() {
                @Override
                public void onSuccess(OnlyIntModelBean onlyIntModelBean) {
                    if (onlyIntModelBean != null) {
                        if (onlyIntModelBean.getStatu() == 1) {
                            ToastL.show("预订成功");
                            AbsUI.startUI(context, MyOrderUI.class);
                            mAdapter.getDatas().clear();
                            mAdapter = null;
                            mRv = null;
                            stopUI(ui);
                        } else if (onlyIntModelBean.getStatu() == -2) {
                            ToastL.show(onlyIntModelBean.getMsg());
//                            StringUtils.IsOUTOFtime(context, OnlineBookingUI3.this.ui);
                        } else {
                            ToastL.show(onlyIntModelBean.getMsg());
                        }
                    }

                }

                @Override
                public void onFinish() {
                    closeDialog();
                }

                @Override
                public void onFailure(String arg2, Throwable arg3) {
                    super.onFailure(arg2, arg3);
                    showToast("请求失败");
                }
            });
        }
    }

}
