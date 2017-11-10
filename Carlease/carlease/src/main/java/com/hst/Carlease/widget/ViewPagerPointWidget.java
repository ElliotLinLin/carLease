package com.hst.Carlease.widget;



import com.hst.Carlease.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * 自定义容器，可根据引导页数量相应的生成对应数量的引导点，并且点点的背景可根据当前选中引导页相应的做切换
 * @author HL
 *
 */
public class ViewPagerPointWidget extends LinearLayout{
	
	
	private int pageCount = 1;//页面数
	private int currIndex = 0;//当前页
	private View.OnClickListener on_changePage = null;//点点点击的回调事件

	private Context context;
	
	//点点点击事件
	private View.OnClickListener image_click = new View.OnClickListener() {
		public void onClick(View v) {
			int tag = Integer.valueOf(v.getTag().toString());
			setCurrChoose(tag);
			if (on_changePage != null) {
				//点点点击回调切换页面
				on_changePage.onClick(v);
			}
		}
	};

	public ViewPagerPointWidget(Context context) {
		super(context);
		this.context=context;
		
	}
	public ViewPagerPointWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}
	/**
	 * 返回多少页
	 */
	public int getCount(){
		return pageCount;
	}
	/**
	 * 点点点击页面切换事件
	 * @param on_changePage
	 */
	public void setChangePage(View.OnClickListener on_changePage){
		this.on_changePage=on_changePage;
	}
	
	/**
	 * 根据页面数设置相应数量的引导点
	 * @param number
	 */
	public void setPointNumber(int number){
		pageCount=number;
		
		this.removeAllViews();//设置数量时先清空容器
		
		//创建相应数量的ImageView显示点点图标
		for (int i = 0; i < pageCount; i++) {
			ImageView imageView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			//点点之间的距离
			params.setMargins(10, 10, 10, 10);
			imageView.setLayoutParams(params);
			//点点点击事件
			imageView.setOnClickListener(image_click);
			//为每个点点设置一个标识，方便在点击事件中获取的该标识
			imageView.setTag(i);
			this.addView(imageView);
		}
		//设置当前选中的点点，默认为第一个点点
		setCurrChoose(currIndex);
	}
	/*
	 * 获取当前处于选中状态的点点下标
	 */
	public int getCurrChoose() {
		return currIndex;
	}
	/**
	 * 根据当前选中标识切换点点的背景
	 * @param index
	 */
	public void setCurrChoose(int index) {
		if(index>=pageCount){
			return;
		}
		currIndex = index;
		for (int i = 0; i < pageCount; i++) {
			ImageView imageView = (ImageView) ViewPagerPointWidget.this.getChildAt(i);
			if (i == currIndex) {
				imageView.setImageResource(R.drawable.guide_point1);//选中状态
			} else {
				imageView.setImageResource(R.drawable.guide_point2);//未选中状态
			}
		}
	}

}

