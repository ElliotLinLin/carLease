package com.hst.Carlease.ui.thirdperiodui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.BaseCallBack;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.http.bean.GetOutOrInType;
import com.hst.Carlease.http.bean.OnlyIntModelBean;
import com.hst.Carlease.http.bean.OutStorageBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.LoginUI;
import com.hst.Carlease.util.CommonRecyclerAdapter;
import com.hst.Carlease.util.CommonViewHolder;
import com.hst.Carlease.util.CompressUtil;
import com.hst.Carlease.util.SPUtils;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;
import com.tools.widget.CreateLoadingView;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumListener;
import com.yanzhenjie.album.api.ImageCameraWrapper;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/8/24
 */

public class OutStoragePictureActivity extends ThirdBaseUi implements View.OnClickListener {

    private static final String TAG = "OutStoragePictureActivity";
    private TitleBar titleBar;
    private ImageCameraWrapper mCameraWrapper;
    private TextView mTv_hetong;
    private TextView mTv_ku, tv_commit;
    private List<GetOutOrInType> mOutOrInTypes;
    private String mCarid;
    private String mCompanyid;
    private RecyclerView rv;
    private CommonRecyclerAdapter<OutStorageBean> mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle arg0) {
        setContentView(R.layout.activity_outstorage_picture);
        super.onCreate(arg0);

        mCarid = getIntent().getStringExtra("carid");
        mCompanyid = getIntent().getStringExtra("id");

    }

    @Override
    protected void initControl() {
        titleBar = new TitleBar();
        mTv_hetong = (TextView) findViewById(R.id.tv_hetong);
        mTv_ku = (TextView) findViewById(R.id.tv_ku);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        rv = (RecyclerView) findViewById(R.id.rv);

        mTv_hetong.setOnClickListener(this);
        mTv_ku.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
        mCameraWrapper = Album.camera(OutStoragePictureActivity.this)
                .image()
                .listener(albumListener);

        initRv();
    }

    private void initRv() {
        List<OutStorageBean> datas = getOutBeans();
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerAdapter = new CommonRecyclerAdapter<OutStorageBean>(this, datas, R.layout.outstorage_item) {
            @Override
            public void convert(CommonViewHolder helper, OutStorageBean item, int position, boolean itemChanged) {
                if (item.getImagePath().length() < 3) {
                    ((ImageView) helper.getView(R.id.iv)).setImageResource(getIvResources(item.getImagePath()));
                } else {
                    ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(item.getImagePath()),(ImageView) helper.getView(R.id.iv));
                }
                helper.setText(R.id.tv_title, item.getTitle());
                helper.getView(R.id.btn_1).setTag(position);
                helper.getView(R.id.btn_1).setOnClickListener(OutStoragePictureActivity.this);
            }
        };
        rv.setAdapter(mRecyclerAdapter);
    }

    private int getIvResources(String imagePath) {
        switch (imagePath) {
            case "1":
                return R.drawable.example_1;
            case "2":
                return R.drawable.example_2;
            case "3":
                return R.drawable.photo3;
            case "4":
                return R.drawable.photo4;
            case "5":
                return R.drawable.photo5;
            case "6":
                return R.drawable.photo6;
            case "7":
                return R.drawable.photo7;
            case "8":
                return R.drawable.photo8;
            case "9":
                return R.drawable.photo9;
            case "10":
                return R.drawable.photo10;
            case "11":
                return R.drawable.photo11;
            case "12":
                return R.drawable.photo12;
            case "13":
                return R.drawable.photo13;
            case "14":
                return R.drawable.photo14;
            case "15":
                return R.drawable.photo15;
        }

        return 0;
    }

    @NonNull
    private List<OutStorageBean> getOutBeans() {
        List<OutStorageBean> datas = new ArrayList<>();
        datas.add(new OutStorageBean("1", "用于制作车辆运输证照片"));
        datas.add(new OutStorageBean("2", "左前方45°角照片"));
        datas.add(new OutStorageBean("3", "行驶证+钥匙"));
        datas.add(new OutStorageBean("4", "车辆交接清单"));
        datas.add(new OutStorageBean("5", "车辆正前方"));
        datas.add(new OutStorageBean("6", "车辆正后方"));
        datas.add(new OutStorageBean("7", "车辆左前方"));
        datas.add(new OutStorageBean("8", "车辆右前方"));
        datas.add(new OutStorageBean("9", "车辆正左侧方"));
        datas.add(new OutStorageBean("10", "车辆正右侧方"));
        datas.add(new OutStorageBean("11", "车辆左后方"));
        datas.add(new OutStorageBean("12", "车辆右后方"));
        datas.add(new OutStorageBean("13", "车辆仪表内饰"));
        datas.add(new OutStorageBean("14", "车辆后排内饰"));
        datas.add(new OutStorageBean("15", "车辆备胎，随车工具，千斤顶"));

        return datas;
    }


    @Override
    protected void initControlEvent() {
        sendRequest(Http_Url.GetOutOrInType, new RequestParams(), GetOutOrInType[].class, new BaseCallBack<GetOutOrInType[]>() {
            @Override
            public void onSuccess(GetOutOrInType[] getOutOrInTypes) {
//                Toast.makeText(ui, "getOutOrInTypes.length:" + getOutOrInTypes.length, Toast.LENGTH_SHORT).show();
                if (null != getOutOrInTypes) {
                    mOutOrInTypes = Arrays.asList(getOutOrInTypes);
                }
            }
        });
    }

    @Override
    protected void initMember() {
        super.addFgm(R.id.titlebar, titleBar);
    }

    @Override
    public void onAttachedToWindow() {
        titleBar.setTitle("出入库拍照");
        Button buttonRight = titleBar.getRightView(1);
        buttonRight.setText("首页");
        buttonRight.setVisibility(View.VISIBLE);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbsUI.startClearTopUI(context, ThirdMainActivity.class);
                AbsUI.stopUI(ui);
            }
        });
        titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        super.onAttachedToWindow();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                mCameraWrapper
                        .requestCode((Integer) v.getTag())
                        .start();
                break;

            case R.id.tv_hetong:

                showPop(mTv_hetong, mOutOrInTypes);
                break;
            case R.id.tv_ku:
                ArrayList list1 = new ArrayList() {
                    {
                        add("入库");
                        add("出库");

                    }
                };
                showPop(mTv_ku, list1);
                break;
            case R.id.tv_commit:
                commit();
                break;
        }
    }

    /**
     * 提交图片
     */
    private void commit() {
        if (!checkPicComplete()) {
            showToast("拍照未完成");
            return;
        }
        if (null == mTv_hetong.getTag()) {
            showToast("请选择合同类型");
            return;
        }
        if (null == mTv_ku.getTag()) {
            showToast("请选择出入库");
            return;
        }
        if (TextUtils.isEmpty(mCarid) || TextUtils.isEmpty(mCompanyid)) {
            showToast("数据异常,请退出后重新进入!");
            return;
        }
        showProgressDiag();
        new MyTask().execute();
//

    }

    /**
     * 显示选择框
     *
     * @param tv
     * @param datas
     */
    private void showPop(final TextView tv, final List datas) {
        if (datas == null || datas.size() == 0) {
            showToast("暂无数据,请检查网络!");
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if (datas.get(0) instanceof String) {
                    tv.setText((CharSequence) datas.get(options1));
                    if ("出库".equals((CharSequence) datas.get(options1).toString().trim())) {
                        tv.setTag("0");
                    } else {
                        tv.setTag("1");
                    }
                } else {
                    tv.setTag(((GetOutOrInType) datas.get(options1)).getCOP_ID());
                    tv.setText(((GetOutOrInType) datas.get(options1)).getPickerViewText());
                }
            }
        })

                .setTitleText("请选择")
                .setDividerColor(Color.TRANSPARENT)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setSelectOptions(0)
                .build();

        pvOptions.setPicker(datas);//一级选择器
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        pvOptions.show();
    }

    public boolean checkPicComplete() {
        for (OutStorageBean outStorageBean : mRecyclerAdapter.getDatas()) {
            if (outStorageBean.getImagePath().length() < 3) {
                return false;
            }
        }
        return true;
    }
    /**
     * 拍照结束的回调
     */
    AlbumListener<String> albumListener = new AlbumListener<String>() {
        @Override
        public void onAlbumResult(int requestCode, @NonNull String result) {
            mRecyclerAdapter.getDatas().get(requestCode).setImagePath(result);
            mRecyclerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onAlbumCancel(int requestCode) {

        }
    };

    /**
     * 关闭对话框
     */
    protected void closeDialog() {
        if (progressDialog != null && progressDialog.isShowing() && ui != null
                && !ui.isFinishing()) {
            progressDialog.dismiss();
        }
    }
    private ProgressDialog progressDialog;
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
    private class MyTask extends AsyncTask<Object, Object, RequestParams> {

        @Override
        protected RequestParams doInBackground(Object... params) {

            RequestParams requestParams = new RequestParams();
            requestParams.put("CarID", mCarid);
            requestParams.put("op", "CarOutInImgUpload");
            requestParams.put("Type", mTv_hetong.getTag());
            requestParams.put("OutOrInType", mTv_ku.getTag());
            requestParams.put("token", SPUtils.get(OutStoragePictureActivity.this, Constants.StaffID, ""));
            for (int i = 0; i < mRecyclerAdapter.getDatas().size(); i++) {
                try {
                    String path = mRecyclerAdapter.getDatas().get(i).getImagePath();
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
//            String url = "http://192.168.80.5:99" + "/ashx/ERPCarHandler.ashx";
            sendRequest(Http_Url.ERPCARHANDLER, requestParams, OnlyIntModelBean.class, "", false, tv_commit, new BaseCallBack<OnlyIntModelBean>() {
                @Override
                public void onSuccess(OnlyIntModelBean bean) {
                    if (bean != null) {
                        if (1 == bean.getStatu()) {
                            finish();
                        } else if (2 == bean.getStatu()) {
                            AlertDialog.Builder b = new AlertDialog.Builder(OutStoragePictureActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                            b.setMessage(bean.getMsg());
                            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SPUtils.put(context, Constants.ForgetCode,
                                            "false");

                                    AbsUI.startClearTopUI(context, LoginUI.class);
                                    AbsUI.stopUI(ui);
                                }
                            });
                            b.setCancelable(false);
                            b.create().show();
                            return;
                        }
                        showToast(bean.getMsg());
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
