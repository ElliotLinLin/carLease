package com.hst.Carlease.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import com.hst.Carlease.R;
import com.tools.app.AbsUI;
/**
 * 引导页
 * @author HL
 *
 */
public class GuideUI extends AbsUI{

	private static final String TAG = GuideUI.class.getSimpleName();
	protected com.tools.app.TitleBar titleBar;
	ViewPager viewPager;

	LinearLayout linear_point_guide;//点点布局
	LinearLayout linear_guide_in;//引导页
	Button btn_guide_in;//立即体验
	Button btn_guide_pass;//跳过


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ui_guide);
		Log.i(TAG,"GuideUI=====");
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		viewPager = (ViewPager) findViewById(R.id.viewpager_guide);
		linear_point_guide = (LinearLayout) findViewById(R.id.linear_point_guide);
		linear_guide_in = (LinearLayout) findViewById(R.id.linear_guide_in);

		btn_guide_pass = (Button) findViewById(R.id.btn_guide_pass);

		btn_guide_in = (Button) findViewById(R.id.btn_guide_in);

		titleBar = new com.tools.app.TitleBar();
	}

	private void openUI(){
		
//		AbsUI.startUI(context, MainUI.class);
		// 关闭自身
		AbsUI.stopUI(GuideUI.this);
	}

	@Override
	protected void initControlEvent() {
		// 跳过
		btn_guide_pass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openUI();
			}
		});

		btn_guide_in.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openUI();
			}
		});

		//滚动页面事件
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				int size = linear_point_guide.getChildCount();
				setImageIcon(arg0,size);
				if (arg0 == size -1) {
					btn_guide_in.setVisibility(View.VISIBLE);
					btn_guide_pass.setVisibility(View.GONE);
				}else{
					btn_guide_in.setVisibility(View.GONE);
					btn_guide_pass.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}


	@Override
	protected void initMember() {
		
		super.setSlideFinishEnabled(false);//设置不可左右滑动退出
		super.setFullScreen();//设置全屏

		setViewPager();

		super.setSlideFinishEnabled(false);		// 设置滑动不关闭
	}

	/**
	 * 设置ViewPager
	 */
	protected void setViewPager(){
		ArrayList<View> imageList = new ArrayList<View>();
		imageList.add(addView(R.drawable.guide_one));
		imageList.add(addView(R.drawable.guide_two));
		imageList.add(addView(R.drawable.guide_three));
		imageList.add(addView(R.drawable.guide_four));

		MyAdapter adapter = new MyAdapter(imageList);
		viewPager.setAdapter(adapter);
	}

	/**
	 * 添加视图
	 * @param id
	 * @return
	 */
	private View addView(int id,int id2) {
		LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER_HORIZONTAL);


		ImageView image2 = new ImageView(this);
		image2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT,1));
		image2.setPadding(0, 0, 0, 10);
		image2.setScaleType(ScaleType.FIT_CENTER);
		image2.setImageResource(id2);
		layout.addView(image2);

		return layout;
	}

	/**
	 * 添加视图
	 * @param id
	 * @return
	 */
	private View addView(int id) {
		LinearLayout layout = new LinearLayout(this);
		layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER_HORIZONTAL);

		InputStream is = this.getResources().openRawResource(id);
		BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
		Bitmap btp = BitmapFactory.decodeStream(is, null, opt);

		ImageView image2 = new ImageView(this);
		image2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT,1));
		image2.setPadding(0, 0, 0, 0);
		image2.setScaleType(ScaleType.FIT_XY);
		image2.setImageBitmap(btp);
		layout.addView(image2);

		return layout;
	}

	/**
	 * 设置下面的圆点
	 * @param item 选择的项
	 */
	protected void setImageIcon(int item,int size) {
		ImageView imageView = null;

		if (item < 0 || size <= 0) {
			return;
		}

		for (int i = 0; i < size; i++) {
			imageView = (ImageView) linear_point_guide.getChildAt(i);
			if (i== item) {
				imageView.setImageResource(R.drawable.guide_point1);
			}else {
				imageView.setImageResource(R.drawable.guide_point2);
			}
		}
	}

	/**
	 * 自定义PagerAdapter
	 */
	public class MyAdapter extends PagerAdapter{
		List<View> list;
		public MyAdapter(List<View> list){
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = list.get(position);
			view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			container.addView(view);
			return view;
		}

	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onStartLoader() {


	}

	@Override
	protected byte[] doInBackgroundLoader() {

		return null;
	}

	@Override
	protected void onFinishedLoader(Loader<byte[]> loader, byte[] bytes) {


	}

}
