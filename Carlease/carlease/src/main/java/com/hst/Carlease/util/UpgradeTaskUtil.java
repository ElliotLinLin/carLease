package com.hst.Carlease.util;

import android.content.Context;
import android.widget.Toast;

import com.hst.Carlease.R;
import com.hst.Carlease.http.bean.Bean;
import com.hst.Carlease.http.bean.UpgradeMessageBean;
import com.hst.Carlease.ram.Http_Url;
import com.hst.Carlease.widget.UpgradeDialog;
import com.tools.bean.UpgradeNotificationBean;
import com.tools.json.GJson;
import com.tools.net.http.HttpTool.Error;
import com.tools.os.storage.StorageTool;
import com.tools.ram.SDRam;
import com.tools.task.UpgradeTask;
import com.tools.util.Log;



/**
* 
* 升级工具类
*
*/
public class UpgradeTaskUtil {
	
	private static final String TAG = UpgradeTaskUtil.class.getSimpleName();

	private static final boolean isTest = false;//是否是测试数据

	public static boolean isCanDownload = true;

	protected android.support.v4.app.FragmentActivity ui = null;

	protected Context context = null;

	public UpgradeTaskUtil(android.support.v4.app.FragmentActivity ui) {
		init(ui);
	}

	/**
	 * 初始化
	 *
	 * @param ui
	 */
	private void init(android.support.v4.app.FragmentActivity ui) {
		if (ui == null) {
			Log.exception(TAG, new NullPointerException("ui == null"));
		}
		this.ui = ui;
		this.context = ui.getApplicationContext();
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
	 * 检查
	 */
	public void check(boolean isShowDialog,String showText) {
		//如果正在检查或是正在升级，再点击则不会重新检查
		if (UpgradeTask.isCheck()) {
			return;
		}
		isCanDownload = true;
		UpgradeTask task = new UpgradeTask(ui);
		
		// 请求地址
		String queryUri =Http_Url.GetAppVersion;
		// 本地下载保存路径        		要不要修改成路径 ???? 有一个缺点，更新版本越多，本地保存文件越多。
		StorageTool storageTool = new StorageTool(context);
		String path = storageTool.getMountedPath();
		String downloadLocalPath = null;
		if (path == null) {
			isCanDownload = false;//不能下载 
		}

		downloadLocalPath = path + SDRam.getUpgradePath() + SDRam.getApkname();
		Log.i(TAG, "downloadLocalPath == "+downloadLocalPath);

		task.setDownloadLocalPath(downloadLocalPath);
		// 下载显示的标题
		task.setTitle(context.getResources().getString(R.string.app_name));
		// 执行检查
		task.check(queryUri, isShowDialog, showText, new UpgradeTask.Adapter() {

			private String VersionNO = null;
			private String downloadRemoteUri = null;
			private UpgradeMessageBean taskBean = null;

			@Override
			public void onQueryCompleted(String result) {
				try {
					Bean d = GJson.parseObject(result, Bean.class);
					taskBean = GJson.parseObject(d.getD(), UpgradeMessageBean.class);
					
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(context, "检测失败", Toast.LENGTH_SHORT).show();
					return;
				}
				if (taskBean.getModel()==null) {
					return;
				}
					// 最新版本
					VersionNO =taskBean.getModel().getVersionNo();
					// 下载地址
					downloadRemoteUri =taskBean.getModel().getVersionLink();
			}

			

			@Override
			public void onUpgrade(boolean upgrade, String oldVersion, String newVersion, String downloadRemoteUri, String downloadLocalPath,String size) {
				Log.i(TAG, "upgrade==="+upgrade);
				if (isTest) {
					upgrade = false;
					Log.i(TAG, "upgrade= isTest=="+upgrade);
				}
				if (upgrade) {
					Log.e(TAG, "找到最新版。");
					UpgradeNotificationBean notificationBean = new UpgradeNotificationBean();
					notificationBean.setTitle(context.getResources().getString(R.string.app_name));
					Log.e(TAG, "notificationBean.getTitle():"+notificationBean.getTitle());
					notificationBean.setOldVersion(oldVersion);
					notificationBean.setNewVersion(newVersion);
					notificationBean.setDownloadRemoteUri(downloadRemoteUri);
					notificationBean.setDownloadLocalPath(downloadLocalPath);
					notificationBean.setSize(size);
					UpgradeDialog upgradeDialogF = new UpgradeDialog(ui);//这里的ui不能改为context，必须为界面
					upgradeDialogF.setTitle("版本更新");
					upgradeDialogF.setContent(context.getResources().getString(R.string.app_name)+"新版发布(v"+newVersion+")");
					String content = null;
					if (isTest) {
						Log.i(TAG, "upgrade= taskBean.getData().getNote()==");
						content = "发布内容：\n1 优化速度最新版本\n2 优化速度最新版本\n3 优化速度最新版本\n4 优化速度最新版本\n5 优化速度最新版本\n6 优化速度最新版本\n7 优化速度最新版本";
						upgradeDialogF.setContent(content);
					}
					upgradeDialogF.setTaskBean(taskBean);
					upgradeDialogF.setNotificationBean(notificationBean);
					if (!ui.isFinishing()) {
						upgradeDialogF.show();
					}else {
						Log.i(TAG, "ui.isFinishing()="+ui.isFinishing());
					}

				}else{
				}
			}

			@Override
			public String getDownloadRemoteUri() {
				return downloadRemoteUri;
			}

			@Override
			public void onHttpFailed(Error error, Exception exception, int responseCode, byte[] out) {

			}


			@Override
			public void onHttpFailed(java.lang.Error error,
					Exception exception, int responseCode, byte[] out) {
				
			}




@Override
public String getVersionNO() {
	return VersionNO;
}



@Override
public String getSize() {
	return null;
}

		} );
	}


}
