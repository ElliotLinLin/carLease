package com.hst.Carlease.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.Loader;
import android.view.KeyEvent;

import com.hst.Carlease.R;
import com.tools.app.AbsUI;
import com.tools.util.Log;

/**
 * 接收推送消息的过渡页
 *
 * @author HL
 */
public class LauncherPassUI extends AbsUI {

    private static final String TAG = LauncherPassUI.class.getSimpleName();

    public static final String Extra_CLS = "cls";

    private static final int MSG_1 = 1;//消息常量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "LauncherGuideUI=====");

        setContentView(R.layout.ui_launcher);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initControl() {
        Log.i(TAG, "initControl()");


    }

    @Override
    protected void initControlEvent() {
        Log.i(TAG, "initControlEvent()");

    }

    @Override
    protected void initMember() {
        Log.i(TAG, "initMember()");

        super.setSlideFinishEnabled(false); // 设置不可左右滑动退出
        super.setBackAnimationEnabled(false); // 是否允许退出动画


        handler.sendEmptyMessage(MSG_1);


    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == MSG_1) {


//							// 跳转
//							if (!LoginUI.islogin) {
//								// 没有登录成功，表示应用还没有启动。
////								UIManager.getInstance().finishAll(LauncherGuideUI.class);
//								// 启动cls，保留intent数据
//								AbsUI.startUI(context, LauncherGuideUI.class);
//								stopUI(ui);
//								Log.i(TAG, "应用没有启动no. NO no NO");
//							} else {
//								// 登录成功，表示应用已启动，在前台或者后台运行中
//								Log.i(TAG, "应用已启动ok. OK  OK  OK  OK ");
//								// 启动cls，保留intent数据
//								AbsUI.startUI(context, MyMessageUI.class);
                Intent intent = new Intent();
                intent.putExtra("from", "push");
                AbsUI.startUI(context, LoginUI.class,intent);
                stopUI(ui);
//							}

            }
            super.handleMessage(msg);
        }

    };


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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            Log.e(TAG, "禁用返回键");
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
