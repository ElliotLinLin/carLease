package com.tools.net.http;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import com.tools.content.pm.PermissionTool;
import com.tools.util.Log;

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
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class TmpHttpToolC {

	private static final String TAG = TmpHttpToolC.class.getSimpleName();

	// Content-Type: [type]/[subtype]; parameter
	protected String contentType = HttpContentType.Application_JSON;
	// org.apache.http.protocol.HTTP里面有
	protected String charset = "UTF-8";

	protected int connectTimeout = 10 * 1000; // 5s太短会出错，建议10s
	protected int readTimeout = 20 * 1000; // 建议20s

	protected int responseCodeOK = HttpURLConnection.HTTP_OK;

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

	public static final String Property_Content_Type = "Content-Type";
	public static final String Property_Charset = "Charset";
	public static final String Property_Content_Length = "Content-Length";
	public static final String Property_Connection = "Connection"; // "Connection", "Keep-Alive");// 维持长连接
	public static final String Property_Cookie = "cookie"; // 是cookie

	public static final String Property_Content_Encoding = "Content-Encoding"; // 没用到
	public static final String Property_Transfer_Encoding = "Transfer-Encoding"; // 没用到

	public static final String Header_Field_Set_Cookie = "Set-Cookie"; // 是Set-Cookie

	protected Pattern pattern = Pattern.compile("charset.*=.*>?", Pattern.CASE_INSENSITIVE);

	protected HttpURLConnection connection = null;

	// Cookie
	protected String cookie = null;
	// 是否使用会话ID
	protected boolean useSessionId = false;
	// 会话
	protected String sessionId = null;

	// 网址是否经过编码（建议，仅修改参数中的值） --- 不要了
	//	protected boolean useURLEncoder = false;

	// 请求的网址
	protected String url = null;

	protected OnCompletedListener completedListener = null;

	// 失败的状态码
	protected int responseCodeError = -1;

	public enum Method {
		GET,POST,DELETE,PUT,TRACE,HEAD,OPTIONS,CONNECT
	}

	// 错误
	public enum Error {
		None, // 无错误
		ResponseCodeError, // 得到响应码时发生错误
		NotNetwork, // 无网络
		ConnectException, // 连接异常
		IOException, // IO异常
		SocketException, // Socket异常
		SocketTimeoutException, // 超时
		InterruptedIOException, // 中断异常
		ConnectTimeoutException, // 连接超时异常
		Other // 其它
	}

	// 决定使用此接口
	public interface OnCompletedListener {
		void onSuccessed(byte[] out); // 成功
		// 当error == ResponseCodeError时，检查int responseCode才有意义
		void onFailed(Error error, java.lang.Exception exception, int responseCode); // 失败
	}

	//	public interface OnCompletedListener {
	//		// 开始之前  begin in before
	//		void onBegin();
	//		// 运行中.....
	//		void doRun();
	//		// 运行结束...更新UI
	//		// 结束（可更新UI）
	//		void onFinish(Response msg);
	//		void onCompleted(Error error);
	//	}

	/**
	 * 响应类
	 * 
	 * @author lmc
	 *
	 */
	public class Response {
		public String url = null;
		public int responseCode = -1;
		public byte[] responseByte = null;
	}

	public TmpHttpToolC(Context context) {
		// 检查权限
		PermissionTool.checkThrow(context, android.Manifest.permission.INTERNET);
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

	//	public boolean isUseURLEncoder() {
	//		return useURLEncoder;
	//	}
	//
	//	public void setUseURLEncoder(boolean useURLEncoder) {
	//		this.useURLEncoder = useURLEncoder;
	//	}

	/**
	 * 获得MIME类型
	 * 
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * 设置MIME类型
	 * http://zh.wikipedia.org/wiki/MIME
	 * 
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setContentTypeJSON() {
		setContentType(HttpContentType.Application_JSON);
	}

	public void setContentTypeXML() {
		setContentType(HttpContentType.Application_XML);
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

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
	public void setTimeout(int connectTimeout, int readTimeout) {
		this.setConnectTimeout(connectTimeout);
		this.setReadTimeout(readTimeout);
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getResponseCodeOK() {
		Log.e(TAG, "getResponseCodeOK():responseCodeOK:"+responseCodeOK);
		return responseCodeOK;
	}

	public void setResponseCodeOK(int responseCodeOK) {
		Log.e(TAG, "setResponseCodeOK():responseCodeOK:"+responseCodeOK);
		this.responseCodeOK = responseCodeOK;
	}

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
			Log.exception(TAG, new NullPointerException("urlString == isEmptyString"));
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
			conn = (HttpURLConnection)urlConn.openConnection();
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
			Log.exception(TAG, new NullPointerException("conn == null"));
			return;
		}

		// TODO 如果是采用get不用下述二个方法
		// 内容编码
		//		conn.setRequestProperty(Property_Charset, this.charset);
		// 设置MIME类型
		//		conn.setRequestProperty(Property_Content_Type, this.contentType);

		// 设置超时
		conn.setConnectTimeout(getConnectTimeout());

		// 设置超时
		conn.setReadTimeout(getReadTimeout());

		// 是否使用缓冲
		conn.setUseCaches(false);
	}

	/**
	 * 设置连接参数
	 * 
	 * @param conn
	 * @param data
	 */
	protected void setConntectionParame(HttpURLConnection conn) {
		if (conn == null) {
			Log.exception(TAG, new NullPointerException("conn == null"));
			return;
		}

		// 允许对外传输数据
		conn.setDoOutput(true);
		// 允许服务器向客户端响应数据（或者发送数据）
		conn.setDoInput(true);
		// 是否使用缓冲
		conn.setUseCaches(false);
	}

	/**
	 * 添加头信息
	 * 
	 * @param conn
	 * @param data
	 */
	protected void addHeader(HttpURLConnection conn, byte[] data) {
		if (conn == null) {
			Log.exception(TAG, new NullPointerException("conn == null"));
			return;
		}

		// 设置MIME类型
		conn.setRequestProperty(Property_Content_Type, getContentType());

		// 设置Charset
		conn.setRequestProperty(Property_Charset, getCharset());

		// 得到发送包大小
		int length = 0;
		if (data != null) {
			length = data.length;
		}
		Log.e(TAG, "Content-Length:"+length);

		// 设置长度
		conn.setRequestProperty(Property_Content_Length, String.valueOf(length));
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
		boolean bConnect = false;
		if (conn != null) {
			try {
				conn.connect();
				bConnect = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.d(TAG, "connect:"+bConnect);
		return bConnect;
	}

	/**
	 * 断开连接
	 * 
	 * @param conn
	 */
	private void disconnect(HttpURLConnection conn) {
		Log.e(TAG, "conn.disconnect(conn);");
		if (conn != null) {
			conn.disconnect();
		}
	}

	/**
	 * 断开连接
	 */
	public void disconnect() {
		Log.e(TAG, "conn.disconnect();");
		disconnect(connection);
	}

	/**
	 * 得到cookie
	 * 
	 * @param conn
	 * @return
	 */
	protected String getCookie(HttpURLConnection conn) {
		Log.e(TAG, "------ get cookie start ------");
		String cookie = null;
		if (conn != null) {
			cookie = conn.getHeaderField(Header_Field_Set_Cookie);
		}else{
			Log.exception(TAG, new NullPointerException("conn == null"));
		}
		Log.e(TAG, "cookie:"+cookie);
		Log.e(TAG, "------ get cookie end ------");
		return cookie;
	}

	/**
	 * 是否使用会话ID
	 * 
	 * @param use
	 */
	//	public void setUseSessionId(boolean use) {
	//		if (use == false) {
	//			// 清除SessionId
	//			clearSessionId();
	//		}
	//		useSessionId = use;
	//	}

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
		Log.e(TAG, "sessionId:"+sessionId);
		return sessionId;
	}

	/**
	 * 得到SessionId会话ID
	 * 
	 * @param cookie
	 * @return
	 */
	protected String getSessionIdFromCookie(String cookie) {
		String sessionId = null;
		if (!isEmptyString(cookie)) {
			int indexStart = 0;
			// TODO 如果找不到;则要长度设置为end
			int indexEnd = cookie.indexOf(';');
			sessionId = cookie.substring(indexStart, indexEnd);
		}else{
			Log.exception(TAG, new NullPointerException("isEmptyString(cookie) == true"));
		}
		Log.e(TAG, "sessionId:"+sessionId);
		return sessionId;
	}

	/**
	 * 添加头SessionId会话ID
	 * 
	 * @param conn
	 */
	protected void addHeaderSessionId(HttpURLConnection conn, String sessionId) {
		Log.e(TAG, "------ add header sessionId start ------");
		if (conn != null && !isEmptyString(sessionId)) {
			Log.e(TAG, "addHeaderSessionId()->sessionId:"+sessionId);
			conn.setRequestProperty(Property_Cookie, sessionId);
			Log.e(TAG, "addHeaderSessionId()->sessionId --- success.");
		}else {
			//			Log.exception(TAG, new NullPointerException("conn == null || isEmptyString(sessionId) == true"));
		}
		Log.e(TAG, "------ add header sessionId end ------");
	}

	/**
	 * 得到响应字节数组
	 * 
	 * @return
	 */
	protected byte[] getResponseByteArray(HttpURLConnection conn) throws IOException {

		if (conn == null) {
			Log.exception(TAG, new NullPointerException("conn == null"));
			return null;
		}

		//		if (data != null) {
		//			OutputStream outputStream = null;
		//			outputStream = conn.getOutputStream();
		//			if (outputStream != null) {
		//				outputStream.write(data); // 输入参数 
		//			}
		//		}

		// 得到输入流（即返回数据）
		InputStream inputStream = null;
		inputStream = conn.getInputStream();
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

		return byteOutputStream.toByteArray();
	}

	/**
	 * 设置完成事件观察者
	 */
	public void setOnCompletedListener(OnCompletedListener listener) {
		this.completedListener = listener;
	}

	/**
	 * get
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public byte[] doGet(String url, String data) {
		Log.e(TAG, "doGet()");
		return executeGet(url, data);
	}

	/**
	 * get执行体
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	protected byte[] executeGet(String url, String data) {
		Log.e(TAG, "executeGet()");

		byte[] out = null;

		try {
			// 打开连接
			connection = openConnection(url);
			if (connection == null) {
				Log.exception(TAG, new NullPointerException("connection == null"));
				if (this.completedListener != null) completedListener.onFailed(Error.ConnectException, new NullPointerException("connection == null"), responseCodeError);
				return null;
			}

			// 初始化连接（公共参数），如：设置超时
			initConnection(connection);

			// 设置请求模式
			Log.e(TAG, "Method.GET.name():"+Method.GET.name());
			connection.setRequestMethod(Method.GET.name());

			// 添加sessionId
			if (isUseSessionId()) {
				addHeaderSessionId(connection, sessionId);
			}else{
				// 得到Cookie
				cookie = getCookie(connection);
				// 得到sessionId会话ID
				sessionId = getSessionIdFromCookie(cookie);
			}

			// 连接（会自动连接，这里就不要了）
			//			Log.e(TAG, "connect start.");
			//			boolean bConnect = connect(connection);
			//			Log.e(TAG, "connect end.");
			//			if (!bConnect) {
			//				Log.exception(TAG, new IOException("bConnect == false"));
			//				return null;
			//			}

			// 打印信息
			print(connection);

			// 得到响应码
			int responseCode = connection.getResponseCode();
			Log.e(TAG, "conn...responseCode:"+responseCode);
			if (responseCode == getResponseCodeOK()) {
				// 得到响应字节数组
				out = getResponseByteArray(connection);
				if (out != null) {
					Log.e(TAG, "out buffer len:"+out.length);
				}
			}else {
				String error = String.format("conn.getResponseCode():"+responseCode+" != HTTP_OK");
				Log.exception(TAG, new IllegalStateException(error));
				if (this.completedListener != null) completedListener.onFailed(Error.ResponseCodeError, new IllegalStateException(error), responseCodeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 处理异常
			handleException(e);
			// 返回
			return null;
		} finally {
			// 断开
			disconnect(connection);
		}

		if (out != null) {
			// 成功
			if (this.completedListener != null) completedListener.onSuccessed(out);
		}

		return out;
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

		if (className != null) {
			if (className.equalsIgnoreCase(new java.io.IOException().getClass().getName())) {
				if (this.completedListener != null) completedListener.onFailed(Error.IOException, e, responseCodeError);
			}else if (className.equalsIgnoreCase(new java.net.SocketException().getClass().getName())) {
				if (this.completedListener != null) completedListener.onFailed(Error.SocketException, e, responseCodeError);
			}else if (className.equalsIgnoreCase(new java.net.UnknownHostException().getClass().getName())) {
				if (this.completedListener != null) completedListener.onFailed(Error.NotNetwork, e, responseCodeError);
			}else if (className.equalsIgnoreCase(new java.net.SocketTimeoutException().getClass().getName())) {
				// 当连接和读的超时时间叠加起来才返回。
				if (this.completedListener != null) completedListener.onFailed(Error.SocketTimeoutException, e, responseCodeError);
			}else {
				if (this.completedListener != null) completedListener.onFailed(Error.Other, e, responseCodeError);
			}
		}else {
			if (this.completedListener != null) completedListener.onFailed(Error.Other, e, responseCodeError);
		}

	}

	/**
	 * post
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public byte[] doPost(String url, byte[] data) {
		Log.e(TAG, "doPost()");
		return executePost(url, data);
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

		byte[] out = null;

		try {
			// 打开连接
			connection = openConnection(url);
			if (connection == null) {
				Log.exception(TAG, new NullPointerException("connection == null"));
				if (this.completedListener != null) completedListener.onFailed(Error.ConnectException, new NullPointerException("connection == null"), responseCodeError);
				return null;
			}

			// 初始化连接（公共参数）
			initConnection(connection);

			// 设置请求模式
			Log.e(TAG, "Method.POST.name():"+Method.POST.name());
			connection.setRequestMethod(Method.POST.name());

			// 得到发送包大小
			//				Log.e(TAG, "data length:"+data.length());
			//				byte[] data_buffer = data.getBytes();
			//				int length = data_buffer.length;
			//				Log.e(TAG, "Content-Length:"+length);

			// 允许对外传输数据
			//				connection.setDoOutput(true);
			//				// 允许服务器向客户端响应数据（或者发送数据）
			//				connection.setDoInput(true);
			//				connection.setUseCaches(false);

			// 设置属性
			//				String contentType = String.format("%s; charset=%s", this.mimeType, this.charset);
			//				Log.e(TAG, "Content-Type:"+contentType);
			//				connection.setRequestProperty("Content-Type", contentType);
			//				//				conn.setRequestProperty("Content-type", "text/xml; charset=GBK");
			//				// conn.setRequestProperty("Charset", "UTF-8");
			//				connection.setRequestProperty("Content-Length", String.valueOf(length));

			// post一定要这个
			this.setContentType(HttpContentType.Application_UrlEncoded);

			// 设置连接参数
			setConntectionParame(connection);

			// 添加头信息
			addHeader(connection, data);

			// 连接
			//				boolean bConnect = connect(connection);
			//				if (!bConnect) {
			//					Log.exception(TAG, new IOException("bConnect == false"));
			//					return null;
			//				}

			// 添加sessionId
			if (isUseSessionId()) {
				addHeaderSessionId(connection, sessionId);
			}else{
				// 得到Cookie
				cookie = getCookie(connection);
				// 得到sessionId会话ID
				sessionId = getSessionIdFromCookie(cookie);
			}

			// post一定要这个
			if (data != null) {
				OutputStream outputStream = null;
				outputStream = connection.getOutputStream();
				if (outputStream != null) {
					outputStream.write(data); // 写入参数 
				}
			}

			// 打印信息
			print(connection);

			// 得到响应码
			int responseCode = connection.getResponseCode();
			Log.e(TAG, "conn...responseCode:"+responseCode);
			if (responseCode == getResponseCodeOK()) {
				// 得到响应字节数组
				out = getResponseByteArray(connection);
				if (out != null) {
					Log.e(TAG, "out buffer len:"+out.length);
				}
			}else {
				String error = String.format("conn.getResponseCode():"+responseCode+" != HTTP_OK");
				Log.exception(TAG, new IllegalStateException(error));
				if (this.completedListener != null) completedListener.onFailed(Error.ResponseCodeError, new IllegalStateException(error), responseCodeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// 处理异常
			handleException(e);
			// 返回
			return null;
		} finally {
			// 断开
			disconnect(connection);
		}

		if (out != null) {
			// 成功
			if (this.completedListener != null) completedListener.onSuccessed(out);
		}

		return out;
	}

	/**
	 * 打印信息
	 * 
	 * @param connection
	 */
	protected void print(HttpURLConnection connection) {
		Log.e(TAG, "------ Client提交参数列表  start ------");
		Log.e(TAG, "getCharset:"+getCharset());
		Log.e(TAG, "getContentType:"+getContentType());
		Log.e(TAG, "getReadTimeout:"+getReadTimeout());
		Log.e(TAG, "getConnectTimeout:"+getConnectTimeout());
		Log.e(TAG, "getResponseCodeOK:"+this.getResponseCodeOK());
		Log.e(TAG, "------ Client提交参数列表  end ------");

		Log.e(TAG, "------ Server返回参数列表  start ------");
		if (connection != null) {
			Log.e(TAG, "conn.getConnectTimeout:"+connection.getConnectTimeout());
			Log.e(TAG, "conn.getReadTimeout:"+connection.getReadTimeout());
			Log.e(TAG, "conn.getRequestMethod:"+connection.getRequestMethod());
			Log.e(TAG, "conn.getContentType:"+connection.getContentType());
			Log.e(TAG, "conn.getContentLength:"+connection.getContentLength());
			Log.e(TAG, "conn.getContentEncoding:"+connection.getContentEncoding());
		}
		Log.e(TAG, "------ Server返回参数列表  end ------");
	}

	private String doGetOld(String http, String data) {

		if (isEmptyString(http)) {
			//			if (this.onCompleteListener != null) {
			//				this.onCompleteListener.onComplete(this.Http, null, false);
			//			}
			new NullPointerException("http.isEmpty").printStackTrace();
			return null;
		}

		String out = null;

		try {

			URL url = new URL(http);

			// 打开连接
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			if (conn != null) {

				// 设置超时
				conn.setConnectTimeout(connectTimeout);
				Log.e(TAG, "mTimeout:"+connectTimeout);

				// 设置请求模式
				conn.setRequestMethod(Method.GET.name());

				// 得到文本类型
				String contentType = conn.getContentType();
				Log.e(TAG, "conn.getContentType():"+contentType);

				// 从contentType字符串获得字符集
				if (contentType != null) {
					this.charset = findCharset(pattern, contentType);
					Log.e(TAG, "contentType->mCharset:"+charset);
				}

				// 得到响应码
				int responseCode = conn.getResponseCode();
				Log.e(TAG, "responseCode:"+responseCode);
				if (responseCode == responseCodeOK) {

					// 得到输入流（即返回数据）
					InputStream inStream = conn.getInputStream();

					// 读输入流
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[MAX_BUFFER_LENGTH];

					int len = 0;
					while((len = inStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, len);
						outputStream.flush();
					}

					// 从输出流查找字符集
					if (charset == null) {
						charset = findCharset(pattern, outputStream.toString());
						Log.e(TAG, "OutputStream->mCharset:"+charset);
					}

					// 关闭流
					outputStream.close();
					inStream.close();

					// 使用系统默认的字符集 
					if (charset == null) {
						charset = Charset.defaultCharset().name();
						Log.e(TAG, "System->defaultCharset:"+charset);
					}

					if (charset != null) {
						// 通过字符集转为可视字符串
						out = new String(outputStream.toByteArray(), charset);
					}else{
						out = new String(outputStream.toByteArray());
					}

				}else{
					String error = String.format("conn.getResponseCode():"+responseCode+" != HTTP_OK");
					new IllegalStateException(error).printStackTrace();
				}

			}else{
				new NullPointerException("conn == null").printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		//		if (success) {
		//			// 成功
		//			if (this.onCompleteListener != null) {
		//				this.onCompleteListener.onComplete(this.Http, out, true);
		//			}
		//		}else{
		//			// 失败
		//			if (this.onCompleteListener != null) {
		//				this.onCompleteListener.onComplete(this.Http, null, false);
		//			}
		//		}

		return out;
	}

	protected String executeOldPost(String url, byte[] data) {
		Log.e(TAG, "doPost()");

		//		if (isEmptyString(url)) {
		//			new NullPointerException("url isEmpty || data isEmpty").printStackTrace();
		//			return null;
		//		}

		String out = null;

		try {

			// URL url = new URL(http);

			// 打开连接
			connection = openConnection(url);

			// 打开连接
			//			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			if (connection != null) {

				// 设置超时
				//				connection.setConnectTimeout(timeout);
				//				Log.e(TAG, "mTimeout:"+timeout);

				// 初始化连接（公共参数）
				initConnection(connection);

				// 设置请求模式
				connection.setRequestMethod(Method.POST.name());

				// 得到发送包大小
				//				Log.e(TAG, "data length:"+data.length());
				//				byte[] data_buffer = data.getBytes();
				//				int length = data_buffer.length;
				//				Log.e(TAG, "Content-Length:"+length);

				// 允许对外传输数据
				//				connection.setDoOutput(true);
				//				// 允许服务器向客户端响应数据（或者发送数据）
				//				connection.setDoInput(true);
				//				connection.setUseCaches(false);

				// 设置属性
				//				String contentType = String.format("%s; charset=%s", this.mimeType, this.charset);
				//				Log.e(TAG, "Content-Type:"+contentType);
				//				connection.setRequestProperty("Content-Type", contentType);
				//				//				conn.setRequestProperty("Content-type", "text/xml; charset=GBK");
				//				// conn.setRequestProperty("Charset", "UTF-8");
				//				connection.setRequestProperty("Content-Length", String.valueOf(length));

				// 设置连接参数
				setConntectionParame(connection);

				// 添加头信息
				addHeader(connection, data);

				// 连接
				//				boolean bConnect = connect(connection);
				//				if (!bConnect) {
				//					Log.exception(TAG, new IOException("bConnect == false"));
				//					return null;
				//				}

				// 输出流（即发送数据）
				OutputStream outStream = connection.getOutputStream();

				if (outStream != null) {
					//					outStream.write(data_buffer);
					outStream.write(null);
					outStream.flush();
					outStream.close();

					// 得到响应码（要等发送数据完毕后，才得到响应码）
					int responseCode = connection.getResponseCode();
					Log.e(TAG, "responseCode:"+responseCode);
					Log.e(TAG, "conn.getResponseMessage():"+connection.getResponseMessage());
					if (responseCode == responseCodeOK) {

						// 得到输入流（即返回数据）
						InputStream inStream = connection.getInputStream();

						if (inStream != null) {

							// 读输入流
							ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
							byte[] buffer = new byte[MAX_BUFFER_LENGTH];

							int len = 0;
							while((len = inStream.read(buffer)) != -1) {
								byteOutStream.write(buffer, 0, len);
								byteOutStream.flush();
							}

							// 关闭流
							byteOutStream.close();
							inStream.close();

							// 将字节流转成字符串
							//				out = new String(byteOutStream.toByteArray(), "UTF-8");
							out = new String(byteOutStream.toByteArray());

						}else{
							new NullPointerException("inStream == null").printStackTrace();
						}

					}else{
						String error = String.format("conn.getResponseCode():"+responseCode+" != HTTP_OK");
						new IllegalStateException(error).printStackTrace();
					}

				}else{
					new NullPointerException("outStream == null").printStackTrace();
				}

			}else{
				new NullPointerException("conn == null").printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;
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

	/**
	 * 字节数组转成字符串
	 * 
	 * @return
	 */
	public static String bytes2String(byte[] buffer, String charset) {
		if (buffer == null) {
			Log.exception(TAG, new NullPointerException("buffer == null"));
			return null;
		}

		if (isEmptyString(charset)) {
			return null;
		}

		try {
			return new String(buffer, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
