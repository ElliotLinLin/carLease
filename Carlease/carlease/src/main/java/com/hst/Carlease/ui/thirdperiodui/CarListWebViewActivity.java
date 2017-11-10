package com.hst.Carlease.ui.thirdperiodui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hst.Carlease.R;
import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.ui.LoginUI;
import com.hst.Carlease.util.CompressUtil;
import com.hst.Carlease.util.SPUtils;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumListener;

import java.util.ArrayList;

public class CarListWebViewActivity extends ThirdBaseUi {

    private static final int FILE_CHOOSER_RESULT_CODE = 101;
    private WebView mWebView;
    private String mUrl;
    private TitleBar titleBar;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    private Intent mSourceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_car_list_web_view);
        super.onCreate(savedInstanceState);
//        AndroidBug5497Workaround.assistActivity(this);
        mWebView = (WebView) findViewById(R.id.webview);
        mUrl = getIntent().getStringExtra("url");
//        mUrl = "http://192.168.80.5:99/html5/erp_business/addCustomerInfo.html?token=11857&id=39660&comId=10000";
        initWebView();

        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        } else {
            mWebView.loadUrl(mUrl);
        }
    }


    @Override
    protected void initControl() {
        titleBar = new TitleBar();

    }

    @Override
    protected void initControlEvent() {

    }

    @Override
    public void onAttachedToWindow() {

        titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        try {
                            Instrumentation inst = new Instrumentation();
                            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
        super.onAttachedToWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initMember() {
        super.addFgm(R.id.titlebar, titleBar);
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        mWebView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + "cache";
//      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
//        Log.i(TAG, "cacheDirPath="+cacheDirPath);
        //设置数据库缓存路径
        mWebView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        mWebView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        mWebView.getSettings().setAppCacheEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.w("h5message", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                return true;
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                if ("2".equals(message)) {
                    AlertDialog.Builder b = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_HOLO_LIGHT);
                    b.setMessage("用户标识已过期，请重新登陆");
                    b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                            SPUtils.put(context, Constants.ForgetCode,
                                    "false");

                            AbsUI.startClearTopUI(context, LoginUI.class);
                            AbsUI.stopUI(ui);
                        }
                    });
                    b.setCancelable(false);
                    b.create().show();
                } else {
                    Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                    result.confirm();
                }

                return true;
            }

            //设置响应js 的Confirm()函数
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_HOLO_LIGHT);
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                b.create().show();
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titleBar.setTitle(title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(CarListWebViewActivity.this, "加载错误", Toast.LENGTH_SHORT).show();
//                view.loadUrl("file:///android_asset/error.html");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
//                Toast.makeText(MainActivity.this, "页面加载完成", Toast.LENGTH_SHORT).show();
                titleBar.setTitle(view.getTitle());
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
                return needIntercept(url);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
//                if (needIntercept(url)) {
//                    return true;
//                }
//                return super.shouldOverrideUrlLoading(view, request);
                return needIntercept(url);
            }

        });

        mWebView.addJavascriptInterface(this, "android");

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @JavascriptInterface
    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean needIntercept(String url) {
        if (url.contains("yingshitongERP")) {
            String[] split = url.split("\\?");
            if (split.length == 2) {
                Intent intent = new Intent(CarListWebViewActivity.this, OutStoragePictureActivity.class);
                String s = split[1];
                String[] split1 = s.split("&");
                for (String s1 : split1) {
                    String[] split2 = s1.split("=");
                    if (split2.length == 2) {
                        intent.putExtra(split2[0], split2[1]);
                    }
                }
                startUI(CarListWebViewActivity.context, intent);
                return true;
            } else {
                showToast("数据错误");
                return true;
            }
        } else if (url.contains("CallPhone")) {
            String[] split = url.split("\\?");
            if (split.length == 2) {
                String s = split[1];
                String[] split1 = s.split("=");
                if (split1.length == 2) {
                    call(split1[1]);
                }

            } else {
                showToast("数据错误");
            }
            return true;
        } else if (url.contains("getPhotoUrl")) {
            String[] split = url.split("\\?");

            if (split.length == 2) {
                String s = split[1];
                String[] split1 = s.split("=");
                if (split1.length == 2) {
                    try {
                        showChosePic(Integer.parseInt(split1[1]));
                    } catch (NumberFormatException e) {
                        showToast("您不能选取照片了");
                    }
                }
            } else {
                showToast("数据错误");
            }
            return true;
        }
        return false;
    }

    public void showChosePic(int num) {
        Album.image(CarListWebViewActivity.this) // 选择图片。
                .multipleChoice()
                .requestCode(200)
                .camera(true)
                .columnCount(3)
                .selectCount(num)
                .listener(new AlbumListener<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        ArrayList<String> newList = new ArrayList<String>();
                        for (AlbumFile file : result) {
                            newList.add(getBase64String(file.getPath()));
                        }
                        String s = "";
                        for (int i = 0; i < result.size(); i++) {
                            if (i == 0) {
                                s += "[\"" + getBase64String(result.get(i).getPath()) + "\"";
                            } else {
                                s += ",\"" + getBase64String(result.get(i).getPath()) + "\"";
                            }
                        }
                        s += "]";

//                        String s = new Gson().toJson(newList);

                        Log.w("CarListWebViewActivity", s);
                        mWebView.loadUrl("javascript:getPic('" + s + "')");
                    }

                    @Override
                    public void onAlbumCancel(int requestCode) {

                    }
                }).start();
    }


    /**
     * 创建图片数组---通过ImageLoader
     *
     * @return
     */
    private String getBase64String(String path) {
        Bitmap bitmap = CompressUtil.getimage(path);
        byte[] bytes = CompressUtil.compressImage(bitmap);
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        // 回收
        bitmap.recycle();
        return base64;
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
