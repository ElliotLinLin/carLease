package com.hst.Carlease.ui;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hst.Carlease.R;
import com.tools.app.AbsUI;
import com.tools.app.TitleBar;

public class TmWebViewUI extends AbsUI{
	private TitleBar titleBar = null;// 标题

	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ui_webview);
		super.onCreate(savedInstanceState);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	}

	@Override
	protected void initControl() {
		titleBar = new TitleBar();
		webView = (WebView) findViewById(R.id.wv_webview);
		setWebView();
		webView.loadUrl("https://yingshitongqc.m.tmall.com/");
//		webView.loadUrl("http://hstoa.wisdom-gps.com:8888/Frames/Default.aspx");
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
		titleBar.setTitle("");
		super.setViewVisibility(titleBar.getLeftView(1), true);
		titleBar.getLeftView(1).setBackgroundResource(
				R.drawable.tools_btn_back_selector);
		super.setViewVisibility(titleBar.getRightView(1), true);
		titleBar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (webView.canGoBack()) {
					webView.goBack();
				}else {
					finish();
				}
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
			public void onPageFinished(WebView view, String url) {
				titleBar.setTitle(view.getTitle());
				super.onPageFinished(view, url);
			}

		});

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return false;
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
			

		});

		// webView.setInitialScale(100);//web端设置大小
		WebSettings webSettings = webView.getSettings();
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setSupportZoom(true);
		webSettings.setDefaultZoom(ZoomDensity.CLOSE);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口 
		webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片

	}
	@Override
	protected void onStartLoader() {

	}
	//覆盖Acitivity类的onkeydown
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK&&webView.canGoBack()) {
			webView.goBack();
			return true;
		}else {
			finish();
		}
		
		return false;
	}

	@Override
	protected byte[] doInBackgroundLoader() {
		return null;
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

	}

}
