package com.tools.lang.reflect;

import java.util.ArrayList;
import java.util.List;

import com.tools.util.Log;

/**
 * 单个属性
 * 
 * 属性有：枚举、对象、基本类型
 * 
 * @author LMC
 *
 */
public class ReflectField {

	private static final String TAG = ReflectField.class.getSimpleName();

	private static final boolean D = true;

	// 修饰符
	private int modifier = java.lang.reflect.Modifier.PRIVATE;

	// 名称
	private String name = null;

	// 类或者类型 TODO 要考虑是否为数组
	private java.lang.reflect.Type type = null;

	// 值
	private Object value = null;

	// 自身
	private java.lang.reflect.Field field = null;

	// 是否为数组
	//	private boolean array = false;

	// 枚举模式
	private ReflectMode declared = ReflectMode.Self;

	// 注释
	private List< java.lang.annotation.Annotation > annotations = new ArrayList< java.lang.annotation.Annotation >();

	// 表示要解析的是clazz还是object
	private boolean isObject = false;
	private Object objectClass = null;

	public ReflectField(java.lang.reflect.Field field, ReflectMode declared) {
		init(field, declared, null);
	}

	public ReflectField(java.lang.reflect.Field field, ReflectMode declared, Object object) {
		init(field, declared, object);
	}

	/**
	 * 初始化
	 * 
	 * @param field
	 * @param object
	 */
	private void init(java.lang.reflect.Field field, ReflectMode declared, Object object) {

		this.field = field;

		this.objectClass = object;

		if (this.objectClass != null) {
			this.isObject = true;
		}

		this.declared = declared;

		this.modifier = this.field.getModifiers();
		this.name = this.field.getName();
		//  返回一个 Type 对象，它表示此 Field 对象所表示字段的声明类型。
		this.type = this.field.getGenericType();
		// 返回一个 Class 对象，它标识了此 Field 对象所表示字段的声明类型。
		//		this.type = this.field.getType();

		// +++ 分解注解
		java.lang.annotation.Annotation[] __annotations = null;
		if (this.declared == ReflectMode.Self) {
			__annotations = this.field.getDeclaredAnnotations(); // 自身注解
		}else if (this.declared == ReflectMode.Public) {
			__annotations = this.field.getAnnotations(); // 共有注解
		}
		if (__annotations != null) {
			for (java.lang.annotation.Annotation anno : __annotations) {
				if (anno != null) {
					getAnnotations().add( anno );
				}
			}
		}

		// 得到值
		if (isObject) {
			this.value = getValue(this.field, this.objectClass);
		}else{
			this.value = getValue(this.field, null);
		}

	}

	/**
	 * 得到值，这里要考虑是clazz还是object，
	 * 要考虑field是否为static或者final等等
	 * 
	 * @param field
	 * @param objectClass
	 * @return
	 */
	private Object getValue(java.lang.reflect.Field field, Object objectClass) {

		Object value = null;
		boolean isObject = false;

		if (objectClass != null) {
			isObject = true;
		}

		if (field != null) {

			// 设置权限
			if ( field.isAccessible() == false ) {
				field.setAccessible(true);
			}

			try {

				// 如果是static属性，则可get(null)
				if ( java.lang.reflect.Modifier.isStatic(field.getModifiers()) ) {
					value = field.get( null );
				} else if (isObject) {
					// 不是static，并且为object传入
					value = field.get( objectClass );
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return value;
	}

	public java.lang.reflect.Type getType() {
		return type;
	}

	public java.lang.reflect.Field getField() {
		return field;
	}

	public List<java.lang.annotation.Annotation> getAnnotations() {
		return annotations;
	}

	public int getModifier() {
		return modifier;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public boolean isAccessible() {
		return this.field.isAccessible();
	}

	/**
	 * 如果此字段表示枚举类型的元素，则返回 true；否则返回 false。
	 * 
	 * @return
	 */
	public boolean isEnumConstant() {
		return this.field.isEnumConstant();
	}

	public boolean isSynthetic() {
		return this.field.isSynthetic();
	}

	/**
	 * 打印
	 */
	public void print() {
		if(D) Log.i(TAG, "------ ReflectField print() start ------");

		if(D) Log.i(TAG, "getName:"+getName());
		if(D) Log.i(TAG, "getModifier:"+getModifier());
		if(D) Log.i(TAG, "Modifier.toString:"+java.lang.reflect.Modifier.toString( getModifier() ));

		if(D) Log.i(TAG, "getType:"+getField().getType().getCanonicalName());
		if(D) Log.i(TAG, "getGenericType:"+getField().getGenericType().toString());

		if(D) Log.i(TAG, "getValue:"+getValue());

		if(D) Log.i(TAG, "isObject:"+this.isObject);

		if(D) Log.i(TAG, "getAnnotations size:"+this.getAnnotations().size());

		if(D) Log.i(TAG, "isAccessible:"+isAccessible());
		if(D) Log.i(TAG, "isEnumConstant:"+isEnumConstant());
		if(D) Log.i(TAG, "isSynthetic:"+isSynthetic());
		
		if (this.declared != null) {
			if(D) Log.i(TAG, "declared:"+this.declared.name());
		}
		if(D) Log.i(TAG, "------ ReflectField print() end ------");
	}

}
