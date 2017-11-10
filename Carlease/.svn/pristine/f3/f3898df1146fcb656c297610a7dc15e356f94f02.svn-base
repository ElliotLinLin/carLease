package com.tools.net.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.tools.util.Log;

/**
 * HTTP配置参数
 * 
 * @author LMC
 *
 */
public class HttpConfig {

	private static final String TAG = HttpConfig.class.getSimpleName();

	private int connectTimeout = 20 * 1000; // 5s-10s太短会出错，建议20s
	private int readTimeout = 20 * 1000; // 建议20s

	// 请求头
	private HttpConfig.Header header = new HttpConfig.Header();

	// 代理（代理类型有三种，直接；HTTP; SOCKET）
	private HttpConfig.Proxy proxy = null;

	// SSL(https)
	private HttpConfig.SSL SSL = null;

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

	public HttpConfig.Proxy getProxy() {
		return proxy;
	}

	public void setProxy(HttpConfig.Proxy proxy) {
		this.proxy = proxy;
	}

	public HttpConfig.SSL getSSL() {
		return SSL;
	}

	public void setSSL(HttpConfig.SSL sSL) {
		SSL = sSL;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
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

	public void print() {
		Log.e(TAG, "--- HttpConfig() start ---");
		Log.d(TAG, "getConnectTimeout():"+getConnectTimeout());
		Log.d(TAG, "getReadTimeout():"+getReadTimeout());
		Log.e(TAG, "--- HttpConfig() end ---");
	}

	public static class Header {

		private static final String TAG = Header.class.getSimpleName();

		private String charset = com.tools.os.Charset.UTF_8;

		private final static String ___charset___ = "; charset=";

		private String contentType = HttpContentType.Application_JSON + ___charset___ + charset;

		private boolean bContentType = false;

		private String accept_Encoding = HttpHeaderField.Encoding_GZIP_Deflate;

		// 默认最少有一个Content_Type
		private List<BasicNameValuePair> fields = new ArrayList<BasicNameValuePair>();

		// 会话
		private String sessionId = null;

		/**
		 * 默认要加入一个ContentType
		 * 
		 * @return
		 */
		public List<BasicNameValuePair> getFields() {

			// 加入一个默认的
			// TODO 默认不要，除非setContentType(...)设置
			if (bContentType) {
				addField(HttpHeaderField.Field_Content_Type, getContentType());
			}

			if (isEmptyString(sessionId) == false) {
				addField(HttpHeaderField.Field_Cookie, getSessionId());
			}

			// TODO 默认加入客户端GZIP编码
			if (isEmptyString(accept_Encoding) == false) {
				addField(HttpHeaderField.Field_Accept_Encoding, accept_Encoding);
			}

			return fields;
		}

		public void addField(BasicNameValuePair pair) {
			fields.add(pair);
		}

		public void addField(String name, String value) {
			fields.add( new BasicNameValuePair(name, value) );
		}

		// TODO 没有测试过
		public void addField(List<BasicNameValuePair> list) {
			this.fields.addAll( list );
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType, String charset) {
			this.bContentType = true;
			setCharset(charset);
			this.contentType = contentType + ___charset___ + getCharset();
		}

		public void setContentTypeJSON() {
			setContentType(HttpContentType.Application_JSON , getCharset());
		}

		public void setContentTypeXML() {
			setContentType(HttpContentType.Application_XML , getCharset());
		}

		// 要用到的
		public String getCharset() {
			return charset;
		}

		// 要用到的
		public void setCharset(String charset) {
			this.charset = charset;
		}

		public String getSessionId() {
			return sessionId;
		}

		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}

		public void print() {
			Log.e(TAG, "--- HttpConfig.Header() start ---");
			Log.d(TAG, "getCharset():"+getCharset());
			Log.d(TAG, "getContentType():"+getContentType());
			Log.d(TAG, "getSessionId():"+getSessionId());
			int len = fields.size();
			Log.d(TAG, "fields.size():"+len);
			for (int n = 0; n < len; n++) {
				BasicNameValuePair pair = fields.get(n);
				String name = pair.getName();
				String value = pair.getValue();
				Log.d(TAG, "Header name:"+name+",value:"+value);
			}
			Log.e(TAG, "--- HttpConfig.Header() end ---");
		}

	}

	/**
	 * 代理
	 * 
	 * @author LMC
	 *
	 */
	public static class Proxy {
		// 代理类型，有三种，直接；HTTP; SOCKET
		private java.net.Proxy.Type type = java.net.Proxy.Type.DIRECT;
		private String scheme = HttpTool.Scheme_Http; // http or https
		private String hostname;
		private int port = 8080;
		private String username;
		private String password;
		public java.net.Proxy.Type getType() {
			return type;
		}
		public void setType(java.net.Proxy.Type type) {
			this.type = type;
		}
		public String getScheme() {
			return scheme;
		}
		public void setScheme(String scheme) {
			this.scheme = scheme;
		}
		public String getHostname() {
			return hostname;
		}
		public void setHostname(String hostname) {
			this.hostname = hostname;
		}
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public void print() {
			Log.e(TAG, "--- HttpConfig.Proxy() start ---");
			Log.d(TAG, "getType():"+getType().name());
			Log.d(TAG, "getScheme():"+getScheme());
			Log.d(TAG, "setHostname():"+getHostname());
			Log.d(TAG, "getPort():"+getPort());
			Log.d(TAG, "getUsername():"+getUsername());
			Log.d(TAG, "getPassword():"+getPassword());
			Log.e(TAG, "--- HttpConfig.Proxy() end ---");
		}
	}

	public static class SSL {
		public void print() {
			Log.e(TAG, "--- HttpConfig.SSL() start ---");
			Log.e(TAG, "--- HttpConfig.SSL() end ---");
		}
	}

}
