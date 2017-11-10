package com.tools.net.http;

import android.content.Context;

import com.hst.Carlease.ram.HttpRam;
import com.tools.net.http.HttpTool.Error;
import com.tools.os.Charset;
import com.tools.util.Log;


public class TestHttpTool {

	private static final String TAG = TestHttpTool.class.getSimpleName();

	private static String url = "http://192.168.1.1/html/index.html?version=22.001.14.00.03";

	public static void mainTimeout(final Context context) {

		HttpTool http = new HttpTool(context);
		//				http.setTimeout(3000);
		http.setOnCompletedListener(new HttpTool.OnCompletedListener() {

			@Override
			public void onFailed(Error error, java.lang.Exception exception, int responseCode, byte[] bytes) {
				Log.e(TAG, "onFailed:"+error.name());
			}

			@Override
			public void onSuccessful(byte[] out) {

			}

		});

		byte[] out = http.doGet(url);
		Log.e(TAG, "out:"+out);

	}

	public static void mainURL(final Context context) {
		Log.e(TAG, "url:"+HttpTool.encode(url, "UTF-8"));
		Log.e(TAG, "url:"+HttpTool.encode(HttpTool.encode(url, "UTF-8"), "UTF-8"));
	}

	public static void doGet(Context context) {

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置

		http.setConfig( httpConfig ); // 设置HTTP配置

		String url = "http://rental.wisdom-gps.com/mobile.asmx/LoginNew?UserName=tiyan&Password=tiyan&PlatName=&ClientType=1";
		Log.e(TAG, "url:"+url);

		byte[] bytes = http.doGet(url, null);
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, HttpRam.getTextcharset(), HttpRam.getLocalcharset());
		Log.e(TAG, "text:"+text);

		// 提交
		//String resultLogin = Charset.convertString( http.doPost(HTTPURL.getLogin(), Charset.convert(urlParame, HttpRam.getTextcharset())), HttpRam.getTextcharset(), HttpRam.getLocalcharset() );

		//从XML获取json
		//		String resultLogin = XmlDOMUtil.getValue(http.doGet(url, null),HttpRam.getTextcharset());
		//		String resultLogin = XmlDOMUtil.getValue(http.doPost(HTTPURL.getLogin(), Charset.string2Bytes(urlParame, Charset.UTF_8)), HttpRam.getTextcharset());
		//
		//		// {"PageData":null,"Result":2,"ResultMsg":"","RowsCount":0,"Rows":null,"Total":0}
		//		Log.e(TAG, "resultLogin:"+resultLogin);
		//
		//		//从XML得到JSON内容类还未做，使用测试数据。。。
		//		//resultLogin = "{\"ComID\":1,\"ComName\":null,\"UsrID\":null,\"UserName\":\"tiyan\"}";
		//		Log.e(TAG, "resultLogin:"+resultLogin);
		//		Log.i(TAG, "SessionId http.getSessionId()--->"+http.getSessionId());

