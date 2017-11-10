package com.tools.widget;

import org.greenrobot.eventbus.EventBus;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hst.Carlease.R;
import com.hst.Carlease.eventBean.Stype;
import com.hst.Carlease.util.PopupWindowHelper;
import com.tools.util.Log;

public class PopZx implements OnClickListener{
	private Context context;
	private View rootView;
	private PopupWindowHelper helper;
	private View anchor;// 宿主
	private TextView agree;
	// 写倒计时
	private static final int MSG_COUNT_DOWN = 1; // 倒计时消息
	private static final int delayMillis = 1 * 1000; // 1秒钟发送一次消息
	private int time = 10; // 60秒内只能获取一次
	public PopZx(Context context, View anchor) {
		this.context = context;
		this.anchor = anchor;
		init();
	}

	private void init() {
		rootView = View.inflate(context, R.layout.pop_zx, null);
		helper = new PopupWindowHelper(rootView);
		agree=(TextView) rootView.findViewById(R.id.agree);
		agree.setMovementMethod(ScrollingMovementMethod.getInstance());
		mHandler.sendEmptyMessage(MSG_COUNT_DOWN);
	}
	
	public void show() {
		if (helper != null) {
			helper.showFillParent(anchor);
		}
	}

	public void close() {
		if (helper != null) {
			helper.dismiss();
		}
	}
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			// 倒计时
			case MSG_COUNT_DOWN:
				Log.i("PopZx", "time----"+time);
				if (time>1) {
					// 60秒内倒计时
					Message msg2 = Message.obtain();
					msg2.arg1 = time--;
					msg2.what = MSG_COUNT_DOWN;
					agree.setText("同意("+time+"S)");
					agree.setTextColor(Color.parseColor("#999999"));
					mHandler.sendMessageDelayed(msg2, delayMillis);
				} else {
					time=10;
					agree.setText("同意");
					agree.setTextColor(Color.parseColor("#333333"));
					agree.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							EventBus.getDefault().post(new Stype(1));
							close();
						}
					});
				}
				break;

			default:
				break;
			}

		}

	};
	@Override
	public void onClick(View v) {
		
	}
}
