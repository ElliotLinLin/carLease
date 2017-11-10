package com.tools.lang.reflect;

import android.os.Parcel;
import android.os.Parcelable;

import com.tools.sqlite.annotation.Column;
import com.tools.sqlite.annotation.Table;


@Table(name = "TestReflectBeanB__anno")
public class TestReflectBeanB implements android.os.Parcelable, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6712285860009105262L;

	@Column(key = true, auto = true, notNull = true)
	private int number; 

	@Column(notNull = true, nocase = true, lower = true)
	public String address;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(number);
		dest.writeString(address);
	}

	public static final Parcelable.Creator<TestReflectBeanB> CREATOR 
	= new Parcelable.Creator<TestReflectBeanB>() {

		public TestReflectBeanB createFromParcel(Parcel in) {
			TestReflectBeanB bean = new TestReflectBeanB();

			bean.number = in.readInt();
			bean.address = in.readString();

			return bean;
		}   

		public TestReflectBeanB[] newArray(int size) {
			return new TestReflectBeanB[size];
		}

	};
}
