package com.tools.task;


import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.tools.util.Log;
import com.tools.widget.CreateLoadingView;

/**
 * http同步Loader任务。
 * 
 * 权限：
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * 
 * @author LMC
 *
 * @param <D>
 */
public abstract class AbsTaskLoaderHttpWait<D> extends AbsTaskLoaderHttp<D> {

	private static final String TAG = AbsTaskLoaderHttpWait.class.getSimpleName();

	// UI
	protected android.support.v4.app.FragmentActivity ui = null;

	// 圆形进度条
	protected ProgressDialog progressDialog = null;

	// 是否显示圆形进度对话框
	protected boolean dialogShowable = true;

	// 是否允许关闭对话框
	protected boolean dialogCloseable = true;

	// 显示的文本
	protected String showText = "";

	public AbsTaskLoaderHttpWait(android.support.v4.app.FragmentActivity ui) {
		super(ui.getApplicationContext());
		init(ui, "");
	}

	public AbsTaskLoaderHttpWait(android.support.v4.app.FragmentActivity ui, int resId) {
		super(ui.getApplicationContext());
		init(ui, context.getResources().getString(resId));
	}

	public AbsTaskLoaderHttpWait(android.support.v4.app.FragmentActivity ui, String showText) {
		super(ui.getApplicationContext());
		init(ui, showText);
	}

	/**
	 * 初始化
	 * 
	 * @param ui
	 * @param showText
	 */
	private void init(android.support.v4.app.FragmentActivity ui, String showText) {
		this.ui = ui;
		this.showText = showText;
	}

	public boolean getDialogShowable() {
		return this.dialogShowable;
	}
	
	/**
	 * 是否显示圆形进度对话框
	 */
	public void setDialogShowable(boolean enable) {
		this.dialogShowable = enable;
		Log.e(TAG, "dialogShowable:"+dialogShowable);
	}

	public boolean getDialogCloseable() {
		return this.dialogCloseable;
	}
	
	/**
	 * 是否允许关闭对话框
	 */
	public void setDialogCloseable(boolean enable) {
		this.dialogCloseable = enable;
		Log.e(TAG, "dialogCloseable:"+dialogCloseable);
	}

	@Override
	protected void onStartLoading() {

		if ( dialogShowable ) {
			// 进度
			progressDialog = new ProgressDialog(ui);

			// 是否允许关闭
			progressDialog.setCancelable(dialogCloseable);

			// 点击允许点击外部关闭对话框？
			progressDialog.setCanceledOnTouchOutside(false);

			progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					Log.e(TAG, "Dialog::onDismiss()");
					// 丢弃
					abandon();
				}

			});

			progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					Log.e(TAG, "Dialog::onCancel()");
					// 丢弃
					abandon();
					//点击返回键的时候，需要销毁loader，否则再次点击加载不起作用
					if (ui != null) {
						ui.getSupportLoaderManager().destroyLoader(getId());
					}
				}

			});

			// 显示对话框
			progressDialog.show();
			progressDialog.setContentView(CreateLoadingView.createView(context, showText));

		}

		super.onStartLoading();
	}

	@Override
	protected void onStopLoading() {
		// 关闭对话框
		closeDialog();
		super.onStopLoading();
	}

	@Override
	public void onCanceled(D data) {
		// 关闭对话框
		closeDialog();
		super.onCanceled(data);
	}

	@Override
	protected void onAbandon() {
		// 关闭对话框
		closeDialog();
		super.onAbandon();
	}

	@Override
	protected void onReset() {
		// 关闭对话框
		closeDialog();
		super.onReset();
	}

	/*
	 * 关闭对话框
	 */
	protected void closeDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void abandon() {
		// 关闭对话框
		closeDialog();
		super.abandon();
	}

}