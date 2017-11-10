package com.hst.Carlease.util;

import com.hst.Carlease.R;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * @author Ljj
 *         描述：PopupWindow帮助工具 
 *         </br>逻辑/用途：快速生成一个popupwindow，并进行相应的显示
 *         </br>使用步骤:
 *         </br>1.生成view View.infalte
 *         </br>2.helper=new PopupWindowHelper(view)
 *         </br>3.你要显示在哪里:调用helper.show....
 */
public class PopupWindowHelper {

	private View popupView;
	private PopupWindow mPopupWindow;
	private boolean isCancelable;
	private static final int TYPE_WRAP_CONTENT = 0, TYPE_MATCH_PARENT = 1, TYPE_MATCH_ACTIVITY = 2;

	public PopupWindowHelper(View view) {
		popupView = view;
	}

	/**
	 * 显示在某个控件下方,不定位
	 * 
	 * @param anchor
	 * @param type
	 *            0-wrap;1-match
	 */
	public void showAsDropDown(View anchor, int type) {
		initPopupWindow(type);
		mPopupWindow.showAsDropDown(anchor);
	}

	/**
	 * 显示在某个控件下方,定位点(左上方的点)
	 * 
	 * @param anchor
	 * @param xoff
	 * @param yoff
	 */
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		initPopupWindow(TYPE_WRAP_CONTENT);
		mPopupWindow.showAsDropDown(anchor, xoff, yoff);
	}

	/**
	 * @param parent
	 * @param gravity
	 * @param x
	 * @param y
	 */
	public void showAtLocation(View parent, int gravity, int x, int y) {
		initPopupWindow(TYPE_WRAP_CONTENT);
		mPopupWindow.showAtLocation(parent, gravity, x, y);
	}

	public void dismiss() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	public boolean isShowing() {
		if (mPopupWindow == null) {
			return false;
		}
		return mPopupWindow.isShowing();
	}

	/**
	 * 显示在某个控件上方
	 * 
	 * @param anchor
	 */
	public void showAsPopUp(View anchor) {
		showAsPopUp(anchor, 0, 0);
	}

	public void showAsPopUp(View anchor, int xoff, int yoff) {
		initPopupWindow(TYPE_WRAP_CONTENT);
		mPopupWindow.setAnimationStyle(R.style.AnimationUpPopup);
		popupView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		int height = popupView.getMeasuredHeight();
		int[] location = new int[2];
		anchor.getLocationInWindow(location);
		mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, location[0] + xoff, location[1] - height + yoff);
	}

	/**
	 * 从窗体底部出现
	 * 
	 * @param anchor
	 */
	public void showFromBottom(View anchor) {
		initPopupWindow(TYPE_MATCH_PARENT);
		mPopupWindow.setAnimationStyle(R.style.AnimationFromButtom);
		mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.BOTTOM, 0, 0);
	}

	/**
	 * 从窗体顶部出现
	 * 
	 * @param anchor
	 */
	public void showFromTop(View anchor) {
		initPopupWindow(TYPE_MATCH_PARENT);
		mPopupWindow.setAnimationStyle(R.style.AnimationFromTop);
		mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, 0, getStatusBarHeight());
	}

	/**
	 * 像dialog那样，弹出来，设置布局的背景为半透明就ok
	 * @param anchor
	 */
	public void showFillParent(View anchor) {
		initPopupWindow(TYPE_MATCH_ACTIVITY);
		mPopupWindow.setAnimationStyle(R.style.AnimationFromBack);
		mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, 0, 0);
	}
	/**
	 * 像dialog那样，弹出来，设置布局的背景为半透明就ok
	 * @param anchor
	 */
	public void showFillParent(View anchor,int animResId) {
		initPopupWindow(TYPE_MATCH_ACTIVITY);
		mPopupWindow.setAnimationStyle(animResId);
		mPopupWindow.showAtLocation(anchor, Gravity.LEFT | Gravity.TOP, 0, 0);
	}

	/**
	 * 设置点击是否可取消
	 */
	public void setCancelable(boolean isCancelable) {
		this.isCancelable = isCancelable;
	}

	/**
	 * @param type
	 *            </br> 0-WRAP_CONTENT</br> 1-MATCH_PARENT
	 */
	private void initPopupWindow(int type) {
		if(mPopupWindow!=null){
			return;
		}
		if (type == TYPE_WRAP_CONTENT) {
			mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		} else if (type == TYPE_MATCH_PARENT) {
			mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		} else if (type == TYPE_MATCH_ACTIVITY) {
			mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		}

		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mPopupWindow.setFocusable(isCancelable);
		mPopupWindow.setOutsideTouchable(isCancelable);
	}

	private int getStatusBarHeight() {
		return Math.round(25 * Resources.getSystem().getDisplayMetrics().density);
	}

	public void setFocusable(boolean isCancelable) {
		this.isCancelable = isCancelable;
		if (mPopupWindow != null) {
			mPopupWindow.setFocusable(isCancelable);
		}
	}

}
