package com.tools.app;



import com.hst.Carlease.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 代替TitleBar.java
 * 
 * xml属性一定要有：
 * title="标题"
 * 
 * 不要小的圆形进度条
 * 不要小标题
 * 
 * 只有五个控件：左边二个Button，右边二个Button，中间一个TextView
 * 
 * 默认时，两边四个控件是隐藏的，中间控件是显示的。
 * 
 * 两边控件的背景图和中间控件的文字可以用xml方式实现，也可以用代码来设置。
 * 
 * 使用说明：
 * 包含五个属性：title background_left0 background_left1 background_right0 background_right1
 * 步骤：
 * 1 布局文件使用com.tools.app.TitleBar2 举例：
 *  <!-- 记得增加  xmlns:tools="http://schemas.android.com/apk/res/com.hst.hiringcar" -->
 *  <com.tools.app.TitleBar2
        android:id="@+id/titlebar2"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        tools:title="选择条件"
        tools:background_left0="@drawable/tools_btn_back_selector"
        />
   2 界面里调用
   TitleBar2 titlebar2 = (TitleBar2) findViewById(R.id.titlebar2);
   3 实现接口
   titlebar2.setOnClickListener(new TitleBar2.OnClickListener() {
			
			/
			  标题栏按钮点击事件
			  leftIndex= 0表示左边第一个按钮，leftIndex= 1表示左边第二个按钮；
			  rightIndex=0表示右边第一个按钮，rightIndex=1表示右边第二个按钮；
			 /
			@Override
			public void onClick(View v, int leftIndex, int rightIndex) {
				if (leftIndex == 0) {
					finish();
				}
			}
		});
 * 
 * @author LMC
 *
 */
public class TitleBar2 extends LinearLayout {
	public static final String TAG = TitleBar2.class.getSimpleName();
	private OnClickListener onClickListener = null;

	private Context context = null;

	private String title = "";

	private Button leftView0;
	private Button leftView1;

	private TextView middleView;

	private Button rightView0;
	private Button rightView1;
	
	//从布局获取的属性值
	private int left0;//1为显示；2为隐藏；默认隐藏；下同
	private int left1;
	private int right0;
	private int right1;
	
	private Drawable left0background;
	private Drawable left1background;
	private Drawable right0background;
	private Drawable right1background;

	public TitleBar2(Context context) {
		super(context);
		init(context, null);
	}

	public TitleBar2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.tools_ui_titlebar2, this,true);
		leftView0 = (Button) view.findViewById(R.id.titleLeft0);
		leftView1 = (Button) view.findViewById(R.id.titleLeft1);
		rightView0 = (Button) view.findViewById(R.id.titleRight0);
		rightView1 = (Button) view.findViewById(R.id.titleRight1);
		
		middleView = (TextView) view.findViewById(R.id.titleText);
		
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.tools_titlebar);
//			left0 = typedArray.getInt(R.styleable.tools_titlebar_left0Visibility, -1);
//			left1 = typedArray.getInt(R.styleable.tools_titlebar_left1Visibility, -1);
//			right0 = typedArray.getInt(R.styleable.tools_titlebar_right0Visibility, -1);
//			right1 = typedArray.getInt(R.styleable.tools_titlebar_right1Visibility, -1);
			
			left0background = typedArray.getDrawable(R.styleable.tools_titlebar_background_left0);
			left1background = typedArray.getDrawable(R.styleable.tools_titlebar_background_left1);
			right0background = typedArray.getDrawable(R.styleable.tools_titlebar_background_right0);
			right1background = typedArray.getDrawable(R.styleable.tools_titlebar_background_right1);
			
			title = typedArray.getString(R.styleable.tools_titlebar_title);
			
			typedArray.recycle();
			
			//设置标题
			if (title != null && middleView != null) {
				middleView.setText(title);
			}
			
			//设置控件的可见性
