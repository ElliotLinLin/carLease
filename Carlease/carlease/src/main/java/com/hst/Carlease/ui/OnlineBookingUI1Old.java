package com.hst.Carlease.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.hst.Carlease.R;
import com.hst.Carlease.adapter.CommonAdapter;
import com.hst.Carlease.adapter.CommonViewHolder;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.asynchttp.BaseCallBack;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.http.bean.AreaBean;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.GetOrderListBean;
import com.hst.Carlease.http.bean.OnlineBooking1Bean;
import com.hst.Carlease.http.bean.PurchaseDataBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.thirdperiodui.ThirdBaseUi;
import com.hst.Carlease.util.LocationChangedUtils;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.StringUtils;
import com.hst.Carlease.widget.citypicker.wid.CityPicker;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.loopj.android.http.RequestParams;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.widget.PopupWindowExt;
import com.tools.widget.Prompt;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OnlineBookingUI1Old extends ThirdBaseUi implements OnClickListener {
    private final String TAG = OnlineBookingUI1Old.class.getSimpleName();
    private TitleBar titlebar;
    private EditText name, cardno, mine_phone, family, contanct, fa_phone,// 名字，身份证号,亲属名字，亲属关系,亲属电话
            work_loc, et_emergency_contact, et_emergency_contact_phone, //单位地址 紧急联系人姓名 紧急联系人电话
            et_emergency_contact_relation, et_user_bank_name, et_user_bank_number, //紧急联系人关系  开户名称 银行卡号
            et_sub_bank_name, now_detail_loc //支行名称 详细居住地址
            ;
    private TextView tv_spinner,
            now_loc, tv_bank_name, tv_driver_license_date,//现居住地址 银行名称  驾驶证日期
    //			mine_address,getcar,,jingbanren ,message
    mine_yewu;// 是否结婚,是否征信
    private PopupWindowExt mAddFrindsPopup;
    private CommonAdapter<String> adapter2;
    private TextView btn_next;
    private Intent intent;
    ArrayList<String> list = new ArrayList<String>() {
    };
    ArrayList<String> list2 = new ArrayList<String>() {
        {
            add("是");
            add("否");

        }
    };

    ArrayList<String> emergyList = new ArrayList<>();//紧急联系人
    ArrayList<String> immFamList = new ArrayList<>();//亲属
    ArrayList<String> bankList = new ArrayList<String>();//银行列表
    CityPicker cityPicker;
    private LocationChangedUtils utils;
    private List<PurchaseDataBean.ModelBean.BankListBean> mBankList;
    private List<PurchaseDataBean.ModelBean.MarriageListBean> mMarriageList;
    private List<PurchaseDataBean.ModelBean.EmergyRelationListBean> mEmergyRelationList;
    private List<PurchaseDataBean.ModelBean.ImmFamRelationlistBean> mImmFamRelationlist;

    @Override
    protected void onCreate(Bundle arg0) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(R.layout.ui_onlinebooking1_old);
        super.setSlideFinishEnabled(false);
        super.onCreate(arg0);

        getBankList();
        getAreaList();
    }

    private ArrayList<AreaBean.ModelBean> provinceList = new ArrayList();
    private ArrayList<ArrayList<AreaBean.ModelBean>> cityList = new ArrayList();
    private ArrayList<ArrayList<ArrayList<AreaBean.ModelBean>>> areaList = new ArrayList();

    private void getAreaList() {
        sendRequest(Http_Url.GETAREALIST, new RequestParams(), AreaBean.class, new BaseCallBack<AreaBean>() {
            @Override
            public void onSuccess(AreaBean areaBean) {
                if (areaBean != null) {
                    List<AreaBean.ModelBean> model = areaBean.getModel();

                    for (AreaBean.ModelBean modelBean : model) {
                        if (modelBean.getArea_ParentID() == 0) {
                            provinceList.add(modelBean);
                        }
                    }
                    if (provinceList == null || provinceList.size() == 0)
                        provinceList.add(new AreaBean.ModelBean(-1, ""));
                    for (AreaBean.ModelBean bean : provinceList) {
                        ArrayList<AreaBean.ModelBean> tempcity = new ArrayList<AreaBean.ModelBean>(); //每一个城市都要有一个列表
                        for (AreaBean.ModelBean modelBean : model) {
                            if (bean.getArea_ID() == modelBean.getArea_ParentID()) {//如果该项是属于该城市的就加入
                                tempcity.add(modelBean);
                            }
                        }
                        if (tempcity.size() == 0) tempcity.add(new AreaBean.ModelBean(-1, ""));
                        cityList.add(tempcity);
                    }


                    for (ArrayList<AreaBean.ModelBean> modelBeen : cityList) {//每一个城市中的区的列表
                        ArrayList<ArrayList<AreaBean.ModelBean>> tempArea = new ArrayList<ArrayList<AreaBean.ModelBean>>();//每一个区都装的是一个列表
                        for (AreaBean.ModelBean modelBean : modelBeen) {//每一个列表中的区
                            ArrayList<AreaBean.ModelBean> tempArea1 = new ArrayList<AreaBean.ModelBean>();
                            for (AreaBean.ModelBean bean : model) {
                                if (bean.getArea_ParentID() == modelBean.getArea_ID()) {
                                    tempArea1.add(bean);
                                }
                            }
                            if (tempArea1.size() == 0)
                                tempArea1.add(new AreaBean.ModelBean(-1, ""));
                            tempArea.add(tempArea1);
                        }
                        areaList.add(tempArea);
                    }

                }

//                showToast("添加完成");
            }
        });
    }

    private void getBankList() {
        sendRequest(Http_Url.GETPURCHASEDATALIST, new RequestParams(), PurchaseDataBean.class, new BaseCallBack<PurchaseDataBean>() {
            @Override
            public void onSuccess(PurchaseDataBean purchaseDataBean) {
                if (purchaseDataBean == null) {

                } else {
                    PurchaseDataBean.ModelBean model = purchaseDataBean.getModel();
                    mBankList = model.getBankList();
                    mMarriageList = model.getMarriageList();
                    mEmergyRelationList = model.getEmergyRelationList();
                    mImmFamRelationlist = model.getImmFamRelationlist();

                    for (PurchaseDataBean.ModelBean.MarriageListBean marriageListBean : mMarriageList) {
                        list.add(marriageListBean.getRelationKey());
                    }

                    for (PurchaseDataBean.ModelBean.EmergyRelationListBean emergyRelationListBean : mEmergyRelationList) {
                        emergyList.add(emergyRelationListBean.getText());
                    }

                    for (PurchaseDataBean.ModelBean.ImmFamRelationlistBean immFamRelationlistBean : mImmFamRelationlist) {
                        immFamList.add(immFamRelationlistBean.getRelationKey());
                    }
                    for (PurchaseDataBean.ModelBean.BankListBean bankListBean : mBankList) {
                        bankList.add(bankListBean.getBN_Name());
                    }
                }

            }

            @Override
            public void onFailure(String arg2, Throwable arg3) {
                super.onFailure(arg2, arg3);
                Log.w("OnlineBookingUI1", arg3.getMessage());
            }
        });

    }

    @Override
    protected void initControl() {
        titlebar = new TitleBar();
        name = (EditText) findViewById(R.id.name);
        cardno = (EditText) findViewById(R.id.cardno);
        mine_phone = (EditText) findViewById(R.id.mine_phone);
        family = (EditText) findViewById(R.id.family);
        contanct = (EditText) findViewById(R.id.contanct);
        fa_phone = (EditText) findViewById(R.id.fa_phone);
//		jingbanren = (EditText) findViewById(R.id.jingbanren);
//		getcar = (TextView) findViewById(R.id.getcar);
//		mine_address = (TextView) findViewById(R.id.mine_address);
        mine_yewu = (TextView) findViewById(R.id.mine_yewu);
        tv_spinner = (TextView) findViewById(R.id.tv_spinner);
//        message = (TextView) findViewById(R.id.message);
        btn_next = (TextView) findViewById(R.id.btn_next);
        intent = getIntent();
        utils = new LocationChangedUtils(context, ui);
        cityPicker = new CityPicker.Builder(OnlineBookingUI1Old.this).textSize(20)
                .title("选择城市").backgroundPop(0xa0000000)
                .titleBackgroundColor("#2A8AE0").confirTextColor("#333333")
                .cancelTextColor("#333333").province("广东省").city("深圳市")
                .district("罗湖区").textColor(Color.parseColor("#333333"))
                .provinceCyclic(true).cityCyclic(false).districtCyclic(false)
                .visibleItemsCount(7).itemPadding(10).build();
        mine_phone.setText(SPUtils.get(context, Constants.UserName, "").toString());
        mine_phone.setFocusable(false);

        work_loc = (EditText) findViewById(R.id.work_loc);
        et_emergency_contact = (EditText) findViewById(R.id.et_emergency_contact);
        et_emergency_contact_phone = (EditText) findViewById(R.id.et_emergency_contact_phone);
        et_emergency_contact_relation = (EditText) findViewById(R.id.et_emergency_contact_relation);
        et_user_bank_name = (EditText) findViewById(R.id.et_user_bank_name);
        et_user_bank_number = (EditText) findViewById(R.id.et_user_bank_number);
        et_sub_bank_name = (EditText) findViewById(R.id.et_sub_bank_name);
        now_detail_loc = (EditText) findViewById(R.id.now_detail_loc);
        now_loc = (TextView) findViewById(R.id.now_loc);
        tv_bank_name = (TextView) findViewById(R.id.tv_bank_name);
        tv_driver_license_date = (TextView) findViewById(R.id.tv_driver_license_date);

        GetOrderList(); //获取我的违章列表
    }

    @Override
    protected void initControlEvent() {
        btn_next.setOnClickListener(this);
        mine_yewu.setOnClickListener(this);
//		getcar.setOnClickListener(this);
//		mine_address.setOnClickListener(this);
//        message.setOnClickListener(this);
        tv_spinner.setOnClickListener(this);
        now_loc.setOnClickListener(this);
        et_emergency_contact_relation.setOnClickListener(this);
        contanct.setOnClickListener(this);
        tv_driver_license_date.setOnClickListener(this);
        tv_bank_name.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        if (intent.getStringExtra("UI").equals("BranchOfficeUI")) {

            if (!TextUtils.isEmpty(intent.getStringExtra("cardno"))) {
                cardno.setText(intent.getStringExtra("cardno").toUpperCase().toString().trim());
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("carname"))) {
                name.setText(intent.getStringExtra("carname"));
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("fa_phone"))) {
                fa_phone.setText(intent.getStringExtra("fa_phone"));
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("family"))) {
                family.setText(intent.getStringExtra("family"));
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("contanct"))) {
                contanct.setText(intent.getStringExtra("contanct"));
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("mine_phone"))) {
                mine_phone.setText(intent.getStringExtra("mine_phone"));
            }


            if (intent.getIntExtra("spinner", -1) == 1) {
                tv_spinner.setText("未婚");
            } else if (intent.getIntExtra("spinner", -1) == 0) {
                tv_spinner.setText("已婚");
            }
