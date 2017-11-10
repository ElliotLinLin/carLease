package com.tools.widget;

import com.hst.Carlease.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * 用于公司列表右侧，字母索引列表
 * @author luman
 *
 */
public class LetterListView extends View
{

	//索引字母点击监听器
	OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	//索引字母列表
	String[] b = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    //字母选择位置
	int choose = -1;
	//字母列表描绘器
	Paint paint = new Paint();
	//是否显示选中的字母
	boolean showBkg = false;

	public LetterListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public LetterListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public LetterListView(Context context)
	{
		super(context);
	}

	/**
	 * 绘制字母列表
	 */
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (showBkg)
		{
			canvas.drawColor(getResources().getColor(R.color.letterlist_bg_color));
		}
       //View的总高度
		int height = getHeight();
		//View的宽度
		int width = getWidth();
		//每个字母的高度
		int singleHeight = height / b.length;
		for (int i = 0; i < b.length; i++)
		{
			paint.setColor(Color.WHITE);
			paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.companylist_text_size));
			paint.setTypeface(Typeface.MONOSPACE);
			paint.setAntiAlias(true);
			if (i == choose)
			{
				paint.setColor(getResources().getColor(R.color.letter_text_bg));
				paint.setFakeBoldText(true);
			}
			//字母的X坐标, View的中点偏左半个字母宽度
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			//字母的Y坐标，
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();
		}

	}

	/**
	 * 显示触摸字母，按下显示、移开隐藏
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		//触摸字母位置
		final int c = (int) (y / getHeight() * b.length);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			showBkg = true;
			if (oldChoose != c && listener != null)
			{
				if (c < b.length)
				{
					listener.onTouchingLetterChanged(b[c]);
					choose = c;
					invalidate();
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != c && listener != null)
			{
				if (c > 0 && c < b.length)
				{
					listener.onTouchingLetterChanged(b[c]);
					choose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			showBkg = false;
			choose = -1;
			invalidate();
			break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return super.onTouchEvent(event);
	}

	/**
	 * 设置触摸索引字母监听
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener)
	{
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 触摸索引字母监听接口
	 * @author luman
	 *
	 */
	public interface OnTouchingLetterChangedListener
	{
		public void onTouchingLetterChanged(String s);
	}

}
