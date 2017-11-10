package com.tools.widget;




import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;


/**

showAsDropDown(View anchor)：相对某个控件的位置（正左下方），无偏移
showAsDropDown(View anchor, int xoff, int yoff)：相对某个控件的位置，有偏移
showAtLocation(View parent, int gravity, int x, int y)：相对于父控件的位置（例如正中央Gravity.CENTER，下方Gravity.BOTTOM等），可以设置偏移或无偏移

相对某个控件的位置（正左下方），无偏移 
popupWindow.showAsDropDown(v);

相对某个控件的位置（正左下方），有偏移 
popupWindow.showAsDropDown(v, 50, 50);// X、Y方向各偏移50

相对于父控件的位置，无偏移
popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

相对于父控件的位置，有偏移
popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 50);


使用例子：

	private PopupWindowExt popupWindow;

	if (popupWindow == null) {
			initPopupWindow();
	}

	 // 创建PopupWindow
	private void initPopupWindow() {
		LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = mInflater.inflate(R.layout.ui_popupwindow_child, null);
		// 创建
		popupWindow = new PopupWindowExt(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		// 控件ID
		Button buttonA = (Button)popupView.findViewById(R.id.buttonA);
		Button buttonD = (Button)popupView.findViewById(R.id.buttonD);
		// 事件
		buttonA.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e(TAG, "buttonA");
			}
		});
		buttonD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e(TAG, "buttonD");
			}
		});
	}

		// 在控件的事件里使用
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.showAsDropDown(v);
			}
		});

 * @author lmc
 *
 */
public class PopupWindowExt extends PopupWindow {

	//	protected Context context;
	protected View rootView;

	//	public PopupWindowExt(Context context, int resource, int width, int height) {
	////		super(getRootView(context, resource), width, height);
	//		init(context, resource, width, height);
	//		super(rootView, width, height);
	////		super(rootView, width, height);
	//	}

	public PopupWindowExt(View contentView, int width, int height) {
		super(contentView, width, height);
		this.rootView = contentView;
		init();
	}

	//	protected void init(Context context, int resource, int width, int height) {
	//		LayoutInflater layoutInflater = LayoutInflater.from(context);
	//		rootView = layoutInflater.inflate(resource, null);
	//	}

	private void init() {
		// 点击外部区域消失
		setBackgroundDrawable(new BitmapDrawable());
		// 允许点击外部区域消失
		setOutsideTouchable(true);
		setTouchable(true);
		// 按返回键才有反应
		setFocusable(true);
	}

	public View getRootView() {
		return this.rootView;
	}
	
	public void close() {
		Log.i("mAddFrindsPopup", "mAddFrindsPopup   dismiss");
		this.dismiss();
	}

}