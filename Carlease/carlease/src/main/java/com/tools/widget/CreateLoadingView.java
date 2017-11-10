package com.tools.widget;

import com.hst.Carlease.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




public class CreateLoadingView {
	public static LinearLayout createView(Context context, String showText) {
		LayoutInflater lyoutInflater = LayoutInflater.from(context);
		if (lyoutInflater == null) {
			return null;
		}
		View view = lyoutInflater.inflate(R.layout.tools_loading_dialog, null);
		if (view == null) {
			return null;
		}
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.dialog_view);
		ImageView imageView = (ImageView) view.findViewById(R.id.img);
		TextView tipTextView = (TextView) view.findViewById(R.id.tipTextView);
		tipTextView.setText(showText);
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.tools_loading);
		if (animation == null) {
			return null;
		}
		imageView.startAnimation(animation);
		return layout;
	}
}
