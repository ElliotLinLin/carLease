package com.tools.widget.timepicker;

import java.text.DecimalFormat;
import java.util.Calendar;

import com.hst.Carlease.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class TimeSelecter {
	private Dialog dialog;
	private Context context;
	private TimeChangeCallBack timeChangeCallBack;
	private String time;
	private int hour;
	private int minute;

	public interface TimeChangeCallBack {
		void onChangeTime(String time);
	}

	/**
	 * 
	 * @param context
	 *            该上下文为当前Activity的上下文，不能是应用的上下文
	 *            如果是Fragment获取上下文方式为this.getActivity()
	 *            在本重用库中直接填ui，因为在AbsFgm2中已经得到上下文赋值给ui
	 */
	public TimeSelecter(Context context, TimeChangeCallBack callback, String lastTime) {
		super();
		this.context = context;
		/**
		 * @Description: TODO 弹出日期时间选择器
		 */
		timeChangeCallBack = callback;
		Calendar calendar = Calendar.getInstance();
		if (!lastTime.equals("")) {
			if(lastTime.contains(":")){
				String arrayTime[] = lastTime.split("[:]");
				hour = Integer.parseInt(arrayTime[0]);
				minute = Integer.parseInt(arrayTime[1]);
			}else {
				hour = calendar.get(Calendar.HOUR_OF_DAY);
				minute = calendar.get(Calendar.MINUTE);
			}
		} else {
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			minute = calendar.get(Calendar.MINUTE);
		}
		
		
		
		dialog = new Dialog(context,
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.setContentView(R.layout.tools_dialog_time_layout);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.alpha = 0.97f;
		window.setWindowAnimations(R.style.dialogWindowAnim); // 设置窗口弹出动画
		window.setAttributes(lp);

		// 时
		final WheelView wv_hours = (WheelView) dialog.findViewById(R.id.hour);
		wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
		wv_hours.setCyclic(true);
		wv_hours.setCurrentItem(hour);

		// 分
		final WheelView wv_mins = (WheelView) dialog.findViewById(R.id.mins);
		wv_mins.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		wv_mins.setCyclic(true);
		wv_mins.setCurrentItem(minute);

		// 根据屏幕密度来指定选择器字体的大小
		int textSize = 0;
		textSize = 17;

		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;

		Button btn_sure = (Button) dialog.findViewById(R.id.btn_datetime_sure);
		Button btn_cancel = (Button) dialog
				.findViewById(R.id.btn_datetime_cancel);
		// 确定
		btn_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 如果是个数,则显示为"02"的样式
				String parten = "00";
				DecimalFormat decimal = new DecimalFormat(parten);
				// 设置时间的显示
				time = decimal.format(wv_hours.getCurrentItem()) + ":"
						+ decimal.format(wv_mins.getCurrentItem());
				timeChangeCallBack.onChangeTime(time);
				dialog.dismiss();
			}
		});
		// 取消
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		// 设置dialog的布局,并显示
		// dialog.setContentView(dialog);
		dialog.show();
	}

}
