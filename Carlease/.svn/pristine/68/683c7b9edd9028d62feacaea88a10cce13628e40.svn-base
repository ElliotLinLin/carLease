package com.tools.lang.reflect;

import java.util.ArrayList;
import java.util.List;

import com.tools.util.Log;

/**
 * 反射类数据
 * 
 * 功能：
 * 1.构造函数支持三种：String Object Class<?> 类型
 * 2.支持序列化java.io.Serializable、android.os.Parcelable，即过滤掉无用的属性和方法
 * 3.支持普通类、注释类、接口类、枚举类、抽象类、内部类。
 * 
 * @author LMC
 *
 */
public class ReflectClass  {

	private static final String TAG = ReflectClass.class.getSimpleName();

	private static final boolean D = true;

	// 自身类
	private Class<?> clazz = null;

	// 父类
	private Class<?> superClass = null;

	// 名称
	private String name = null;

	// 修饰符
	private int modifier = java.lang.reflect.Modifier.PUBLIC;

	// 全部属性(分解) 过滤掉序列化的属性
	// public static final android.os.Parcelable.Creator CREATOR
	private static final String Exclude_Parcelable_Field_CREATOR = "CREATOR";
	// private static final long serialVersionUID = 42L;
	private static final String Exclude_Serializable_Field_serialVersionUID = "serialVersionUID";

	// == ReflectMode.Public时会有
	// public static final int CONTENTS_FILE_DESCRIPTOR
	private static final String Exclude_Parcelable_Field_CONTENTS_FILE_DESCRIPTOR = "CONTENTS_FILE_DESCRIPTOR";
	// public static final int PARCELABLE_WRITE_RETURN_VALUE
	private static final String Exclude_Parcelable_Field_PARCELABLE_WRITE_RETURN_VALUE = "PARCELABLE_WRITE_RETURN_VALUE";

	// 属性排除列表
	private List< String > exclede_parcelable_field_list = new ArrayList< String >();
	private List< String > exclede_serializable_field_list = new ArrayList< String >();

	private List< String > exclede_field_list = new ArrayList< String >();

	private List< ReflectField > fields = new ArrayList< ReflectField >();

	// 全部方法(分解) 过滤掉序列化的方法
	// public int describeContents()
	private static final String Exclude_Parcelable_Method_describeContents = "describeContents";
	// public void writeToParcel(android.os.Parcel,int)
	private static final String Exclude_Parcelable_Method_writeToParcel = "writeToParcel";
	// static void access$0(com.tools.lang.reflect.TestReflectBeanA,int)
	// static void access$1(com.tools.lang.reflect.TestReflectBeanA,java.lang.String)
	private static final String Exclude_Method_access = "access$"; // 这个是比较前缀

	private List< ReflectMethod > methods = new ArrayList< ReflectMethod >();

	// 方法排除列表
	private List< String > exclede_parcelable_method_list = new ArrayList< String >();
	private List< String > exclede_method_list = new ArrayList< String >();

	// 全部构造方法(分解)
	//	private Set< ReflectConstructor > constructors = new HashSet< ReflectConstructor >();
	private List< ReflectConstructor > constructors = new ArrayList< ReflectConstructor >();

	// 全部接口(不分解)
	private List< Class<?> > interfaces = new ArrayList< Class<?> >();

	// 全部注解(不分解)
	private List< java.lang.annotation.Annotation > annotations = new ArrayList< java.lang.annotation.Annotation >();

	// java.lang.Object公有方法Public
	private static final String Exclude_Object_Method_equals = "equals";
	private static final String Exclude_Object_Method_getClass = "getClass";
	private static final String Exclude_Object_Method_hashCode = "hashCode";
	private static final String Exclude_Object_Method_notify = "notify";
	private static final String Exclude_Object_Method_notifyAll = "notifyAll";
	private static final String Exclude_Object_Method_toString = "toString";
	private static final String Exclude_Object_Method_wait = "wait";

	// java.lang.Object公有方法Public排除列表
	private List< String > exclede_object_method_list = new ArrayList< String >();

	// 是否实现序列化android.os.Parcelable
	private boolean implParcelable = false;

	// 是否实现序列化java.io.Serializable
	private boolean implSerializable = false;

	// 枚举模式
	private ReflectMode declared = ReflectMode.Self;

	// 表示要解析的是clazz还是object
	private boolean isObject = false;
	private Object objectClass = null;

	public ReflectClass(Class<?> clazz) {
		this( clazz, ReflectMode.Self );
	}

	public ReflectClass(Object object) {
		this( object, ReflectMode.Self );
	}

	public ReflectClass(String classPath) {
		this( classPath, ReflectMode.Self );
	}

	public ReflectClass(Class<?> clazz, ReflectMode declared) {
		init( clazz, declared );
	}

