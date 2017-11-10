package com.tools.lang.reflect;

/**
 * 反射处理方式
 * 
 * @author LMC
 *
 */
public enum ReflectMode {
	Self, // 自身的全部，不包括继承来的。
	Public_Exclude_Object, // 全部Public公有的，排除java.lang.Object(已支持)
	Public // Public公有的
	//	All_Exclude_Object, // 全部(类+全部父类)，排除Object TODO 暂不支持
	//	All // 全部(类+全部父类) TODO 暂不支持
}