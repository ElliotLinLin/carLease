package com.tools.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * 原生的Spring 控件是无法更改字体和颜色的...
 * 
 * 第一法：adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
 * 
 * http://www.itivy.com/android/archive/2011/7/10/how-android-use-spinner-to-change-font-and-color.html
里面可以随意设置TextView的属性..比如字体...颜色等等.... 然后替换adapter.setDropDownViewResource

(android.R.layout.simple_spinner_dropdown_item);的xml...这样就能改变字体之类的属性了...


第二法：使用SpinnerAdapter
http://blog.csdn.net/liubing_2010/article/details/6973500

 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public class SpinnerExt extends Spinner {

	public SpinnerExt(Context context) {
		super(context);
		//
	}

	public SpinnerExt(Context context, AttributeSet attrs) {
		super(context, attrs);
		//
	}

	public SpinnerExt(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//
	}

	public void setTextColor() {

	}

	public void setTextSize() {

	}
}
