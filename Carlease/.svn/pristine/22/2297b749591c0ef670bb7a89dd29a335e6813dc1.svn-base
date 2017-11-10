package com.tools.lang.reflect;

import android.os.Parcel;
import android.os.Parcelable;

import com.tools.sqlite.annotation.Column;
import com.tools.sqlite.annotation.Table;


@Table(name = "TestReflectBeanA__anno")
public class TestReflectBeanA extends TestReflectBeanB implements android.os.Parcelable, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3791126571090537532L;

	@Column(key = true, auto = true, notNull = true)
	private int id; 

	@Column(notNull = true, nocase = true, lower = true)
	private String myName;

	private String myNameDDDD;

	private static final int sf_number = 32332;

	private final int f_number = 8789454;

	public static int s_number = 675484;

	public TestReflectBeanA() {

	}

	protected TestReflectBeanA(int id, String name) {
		this.id = id;
		this.myName = name;
	}

	public static int getS_number() {
		return s_number;
	}

	public static void setS_number(int s_number) {
		TestReflectBeanA.s_number = s_number;
	}

	public static int getSfNumber() {
		return sf_number;
	}

	public int getF_number() {
		return f_number;
	}

	public String getMyNameDDDD() {
		return myNameDDDD;
	}

	public void setMyNameDDDD(String myNameDDDD) {
		this.myNameDDDD = myNameDDDD;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMyName() {
		return myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(myName);
		dest.writeString(myNameDDDD);
	}

	public static final Parcelable.Creator<TestReflectBeanA> CREATOR 
	= new Parcelable.Creator<TestReflectBeanA>() {

		public TestReflectBeanA createFromParcel(Parcel in) {
			TestReflectBeanA bean = new TestReflectBeanA();

			bean.id = in.readInt();
			bean.myName = in.readString();
			bean.myNameDDDD = in.readString();

			return bean;
		}   

		public TestReflectBeanA[] newArray(int size) {
			return new TestReflectBeanA[size];
		}

	};
}
