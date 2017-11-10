package com.hst.Carlease.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.hst.Carlease.constants.Constants;
import com.hst.Carlease.task.LoginTask;
import com.hst.Carlease.ui.LoginUI;
import com.tools.app.AbsUI;
import com.tools.net.NetworkState;
import com.tools.widget.Prompt;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;

public class StringUtils {
	// ======================判断手机号吗的合法性
	/**
	 * 大陆号码或香港号码均可
	 */
	public static boolean isPhoneLegal(String str)
			throws PatternSyntaxException {
		return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
	}

	/**
	 * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 此方法中前三位格式有： 13+任意数 15+除4的任意数 18+除1和4的任意数
	 * 17+除9的任意数 147
	 */
	public static boolean isChinaPhoneLegal(String str)
			throws PatternSyntaxException {
//		String regExp = "^((13[0-9])|(15[^4])|(18[0,1,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
//		String regExp = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(14[0-9]))\\d{8}$";
		String regExp = "^[1]\\d{10}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}
	/**
	 * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数 此方法中前三位格式有： 13+任意数 15+除4的任意数 18+除1和4的任意数
	 * 17+除9的任意数 147
	 */
	public static boolean isPrice(String str)
			throws PatternSyntaxException {
		String regExp = "^(0|[1-9][0-9]{0,9})(.[0-9]{1,2})?$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}
	/**
	 * 香港手机号码8位数，5|6|8|9开头+7位任意数
	 */
	public static boolean isHKPhoneLegal(String str)
			throws PatternSyntaxException {
		String regExp = "^(5|6|8|9)\\d{7}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static boolean isContainChinese(String str) {

		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	// ==========================判断身份证号码的合法性
	/**
	 * 验证身份证号码 身份证号码, 可以解析身份证号码的各个字段，以及验证身份证号码是否有效;
	 * 身份证号码构成：6位地址编码+8位生日+3位顺序码+1位校验码
	 * 
	 * 
	 */

	private String cardNumber; // 完整的身份证号码
	private Boolean cacheValidateResult = null; // 缓存身份证是否有效，因为验证有效性使用频繁且计算复杂
	private Date cacheBirthDate = null; // 缓存出生日期，因为出生日期使用频繁且计算复杂
	private final static String BIRTH_DATE_FORMAT = "yyyyMMdd"; // 身份证号码中的出生日期的格式
	private final static Date MINIMAL_BIRTH_DATE = new Date(-2209017600000L); // 身份证的最小出生日期,1900年1月1日
	private final static int NEW_CARD_NUMBER_LENGTH = 18;
	private final static int OLD_CARD_NUMBER_LENGTH = 15;
	private final static char[] VERIFY_CODE = { '1', '0', 'X', '9', '8', '7',
			'6', '5', '4', '3', '2' }; // 18位身份证中最后一位校验码
	private final static int[] VERIFY_CODE_WEIGHT = { 7, 9, 10, 5, 8, 4, 2, 1,
			6, 3, 7, 9, 10, 5, 8, 4, 2 };// 18位身份证中，各个数字的生成校验码时的权值

	public boolean validate() {
		if (null == cacheValidateResult) {
			boolean result = true;
			result = result && (null != cardNumber); // 身份证号不能为空
			result = result && NEW_CARD_NUMBER_LENGTH == cardNumber.length(); // 身份证号长度是18(新证)
			// 身份证号的前17位必须是阿拉伯数字
			for (int i = 0; result && i < NEW_CARD_NUMBER_LENGTH - 1; i++) {
				char ch = cardNumber.charAt(i);
				result = result && ch >= '0' && ch <= '9';
			}
			// 身份证号的第18位校验正确
			result = result
					&& (calculateVerifyCode(cardNumber) == cardNumber
							.charAt(NEW_CARD_NUMBER_LENGTH - 1));
			// 出生日期不能晚于当前时间，并且不能早于1900年
			try {
				Date birthDate = this.getBirthDate();
				result = result && null != birthDate;
				result = result && birthDate.before(new Date());
				result = result && birthDate.after(MINIMAL_BIRTH_DATE);
				/**
				 * 出生日期中的年、月、日必须正确,比如月份范围是[1,12],日期范围是[1,31]，还需要校验闰年、大月、小月的情况时，
				 * 月份和日期相符合
				 */
				String birthdayPart = this.getBirthDayPart();
				String realBirthdayPart = this.createBirthDateParser().format(
						birthDate);
				result = result && (birthdayPart.equals(realBirthdayPart));
			} catch (Exception e) {
				result = false;
			}
			cacheValidateResult = Boolean.valueOf(result);// TODO
			// 完整身份证号码的省市县区检验规则
		}
		return cacheValidateResult;
	}

	/**
	 * 如果是15位身份证号码，则自动转换为18位
	 * 
	 * @param cardNumber
	 * @return
	 * @return
	 */
	public void CheckIdCard(String cardNumber) {
		if (null != cardNumber) {
			cardNumber = cardNumber.trim();
			if (OLD_CARD_NUMBER_LENGTH == cardNumber.length()) {
				cardNumber = contertToNewCardNumber(cardNumber);
			}
		}
		this.cardNumber = cardNumber;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getAddressCode() {
		this.checkIfValid();
		return this.cardNumber.substring(0, 6);
	}

	public Date getBirthDate() {
		if (null == this.cacheBirthDate) {
			try {
				this.cacheBirthDate = this.createBirthDateParser().parse(
						this.getBirthDayPart());
			} catch (Exception e) {
				throw new RuntimeException("身份证的出生日期无效");
			}
		}
		return new Date(this.cacheBirthDate.getTime());
	}

	public boolean isMale() {
		return 1 == this.getGenderCode();
	}

	public boolean isFemal() {
		return false == this.isMale();
	}

	/**
	 * 获取身份证的第17位，奇数为男性，偶数为女性
	 * 
	 * @return
	 */
	private int getGenderCode() {
		this.checkIfValid();
		char genderCode = this.cardNumber.charAt(NEW_CARD_NUMBER_LENGTH - 2);
		return (((int) (genderCode - '0')) & 0x1);
	}

	private String getBirthDayPart() {
		return this.cardNumber.substring(6, 14);
	}

	private SimpleDateFormat createBirthDateParser() {
		return new SimpleDateFormat(BIRTH_DATE_FORMAT);
	}

	private void checkIfValid() {
		if (false == this.validate()) {
			throw new RuntimeException("身份证号码不正确！");
		}
	}

	/**
	 * 校验码（第十八位数）：
	 * 
	 * 十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0...16 ，先对前17位数字的权求和；
	 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
	 * 2; 计算模 Y = mod(S, 11)< 通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9
	 * 8 7 6 5 4 3 2
	 * 
	 * @param cardNumber
	 * @return
	 */
	private static char calculateVerifyCode(CharSequence cardNumber) {
		int sum = 0;
		for (int i = 0; i < NEW_CARD_NUMBER_LENGTH - 1; i++) {
			char ch = cardNumber.charAt(i);
			sum += ((int) (ch - '0')) * VERIFY_CODE_WEIGHT[i];
		}
		return VERIFY_CODE[sum % 11];
	}

	public static boolean isCard(String str) {
		String regExp = "^(\\d{14}|\\d{17})(\\d|[xX])$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 把15位身份证号码转换到18位身份证号码<br>
	 * 15位身份证号码与18位身份证号码的区别为：<br>
	 * 1、15位身份证号码中，"出生年份"字段是2位，转换时需要补入"19"，表示20世纪<br>
	 * 2、15位身份证无最后一位校验码。18位身份证中，校验码根据根据前17位生成
	 * 
	 * @param cardNumber
	 * @return
	 */
	private String contertToNewCardNumber(String oldCardNumber) {
		StringBuilder buf = new StringBuilder(NEW_CARD_NUMBER_LENGTH);
		buf.append(oldCardNumber.substring(0, 6));
		buf.append("19");
		buf.append(oldCardNumber.substring(6));
		buf.append(StringUtils.calculateVerifyCode(buf));
		return buf.toString();
	}

	/**
	 * 获取手机imei
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		// 获取手机IMEI
		TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei;
	}

	// 处理请求
	public static void IsOUTOFtime(Context context,FragmentActivity ui) {
		// 判断是否记住密码
		String pwd = SPUtils.get(context, Constants.Pwd, "").toString();
		String username = SPUtils.get(context, Constants.UserName, "")
				.toString();
		NetworkState state = new NetworkState(context);
		if (state.isConnected() == false) {
			Prompt.showWarning(context, "请检查您的网络");
			AbsUI.startUI(context, LoginUI.class);
			return;
		}
		LocationChangedUtils utils=new LocationChangedUtils(context, ui);
		if (SPUtils.get(context, Constants.ForgetCode, "false").toString()
				.equals("true")) {
			LoginTask task=new LoginTask(context);
			utils.startOnce();
			task.Login(context,username, pwd,SPUtils.get(context, Constants.DeviceNO, "888888")
					.toString(),utils.cityname,true);
		} else {
			AbsUI.startUI(context, LoginUI.class);
		}
	}
}
