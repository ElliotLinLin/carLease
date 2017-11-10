package com.hst.Carlease.ui.thirdperiodui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hst.Carlease.R;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.ui.LoginUI;
import com.hst.Carlease.util.SPUtils;
import com.tools.app.AbsUI;
import com.tools.app.AlertDialog;

public class ThirdMainActivity extends ThirdBaseUi implements View.OnClickListener {
    private Button btn_car_manager, btn_yewu, btn_car_weihu, btn_hetong_manager, btn_money, btn_qianzai_kehu, btn_shou_car, btn_qiankuan_kehu;
    private ImageView mIv_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_third_main);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initControl() {
        btn_car_manager = (Button) findViewById(R.id.btn_car_manager);
        btn_yewu = (Button) findViewById(R.id.btn_yewu);
        btn_car_weihu = (Button) findViewById(R.id.btn_car_weihu);
        btn_hetong_manager = (Button) findViewById(R.id.btn_hetong_manager);
        btn_money = (Button) findViewById(R.id.btn_money);
        btn_qianzai_kehu = (Button) findViewById(R.id.btn_qianzai_kehu);
        btn_shou_car = (Button) findViewById(R.id.btn_shou_car);
        btn_qiankuan_kehu = (Button) findViewById(R.id.btn_qiankuan_kehu);
        mIv_exit = (ImageView) findViewById(R.id.iv_exit);
        btn_car_manager.setOnClickListener(this);
        btn_yewu.setOnClickListener(this);
        btn_car_weihu.setOnClickListener(this);
        btn_hetong_manager.setOnClickListener(this);
        btn_money.setOnClickListener(this);
        btn_qianzai_kehu.setOnClickListener(this);
        btn_shou_car.setOnClickListener(this);
        btn_qiankuan_kehu.setOnClickListener(this);
        mIv_exit.setOnClickListener(this);
    }

    @Override
    protected void initControlEvent() {

    }

    @Override
    protected void initMember() {

    }

    @Override
    public void onClick(View v) {
        String url;
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_car_manager:
                String url1 = Http_Url.carManageList + "?token=" + SPUtils.get(this, Constants.StaffID, "") + "&comId=" + SPUtils.get(this, Constants.CompanyID, "");
                Intent intent1 = new Intent(this, CarListWebViewActivity.class);
                intent1.putExtra("url", url1);
                startUI(this, intent1);
                break;
            case R.id.btn_yewu:
                url = Http_Url.businessDeclaration + "?token=" + SPUtils.get(this, Constants.StaffID, "") + "&comId=" + SPUtils.get(this, Constants.CompanyID, "");
//                url = "http://192.168.80.5:99/html5/erp_business/businessDeclaration.html" + "?token=" + SPUtils.get(this, Constants.StaffID, "") + "&comId=" + SPUtils.get(this, Constants.CompanyID, "");
                intent = new Intent(this, CarListWebViewActivity.class);
                intent.putExtra("url", url);
                startUI(this, intent);
                break;
            case R.id.btn_car_weihu:
                showToast("车辆维护正在开发中");
                break;
            case R.id.btn_hetong_manager:
                showToast("合同管理正在开发中");
                break;
            case R.id.btn_money:
                showToast("费用申请正在开发中");
                break;
            case R.id.btn_qianzai_kehu:
                url = Http_Url.potentialCustomer + "?token=" + SPUtils.get(this, Constants.StaffID, "") + "&comId=" + SPUtils.get(this, Constants.CompanyID, "");
                intent = new Intent(this, CarListWebViewActivity.class);
                intent.putExtra("url", url);
                startUI(this, intent);
//                showToast("潜在客户");
                break;
            case R.id.btn_shou_car:
                showToast("收车正在开发中");
                break;
            case R.id.btn_qiankuan_kehu:
                showToast("欠款客户正在开发中");
                break;
            case R.id.iv_exit:
                exit();
                break;
        }
    }

    private void exit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ui);
        builder.setTitle(" ");
        builder.setMessage("您确定注销登录吗？");

        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        SPUtils.put(context, Constants.ForgetCode,
                                "false");
                        AbsUI.startClearTopUI(context, LoginUI.class);
                        AbsUI.stopUI(ui);
                    }

                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }

                        }).create().show();
    }
}
