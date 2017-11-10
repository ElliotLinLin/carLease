package com.tools.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数字计算的工具类 TODO
 * @author aaa
 *
 */
public class MathUtils {

	/**
	 * 两个数相除得百分比
	 * @param p1  被除数
	 * @param p2   除数
	 * @return
	 */
	public static String percent( double   p1,   double   p2)    {
		String str;
		double   p3   =   p1   /   p2;
		NumberFormat nf   =   NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits( 2 );
		str   =   nf.format(p3);
		return   str;
	}

	/**从字符串中取出所有数字
	 * 如:将时间格式的字符串转换成只有数字的字符串
	 *      2013/11/15 17:04:22 转换成20131115170422
	 * @param strs
	 * @return
	 *@date 2013-11-15 下午4:55:19
	 *@author aaa
	 */
	public static String getNumber(String strs){
		String s="";
		Pattern    p;//用于创建一个正则表达式
		Matcher    m;//匹配器
		p    = Pattern.compile("[0-9]");//模式匹配
		m    = p.matcher(strs);
		while (m.find()){
			String str    = m.group();//取出匹配内容
			s+=str;
		}

		return s;
	}

	/**
	 * 从sList中获取随机元素（相邻不重复） 加入新的列表
	 * @param sList 提供随机元素的列表
	 * @param randomSize 生成的元素个数
	 * @return 包含随机元素的新列表
	 */
	public static List getRandomList(List sList, int randomSize) {
		List list = new ArrayList();
		int t = 0;
		for (int i = 0; i < randomSize; i++) {
			int r = getRandomInt(sList.size());
			while (r == t) {
				//相邻重复 重新获取
				r = getRandomInt(sList.size());
			}
			t = r;
			list.add(sList.get(r));
		}
		return list;
	}

	/**
	 * 获取随机整数
	 * @param max 最大范围
	 * @return 随机整数
	 */
	public static int getRandomInt(int max) {
		return (int) (Math.random() * max);
	}
}
