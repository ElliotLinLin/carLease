package com.hst.Carlease.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.ContractDetailsBean;
import com.hst.Carlease.http.bean.RegisterBean;
import com.hst.Carlease.ram.Http_Url;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.widget.Prompt;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public class RentCarWebviewUI extends AbsUI2 {
	private static final String TAG = WebViewUI.class.getSimpleName();
	private TitleBar titleBar = null;// 标题
	private WebView webView;
	private Intent intent;

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
		setWebView();
		getInfoAysncHttpGet();

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
		titleBar.setTitle("合同详情");
		super.setViewVisibility(titleBar.getLeftView(1), true);
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
				 int w = View.MeasureSpec.makeMeasureSpec(0,  
                         View.MeasureSpec.UNSPECIFIED);  
                 int h = View.MeasureSpec.makeMeasureSpec(0,  
                         View.MeasureSpec.UNSPECIFIED);  
                 //重新测量  
                 webView.measure(w, h);
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
		webSettings.setUseWideViewPort(true); 
	    webSettings.setLoadWithOverviewMode(true); 
	     //支持屏幕缩放
		webSettings.setSupportZoom(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
	     //不显示webview缩放按钮
		webSettings.setDisplayZoomControls(false);
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
		ContractDetailsBean getBean = new ContractDetailsBean();
		getBean.setId(intent.getStringExtra("id"));
		try {
			AsyncHttpUtil.post(context, Http_Url.GetContractDetailsByID, getBean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							webView) {

								@Override
								public void myFailure(int arg0, Header[] arg1,
										String arg2, Throwable arg3) {
									
								}

								@Override
								public void mySuccess(int arg0, Header[] arg1,
										String arg2) {
									Log.i(TAG, "arg2=---"+arg2);
									Bean bean = GJson.parseObject(arg2, Bean.class);
									Log.i(TAG, "result 截取" + bean.getD());
									if (bean.getD()!=null) {
										RegisterBean Registe=GJson.parseObject(bean.getD(), RegisterBean.class);
										if (Registe!=null&&Registe.getModel()!=null) {
											String model = Registe.getModel();
											webView.loadUrl(model);
										}
										
									}
								}
				
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	};

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
