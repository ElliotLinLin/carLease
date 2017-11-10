package com.tools.app;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;

import com.tools.util.Log;

public abstract class AbsListFgm extends android.support.v4.app.ListFragment {

	private static final String TAG = AbsListFgm.class.getSimpleName();
	private static final boolean DEBUG = false;

	protected android.support.v4.app.FragmentActivity ui = null;

	protected Context context = null;

	/**
	 * 初始化
	 */
	private void init() {
		if (DEBUG) Log.e(TAG, "init()");
		initControl();
		initControlEvent();
		initMember();
	}

	protected abstract void initControl();
	protected abstract void initControlEvent();
	protected abstract void initMember();

	/**
	 * 获取扩展包
	 * 
	 * @return
	 */
	protected Bundle getExtras() {
		Bundle extras = getArguments();
		return extras;
	}

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 *
	 * @param src
	 * @return
	 */
	protected static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 清除列表适配器数据
	 */
	public static void clearListView(BaseAdapter adapter, List<?> list) {
		Log.e(TAG, "clearListView()");
		if (list != null) {
			list.clear();
		}
		refreshListView(adapter);
	}

	/**
	 * 刷新列表适配器数据
	 */
	public static void refreshListView(BaseAdapter adapter) {
		Log.e(TAG, "refreshListView()");
		if (adapter == null) {
			return;
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.ui = this.getActivity();
		context = ui.getApplicationContext();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	@Override
	public void setListShown(boolean shown) {
		// ListFragment之间切换时，线程里执行setListShown()可能会出错,因ListFragment移除。
		if (this.isAdded() == false) {
			Log.i(TAG, "ListFragment未添加。");
			return;
		} else {
			//			Log.i(TAG, "ListFragment已添加。");
		}
		super.setListShown(shown);
	}

	@Override
	public void setEmptyText(CharSequence text) {
		if (this.isAdded() == false) {
			Log.i(TAG, "ListFragment未添加。");
			return;
		} else {
			//			Log.i(TAG, "ListFragment已添加。");
		}
		super.setEmptyText(text);
	}

}
