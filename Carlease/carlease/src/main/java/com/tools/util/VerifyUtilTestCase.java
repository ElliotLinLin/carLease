package com.tools.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import android.test.AndroidTestCase;


public class VerifyUtilTestCase extends AndroidTestCase {

	private static final String TAG = VerifyUtilTestCase.class.getCanonicalName();

	/*
	 * 初始化资源
	 */
	@Override
	protected void setUp() throws Exception {
		//		Log.e(TAG, "setUp");
		super.setUp();
	}

	@Override
	protected void runTest() throws Throwable {
		//		Log.e(TAG, "runTest");
		super.runTest();
	}

	/*
	 * 垃圾清理与资源回收
	 */
	@Override
	protected void tearDown() throws Exception {
		//		Log.e(TAG, "tearDown");
		super.tearDown();
	}

	public void testRun() {
		URI uri = null;
		try {
			uri = new URI("file:///sdcard/s.jpg");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Log.printURI(TAG, uri);

		URL url = null;
		try {
			url = new URL("file:///sdcard/s.jpg");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// toExternalForm:file:/sdcar/s.jpg

		//		URL uri = new URL("http://www.baidu.com:9090/aaaa/s.jpg");
		//		URI uri = new URI("http://www.baidu.com/s.jpg");

		// 当是http时,toExternalForm toString toURI 都一样 是 http://www.baidu.com:9090/aaaa/s.jpg

		Log.printURL(TAG, url);

		//		Log.e(TAG, "uri.toExternalForm():"+uri.toString());
		//		Log.e(TAG, "uri.toExternalForm():"+uri.toASCIIString());
		//		Log.e(TAG, "uri.toExternalForm():"+uri.toURL().toExternalForm());
		//		File file = new File(uri);
		//		Log.e(TAG, "getAbsolutePath:"+file.getAbsolutePath());
		//		Log.e(TAG, "getPath:"+file.getPath());

		Log.e(TAG, "VerifyUtil.isFileURL111:"+VerifyUtil.isFileURL(url));
		Log.e(TAG, "VerifyUtil.isFileURL2222:"+VerifyUtil.isFileURL(url.toExternalForm()));
	
		//账号验证
		Log.e(TAG,"VerifyUtil.isAccount:"+VerifyUtil.isAccountStandard(""));
		Log.e(TAG,"VerifyUtil.isAccount:我"+VerifyUtil.isAccountStandard("我"));
		Log.e(TAG,"VerifyUtil.isAccount:.我"+VerifyUtil.isAccountStandard(".我"));
		Log.e(TAG,"VerifyUtil.isAccount:我."+VerifyUtil.isAccountStandard("我."));
		Log.e(TAG,"VerifyUtil.isAccount:a我"+VerifyUtil.isAccountStandard("a我"));
		Log.e(TAG,"VerifyUtil.isAccount:1我"+VerifyUtil.isAccountStandard("1我"));
		Log.e(TAG,"VerifyUtil.isAccount:-我"+VerifyUtil.isAccountStandard("-我"));
		Log.e(TAG,"VerifyUtil.isAccount:_我"+VerifyUtil.isAccountStandard("_我"));
		Log.e(TAG,"VerifyUtil.isAccount:#我"+VerifyUtil.isAccountStandard("#我"));
		Log.e(TAG,"VerifyUtil.isAccount:%我"+VerifyUtil.isAccountStandard("%我"));
		Log.e(TAG,"VerifyUtil.isAccount:123"+VerifyUtil.isAccountStandard("123"));
		Log.e(TAG,"VerifyUtil.isAccount:'123@\""+VerifyUtil.isAccountStandard("'123@\""));
		Log.e(TAG,"VerifyUtil.isAccount:281580662@qq.com"+VerifyUtil.isAccountStandard("281580662@qq.com"));
		Log.e(TAG,"VerifyUtil.isAccount:281580662@@qq.com"+VerifyUtil.isAccountStandard("281580662@@qq.com"));
		Log.e(TAG,"VerifyUtil.isAccount:18318816007"+VerifyUtil.isAccountStandard("18318816007"));
		Log.e(TAG,"VerifyUtil.isAccount:1831881600"+VerifyUtil.isAccountStandard("1831881600"));
		Log.e(TAG,"VerifyUtil.isAccount:0752-6768683"+VerifyUtil.isAccountStandard("0752-6768683"));
		Log.e(TAG,"VerifyUtil.isAccount:075587654321"+VerifyUtil.isAccountStandard("075587654321"));
		Log.e(TAG,"VerifyUtil.isAccount:075587654321#"+VerifyUtil.isAccountStandard("075587654321#"));
		Log.e(TAG,"VerifyUtil.isAccount:075587654321$"+VerifyUtil.isAccountStandard("075587654321$"));
		Log.e(TAG,"VerifyUtil.isAccount:075587654321%"+VerifyUtil.isAccountStandard("075587654321%"));
		Log.e(TAG,"VerifyUtil.isAccount:075587654321&"+VerifyUtil.isAccountStandard("075587654321&"));
	
	   //密码验证
		Log.e(TAG,"VerifyUtil.isPasswordStandard:12345"+VerifyUtil.isPasswordStandard("12345"));
		Log.e(TAG,"VerifyUtil.isPasswordStandard:075587654321"+VerifyUtil.isPasswordStandard("075587654321"));
		Log.e(TAG,"VerifyUtil.isPasswordStandard:075587654321&"+VerifyUtil.isPasswordStandard("075587654321&"));
		Log.e(TAG,"VerifyUtil.isPasswordStandard:075587654321我"+VerifyUtil.isPasswordStandard("075587654321我"));
		Log.e(TAG,"VerifyUtil.isPasswordStandard:我075587654321"+VerifyUtil.isPasswordStandard("我075587654321"));
		Log.e(TAG,"VerifyUtil.isPasswordStandard:0755我87654321"+VerifyUtil.isPasswordStandard("0755我87654321"));
		Log.e(TAG,"VerifyUtil.isPasswordStandard:075587654321 "+VerifyUtil.isPasswordStandard("075587654321 "));
		Log.e(TAG,"VerifyUtil.isPasswordStandard:075587654321'"+VerifyUtil.isPasswordStandard("075587654321'"));
		Log.e(TAG,"VerifyUtil.isPasswordStandard:075587654321\""+VerifyUtil.isPasswordStandard("075587654321\""));
		Log.e(TAG,"VerifyUtil.isPasswordStandard:1234567891234567891"+VerifyUtil.isPasswordStandard("1234567891234567891"));
	}

}
