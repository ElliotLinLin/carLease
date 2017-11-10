package com.hst.Carlease.widget.mywidget;

import com.hst.Carlease.app.MainApplication;

import android.app.Activity;
import android.util.DisplayMetrics;


/**
 * @author Ljj
 *
 * 描述：屏幕适配工具
 * 功能/说明：
 */
public class DisplayUtil {
	private static int baseX = 720;
	private static int baseY = 1280;

	private DisplayUtil(Activity ui) {
		if (ui == null) {
			throw new RuntimeException("params error");
		}
		metrics = new DisplayMetrics();
		ui.getWindowManager().getDefaultDisplay().getMetrics(metrics);

	};

	private static DisplayUtil instance = null;
	private DisplayMetrics metrics;

	public static DisplayUtil getInstance(Activity ui) {
		if (instance == null) {
			instance = new DisplayUtil(ui);
		}
		return instance;
	}

	public float getX(int x) {
		return (metrics.widthPixels * 1.0f / baseX) * x / metrics.density;
	}

	public float getY(int y) {
		return (metrics.heightPixels * 1.0f / baseY) * y / metrics.density;
	}

	public int getBLX(int x) {
		return (int) ((metrics.widthPixels * 1.0f / baseX) * x);
	}

	public int getBLY(int y) {
		return (int) ((metrics.heightPixels * 1.0f / baseY) * y);
	}

	public int getScreenWidthPx() {
		return metrics.widthPixels;
	}

	public int getScreenHeightPx() {
		return metrics.heightPixels;
	}

	public int getDensityDpi() {
		return metrics.densityDpi;
	}
	//长度
	public static int get_BLX(int x) {
		return (int) ((MainApplication.getInstance().getScreenWidth() * 1.0f / baseX) * x);
	}

	//字体
	public static int get_X(int x) {
		MainApplication app = MainApplication.getInstance();
		return (int) ((app.getScreenWidth() * 1.0f / baseX) * x / app.getDensity());
	}
}
