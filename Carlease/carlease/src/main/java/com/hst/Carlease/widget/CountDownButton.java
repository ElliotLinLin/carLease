package com.hst.Carlease.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Button;

import com.hst.Carlease.R;
import com.tools.net.NetworkState;

/*
 * 倒计时按钮
 */
public class CountDownButton extends Button {
	private static final int msg_count_down = 1;// 倒计时消息
	private int time = 0;
	private int saveTime = 0;
	String text = null;
	Drawable bgResource = null;

	public CountDownButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		text = this.getText().toString();
		bgResource = this.getBackground();
	}

	/**
	 * 设置倒计时，设置后，view不可点击
	 * 
	 * @param view
	 *            点击的View
	 * @param millisecond
	 *            倒计时多少毫秒 2014-12-31 下午3:26:58
	 * @author MoSQ
	 */
	public void setCountdown(int seconds) {

		if (seconds <= 0) {
			return;
		}

		this.time = seconds;
		this.saveTime = time;
		CountDownButton.this.setBackgroundResource(R.drawable.get_code);

		// 发送倒计时消息
		handler.sendEmptyMessage(msg_count_down);

		// 无网络，就停止倒计时
		NetworkState networkState = new NetworkState(getContext());
		boolean isconnected = networkState.isConnected();
		if (!isconnected) {
			stop();
		}

	}

	/**
	 * 停止倒计时 2015-2-9 下午5:31:30
	 * 
	 * @author MoSQ
	 */
	public void stop() {
		time = 0;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == msg_count_down) {

				if (time > 0) {

					// CountDownView不可点击
					CountDownButton.this.setClickable(false);
					// 点击状态
					CountDownButton.this.setPressed(true);

					// 更新CountDownView
					CountDownButton.this.setText(time + "秒");
					CountDownButton.this.invalidate();

					// 倒计时
					time--;

					// 一秒钟发送一次
					handler.sendEmptyMessageDelayed(msg_count_down, 1 * 1000);

				} else {
					// 时间还原
					time = saveTime;
					CountDownButton.this.setBackgroundDrawable(bgResource);
					// CountDownView还原为可点击
					CountDownButton.this.setClickable(true);
					// 点击还原
					CountDownButton.this.setPressed(false);
					// 还原原来的字样
					CountDownButton.this.setText(text);
					CountDownButton.this.invalidate();
				}

			}
		}

	};
}
