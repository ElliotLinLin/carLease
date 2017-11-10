package com.tools.widget;



import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * View显示动画，支持淡入淡出，滑入画出，放大缩小,旋转效果
 * 使用说明：
 * fadeIn(View view, long durationMillis, long delayMillis)；
 * //淡入，view：视图，durationMillis动画过程时间，delayMillis延时再开始动画
 * @author aaa
 *
 *@date 2013-11-12 上午10:36:50
 */
public class AnimationController{
	public final int rela1 = Animation.RELATIVE_TO_SELF;
	public final int rela2 = Animation.RELATIVE_TO_PARENT;

	public final int Default = -1;
	public final int Linear = 0;
	public final int Accelerate = 1;
	public final int Decelerate = 2;
	public final int AccelerateDecelerate = 3;
	public final int Bounce = 4;
	public final int Overshoot = 5;
	public final int Anticipate = 6;
	public final int AnticipateOvershoot = 7;

	// LinearInterpolator,AccelerateInterpolator,DecelerateInterpolator,AccelerateDecelerateInterpolator,
	// BounceInterpolator,OvershootInterpolator,AnticipateInterpolator,AnticipateOvershootInterpolator

	public AnimationController()
	{
	}

	private class MyAnimationListener implements AnimationListener
	{
		private View view;

		public MyAnimationListener(View view)
		{
			this.view = view;
		}

		@Override
		public void onAnimationStart(Animation animation)
		{
			// this.view.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationEnd(Animation animation)
		{
			this.view.setVisibility(View.GONE);
		}

		@Override
		public void onAnimationRepeat(Animation animation)
		{
		}

	}

	private void setEffect(Animation animation, int interpolatorType, long durationMillis, long delayMillis)
	{
		switch (interpolatorType)
		{
		case 0:
			animation.setInterpolator(new LinearInterpolator());
			break;
		case 1:
			animation.setInterpolator(new AccelerateInterpolator());
			break;
		case 2:
			animation.setInterpolator(new DecelerateInterpolator());
			break;
		case 3:
			animation.setInterpolator(new AccelerateDecelerateInterpolator());
			break;
		case 4:
			animation.setInterpolator(new BounceInterpolator());
			break;
		case 5:
			animation.setInterpolator(new OvershootInterpolator());
			break;
		case 6:
			animation.setInterpolator(new AnticipateInterpolator());
			break;
		case 7:
			animation.setInterpolator(new AnticipateOvershootInterpolator());
			break;
		default:
			break;
		}
		animation.setDuration(durationMillis);
		animation.setStartOffset(delayMillis);
	}

	private void baseIn(View view, Animation animation, long durationMillis, long delayMillis)
	{
		setEffect(animation, Default, durationMillis, delayMillis);
		view.setVisibility(View.VISIBLE);
		view.startAnimation(animation);
	}

	private void baseOut(View view, Animation animation, long durationMillis, long delayMillis)
	{
		setEffect(animation, Default, durationMillis, delayMillis);
		animation.setAnimationListener(new MyAnimationListener(view));
		view.startAnimation(animation);
	}

	public void show(View view)
	{
		view.setVisibility(View.VISIBLE);
	}
	public void hide(View view)
	{
		view.setVisibility(View.GONE);
	}
	public void transparent(View view)
	{
		view.setVisibility(View.INVISIBLE);
	}

	/**淡入效果
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:39:24
	 *@author aaa
	 */
	public void fadeIn(View view, long durationMillis, long delayMillis){
		AlphaAnimation animation = new AlphaAnimation(0, 1);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**淡出效果
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:39:38
	 *@author aaa
	 */
	public void fadeOut(View view, long durationMillis, long delayMillis){
		AlphaAnimation animation = new AlphaAnimation(1, 0);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**滑入效果
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:39:55
	 *@author aaa
	 */
	public void slideIn(View view, long durationMillis, long delayMillis){
		TranslateAnimation animation = new TranslateAnimation(rela2, 1, rela2, 0, rela2, 0, rela2, 0);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**滑出效果
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:40:07
	 *@author aaa
	 */
	public void slideOut(View view, long durationMillis, long delayMillis){
		TranslateAnimation animation = new TranslateAnimation(rela2, 0, rela2, -1, rela2, 0, rela2, 0);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**放大进入效果
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:40:24
	 *@author aaa
	 */
	public void scaleIn(View view, long durationMillis, long delayMillis){
		ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, rela2, 0.5f, rela2, 0.5f);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**缩小退出效果
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:40:43
	 *@author aaa
	 */
	public void scaleOut(View view, long durationMillis, long delayMillis){
		ScaleAnimation animation = new ScaleAnimation(1, 0, 1, 0, rela2, 0.5f, rela2, 0.5f);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**旋转进入
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:43:58
	 *@author aaa
	 */
	public void rotateIn(View view, long durationMillis, long delayMillis){
		RotateAnimation animation = new RotateAnimation(-90, 0, rela1, 0, rela1, 1);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**旋转退出
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:43:47
	 *@author aaa
	 */
	public void rotateOut(View view, long durationMillis, long delayMillis){
		RotateAnimation animation = new RotateAnimation(0, 90, rela1, 0, rela1, 1);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**放大旋转进入
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:43:26
	 *@author aaa
	 */
	public void scaleRotateIn(View view, long durationMillis, long delayMillis){
		ScaleAnimation animation1 = new ScaleAnimation(0, 1, 0, 1, rela1, 0.5f, rela1, 0.5f);
		RotateAnimation animation2 = new RotateAnimation(0, 360, rela1, 0.5f, rela1, 0.5f);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**缩小旋转退出
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:42:56
	 *@author aaa
	 */
	public void scaleRotateOut(View view, long durationMillis, long delayMillis){
		ScaleAnimation animation1 = new ScaleAnimation(1, 0, 1, 0, rela1, 0.5f, rela1, 0.5f);
		RotateAnimation animation2 = new RotateAnimation(0, 360, rela1, 0.5f, rela1, 0.5f);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseOut(view, animation, durationMillis, delayMillis);
	}

	/**滑动淡入
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:42:35
	 *@author aaa
	 */
	public void slideFadeIn(View view, long durationMillis, long delayMillis){
		TranslateAnimation animation1 = new TranslateAnimation(rela2, 1, rela2, 0, rela2, 0, rela2, 0);
		AlphaAnimation animation2 = new AlphaAnimation(0, 1);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseIn(view, animation, durationMillis, delayMillis);
	}

	/**滑动淡出
	 * @param view
	 * @param durationMillis
	 * @param delayMillis
	 *@date 2013-11-12 上午10:42:11
	 *@author aaa
	 */
	public void slideFadeOut(View view, long durationMillis, long delayMillis){
		TranslateAnimation animation1 = new TranslateAnimation(rela2, 0, rela2, -1, rela2, 0, rela2, 0);
		AlphaAnimation animation2 = new AlphaAnimation(1, 0);
		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(animation1);
		animation.addAnimation(animation2);
		baseOut(view, animation, durationMillis, delayMillis);
	}
}
