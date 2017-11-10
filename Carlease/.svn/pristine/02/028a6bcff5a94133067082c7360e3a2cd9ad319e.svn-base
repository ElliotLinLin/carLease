package com.hst.Carlease.widget;

import org.greenrobot.eventbus.EventBus;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hst.Carlease.R;
import com.hst.Carlease.eventBean.Stype;
import com.hst.Carlease.http.bean.UpgradeMessageBean;
import com.hst.Carlease.ram.Constant;
import com.hst.Carlease.util.UpgradeTaskUtil;
import com.tools.bean.UpgradeNotificationBean;
import com.tools.net.NetworkState;
import com.tools.task.UpgradeTask;
import com.tools.util.Log;

/**
 * 升级对话框
 */
public class UpgradeDialog extends Dialog {
	
	private static final String TAG = UpgradeDialog.class.getSimpleName();
	
	Context context;
	private TextView tv_upgrade_title;//标题栏
	private TextView tv_upgrade_content;//内容
	Button btn_upgrade_cancle;//下次再说
	Button btn_upgrade_sure;//立即更新
	
	String title = null;
	String content = null;
	UpgradeMessageBean taskBean;//web端返回来的bean
	UpgradeNotificationBean notificationBean;//用于状态栏
	
	public UpgradeDialog(Context context) {
		super(context, R.style.tools_filter_dialog);
		this.context = context;
		setCancelable(false);
		setCanceledOnTouchOutside(false);
	}
	
	@Override
	public void show() {
		
		setContentView(R.layout.tools_upgrade_dialog);
		
		initControl();
		initControlEvent();
		initMember();
		super.show();
		
	}
	
	protected void  initControl() {
		tv_upgrade_title = (TextView) findViewById(R.id.tv_upgrade_title);
		tv_upgrade_content = (TextView) findViewById(R.id.tv_upgrade_content);
		btn_upgrade_cancle = (Button) findViewById(R.id.btn_upgrade_cancle);
		btn_upgrade_sure = (Button) findViewById(R.id.btn_upgrade_sure);
	}
	
	protected void initControlEvent() {
		//立即更新
		btn_upgrade_sure.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (notificationBean != null) {
					if (new NetworkState(context).isConnected() == false) {
						Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show();
						return;
					}
					if (!UpgradeTaskUtil.isCanDownload) {
						Toast.makeText(context, "没有存储卡，不能下载新版本", Toast.LENGTH_SHORT).show();
						UpgradeDialog.this.dismiss();
						return;
					}
					// 下载并且更新应用
					UpgradeTask task = new UpgradeTask((android.support.v4.app.FragmentActivity)context);
					if (notificationBean != null) {
						task.setTitle( notificationBean.getTitle() );
						task.setDownloadRemoteUri(notificationBean.getDownloadRemoteUri());
						task.setDownloadLocalPath(notificationBean.getDownloadLocalPath());
						task.download();
					}
					// 关闭自身窗口
					UpgradeDialog.this.dismiss();
				}else{
					Log.e(TAG, "notificationBean == null");
				}
			}
		});
		
		//下次再说
		btn_upgrade_cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(new Stype(Constant.LOGIN_UI));
				UpgradeDialog.this.dismiss();
			}
		});
	}

	protected void initMember() {
		if (tv_upgrade_title != null && title != null) {
			tv_upgrade_title.setText(title);
		}
		if (tv_upgrade_content != null) {
			tv_upgrade_content.setText(content);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UpgradeMessageBean getTaskBean() {
		return taskBean;
	}

	public void setTaskBean(UpgradeMessageBean taskBean) {
		this.taskBean = taskBean;
	}

	public UpgradeNotificationBean getNotificationBean() {
		return notificationBean;
	}

	public void setNotificationBean(UpgradeNotificationBean notificationBean) {
		this.notificationBean = notificationBean;
	}
	
}
