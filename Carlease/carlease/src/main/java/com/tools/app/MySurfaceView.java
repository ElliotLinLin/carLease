package com.tools.app;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView {
	private static final String TAG = MySurfaceView.class.getSimpleName();
	Size mPreviewSize;
	Size getPreSize;
	List<Size> mSupportedPreviewSizes;
	Camera camera;
	onMeasureListener listener;
	boolean first = true;
	
	

	public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MySurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		Log.i(TAG, "--onMeasure--");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = resolveSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		setMeasuredDimension(width, height);
		
		if (camera == null) {
			camera = getCamera();
		}
		mSupportedPreviewSizes = camera.getParameters()
				.getSupportedPreviewSizes();
		requestLayout();

		if (mSupportedPreviewSizes != null) {
			Log.i(TAG, "--mSupportedPreviewSizes != null--");
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width,
					height);
			if (mPreviewSize != null) {
				getPreSize = mPreviewSize;
			}
			Log.i(TAG, "mPreviewSize.width-->"+mPreviewSize.width);
			Log.i(TAG, "mPreviewSize.height-->"+mPreviewSize.height);
			if (first && listener != null) {
				listener.getSize(mPreviewSize);
				first =false;
			}
		}
	
	}
	
	
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}
	
	
	public Camera getCamera(){
		if (camera == null) {
			try {
				
				camera = Camera.open();
			} catch (Exception e) {
				if (camera != null) {
					camera.release();
					camera = null;
				}
			}
		}
		return camera;
	}
	
	/**
	 * 此方法暂时不能调用,所以设置为private
	 * 这个方法一直调用在onMeasure之前，所以调用此方法一直获取为null
	 * @return
	 * Size
	 * 2014-3-20
	 * @author MoSQ
	 */
	private Size getPreviewSize(){
		Log.i(TAG, "---getPreviewSize--");
		return getPreSize;
	}
	
	/**
	 * 必须要实现这个接口，否则运行不了
	 * @author MoSQ
	 *2014-3-20下午5:43:56
	 */
	public interface onMeasureListener{
		/**
		 * 获得摄像头支持的视频录制的分辨率的size（宽和高）
		 * @param size
		 * void
		 * 2014-3-20
		 * @author MoSQ
		 */
		public void getSize(Size size);
	}
	
	public void setOnMeasureListener(onMeasureListener listener){
		this.listener = listener;
	}
}
