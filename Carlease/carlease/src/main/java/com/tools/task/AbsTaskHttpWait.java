package com.tools.task;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.tools.util.Log;
import com.tools.widget.CreateLoadingView;

/**
 * http同步任务。
 * 
 * 权限：
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * 
 * @author lmc
 * 版权声明：版权归作者所有，未经协议授权不得使用、转载、抄袭等，否则依法追究法律责任和经济责任。
 */
public abstract class AbsTaskHttpWait<Params, Progress, Result> extends AbsTaskHttp<Params, Progress, Result> {

	private static final String TAG = AbsTaskHttpWait.class.getSimpleName();

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

	public AbsTaskHttpWait(android.support.v4.app.FragmentActivity ui) {
		super(ui.getApplicationContext());
		init(ui, "");
	}

	public AbsTaskHttpWait(android.support.v4.app.FragmentActivity ui, int resId) {
		super(ui.getApplicationContext());
		init(ui, context.getResources().getString(resId));
	}

	public AbsTaskHttpWait(android.support.v4.app.FragmentActivity ui, String showText) {
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

	/**
	 * 是否显示圆形进度对话框
	 */
	public void setDialogShowable(boolean enable) {
		this.dialogShowable = enable;
		Log.e(TAG, "dialogShowable:"+dialogShowable);
	}

	/**
	 * 是否允许关闭对话框
	 */
	public void setDialogCloseable(boolean enable) {
		this.dialogCloseable = enable;
		Log.e(TAG, "dialogCloseable:"+dialogCloseable);
	}

	@Override
	protected void onPreExecute() {
		//		Log.e(TAG, "onPreExecute");

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
					// 取消线程
					cancel();
				}

			});

			progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					Log.e(TAG, "Dialog::onCancel()");
					// 取消线程
					cancel();
				}

			});

			// 显示对话框
			progressDialog.show();
			progressDialog.setContentView(CreateLoadingView.createView(context, showText));

		}

		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Result result) {
		//		Log.e(TAG, "onPostExecute()");
		// 关闭对话框
		closeDialog();
		super.onPostExecute(result);
	}

	/**
	 * 关闭对话框
	 */
	protected void closeDialog() {
		if (progressDialog != null && progressDialog.isShowing() && ui != null && !ui.isFinishing()) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 取消线程
	 */
	public void cancel() {
		Log.e(TAG, "cancel()");
		// 关闭对话框
		closeDialog();
		// 关闭AsyncTask
		super.cancel();
	}

}
