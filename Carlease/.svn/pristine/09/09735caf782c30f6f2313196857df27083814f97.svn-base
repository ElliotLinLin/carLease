package com.hst.Carlease.widget.mywidget;

import com.hst.Carlease.R;
import com.hst.Carlease.app.MainApplication;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastL {

	private static Toast mToast;
	private static TextView mTvMessage;
	
	public static void show(String text) {
		if (mToast == null) {
			mToast = new Toast(MainApplication.getmContext());
			View view = View.inflate(MainApplication.getmContext(), R.layout.toast_layout, null);
			mTvMessage = (TextView) view.findViewById(R.id.tv_message);
			mToast.setView(view);
		} 
		mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, -200);
		mTvMessage.setText(text);
		mToast.show();
	}
public static void MYToast(Context context,String text){
	mToast = Toast.makeText(context,
			text, Toast.LENGTH_LONG);
	mToast.setGravity(Gravity.CENTER, 0, 0);
	mToast.show();
}

}