//            purchaseParamsBean.setCompanyId(intent.getIntExtra("CompanyID", 0));
//            purchaseParamsBean.setLiveAreaID(intent.getIntExtra("liveAreaID", 0));

            if (!TextUtils.isEmpty(intent.getStringExtra("liveAddress"))) {
                now_detail_loc.setText(intent.getStringExtra("liveAddress"));
            }

            if (!TextUtils.isEmpty(intent.getStringExtra("emergyName"))) {
                et_emergency_contact.setText(intent.getStringExtra("emergyName"));
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("emergMobile"))) {
                et_emergency_contact_phone.setText(intent.getStringExtra("emergMobile"));
            }

            if (!TextUtils.isEmpty(intent.getStringExtra("AccountName"))) {
                et_user_bank_name.setText(intent.getStringExtra("AccountName"));
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("bankNum"))) {
                et_user_bank_number.setText(intent.getStringExtra("bankNum"));
            }

            if (!TextUtils.isEmpty(intent.getStringExtra("branchName"))) {
                et_sub_bank_name.setText(intent.getStringExtra("branchName"));
            }

            if (!TextUtils.isEmpty(intent.getStringExtra("DriveFirstDate"))) {
                tv_driver_license_date.setText(intent.getStringExtra("DriveFirstDate"));
            }

            if (!TextUtils.isEmpty(intent.getStringExtra("comAddress"))) {
                work_loc.setText(intent.getStringExtra("comAddress"));
            }
            if (!TextUtils.isEmpty(intent.getStringExtra("contanct"))) {
                contanct.setText(intent.getStringExtra("contanct"));
            }

            if (mBankList != null) {
                for (PurchaseDataBean.ModelBean.BankListBean bankListBean : mBankList) {
                    if (bankListBean.getBN_ID() == intent.getIntExtra("bankId", 0)) {
                        tv_bank_name.setText(bankListBean.getBN_Name());
                    }
                }
            }
            if (mEmergyRelationList != null) {
                for (PurchaseDataBean.ModelBean.EmergyRelationListBean emergyRelationListBean : mEmergyRelationList) {
                    if (emergyRelationListBean.getValue() == intent.getIntExtra("emeryRelation", 0)) {
                        et_emergency_contact_relation.setText(emergyRelationListBean.getText());
                    }
                }
            }