	public ReflectClass(Object object, ReflectMode declared) {
		this.objectClass = object;
		if (this.objectClass != null) {
			this.isObject = true;
		}
		init( object.getClass(), declared );
	}

	public ReflectClass(String classPath, ReflectMode declared) {
		Class<?> c = null;
		try {
			c = Class.forName(classPath);
			init( c, declared );
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化
	 * 
	 * @param classPath
	 */
	private void init(Class<?> clazz, ReflectMode declared) {

		// 属性
		exclede_parcelable_field_list.add(Exclude_Parcelable_Field_CREATOR);
		exclede_parcelable_field_list.add(Exclude_Parcelable_Field_CONTENTS_FILE_DESCRIPTOR);
		exclede_parcelable_field_list.add(Exclude_Parcelable_Field_PARCELABLE_WRITE_RETURN_VALUE);

		exclede_serializable_field_list.add(Exclude_Serializable_Field_serialVersionUID);

		exclede_field_list.addAll(exclede_parcelable_field_list);
		exclede_field_list.addAll(exclede_serializable_field_list);
		Log.e(TAG, "exclede_field_list.size():"+exclede_field_list.size());

		// 方法
		exclede_parcelable_method_list.add(Exclude_Parcelable_Method_describeContents);
		exclede_parcelable_method_list.add(Exclude_Parcelable_Method_writeToParcel);

		exclede_method_list.addAll(exclede_parcelable_method_list);
		Log.e(TAG, "exclede_method_list.size():"+exclede_method_list.size());

		// Object方法
		exclede_object_method_list.add(Exclude_Object_Method_equals);
		exclede_object_method_list.add(Exclude_Object_Method_getClass);
		exclede_object_method_list.add(Exclude_Object_Method_hashCode);
		exclede_object_method_list.add(Exclude_Object_Method_notify);
		exclede_object_method_list.add(Exclude_Object_Method_notifyAll);

		exclede_object_method_list.add(Exclude_Object_Method_toString);
		exclede_object_method_list.add(Exclude_Object_Method_wait);

		Log.e(TAG, "exclede_object_method_list.size():"+exclede_object_method_list.size());

		this.declared = declared;

		try {

			//			this.clazz = Class.forName(classPath);
			this.clazz = clazz;
			if (this.clazz != null) {

				// 名称
				this.name = clazz.getSimpleName();

				// 修饰符
				this.modifier = clazz.getModifiers();

				// 父类
				this.superClass = clazz.getSuperclass();

				// +++ 分解注解
				java.lang.annotation.Annotation[] __annotations = null;
				if (this.declared == ReflectMode.Self) {
					__annotations = this.clazz.getDeclaredAnnotations(); // 自身注解
				}else if (this.declared == ReflectMode.Public || this.declared == ReflectMode.Public_Exclude_Object) {
					__annotations = this.clazz.getAnnotations(); // 共有注解
				}
				if (__annotations != null) {
					for (java.lang.annotation.Annotation anno : __annotations) {
						if (anno != null) {
							getAnnotations().add( anno );
						}
					}
				}

				// +++ 分解接口
				Class<?>[] __interfaces = clazz.getInterfaces(); // 接口没有getDeclaredInterfaces()方法
				if (__interfaces != null) {
					for (Class<?> cls : __interfaces) {
						if (cls != null) {
							// 判断是否实现 了序列化
							if (cls.equals( android.os.Parcelable.class )) {
								this.implParcelable = true;
							} else if (cls.equals( java.io.Serializable.class )) {
								this.implSerializable = true;
							}
							getInterfaces().add( cls );
						}
					}
				}

				// +++ 分解构造
				java.lang.reflect.Constructor<?>[] __constructors = null;
				if (this.declared == ReflectMode.Self) {
					__constructors = this.clazz.getDeclaredConstructors();; // 自身注解
				}else if (this.declared == ReflectMode.Public || this.declared == ReflectMode.Public_Exclude_Object) {
					__constructors = this.clazz.getConstructors();; // 共有注解
				}
				if (__constructors != null) {
					for (java.lang.reflect.Constructor<?> cons : __constructors) {
						if (cons != null) {
							ReflectConstructor reflectConstructor = new ReflectConstructor(cons, this.declared);
							getConstructors().add(reflectConstructor);
						}
					}
				}

				// +++ 分解方法
				java.lang.reflect.Method[] __methods = null;
				if (this.declared == ReflectMode.Self) {
					__methods = this.clazz.getDeclaredMethods();; // 自身注解
				}else if (this.declared == ReflectMode.Public || this.declared == ReflectMode.Public_Exclude_Object) {
					__methods = this.clazz.getMethods();; // 共有注解
				}
				if (__methods != null) {
					for (java.lang.reflect.Method m : __methods) {
						if (m != null) {
							ReflectMethod reflectMethod = new ReflectMethod(m, this.declared);
							// 把不要的方法过滤掉
							if (reflectMethod != null && isExcludeMethod(reflectMethod.getName()) == false) {
								// 不要Object的Public方法
								if (this.declared == ReflectMode.Public_Exclude_Object) {
									if (isExcludeObjectMethod(reflectMethod.getName()) == false) {
										getMethods().add(reflectMethod);
									}
								} else {
									getMethods().add(reflectMethod);
								}
							}
						}
					}
				}

				// +++ 分解属性
				java.lang.reflect.Field[] __fields = null;
				if (this.declared == ReflectMode.Self) {
					__fields = this.clazz.getDeclaredFields();; // 自身注解
				}else if (this.declared == ReflectMode.Public || this.declared == ReflectMode.Public_Exclude_Object) {
					__fields = this.clazz.getFields();; // 共有注解
				}
				if (__fields != null) {
					for (java.lang.reflect.Field f : __fields) {
						if (f != null) {
							ReflectField reflectField = null;
							if (isObject) {
								// 是对象
								reflectField = new ReflectField(f, this.declared, this.objectClass);
							} else {
								// 是clazz
								reflectField = new ReflectField(f, this.declared);
							}
							// 把不要的属性过滤掉
							if (reflectField != null && isExcludeField(reflectField.getName()) == false) {
								getFields().add(reflectField);
							}
						}
					}
				}
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 判断是否为Parcelable的Field (没用到)
	 * 
	 * @param field
	 * @return
	 */
	private boolean isExcludeParcelableField(String field) {
		boolean isExcludeParcelableField = false;
		if (field != null) {
			for ( String value : exclede_parcelable_field_list ) {
				if (value != null && value.equalsIgnoreCase( field )) {
					isExcludeParcelableField = true;
					break;
				}
			}
		}
		if (D) Log.e(TAG, "isExcludeParcelableField:"+isExcludeParcelableField+",field:"+field);
		return isExcludeParcelableField;
	}

	/**
	 * 判断是否为Serializable的Field (没用到)
	 * 
	 * @param method
	 * @return
	 */
	private boolean isExcludeSerializableField(String field) {
		boolean isExcludeParcelableField = false;
		if (field != null) {
			for ( String value : exclede_serializable_field_list ) {
				if (value != null && value.equalsIgnoreCase( field )) {
					isExcludeParcelableField = true;
					break;
				}
			}
		}
		if (D) Log.e(TAG, "isExcludeSerializableField:"+isExcludeParcelableField+",field:"+field);
		return isExcludeParcelableField;
	}

	/**
	 * 是否为排除的属性
	 * 
	 * @return
	 */
	private boolean isExcludeField(String field) {
		boolean isExcludeField = false;
		if (field != null) {
			for ( String value : exclede_field_list ) {
				if (value != null && value.equalsIgnoreCase( field )) {
					isExcludeField = true;
					break;
				}
			}
		}
		if (D) Log.e(TAG, "isExcludeField:"+isExcludeField+",field:"+field);
		return isExcludeField;
	}

	/**
	 * 判断是否为Parcelable的Method (没用到)
	 * 
	 * @param method
	 * @return
	 */
	private boolean isExcludeParcelableMethod(String method) {
		boolean isExcludeParcelableMethod = false;
		if (method != null) {
			for ( String value : exclede_parcelable_method_list ) {
				if (value != null && value.equalsIgnoreCase( method )) {
					isExcludeParcelableMethod = true;
					break;
				}
			}
		}
		if (D) Log.e(TAG, "isExcludeParcelableMethod:"+isExcludeParcelableMethod+",method:"+method);
		return isExcludeParcelableMethod;
	}

	/**
	 * 判断是否为Serializable的Method (没用到)
	 * 
	 * @param method
	 * @return
	 */
	private boolean isExcludeSerializableMethod(String method) {
		return false;
	}

	/**
	 * 是否为排除的方法
	 * 
	 * @return
	 */
	private boolean isExcludeMethod(String method) {
		boolean isExcludeMethod = false;
		if (method != null) {
			for ( String value : exclede_method_list ) {
				if (value != null && value.equalsIgnoreCase( method )) {
					isExcludeMethod = true;
					break;
				} else if (method.startsWith(Exclude_Method_access)) { // 判断前缀字符串
					// 排除Method_Exclude_access方法
					isExcludeMethod = true;
					break;
				}
			}
		}
		if (D) Log.e(TAG, "isExcludeMethod:"+isExcludeMethod+",method:"+method);
		return isExcludeMethod;
	}

	/**
	 * 判断是否为排除Object中的方法
	 * 
	 * @param method
	 * @return
	 */
	private boolean isExcludeObjectMethod(String method) {
		boolean isExcludeObjectMethod = false;
		if (method != null) {
			for ( String value : exclede_object_method_list ) {
				if (value != null && value.equalsIgnoreCase( method )) {
					isExcludeObjectMethod = true;
					break;
				}
			}
		}
		if (D) Log.e(TAG, "isExcludeObjectMethod:"+isExcludeObjectMethod+",method:"+method);
		return isExcludeObjectMethod;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public int getModifier() {
		return modifier;
	}

	public String getName() {
		return name;
	}

	public Class<?> getSuperClass() {
		return superClass;
	}

	public List<java.lang.annotation.Annotation> getAnnotations() {
		return annotations;
	}

	public List<Class<?>> getInterfaces() {
		return interfaces;
	}

	public List<ReflectField> getFields() {
		return fields;
	}

	public List<ReflectMethod> getMethods() {
		return methods;
	}

	public List<ReflectConstructor> getConstructors() {
		return constructors;
	}

	/**
	 * 如果此 Class 对象表示一个注释类型则返回 true。
	 * 此类是否为注释类
	 * 
	 * @return
	 */
	public boolean isAnnotation() {
		return this.clazz.isAnnotation();
	}

	public boolean isAnonymousClass() {
		return this.clazz.isAnonymousClass();
	}

	/**
	 * 判定此 Class 对象是否表示一个数组类。
	 * 
	 * @return
	 */
	public boolean isArray() {
		return this.clazz.isArray();
	}

	public boolean isEnum() {
		return this.clazz.isEnum();
	}

	public boolean isInterface() {
		return this.clazz.isInterface();
	}

	public boolean isLocalClass() {
		return this.clazz.isLocalClass();
	}

	public boolean isMemberClass() {
		return this.clazz.isMemberClass();
	}

	/**
	 * 判定指定的 Class 对象是否表示一个基本类型。
	 * 
	 * @return
	 */
	public boolean isPrimitive() {
		return this.clazz.isPrimitive();
	}

	/**
	 * 如果此类是复合类，则返回 true，否则 false。
	 * 
	 * @return
	 */
	public boolean isSynthetic() {
		return this.clazz.isSynthetic();
	}

	public boolean isImplSerializable() {
		return implSerializable;
	}

	public boolean isImplParcelable() {
		return implParcelable;
	}

	/**
	 * 打印
	 */
	public void print() {

		if(D) Log.i(TAG, "------ ReflectClass print() start ------");

		if (this.getClazz() != null) {
			if(D) Log.i(TAG, "getCanonicalName:"+this.getClazz().getCanonicalName());
			if(D) Log.i(TAG, "toString:"+this.getClazz().toString());
		}
		
		if(D) Log.i(TAG, "getName:"+getName());
		if(D) Log.i(TAG, "getModifier:"+getModifier());
		if(D) Log.i(TAG, "Modifier.toString:"+java.lang.reflect.Modifier.toString( getModifier() ));
		if (this.getSuperClass() != null) {
			if(D) Log.i(TAG, "getSuperClass:"+getSuperClass().getCanonicalName());
		}

		if(D) Log.i(TAG, "getAnnotations size:"+this.getAnnotations().size());
		if(D) Log.i(TAG, "getInterfaces size:"+this.getInterfaces().size());
		if(D) Log.i(TAG, "getConstructors size:"+this.getConstructors().size());
		if(D) Log.i(TAG, "getFields size:"+this.getFields().size());
		if(D) Log.i(TAG, "getMethods size:"+this.getMethods().size());

		if(D) Log.i(TAG, "isAnnotation:"+isAnnotation());
		if(D) Log.i(TAG, "isAnonymousClass:"+isAnonymousClass());
		if(D) Log.i(TAG, "isArray:"+isArray());
		if(D) Log.i(TAG, "isEnum:"+isEnum());
		if(D) Log.i(TAG, "isInterface:"+isInterface());

		if(D) Log.i(TAG, "isLocalClass:"+isLocalClass());
		if(D) Log.i(TAG, "isMemberClass:"+isMemberClass());
		if(D) Log.i(TAG, "isPrimitive:"+isPrimitive());
		if(D) Log.i(TAG, "isSynthetic:"+isSynthetic());
		if(D) Log.i(TAG, "isImplSerializable:"+isImplSerializable());

		if(D) Log.i(TAG, "isImplParcelable:"+isImplParcelable());

		if (this.declared != null) {
			if(D) Log.i(TAG, "declared:"+this.declared.name());
		}

		if(D) Log.i(TAG, "isObject:"+this.isObject);

		if(D) Log.i(TAG, "------ ReflectClass print() end ------");
	}

}
