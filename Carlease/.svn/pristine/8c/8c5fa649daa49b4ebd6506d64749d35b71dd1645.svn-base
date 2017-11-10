package com.hst.Carlease.ui;

import com.hst.Carlease.R;
import com.hst.Carlease.app.MainApplication;
import com.hst.Carlease.ui.fragement.HomePageFgm;
import com.hst.Carlease.ui.fragement.HotModelFgm;
import com.hst.Carlease.ui.fragement.MineFgm;
import com.hst.Carlease.util.FullTitleBar;
import com.hst.Carlease.widget.mywidget.CMMenu;
import com.hst.Carlease.widget.mywidget.MenuItem;
import com.hst.Carlease.widget.mywidget.OnCheckedChanged;

import com.tools.app.AbsUI2;
import com.tools.app.AlertDialog;
import com.tools.app.Config;
import com.tools.app.UIManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;

/**
 * 主界面
 * 
 * @author lyq
 * 由于每次都访问网络，暂时不用这个主界面
 */
public class MainUI extends AbsUI2 {

	private CMMenu menu;// 下面五个菜单
	FullTitleBar TITLE;// 沉浸式标题栏

	@Override
	protected void onCreate(Bundle arg0) {
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {  
	        
	        finish();  
	        return;  
	    }  
		setContentView(R.layout.main_layout);
		setSlideFinishEnabled(false);
		TITLE = new FullTitleBar(this, Color.parseColor("#2A8AE0"));
		super.onCreate(arg0);

	}

	@Override
	protected void initControl() {
		menu = (CMMenu) findViewById(R.id.tabhost);
	}

	@Override
	protected void initControlEvent() {
		menu.setOnCheckedChanged(new OnCheckedChanged() {
			@Override
			public void onChanged(MenuItem item, boolean isChecked) {
				Fragment fragmentClass = null;
				switch (item) {
				case ADD:
					fragmentClass = new HotModelFgm();// 热门车型
					break;
				case QUERY:
					fragmentClass = new HomePageFgm();// 首页
					break;
				// case COUNT:
				// fragmentClass=new OnLineBookingFgm();//在线预订
				// break;
				case MORE:
					fragmentClass = new MineFgm();// 我
					break;
				}
				replaceFgm(R.id.fl_content, fragmentClass);
			}
		});
		// 默认显示的页面
		addFgm(R.id.fl_content, new HomePageFgm());
	}

	@Override
	protected void initMember() {

	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ui);
		builder.setTitle(" ");
		builder.setMessage("您确定退出吗？");

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (Config.isMonkeyEnabled() == false) {
					UIManager.getInstance().finishAll();
					MainApplication.quit(ui);
				}
			}

		}).setNegativeButton("取消", new DialogInterface.OnClickListener() { 

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		}).create().show();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
