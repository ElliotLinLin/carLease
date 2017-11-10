package com.tools.lang.reflect;

import java.util.HashSet;
import java.util.Set;

import com.tools.util.Log;

/**
 * 单个构造方法
 * 
 * @author LMC
 *
 */
public class ReflectConstructor {

	private static final String TAG = ReflectConstructor.class.getSimpleName();

	private static final boolean D = true;
	
	// 修饰符
	private int modifier = java.lang.reflect.Modifier.PUBLIC;

	// 名称
	private String name = null;

	// 参数
	//	private Object[] parameter = null;

	// 自身构造
	private java.lang.reflect.Constructor<?> constructor = null;

	// 枚举模式
	private ReflectMode declared = ReflectMode.Self;

	// 全部注解(不分解)
	private Set< java.lang.annotation.Annotation > annotations = new HashSet< java.lang.annotation.Annotation >();

	public ReflectConstructor(java.lang.reflect.Constructor<?> constructor) {
		init(constructor, ReflectMode.Self);
	}

	public ReflectConstructor(java.lang.reflect.Constructor<?> constructor, ReflectMode declared) {
		init(constructor, declared);
	}

	/**
	 * 初始化
	 * 
	 * @param constructor
	 */
	private void init(java.lang.reflect.Constructor<?> constructor, ReflectMode declared) {

		this.modifier = constructor.getModifiers();
		this.name = constructor.getName();
		this.constructor = constructor;
		this.declared = declared;

		// +++ 分解注解
		java.lang.annotation.Annotation[] __annotations = null;
		if (this.declared == ReflectMode.Self) {
			__annotations = this.constructor.getDeclaredAnnotations(); // 自身注解
		}else if (this.declared == ReflectMode.Public) {
			__annotations = this.constructor.getAnnotations(); // 自身注解
		}
		if (__annotations != null) {
			for (java.lang.annotation.Annotation anno : __annotations) {
				if (anno != null) {
					getAnnotations().add( anno );
				}
			}
		}

	}

	public java.lang.reflect.Constructor<?> getConstructor() {
		return constructor;
	}

	public Set<java.lang.annotation.Annotation> getAnnotations() {
		return annotations;
	}

	public int getModifier() {
		return modifier;
	}

	public String getName() {
		return name;
	}

	/**
	 * 打印
	 */
	public void print() {
		if(D) Log.i(TAG, "------ ReflectConstructor print() start ------");
		
		if(D) Log.i(TAG, "getName:"+getName());
		if(D) Log.i(TAG, "getModifier:"+getModifier());
		if(D) Log.i(TAG, "Modifier.toString:"+java.lang.reflect.Modifier.toString( getModifier() ));

		if(D) Log.i(TAG, "getAnnotations size:"+this.getAnnotations().size());

		if (this.declared != null) {
			if(D) Log.i(TAG, "declared:"+this.declared.name());
		}
		if(D) Log.i(TAG, "------ ReflectConstructor print() end ------");
	}

}
