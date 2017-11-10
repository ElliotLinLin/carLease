package com.hst.Carlease.operate;

import android.content.Context;

import com.hst.Carlease.ram.HttpErrorCode;
import com.hst.Carlease.sqlite.bean.UserInforDb;
import com.tools.net.http.HttpConfig;
import com.tools.net.http.HttpTool;
import com.tools.os.Charset;
import com.tools.widget.Prompt;

public class HttpOperate {

	private static final String TAG = HttpOperate.class.getSimpleName();

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
	 * 处理HTTP自定义错误码
	 * 
	 * @param ui
	 * @param errorCode
	 * @param errorMessage
	 */
	public static void handleErrorCode(
			android.support.v4.app.FragmentActivity ui, String errorCode,
			String errorMessage) {

		if (ui == null) {
			return;
		}

		if (isEmptyString(errorCode)) {
			return;
		}

		if (isEmptyString(errorMessage)) {
			return;
		}

		Context context = ui.getApplicationContext();

		if (context == null) {
			return;
		}

		if (errorCode != null
				&& errorCode.equalsIgnoreCase(HttpErrorCode.success)) {
			// 成功
			return;
		} else {
			// 提示错误内容
			Prompt.showWarning(context, errorMessage);

			// 处理HTTP Session 超时
			if (HttpErrorCode.timeout.equalsIgnoreCase(errorCode)) {
				/**
				 * 处理HTTP SESSION超时
				 * 
				 * @param ui
				 */
				handleTimeout(ui);
			}

		}

	}

	/**
	 * 处理HTTP SESSION超时
	 * 
	 * @param ui
	 */
	public static void handleTimeout(android.support.v4.app.FragmentActivity ui) {
		Context context = ui.getApplicationContext();
		handleTimeout(context);
	}

	/**
	 * 处理HTTP SESSION超时
	 * 
	 * @param ui
	 */
	public static void handleTimeout(Context context) {

		if (context == null) {
			return;
		}

		String account = "";
		String password = "";

		UserInforDb accountInfo = AccountOperate.get();
		if (accountInfo != null) {
			account = accountInfo.getLoginName();
			password = accountInfo.getPassword();
		}

		// wzy修改 因新项目 UI都被清除
		// LoginUI.openUI(context, account, password, false, true);

	}

	/**
	 * 处理HTTP错误
	 * 
	 * @param ui
	 * @param error
	 * @param exception
	 * @param responseCode
	 * @param out
	 */
	public static void handleFailed(android.support.v4.app.FragmentActivity ui,
			HttpTool.Error error, Exception exception, int responseCode,
			byte[] out) {

		if (ui == null) {
			return;
		}

		Context context = ui.getApplicationContext();

		if (context == null) {
			return;
		}

		String errorText = null;

		if (error == HttpTool.Error.ResponseCodeError) {
			// 响应码错误
			errorText = Charset.bytes2String(out,
					new HttpConfig.Header().getCharset());
		} else {
			errorText = "HTTP失败，请重试。";
		}

		Prompt.showError(context, errorText);

	}

}
