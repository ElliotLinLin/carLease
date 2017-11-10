package com.tools.net.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tools.util.Log;


/**
 *	
 *	听说2.3及以下的httpurlconnection有少量的BUG 
 * 
 * 主要下述方法（还要考虑编码，最好是可以设置编码）
 * sendGET		以GET模式提交数据，不等待返回
 * sendPOST		以POST模式提交数据，不等待返回（没完成）
 * 
 * requestGET		以GET模式提交数据，并且获取返回数据
 * requestPOST		以POST模式提交???数据，并且获取返回数据（没完成）
 * 
 * requestJSON		以GET模式提交JSON数据，并且获取返回数据
 * requestXML		以GET模式提交XML数据，并且获取返回数据
 * requestFile		发送文件，如：图片，文本等（没有完成）
 * 
 * 使用前要加入权限
 * <uses-permission android:name="android.permission.INTERNET"/>
 * 
 * 发送JSON数据例子：
 * JSONObject head = new JSONObject();
 * .......
 * .......
	HttpTool http = new HttpTool();
	http.setTimeout(3000); // 超时
	http.setCharset("UTF-8"); // 编码
	// http://127.0.0.1:9090/plugins/exampleplugin/?username=aaaaa&password=bbbbb
	String data = http.requestJSON(INFO.mHost+"/plugins/exampleplugin/?", JSON.toJSONString(head));
	Log.e(TAG, "result_data:"+data);

	sendGET()例子
	 HttpTool http = new HttpTool();
     http.setTimeout(3000); // 超时
     http.sendGET("http://www.baidu.com");

     requestGET()例子
      HttpTool http = new HttpTool();
      http.setTimeout(3000);
      String data = http.requestGET("http://www.baidu.com");

 * 
 * 
 * 
 * 
 * 
 * 
 * android-async-http 是 Android 上的一个异步 HTTP 客户端开发包。

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
 * 
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class TmpHttpToolB {

	private static final String TAG = HttpTool.class.getSimpleName();
	private static final boolean DEBUG = true;
	
	private static final int MAX_LENGTH = 1024;

	protected int timeout = 10 * 1000; // 10秒
	protected String charset = "UTF-8"; // 默认为utf-8
	protected String Http = null;
	protected String Data = null;
	protected URL URL = null;
	protected String mimeType = "text/plain";
	// HttpURLConnection.HTTP_OK
	protected int responseCodeSuccess = HttpURLConnection.HTTP_OK;
	protected HttpURLConnection connection = null;
	protected boolean enableInput = false;

	protected OnEventListener onCompleteListener = null;

	protected Pattern pattern = Pattern.compile("charset.*=.*>?", Pattern.CASE_INSENSITIVE);

	public enum Method {
		GET,POST,DELETE,PUT,TRACE,HEAD,OPTIONS,CONNECT
	}

	public interface OnEventListener {
		void onComplete(String http, String text, boolean status);
		//		void onSuccessed(ResponseMessage response);
		//		void onFailed(ResponseMessage response);
		//		void onCanceled(ResponseMessage response);
		String doInBackground(String... params);
		void onPostExecute(String result);
	}

	public static class ResponseMessage {
		public URL url = null;
		public String text = null;
		public int responseCode = 0;
	}

	public TmpHttpToolB() {

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

	public boolean setURL(String url) {
		boolean result = false;
		try {
			this.URL = new URL(url);
			result = true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			//			throw new IllegalArgumentException("MalformedURLException");
		}
		Log.i(TAG, "setURL:"+result);
		return result;
	}

	public boolean isEnableInput() {
		return enableInput;
	}

	public void setEnableInput(boolean enableInput) {
		this.enableInput = enableInput;
	}

	public int getResponseCodeSuccess() {
		return responseCodeSuccess;
	}

	public void setResponseCodeSuccess(int responseCodeSuccess) {
		this.responseCodeSuccess = responseCodeSuccess;
	}

	/**
	 * 获得超时时间
	 * 
	 * @return
	 */
	public int getTimeout() {
		return this.timeout;
	}

	/**
	 * 设置超时时间
	 * 
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * 获取编码
	 * 
	 * @return
	 */
	public String getCharset() {
		if (charset == null) {
			return null;
		}
		return this.charset.toLowerCase();
	}

	/**
	 * 设置编码
	 * 
	 * @param charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * 获得MIME类型
	 * 
	 * @return
	 */
	public String getMimeType() {
		return this.mimeType;
	}

	/**
	 * http://zh.wikipedia.org/wiki/MIME
	 * 
	 * 设置MIME类型
	 * 
	 * @param mimeType
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setMimeTypeJSON() {
		this.mimeType = "application/json";
	}

	public void setMimeTypeXML() {
		this.mimeType = "application/xml";
	}

	protected void setContentType(HttpURLConnection conn) {
		// 设置属性
		String contentType = String.format("%s; charset=%s", this.mimeType, this.charset);
		if (DEBUG) Log.i(TAG, "Content-Type:"+contentType);
		conn.setRequestProperty("Content-Type", contentType);
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
			if (DEBUG) new NullPointerException("findCharset()->matcher == null").printStackTrace();
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
	public static String toCharset(String text, String encoding) {

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

	public HttpURLConnection getConnection() {
		return connection;
	}

	public HttpURLConnection openConnection() {
		try {
			connection = (HttpURLConnection)this.URL.openConnection();
			// 设置常用
			initConnection(connection);
			return connection;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void initConnection(HttpURLConnection conn) {
		// 设置超时
		conn.setConnectTimeout(timeout);
		if (DEBUG) Log.i(TAG, "mTimeout:"+timeout);

		// 得到发送包大小
		//		if (DEBUG) Log.i(TAG, "data length:"+data.length());
		//		byte[] data_buffer = data.getBytes();
		//		int length = data_buffer.length;
		//		if (DEBUG) Log.i(TAG, "Content-Length:"+length);

		// 允许对外传输数据
		conn.setDoOutput(true);
		// 允许服务器向客户端响应数据（或者发送数据）
		conn.setDoInput(true);
		// 不使用缓存
		conn.setUseCaches(false);

		// 设置属性
		//		String contentType = String.format("%s; charset=%s", this.mimeType, this.charset);
		//		if (DEBUG) Log.i(TAG, "Content-Type:"+contentType);
		//		conn.setRequestProperty("Content-Type", contentType);
		//				conn.setRequestProperty("Content-type", "text/xml; charset=GBK");
		//		conn.setRequestProperty("Content-Length", String.valueOf(length));
	}

	protected void setPropertyLength(HttpURLConnection conn, String text) {
		// 得到发送包大小
		if (DEBUG) Log.i(TAG, "data length:"+text.length());
		byte[] buffer = text.getBytes();
		int length = buffer.length;
		if (DEBUG) Log.i(TAG, "Content-Length:"+length);
		conn.setRequestProperty("Content-Length", String.valueOf(length));
	}

	/**
	 * 获取服务器返回给客户端的数据（获取数据）
	 * 
	 * @param conn
	 * @return
	 */
	public String getResultString(HttpURLConnection conn) {

		if (conn == null) {
			return null;
		}

		String outString = null;
		boolean success = false;

		try {
			// 连接
			conn.connect();

			// 得到文本类型
			String contentType = conn.getContentType();
			if (DEBUG) Log.i(TAG, "conn.getContentType():"+contentType);

			// 从contentType字符串获得字符集
			if (contentType != null) {
				this.charset = findCharset(pattern, contentType);
				if (DEBUG) Log.i(TAG, "contentType->mCharset:"+charset);
			}

			// 得到响应码
			int responseCode = conn.getResponseCode();
			if (DEBUG) Log.i(TAG, "responseCode:"+responseCode);
			if (responseCode == responseCodeSuccess) {

				// 得到输入流（即返回数据）
				InputStream inStream = conn.getInputStream();

				// 读输入流
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[MAX_LENGTH];

				int len = 0;
				while((len = inStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, len);
					outputStream.flush();
				}

				// 从输出流查找字符集
				if (charset == null) {
					charset = findCharset(pattern, outputStream.toString());
					if (DEBUG) Log.i(TAG, "OutputStream->mCharset:"+charset);
				}

				// 关闭流
				outputStream.close();
				inStream.close();

				// 使用系统默认的字符集 
				if (charset == null) {
					charset = Charset.defaultCharset().name();
					if (DEBUG) Log.i(TAG, "System->defaultCharset:"+charset);
				}

				if (charset != null) {
					// 通过字符集转为可视字符串
					outString = new String(outputStream.toByteArray(), charset);
				}else{
					outString = new String(outputStream.toByteArray());
				}

				// 成功
				success = true;

			}else{
				String error = String.format("conn.getResponseCode():"+responseCode+" != HTTP_OK");
				if (DEBUG) new IllegalStateException(error).printStackTrace();
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

		return outString;
	}

	/**
	 * 客户端向服务器发送数据（发送数据）
	 * 
	 * @param conn
	 * @param text
	 * @return
	 */
	protected String sendString(HttpURLConnection conn, String text) {

		String outString = null;

		try {
			// 连接
			conn.connect();

			// 输出流（即发送数据）
			OutputStream outStream = conn.getOutputStream();

			if (outStream != null) {

				byte[] sbuffer = text.getBytes();
				outStream.write(sbuffer);
				outStream.flush();
				outStream.close();

				// 得到响应码（要等发送数据完毕后，才得到响应码）
				int responseCode = conn.getResponseCode();
				if (DEBUG) Log.i(TAG, "responseCode:"+responseCode);
				if (DEBUG) Log.i(TAG, "conn.getResponseMessage():"+conn.getResponseMessage());
				if (responseCode == responseCodeSuccess) {

					// 得到输入流（即返回数据）
					InputStream inStream = conn.getInputStream();

					if (inStream != null) {

						// 读输入流
						ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
						byte[] buffer = new byte[MAX_LENGTH];

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
						outString = new String(byteOutStream.toByteArray());

						// 成功
						//					success = true;

					}else{
						if (DEBUG) new NullPointerException("inStream == null").printStackTrace();
					}

				}else{
					String error = String.format("conn.getResponseCode():"+responseCode+" != HTTP_OK");
					if (DEBUG) new IllegalStateException(error).printStackTrace();
				}

			}else{
				if (DEBUG) new NullPointerException("outStream == null").printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return outString;
	}

	/**
	 * 不用设置Content-Type和Content-Length
	 * 最多1024字节
	 * 
	 * @return
	 */
	public String doGet(String url) {

		// 设置URL
		if(!setURL(url)) {
			return null;
		}

		// 打开连接
		HttpURLConnection conn = openConnection();
		if(conn == null) {
			return null;
		}

		// 设置请求模式
		try {
			conn.setRequestMethod(Method.GET.name());
		} catch (ProtocolException e) {
			e.printStackTrace();
			return null;
		}

		// 得到返回字符串
		String out = getResultString(conn);

		return out;
	}

	public String doGetAsync(String url, OnEventListener listener) {
		return null;
	}

	/**
	 * 需要设置Content-Type和Content-Length
	 * 
	 * @param text
	 * @return
	 */
	public String doPost(String url, String text) {
		// 设置URL
		setURL(url);

		if(isEmptyString(text)) {
			new NullPointerException("text == null").printStackTrace();
			return null;
		}

		// 打开连接
		HttpURLConnection conn = openConnection();
		if(conn == null) {
			return null;
		}

		// 设置请求模式
		try {
			conn.setRequestMethod(Method.POST.name());
		} catch (ProtocolException e) {
			e.printStackTrace();
			return null;
		}

		// 设置ContentType
		setContentType(conn);

		// 设置大小
		setPropertyLength(conn, text);

		// 得到返回流
		String result = sendString(conn, text);

		return result;
	}

	public String doPostAsync(String url, String text, OnEventListener listener) {
		return null;
	}

	public void doPut() {

	}

	public void doDelete() {

	}

	public void doHead() {

	}

	public void doTrace() {

	}

	public void doOptions() {

	}

	public void doConnect() {

	}

	/**
	 * 关闭异步任务 
	 */
	public void cancel() {

	}

	/**
	 * 发送JSON执行体
	 * 
	 * @param http
	 * @param data
	 * @return
	 */
	protected String doJson(String http, String data) {

		// 设置JSON类型
		//		setContentTypeJSON();

		return doPost(http, data);
	}

	/**
	 * 发送XML执行休
	 * 
	 * @param http
	 * @param data
	 * @return
	 */
	protected String doXml(String http, String data) {

		// 设置JSON类型
		//		setContentTypeXML();

		return doPost(http, data);
	}

	/**
	 * post执行体，并且返回数据
	 * 
	 * @param http
	 * @param data
	 * @return
	 */
	protected String doPostAAA(String http, String data) {

		if (isEmptyString(http) || isEmptyString(data)) {
			if (this.onCompleteListener != null) {
				this.onCompleteListener.onComplete(this.Http, null, false);
			}
			if (DEBUG) new NullPointerException("http.isEmpty || jsondata.isEmpty").printStackTrace();
			return null;
		}

		String out = null;
		boolean success = false;

		try {

			URL url = new URL(http);

			// 打开连接
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			if (conn != null) {

				// 设置超时
				conn.setConnectTimeout(timeout);
				if (DEBUG) Log.i(TAG, "mTimeout:"+timeout);

				// 设置请求模式
				conn.setRequestMethod("POST");

				// 得到发送包大小
				if (DEBUG) Log.i(TAG, "data length:"+data.length());
				byte[] data_buffer = data.getBytes();
				int length = data_buffer.length;
				if (DEBUG) Log.i(TAG, "Content-Length:"+length);

				// 允许对外传输数据
				conn.setDoOutput(true);
				// 允许服务器向客户端响应数据（或者发送数据）
				conn.setDoInput(true);
				conn.setUseCaches(false);

				// 设置属性
				String contentType = String.format("%s; charset=%s", this.mimeType, this.charset);
				if (DEBUG) Log.i(TAG, "Content-Type:"+contentType);
				conn.setRequestProperty("Content-Type", contentType);
				//				conn.setRequestProperty("Content-type", "text/xml; charset=GBK");
				conn.setRequestProperty("Content-Length", String.valueOf(length));

				// 连接
				conn.connect();

				// 输出流（即发送数据）
				OutputStream outStream = conn.getOutputStream();

				if (outStream != null) {

					outStream.write(data_buffer);
					outStream.flush();
					outStream.close();

					// 得到响应码（要等发送数据完毕后，才得到响应码）
					int responseCode = conn.getResponseCode();
					if (DEBUG) Log.i(TAG, "responseCode:"+responseCode);
					if (DEBUG) Log.i(TAG, "conn.getResponseMessage():"+conn.getResponseMessage());
					if (responseCode == responseCodeSuccess) {

						// 得到输入流（即返回数据）
						InputStream inStream = conn.getInputStream();

						if (inStream != null) {

							// 读输入流
							ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
							byte[] buffer = new byte[MAX_LENGTH];

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

							// 成功
							success = true;

						}else{
							if (DEBUG) new NullPointerException("inStream == null").printStackTrace();
						}

					}else{
						String error = String.format("conn.getResponseCode():"+responseCode+" != HTTP_OK");
						if (DEBUG) new IllegalStateException(error).printStackTrace();
					}

				}else{
					if (DEBUG) new NullPointerException("outStream == null").printStackTrace();
				}

			}else{
				if (DEBUG) new NullPointerException("conn == null").printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (success) {
			// 成功
			if (this.onCompleteListener != null) {
				this.onCompleteListener.onComplete(this.Http, out, true);
			}
		}else{
			// 失败
			if (this.onCompleteListener != null) {
				this.onCompleteListener.onComplete(this.Http, null, false);
			}
		}

		return out;
	}

	/**
	 * 使用网页的字符集charset，如果得不到，则使用系统默认的字符集
	 * 
	 * @param http
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public String doGetAAA(String http) {

		if (isEmptyString(http)) {
			if (this.onCompleteListener != null) {
				this.onCompleteListener.onComplete(this.Http, null, false);
			}
			if (DEBUG) new NullPointerException("http.isEmpty").printStackTrace();
			return null;
		}

		String out = null;
		boolean success = false;

		try {
			URL url = new URL(http);

			// 打开连接
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			if (conn != null) {

				// 设置超时
				conn.setConnectTimeout(timeout);
				if (DEBUG) Log.i(TAG, "mTimeout:"+timeout);

				// 设置请求模式
				conn.setRequestMethod("GET");

				// 得到文本类型
				String contentType = conn.getContentType();
				if (DEBUG) Log.i(TAG, "conn.getContentType():"+contentType);

				// 从contentType字符串获得字符集
				if (contentType != null) {
					this.charset = findCharset(pattern, contentType);
					if (DEBUG) Log.i(TAG, "contentType->mCharset:"+charset);
				}

				// 得到响应码
				int responseCode = conn.getResponseCode();
				if (DEBUG) Log.i(TAG, "responseCode:"+responseCode);
				if (responseCode == responseCodeSuccess) {

					// 得到输入流（即返回数据）
					InputStream inStream = conn.getInputStream();

					// 读输入流
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[MAX_LENGTH];

					int len = 0;
					while((len = inStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, len);
						outputStream.flush();
					}

					// 从输出流查找字符集
					if (charset == null) {
						charset = findCharset(pattern, outputStream.toString());
						if (DEBUG) Log.i(TAG, "OutputStream->mCharset:"+charset);
					}

					// 关闭流
					outputStream.close();
					inStream.close();

					// 使用系统默认的字符集 
					if (charset == null) {
						charset = Charset.defaultCharset().name();
						if (DEBUG) Log.i(TAG, "System->defaultCharset:"+charset);
					}

					if (charset != null) {
						// 通过字符集转为可视字符串
						out = new String(outputStream.toByteArray(), charset);
					}else{
						out = new String(outputStream.toByteArray());
					}

					// 成功
					success = true;

				}else{
					String error = String.format("conn.getResponseCode():"+responseCode+" != HTTP_OK");
					if (DEBUG) new IllegalStateException(error).printStackTrace();
				}

			}else{
				if (DEBUG) new NullPointerException("conn == null").printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (success) {
			// 成功
			if (this.onCompleteListener != null) {
				this.onCompleteListener.onComplete(this.Http, out, true);
			}
		}else{
			// 失败
			if (this.onCompleteListener != null) {
				this.onCompleteListener.onComplete(this.Http, null, false);
			}
		}

		return out;
	}

	/**
	 * 单单提交，不理会是否返回数据
	 * 
	 * @return
	 */
	protected boolean sendGET(String http) {

		boolean success = false;

		if (isEmptyString(http)) {
			return false;
		}

		try {
			URL url = new URL(http);

			// 打开连接
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			if (conn != null) {

				// 设置超时
				conn.setConnectTimeout(timeout);
				if (DEBUG) Log.i(TAG, "mTimeout:"+timeout);

				// 设置请求模式
				conn.setRequestMethod("GET");

				// 得到响应码
				int responseCode = conn.getResponseCode();
				if (responseCode == responseCodeSuccess) {
					// 成功
					success = true;
				}else{
					String error = String.format("conn.getResponseCode():"+responseCode+" != HTTP_OK");
					if (DEBUG) new IllegalStateException(error).printStackTrace();
				}

			}else{
				if (DEBUG) new NullPointerException("conn == null").printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	protected void requestGET(OnEventListener listener, String http) {

		if (listener == null) {
			throw new NullPointerException("listener == null");
		}
		this.onCompleteListener = listener;

		this.Http = http;

		new Thread() {

			@Override
			public void run() {

				doGet(Http);

				super.run();
			}

		}.start();

	}

	protected String requestGET(String http) {
		this.onCompleteListener = null;
		this.Http = http;
		return doGet(Http);
	}

	/**
	 * 以回调的方式和POST模式发送JSON数据包，并得到返回数据包
	 * 
	 * @param listener
	 * @param http
	 * @param data
	 */
	protected void requestJSON(OnEventListener listener, String http, String data) {

		if (listener == null) {
			throw new NullPointerException("listener == null");
		}
		this.onCompleteListener = listener;

		this.Http = http;
		this.Data = data;

		new Thread() {

			@Override
			public void run() {

				doJson(Http, Data);

				super.run();
			}

		}.start();

	}

	/**
	 * 以POST模式发送JSON数据包，并得到返回数据包
	 * 
	 * @param http
	 * @param data
	 * @return
	 */
	protected String requestJSON(String http, String data) {
		this.onCompleteListener = null;
		this.Http = http;
		this.Data = data;
		return doJson(Http, Data);
	}

	protected void requestXML(OnEventListener listener, String http, String data) {

		if (listener == null) {
			throw new NullPointerException("listener == null");
		}
		this.onCompleteListener = listener;

		this.Http = http;
		this.Data = data;

		new Thread() {

			@Override
			public void run() {

				doXml(Http, Data);

				super.run();
			}

		}.start();

	}

	protected String requestXML(String http, String data) {
		this.onCompleteListener = null;
		this.Http = http;
		this.Data = data;
		return doXml(Http, Data);
	}

	/*
	protected String doJson(String http, String jsondata) {

		if (isEmpty(http) || isEmpty(jsondata)) {
			if (this.mOnCompleteListener != null) {
				this.mOnCompleteListener.onComplete(this.mHttp, null, false);
			}
			if (DEBUG) new NullPointerException("http.isEmpty || jsondata.isEmpty").printStackTrace();
			return null;
		}

		// 设置JSON类型
		setContentTypeJSON();

		String out = null;
		boolean success = false;

		try {
			URL url = new URL(http);

			// 打开连接
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();

			if (conn != null) {

				// 设置超时
				conn.setConnectTimeout(mTimeout);
				if (DEBUG) Log.i(TAG, "mTimeout:"+mTimeout);

				// 设置请求模式
				conn.setRequestMethod("POST");

				// 得到发送包大小
				if (DEBUG) Log.i(TAG, "jsondata length:"+jsondata.length());
				byte[] data_buffer = jsondata.getBytes();
				int length = data_buffer.length;
				if (DEBUG) Log.i(TAG, "Content-Length:"+length);

				// 允许对外传输数据
				conn.setDoOutput(true);

				// 设置属性
				String contentType = String.format("%s; charset=%s", mContentType, mCharset);
				if (DEBUG) Log.i(TAG, "Content-Type:"+contentType);
				conn.setRequestProperty("Content-Type", contentType);
				conn.setRequestProperty("Content-Length", String.valueOf(length));

				// 输出流（即发送数据）
				OutputStream outStream = conn.getOutputStream();
				outStream.write(data_buffer);
				outStream.flush();
				outStream.close();

				// 得到响应码
				if (conn.getResponseCode() == responseCodeSuccess) {

					// 得到输入流（即返回数据）
					InputStream inStream = conn.getInputStream();

					// 读输入流
					ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[OUTPUT_STREAM_MAX_LENGTH];

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

					// 成功
					success = true;

				}else{
					if (DEBUG) new IllegalStateException("conn.getResponseCode() != HTTP_OK").printStackTrace();
				}

			}else{
				if (DEBUG) new NullPointerException("conn == null").printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (success) {
			// 成功
			if (this.mOnCompleteListener != null) {
				this.mOnCompleteListener.onComplete(this.mHttp, out, true);
			}
		}else{
			// 失败
			if (this.mOnCompleteListener != null) {
				this.mOnCompleteListener.onComplete(this.mHttp, null, false);
			}
		}

		return out;
	}
	 */

}
