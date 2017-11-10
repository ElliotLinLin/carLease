package com.tools.app;

import java.util.LinkedList;
import java.util.List;

import com.tools.util.Log;

/**
 * UI管理器
 * 
 * @author LMC
 * 
 */
public class UIManager {

	private static final String TAG = UIManager.class.getSimpleName();

	private List<android.support.v4.app.FragmentActivity> uiList = new LinkedList<android.support.v4.app.FragmentActivity>();

	private static UIManager instance;

	private UIManager() {

	}

	public static UIManager getInstance() {
		if (null == instance) {
			instance = new UIManager();
		}
		return instance;
	}

	/**
	 * 添加UI到容器中
	 * 
	 * @param ui
	 */
	public void addUI(android.support.v4.app.FragmentActivity ui) {
		if (ui == null) {
			return;
		}
		if (ui.isFinishing() == false) {
			// 没有finish才add
			uiList.add(ui);
		}
	}

	/**
	 * 关闭UI
	 * 
	 * Tasks and Back Stack(任务栈和返回堆栈) finish()会将Back Stack返回堆栈清除
	 * 
	 * @param clazz
	 */
	public void finish(Class<?> clazz) {
		if (clazz == null) {
			return;
		}

		List<android.support.v4.app.FragmentActivity> tmpList = new LinkedList<android.support.v4.app.FragmentActivity>();

		for (android.support.v4.app.FragmentActivity ui : uiList) {
			if (ui.getClass() == clazz) {
				tmpList.add(ui);
				ui.finish();
				Log.i(TAG, "finish(clazz) ui.getClass():"
						+ ui.getClass().getCanonicalName());
				// uiList.remove(ui); // List不能在遍历时将元素删除
			}
		}

		uiList.removeAll(tmpList);
	}

	/**
	 * 关闭UI
	 * 
	 * @param ui
	 */
	public void finish(android.support.v4.app.FragmentActivity ui) {
		if (ui == null) {
			return;
		}

		List<android.support.v4.app.FragmentActivity> tmpList = new LinkedList<android.support.v4.app.FragmentActivity>();

		for (android.support.v4.app.FragmentActivity tmpUI : uiList) {
			// Log.i(TAG,
			// "aaaaaaaaaaaa tmpui:"+tmpUI.getClass().getCanonicalName());
			// Log.i(TAG, "aaaaaaaaaaaa ui:"+ui.getClass().getCanonicalName());
			if (tmpUI.equals(ui)) {
				tmpList.add(tmpUI);
				tmpUI.finish();
				Log.i(TAG, "finish(clazz) tmpUI.equals(ui):"
						+ tmpUI.getClass().getCanonicalName() + " == true");
				// uiList.remove(ui); // List不能在遍历时将元素删除
			}
		}

		uiList.removeAll(tmpList);
	}

	/**
	 * 关闭UI
	 * 
	 * @param ui
	 */
	// public void finish(android.support.v4.app.FragmentActivity ui) {
	// if (ui == null) {
	// return;
	// }
	// if (ui.isFinishing() == false) {
	// Log.i(TAG, "execute ... ui.finish():"+ui.getClass().getCanonicalName());
	// ui.finish();
	// }
	// }

	/**
	 * 关闭全部UI
	 * 
	 * 这个问题是说,你不能在对一个List进行遍历的时候将其中的元素删除掉
	 * 解决办法是,你可以先将要删除的元素用另一个list装起来,等遍历结束再remove掉
	 * 否则会发生java.util.ConcurrentModificationException
	 */
	public void finishAll() {
		for (android.support.v4.app.FragmentActivity ui : uiList) {
			ui.finish();
			Log.i(TAG, "finishAll() ui.getClass():"
					+ ui.getClass().getCanonicalName());
			// uiList.remove(ui); // List不能在遍历时将元素删除
		}
		// List不能在遍历时将元素删除掉
		uiList.removeAll(uiList); // 遍历完成后执行删除
	}

	/**
	 * 关闭全部UI，但除了excludeUI
	 * 
	 * @param excludeUI
	 */
	public void finishAll(Class<?>... excludeUI) {

		List<android.support.v4.app.FragmentActivity> tmpList = new LinkedList<android.support.v4.app.FragmentActivity>();

		for (android.support.v4.app.FragmentActivity ui : uiList) {
			if (isArrayExists(ui, excludeUI) == false) { // 不存在数组中，才关闭和移除
				tmpList.add(ui);
				ui.finish();
				Log.i(TAG, "finishAll(Class<?>... excludeUI) ui.getClass():"
						+ ui.getClass().getCanonicalName());
				// uiList.remove(ui); // List不能在遍历时将元素删除
			}
		}

		// List不能在遍历时将元素删除
		uiList.removeAll(tmpList);
	}

	/**
	 * 判断是否存在数组中
	 * 
	 * @param ui
	 * @param excludeUI
	 * @return
	 */
	protected boolean isArrayExists(android.support.v4.app.FragmentActivity ui,
			Class<?>... excludeUI) {
		if (ui == null) {
			return false;
		}

		if (excludeUI == null) {
			return false;
		}

		for (Class<?> clazz : excludeUI) {
			if (ui.getClass() == clazz) {
				Log.i(TAG, "isArrayExists() clazz:" + clazz.getCanonicalName()
						+ " == true");
				return true;
			}
		}

		return false;
	}

	/**
	 * 打印
	 */
	public void print() {
		Log.i(TAG, "------ UIManager() start ------");
		Log.i(TAG, "size:" + uiList.size());
		for (android.support.v4.app.FragmentActivity ui : uiList) {
			Log.i(TAG, "getCanonicalName:" + ui.getClass().getCanonicalName()
					+ ",isFinishing:" + ui.isFinishing());
		}
		Log.i(TAG, "------ UIManager() end ------");
	}

	/**
	 * 获取UI数量
	 * 
	 * @return
	 */
	public int getUICount() {
		if (uiList == null) {
			return 0;
		}
		return uiList.size();
	}
}