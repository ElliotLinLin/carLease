package com.tools.lang.reflect;

import java.util.HashSet;
import java.util.Set;

import com.tools.util.Log;

/**
 * 单个方法
 * 
 * @author LMC
 *
 */
public class ReflectMethod {

	private static final String TAG = ReflectMethod.class.getSimpleName();

	private static final boolean D = true;

	// 修饰符
	private int modifier = java.lang.reflect.Modifier.PUBLIC;

	// 类型
	private Class<?> returnType = null;

	// 名称
	private String name = null;

	// 参数
	//	private Object[] parameter = null;

	// 自身
	private java.lang.reflect.Method method = null;

	// 注释
	private Set< java.lang.annotation.Annotation > annotations = new HashSet< java.lang.annotation.Annotation >();

	// 枚举模式
	private ReflectMode declared = ReflectMode.Self;

	public ReflectMethod(java.lang.reflect.Method method) {
		init(method, ReflectMode.Self);
	}

	public ReflectMethod(java.lang.reflect.Method method, ReflectMode declared) {
		init(method, declared);
	}

	/**
	 * 初始化
	 * 
	 * @param method
	 */
	private void init(java.lang.reflect.Method method, ReflectMode declared) {
		this.method = method;
		this.name = method.getName();

		this.returnType = method.getReturnType();
		this.declared = declared;

		this.modifier = this.method.getModifiers();

		// +++ 分解注解
		java.lang.annotation.Annotation[] __annotations = null;
		if (this.declared == ReflectMode.Self) {
			__annotations = this.method.getDeclaredAnnotations(); // 自身注解
		}else if (this.declared == ReflectMode.Public) {
			__annotations = this.method.getAnnotations(); // 共有注解
		}
		if (__annotations != null) {
			for (java.lang.annotation.Annotation anno : __annotations) {
				if (anno != null) {
					getAnnotations().add( anno );
				}
			}
		}

	}

	public String getName() {
		return name;
	}

	public java.lang.reflect.Method getMethod() {
		return method;
	}

	public int getModifier() {
		return modifier;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public Set<java.lang.annotation.Annotation> getAnnotations() {
		return annotations;
	}

	public boolean isBridge() {
		return this.method.isBridge();
	}

	public boolean isAccessible() {
		return this.method.isAccessible();
	}

	public boolean isSynthetic() {
		return this.method.isSynthetic();
	}

	public boolean isVarArgs() {
		return this.method.isVarArgs();
	}

	/**
	 * 打印
	 */
	public void print() {
		if(D) Log.i(TAG, "------ ReflectMethod print() start ------");

		if(D) Log.i(TAG, "getName:"+getName());
		if(D) Log.i(TAG, "getModifier:"+getModifier());
		if(D) Log.i(TAG, "Modifier.toString:"+java.lang.reflect.Modifier.toString( getModifier() ));

		if(D) Log.i(TAG, "getAnnotations size:"+this.getAnnotations().size());

		if(D) Log.i(TAG, "getReturnType:"+getReturnType().getCanonicalName());

		if(D) Log.i(TAG, "isBridge:"+isBridge());
		if(D) Log.i(TAG, "isAccessible:"+isAccessible());
		if(D) Log.i(TAG, "isSynthetic:"+isSynthetic());
		if(D) Log.i(TAG, "isVarArgs:"+isVarArgs());

		if (this.declared != null) {
			if(D) Log.i(TAG, "declared:"+this.declared.name());
		}
		if(D) Log.i(TAG, "------ ReflectMethod print() end ------");
	}

}
