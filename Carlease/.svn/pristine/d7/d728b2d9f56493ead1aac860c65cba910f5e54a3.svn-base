package com.hst.Carlease.ui;

import org.apache.http.Header;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.http.bean.GetInfo1Bean;
import com.hst.Carlease.http.bean.GetInfo2Bean;
import com.hst.Carlease.ram.HTTPURL;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.widget.Prompt;

/**
 * 协议界面
 * 
 * @author wzy
 * 
 */
public class WebViewUI extends AbsUI {
	private static final String TAG = WebViewUI.class.getSimpleName();
	private TitleBar titleBar = null;// 标题

	private WebView webView;

	private Intent intent;

	private String name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ui_webview);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		titleBar = new TitleBar();
		intent = getIntent();
		webView = (WebView) findViewById(R.id.wv_webview);
//		setWebView();
//		getInfoAysncHttpGet();

	}

	@Override
	protected void initControlEvent() {

	}

	@Override
	protected void initMember() {
		super.addFgm(R.id.titleBar_webview, titleBar);
		super.setSlideFinishEnabled(false); // 设置不可左右滑动退出
	}

	@Override
	public void onAttachedToWindow() {
		titleBar.setTitle("会员服务协议");
		if (intent.getStringExtra("UI") != null) {
			if (intent.getStringExtra("UI").equals("RegisterUI")) {
				titleBar.setTitle("会员服务协议");
			} else if (intent.getStringExtra("UI").equals("ConfirmOrderUI")) {
				titleBar.setTitle("会员服务协议");
			}
		}

		super.setViewVisibility(titleBar.getLeftView(1), true);
		titleBar.getLeftView(1).setBackgroundResource(
				R.drawable.tools_btn_back_selector);
		super.setViewVisibility(titleBar.getRightView(1), true);
		titleBar.getRightView(1).setText("同意");
		titleBar.getRightView(1).setTextColor(Color.WHITE);
		titleBar.getRightView(1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (intent.getStringExtra("UI").equals("RegisterUI")) {
				//
				// }else{
				//
				// }
				if (intent.getStringExtra("UI") != null) {
					if (intent.getStringExtra("UI").equals("ConfirmOrderUI")) {
//						ConfirmOrderUI.cb_confirmorder_ServiceAgreement
//								.setChecked(true);
					} else {
//						RegisterUI.cb_register_ServiceAgreement
//								.setChecked(true);
					}
				}
				finish();

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

	/**
	 * 设置WebView控件
	 * 
	 */
	public void setWebView() {

		// 为了让WebView能够响应超链接功能
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}

		});

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return false;
			}

		});

		// webView.setInitialScale(100);//web端设置大小
		WebSettings webSettings = webView.getSettings();
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setSupportZoom(true);
		webSettings.setDefaultZoom(ZoomDensity.CLOSE);

	}

	/**
	 * 显示WebView
	 * 
	 * @param webView
	 *            要显示的控件
	 * @param contents
	 *            显示的内容
	 */
	public void showWebView(WebView webView, String contents) {
		if (webView != null && contents != null) {
			webView.loadDataWithBaseURL("", contents, "text/html", "UTF-8", "");
		} else if (isEmptyString(contents)) {
			showToast("内容为空");
		}
	}

	/**
	 * 获取协议
	 */
	protected void getInfoAysncHttpGet() {
		NetworkState nState = new NetworkState(ui);
		if (nState.isConnected() == false) {
			Prompt.showWarning(context, "请检查您的网络");
			return;
		}
		GetInfo1Bean getBean = new GetInfo1Bean();

		if (intent.getStringExtra("UI") != null) {
			if (intent.getStringExtra("UI").equals("RegisterUI")) {
				getBean.setInfoID(110);
			} else if (intent.getStringExtra("UI").equals("ConfirmOrderUI")) {
				getBean.setInfoID(111);
			}
		}

		AsyncHttpUtil.get(HTTPURL.getGetinfo(), getBean,
				new AsyncCallBackHandler(this, "获取中...", true,webView) {
					@Override
					public void mySuccess(int arg0, Header[] arg1, String data) {
						Log.e(TAG, "result:" + data);
						if (isEmptyString(data) == false) { // 不为空时，才判断isJSON
							GetInfo2Bean bean = GJson.parseObject(data,
									GetInfo2Bean.class);
							if (bean != null) {
								if (bean.getCode() == 0) {
									if (bean.getData() != null) {
										String contents = bean.getData()
												.getContent();
										name = bean.getData().getName();
										// titleBar.setTitle(name);
										// 显示
										showWebView(webView, contents);

									}
								} else {
									showToast(bean.getMsg());
								}
							}

						}

					}

					@Override
					public void myFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Log.e(TAG, "result:fail");
					}
				});

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
