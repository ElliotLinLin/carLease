package com.tools.util;

import java.io.File;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 用于校验，如：检查File是否可用，检查HTTP是否有效，检查URL，检查String
 * 
 * string/file/http/url/email/phone/number/ftp/rstp
 * 
 * 也就是是说只要你的静态方法不访问全局变量的话，就不会有并发问题 
 * 
 * URL是指绝对路径
 * 
 * URI是指相对路径
 * 
 * scheme://host:port/path
 * 
 * 没有host的，则写成file:形式
 * 有host的，则写成http://形式
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class VerifyUtil {

	private static final String TAG = VerifyUtil.class.getSimpleName();

	public static String text = "aaa";

	public static final String ASSET_BASE = "file:///android_asset/";
	public static final String RESOURCE_BASE = "file:///android_res/";
	public static final String PROXY_BASE = "file:///cookieless_proxy/";

	public static final String SCHEME_HTTP = "http://";
	public static final String SCHEME_HTTPS = "https://";

	public static final String SCHEME_MMS = "mms://";
	public static final String SCHEME_FTP = "ftp://";
	public static final String SCHEME_RTSP = "rtsp://";

	public static final String SCHEME_FILE = "file:";

	public static final String SCHEME_EMAIL = "mailto:";
	public static final String SCHEME_JAVASCRIPT = "JavaScript:";

	public static final String SCHEME_ABOUT = "about:";
	public static final String SCHEME_DATA = "data:";
	public static final String SCHEME_CONTENT = "content:";

	/**
	 * 测试多线程（并发）时，访问static方法的问题，
	 * 只要没有访问全局变量，就不会有问题。
	 * 
	 * 如果多个线程一起访问testString()时，参数text是不会发生并发问题的，
	 * 如果static方法里面访问了全局变量test，就会有并发问题了。
	 * 
	 * @param string
	 */
	public static void testString(String string) {
		Log.e(TAG, "text111:"+string);
		text = string;

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Log.e(TAG, "text222:"+string);
		Log.e(TAG, "text ___ end:"+text);
	}

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}

	private static boolean startWithLowerCase(String text, String with) {
		if (isEmptyString(text)) {
			return false;
		}

		if (text.toLowerCase().startsWith(with)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断URL是否为File
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isFileURL(URL url) {
		if (url == null) {
			return false;
		}
		return isFileURL(url.toExternalForm());
	}

	/**
	 * 判断path是否为File
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isFileURL(String url) {
		return startWithLowerCase(url, SCHEME_FILE);
	}

	public static boolean isHttpURL(String url) {
		return startWithLowerCase(url, SCHEME_HTTP);
	}

	public static boolean isHttpsURL(String url) {
		return startWithLowerCase(url, SCHEME_HTTPS);
	}

	public static boolean isMmsURL(String url) {
		return startWithLowerCase(url, SCHEME_MMS);
	}

	public static boolean isRtspURL(String url) {
		return startWithLowerCase(url, SCHEME_RTSP);
	}

	public static boolean isFtpURL(String url) {
		return startWithLowerCase(url, SCHEME_FTP);
	}

	public static boolean isEmailURL(String url) {
		return startWithLowerCase(url, SCHEME_EMAIL);
	}

	public static boolean isJavaScriptURL(String url) {
		return startWithLowerCase(url, SCHEME_JAVASCRIPT);
	}

	public static boolean isAssetURL(String url) {
		return startWithLowerCase(url, ASSET_BASE);
	}

	public static boolean isResourceURL(String url) {
		return startWithLowerCase(url, RESOURCE_BASE);
	}

	public static boolean isCookielessProxyURL(String url) {
		return startWithLowerCase(url, PROXY_BASE);
	}

	public static boolean isAboutURL(String url) {
		return startWithLowerCase(url, SCHEME_ABOUT);
	}

	public static boolean isDataURL(String url) {
		return startWithLowerCase(url, SCHEME_DATA);
	}

	public static boolean isContentURL(String url) {
		return startWithLowerCase(url, SCHEME_CONTENT);
	}
	public static boolean isNetworkURL(String url) {
		if (isEmptyString(url)) {
			return false;
		}
		return (isFtpURL(url) ||
				isHttpURL(url) ||
				isHttpsURL(url) ||
				isMmsURL(url) ||
				isRtspURL(url));
	}

	/**
	 * 判断URL是否有效，这个比SDK要好
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isValidURL(String url) {
		if(isEmptyString(url)) {
			return false;
		}

		boolean valid = false;

		try {
			new URL(url);
			valid = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.i(TAG, "isValidURL:"+valid);
		return valid;
	}

	/**
	 * 判断文件是否有效，主要是判断文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isValidFile(String path) {
		if (isEmptyString(path)) {
			return false;
		}

		// 判断文件是否存在
		File file = new File(path);
		if (!file.exists()) {
			return false;
		}

		return true;
	}

	/**
	 * 判断文件是否有效
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isValidFile(File file) {
		if (file == null) {
			return false;
		}

		// 判断文件是否存在
		if (!file.exists()) {
			return false;
		}

		return true;
	}

	/**
	 * 判断字符串长度是否在范围内
	 * 
	 * 实例：
	 * Log.e(TAG, "isLengthRange:"+VerifyUtil.isLengthRange(null, 0, 0)); // == true
		Log.e(TAG, "isLengthRange:"+VerifyUtil.isLengthRange("", 0, 0)); // == true
		Log.e(TAG, "isLengthRange:"+VerifyUtil.isLengthRange("cc", 0, 6)); // == true
		Log.e(TAG, "isLengthRange:"+VerifyUtil.isLengthRange("ccsssssss", 1, 5)); // == false
		Log.e(TAG, "isLengthRange:"+VerifyUtil.isLengthRange("ccsssssss", 1, 56)); // == true
		Log.e(TAG, "isLengthRange:"+VerifyUtil.isLengthRange("12346", 5, 5)); // true

	 * @param src
	 * @param minLen
	 * @param maxLen
	 * @return
	 */
	public static boolean isLengthRange(String src, int minLen, int maxLen) {

		//		Log.e(TAG, "isLengthRange():src:"+src+",minLen:"+minLen+",maxLen:"+maxLen);

		if (minLen < 0) {
			Log.exception(TAG, new IllegalArgumentException("minLen < 0"));
			return false;
		}

		if (maxLen < 0) {
			Log.exception(TAG, new IllegalArgumentException("maxLen < 0"));
			return false;
		}

		int srcLen = 0;

		if (isEmptyString(src)) {
			srcLen = 0;
		}else {
			srcLen = src.length();	
		}

		if ( srcLen >= minLen && srcLen <= maxLen ) {
			return true;
		}

		return false;
	}

	/**
	 *  判断是否为邮箱
	 * @param text
	 * @return
	 *@author aaa
	 */
	public static boolean isEmail(String text) {  
		if (isEmptyString(text)) {
			return false;
		}
		/** 
		 * EMail长度匹配：
		 * EMail地址由两个部分组成：
		 *  1、local part：为“@”前面的部分，最多64个字符
		 *  2、domain part：为“@”后面的部分，最多255个字符
		 *  于是，255+64+“@”一个=320个字符
		 */
		if(text.length()>320){
			return false;
		}

		/**正则表达式匹配邮箱
		 * 	//w{2,15}: 2~15个[a-zA-Z_0-9]字符；w{}内容是必选的。 如：dyh@152.com是合法的。
			//[a-z0-9]{2,}：至少两个[a-z0-9]字符,[]内的是必选的；如：dyh200896@1.com是不合法的。
			//[.]:'.'号时必选的；	如：dyh200896@163com是不合法的。
			//p{Lower}{2,}小写字母，两个以上。如：dyh200896@163.c是不合法的。
		 */
		String format = "\\w{2,15}[@][a-z0-9]{2,}[.]\\p{Lower}{2,}";
		if (text.matches(format))
		{ 
			return true;
		}
		return false;
	}

	/**
	 * 是否为电话号码（座机号码，如：400开头400-8099-888、400-838-5155，800开头）  
	 * (0755)5245852 匹配
	 * 0755-52485748p475 匹配
	 * @param text
	 * @return
	 * @author aaa
	 */
	public static boolean isPhoneNumber(String text) {
		if (isEmptyString(text)) {
			return false;
		}

		/**
		 * 长度匹配：
		 *中国国际区号0086， 从国外拨国内电话,比如拨北京的87654321,须加拨0086+北京区号
		 * 国内区号最长4位
		 * 国内电话号码最长8位
		 * 拨打分机号需要加p,如87654321p123,分机号一般长度3位以内(按4位计算)
		 * 400/800开头号码不分区号，长度10，加上"-"则12
		 * 则最长长度按座机号码+分机号算，（0752-87654321p123),18位
		 */
		if(text.length()>18){
			return false;
		}
		/**
		 * 正则匹配
		 * ((400|800)(\\-){0,1}\\d{3,4}(\\-){0,1}\\d{3,4})匹配400、800开头的号码：
		 * 如 4001112222/400-111-2222/400-1112-222
		 * (((0\\d{3}\\-)|(\\(0\\d{3}\\)))?\\d{7,}(\\w\\d{1,})?)匹配普通座机号码：
		 * 如075587654321/075587654321p123/0755-87654321/0755-87654321p123
		 * 
		 */
		String format = "^((400|800)(\\-){0,1}\\d{3,4}(\\-){0,1}\\d{3,4})|(((0\\d{3}\\-)|(\\(0\\d{3}\\)))?\\d{7,}(\\w\\d{1,})?)?";
		if(text.matches(format)){
			return true;
		}
		return isNumber(text);
	}

	/**
	 * 是否为移动电话号码，如：
	 * 13500000000
	 * +8613500000000等同008613500000000
	 * 8613500000000
	 * @param text
	 * @return
	 * @author luman
	 */
	public static boolean isMobilePhoneNumber(String number) { 
		if (isEmptyString(text)) {
			return false;
		}

		/**
		 * 长度匹配：
		 * 国际区域码最长6位，（中国 0086（等同+86)，夏威夷 001808）
		 * 手机号码11位
		 */
		if(number.length()>17){
			return false;
		}
		/**正则表达式匹配手机号码
		 * +{0,1}：匹配一次或0次 +
		 * (00){0,1}：匹配一次或0次 00
		 * (86)){0,1}：匹配一次或0次 86
		 * 1[0-9] ：1开头后面为数字
		 * {10}：取10位
		 */
		String format="^((\\+{0,1}(00){0,1}(86)){0,1})1[0-9]{10}";
		if(number.matches(format)){
			return true;
		}

		return isNumber(text);
	}

	/**
	 * 判断字符串是否由数字组成
	 * 
	 * @param text
	 * @return
	 * @author aaa
	 */
	public static boolean isNumber(String text) { 
		if (isEmptyString(text)) {
			return false;
		}
		//只能输入0-9中的数字
		String format="^[0-9]*$";
		if(text.matches(format)){
			return true;
		}

		return false;
	}
	/**
	 * 是否为账号规范：
	 * 6-16位，字母，或数字，或是字母和数字的组合
	 * @param text
	 * @return
	 * 2014-9-2 下午12:07:45
	 * @author MoSQ
	 */
	public static boolean isAccountStandard2(String text){
		/**
		 * 正则匹配
		 * \\w{6,16}匹配所有字母、数字、下划线 字符串长度6到16（不含空格）
		 */
		String format = "([0-9a-zA-Z\\u4E00-\\u9FA5]*+)";
		//可以包含中文
		if(text.matches(format)){
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param 注册的账号必须大于4位，并且可以为中文，数字，字母.
	 * @return
	 */
	public static boolean isAccountStandard3(String text){
		/**
		 * 正则匹配
		 * \\w{6,16}匹配所有字母、数字、下划线 字符串长度6到16（不含空格）
		 */
		String format = "([0-9a-zA-Z\\u4E00-\\u9FA5]{4,16}+)";
		//可以包含中文
		if(text.matches(format)){
			return true;
		}
		return false;
	}
	/**
	 * 是否为账号规范
	 * 
	 * 下划线：_
	 * 连接符：-
	 * 邮箱符号：@
	 * 实点：.
	 * 
	 * 邮件名长度范围是6-18
	 * 域名长度范围是不超过26个
	 * 
	 * 账号分类有：手机号、邮箱、中文、数字、字母和数字组合
	 * 
	 * 条件如下:
	 * 除了字母、数字、实点、中文、邮箱符号、下划线、连接符，不能含有其它字符。
	 * 长度是1-44
	 * 不考虑某个符号为开头的情况。
	 * 只能有一个@
	 * 
	 * @param text
	 * @return
	 * @author aaa
	 */
	public static boolean isAccountStandard(String text) {

		if(text==null){
			return false;
		}

		if(text.length()<1||text.length()>44){
			return false;
		}

		//手机号
		if(isMobilePhoneNumber(text)){
			return true;
		}
		//邮箱
		if(isEmail(text)){
			return true;
		}
        //座机号码
		if(isPhoneNumber(text)){
			return true;
		}
		/**
		 * 正则匹配：
		 * 除了字母、数字、实点、中文、邮箱符号、下划线、连接符，不能含有其它字符。
		 * 长度是1-44
		 * 不考虑某个符号为开头的情况。
		 * 只能有一个@
		 */
		String format = "(-?+\\w*+-?@?\\.?+\\w*){1,44}+";
		if(text.matches(format)){
			return true;
		}
		return false;
	}

	/**
	 * 是否为密码规范
	 * 
	 * 条件如下：
	 * 不能含有：中文、空格、单引号、双引号
	 * 长度6-18
	 * 
	 * @param text
	 * @return
	 * @author aaa
	 */
	public static boolean isPasswordStandard(String text) {

		//不能包含中文
		if(hasChinese(text)){
			return false;
		}

		/**
		 * 正则匹配
		 * \\w{6,18}匹配所有字母、数字、下划线 字符串长度6到18（不含空格）
		 */
		String format = "[a-zA-Z0-9]{6,12}";
		if(text.matches(format)){
			return true;
		}
		return false;
	}

	/**
	 * 中文识别
	 *@date 2014-2-14 下午3:19:29
	 *@author aaa
	 */
	public static boolean hasChinese(String source)  { 
		String reg_charset = "([\\u4E00-\\u9FA5]*+)"; 
		Pattern p = Pattern.compile(reg_charset); 
		Matcher m = p.matcher(source); 
		boolean hasChinese=false;
		while (m.find()) 
		{ 
			String str=m.group(1);
			if(str!=null&&!"".equals(str)){

				hasChinese=true;
			}

		} 
		return hasChinese;
	} 
}