//			setViewVisible(leftView0,left0);
//			setViewVisible(leftView1,left1);
//			setViewVisible(rightView0,right0);
//			setViewVisible(rightView1,right1);
			//设置背景
			setBackground(leftView0,left0background);
			setBackground(leftView1,left1background);
			setBackground(rightView0,right0background);
			setBackground(rightView1,right1background);
			
			//点击事件
			setonclick(leftView0,0,-1);
			setonclick(leftView1,1,-1);
			setonclick(rightView0,-1,0);
			setonclick(rightView1,-1,1);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

	}
	
	/**
	 * 点击事件
	 * @param v 点击的View
	 * @param leftIndex 左边按钮下标 0为第一个，1为第二个；-1表示没有点击
	 * @param rightIndex 右边按钮下标  0为第一个，1为第二个；-1表示没有点击
	 * 2014-10-22 下午12:03:59
	 * @author MoSQ
	 */
	private void setonclick(View v,final int leftIndex, final int rightIndex){
		if (v == null) {
			return;
		}
		v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (onClickListener != null) {
					onClickListener.onClick(v, leftIndex, rightIndex);
				}
			}
		});
	}
	
	/**
	 * 设置控件的可见性
	 * @param view
	 * @param id 1为可见； 2为不可见
	 * 2014-10-21 下午5:30:02
	 * @author MoSQ
	 */
	private void setViewVisible(View view,int id){
		if (view == null) {
			return;
		}
		if (id == 1) {
			view.setVisibility(View.VISIBLE);
		}else {
			view.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 设置背景
	 * @param view 设置背景的View
	 * @param drawable 资源
	 * 2014-10-21 下午5:48:45
	 * @author MoSQ
	 */
	private void setBackground(View view,Drawable drawable){
		if (view == null) {
			return;
		}
		if (drawable == null) {
			return;
		}
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		view.setBackgroundDrawable(drawable);
	}

	/**
	 * 事件接口
	 * 
	 * @author LMC
	 *
	 */
	public interface OnClickListener {
		// leftIndex == 0 表示左边第一个
		// leftIndex == 1 表示左边第二个
		// rightIndex == 0 表示右边第一个
		// rightIndex == 1 表示右边第二个
		// v 表示点击的控件
		abstract void onClick(View v, int leftIndex, int rightIndex);
	}

	public void setOnClickListener(OnClickListener l) {
		this.onClickListener = l;
	}

	/**
	 * 获取左边控件
	 * 
	 * @param index
	 * @return
	 */
	public Button getLeftView(int index) {
		if (index == 0) {
			return leftView0;
		}else if (index == 1) {
			return leftView1;
		}
		return null;
	}

	/**
	 * 获取右边控件
	 * 
	 * @param index
	 * @return
	 */
	public Button getRighView(int index) {
		if (index == 0) {
			return rightView0;
		}else if (index == 1) {
			return rightView1;
		}
		return null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if (middleView != null) {
			middleView.setText(title);
		}
	}

	/**
	 * 通过资源来设置左边控件背景图
	 * 
	 * @param index 等于0时，表示左边第一个，等于1时，表示左边第二个
	 * @param resId
	 */
	public void setLeftBackgroundResource(int index, int resId) {
		if (index == 0) {
			if (leftView0 != null) {
				leftView0.setBackgroundResource(resId);
			}
		}else if (index == 1) {
			if (leftView1 != null) {
				leftView1.setBackgroundResource(resId);
			}
		}
	}

	/**
	 * 通过资源来设置右边控件背景图
	 * 
	 * @param index 等于0时，表示右边第一个，等于1时，表示右边第二个
	 * @param resId
	 */
	public void setRightBackgroundResource(int index, int resId) {
		if (index == 0) {
			if (rightView0 != null) {
				rightView0.setBackgroundResource(resId);
			}
		}else if (index == 1) {
			if (rightView1 != null) {
				rightView1.setBackgroundResource(resId);
			}
		}
	}

	/**
	 * 通过Color来设置左边控件背景图
	 * 
	 * @param index 等于0时，表示左边第一个，等于1时，表示左边第二个
	 * @param color
	 */
	public void setLeftBackgroundColor(int index, int color) {
		if (index == 0) {
			if (leftView0 != null) {
				leftView0.setBackgroundColor(color);
			}
		}else if (index == 1) {
			if (leftView1 != null) {
				leftView1.setBackgroundColor(color);
			}
		}
	}

	/**
	 * 通过Color来设置右边控件背景图
	 * 
	 * @param index 等于0时，表示右边第一个，等于1时，表示右边第二个
	 * @param color
	 */
	public void setRightBackgroundColor(int index, int color) {
		if (index == 0) {
			if (rightView0 != null) {
				rightView0.setBackgroundColor(color);
			}
		}else if (index == 1) {
			if (rightView1 != null) {
				rightView1.setBackgroundColor(color);
			}
		}
	}

	/**
	 * 通过Drawable来设置左边控件背景图
	 * 
	 * @param index 等于0时，表示左边第一个，等于1时，表示左边第二个
	 * @param d
	 */
	public void setLeftBackgroundDrawable(int index, Drawable d) {
		if (index == 0) {
			if (leftView0 != null) {
				leftView0.setBackgroundDrawable(d);
			}
		}else if (index == 1) {
			if (leftView1 != null) {
				leftView1.setBackgroundDrawable(d);
			}
		}
	}

	/**
	 * 通过Drawable来设置右边控件背景图
	 * 
	 * @param index 等于0时，表示右边第一个，等于1时，表示右边第二个
	 * @param d
	 */
	public void setRightBackgroundDrawable(int index, Drawable d) {
		if (index == 0) {
			if (rightView0 != null) {
				rightView0.setBackgroundDrawable(d);
			}
		}else if (index == 1) {
			if (rightView1 != null) {
				rightView1.setBackgroundDrawable(d);
			}
		}
	}
	


}