//            else (intent.getStringExtra("spinner").equals("2")){
//            }

//			if (intent.getStringExtra("mine_address").isEmpty() == false) {
//				mine_address.setText(intent.getStringExtra("mine_address"));
//			}
//
//			if (intent.getStringExtra("getcar").isEmpty() == false) {
//				getcar.setText(intent.getStringExtra("getcar"));
//			}
//			if (intent.getStringExtra("jingbanren").isEmpty() == false) {
//				jingbanren.setText(intent.getStringExtra("jingbanren"));
//			}
//            if (intent.getStringExtra("message").isEmpty() == false) {
//                message.setText(intent.getStringExtra("message").toString());
//            }
            if (intent.getStringExtra("CompanyName").isEmpty() == false) {
                if (intent.getStringExtra("storeName").isEmpty() == false) {
                    mine_yewu.setText(intent.getStringExtra("CompanyName") + ""
                            + intent.getStringExtra("storeName"));
                } else {
                    mine_yewu.setText(intent.getStringExtra("CompanyName"));
                }

            }
        }
        utils.startOnce();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        utils.stop();
        super.onDestroy();
    }

    // 检查参数
    private void CheckPram() {
        if (name.getText().toString().trim().isEmpty()) {
            ToastL.show("请输入您的姓名");
            return;
        }
        if (name.getText().toString().contains(" ")) {
            ToastL.show("姓名不能包含空格");
            return;
        }
        if (cardno.getText().toString().trim().isEmpty()) {
            ToastL.show("请输入您的身份证号码");
            return;
        }
        if (cardno.getText().toString().trim().contains(" ")) {
            ToastL.show("身份证号码不能包含空格");
            return;
        }
        if (cardno.getText().toString().trim().length() != 18) {
            ToastL.show("您输入的身份证号码有误");
            return;
        }
        if (mine_phone.getText().toString().trim().isEmpty()) {
            ToastL.show("请输入您的手机号码");
            return;
        }
        if (mine_phone.getText().toString().trim().contains(" ")) {
            ToastL.show("手机号码不能包含空格");
            return;
        }
        if (StringUtils.isPhoneLegal(mine_phone.getText().toString()) == false) {
            ToastL.show("请输入合法的手机号码");
            return;
        }
//		if (mine_address.getText().toString().isEmpty()) {
//			ToastL.show("请选择下订单的位置");
//			return;
//		}
//		if (getcar.getText().toString().isEmpty()) {
//			ToastL.show("请选择提车城市");
//			return;
//		}

        if (mine_yewu.getText().toString().trim().isEmpty()) {
            ToastL.show("请选择业务网点");
            return;
        }
        if (work_loc.getText().toString().trim().isEmpty()) {
            ToastL.show("请填写单位地址");
            return;
        }
        if (now_loc.getText().toString().trim().isEmpty()) {
            ToastL.show("请选择现居住地址");
            return;
        }
        if (now_detail_loc.getText().toString().trim().isEmpty()) {
            ToastL.show("请输入详细居住地址");
            return;
        }

        if (tv_spinner.getText().toString().isEmpty()) {
            ToastL.show("请选择您的婚姻关系");
            return;
        }

        if (et_emergency_contact.getText().toString().trim().isEmpty()) {
            ToastL.show("请填写紧急联系人");
            return;
        }
        if (et_emergency_contact_phone.getText().toString().trim().isEmpty()) {
            ToastL.show("请填写紧急联系人电话");
            return;
        }
        if (StringUtils.isPhoneLegal(et_emergency_contact_phone.getText().toString()) == false) {
            ToastL.show("请输入合法的紧急联系人手机号码");
            return;
        }
        if (et_emergency_contact_relation.getText().toString().trim().isEmpty()) {
            ToastL.show("请填写紧急联系人与本人关系");
            return;
        }

//        if (message.getText().toString().isEmpty()) {
//            ToastL.show("请选择是否征信");
//            return;
//        }
        if (family.getText().toString().isEmpty()) {
            ToastL.show("请输入您的直系亲属姓名");
            return;
        }
        if (family.getText().toString().trim().contains(" ")) {
            ToastL.show("直系亲属姓名不能包含空格");
            return;
        }

        if (fa_phone.getText().toString().trim().isEmpty()) {
            ToastL.show("请输入您直系亲属的联系电话");
            return;
        }
        if (fa_phone.getText().toString().trim().contains(" ")) {
            ToastL.show("直系亲属电话不能包含空格");
            return;
        }
        if (StringUtils.isPhoneLegal(fa_phone.getText().toString()) == false) {
            ToastL.show("请输入合法的直系亲属电话");
            return;
        }
        if (contanct.getText().toString().trim().isEmpty()) {
            ToastL.show("请输入直系亲属和本人关系");
            return;
        }
        if (contanct.getText().toString().contains(" ")) {
            ToastL.show("亲属关系不能包含空格");
            return;
        }
        if (tv_bank_name.getText().toString().trim().isEmpty()) {
            ToastL.show("请选择银行名称");
            return;
        }
        if (et_user_bank_name.getText().toString().trim().isEmpty()) {
            ToastL.show("请输入开户名称");
            return;
        }
        if (et_user_bank_number.getText().toString().trim().isEmpty()) {
            ToastL.show("请输入银行卡号");
            return;
        }
        if (et_sub_bank_name.getText().toString().trim().isEmpty()) {
            ToastL.show("请输入支行名称");
            return;
        }
        if (tv_driver_license_date.getText().toString().trim().isEmpty()) {
            ToastL.show("请选择驾驶证领证日期");
            return;
        }


        putIntent();
        AbsUI2.startUI(context, OnlineBookingUI3.class, intent);
    }

    /**
     * 我的违章列表
     */
    protected void GetOrderList() {
        NetworkState nState = new NetworkState(ui);
        if (nState.isConnected() == false) {
            Prompt.showWarning(context, "无网络连接");
            return;
        }
        GetOrderListBean bean = new GetOrderListBean();
        bean.setTokenID(SPUtils.get(context, Constants.tokenID, "").toString());
        Log.i("oooooo", "bean---" + bean.getTokenID());

        try {
            AsyncHttpUtil.post(ui, Http_Url.GetCustomerDetail, bean,
                    "application/json", new AsyncCallBackHandler(ui, "", true,
                            fa_phone) {

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
                            Log.i("1111", "result 截取" + bean.getD());
                            if (bean.getD() != null) {
                                OnlineBooking1Bean OnlineBooking1Bean = GJson.parseObject(
                                        bean.getD(), OnlineBooking1Bean.class);
                                if (OnlineBooking1Bean != null) {
                                    name.setText(OnlineBooking1Bean.getModel().getCusName());
                                    cardno.setText(OnlineBooking1Bean.getModel().getCusNum());
                                    mine_phone.setText(OnlineBooking1Bean.getModel().getCusMobile());
                                    tv_spinner.setText(OnlineBooking1Bean.getModel().getMarriageStatusCh());

                                    if (intent.getStringExtra("UI").equals("BranchOfficeUI")) {
                                        if (intent.getStringExtra("cardno").isEmpty() == false) {
                                            cardno.setText(intent.getStringExtra("cardno"));
                                            Log.i(TAG, "onResume()   TAG" + intent.getStringExtra("cardno"));
                                        }
                                        if (intent.getStringExtra("carname").isEmpty() == false) {
                                            name.setText(intent.getStringExtra("carname"));
                                            Log.i(TAG, "onResume()   TAG" + intent.getStringExtra("carname"));
                                        }
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
        super.addFgm(R.id.titlebar, titlebar);
    }

    @Override
    public void onAttachedToWindow() {
        titlebar.setTitle("在线预订");
        titlebar.getLeftView(1).setOnClickListener(new OnClickListener() {

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
    protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_address:
//			showCityPop(mine_address);
                break;
            case R.id.tv_spinner:
                ShowAddFrindsPopup(list, tv_spinner, 1, tv_spinner.getText()
                        .toString());
                break;
//            case R.id.message:
//                ShowAddFrindsPopup(list2, message, 2, message.getText().toString());
//                break;
            case R.id.btn_next:// 下一步
                CheckPram();
                break;
            case R.id.mine_yewu:
                putIntent();
                AbsUI.startUI(context, BranchOfficeUI.class, intent);
                break;
            case R.id.getcar:
//			showCityPop(getcar);
                break;
            case R.id.now_loc:
//                showCityPop(now_loc);
                showNewCityPop();
                break;

            case R.id.et_emergency_contact_relation:
                ShowAddFrindsPopup(emergyList, et_emergency_contact_relation, 2, et_emergency_contact_relation.getText().toString());
                break;
            case R.id.contanct:
                ShowAddFrindsPopup(immFamList, contanct, 2, contanct.getText().toString());
                break;
            case R.id.tv_driver_license_date:
                Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tv_driver_license_date.setText(year + "-" + (month+1) + "-" + dayOfMonth);

                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.tv_bank_name:
                ShowAddFrindsPopup(bankList, tv_bank_name, 2, tv_bank_name.getText().toString());
                break;
            default:
                break;
        }
    }

    private int liveAreaID;

    private void showNewCityPop() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = provinceList.get(options1).getPickerViewText() +
                        cityList.get(options1).get(options2).getPickerViewText() +
                        areaList.get(options1).get(options2).get(options3).getPickerViewText();
                liveAreaID = areaList.get(options1).get(options2).get(options3).getArea_ID();
                now_loc.setText(tx);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.TRANSPARENT)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(provinceList, cityList, areaList);//三级选择器
        pvOptions.show();
    }

    /**
     * 城市选择
     *
     * @param textview
     */
    private void showCityPop(final TextView textview) {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (cityPicker.isShow()) {
            cityPicker.hide();
        }

        cityPicker.show();
        cityPicker
                .setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {

                    @Override
                    public void onSelected(String... citySelected) {
                        textview.setText(citySelected[0] + "" + citySelected[1]);
                        cityPicker.hide();
                    }
                });

    }

    // 将参数放在intent里面
    private void putIntent() {
        intent.putExtra("carname", name.getText().toString().trim());//身份证姓名
        intent.putExtra("cardno", cardno.getText().toString().trim().toUpperCase());//身份证号码
        intent.putExtra("mine_phone", mine_phone.getText().toString().trim());//本人电话
//        intent.putExtra("CompanyID", id);//业务网点
        intent.putExtra("comAddress", work_loc.getText().toString().trim());//单位住址
        intent.putExtra("liveAreaID", this.liveAreaID);
        intent.putExtra("liveAddress", now_detail_loc.getText().toString().trim());//现居住详细地址

        for (PurchaseDataBean.ModelBean.MarriageListBean marriageListBean : mMarriageList) {
            if (tv_spinner.getText().toString().equals(marriageListBean.getRelationKey())) {
                intent.putExtra("spinner", marriageListBean.getRelationValue()); //婚姻状况
                break;
            }
        }

        intent.putExtra("emergyName", et_emergency_contact.getText().toString().trim());//紧急联系人姓名
        intent.putExtra("emergMobile", et_emergency_contact_phone.getText().toString().trim());//紧急联系人电话

        for (PurchaseDataBean.ModelBean.EmergyRelationListBean emergyRelationListBean : mEmergyRelationList) {
            if (et_emergency_contact_relation.getText().toString().trim().equals(emergyRelationListBean.getText())) {
                intent.putExtra("emeryRelation", emergyRelationListBean.getValue());//紧急联系人关系
                break;
            }
        }


        intent.putExtra("family", family.getText().toString().trim());//直系亲属姓名
        intent.putExtra("fa_phone", fa_phone.getText().toString().trim());//直系亲属联系电话
        for (PurchaseDataBean.ModelBean.ImmFamRelationlistBean immFamRelationlistBean : mImmFamRelationlist) {
            if (contanct.getText().toString().trim().equals(immFamRelationlistBean.getRelationValue())) {
                intent.putExtra("contanct", immFamRelationlistBean.getRelationKey());//直系亲属与本人关系

            }
        }

        for (PurchaseDataBean.ModelBean.BankListBean bankListBean : mBankList) {
            if (tv_bank_name.getText().toString().trim().equals(bankListBean.getBN_Name())) {
                intent.putExtra("bankId", bankListBean.getBN_ID());//银行名称ID
                break;
            }
        }
        intent.putExtra("AccountName", et_user_bank_name.getText().toString().trim());// 开户名称
        intent.putExtra("bankNum", et_user_bank_number.getText().toString().trim());// 银行卡号
        intent.putExtra("branchName", et_sub_bank_name.getText().toString().trim());// 支行名称
        intent.putExtra("DriveFirstDate", tv_driver_license_date.getText().toString().trim());// 驾驶证初次领证日期
//        intent.putExtra("mine_address", mine_address.getText().toString()
//                .trim());
//        intent.putExtra("getcar", getcar.getText().toString().trim());
//        intent.putExtra("jingbanren", jingbanren.getText().toString().trim());
//        intent.putExtra("message", message.getText().toString().trim());//是否征信
    }

    // 显示弹框
    private void ShowAddFrindsPopup(List<String> poplist, final View views,
                                    final int type, final String dataS) {
        View view = View.inflate(context, R.layout.pop_setect, null);
        mAddFrindsPopup = new PopupWindowExt(view,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mAddFrindsPopup.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mAddFrindsPopup.setBackgroundDrawable(dw);
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
                if (dataS.equals(data)) {
                    checked.setVisibility(View.VISIBLE);
                } else {
                    checked.setVisibility(View.INVISIBLE);
                }
                textView.setText(data);
            }
        };
        mTvAdd.setAdapter(adapter2);

        mTvAdd.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (views instanceof TextView) {
                    ((TextView) views).setText(parent.getAdapter().getItem(position)
                            .toString());
                }

//                if (type == 1) {
//                    tv_spinner.setText(parent.getAdapter().getItem(position)
//                            .toString());
//                }
// else {
//                    message.setText(parent.getAdapter().getItem(position)
//                            .toString());
//                }
                closeAddFrindsPopup();
            }
        });
        showAddFrindsPopup(views);
    }

    @Override
    protected byte[] doInBackgroundLoader() {
        return null;
    }

    // 显示添加的window
    protected void showAddFrindsPopup(View view) {
        if (mAddFrindsPopup != null) {
            if (!mAddFrindsPopup.isShowing()) {
                mAddFrindsPopup.showAsDropDown(view, 0, 10);
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
                mAddFrindsPopup.close();
            }
        }
    }

}
