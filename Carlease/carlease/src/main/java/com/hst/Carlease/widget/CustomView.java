package com.hst.Carlease.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义刻度表（不够完善，慢慢优化）
 * 
 * @author wzy
 * 
 */
public class CustomView extends View {

	private Paint mPaint;

	float width;
	float height;
	
	private boolean isPause=false;//暂停

	private RectF mBounds;
	private float radius;// 半径
	private float smallLength;// 短线长度
	private float largeLength;// 长线长度

	private int redline = -1;// 要变为红色的线的范围

	private int defaultLineColor = 0xff2dcb70;// 默认颜色
	private int afterColor = 0xffff0000;// 要变的颜色

	public CustomView(Context context) {
		super(context);
		init();
	}

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(2);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mBounds = new RectF(w, h, oldw, oldh);// 这里需要设置一下 怎么获取控件的宽高
		width = mBounds.left - mBounds.right;
		height = mBounds.top - mBounds.bottom;

		if (width < height) {
			radius = width / 2 - 2;
		} else {
			radius = height / 2 - 2;
		}
		smallLength = radius / 15+2;
		largeLength = smallLength * 2;
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		super.onDraw(canvas);

		float start_x, start_y;
		float end_x, end_y;
		for (int i = 0; i < 60; ++i) {
			if (i > 6 && i <= 23) {
			} else {
				if (redline <= 6 && redline >= 0) {
					if (i >= 0 && i <= redline) {
						mPaint.setColor(afterColor);
					} else if (i > 23 && i <= 60) {
						mPaint.setColor(afterColor);
					} else {
						mPaint.setColor(defaultLineColor);
					}
				} else {
					if (i > 23 && i <= redline) {
						mPaint.setColor(afterColor);
					} else {
						mPaint.setColor(defaultLineColor);
					}
				}

				start_x = radius * (float) Math.cos(Math.PI / 180 * i * 6);
				start_y = radius * (float) Math.sin(Math.PI / 180 * i * 6);
				if (i % 3 == 0) {
					end_x = start_x - largeLength * (float) Math.cos(Math.PI / 180 * i * 6);
					end_y = start_y - largeLength * (float) Math.sin(Math.PI / 180 * i * 6);
				} else {
					end_x = start_x - smallLength * (float) Math.cos(Math.PI / 180 * i * 6);
					end_y = start_y - smallLength * (float) Math.sin(Math.PI / 180 * i * 6);
				}
				start_x += mBounds.centerX();
				end_x += mBounds.centerX();
				start_y += mBounds.centerY();
				end_y += mBounds.centerY();
				canvas.drawLine(start_x, start_y, end_x, end_y, mPaint);
			}
		}

	}

	/**
	 * 变红的方法(想变成其他颜色 更改afterColor值)
	 */
	public void setbian() {
		redline = 23;
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (isPause) {
					return;
				}
				defaultLineColor=0xff2dcb70;
				afterColor=0xffff0000;
				// 要做的事情
				if (redline == 6) {
					afterColor = defaultLineColor;
					redline = 23;
					invalidate();// 重绘
					afterColor = 0xffff0000;// 要变的颜色
				} else {
					redline++;
					if (redline == 60) {
						redline = 0;
					}
					invalidate();// 重绘
				}

				handler.postDelayed(this, 30);
			}
		};
		handler.postDelayed(runnable, 10);

	}

	/**
	 * 设置默认颜色
	 * 
	 * @param color
	 */
	public void setLineColor(int color) {
		defaultLineColor = color;
		afterColor=color;
		invalidate();// 重绘
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getSmallLength() {
		return smallLength;
	}

	public void setSmallLength(float smallLength) {
		this.smallLength = smallLength;
	}

	public float getLargeLength() {
		return largeLength;
	}

	public void setLargeLength(float largeLength) {
		this.largeLength = largeLength;
	}

	public int getAfterColor() {
		return afterColor;
	}

	public void setAfterColor(int afterColor) {
		this.afterColor = afterColor;
	}
	
	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}
}
