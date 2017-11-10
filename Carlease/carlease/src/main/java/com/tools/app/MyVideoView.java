package com.tools.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * 自定义的VideoView类，为了全屏显示而重写的   定义的 接口为了监听Videoview的开始和暂停
 * @author MoSQ
 *2014-2-25下午3:15:28
 */
public class MyVideoView extends VideoView {

	VideoViewListener listener;
	
	public MyVideoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		setMeasuredDimension(width , height);
	}
	

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		//super.pause();这一句不能去掉，否则放不出视频
		super.pause();
		if (listener != null) {
			listener.onPause();
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		//super.start();这一句不能去掉，否则放不出视频
		super.start();
		if (listener != null) {
			listener.onPlay();
		}
	}
	
	@Override
	public void stopPlayback() {
		// TODO Auto-generated method stub
		super.stopPlayback();
		if (listener != null) {
			listener.onStop();
		}
	}

	/**
	 * 自定义的接口，用于监听
	 * @author MoSQ
	 *2014-2-26上午8:52:22
	 */
	public interface VideoViewListener{
		void onPlay();
		void onPause();
		void onStop();
	}

	public void setVideoViewListener(VideoViewListener listener){
		this.listener = listener;
	}
	
}
