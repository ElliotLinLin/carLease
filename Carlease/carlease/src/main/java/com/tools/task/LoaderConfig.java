package com.tools.task;


public class LoaderConfig {

	private static final String TAG = LoaderConfig.class.getSimpleName();

	// 是否显示圆形进度对话框
	protected boolean dialogShowable = false;

	// 是否允许关闭对话框
	protected boolean dialogCloseable = true;

	/**
	 * 是否显示圆形进度对话框
	 */
	public void setDialogShowable(boolean enable) {
		this.dialogShowable = enable;
	}

	/**
	 * 是否允许关闭对话框
	 */
	public void setDialogCloseable(boolean enable) {
		this.dialogCloseable = enable;
	}
	
	public boolean getDialogShowable() {
		return this.dialogShowable;
	}
	
	public boolean getDialogCloseable() {
		return this.dialogCloseable;
	}

}
