package com.tools.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.tools.util.Log;

/**
 * 升级通知栏的Bean
 * 
 * @author LMC
 *
 */
public class UpgradeNotificationBean implements android.os.Parcelable {
	private static final String TAG = UpgradeNotificationBean.class.getSimpleName();

	private String oldVersion;
	private String newVersion;
	private String title;
	private String downloadRemoteUri;
	private String downloadLocalPath;
	private String size;

	@Override
	public String toString() {
		Log.e(TAG, "------ toString() start------");
		Log.e(TAG, "oldVersion:"+oldVersion);
		Log.e(TAG, "newVersion:"+newVersion);
		Log.e(TAG, "title:"+title);
		Log.e(TAG, "downloadRemoteUri:"+downloadRemoteUri);
		Log.e(TAG, "downloadLocalPath:"+downloadLocalPath);
		Log.e(TAG, "------ toString() end------");
		return super.toString();
	}

	public String getOldVersion() {
		return oldVersion;
	}
	public void setOldVersion(String oldVersion) {
		this.oldVersion = oldVersion;
	}
	public String getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDownloadRemoteUri() {
		return downloadRemoteUri;
	}
	public void setDownloadRemoteUri(String downloadUri) {
		this.downloadRemoteUri = downloadUri;
	}
	public String getDownloadLocalPath() {
		return downloadLocalPath;
	}
	public void setDownloadLocalPath(String downloadLocalPath) {
		this.downloadLocalPath = downloadLocalPath;
	}
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(oldVersion);
		dest.writeString(newVersion);
		dest.writeString(title);
		dest.writeString(downloadRemoteUri);
		dest.writeString(downloadLocalPath);
		dest.writeString(size);
	}

	public static final Parcelable.Creator<UpgradeNotificationBean> CREATOR 
	= new Parcelable.Creator<UpgradeNotificationBean>() {
		public UpgradeNotificationBean createFromParcel(Parcel in) {
			UpgradeNotificationBean bean = new UpgradeNotificationBean();
			bean.oldVersion = in.readString();
			bean.newVersion = in.readString();
			bean.title = in.readString();
			bean.downloadRemoteUri = in.readString();
			bean.downloadLocalPath = in.readString();
			bean.size = in.readString();
			return bean;
		}        
		public UpgradeNotificationBean[] newArray(int size) {
			return new UpgradeNotificationBean[size];
		}
	};

}