		new Thread() {

			@Override
			public void run() {
				// new Bean
				//				LoginRequestBean bean = new LoginRequestBean();
				//				bean.setUserName("mobile.zbb");
				//				bean.setPwd("Abc123123");
				//				// 将bean转为url_parame
				//				String url_parame = BeanTool.toURLEncoder(bean, HttpRam.getURLCharset());
				//				Log.e(TAG, "url_parame:"+url_parame);
				//				// url
				//				String url = "http://202.103.190.97:81/Mobile.asmx/Login?" + url_parame;
				//				Log.e(TAG, "url:"+url);
				//				HttpTool http = new HttpTool();
				//				http.setTimeout(HttpRam.getConnectTimeout(), HttpRam.getReadTimeout()); // 设置超时时间
				//				http.setContentTypeJSON(); // JSON协议
				//				String resultGet = HttpTool.bytes2String(http.doGet(url, null), HttpRam.getCharset());
				//				Log.e(TAG, "resultGet:"+resultGet);
			}

		}.start();

	}

	public static void doPost(Context context) {

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置

		http.setConfig( httpConfig ); // 设置HTTP配置

		String urlParame = "5555555555555";
		Log.e(TAG, "urlParame:"+urlParame);

//		String url = HTTPURL.getCarlist();
		String url = null;
		Log.e(TAG, "url:"+url);

		// 提交
		// String resultLogin = HttpTool.bytes2String(http.doGet(url, null), HttpRam.getCharset());
		// String resultLogin = Charset.convertString( http.doPost(HTTPURL.getLogin(), Charset.convert(urlParame, HttpRam.getTextcharset())), HttpRam.getTextcharset(), HttpRam.getLocalcharset() );

		http.setSessionId( HttpRam.getSessionId() );

		byte[] bytes = http.doPost(url, Charset.string2Bytes(urlParame, HttpRam.getTextcharset()));
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}
		
		String text = Charset.convertString(bytes, HttpRam.getTextcharset(), HttpRam.getLocalcharset());
		Log.e(TAG, "text:"+text);

		new Thread() {

			@Override
			public void run() {
				// new Bean
				//				CompanyList1Bean bean = new CompanyList1Bean();
				//				bean.setCompanyName("深圳");
				//				bean.setAreaGroupID("");
				//				// 将bean转为url_parame
				//				String url_parame = BeanTool.toURLEncoder(bean, HttpRam.getURLCharset());
				//				Log.e(TAG, "url_parame:"+url_parame);
				//				// url
				//				String url = "http://202.103.190.97:81/Mobile.asmx/GetCompanyList?";
				//				Log.e(TAG, "url:"+url);
				//				if (url_parame != null) {
				//					HttpTool http = new HttpTool();
				//					http.setTimeout(HttpRam.getConnectTimeout(), HttpRam.getReadTimeout()); // 设置超时时间
				//					http.setSessionId(HttpRam.getSessionId()); // 添加sessionId
				//					http.setCharset(HttpRam.getCharset()); // 设置返回数据的字符集
				//					String resultPost = HttpTool.bytes2String(http.doPost(url, url_parame.getBytes()), http.getCharset());
				//					Log.e(TAG, "resultPost:"+resultPost);
				//				}
			}

		}.start();

	}

	public static void __test_login(Context context) {

		//		Login1Bean bean = new Login1Bean();
		//		bean.setType("login");
		//		bean.setLoginName("");
		//		bean.setPassword(password);
		// 设备号(IOS要传，Android不用传)
		//		bean.setTerminalNo(null);

		//		// HTTP头配置
		//		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型
		//		
		//		// HTTP配置
		//		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		//		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置

		HttpTool http = null;
		if (http == null) {
			http = new HttpTool(context);
		}

		//		http.setConfig( httpConfig ); // 设置HTTP配置

		// 设置超时
		//		http.setTimeout(HttpRam.getConnectTimeout(), HttpRam.getReadTimeout());

		// 将bean转为url
		//		String urlParame = BeanTool.toURLEncoder(bean, HttpRam.getUrlcharset());
		// http://carlife.wisdom-gps.com/Handler/Login.aspx?LoginName=uus&Password=sssa&TerminalNo=android&Type=login
		//		String url = HTTPURL.getLogin() + urlParame;
		String url = "http://carlive.wisdom-gps.com:8010/Handler/Login.aspx?LoginName=pabc123&Password=1&Type=login";
		Log.e(TAG, "url:"+url);

		// 提交
		//		String resultLogin = HttpTool.bytes2String(http.doGet(url, null), HttpRam.getCharset());
		//		String resultLogin = Charset.convertString( http.doGet(url, null), HttpRam.getTextcharset(), HttpRam.getLocalcharset() );
		// {"PageData":null,"Result":2,"ResultMsg":"","RowsCount":0,"Rows":null,"Total":0}
		//		Log.e(TAG, "resultLogin:"+resultLogin);

		//		Login2Bean bean = GJson.parseObject(resultLogin, Login2Bean.class);
		//		if (bean != null) {
		//			bean.print();
		//		}

		//		return resultLogin;
	}

}