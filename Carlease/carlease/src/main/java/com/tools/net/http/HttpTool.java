package com.tools.net.http;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.tools.content.pm.PermissionTool;
import com.tools.net.NetworkState;
import com.tools.os.Charset;
import com.tools.util.Log;
import com.tools.util.zip.GZIP;

/**
 * 
 * 加入权限:
 * <uses-permission android:name="android.permission.INTERNET"/>
 * 
 * 
 * GET方式传送数据量小，处理效率高，安全性低，会被缓存，而POST反之。
 * Post传输的数据量大，可以达到2M，而Get方法由于受到URL长度的限制,只能传递大约1024字节. 
 * 
 * get 而且参数的大小也是有限制的，一般是1K左右吧
 * 使用get时，提交时中文先要经过URLEncoder编码
 * 
 * post请求方式是将参数放在消息体内将其发送到服务器，所以对大小没有限制
 * 
 * 如果提交的网址经过URLDecoder编码后，通过URL访问提交，则
 * 必须设置conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
 * 
 * 共有几种方式可以提交POST数据？
 * 	String data = null;
	1) HttpPost.setEntity(new StringEntity(data, HTTP.UTF_8)); // 提交字符串，如JSON --- 放在post body，格式是原型
	2) HttpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); --- 放在post body，格式为username=admin&password=123456
	3）HttpPost.addHeader("AABBCC", "112233"); --- 放在post header里，格式为AABBCC: 112233（KEY为大写）
	4) HttpURLConnection -> httpConn.setRequestProperty("AABBCC", "112233"); --- 放在post header里，格式为aabbcc: 112233（KEY为小写）
	5) HttpURLConnection -> conn.getOutputStream().write(data); --- 放在post body里，格式是原型
 *
 *
 *	 完成事件接口
		http.setOnCompletedListener(new HttpTool.OnCompletedListener() {

			@Override
			public void onSuccessed(byte[] out) {
				Log.e(TAG, "onSuccessed()");
			}

			@Override
			public void onFailed(com.tools.net.http.HttpTool.Error error, int responseCode) {
				Log.e(TAG, "onFailed()");
				if (error != null) {
					Log.e(TAG, "onFailed():error:"+error.name());
					if (error == HttpTool.Error.SocketTimeoutException) {
						("请求超时。");
					}else if (error == HttpTool.Error.SocketException) {
						("Socket异常。");
					}else if (error == HttpTool.Error.IOException) {
						("IO异常。");
					}else {
						String textError = String.format("异常：%s", error.name());
						(textError);
					}
				}
			}

		});


	MIME类型
	http://zh.wikipedia.org/wiki/MIME

android-async-http 是 Android 上的一个异步 HTTP 客户端开发包。

主要特性：

    Make asynchronous HTTP requests, handle responses in anonymous callbacks
    HTTP requests happen outside the UI thread
    Requests use a threadpool to cap concurrent resource usage
    GET/POST params builder (RequestParams)
    Multipart file uploads with no additional third party libraries
    Tiny size overhead to your application, only 19kb for everything
    Automatic smart request retries optimized for spotty mobile connections
    Automatic gzip response decoding support for super-fast requests
    Optional built-in response parsing into JSON (JsonHttpResponseHandler)
    Optional persistent cookie store, saves cookies into your app's SharedPreferences
 * 
 * 
 * 封装了很多方法
 * http://blog.csdn.net/lk_blog/article/details/7706348#comments
 * 
 * 
 * 1xx:信息响应类，表示接收到请求并且继续处理 
2xx:处理成功响应类，表示动作被成功接收、理解和接受 
3xx:重定向响应类，为了完成指定的动作，必须接受进一步处理 
4xx:客户端错误，客户请求包含语法错误或者是不能正确执行 
5xx:服务端错误，服务器不能正确执行一个正确的请求


 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class HttpTool {

	private static final String TAG = HttpTool.class.getSimpleName();

	protected static HttpConfig config = new HttpConfig();

	// 单个容器的最大值，数据全部会保存在 outputStream，不用担心MAX_LENGTH不够大。
	// 增大缓冲区，减少读数据次数。 
	// 数据：
	// 缓冲区大小  --- 数据包大小 --- 共计读次数
	// 1024 * 1  --- 65980 --- 77次
	// 1024 * 2  --- 65980 --- 45次 
	// 1024 * 3  --- 65980 --- 33次 --- 要这个
	// 1024 * 4  --- 65980 --- 36次
	// 1024 * 5  --- 65980 --- 35次 
	// 1024 * 10  --- 65980 --- 36次
	// 1024 * 20  --- 65980 --- 33次
	public static int MAX_BUFFER_LENGTH = 1024 * 3; // 建议3KB

	protected Pattern pattern = Pattern.compile("charset.*=.*>?", Pattern.CASE_INSENSITIVE);

	protected HttpURLConnection connection = null;

	// Cookie
	protected String cookie = null;
	// 是否使用会话ID
	protected boolean useSessionId = false;
	// 会话
	protected String sessionId = null;

	// 请求的网址
	protected String url = null;

	protected OnCompletedListener completedListener = null;

	protected Context context = null;

	// 失败的状态码
	protected int responseCode = -1;

	// 错误
	protected Error error = Error.None;

	public static final String Scheme_Http = "http";
	public static final String Scheme_Https = "https";

	public enum Method {
		GET,POST,DELETE,PUT,TRACE,HEAD,OPTIONS,CONNECT
	}

	// 错误（主要是四大类：无网络，响应码错误，异常，超时）
	public enum Error {
		None, // 无信息，没有调用之前

		ResponseCodeError, // 响应码错误

		NotNetwork, // 无网络

		ConnectException, // 连接异常（有可能是无网络，也有可能是服务器无法连接）
		// 解决方案：当遇到java.net.ConnectException时，再使用NetworkState.java判断网络情况。
		ConnectTimeoutException, // 连接超时异常

		IOException, // IO异常
		InterruptedIOException, // IO中断异常

		SocketException, // Socket异常
		SocketTimeoutException, // Socket超时异常

		FileNotFoundException, // 超链接不存在

		OtherException, // 其它异常

		Successful, // 已成功的
	}

	// 决定使用此接口
	public interface OnCompletedListener {
		void onSuccessful(byte[] out); // 已成功的
		// 当error == ResponseCodeError时，检查int responseCode才有意义
		void onFailed(Error error, java.lang.Exception exception, int responseCode, byte[] out); // 失败
	}

	public HttpTool(Context context) {
		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.INTERNET);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {
		this.context = context;
		initExecute();
	}

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 * 
	 * @param src
	 * @return
	 */
	protected static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 得到错误
	 * 
	 * @return
	 */
	public Error getError() {
		return error;
	}

	public static HttpConfig getConfig() {
		return config;
	}

	public void setConfig(HttpConfig config) {
		this.config = config;
	}

	/**
	 * 获得MIME类型
	 * 
	 * @return
	 */
	//	@Deprecated
	//	public String getContentType() {
	//		//		return contentType;
	//		return config.getHeader().getContentType();
	//	}
	//
	//	/**
	//	 * @param contentType
	//	 */
	//	@Deprecated
	//	public void setContentType(String contentType) {
	//		//		this.contentType = contentType;
	//		config.getHeader().setContentType(contentType, getCharset());
	//	}
	//
	//	@Deprecated
	//	public void setContentTypeJSON() {
	//		//		setContentType(HttpContentType.Application_JSON);
	//		config.getHeader().setContentType(HttpContentType.Application_JSON, getCharset());
	//	}
	//
	//	@Deprecated
	//	public void setContentTypeXML() {
	//		//		setContentType(HttpContentType.Application_XML);
	//		config.getHeader().setContentType(HttpContentType.Application_XML, getCharset());
	//	}
	//
	//	@Deprecated
	//	public String getCharset() {
	//		//		return charset;
	//		return config.getHeader().getCharset();
	//	}
	//
	//	@Deprecated
	//	public void setCharset(String charset) {
	//		//		this.charset = charset;
	//		config.getHeader().setCharset(charset);
	//	}

	/**
	 * 查找网页的字符集编码
	 * 
	 * @param pattern
	 * @param search
	 * @return
	 * @throws Exception
	 */
	protected String findCharset(Pattern pattern, String search) throws Exception {
		Matcher matcher = null;
		String matchStr = null;
		String charset = null;
		matcher = pattern.matcher(search);

		if (matcher == null) {
			// 发生异常，但不崩溃
			Log.exception(TAG, new NullPointerException("matcher == null"));
			return null;
		}

		if(matcher.find()) {
			matchStr = matcher.group();
			charset = matchStr.substring(matchStr.indexOf("=") + 1).replaceAll("[\"|/|/|/s].*[/>|>]", "");
		}
		return charset;
	}

	/**
	 * 字符集转换，有几种字符集Unicode、GB2312、UTF-8
	 * 
	 * @return
	 */
	protected String toCharset(String text, String encoding) {
		if (isEmptyString(text) || isEmptyString(encoding)) {
			return null;
		}

		try {
			return new String(text.getBytes(), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 单位为毫秒
	 * 
	 * @param connectTimeout
	 * @param readTimeout
	 */
	//	@Deprecated
	//	public void setTimeout(int connectTimeout, int readTimeout) {
	//		this.setConnectTimeout(connectTimeout);
	//		this.setReadTimeout(readTimeout);
	//	}
	//
	//	@Deprecated
	//	public int getConnectTimeout() {
	//		//		return connectTimeout;
	//		return config.getConnectTimeout();
	//	}
	//
	//	@Deprecated
	//	public void setConnectTimeout(int connectTimeout) {
	//		//		this.connectTimeout = connectTimeout;
	//		config.setConnectTimeout(connectTimeout);
	//	}
	//
	//	@Deprecated
	//	public int getReadTimeout() {
	//		//		return readTimeout;
	//		return config.getReadTimeout();
	//	}
	//
	//	@Deprecated
	//	public void setReadTimeout(int readTimeout) {
	//		//		this.readTimeout = readTimeout;
	//		config.setReadTimeout(readTimeout);
	//	}
	//
	//	@Deprecated
	//	public int getResponseCodeOK() {
	//		//		return responseCodeOK;
	//		return HttpURLConnection.HTTP_OK;
	//	}
	//
	//	@Deprecated
	//	public void setResponseCodeOK(int responseCodeOK) {
	//		//		this.responseCodeOK = responseCodeOK;
	//	}

	/**
	 * 打开连接
	 * 
	 * @param urlString
	 * @return
	 */
	protected HttpURLConnection openConnection(String urlString) {
		// 是否要经过编码
		//		if (isUseURLEncoder()) {
		//			// 编码
		//			urlString = encoder(urlString, getCharset());
		//		}
		this.url = urlString;

		if (isEmptyString(urlString)) {
			Log.e(TAG, "urlString == isEmptyString");
			return null;
		}

		URL urlConn = null;
		try {
			urlConn = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}

		HttpURLConnection conn = null;
		try {
			if (config.getProxy() != null) {
				Log.d(TAG, "proxy != null");
				// TODO 有几种方式要测试：1）http or https；2）http or socket4/5; 3）账号和密码或者匿名

				// 1)http匿名 ---> 测试通过
				// 2)https匿名 ---> 
				// 3)socket4/5匿名 ---> 
				// 4)http有密码 --->
				// 5)https有密码 --->
				// 6)socket4/5有密码 --->

				// 其中上述 3)好像只有HttpClient4才支持。
				// 代理测试成功
				// HttpConfig.Proxy转成java.net.Proxy
				HttpConfig.Proxy proxyConfig = config.getProxy();
				java.net.Proxy javaProxy = new java.net.Proxy(proxyConfig.getType(), new InetSocketAddress(proxyConfig.getHostname(), proxyConfig.getPort()));  
				conn = (HttpURLConnection)urlConn.openConnection( javaProxy );
			}else{
				Log.d(TAG, "proxy == null");
				conn = (HttpURLConnection)urlConn.openConnection();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return conn;
	}

	/**
	 * 初始化连接（公共参数）
	 * 
	 * @param conn
	 */
	protected void initConnection(HttpURLConnection conn) {
		if (conn == null) {
			return;
		}

		// TODO 如果是采用get不用下述二个方法
		// 内容编码
		//		conn.setRequestProperty(Property_Charset, this.charset);
		// 设置MIME类型
		//		conn.setRequestProperty(Property_Content_Type, this.contentType);

		// 不重定向，不跳转
		//设置此类是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
		//		HttpURLConnection.setFollowRedirects(true); // 全部的请求
		conn.setInstanceFollowRedirects(true);

		// 设置连接超时
		conn.setConnectTimeout(config.getConnectTimeout());

		// 设置读超时
		conn.setReadTimeout(config.getReadTimeout());

		// 是否使用缓冲（TODO 跟Cookie无关系）
		conn.setUseCaches(false);
	}

	/**
	 * 添加请求头信息
	 * 
	 * @param conn
	 * @param header
	 */
	protected void addHeader(HttpURLConnection conn, List<BasicNameValuePair> header) {
		Log.d(TAG, "------ addHeader() start ------");

		if (conn != null && header != null) {
			for (BasicNameValuePair pair : header) {
				String name = pair.getName();
				String value = pair.getValue();
				Log.e(TAG, "(1)add BasicNameValuePair--->name:"+name+",value:"+value);
				// TODO 还有一个value编码没有做。没有测试
				String tmpValue = encode(value, config.getHeader().getCharset());
				if (tmpValue == null) {
					value = tmpValue;
				}
				Log.e(TAG, "(2)add BasicNameValuePair--->name:"+name+",value:"+value);
				conn.setRequestProperty(name, value);
			}
		}
		Log.d(TAG, "------ addHeader() end ------");
	}

	/**
	 * 添加一个头信息
	 * 
	 * @param conn
	 * @param headerField
	 * @param value
	 */
	protected void addHeader(HttpURLConnection conn, String headerField, String value) {
		if (conn == null) {
			return;
		}
		conn.setRequestProperty(headerField, value);
	}

	/**
	 * 连接（TODO 不要用，会重复产生连接）
	 * 
	 * 注意：conn使得connect方法，会产生错误java.net.SocketException: recvfrom failed: ECONNRESET (Connection reset by peer)
	 * 所以connect()方法就不要了。
	 * 
	 * @param conn
	 */
	private boolean connect(HttpURLConnection conn) {
		Log.d(TAG, "------ connect() start ------");
		boolean bConnect = false;
		if (conn != null) {
			try {
				Log.d(TAG, "execute conn.connect() 111");
				conn.connect();
				Log.d(TAG, "execute conn.connect() 222");
				bConnect = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.d(TAG, "connect:"+bConnect);
		Log.d(TAG, "------ connect() end ------");
		return bConnect;
	}

	/**
	 * 断开连接
	 * 
	 * @param conn
	 */
	private void disconnect(HttpURLConnection conn) {
		Log.d(TAG, "------ disconnect() start ------");
		if (conn != null) {
			conn.disconnect();
			Log.d(TAG, "execute conn.disconnect()...");
		}
		Log.d(TAG, "------ disconnect() end ------");
	}

	/**
	 * 断开连接
	 */
	public void disconnect() {
		disconnect(connection);
	}

	/**
	 * 得到cookie
	 * 
	 * @param conn
	 * @return
	 */
	protected String getCookie(HttpURLConnection conn) {
		Log.d(TAG, "------ getCookie() start ------");
		String cookie = null;
		if (conn != null) {
			cookie = conn.getHeaderField(HttpHeaderField.Field_Set_Cookie);
		}
		Log.d(TAG, "cookie:"+cookie);
		Log.d(TAG, "------ getCookie() end ------");
		return cookie;
	}

	/**
	 * 判断是否使用SessionId
	 * 
	 * @return
	 */
	public boolean isUseSessionId() {
		Log.e(TAG, "isUseSessionId:"+useSessionId);
		return useSessionId;
	}

	/**
	 * 设置是否使用SessionId
	 * 
	 * @param enable
	 */
	public void setUseSessionId(boolean enable) {
		useSessionId = enable;
	}

	/**
	 * 设置SessionId
	 * 
	 * @param sessionId
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
		setUseSessionId(true);
	}

	/**
	 * 获取sessionId
	 * 
	 * @param sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * 得到最缓冲区最大值
	 * 
	 * @return
	 */
	public int getMaxBufferSize() {
		return MAX_BUFFER_LENGTH;
	}

	/**
	 * 得到SessionId会话ID
	 * 
	 * @param conn
	 * @return
	 */
	protected String getSessionIdFromCookie(String cookie, int indexStart, int indexEnd) {
		String sessionId = null;
		if (!isEmptyString(cookie)) {
			sessionId = cookie.substring(indexStart, indexEnd);
		}else{
			Log.exception(TAG, new NullPointerException("isEmptyString(cookie) == true"));
		}
		Log.d(TAG, "sessionId:"+sessionId);
		return sessionId;
	}

	/**
	 * 得到SessionId会话ID
	 * 
	 * @param cookie
	 * @return
	 */
	protected String getSessionIdFromCookie(String cookie) {
		Log.d(TAG, "------ getSessionIdFromCookie() start ------");
		String sessionId = null;
		if (!isEmptyString(cookie)) {
			int indexStart = 0;
			// TODO 如果找不到;则要长度设置为end
			int indexEnd = cookie.indexOf(';');
			sessionId = cookie.substring(indexStart, indexEnd);
		}
		Log.d(TAG, "sessionId:"+sessionId);
		Log.d(TAG, "------ getSessionIdFromCookie() end ------");
		return sessionId;
	}

	/**
	 * 添加头SessionId会话ID
	 * 
	 * @param conn
	 */
	protected void addHeaderSessionId(HttpURLConnection conn, String sessionId) {
		Log.d(TAG, "------ addHeaderSessionId() start ------");
		if (conn != null && !isEmptyString(sessionId)) {
			Log.d(TAG, "addHeaderSessionId()->sessionId:"+sessionId);
			conn.setRequestProperty(HttpHeaderField.Field_Cookie, sessionId);
			Log.d(TAG, "addHeaderSessionId()->sessionId --- success.");
		}
		Log.d(TAG, "------ addHeaderSessionId() end ------");
	}

	/**
	 * 得到响应字节数组
	 * 
	 * @return
	 */
	protected byte[] getResponseByteArray(InputStream inputStream) throws IOException {

		//		if (conn == null) {
		//			Log.exception(TAG, new NullPointerException("conn == null"));
		//			return null;
		//		}

		//		if (data != null) {
		//			OutputStream outputStream = null;
		//			outputStream = conn.getOutputStream();
		//			if (outputStream != null) {
		//				outputStream.write(data); // 输入参数
		//			}
		//		}

		// 得到输入流（即返回数据）
		//		InputStream inputStream = null;
		//		inputStream = conn.getInputStream();
		if (inputStream == null) {
			Log.exception(TAG, new NullPointerException("inputStream == null"));
			return null;
		}

		// 读输出流
		ByteArrayOutputStream byteOutputStream = null;
		byteOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[MAX_BUFFER_LENGTH];

		int len = 0;
		Log.e(TAG, "read buffer --- start");
		int readCount = 0; // 一共读了几次
		while((len = inputStream.read(buffer)) != -1) {
			readCount++;
			byteOutputStream.write(buffer, 0, len);
			byteOutputStream.flush();
			Log.e(TAG, "read buffer len:"+len);
		}
		Log.e(TAG, "read buffer --- end");

		Log.e(TAG, "一共读了几次:readCount:"+readCount);

		Log.e(TAG, "MAX_BUFFER_LENGTH:"+MAX_BUFFER_LENGTH);

		// 从输出流查找字符集
		//					if (charset == null) {
		//						charset = findCharset(pattern, outputStream.toString());
		//						Log.e(TAG, "OutputStream->mCharset:"+charset);
		//					}

		// 关闭流
		if (byteOutputStream != null) {
			byteOutputStream.close();
		}

		if (inputStream != null) {
			inputStream.close();
		}

		// 使用系统默认的字符集 
		//					if (charset == null) {
		//						charset = Charset.defaultCharset().name();
		//						Log.e(TAG, "System->defaultCharset:"+charset);
		//					}

		//					if (charset != null) {
		// 通过字符集转为可视字符串
		//			out = new String(outputStream.toByteArray(), charset);
		//					}else{
		//						out = new String(outputStream.toByteArray());
		//					}

		// if (this.completedListener != null) completedListener.onFailed(Error.IOException);

		//		if (outputStream == null) {
		//			Log.exception(TAG, new NullPointerException("outputStream == null"));
		//			return null;
		//		}

		byte[] bytes = byteOutputStream.toByteArray();
		if (bytes != null) {
			Log.d(TAG, "getResponseByteArray():bytes.len:"+bytes.length);
		}else{
			Log.d(TAG, "getResponseByteArray():bytes == null");
		}

		return bytes;
	}

	//	protected byte[] getResponseByteArray(HttpURLConnection conn) throws IOException {
	//
	//		if (conn == null) {
	//			Log.exception(TAG, new NullPointerException("conn == null"));
	//			return null;
	//		}

	//		if (data != null) {
	//			OutputStream outputStream = null;
	//			outputStream = conn.getOutputStream();
	//			if (outputStream != null) {
	//				outputStream.write(data); // 输入参数 
	//			}
	//		}

	// 得到输入流（即返回数据）
	//		InputStream inputStream = null;
	//		inputStream = conn.getInputStream();
	//		if (inputStream == null) {
	//			Log.exception(TAG, new NullPointerException("inputStream == null"));
	//			return null;
	//		}
	//
	//		// 读输出流
	//		ByteArrayOutputStream byteOutputStream = null;
	//		byteOutputStream = new ByteArrayOutputStream();
	//		byte[] buffer = new byte[MAX_BUFFER_LENGTH];
	//
	//		int len = 0;
	//		Log.e(TAG, "read buffer --- start");
	//		int readCount = 0; // 一共读了几次
	//		while((len = inputStream.read(buffer)) != -1) {
	//			readCount++;
	//			byteOutputStream.write(buffer, 0, len);
	//			byteOutputStream.flush();
	//			Log.e(TAG, "read buffer len:"+len);
	//		}
	//		Log.e(TAG, "read buffer --- end");
	//
	//		Log.e(TAG, "一共读了几次:readCount:"+readCount);
	//
	//		Log.e(TAG, "MAX_BUFFER_LENGTH:"+MAX_BUFFER_LENGTH);
	//
	//		// 从输出流查找字符集
	//		//					if (charset == null) {
	//		//						charset = findCharset(pattern, outputStream.toString());
	//		//						Log.e(TAG, "OutputStream->mCharset:"+charset);
	//		//					}
	//
	//		// 关闭流
	//		if (byteOutputStream != null) {
	//			byteOutputStream.close();
	//		}
	//
	//		if (inputStream != null) {
	//			inputStream.close();
	//		}
	//
	//		// 使用系统默认的字符集 
	//		//					if (charset == null) {
	//		//						charset = Charset.defaultCharset().name();
	//		//						Log.e(TAG, "System->defaultCharset:"+charset);
	//		//					}
	//
	//		//					if (charset != null) {
	//		// 通过字符集转为可视字符串
	//		//			out = new String(outputStream.toByteArray(), charset);
	//		//					}else{
	//		//						out = new String(outputStream.toByteArray());
	//		//					}
	//
	//		// if (this.completedListener != null) completedListener.onFailed(Error.IOException);
	//
	//		//		if (outputStream == null) {
	//		//			Log.exception(TAG, new NullPointerException("outputStream == null"));
	//		//			return null;
	//		//		}
	//
	//		return byteOutputStream.toByteArray();
	//	}

	/**
	 * 设置完成事件观察者
	 */
	public void setOnCompletedListener(OnCompletedListener listener) {
		this.completedListener = listener;
	}

	/**
	 * 执行get/post前，先初始化
	 */
	protected void initExecute() {
		this.error = Error.None;
		this.responseCode = -1;
	}

	/**
	 * doGet()[需要的]
	 * 
	 * @param url
	 * @return
	 */
	public byte[] doGet(String url) {
		Log.d(TAG, "doGet():url:"+url);
		return executeGet(url);
	}

	/**
	 * 
	 * 
	 * @param url
	 * @param proxy
	 * @return
	 */
	@Deprecated
	public byte[] doGet(String url, String data) {
		Log.e(TAG, "doGet()");
		return executeGet(url);
	}

	/**
	 * get执行体
	 * 
	 * @param url
	 * @param header
	 * @return
	 */
	protected byte[] executeGet(String url) {
		Log.e(TAG, "executeGet()");

		// 执行get/post前，先初始化
		initExecute();

		byte[] out = null;

		try {
			// 打开连接
			connection = openConnection(url);
			if (connection == null) {
				Log.exception(TAG, new NullPointerException("connection == null"));
				error = Error.ConnectException;
				if (this.completedListener != null) {
					completedListener.onFailed(Error.ConnectException, new NullPointerException("connection == null"), responseCode, null);
				}
				return null;
			}

			// 初始化连接（公共参数），如：设置超时
			initConnection(connection);

			// 设置请求模式
			Log.d(TAG, "Method.GET.name():"+Method.GET.name());
			connection.setRequestMethod(Method.GET.name());

			// 设置连接状态（运行正常）
			connection.setRequestProperty(HttpHeaderField.Field_Connection, HttpHeaderField.Connection_Close);

			// 添加头信息
			addHeader(connection, config.getHeader().getFields());

			// 添加sessionId
			if (isUseSessionId()) {
				addHeaderSessionId(connection, sessionId);
			}

			// 连接（会自动连接，这里就不要了）
			// connection.getResponseCode()会自动连接，所以connect(...)可不要
			//			boolean bConnect = connect(connection);
			//			if (!bConnect) {
			//				return null;
			//			}

			// Returns the response code returned by the remote HTTP server.

			// 得到响应码
			responseCode = connection.getResponseCode();
			Log.e(TAG, "connection.getResponseCode():"+responseCode);

			// 打印信息
			//			print(connection);
			printResponseHeader(connection);

			if (responseCode == HttpURLConnection.HTTP_OK) {
				// 状态等于200，才获取Cookie
				// 得到Cookie
				cookie = getCookie(connection);
				// 得到sessionId会话Id
				sessionId = getSessionIdFromCookie(cookie);

				// 读响应内容getInputStream
				out = getResponseByteArray( connection.getInputStream() );
			}else{
				// 状态码不等于200，也要读响应内容
				// 读响应内容getErrorStream
				out = getResponseByteArray( connection.getErrorStream() );
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 处理--->异常
			handleException(e);
			// 返回
			return null;
		} finally {
			// 断开
			disconnect(connection);
		}

		// 如果WEB端返回的是gzip, deflate压缩过的，则要解压。
		String contentEncoding = getHeader(connection, HttpHeaderField.Field_Content_Encoding);
		if ( isGZIP(contentEncoding) ) {
			// 为gzip, deflate时，则解压
			Log.e(TAG, "响应头是gzip, deflate，所以要GZIP.unCompress()");
			out = GZIP.unCompress(out);
		}

		// 处理--->状态码
		if (responseCode == HttpURLConnection.HTTP_OK) {
			// 成功
			error = Error.Successful;
			if (this.completedListener != null) {
				completedListener.onSuccessful( out );
			}
		} else {
			error = Error.ResponseCodeError;
			// 打印错误内容
			String exceptionText = String.format("conn.getResponseCode():"+responseCode+" != HTTP_OK");
			Log.exception(TAG, new IllegalStateException(exceptionText));
			// 打印，HTTP状态码不等于200时Web端返回错误内容
			String errorText = Charset.convertString(out, config.getHeader().getCharset(), Charset.UTF_8);
			Log.e(TAG, "HTTP状态码不等于200，并且Web端返回错误内容:"+errorText);
			// 回调错误
			if (this.completedListener != null) {
				completedListener.onFailed( Error.ResponseCodeError, new IllegalStateException(exceptionText), responseCode, out );
			}
		}

		return out;
	}

	/**
	 * post
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public byte[] doPost(String url, byte[] data) {
		Log.d(TAG, "doPost():url:"+url);
		return executePost(url, data);
	}

	/**
	 * post
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public byte[] doPost(String url, String data) {
		Log.d(TAG, "doPost():url:"+url);
		Log.d(TAG, "doPost():"+"data:"+data);
		byte[] bytes = Charset.string2Bytes(data, getConfig().getHeader().getCharset());
		return executePost(url, bytes);
	}
	/**
	 * post
	 * 
	 * @param url
	 * @return
	 */
	public byte[] doPost(String url) {
		Log.d(TAG, "doPost():url:"+url);
		Log.d(TAG, "doPost():"+"data == null");
		return executePost(url, null);
	}

	/**
	 * post执行体
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	protected byte[] executePost(String url, byte[] data) {
		Log.e(TAG, "executePost()");

		// 执行get/post前，先初始化
		initExecute();

		byte[] out = null;

		try {
			// 打开连接
			connection = openConnection(url);
			if (connection == null) {
				Log.exception(TAG, new NullPointerException("connection == null"));
				error = Error.ConnectException;
				if (this.completedListener != null) {
					completedListener.onFailed(Error.ConnectException, new NullPointerException("connection == null"), responseCode, null);
				}
				return null;
			}

			// 初始化连接（公共参数）
			initConnection(connection);

			// 设置请求模式
			Log.d(TAG, "Method.POST.name():"+Method.POST.name());
			connection.setRequestMethod(Method.POST.name());

			// 设置连接状态（运行正常）
			connection.setRequestProperty(HttpHeaderField.Field_Connection, HttpHeaderField.Connection_Close);

			// 允许对外传输数据
			connection.setDoOutput(true);
			// 允许服务器向客户端响应数据（或者发送数据）
			connection.setDoInput(true);

			// 添加头信息
			addHeader(connection, config.getHeader().getFields());

			// 设置contentType
			// 注意，post一定要采用HttpContentType.Application_UrlEncoded
			String contentType = String.format("%s; charset=%s", HttpContentType.Application_UrlEncoded, config.getHeader().getCharset());
			Log.d(TAG, "Content-Type:"+contentType);
			connection.setRequestProperty(HttpHeaderField.Field_Content_Type, contentType);

			// 如果config.getHeader().getFields()里有HttpHeader.Header_Accept_Encoding，则压缩data
			if (false && isValueExists(config.getHeader(), HttpHeaderField.Field_Accept_Encoding, HttpHeaderField.Encoding_GZIP)) {
				// TODO 目前暂不实现，因WEB不支持GZIP
				Log.e(TAG, "请求头设置了Accept-Encoding字段，所以要压缩数据GZIP.Compress(data)");
				data = GZIP.compress(data);
			}

			// 设置内容大小
			int length = 0;
			if (data != null) {
				Log.d(TAG, "data != null");
				length = data.length;
			} else {
				Log.e(TAG, "data == null");
			}
			Log.d(TAG, "data length:"+length);
			connection.setRequestProperty(HttpHeaderField.Field_Content_Length, String.valueOf(length));

			// 添加sessionId
			if (isUseSessionId()) {
				addHeaderSessionId(connection, sessionId);
			}

			// 写数据
			if (data != null && length > 0) {
				OutputStream outputStream = null;
				outputStream = connection.getOutputStream();
				if (outputStream != null) {
					outputStream.write(data); // 写入参数
				}
			}

			// 得到响应码
			responseCode = connection.getResponseCode();
			Log.e(TAG, "connection.getResponseCode():"+responseCode);

			// 打印信息
			//			print(connection);
			printResponseHeader(connection);

			if (responseCode == HttpURLConnection.HTTP_OK) {
				// 状态等于200，才获取Cookie
				// 得到Cookie
				cookie = getCookie(connection);
				// 得到sessionId会话Id
				sessionId = getSessionIdFromCookie(cookie);

				// 读响应内容getInputStream
				out = getResponseByteArray( connection.getInputStream() );
			}else{
				// 状态码不等于200，也要读响应内容
				// 读响应内容getErrorStream
				out = getResponseByteArray( connection.getErrorStream() );
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 处理--->异常
			handleException(e);
			// 返回
			return null;
		} finally {
			// 断开
			disconnect(connection);
		}

		// 如果WEB端返回的是gzip, deflate压缩过的，则要解压。
		String contentEncoding = getHeader(connection, HttpHeaderField.Field_Content_Encoding);
		if ( isGZIP(contentEncoding) ) {
			// 为gzip, deflate时，则解压
			Log.e(TAG, "响应头包括Content_Encoding字段并且值包括gzip，所以要解压数据GZIP.unCompress()");
			out = GZIP.unCompress(out);
		}

		// 处理--->状态码
		if (responseCode == HttpURLConnection.HTTP_OK) {
			// 成功
			error = Error.Successful;
			if (this.completedListener != null) {
				completedListener.onSuccessful( out );
			}
		} else {
			error = Error.ResponseCodeError;
			// 打印错误内容
			String exceptionText = String.format("conn.getResponseCode():"+responseCode+" != HTTP_OK");
			Log.exception(TAG, new IllegalStateException(exceptionText));
			// 打印，HTTP状态码不等于200时Web端返回错误内容
			String errorText = Charset.convertString(out, config.getHeader().getCharset(), Charset.UTF_8);
			Log.e(TAG, "HTTP状态码不等于200，并且Web端返回错误内容:"+errorText);
			// 回调错误
			if (this.completedListener != null) {
				completedListener.onFailed( Error.ResponseCodeError, new IllegalStateException(exceptionText), responseCode, out );
			}
		}

		return out;
	}

	/**
	 * TODO doGetApache跟doPostApache很像，可以用doHttpUriRequest()合成一块代码
	 * 
	 * @return
	 */
	private byte[] doHttpUriRequest(String url, HttpEntity entity) {
		return null;
	}

	/**
	 * TODO 还有两个没有实现
	 * 
	 * 一是代理（代理已测试成功java.net.Proxy，但是密码认证(很多认证方式)没有测试）
	 * http://liulang203.iteye.com/blog/965522 密码认证代理
	 * 二是SSL
	 * 
	 * @param url
	 * @return
	 */
	public byte[] doGetApache(String url) {
		Log.d(TAG, "doGetApache():url:"+url);
		Log.e(TAG, "------ doGetApache() start ------");

		if (isEmptyString(url)) {
			return null;
		}

		//		if (entity == null) {
		//			return null;
		//		}

		HttpGet get = new HttpGet(url);

		List<BasicNameValuePair> list = config.getHeader().getFields();

		if (list != null) {
			int len = list.size();
			if (len > 0) {
				Log.e(TAG, "------ setHeader() start ------");
				for (BasicNameValuePair pair : list) {
					String name = pair.getName();
					String value = pair.getValue();
					Log.d(TAG, "setHeader--->name:"+name+",value:"+value);
					get.setHeader(name, value);
				}
				Log.e(TAG, "------ setHeader() end ------");
			}
		}

		byte[] bytes = null;

		try {
			// 设置实体
			//			post.setEntity( entity );
			// 得到HttpClient
			HttpClient client = new DefaultHttpClient();
			// 请求超时
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, config.getConnectTimeout());
			// 读取超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, config.getReadTimeout());
			// 设置代理
			if (config.getProxy() != null) {
				Log.d(TAG, "proxy != null");
				// HttpConfig.Proxy转成java.net.Proxy
				HttpConfig.Proxy proxyConfig = config.getProxy();
				HttpHost host = new HttpHost(proxyConfig.getHostname(), proxyConfig.getPort(), proxyConfig.getScheme());  
				client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
			}else{
				Log.d(TAG, "proxy == null");
			}

			// 得到HttpResponse
			HttpResponse httpResponse = client.execute( get );
			if (httpResponse != null) {
				Header[] responseHeader = httpResponse.getAllHeaders();
				Log.e(TAG, "------ printResponseHeader(打印响应头) start ------");
				for (Header header : responseHeader) {
					String name = header.getName();
					String value = header.getValue();
					Log.d(TAG, "ResponseHeader--->name:"+name+",value:"+value);
				}
				Log.e(TAG, "------ printResponseHeader(打印响应头) end ------");
				// 得到HTTP状态码
				StatusLine statusLine = httpResponse.getStatusLine();
				if (statusLine != null) {
					int statusCode = statusLine.getStatusCode();
					Log.d(TAG, "statusCode:"+statusCode);
					if(statusCode == HttpURLConnection.HTTP_OK) {
						// 转成字节数组
						bytes = EntityUtils.toByteArray( httpResponse.getEntity() );
					}
					//					bytes = EntityUtils.toByteArray( httpResponse.getEntity() );
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.e(TAG, "------ doGetApache() end ------");
		return bytes;
	}

	public void __test__doGetApache(Context context) {

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		headerConfig.setCharset(Charset.UTF_8);
		headerConfig.addField("aaa", "ddd");
		//		headerConfig.setContentTypeJSON();
		headerConfig.print();

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置
		httpConfig.print();

		http.setConfig( httpConfig ); // 设置HTTP配置

		byte[] bytes = null;
		try {
			String url = "http://192.168.20.34/Wisdom/MobileService/MobileService.asmx/Login?account=tiyan&password=111111&platform=&client=1";
			bytes = http.doGetApache( url );
		} catch (Exception e) {
			e.printStackTrace();
		}

		String resultText = Charset.bytes2String(bytes, Charset.UTF_8);

		Log.e(TAG, "resultText:"+resultText);
	}

	/**
	 * httpclient 4.3使用SSL例子
	 * http://itindex.net/blog/2013/11/06/1383728580000.html
	 * 
	 * HttpClient 4.3教程 第一章 基本概念
	 * http://www.yeetrack.com/?p=773
	 * 
	 * @param context
	 */
	public void __test__doGetApache_SSL(Context context) {

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		headerConfig.setCharset(Charset.UTF_8);
		//		headerConfig.addField("aaa", "ddd");
		//		headerConfig.setContentTypeJSON();
		headerConfig.print();

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置
		httpConfig.print();

		http.setConfig( httpConfig ); // 设置HTTP配置

		byte[] bytes = null;
		try {
			String url = "https://www.versign.com";
			bytes = http.doGetApache( url );
		} catch (Exception e) {
			e.printStackTrace();
		}

		String resultText = Charset.bytes2String(bytes, Charset.UTF_8);

		Log.e(TAG, "resultText:"+resultText);
	}

	/**
	 * post for apache
	 * 
	 * @param url
	 * @param entity 有ByteArrayEntity、FileEntity、StringEntity、InputStreamEntity、SerializableEntity
	 * @return
	 */
	public byte[] doPostApache(String url, HttpEntity entity) {
		Log.d(TAG, "doPostApache():url:"+url);
		Log.e(TAG, "------ doPostApache() start ------");

		if (isEmptyString(url)) {
			return null;
		}

		if (entity == null) {
			return null;
		}

		HttpPost post = new HttpPost(url);

		//		Log.d(TAG, "entity.getContentEncoding().getName():"+entity.getContentEncoding().getName()); //  null
		//		Log.d(TAG, "entity.getContentEncoding().getValue():"+entity.getContentEncoding().getValue()); // null
		Log.d(TAG, "entity.getContentEncoding().getName():"+entity.getContentType().getName());// Content-Type
		Log.d(TAG, "entity.getContentEncoding().getValue():"+entity.getContentType().getValue());// application/x-www-form-urlencoded
		//		String contentType = String.format("%s; charset=%s", HttpContentType.Application_UrlEncoded, entity.getContentEncoding().getValue());
		//		Log.d(TAG, "Content-Type:"+contentType);
		// HttpEntity默认为application/x-www-form-urlencoded，所以不用post.setHeader(...)再设置了
		//		post.setHeader(HttpHeader.Header_Content_Type, contentType); // HttpEntity

		List<BasicNameValuePair> list = config.getHeader().getFields();

		if (list != null) {
			int len = list.size();
			if (len > 0) {
				Log.e(TAG, "------ setHeader() start ------");
				for (BasicNameValuePair pair : list) {
					String name = pair.getName();
					String value = pair.getValue();
					Log.d(TAG, "setHeader--->name:"+name+",value:"+value);
					post.setHeader(name, value);
				}
				Log.e(TAG, "------ setHeader() end ------");
			}
		}

		byte[] bytes = null;

		try {
			// 设置实体
			post.setEntity( entity );
			// 得到HttpClient
			HttpClient client = new DefaultHttpClient();
			// 请求超时
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, config.getConnectTimeout());
			// 读取超时
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, config.getReadTimeout());
			// 设置代理
			if (config.getProxy() != null) {
				Log.d(TAG, "proxy != null");
				// HttpConfig.Proxy转成java.net.Proxy
				HttpConfig.Proxy proxyConfig = config.getProxy();
				HttpHost host = new HttpHost(proxyConfig.getHostname(), proxyConfig.getPort(), proxyConfig.getScheme());  
				client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
			}else{
				Log.d(TAG, "proxy == null");
			}

			// 得到HttpResponse
			HttpResponse httpResponse = client.execute( post );
			if (httpResponse != null) {
				Header[] responseHeader = httpResponse.getAllHeaders();
				Log.e(TAG, "------ printResponseHeader(打印响应头) start ------");
				for (Header header : responseHeader) {
					String name = header.getName();
					String value = header.getValue();
					Log.d(TAG, "ResponseHeader--->name:"+name+",value:"+value);
				}
				Log.e(TAG, "------ printResponseHeader(打印响应头) end ------");
				// 得到HTTP状态码
				StatusLine statusLine = httpResponse.getStatusLine();
				if (statusLine != null) {
					int statusCode = statusLine.getStatusCode();
					Log.d(TAG, "statusCode:"+statusCode);
					if(statusCode == HttpURLConnection.HTTP_OK) {
						// 转成字节数组
						bytes = EntityUtils.toByteArray( httpResponse.getEntity() );
					}
					//					bytes = EntityUtils.toByteArray( httpResponse.getEntity() );
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.e(TAG, "------ doPostApache() end ------");
		return bytes;
	}

	public void __test__doPostApache(Context context) {

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		headerConfig.setCharset(Charset.UTF_8);
		headerConfig.addField("aaa", "ddd");
		//		headerConfig.setContentTypeJSON();
		headerConfig.print();

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置
		httpConfig.print();

		http.setConfig( httpConfig ); // 设置HTTP配置

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("account","tiyan"));
		params.add(new BasicNameValuePair("password","111111"));
		params.add(new BasicNameValuePair("platform",""));
		params.add(new BasicNameValuePair("client","1"));

		//		header.add(new org.apache.http.message.BasicHeader(HttpHeader.Header_Content_Type, "application/json; charset=utf-8"));
		//		header.add(new org.apache.http.message.BasicHeader(HttpHeader.Header_Content_Type, "application/json; charset=utf-8"));
		//		header.add(new org.apache.http.message.BasicHeader("Accept", "application/json"));
		//		header.add(new org.apache.http.message.BasicHeader(HttpHeader.Header_Content_Type, "application/json"));
		//		header.add(new org.apache.http.message.BasicHeader(HttpHeader.Header_Content_Type, "application/x-www-form-urlencoded"));

		byte[] bytes = null;
		try {

			bytes = http.doPostApache("http://192.168.20.34/Wisdom/MobileService/MobileService.asmx/Login", 
					new UrlEncodedFormEntity(params, http.getConfig().getHeader().getCharset()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		String resultText = Charset.bytes2String(bytes, Charset.UTF_8);

		Log.e(TAG, "resultText:"+resultText);

	}

	public void __test__doPostApache_Proxy(Context context) {

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		headerConfig.setCharset(Charset.UTF_8);
		//		headerConfig.addField("aaa", "ddd");
		//		headerConfig.setContentTypeJSON();
		headerConfig.print();

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时

		String host = "58.246.210.45";
		int port = 80;
		java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(host, port));

		HttpConfig.Proxy proxy__ = new HttpConfig.Proxy();

		//		httpConfig.setProxy(proxy);

		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置
		httpConfig.print();

		http.setConfig( httpConfig ); // 设置HTTP配置

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		//		params.add(new BasicNameValuePair("account","tiyan"));
		//		params.add(new BasicNameValuePair("password","111111"));
		//		params.add(new BasicNameValuePair("platform",""));
		//		params.add(new BasicNameValuePair("client","1"));

		//		header.add(new org.apache.http.message.BasicHeader(HttpHeader.Header_Content_Type, "application/json; charset=utf-8"));
		//		header.add(new org.apache.http.message.BasicHeader(HttpHeader.Header_Content_Type, "application/json; charset=utf-8"));
		//		header.add(new org.apache.http.message.BasicHeader("Accept", "application/json"));
		//		header.add(new org.apache.http.message.BasicHeader(HttpHeader.Header_Content_Type, "application/json"));
		//		header.add(new org.apache.http.message.BasicHeader(HttpHeader.Header_Content_Type, "application/x-www-form-urlencoded"));

		byte[] bytes = null;
		try {

			bytes = http.doPostApache("http://www.baidu.com", 
					new UrlEncodedFormEntity(params, http.getConfig().getHeader().getCharset()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		String resultText = Charset.bytes2String(bytes, Charset.UTF_8);

		Log.e(TAG, "resultText:"+resultText);

	}

	/**
	 * 处理异常
	 * 
	 * @param e
	 */
	protected void handleException(Exception e) {
		if (e == null) {
			return;
		}

		String className = e.getClass().getName();
		Log.e(TAG, "className:"+className);

		if (className.equalsIgnoreCase(new java.net.ConnectException().getClass().getName())) {
			// 这里有可能是无网络造成的连接异常
			NetworkState networkState = new NetworkState(context);
			if (networkState.isConnected()) {
				error = Error.ConnectException;
				if(completedListener != null) {
					completedListener.onFailed(Error.ConnectException, e, responseCode, null);
				}
			}else{
				error = Error.NotNetwork;
				if(completedListener != null) {
					completedListener.onFailed(Error.NotNetwork, e, responseCode, null);
				}
			}
		}else if (className.equalsIgnoreCase(new org.apache.http.conn.ConnectTimeoutException().getClass().getName())) {
			error = Error.ConnectTimeoutException;
			if(completedListener != null) {
				completedListener.onFailed(Error.ConnectTimeoutException, e, responseCode, null);
			}
		}else if (className.equalsIgnoreCase(new java.io.IOException().getClass().getName())) {
			error = Error.IOException;
			if(completedListener != null) {
				completedListener.onFailed(Error.IOException, e, responseCode, null);
			}
		}else if (className.equalsIgnoreCase(new java.io.InterruptedIOException().getClass().getName())) {
			error = Error.InterruptedIOException;
			if(completedListener != null) {
				completedListener.onFailed(Error.InterruptedIOException, e, responseCode, null);
			}
		}else if (className.equalsIgnoreCase(new java.net.SocketException().getClass().getName())) {
			// 这里有可能是无网络造成的连接异常
			NetworkState networkState = new NetworkState(context);
			if (networkState.isConnected()) {
				error = Error.SocketException;
				if(completedListener != null) {
					completedListener.onFailed(Error.SocketException, e, responseCode, null);
				}
			}else{
				// java.net.SocketException: Network unreachable 此异常是无网络产生的
				error = Error.NotNetwork;
				if(completedListener != null) {
					completedListener.onFailed(Error.NotNetwork, e, responseCode, null);
				}
			}
		}else if (className.equalsIgnoreCase(new java.net.SocketTimeoutException().getClass().getName())) {
			// 当连接和读的超时时间叠加起来才返回。
			error = Error.SocketTimeoutException;
			if(completedListener != null) {
				completedListener.onFailed(Error.SocketTimeoutException, e, responseCode, null);
			}
		}else if (className.equalsIgnoreCase(new java.net.UnknownHostException().getClass().getName())) {
			NetworkState networkState = new NetworkState(context);
			if (networkState.isConnected()) {
				error = Error.ConnectException;
				if(completedListener != null) {
					completedListener.onFailed(Error.ConnectException, e, responseCode, null);
				}
			}else{
				error = Error.NotNetwork;
				if(completedListener != null) {
					completedListener.onFailed(Error.NotNetwork, e, responseCode, null);
				}
			}
		}else if (className.equalsIgnoreCase(new java.io.FileNotFoundException().getClass().getName())) {
			error = Error.FileNotFoundException;
			if(completedListener != null) {
				completedListener.onFailed(Error.FileNotFoundException, e, responseCode, null);
			}
		}else {
			error = Error.OtherException;
			if(completedListener != null) {
				completedListener.onFailed(Error.OtherException, e, responseCode, null);
			}
		}

	}

	/**
	 * 判断HttpURLConnection中的头信息是否存在字段
	 * 
	 * 例子：
	 * 
	 * isHeader(connection, HttpHeader.Header_Accept_Encoding);
	 * isHeader(connection, HttpHeader.Header_Content_Encoding);
	 * isHeader(connection, HttpHeader.Header_Server);
	 * 
	 * @param connection
	 * @return
	 */
	private boolean isHeaderExists(HttpURLConnection connection, String field) {

		boolean isHeaderExists = false;

		// 如果field不存在，则headerText为null，反之不为null
		String headerValue = getHeader(connection, field);
		Log.d(TAG, "isHeaderExists(HttpURLConnection):headerValue:"+headerValue);

		if (isEmptyString(headerValue) == false) {
			isHeaderExists = true;
		}

		Log.d(TAG, "isHeaderExists(HttpURLConnection):isHeaderExists:"+isHeaderExists);
		return isHeaderExists;
	}

	/**
	 * 判断HttpConfig.Header指定的field是否存在
	 * 
	 * @param header
	 * @param field
	 * @return
	 */
	private boolean isHeaderExists(HttpConfig.Header header, String field) {

		boolean isHeaderExists = false;

		if (header != null && isEmptyString(field) == false) {
			List<BasicNameValuePair> list = header.getFields();
			if (list != null) {
				for (BasicNameValuePair pair : list) {
					String name = pair.getName();
					if (isEmptyString(name) == false) {
						if (name.equalsIgnoreCase(field)) {
							// 找到
							isHeaderExists = true;
							break;
						}
					}
				}
			}
		}

		Log.d(TAG, "isHeaderExists(HttpConfig.Header):isHeaderExists:"+isHeaderExists);
		return isHeaderExists;
	}

	/**
	 * 判断HttpConfig.Header指定的field是包含value
	 * 
	 * @param header
	 * @param field
	 * @param value
	 * @return
	 */
	private boolean isValueExists(HttpConfig.Header header, String field, String value) {

		Log.d(TAG, "isValueExists(HttpConfig.Header):field:"+field);
		Log.d(TAG, "isValueExists(HttpConfig.Header):value:"+value);

		boolean isFieldExists = false;
		boolean isValueExists = false;

		if (header != null && isEmptyString(field) == false) {
			List<BasicNameValuePair> list = header.getFields();
			if (list != null) {
				for (BasicNameValuePair pair : list) {
					String pairName = pair.getName();
					if (isEmptyString(pairName) == false) {
						Log.d(TAG, "isValueExists(HttpConfig.Header):pairName:"+pairName);
						if (pairName.equalsIgnoreCase(field)) {
							// 找到Field
							isFieldExists = true;
							String pairValue = pair.getValue();
							if (isEmptyString(pairValue) == false) {
								Log.d(TAG, "isValueExists(HttpConfig.Header):pairValue:"+pairValue);
								if (pairValue.indexOf(value) >= 0) {
									// 找到Value
									isValueExists = true;
									break;
								}
							}
						}
					}
				}
			}
		}

		Log.d(TAG, "isValueExists(HttpConfig.Header):isFieldExists:"+isFieldExists);
		Log.d(TAG, "isValueExists(HttpConfig.Header):isValueExists:"+isValueExists);
		return isFieldExists && isValueExists;
	}

	/**
	 * 得到HttpURLConnection中的头信息字段
	 * 
	 * Log.d(TAG, "getHeader():Accept_Encoding:"+getHeader(connection, HttpHeader.Header_Accept_Encoding));
	 * Log.d(TAG, "getHeader():Content_Encoding:"+getHeader(connection, HttpHeader.Header_Content_Encoding));
	 * Log.d(TAG, "getHeader():Server:"+getHeader(connection, HttpHeader.Header_Server));
	 * 
	 * @param connection
	 * @param field
	 * @return
	 */
	private String getHeader(HttpURLConnection connection, String field) {
		if (connection == null) {
			return null;
		}

		if (isEmptyString(field)) {
			return null;
		}

		return connection.getHeaderField(field);
	}

	/**
	 * 判断字符串是否含有gzip
	 * 
	 * @param text
	 * @return
	 */
	private boolean isGZIP(String text) {
		if (isEmptyString(text)) {
			return false;
		}

		// 转成小写
		text = text.toLowerCase();

		// 搜索
		if (HttpHeaderField.Encoding_GZIP.toLowerCase().indexOf(text) >= 0) {
			// 找到
			return true;
		}

		return false;
	}

	/**
	 * 判断字符串是否含有deflate
	 * 
	 * @param text
	 * @return
	 */
	private boolean isDeflate(String text) {
		if (isEmptyString(text)) {
			return false;
		}

		// 转成小写
		text = text.toLowerCase();

		// 搜索
		if (HttpHeaderField.Encoding_Deflate.toLowerCase().indexOf(text) >= 0) {
			// 找到
			return true;
		}

		return false;
	}

	/**
	 * 打印信息
	 * 
	 * @param connection
	 */
	public void print(HttpURLConnection connection) {
		Log.e(TAG, "------ Client提交参数列表  start ------");
		config.print();
		config.getHeader().print();
		Log.e(TAG, "------ Client提交参数列表  end ------");

		Log.e(TAG, "------ Server返回参数列表  start ------");
		if (connection != null) {
			Log.e(TAG, "getConnectTimeout():"+connection.getConnectTimeout());
			Log.e(TAG, "getReadTimeout():"+connection.getReadTimeout());
			Log.e(TAG, "getRequestMethod():"+connection.getRequestMethod());
			Log.e(TAG, "getContentType():"+connection.getContentType());
			Log.e(TAG, "getContentLength():"+connection.getContentLength());
			Log.e(TAG, "getContentEncoding():"+connection.getContentEncoding());

			Log.e(TAG, "getHeaderField(Header_Content_Type):"+connection.getHeaderField(HttpHeaderField.Field_Content_Type));
			Log.e(TAG, "getHeaderField(Header_Charset):"+connection.getHeaderField(HttpHeaderField.Field_Charset));
			Log.e(TAG, "getHeaderField(Header_Content_Length):"+connection.getHeaderField(HttpHeaderField.Field_Content_Length));
			Log.e(TAG, "getHeaderField(Header_Content_Language):"+connection.getHeaderField(HttpHeaderField.Field_Content_Language));
			Log.e(TAG, "getHeaderField(Header_Connection):"+connection.getHeaderField(HttpHeaderField.Field_Connection));
			Log.e(TAG, "getHeaderField(Header_Pragma):"+connection.getHeaderField(HttpHeaderField.Field_Pragma));
			Log.e(TAG, "getHeaderField(Header_Cookie):"+connection.getHeaderField(HttpHeaderField.Field_Cookie));
			Log.e(TAG, "getHeaderField(Header_Content_Encoding):"+connection.getHeaderField(HttpHeaderField.Field_Content_Encoding));
			Log.e(TAG, "getHeaderField(Header_Transfer_Encoding):"+connection.getHeaderField(HttpHeaderField.Field_Transfer_Encoding));
			Log.e(TAG, "getHeaderField(Header_Content_MD5):"+connection.getHeaderField(HttpHeaderField.Field_Content_MD5));
			Log.e(TAG, "getHeaderField(Header_Content_Range):"+connection.getHeaderField(HttpHeaderField.Field_Content_Range));
			Log.e(TAG, "getHeaderField(Header_Set_Cookie):"+connection.getHeaderField(HttpHeaderField.Field_Set_Cookie));
			Log.e(TAG, "getHeaderField(Header_Cache_Control):"+connection.getHeaderField(HttpHeaderField.Field_Cache_Control));
			Log.e(TAG, "getHeaderField(Header_User_Agent):"+connection.getHeaderField(HttpHeaderField.Field_User_Agent));
			Log.e(TAG, "getHeaderField(Header_Agent):"+connection.getHeaderField(HttpHeaderField.Field_Agent));

			Log.e(TAG, "getHeaderField(Header_Accept_Language):"+connection.getHeaderField(HttpHeaderField.Field_Accept_Language));
			Log.e(TAG, "getHeaderField(Header_Accept_Encoding):"+connection.getHeaderField(HttpHeaderField.Field_Accept_Encoding));
			Log.e(TAG, "getHeaderField(Header_Accept_Charset):"+connection.getHeaderField(HttpHeaderField.Field_Accept_Charset));
			Log.e(TAG, "getHeaderField(Header_Allow):"+connection.getHeaderField(HttpHeaderField.Field_Allow));
			Log.e(TAG, "getHeaderField(Header_Date):"+connection.getHeaderField(HttpHeaderField.Field_Date));
			Log.e(TAG, "getHeaderField(Header_Expires):"+connection.getHeaderField(HttpHeaderField.Field_Expires));
			Log.e(TAG, "getHeaderField(Header_Last_Modified):"+connection.getHeaderField(HttpHeaderField.Field_Last_Modified));

			Log.e(TAG, "getHeaderField(Header_Location):"+connection.getHeaderField(HttpHeaderField.Field_Location));
			Log.e(TAG, "getHeaderField(Header_Refresh):"+connection.getHeaderField(HttpHeaderField.Field_Refresh));
			Log.e(TAG, "getHeaderField(Header_Server):"+connection.getHeaderField(HttpHeaderField.Field_Server));
			Log.e(TAG, "getHeaderField(Header_www_Authenticate):"+connection.getHeaderField(HttpHeaderField.Field_www_Authenticate));
			Log.e(TAG, "getHeaderField(Header_Etag):"+connection.getHeaderField(HttpHeaderField.Field_Etag));

			Log.e(TAG, "getDate():"+connection.getDate());
			Log.e(TAG, "getExpiration():"+connection.getExpiration());
			Log.e(TAG, "getIfModifiedSince():"+connection.getIfModifiedSince());
			Log.e(TAG, "getLastModified():"+connection.getLastModified());

			Log.e(TAG, "getDefaultUseCaches():"+connection.getDefaultUseCaches());
			Log.e(TAG, "getUseCaches():"+connection.getUseCaches());

			try {
				Log.e(TAG, "getResponseCode():"+connection.getResponseCode());
				Log.e(TAG, "getResponseMessage():"+connection.getResponseMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.e(TAG, "------ Server返回参数列表  end ------");
	}

	/**
	 * 打印响应头
	 * 
	 * @param connection
	 */
	public void printResponseHeader(HttpURLConnection connection) {
		Log.d(TAG, "------ printResponseHeader(打印响应头) start ------");

		Map<String, List<String>> map = connection.getHeaderFields();

		Set<Map.Entry<String, List<String>>> set = map.entrySet();
		for (Iterator<Map.Entry<String, List<String>>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) it.next();
			if (entry != null) {
				String key = entry.getKey();
				List<String> list = entry.getValue();
				if (key != null && list != null) {
					Log.d(TAG, key + "--->" + list.toString());
				}
			}
		}

		Log.d(TAG, "------ printResponseHeader(打印响应头) end ------");
	}

	// 1）是否可以自动保存cookie  ==== 不行
	// set conn
	// set header
	// 请求完成后，马上关闭。 === 可以设置 connect == close

	public HttpTool __test__doGet(Context context, String sessionId) {

		Log.e(TAG, "------ __test__doGet() start ------");

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型
		//		headerConfig.addField("userNamesfefe", "ssss");
		//		headerConfig.addField(HttpHeader.Header_Content_Type, HttpContentType.Application_JSON+"; charset=utf-8");
		//		headerConfig.setContentType(HttpContentType.Text_Plain, Charset.UTF_8);
		//		headerConfig.setCharset(Charset.UTF_8);
		//		headerConfig.setContentTypeJSON();
		headerConfig.print();

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置
		httpConfig.print();

		http.setConfig( httpConfig ); // 设置HTTP配置

		String url = "http://192.168.20.34/Wisdom/MobileService/MobileService.asmx/Login?account=tiyan&password=111111&platform=&client=1";
		//		url = "http://carlife.wisdom-gps.com:6666/Handler/Login.aspx?LoginName=demo&Password=wisdomdemo&Type=login&loginkey=A5216C969BEAB7012B0AE10315960A53";
		url = "http://restapi.amap.com/v3/assistant/inputtips?city=%E5%85%A8%E5%9B%BD&key=c998b7878180d47eaa1934df01b78094&keywords=%E5%B9%BF&output=json&loginkey=E02D20C761C6A12405CE87C7591165E3";
		url = "http://www.cppblog.com";
		Log.e(TAG, "url:"+url);

		//		List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
		//		param.add(new BasicNameValuePair("userNamesfefe", "ssss"));

		byte[] bytes = http.doGet(url);
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, Charset.UTF_8, Charset.UTF_8);
		Log.e(TAG, "text:"+text);

		Log.e(TAG, "------ __test__doGet() end ------");

		return http;
	}

	public void __test__doGet_GZIP(Context context) {

		Log.e(TAG, "------ __test__doGet() start ------");

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型
		//		headerConfig.addField("userNamesfefe", "ssss");
		//		headerConfig.addField(HttpHeader.Header_Content_Type, HttpContentType.Application_JSON+"; charset=utf-8");
		//		headerConfig.setContentType(HttpContentType.Text_Plain, Charset.UTF_8);
		//		headerConfig.setCharset(Charset.GB2312);
		//		headerConfig.setContentTypeJSON();
		headerConfig.print();

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置
		httpConfig.print();

		http.setConfig( httpConfig ); // 设置HTTP配置

		String url = "";
		/*
		 * www.cppblog.com
		 * www.163.com
		 * www.sohu.com
		 * www.sina.com.cn
		 * */
		url = "http://www.cppblog.com";
		Log.e(TAG, "url:"+url);

		//		List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
		//		param.add(new BasicNameValuePair("userNamesfefe", "ssss"));

		byte[] bytes = http.doGet(url);
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, http.getConfig().getHeader().getCharset(), Charset.UTF_8);
		Log.e(TAG, "text:"+text);

		Log.e(TAG, "------ __test__doGet() end ------");
	}

	public HttpTool __test__doGet_Proxy(Context context) {
		Log.e(TAG, "------ __test__doGet() start ------");

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型
		//		headerConfig.addField("userNamesfefe", "ssss");
		//		headerConfig.addField(HttpHeader.Header_Content_Type, HttpContentType.Application_JSON+"; charset=utf-8");
		//		headerConfig.setContentType(HttpContentType.Text_Plain, Charset.UTF_8);
		headerConfig.setCharset(Charset.UTF_8);
		//		headerConfig.setContentTypeJSON();
		headerConfig.print();

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		httpConfig.setConnectTimeout( 60 * 1000 ); // 设置连接超时
		httpConfig.setReadTimeout( 60 * 1000 ); // 设置读超时

		// 代理设置  有几种要测试，1）HTTP OR HTTPS 2） http socket 3）账号和密码
		// TODO
		// 1)http匿名 ---> 测试通过
		// 2)https匿名 ---> 
		// 3)socket4/5匿名 ---> 
		// 4)http有密码 --->
		// 5)https有密码 --->
		// 6)socket4/5有密码 --->

		HttpConfig.Proxy proxy = new HttpConfig.Proxy();
		proxy.setType(java.net.Proxy.Type.HTTP);
		proxy.setScheme(HttpTool.Scheme_Http);
		proxy.setHostname("58.246.210.45");
		proxy.setPort(80);
		//		proxy.setUsername(username)
		//		proxy.setPassword(password)
		proxy.print();
		httpConfig.setProxy(proxy);

		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置
		httpConfig.print();

		http.setConfig( httpConfig ); // 设置HTTP配置

		String url = "http://192.168.20.34/Wisdom/MobileService/MobileService.asmx/Login?account=tiyan&password=111111&platform=&client=1";
		url = "http://carlife.wisdom-gps.com:6666/Handler/Login.aspx?LoginName=demo&Password=wisdomdemo&Type=login&loginkey=A5216C969BEAB7012B0AE10315960A53";
		url = "http://www.baidu.com";
		Log.e(TAG, "url:"+url);

		byte[] bytes = http.doGet(url);
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, http.getConfig().getHeader().getCharset(), Charset.UTF_8);
		Log.e(TAG, "text:"+text);

		Log.e(TAG, "------ __test__doGet() end ------");

		return http;
	}

	public HttpTool __test__doGet_SSL(Context context) {
		Log.e(TAG, "------ __test__doGet() start ------");

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型
		//		headerConfig.addField("userNamesfefe", "ssss");
		//		headerConfig.addField(HttpHeader.Header_Content_Type, HttpContentType.Application_JSON+"; charset=utf-8");
		//		headerConfig.setContentType(HttpContentType.Text_Plain, Charset.UTF_8);
		headerConfig.setCharset(Charset.UTF_8);
		//		headerConfig.setContentTypeJSON();
		headerConfig.print();

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		httpConfig.setConnectTimeout( 60 * 1000 ); // 设置连接超时
		httpConfig.setReadTimeout( 60 * 1000 ); // 设置读超时

		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置
		httpConfig.print();

		http.setConfig( httpConfig ); // 设置HTTP配置

		String url = "http://192.168.20.34/Wisdom/MobileService/MobileService.asmx/Login?account=tiyan&password=111111&platform=&client=1";
		url = "http://carlife.wisdom-gps.com:6666/Handler/Login.aspx?LoginName=demo&Password=wisdomdemo&Type=login&loginkey=A5216C969BEAB7012B0AE10315960A53";
		url = "https://www.google.com.hk";
		Log.e(TAG, "url:"+url);

		byte[] bytes = http.doGet(url);
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, http.getConfig().getHeader().getCharset(), Charset.UTF_8);
		Log.e(TAG, "text:"+text);

		Log.e(TAG, "------ __test__doGet() end ------");

		return http;
	}

	public HttpTool __test__doGet222(Context context, String sessionId) {

		Log.e(TAG, "------ __test__doGet222() start ------");

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型
		//		headerConfig.addField(HttpHeader.Header_Cookie, sessionId);

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置

		http.setConfig( httpConfig ); // 设置HTTP配置

		String url = "http://rental.wisdom-gps.com/mobile.asmx/getCarList?";
		url = "http://192.168.20.34/Wisdom/MobileService/MobileService.asmx/ChangePassword";
		Log.e(TAG, "url:"+url);

		//		http.setSessionId( sessionId );

		//		List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
		//		param.add(new BasicNameValuePair("userNamesfefe", "ssss"));

		byte[] bytes = http.doGet(url);
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, Charset.UTF_8, Charset.UTF_8);
		Log.e(TAG, "text:"+text);

		Log.e(TAG, "------ __test__doGet222() end ------");

		return http;
	}

	public HttpTool __test__doGet333(Context context, String sessionId) {

		Log.e(TAG, "------ __test__doGet333() start ------");

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

		String url = "http://rental.wisdom-gps.com/mobile.asmx/getCarGPSInfo?CarID=100002";
		Log.e(TAG, "url:"+url);

		http.setSessionId( sessionId );

		byte[] bytes = http.doGet(url);
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, Charset.UTF_8, Charset.UTF_8);
		Log.e(TAG, "text:"+text);

		Log.e(TAG, "------ __test__doGet333() end ------");

		return http;
	}

	public HttpTool __test__doPost(Context context, String sessionId) {

		Log.e(TAG, "------ __test__doPost() start ------");

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型
		headerConfig.addField("userNamesfefe", "ssss");

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置

		http.setConfig( httpConfig ); // 设置HTTP配置

		String urlParame = "UserName=tiyan&Password=tiyan&PlatName=&ClientType=1";
		Log.e(TAG, "urlParame:"+urlParame);

		String url = "http://rental.wisdom-gps.com/mobile.asmx/LoginNew?";
		Log.e(TAG, "url:"+url);

		byte[] bytes = http.doPost(url, Charset.string2Bytes(urlParame, Charset.UTF_8));
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, Charset.UTF_8, Charset.UTF_8);
		Log.e(TAG, "text:"+text);

		Log.e(TAG, "------ __test__doPost() end ------");

		return http;
	}

	public void __test__doPost_GZIP(Context context) {

		Log.e(TAG, "------ __test__doPost() start ------");

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型
		headerConfig.addField("userNamesfefe", "ssss");

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置

		http.setConfig( httpConfig ); // 设置HTTP配置

		String urlParame = "UserName=tiyan&Password=tiyan&PlatName=&ClientType=1";
		urlParame = null;
		Log.e(TAG, "urlParame:"+urlParame);

		String url = "";
		/*
		 * www.cppblog.com
		 * www.163.com
		 * www.sohu.com
		 * www.sina.com.cn
		 * */
		url = "http://www.cppblog.com/default.aspx";
		Log.e(TAG, "url:"+url);

		byte[] bytes = http.doPost(url, Charset.string2Bytes(urlParame, Charset.UTF_8));
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, Charset.UTF_8, Charset.UTF_8);
		Log.e(TAG, "text:"+text);

		Log.e(TAG, "------ __test__doPost() end ------");
	}

	public HttpTool __test__doPost222(Context context, String sessionId) {

		Log.e(TAG, "------ __test__doPost222() start ------");

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型
		headerConfig.addField(HttpHeaderField.Field_Cookie, sessionId);

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置

		http.setConfig( httpConfig ); // 设置HTTP配置

		String urlParame = "5555555555555";
		Log.e(TAG, "urlParame:"+urlParame);

		String url = "http://rental.wisdom-gps.com/mobile.asmx/getCarList?";
		Log.e(TAG, "url:"+url);

		//		List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
		//		param.add(new BasicNameValuePair(HttpHeader.Header_Connection, HttpHeader.Connection_Close));
		//		param.add(new BasicNameValuePair(HttpHeader.Header_Cookie, sessionId));

		//		http.setSessionId( sessionId );

		byte[] bytes = http.doPost(url, Charset.string2Bytes(urlParame, Charset.UTF_8));
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, Charset.UTF_8, Charset.UTF_8);
		Log.e(TAG, "text:"+text);

		Log.e(TAG, "------ __test__doPost222() end ------");
		return http;
	}

	public HttpTool __test__doPost333(Context context, String sessionId) {

		Log.e(TAG, "------ __test__doPost333() start ------");

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		headerConfig.setCharset( Charset.UTF_8 ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		//		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置

		http.setConfig( httpConfig ); // 设置HTTP配置

		String urlParame = "CarID=100002";
		Log.e(TAG, "urlParame:"+urlParame);

		String url = "http://rental.wisdom-gps.com/mobile.asmx/getCarGPSInfo?";
		Log.e(TAG, "url:"+url);

		//		List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
		//		param.add(new BasicNameValuePair("userNamesfefe", "ssss"));
		//		param.add(new BasicNameValuePair(HttpHeader.Header_Cookie, sessionId));

		http.setSessionId( sessionId );

		byte[] bytes = http.doPost(url, Charset.string2Bytes(urlParame, Charset.UTF_8));
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, Charset.UTF_8, Charset.UTF_8);
		Log.e(TAG, "text:"+text);

		Log.e(TAG, "------ __test__doPost333() end ------");
		return http;
	}

	public HttpTool __test__doPost444(Context context, String sessionId) {

		Log.e(TAG, "------ __test__doPost() start222 ------");

		HttpTool http = new HttpTool(context);

		// HTTP头配置
		HttpConfig.Header headerConfig = new HttpConfig.Header();
		//		headerConfig.setCharset( HttpRam.getTextcharset() ); // 设置内容字符集
		//		headerConfig.setContentType( HttpRam.getContenttype() ); // 设置内容类型
		//		headerConfig.addField("userNamesfefe", "ssss");
		//		headerConfig.addField(HttpHeader.Header_Content_Type, HttpContentType.Application_JSON+"; charset=utf-8");
		//		headerConfig.setContentType(HttpContentType.Application_JSON, Charset.UTF_8);
		//		headerConfig.setCharset(Charset.UTF_8);
		//		headerConfig.setContentTypeJSON();
		headerConfig.print();

		// HTTP配置
		HttpConfig httpConfig = new HttpConfig();
		//		httpConfig.setConnectTimeout( HttpRam.getConnectTimeout() ); // 设置连接超时
		//		httpConfig.setReadTimeout( HttpRam.getReadTimeout() ); // 设置读超时
		httpConfig.setHeader( headerConfig ); // 设置HTTP头配置
		httpConfig.print();

		http.setConfig( httpConfig ); // 设置HTTP配置

		String urlParame = "UserName=tiyan&Password=tiyan&PlatName=&ClientType=1";
		urlParame = "account=tiyan&password=111111&platform=&client=1";
		Log.e(TAG, "urlParame:"+urlParame);

		String url = "http://rental.wisdom-gps.com/mobile.asmx/LoginNew?";
		url = "http://192.168.20.34/Wisdom/MobileService/MobileService.asmx/Login?";
		Log.e(TAG, "url:"+url);

		byte[] bytes = http.doPost(url, Charset.string2Bytes(urlParame, Charset.UTF_8));
		if (bytes != null) {
			Log.e(TAG, "bytes != null len:"+bytes.length);
		}else{
			Log.e(TAG, "bytes == null");
		}

		String text = Charset.convertString(bytes, Charset.UTF_8, Charset.UTF_8);
		Log.e(TAG, "text:"+text);

		Log.e(TAG, "------ __test__doPost() end ------");

		return http;
	}

	/**
	 * 编码（默认将空格转成+，不符合实际，需要将+转为%20）
	 * 
	 * @param text
	 * @param charsetName
	 * @return
	 */
	public static String encode(String text, String charsetName) {
		if (isEmptyString(charsetName)) {
			return null;
		}
		if (text == null) {
			return null;
		}

		String encoder = null;

		try {
			// TODO 可试试android.net.Uri.encode(...)
			encoder = java.net.URLEncoder.encode(text, charsetName);
			// 空格转成了+，跟实际不符，需要将空格转成%20
			if (encoder != null) {
				encoder = encoder.replace("+", "%20");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// 可能会出现内存溢出异常
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return encoder;
	}

	// TODO 没完成
	public static String decode(String url, String charsetName) {

		if (isEmptyString(url) || isEmptyString(charsetName)) {
			return null;
		}

		return null;
	}

	// +++　下述是新增的

	/**
	 * 将字节数据转成特定字符集
	 * 
	 * @param bytes
	 * @param bytesCharset 字节数组的字符集
	 * @param destCharset 将转换成目标字符集
	 * @return
	 */
	protected static String bytes2String(byte[] bytes, String bytesCharset, String destCharset) {
		return Charset.convertString(bytes, bytesCharset, destCharset);
	}

	/**
	 * 将字节数据转成特定字符集
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2String(byte[] bytes) {
		return bytes2String(bytes, getConfig().getHeader().getCharset(), Charset.UTF_8);
	}

	/**
	 * 将字符数组转成Base64
	 * 
	 * @param bytes
	 * @param flags
	 * @return
	 */
	public static String bytes2Base64String(byte[] bytes, int flags) {
		if (bytes == null) {
			return null;
		}

		// 编码
		return android.util.Base64.encodeToString(bytes, flags);
	}

	/**
	 * 将字符数组转成Base64
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2Base64String(byte[] bytes) {
		return bytes2Base64String(bytes, android.util.Base64.DEFAULT);
	}

	/**
	 * 将Base64字符串转成字节数组
	 * 
	 * @param base64String
	 * @param flags
	 * @return
	 */
	public static byte[] base64String2Bytes(String base64String, int flags) {
		if (isEmptyString(base64String)) {
			return null;
		}

		// 解码
		return android.util.Base64.decode(base64String, flags);
	}

	/**
	 * 将Base64字符串转成字节数组
	 * 
	 * @param base64String
	 * @return
	 */
	public static byte[] base64String2Bytes(String base64String) {
		return base64String2Bytes(base64String, android.util.Base64.DEFAULT);
	}

}
