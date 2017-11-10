package com.hst.Carlease.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hst.Carlease.R;
import com.hst.Carlease.app.MainApplication;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.eventBean.MainStype;
import com.hst.Carlease.eventBean.Stype;
import com.hst.Carlease.http.bean.LoginBean2;
import com.hst.Carlease.operate.PushServiceOperate;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.task.LoginTask;
import com.hst.Carlease.ui.fragement.MainUI2;
import com.hst.Carlease.ui.thirdperiodui.ThirdMainActivity;
import com.hst.Carlease.util.LocationChangedUtils;
import com.hst.Carlease.util.SPUtils;
import com.hst.Carlease.util.UpgradeTaskUtil;
import com.hst.Carlease.widget.mywidget.PopRegister;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.loopj.android.http.RequestParams;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.app.Config;
import com.tools.app.UIManager;
import com.tools.json.GJson;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 登陆界面
 *
 * @author wzy
 */
public class LoginUI extends AbsUI {
    public static final String TAG = LoginUI.class.getSimpleName();
    private TextView loginButton;// 登录按钮
    private TextView registerTextView;// 注册按钮
    private TextView forgetpwdTextView;// 忘记密码
    private EditText et_login_Account;// 帐号
    private EditText et_login_Password;// 密码
    public static String customerName = "";// 目前保存的用户名，后期修改
    private LoginTask task;
    private CheckBox checkbox;// 判断是否记住密码
    private Button btn_login_clear2;
    private boolean visiable = false;
    LocationChangedUtils utils;
    String ForgetCode = "";// 是否记住密码
    private static PopRegister popwindow;
    private String mFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setNoSoftKeyboard();
        EventBus.getDefault().register(this);
        setSlideFinishEnabled(true);
        setContentView(R.layout.ui_login);
        super.onCreate(savedInstanceState);
        mFrom = getIntent().getStringExtra("from");
    }

    // protected void setNoSoftKeyboard() {
    // getWindow().setSoftInputMode(
    // WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    // }

    @Override
    protected void initControl() {
        btn_login_clear2 = (Button) findViewById(R.id.btn_login_clear2);
        utils = new LocationChangedUtils(context, ui);
        loginButton = (TextView) findViewById(R.id.btn_login_Login);
        registerTextView = (TextView) findViewById(R.id.tv_login_Register);
        forgetpwdTextView = (TextView) findViewById(R.id.tv_login_forgetpwd);
        et_login_Account = (EditText) findViewById(R.id.et_login_Account);
        et_login_Password = (EditText) findViewById(R.id.et_login_Password);

        if (SPUtils.get(context, Constants.UserName, "") != null) {
            customerName = SPUtils.get(context, Constants.UserName, "")
                    .toString();
            et_login_Account.setText(customerName);

        }
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        String pwd = SPUtils.get(context, Constants.Pwd, "").toString();
        ForgetCode = SPUtils.get(context, Constants.ForgetCode, "false")
                .toString();
        if (ForgetCode.equals("true")) {
            et_login_Password.setText(pwd);
            checkbox.setChecked(true);
        } else {
            checkbox.setChecked(false);
        }
        et_login_Password.setLongClickable(false);

    }

    private void checkVersion() {
        UpgradeTaskUtil upgradeTaskExt = new UpgradeTaskUtil(ui);
        upgradeTaskExt.check(true, "检查版本更新...");
    }

    @Override
    protected void onResume() {
        utils.startOnce();
        super.onResume();
    }

    @Override
    protected void initControlEvent() {
        btn_login_clear2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (visiable) {
                    visiable = false;
                    et_login_Password
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);// 设置密码不可见
                } else {
                    visiable = true;
                    et_login_Password
                            .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD
                                    | InputType.TYPE_CLASS_TEXT);// 设置密码可见，如果只设置TYPE_TEXT_VARIATION_PASSWORD则无效);
                }
                et_login_Password.setSelection(et_login_Password.getText()
                        .toString().length());
            }
        });
        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isEmptyString(et_login_Account.getText().toString().trim())) {
                    ToastL.show("请输入账号");
                    return;
                }
                if (isEmptyString(et_login_Password.getText().toString().trim())) {
                    ToastL.show("请输入密码");
                    return;
                }
                loginTask();

            }
        });
        registerTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AbsUI.startUI(context, RegisterUI.class);
            }
        });
        forgetpwdTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AbsUI.startUI(context, FindPassUI.class);
            }
        });
        et_login_Account.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        et_login_Password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

    }

    @Subscribe
    public void onEventMainThread(Stype ev) {
        if (ev.getMsg() == Constant.LOGIN_UI) {
            if (ForgetCode.equals("true")) {
                loginTask();
            }
        } else if (ev.getMsg() == Constant.LOGIN_Urage) {
            checkVersion();
        } else if (ev.getMsg() == Constant.update_username) {
            if (SPUtils.get(context, Constants.UserName, "") != null) {
                customerName = SPUtils.get(context, Constants.UserName, "")
                        .toString();
                et_login_Account.setText(customerName);

            }
            et_login_Password.setText("");
        }

    }

    @Override
    protected void onDestroy() {
        utils.stop();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initMember() {
        super.setSlideFinishEnabled(false); // 设置不可左右滑动退出
    }

    @Override
    public void onAttachedToWindow() {

        super.onAttachedToWindow();
    }

    /**
     * 登录网络请求
     */
    private void loginTask() {
        RequestParams param = new RequestParams();
        // • phone 【手机号码】 password 【登陆密码】
        param.put("phone", et_login_Account.getText().toString());
        param.put("password", et_login_Password.getText().toString());
        param.put("deviceNo", getIMEI(context));
        param.put("LoginAddr", utils.cityname);
        param.put("client", "1");// 客户端标识 1安卓 2IOS
        AsyncHttpUtil.post(Http_Url.userLoginNew, context, param,
                new AsyncCallBackHandler(this, "", true, loginButton) {

                    @Override
                    public void myFailure(int arg0, Header[] arg1, String arg2,
                                          Throwable arg3) {
                    }

                    @Override
                    public void mySuccess(int arg0, Header[] arg1, String result) {
                        Log.i(TAG, "result==" + result);
                        LoginBean2 bean = GJson.parseObject(result,
                                LoginBean2.class);
                        if (bean == null) {
                            return;
                        }
                        SPUtils.put(ui, Constants.UserName, et_login_Account
                                .getText().toString());
                        if (bean == null) {
                            Toast.makeText(ui, "数据格式错误", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 1：登陆成功，-1：登陆失败
                        if (bean.getStatu() == 1) {
                            SPUtils.put(ui, Constants.ForgetCode,
                                    checkbox.isChecked() + "");
                            SPUtils.put(ui, Constants.Pwd, et_login_Password
                                    .getText().toString());
                            SPUtils.put(ui, Constants.StaffID, bean.getModel().getGuid() + "");//staffd即为guid
                            if (!TextUtils.isEmpty(bean.getModel().getGuid())) {
                                SPUtils.put(ui, Constants.tokenID, bean.getModel()
                                        .getGuid());
                            }
                            if (!TextUtils.isEmpty(bean.getModel().getCompanyID() + "")) {
                                SPUtils.put(ui, Constants.CompanyID, bean.getModel().getCompanyID() + "");
                            }
                            if (bean.getModel().getIsHire() == 1) {
                                SPUtils.put(ui, Constants.hasOrder, true);
                            } else {
                                SPUtils.put(ui, Constants.hasOrder, false);
                            }
                            SPUtils.put(ui, Constants.DeviceNO, getIMEI(context));

                            PushServiceOperate.tryStart(ui);
                            if (2 == bean.getModel().getPersonType()) {
                                AbsUI.startClearTopUI(ui, ThirdMainActivity.class);
                            } else {
                                if ("push".equals(mFrom)) {
                                    AbsUI.startClearTopUI(ui, MainUI2.class);
                                    EventBus.getDefault().post(new MainStype(100));
                                } else {
                                    AbsUI.startClearTopUI(ui, MainUI2.class);
                                }
                            }
                            AbsUI.stopUI(ui);

                        } else if (bean.getStatu() == -3) {
                            popWindowShow();
                        } else {
                            ToastL.show(bean.getMsg());
                        }

                    }
                });
    }

    // 显示弹框
    private void popWindowShow() {
        popwindow = new PopRegister(ui, loginButton, "请进行老用户绑定", "关闭");
        loginButton.postDelayed(new Runnable() {

            @Override
            public void run() {
                popwindow.show();
                // 老用户绑定
                popwindow.setalipay(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popwindow.close();
                        // 执行登录任务进入主界面
                    }
                });
                popwindow.setweixinPay(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popwindow.close();
                        AbsUI2.startUI(context, OldUserBingUI.class);
                    }
                });
            }
        }, 300);
    }

    /**
     * 获取手机imei
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        // 获取手机IMEI
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String imei = mTelephonyMgr.getDeviceId();
        return imei;
    }

    @Override
    protected void onStartLoader() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Config.isMonkeyEnabled() == false) {
            UIManager.getInstance().finishAll();
            MainApplication.quit(ui);
        }
    }

    @Override
    protected byte[] doInBackgroundLoader() {

        return null;
    }

    @Override
    protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

    }

}
