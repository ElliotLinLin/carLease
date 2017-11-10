package com.hst.Carlease.ui.fragement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.adapter.MainFragmentAdapter;
import com.hst.Carlease.app.MainApplication;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.eventBean.MainStype;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.ui.MyMessageUI;
import com.hst.Carlease.util.LocationChangedUtils;
import com.hst.Carlease.util.SPUtils;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.app.AlertDialog;
import com.tools.app.Config;
import com.tools.app.TitleBar;
import com.tools.app.UIManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainUI2 extends AbsUI2 {
    private final String TAG = MainUI2.class.getSimpleName();
    // FullTitleBar TITLE;// 沉浸式标题栏
    private MainFragmentAdapter adapter;

    private HomePageFgm oneFragment;

    private HotModelFgm twoFragment;

    private MineFgm threeFragment;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    private int index = 0;// 设置首先显示哪个页面
    private TextView tv_weixin;
    private TextView tv_tongxunlu;
    private TextView tv_faxian;

    private ImageView image_index;
    private ImageView image_car;
    private ImageView image_user;
    private LinearLayout liner1;
    private LinearLayout liner2;
    private LinearLayout liner3;
    private LinearLayout liner4;
    private ImageView image_car2;
    private TextView tv_car2;
    private OnLineBookingFgm OnLineBooking;
    private TitleBar titlebar;
    LocationChangedUtils utils;
    private boolean hasOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        setSlideFinishEnabled(false);
        // TITLE = new FullTitleBar(this, Color.parseColor("#2A8AE0"));
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initControl() {
        titlebar = new TitleBar();
        utils = new LocationChangedUtils(context, ui);
        tv_weixin = (TextView) findViewById(R.id.tv_weixin);
        tv_tongxunlu = (TextView) findViewById(R.id.tv_tongxunlu);
        tv_faxian = (TextView) findViewById(R.id.tv_faxian);
        image_index = (ImageView) findViewById(R.id.image_index);
        image_car = (ImageView) findViewById(R.id.image_car);
        image_user = (ImageView) findViewById(R.id.image_user);
        image_car2 = (ImageView) findViewById(R.id.image_car4);
        tv_car2 = (TextView) findViewById(R.id.tv_tongxunlu4);
        liner1 = (LinearLayout) findViewById(R.id.liner1);
        liner2 = (LinearLayout) findViewById(R.id.liner2);
        liner3 = (LinearLayout) findViewById(R.id.liner3);
        liner4 = (LinearLayout) findViewById(R.id.liner4);
        liner1.setOnClickListener(listener);
        liner2.setOnClickListener(listener);
        liner3.setOnClickListener(listener);
        liner4.setOnClickListener(listener);
        oneFragment = new HomePageFgm();
        twoFragment = new HotModelFgm();
        threeFragment = new MineFgm();
        OnLineBooking = new OnLineBookingFgm();

        fragmentList.add(oneFragment);
        fragmentList.add(twoFragment);
        fragmentList.add(OnLineBooking);
        fragmentList.add(threeFragment);
        hasOrder = (Boolean) SPUtils.get(context, Constants.hasOrder, false);
        Log.i(TAG, "hasOrder-------" + hasOrder);
        if (hasOrder) {
            index = 3;
            adapter = new MainFragmentAdapter(this, fragmentList, R.id.frame_main,
                    index);
            tv_weixin.setTextColor(Color.parseColor("#A5AAD0"));
            tv_tongxunlu.setTextColor(Color.parseColor("#A5AAD0"));
            tv_faxian.setTextColor(Color.parseColor("#56B0F4"));
            image_user.setBackgroundResource(R.drawable.user_click);
            image_car.setBackgroundResource(R.drawable.car);
            image_index.setBackgroundResource(R.drawable.index);
            image_car2.setBackgroundResource(R.drawable.car_2);
            tv_car2.setTextColor(Color.parseColor("#A5AAD0"));
            Log.i(TAG, "hasOrder-----" + hasOrder);
        } else {
            adapter = new MainFragmentAdapter(this, fragmentList, R.id.frame_main,
                    index);
        }


    }


    View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.liner1:
                    changeToMain();
                    tv_weixin.setTextColor(Color.parseColor("#56B0F4"));
                    tv_tongxunlu.setTextColor(Color.parseColor("#A5AAD0"));
                    tv_faxian.setTextColor(Color.parseColor("#A5AAD0"));
                    image_user.setBackgroundResource(R.drawable.user);
                    image_car.setBackgroundResource(R.drawable.car);
                    image_index.setBackgroundResource(R.drawable.index_click);
                    image_car2.setBackgroundResource(R.drawable.car_2);
                    tv_car2.setTextColor(Color.parseColor("#A5AAD0"));
                    adapter.onChange(0);
                    changeTitle(0);
                    break;
                case R.id.liner2://热门车型
                    changeToHot();
                    break;
                case R.id.liner4://二手车
                    changeTOSecondCar();
                    break;
                case R.id.liner3:
                    //个人中心
                    changeToMine();
                    break;
            }
        }


    };


    private void changeToMain() {

    }

    private void changeTitle(int num) {
        showFgm(titlebar);
        titlebar.setTitle("");
        titlebar.getTitleView().setBackgroundDrawable(null);
        titlebar.getRightView(2).setVisibility(View.GONE);
        titlebar.getLeftView(1).setVisibility(View.GONE);
        titlebar.getLeftView(2).setVisibility(View.GONE);
        if (num == 0) {
            titlebar.getLeftView(1).setVisibility(View.VISIBLE);
            titlebar.getRightView(2).setVisibility(View.VISIBLE);
            titlebar.getLeftView(2).setVisibility(View.VISIBLE);
            titlebar.getLeftView(1).setBackgroundResource(R.drawable.location);
            titlebar.getLeftView(2).setText(utils.cityname);
            titlebar.getTitleView().setBackgroundResource(R.drawable.main_title);
            titlebar.getRightView(2).setBackgroundResource(R.drawable.comment);
            Imageclock();
        } else if (num == 1) {
            titlebar.setTitle("热门车型");
        } else if (num == 2) {
            titlebar.setTitle("二手车");
        } else {
            hideFgm(titlebar);
        }
    }

    private void Imageclock() {
        titlebar.getRightView(2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AbsUI.startUI(context, MyMessageUI.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new MainStype(Constant.me_pop));
        AlertDialog.Builder builder = new AlertDialog.Builder(ui);
        builder.setTitle(" ");
        builder.setMessage("您确定退出吗？");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Config.isMonkeyEnabled() == false) {
                    UIManager.getInstance().finishAll();
                    MainApplication.quit(ui);
                }
            }

        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        }).create().show();
    }

    @Override
    protected void initControlEvent() {
    }

    @Override
    protected void initMember() {
        Log.i(TAG, "initMember-----" + hasOrder);
        super.addFgm(R.id.titlebar, titlebar);
    }

    @Override
    protected void onStartLoader() {

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        changeTitle(index);

    }

    @Subscribe
    public void onEventMainThread(MainStype ev) {
        Log.i(TAG, "收到广播------" + ev.getMsg());
        if (ev.getMsg() == Constant.Main_change) {
            changeToHot();
        } else if (ev.getMsg() == Constant.Main_changeSecond) {
            changeTOSecondCar();
        } else if (ev.getMsg() == Constant.Main_mine) {
            changeToMine();
        } else if (ev.getMsg() == Constant.mai_Urage) {
            finish();
        } else if (ev.getMsg() == 100) {
            AbsUI.startUI(context, MyMessageUI.class);
        }
    }

    private void changeTOSecondCar() {
        image_car2.setBackgroundResource(R.drawable.car_2_yes);
        tv_car2.setTextColor(Color.parseColor("#56B0F4"));
        tv_weixin.setTextColor(Color.parseColor("#A5AAD0"));
        tv_tongxunlu.setTextColor(Color.parseColor("#A5AAD0"));
        tv_faxian.setTextColor(Color.parseColor("#A5AAD0"));
        image_user.setBackgroundResource(R.drawable.user);
        image_car.setBackgroundResource(R.drawable.car);
        image_index.setBackgroundResource(R.drawable.index);
        adapter.onChange(2);
        changeTitle(2);
    }

    private void changeToMine() {
        Log.i(TAG, "changeToMine()");
        tv_weixin.setTextColor(Color.parseColor("#A5AAD0"));
        tv_tongxunlu.setTextColor(Color.parseColor("#A5AAD0"));
        tv_faxian.setTextColor(Color.parseColor("#56B0F4"));
        image_user.setBackgroundResource(R.drawable.user_click);
        image_car.setBackgroundResource(R.drawable.car);
        image_index.setBackgroundResource(R.drawable.index);
        image_car2.setBackgroundResource(R.drawable.car_2);
        tv_car2.setTextColor(Color.parseColor("#A5AAD0"));
        adapter.onChange(3);
        changeTitle(3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        utils.startOnce();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        utils.stop();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected byte[] doInBackgroundLoader() {
        return null;
    }

    // 跳转到热门车型界面
    public void changeToHot() {
        tv_weixin.setTextColor(Color.parseColor("#A5AAD0"));
        tv_tongxunlu.setTextColor(Color.parseColor("#56B0F4"));
        tv_faxian.setTextColor(Color.parseColor("#A5AAD0"));
        image_user.setBackgroundResource(R.drawable.user);
        image_car.setBackgroundResource(R.drawable.car_click);
        image_index.setBackgroundResource(R.drawable.index);
        image_car2.setBackgroundResource(R.drawable.car_2);
        tv_car2.setTextColor(Color.parseColor("#A5AAD0"));
        adapter.onChange(1);
        changeTitle(1);
    }

    @Override
    protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

    }

}
