package com.tools.app;


import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.tools.util.Log;

/**
 * 标题栏
 * 
 * @author LMC
 */
public class TitleBar extends AbsFgm2 {

	private static final String TAG = TitleBar.class.getSimpleName();

	protected static final int leftCount = 1;
	protected static final int rightCount = 2;

	protected Button titleLeft1;
	protected Button titleLeft2;
	protected ProgressBar titleProgressBar;
	protected TextView titleText;
	protected TextView titleSmallText;
	protected Button titleRight2;
	protected Button titleRight1;
    protected RelativeLayout rl_tools_titlebar;
	protected LinearLayout titleLeftParent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "onCreateView()");
		return inflater.inflate(R.layout.tools_ui_titlebar, container, false);
	}

	@Override
	protected void initControl() {
		rl_tools_titlebar=(RelativeLayout)ui.findViewById(R.id.tools_titlebar);
		titleLeft1 = (Button)ui.findViewById(R.id.titleLeft1);
		titleLeft2 = (Button)ui.findViewById(R.id.titleLeft2);
		titleProgressBar = (ProgressBar)ui.findViewById(R.id.titleProgressBar);
		titleText = (TextView)ui.findViewById(R.id.titleText);
		titleSmallText = (TextView)ui.findViewById(R.id.titleSmallText);
		titleRight2 = (Button)ui.findViewById(R.id.titleRight2);
		titleRight1 = (Button)ui.findViewById(R.id.titleRight1);
		titleLeftParent = (LinearLayout)ui.findViewById(R.id.titleLeftParent);
	}

	@Override
	protected void initControlEvent() {

	}

	@Override
	protected void initMember() {
		this.init();
	}

	/**
	 * 初始化
	 */
	public void init() {
		Log.e(TAG, "init()");
		//		this.setTitle("");
		//		this.setTitleSmall("");
		super.setViewVisibilityGONE(titleRight1);
		super.setViewVisibilityGONE(titleRight2);
		// 圆形进度条
		super.setViewVisibilityGONE(titleProgressBar);
		// titleSmallText
		super.setViewVisibilityGONE(titleSmallText);
		// setBoldText
//		super.setBoldText(this.getTitleView(), true);
//		super.setBoldText(this.getTitleSmallView(), true);
	}

	/**
	 * 得到左边控件
	 * 
	 * @param index
	 * @return
	 */
	public Button getLeftView(int index) {
		if (index == 1) {
			return titleLeft1;
		}else if(index==2){
			return titleLeft2;
		}
		return null;
	}

	/**
	 * 设置圆形进度条的可见性
	 * 
	 * @param enable
	 */
	public void showProgressBar(boolean enable) {
		if (enable) {
			super.setViewVisibility(titleProgressBar, true);
		}else{
			super.setViewVisibilityGONE(titleProgressBar);
		}
	}

	/**
	 * 得到标题控件
	 * 
	 * @return
	 */
	public TextView getTitleView() {
		return titleText;
	}

	/**
	 * 得到小标题控件
	 * 
	 * @return
	 */
	public TextView getTitleSmallView() {
		return titleSmallText;
	}

	/**
	 * 设置标题内容
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		super.setViewVisibility(titleText, true);
		titleText.setText(text);
	}

	/**
	 * 设置小标题内容
	 * 
	 * @param text
	 */
	public void setTitleSmall(String text) {
		super.setViewVisibility(titleSmallText, true);
		titleSmallText.setText(text);
	}

	/**
	 * 得到右边控件
	 * 
	 * @param index
	 * @return
	 */
	public Button getRightView(int index) {
		if (index == 1) {
			return titleRight1;
		}else if (index == 2) {
			return titleRight2;
		}
		return null;
	}

	/**
	 * 得到左边多少个View
	 * 
	 * @return
	 */
	public int getLeftCount() {
		return leftCount;
	}

	/**
	 * 得到右边多少个View
	 * 
	 * @return
	 */
	public int getRightCount() {
		return rightCount;
	}

	/**设置背景
	 * @param id
	 */
	public void setBackground(int id){
		rl_tools_titlebar.setBackgroundResource(id);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.e(TAG, "onActivityCreated()");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "onDestroy()");
		super.onDestroy();
	}

	@Override
	protected void onStartLoader() {
		
		
	}

	@Override
	protected byte[] doInBackgroundLoader() {
		
		return null;
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {
		
		
	}

}