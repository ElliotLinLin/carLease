package com.tools.widget;


import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.hst.Carlease.R;
import com.tools.util.Log;


/**
 * 几个方法
 * 一个是像toast一样显示,一个图片和文字，图片在上，文字在下
 * 另一个是显示圆形进度条，这个toast做不到，应该采用对话框的方式
 * 

 		对象的使用方法：
		Prompt prompt = new Prompt(context);
		// 设置位置
		prompt.setGravity( android.view.Gravity.CENTER );
		// 设置背景图片资源ID
		prompt.setBackgroundResource(R.drawable.prompt);
		// 设置图片资源ID
		prompt.setIcon(R.drawable.prompt_successed);
		// 设置文字大小
		prompt.setTextSize(20);
		// 设置文字字体
		prompt.setTextColor(Color.WHITE);
		// 设置持续时间
		prompt.setDuration(Toast.LENGTH_SHORT);
		// 设置文本
		prompt.setText("你好.");
		// 显示
		prompt.show();
		// 关闭上一个SHOW
		//		prompt.cancel();
 * 
 * 
 *	静态方法的使用例子：
 * 	Prompt.showSuccessful(context, "A");
 *  Prompt.showWarning(context, "B");
 *  Prompt.showError(context, "C");
 * 
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class Prompt {

	private static final String TAG = Prompt.class.getSimpleName();

	private Context context = null;

	private LayoutParams paramWrap = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	private TextView textView = null;
	private ImageView imageView = null;
	private LinearLayout linearShow = null;

	private int imageViewID = 0;

	private Toast toast = null;



	// 位置
	private int gravity = android.view.Gravity.CENTER;

	private int backgroundResourceID = R.drawable.tools_prompt;

	private int textSize = 20;
	private int textColor = Color.WHITE;
	private String showText = "";

	// private int duration = Toast.LENGTH_LONG;
	private int duration = Toast.LENGTH_SHORT;



	public Prompt(Context context) {
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {
		if (context == null) {
			throw new NullPointerException("context == null");
		}
		this.context = context;
		// 默认设置
		defaultSet();
	}

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 * 
	 * @param src
	 * @return
	 */
	private static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 默认设置
	 */
	private void defaultSet() {
		// 设置位置
		setGravity( android.view.Gravity.CENTER );
		// 设置背景图片资源ID
		setBackgroundResource(R.drawable.tools_prompt);
		// 设置图片资源ID
		setIcon(R.drawable.tools_prompt_successed);
		// 设置文字大小
		setTextSize(20);
		// 设置文字字体
		setTextColor(Color.WHITE);
		// 设置持续时间
		setDuration(Toast.LENGTH_SHORT);
		// 设置文本
		setText("");
	}

	/**
	 * 设置背景图片资源ID
	 * 
	 * mLinearShow.setBackgroundResource(R.drawable.toast);
	 * 
	 * @param resid
	 */
	public void setBackgroundResource(int resid) {
		this.backgroundResourceID = resid;
	}

	/**
	 * 设置图片资源ID
	 * 
	 * @param resId
	 */
	public void setIcon(int resId) {
		imageViewID = resId;
	}

	/**
	 * 设置文字大小
	 * 
	 * @param size
	 */
	public void setTextSize(int size) {
		textSize = size;
	}

	/**
	 * 设置文字色彩
	 * 
	 * @param color
	 */
	public void setTextColor(int color) {
		textColor = color;
	}

	/**
	 * 设置文本
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.showText = text;
	}

	/**
	 * 设置文本
	 * 
	 * @param resId
	 */
	public void setText(int resId) {
		String text = context.getString(resId);
		setText(text);
	}

	/**
	 * 设置持续时间
	 * 
	 * @param duration
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * 设置位置
	 * android.view.Gravity.CENTER
	 */
	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	/**
	 * 显示Toast
	 * 
	 * @param context
	 * @param resid
	 */
	public void show() {

		Log.d(TAG, "Prompt.show()");

		// 图片
		if (imageView == null) {
			imageView = new ImageView(context);
			imageView.setLayoutParams(paramWrap);
		}
		imageView.setBackgroundResource(imageViewID);

		// 文字
		if (textView == null) {
			textView = new TextView(context);
			paramWrap.gravity = this.gravity; // 居中
			textView.setLayoutParams(paramWrap);
		}
		textView.setTextSize(textSize);
		textView.setTextColor(textColor);
		textView.setText(this.showText);

		// 布局
		if (linearShow == null) {
			linearShow = new LinearLayout(context);
			linearShow.setLayoutParams(paramWrap);
			linearShow.setOrientation(LinearLayout.VERTICAL);
		}
		linearShow.setBackgroundResource(backgroundResourceID);

		// 添加
		if (linearShow.getChildCount() <= 0) {
			linearShow.addView(imageView);
			linearShow.addView(textView);
		}

		// 显示
		if (toast == null) {
			toast = new Toast(context);
			toast.setGravity(this.gravity, 0, 0); // 居中
			toast.setView(linearShow);
		}
		toast.setDuration(duration);

		// 显示
		toast.show();
	}

	/**
	 * 显示成功
	 * 
	 * @param text
	 */
	public void showSuccessful(String text) {
		// 设置图片资源ID
		setIcon(R.drawable.tools_prompt_successed);
		// 设置文本
		setText(text);
		// 显示
		show();
	}

	/**
	 * 显示警告
	 * 
	 * @param text
	 */
	public void showWarning(String text) {
		// 设置图片资源ID
		setIcon(R.drawable.tools_prompt_warning);
		// 设置文本
		setText(text);
		// 显示
		show();
	}

	/**
	 * 显示错误
	 * 
	 * @param text
	 */
	public void showError(String text) {
		// 设置图片资源ID
		setIcon(R.drawable.tools_prompt_error);
		// 设置文本
		setText(text);
		// 显示
		show();
	}

	/**
	 * 创建一个默认的Prompt
	 * 
	 * @return
	 */
	private static Prompt makeDefault(Context context) {
		if (context == null) {
			new NullPointerException("context == null").printStackTrace();
			return null;
		}

		return new Prompt(context);
	}

	/**
	 * 显示成功
	 * 
	 * @param text
	 */
	public static void showSuccessful(Context context, String text) {
		// 创建默认的
		Prompt prompt = makeDefault(context);
		if (prompt == null) {
			return;
		}
		// 显示成功
		prompt.showSuccessful(text);
	}

	/**
	 * 显示成功
	 * 
	 * @param text
	 */
	public static void showSuccessful(Context context, int resId) {
		showSuccessful(context, context.getString(resId));
	}

	/**
	 * 显示警告
	 * 
	 * @param text
	 */
	public static void showWarning(Context context, String text) {
		// 创建默认的
		Prompt prompt = makeDefault(context);
		if (prompt == null) {
			return;
		}
		// 显示警告
		prompt.showWarning(text);
	}

	/**
	 * 显示警告
	 * 
	 * @param text
	 */
	public static void showWarning(Context context, int resId) {
		showWarning(context, context.getString(resId));
	}

	/**
	 * 显示错误
	 * 
	 * @param text
	 */
	public static void showError(Context context, String text) {
		// 创建默认的
		Prompt prompt = makeDefault(context);
		if (prompt == null) {
			return;
		}
		// 显示错误
		prompt.showError(text);
	}

	/**
	 * 显示错误
	 * 
	 * @param text
	 */
	public static void showError(Context context, int resId) {
		showError(context, context.getString(resId));
	}

	/**
	 *  关闭Toast 
	 */
	public void cancel() {
		if (toast == null) {
			return;
		}
		toast.cancel();
	}

	/**
	 * 显示Toast
	 *
	 * @param context
	 * @param text
	 */
	public static void showToast(Context context, String text) {
		if (context == null) {
			new NullPointerException("context == null").printStackTrace();
			return;
		}

		if (isEmptyString(text)) {
			new NullPointerException("text == null").printStackTrace();
			return;
		}

		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示Toast
	 * 
	 * @param context
	 * @param resId 字符串资源ID
	 */
	public static void showToast(Context context, int resId) {
		if (context == null) {
			new NullPointerException("context == null").printStackTrace();
			return;
		}

		showToast(context, context.getString(resId));
	}

}
