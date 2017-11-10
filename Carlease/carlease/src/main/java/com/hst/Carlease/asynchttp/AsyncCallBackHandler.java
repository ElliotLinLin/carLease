package com.hst.Carlease.asynchttp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import com.hst.Carlease.widget.mywidget.ToastL;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tools.util.Log;
import com.tools.widget.CreateLoadingView;

import org.apache.http.Header;

/**
 * 自定义自己的网络请求回调事件 为了在发送请求的页面处理网络请求事件，这里将继承类定义为抽象类，实现该类的子类就会重写父类的抽象方法
 * 在重写方法类对网络请求做处理
 * 
 * @author HL
 * 
 */
public abstract class AsyncCallBackHandler extends AsyncHttpResponseHandler {

	private static final String TAG = AsyncCallBackHandler.class
			.getSimpleName();

	// 圆形进度条
	protected ProgressDialog progressDialog = null;

	// UI
	protected android.support.v4.app.FragmentActivity ui = null;

	protected String showTextString = "";

	// 是否要显示进度条，外部传递
	protected boolean isdialogShow = true;
	protected boolean isCloseDig = true;
	protected View view;// 需要控制是否能点击的view

	public AsyncCallBackHandler(android.support.v4.app.FragmentActivity ui,
			boolean isdialogShow, View view) {
		init(ui, "", isdialogShow, view);
	}

	public AsyncCallBackHandler(android.support.v4.app.FragmentActivity ui,
			int resId, boolean isdialogShow, View view) {
		init(ui, ui.getResources().getString(resId), isdialogShow, view);
	}

	public AsyncCallBackHandler(android.support.v4.app.FragmentActivity ui,
			String showText, boolean isdialogShow, View view) {
		init(ui, showText, isdialogShow, view);
	}

	private void init(FragmentActivity ui, String string, boolean isdialogShow,
			View view) {

		this.ui = ui;
		this.showTextString = string;
		this.isdialogShow = isdialogShow;
		this.view = view;
	}

	public boolean isCloseDig() {
		return isCloseDig;
	}

	public void setCloseDig(boolean isCloseDig) {
		this.isCloseDig = isCloseDig;
	}

	public void showProgressDiag() {
		// 进度
		progressDialog = new ProgressDialog(ui);

		// 是否允许关闭
		progressDialog.setCancelable(isCloseDig);

		// 点击允许点击外部关闭对话框？
		progressDialog.setCanceledOnTouchOutside(false);

		progressDialog
				.setOnDismissListener(new DialogInterface.OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {
						Log.e(TAG, "Dialog::onDismiss()");
						// 取消线程
						cancel();
					}

				});

		DialogInterface.OnCancelListener listener = new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				Log.e(TAG, "Dialog::onCancel()");
				// 取消线程
				cancel();
			}

		};
		DetachableClickListener clickListener = DetachableClickListener.wrap(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				cancel();
			}
		});

		progressDialog
				.setOnCancelListener(clickListener);
		// 显示对话框
		progressDialog.show();
		clickListener.clearOnDetach(progressDialog);
		progressDialog.setContentView(CreateLoadingView.createView(ui,showTextString));
	}

	/**
	 * 关闭对话框
	 */
	protected void closeDialog() {
		if (progressDialog != null && progressDialog.isShowing() && ui != null
				&& !ui.isFinishing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}



	/**
	 * 取消线程
	 */
	public void cancel() {
		Log.e(TAG, "cancel()");
		// 关闭对话框
		closeDialog();
		// 关闭请求
		super.onCancel();
	}

	/**
	 * 网络请求失败回调事件
	 */
	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// 取消进度条
		cancel();
		Log.e(TAG, "Http Error" + arg0+"   "+arg3);
		closeDialog();
//		doErrorCode(arg0);
		if (arg2 != null) {
			// {"Message":"There was an error processing the request.","StackTrace":"","ExceptionType":""}
			myFailure(arg0, arg1, new String(arg2), arg3);
		} else {
			myFailure(arg0, arg1, null, arg3);
		}
	}

	private void doErrorCode(int code){
		if (code==0) {
			ToastL.show("连接超时");
			return;
		}
	}
	@Override
	public void onStart() {
		if (isdialogShow) {
			// 显示请求进度条
			showProgressDiag();
		}

		super.onStart();
	}

	/**
	 * 网络请求成功回调事件
	 */
	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// 取消进度条
		cancel();
		// 控制控件是否能点击
		if (view != null) {
			view.setEnabled(true);
		}
		closeDialog();
		if (arg2 != null) {
			mySuccess(arg0, arg1, new String(arg2));
		} else {
			mySuccess(arg0, arg1, null);
		}

	}

	/**
	 * 定义自己的网络失败回调事件,子类重写
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public abstract void myFailure(int arg0, Header[] arg1, String arg2,
			Throwable arg3);

	/**
	 * 定义自己的网络成功回调事件，子类重写
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public abstract void mySuccess(int arg0, Header[] arg1, String arg2);




}
final class DetachableClickListener implements DialogInterface.OnCancelListener {

	public static DetachableClickListener wrap(DialogInterface.OnCancelListener delegate) {
		return new DetachableClickListener(delegate);
	}

	private DialogInterface.OnCancelListener delegateOrNull;

	private DetachableClickListener(DialogInterface.OnCancelListener delegate) {
		this.delegateOrNull = delegate;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (delegateOrNull != null) {
			delegateOrNull.onCancel(dialog);
		}

	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void clearOnDetach(Dialog dialog) {
		dialog.getWindow()
				.getDecorView()
				.getViewTreeObserver()
				.addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
					@Override public void onWindowAttached() { }
					@Override public void onWindowDetached() {
						delegateOrNull = null;
					}
				});
	}

}