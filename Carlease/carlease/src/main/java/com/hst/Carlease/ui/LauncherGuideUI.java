package com.hst.Carlease.ui;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.Loader;
import android.view.KeyEvent;

import com.hst.Carlease.R;
import com.hst.Carlease.eventBean.Stype;
import com.hst.Carlease.ram.Constant;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * 启动页
 *
 * @author HL
 */
public class LauncherGuideUI extends AbsUI2 {

    private static final String TAG = LauncherGuideUI.class.getSimpleName();
    private static final int MSG_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ui_launcher);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            // 当获取到资料（包括图片地址），则设置Gallery显示图片
            if (msg.what == MSG_1) {
                AbsUI.startUI(context, LoginUI.class);
                AbsUI2.stopUI(LauncherGuideUI.this);
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        EventBus.getDefault().post(new Stype(Constant.LOGIN_Urage));
                    }
                }, 200);
            }

            super.handleMessage(msg);
        }

    };

    @Override
    protected void initControl() {
        Log.i(TAG, "initControl()");
        handler.sendEmptyMessage(MSG_1);
    }

    @Override
    protected void initControlEvent() {
        Log.i(TAG, "initControlEvent()");

    }

    @Override
    protected void initMember() {
        super.setSlideFinishEnabled(false); // 设置不可左右滑动退出
        super.setBackAnimationEnabled(false); // 是否允许退出动画

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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            Log.e(TAG, "禁用返回键");
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
