package com.hst.Carlease.ui;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.asynchttp.AsyncCallBackHandler;
import com.hst.Carlease.asynchttp.AsyncHttpUtil;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.HomePageDetailsBean;
import com.hst.Carlease.http.bean.HomepageDetails2Bean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.widget.mywidget.ToastL;
import com.tools.app.AbsUI;
import com.tools.app.AbsUI2;
import com.tools.app.TitleBar;
import com.tools.json.GJson;
import com.tools.net.NetworkState;
import com.tools.widget.Prompt;

/**
 * 广告详情
 * 
 * @author lyq
 * @param <D>
 * 
 */
public class HomePageDetailsUI extends AbsUI2 {
	private TitleBar titlebar;
	private Intent intent;
	private TextView content, time, updatename;
	private WebView webdetails;
	private Button bn_order;
	private int id;
	private HomepageDetails2Bean beans;
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.ui_homepagedetails);
		super.setSlideFinishEnabled(false);
		super.onCreate(arg0);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {

			finish();
			return;
		}
	}

	@Override
	protected void initControl() {
		titlebar = new TitleBar();
		intent = getIntent();
		content = (TextView) findViewById(R.id.content);
		time = (TextView) findViewById(R.id.time);
		updatename = (TextView) findViewById(R.id.updatename);
		webdetails = (WebView) findViewById(R.id.webdetails);
		bn_order = (Button) findViewById(R.id.bn_order);
		WebSetting();
		GetListTask();
	}

	@Override
	protected void initControlEvent() {
		bn_order.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.putExtra("id", id);
				if (id == 0) {
					ToastL.show("没有匹配的车型");
					return;
				}
				AbsUI.startUI(context, HotModelDetailsUI.class, intent);
			}
		});
	}

	private void WebSetting() {
		
		// 为了让WebView能够响应超链接功能
		webdetails.setWebViewClient(new WebViewClient() {
			 @Override
			    public void onPageFinished(WebView view, String url) {
			        super.onPageFinished(view, url);
			        imgReset();//重置webview中img标签的图片大小
			        // html加载完成之后，添加监听图片的点击js函数
			      //这个是一定要加上那个的,配合scrollView和WebView的height=wrap_content属性使用  
	                   int w = View.MeasureSpec.makeMeasureSpec(0,  
	                           View.MeasureSpec.UNSPECIFIED);  
	                   int h = View.MeasureSpec.makeMeasureSpec(0,  
	                           View.MeasureSpec.UNSPECIFIED);  
	                   //重新测量  
	                   webdetails.measure(w, h);  
			    }
			    @Override
			    public boolean shouldOverrideUrlLoading(WebView view, String url) {
			        view.loadUrl(url);
			        return true;
			    }
			    @Override
			    public void onReceivedSslError(WebView view,
			    		SslErrorHandler handler, SslError error) {
			    	handler.proceed();
			    	super.onReceivedSslError(view, handler, error);
			    }

		});

		// webdetails.addJavascriptInterface(this, "App2");

		webdetails.setWebChromeClient(new WebChromeClient() {

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return false;
			}

		});

		WebSettings webSettings = webdetails.getSettings();
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		webSettings.setUseWideViewPort(true); 
//	    webSettings.setLoadWithOverviewMode(true); 
	    webSettings.setJavaScriptEnabled(true);//支持js
	 // 清除缓存和记录
	    webdetails.clearCache(true);
	    webdetails.clearHistory();
//		webSettings.setBuiltInZoomControls(false);
//		webSettings.setSupportZoom(false);
//		webSettings.setTextSize(TextSize.SMALLER);
	}
	@Override
	protected void initMember() {
		super.addFgm(R.id.titlebar, titlebar);
	}

	// 找回密码的任务
	private void GetListTask() {
		HomePageDetailsBean bean = new HomePageDetailsBean();
		bean.setId(intent.getIntExtra("id", 1));
		NetworkState state = new NetworkState(context);
		if (state.isConnected() == false) {
			Prompt.showWarning(context, "请检查您的网络");
			return;
		}

		try {
			AsyncHttpUtil.post(context, Http_Url.GetInfo, bean,
					"application/json", new AsyncCallBackHandler(ui, "", true,
							content) {

						@Override
						public void myFailure(int arg0, Header[] arg1,
								String arg2, Throwable arg3) {
						}

						@Override
						public void mySuccess(int arg0, Header[] arg1,
								String arg2) {
							Bean d = GJson.parseObject(arg2, Bean.class);
							if (d != null) {
								 beans = GJson.parseObject(
										d.getD(), HomepageDetails2Bean.class);
								if (beans != null && beans.getModel() != null
										&& beans.getStatu() == 1) {
									setView(beans.getModel());
								} else {
									ToastL.show(beans.getMsg());
								}

							}

						}

					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	// 给view赋值
	private void setView(HomepageDetails2Bean.HomepageDetails beans) {
		content.setText("" + beans.getAdvertName());
		time.setText("创建时间：" + beans.getCreateTime());
		id = beans.getHcmID();
		showWebView(webdetails,beans.getAdvertContent());
//		showWebView(webdetails,getNewContent(beans.getAdvertContent()));
		// updatename.setText("修改人名字：" + beans.getUpdateName());
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
			webView.loadDataWithBaseURL(null, contents,
					"text/html", "UTF-8", null);
		} else if (isEmptyString(contents)) {
			Prompt.showWarning(context, "内容为空");
		}
	}

	/**
	 * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
	 **/
	private void imgReset() {
		webdetails.loadUrl("javascript:(function(){" +
	                "var objs = document.getElementsByTagName('img'); " +
	                "for(var i=0;i<objs.length;i++)  " +
	                "{"
	                + "var img = objs[i];   " +
	                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
	                "}" +
	                "})()");
		webdetails.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
	    }
	@Override
	public void onAttachedToWindow() {
		titlebar.getLeftView(2).setVisibility(View.GONE);
		titlebar.getTitleView().setVisibility(View.GONE);
		titlebar.getLeftView(1).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (webdetails.canGoBack()) {
					webdetails.goBack();
					
				}else {
					finish();
				}
			}
		});
		titlebar.getLeftView(2).setOnClickListener(new View.OnClickListener() {

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
	protected byte[] doInBackgroundLoader() {
		return null;
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	//覆盖Acitivity类的onkeydown
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode==KeyEvent.KEYCODE_BACK&&webdetails.canGoBack()) {
				webdetails.goBack();
				return true;
			}else {
				finish();
			}
			
			return false;
		}


	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {

	}

}
