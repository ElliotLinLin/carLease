package com.tools.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 升级任务Bean
 * 
 * 升级协议：
 * {
    "upgrade": {
    	"name": "应用名称"
        "version": "1.2.52",
        "url": "http://www.baidu.com/aaa.apk",
        "size": "5M",
        "releaseDate": "2013-08-08",
        "note": "1)增加aaaa\n2)增加ooooo\n"
    }
}

说明：
name ---> 应用名称
version ---> 版本
url ---> 绝对下载地址
size ---> 应用大小
releaseDate ---> 升级发布日期
note ---> 功能更新说明

变成一行
{    \"upgrade\": {        \"version\": \"1.2.52\",        \"url\": \"http://www.baidu.com/aaa.apk\",        \"size\": \"5M\",        \"releaseDate\": \"2013-08-08\",        \"note\": \"1)增加ACC\\n2)增加Color\\n\"    }}

 * 
 * @author LMC
 *
 */
public class UpgradeTaskBean  implements Parcelable{
	
	private static final String TAG = UpgradeTaskBean.class.getSimpleName();
	
	public static final String KEY = "upgrade";
	
	private String result = null;//返回状态:”success”,”error”
	private String resultInfo = null;
	private data data;
	
	public static class data implements Parcelable{
		String version = null;
		String url = null;//"http://Upgrade/GPSCheck_v1.3.1_2013-10-24.apk"
		String size = null;//"5M",//软件大小
		String releaseDate = null;//"2013-10-24",//发布日期
		String note;//说明
		
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getSize() {
			return size;
		}
		public void setSize(String size) {
			this.size = size;
		}
		public String getReleaseDate() {
			return releaseDate;
		}
		public void setReleaseDate(String releaseDate) {
			this.releaseDate = releaseDate;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			
		}
		public static final Parcelable.Creator<data> CREATOR 
		= new Parcelable.Creator<data>() {
			public data createFromParcel(Parcel in) {
				data bean = new data();
//				bean.name = in.readString();
				bean.version = in.readString();
				bean.url = in.readString();
				bean.size = in.readString();
				bean.releaseDate = in.readString();
				bean.note = in.readString();
				return bean;
			}
			public data[] newArray(int size) {
				return new data[size];
			}
		};
		
		
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}

	public data getData() {
		return data;
	}

	public void setData(data data) {
		this.data = data;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(result);
		dest.writeString(resultInfo);
		dest.writeParcelable(data, 0);
	}
	
	public static final Parcelable.Creator<UpgradeTaskBean> CREATOR 
	= new Parcelable.Creator<UpgradeTaskBean>() {
		public UpgradeTaskBean createFromParcel(Parcel in) {
			UpgradeTaskBean bean = new UpgradeTaskBean();
			bean.result = in.readString();
			bean.resultInfo = in.readString();
			bean.data = in.readParcelable(data.class.getClassLoader());
			return bean;
		}
		public UpgradeTaskBean[] newArray(int size) {
			return new UpgradeTaskBean[size];
		}
	};

}
