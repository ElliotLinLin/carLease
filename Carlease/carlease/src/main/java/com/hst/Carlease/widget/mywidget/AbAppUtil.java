package com.hst.Carlease.widget.mywidget;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextPaint;
import android.util.DisplayMetrics;

public class AbAppUtil {
	
	
	/**
	 * 描述：判断是否是闰年()
	 * <p>(year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
	 *
	 * @param year 年代（如2012）
	 * @return boolean 是否为闰年
	 */
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	 /**
     * 获取屏幕尺寸与密度.
     *
     * @param context the context
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null){
            mResources = Resources.getSystem();
            
        }else{
            mResources = context.getResources();
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }
    
    /**
     * 描述：获取文字的像素宽.
     *
     * @param str the str
     * @param paint the paint
     * @return the string width
     */
    public static float getStringWidth(String str,TextPaint paint) {
   	 float strWidth  = paint.measureText(str);
        return strWidth;
    }
    
    /**
	  * 描述：标准化日期时间类型的数据，不足两位的补0.
	  *
	  * @param dateTime 预格式的时间字符串，如:2012-3-2 12:2:20
	  * @return String 格式化好的时间字符串，如:2012-03-20 12:02:20
	  */
	public static String dateTimeFormat(String dateTime) {
		StringBuilder sb = new StringBuilder();
		try {
			if(isEmpty(dateTime)){
				return null;
			}
			String[] dateAndTime = dateTime.split(" ");
			if(dateAndTime.length>0){
				  for(String str : dateAndTime){
					if(str.indexOf("-")!=-1){
						String[] date =  str.split("-");
						for(int i=0;i<date.length;i++){
						  String str1 = date[i];
						  sb.append(strFormat2(str1));
						  if(i< date.length-1){
							  sb.append("-");
						  }
						}
					}else if(str.indexOf(":")!=-1){
						sb.append(" ");
						String[] date =  str.split(":");
						for(int i=0;i<date.length;i++){
						  String str1 = date[i];
						  sb.append(strFormat2(str1));
						  if(i< date.length-1){
							  sb.append(":");
						  }
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		return sb.toString();
	}
	
	/**
	  * 描述：不足2个字符的在前面补“0”.
	  *
	  * @param str 指定的字符串
	  * @return 至少2个字符的字符串
	  */
   public static String strFormat2(String str) {
		try {
			if(str.length()<=1){
				str = "0"+str;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return str;
	}
   
   /**
    * 描述：判断一个字符串是否为null或空值.
    *
    * @param str 指定的字符串
    * @return true or false
    */
   public static boolean isEmpty(String str) {
       return str == null || str.trim().length() == 0;
   }

}
