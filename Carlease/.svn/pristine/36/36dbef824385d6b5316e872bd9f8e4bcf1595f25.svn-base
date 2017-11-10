package com.tools.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.tools.util.Log;

/**
 * xls 读写工具（Excel表读写）
 * excel文件可以包含多个表（sheet）,因此需要指定表名sheetName
 * 
 * 2014-7-7 下午4:06:45
 * @author MoSQ
 * 
 * 使用：
 * 
 * 
 * 1 写内容到指定路径的Excel文件
 * 
 * // path 应该是不存在的文件，如果存在，则openWrite()不成功（因为如果让成功的话，则会覆盖已经存在的文件，可能造成有用数据丢失。）
 * String path = Environment.getExternalStorageDirectory()+"/xxx/ccc.xls";
 * jExcel1 = new JExcel(path);
 * if(jExcel1.openWrite()){
 *  list2 = ......;
 *  //Sheet1 是你指定的表名，list2 是要写入的列表，list2是对象类型，不能是基本数据类型
 * 	jExcel1.write("Sheet1", list2);
 *  jExcel1.close();//当前对象全部写完之后再关闭，否则后面关闭的无效，并且同一个对象关闭多次会报错。
 * }
 * 
 * 
 * 2 读出xls文件的数据
 * 
 * //读取bbb.xls文件的数据
 * String path = Environment.getExternalStorageDirectory()+"/xxx/bbb.xls";//有效路径
 * JExcel jExcel = new JExcel(path);
 * boolean isSuccess = jExcel.openRead();
 *	if (!isSuccess) {
 *		Log.i(TAG, "openRead !isSuccess");
 *		return;
 *	}
 * //读取bbb.xls文件里名为“Sheet1”的表，每一行保存在ReadXlsBean的对象里，然后将所有的ReadXlsBean对象保存在list
 * List<ReadXlsBean> list = jExcel.read("Sheet1", ReadXlsBean.class);
 * 
 */
public class JExcel {

	public static final String TAG = JExcel.class.getSimpleName();

	private WritableWorkbook writableWorkbook;//.xls文件,写入Excel的时候使用这个对象
	private Workbook workbook;//读取Excel文件时，使用这个对象
	private String path;//路径，包括xls文件名

	/**
	 * 构造方法
	 *
	 */
	public JExcel(String path) {
		init(path);
	}


	/**
	 * 初始化路径，包括Excel文件名，以.xls结尾，如 "sheet333.xls"
	 * @param path
	 * 2014-7-16 下午2:52:39
	 * @author MoSQ
	 */
	private void init(String path) {
		this.path = path;
	}
	
	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * (已验证)
	 * 写，要先调用此方法，因为读和写使用的不是一个对象
	 * 写：writableWorkbook；	读：workbook
	 * 创建对象后，如有写的操作，需要先调用此方法
	 * @param password 如果没有密码，则填null
	 * @return
	 * 2014-7-18 上午8:39:49
	 * @author MoSQ
	 */
	public boolean openWrite() {
		Log.e(TAG,"openWrite()");

		if (path == null) {
			Log.exception(TAG, new NullPointerException("path == null"));
			return false;
		}

		//将整个路径创建文件，再通过这个file获得路径和文件名
		File file = new File(path);
		Log.i(TAG, "path file == "+file);
		
		if (file.getName() ==null) {
			Log.e(TAG,"file.getName() == null");
			return false;
		}
		
		//如果没有以.xls结尾，则返回false
//		if (!file.getName().endsWith(".xls")) {
//			Log.i(TAG, "没有以.xls结尾");
//			return false;
//		}
		
		//判断是不是Excel类型（当是自己使用代码创建的Excel文件，判断不出）
		if (!isXlsType(file)) {
			Log.i(TAG, "不是.xls类型数据");
			return false;
		}


		String parent = file.getParent();
		if (parent == null) {
			Log.e(TAG,"parent == null");
			return false;
		}
		File file3 = new File(parent);
		Log.i(TAG, "getPath file3 == "+file3);

		if (!file3.exists()) {
			boolean mkSuccess = file3.mkdirs();
			if (mkSuccess == false) {
				Log.i(TAG, "路径不合法");
				return false;
			}
		}

		//创建文件
		File file2 = new File(parent,file.getName());
		//如果文件不存在，或是这个文件是一个路径，则创建文件
		Log.i(TAG, "file2.isDirectory=="+file2.isDirectory());

		try {
			if (!file2.exists() || file2.isDirectory()) {
				boolean isSuccess = file2.createNewFile();//创建xlsName文件

				Log.i(TAG, "file2 createNewFile isSuccess=="+isSuccess);
				if (isSuccess == false) {
					Log.e(TAG, "create file failed");
					return false;
				}
			}else {
				//如果文件本来就已经存在
				Log.i(TAG, "已经存在文件=="+file2.getAbsolutePath()+",请替换文件名或更改路径");
				return false;
			}
		} catch (IOException e) {
			Log.e(TAG, "路径不合法，创建文件失败");
			e.printStackTrace();
			return false;
		}
		writableWorkbook = createWorkbook(file2);//创建xls文件
		if (writableWorkbook == null) {
			Log.e(TAG, "writableWorkbook == null");
			return false;
		}

		return true;
	}


	/**
	 * (已验证)
	 * 读，要先调用此方法，因为读和写使用的不是一个对象
	 * 写：writableWorkbook；	读：workbook
	 * 创建 对象后，如果只有读的操作，则先调用此方法
	 * @return
	 * 2014-7-22 上午10:55:56
	 * @author MoSQ
	 */
	public boolean openRead(){
		Log.e(TAG,"openRead()");

		if (path == null) {
			Log.exception(TAG, new NullPointerException("path == null"));
			return false;
		}

		File file = new File(path);
		Log.i(TAG, "path file == "+file);
		
		if (file.getName() == null) {
			Log.i(TAG, "file.getName() == null");
			return false;
		}
		
		if (!file.exists()) {
			Log.i(TAG, "!file.exists()");
			return false;
		}
		
//		if (!file.getName().endsWith(".xls")) {
//			Log.i(TAG, "没有以.xls结尾");
//			return false;
//		}
		
		//判断是不是Excel类型
		if (!isXlsType(file)) {
			Log.i(TAG, "不是.xls类型数据");
			return false;
		}
		
		

		try {
			workbook = Workbook.getWorkbook(file);//读取
			Log.i(TAG, "workbook == "+workbook);
			if (workbook == null) {
				Log.i(TAG, "workbook == null");
				return false;
			}
		} catch (BiffException e) {
			e.printStackTrace();
			Log.i(TAG, "BiffException");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			Log.i(TAG, "IOException");
			return false;
		}

		return true;
	}

	private WritableWorkbook createWorkbook(File file) {
		if (file == null) {
			Log.e(TAG, "file == null");
			return null;
		}
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return workbook; 
	}

	/**
	 * 得到可写的Excel文件
	 * @return
	 * 2014-7-25 下午4:34:23
	 * @author MoSQ
	 */
	public WritableWorkbook getWritableWorkbook() {
		return this.writableWorkbook;
	}

	/**
	 * 得到Excel文件（不可写）
	 * @return
	 * 2014-7-25 下午4:33:50
	 * @author MoSQ
	 */
	public Workbook getWorkbook(){
		return this.workbook;
	}

	/**
	 * （已验证）
	 * 读取xls文件
	 * @param sheetName 表名
	 * @param clazz 将要读出来的字段封装成bean类，这个参数就是bean的class文件
	 * 2014-7-8 上午9:15:00
	 * @author MoSQ
	 */
	public <T> List<T> read(String sheetName, Class<T> clazz) {
		Log.i(TAG,"read(String sheetName, Class<T> clazz)");

		if (sheetName == null) {
			Log.e(TAG,"sheetName == null");
			return null;
		}

		if (clazz == null) {
			Log.e(TAG,"clazz == null");
			return null;
		}

		if (workbook == null) {
			Log.e(TAG,"workbook == null");
			return null;
		}

		// getFields()获得某个类的所有的公共（public）的字段，包括父类。 
		// getDeclaredFields()获得某个类的所有申明的字段，即包括public、private和proteced，
		// 但是不包括父类的申明字段。 
		// 得到属性数组
		Field fields[] = clazz.getDeclaredFields();
		Constructor<T> constructor = null;
		try {
			constructor = clazz.getDeclaredConstructor();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		if (fields == null) {
			Log.e(TAG,"fields == null");
			return null;
		}
		
		if (constructor == null) {
			Log.e(TAG,"constructor == null");
			return null;
		}

		List<T> list = new ArrayList<T>();//保存读出来的数据
		int size = workbook.getNumberOfSheets();//表格个数
		Log.i(TAG, "sheet size==="+size);
		Log.i(TAG, "workbook.getSheetNames==="+workbook.getSheetNames());
		
		Sheet sheet = workbook.getSheet(sheetName);//获得表格

		if (sheet == null) {
			Log.e(TAG,"sheet == null");
			return null;
		}

		Log.e(TAG,"---------------读取Excel表----------------");
		Log.e(TAG,"表名为："+sheet.getName());

		//得到列数.可能存在表的列数和要保存的clazz的参数个数不相等的情况，
		//这时候应该以要保存的clazz为准，多出的则不保存。否则会出现数组下标越界异常。所以columns = fields.length;
		//int columns = sheet.getColumns();
		int columns = fields.length;//列数
		int rows = sheet.getRows();//得到行数
		Log.i(TAG,"columns == "+columns+",rows == "+rows);
		if (columns == 0 || rows == 0) {
			Log.e(TAG,"columns == "+columns+",rows == "+rows);
			return null;
		}

		String columnName[] = new String[columns];//保存列名
		T object = null;//构造对象,需要bean类的对象
		//遍历行和列，将数据保存到list
		for (int j = 0; j < rows; j++) {
			
			object = getConstructor(constructor);
			
			for (int k = 0; k < columns; k++) {

				Cell cell = sheet.getCell(k, j);
				if (cell == null) {
					continue;
				}
				String contents = cell.getContents();//得到内容
//				Log.i(TAG, "列="+k+",行="+j+",content="+contents);

				if (contents == null) {
					contents = "";
				}
				//第一行为列名
				if (j == 0) {
					if (fields[k] == null) {
						Log.e(TAG, "fields["+k+"] == null");
						continue;
					}
					
					columnName[k] = contents;//获得列名保存，便于后面进行比较
					//赋值，第一行为列名，直接将列名赋值进去
					try {
						
						fields[k].setAccessible(true);
						fields[k].set(object, fields[k].getName());
						
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}else {
					//将列名和通过反射得到的列名比较，如果相同，则赋值，然后写进对应的object对象，再加入list保存
					for (int l = 0; l < columns; l++) {

						if (fields[l] == null) {
							Log.e(TAG, "fields["+l+"] == null");
							continue;
						}

						if (fields[l].getName() == null) {
							Log.e(TAG, "fields["+l+"].getName() == null");
							continue;
						}
						if (columnName[k] == null) {
							Log.e(TAG, "columnName["+l+"] == null");
							continue;
						}
						
						if (fields[l].getName().equalsIgnoreCase(columnName[k])) {
//							Log.i(TAG, "--------------------");
//							Log.i(TAG, "fields["+l+"]"+fields[l].getName());
//							Log.i(TAG, "columnName["+k+"]"+columnName[k]);
							try {
								
								//这一句一定要写上，否则下面的set设置无效.
								fields[l].setAccessible(true);
								fields[l].set(object, contents);
								break;
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			try {
				list.add(object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//list.size() <= 1，说明只读出列名，并没有读出数据
		if (list.size() <= 1) {
			return null;
		}

		return list;
	}
	
	/*
	 * 获得构造方法
	 */
	private <T> T getConstructor(Constructor<T> constructor){
		T object = null;
		try {
			object = constructor.newInstance();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * （已验证）
	 * 表格是否存在
	 * @param sheetName 表名
	 * 2014-7-16 下午4:00:13
	 * @author MoSQ
	 */
	public boolean isSheetExists(String sheetName) {
		Log.e(TAG, "isSheetExists(String sheetName)");
		
		if (workbook == null) {
			openRead();
			return false;
		}
		if (workbook == null) {
			Log.e(TAG, "workbook == null");
			return false;
		}

		if (sheetName == null) {
			Log.e(TAG, "sheetName == null");
			return false;
		}

		Log.e(TAG, "表名为："+sheetName);

		Sheet sheet = workbook.getSheet(sheetName);
		
		if (sheet == null) {
			Log.e(TAG,"表不存在 sheet == null");
			return false;
		}
		return true;
	}

	/**
	 * (已验证)
	 * 写字符串内容到指定路径的xls文件的指定行列的地方,如果不存在，则先创建
	 * @param content 内容
	 * @param xlsName 文件名
	 * @param column 第几列
	 * @param row 第几行
	 * 2014-7-8 上午9:18:35
	 * @author MoSQ
	 */
	public boolean write(String sheetName,int column,int row,String content){
		Log.e(TAG,"write(String sheetName,int column,int row,String content)");
		if (content == null ) {
			Log.e(TAG,"content == null");
			return false;
		}

		if (isEmptyString(sheetName)) {
			Log.e(TAG,"isEmptyString(sheet)");
			return false;
		}

		if (column < 0 || row < 0) {
			Log.e(TAG,"column < 0 || row < 0 column="+column + ",row="+row);
			return false;
		}

		//字符串类型的单元格
		Label label = new Label(column, row, content);//创建单元格

		boolean isSuccess = writeToCell(sheetName,label);
		if (isSuccess == false) {
			Log.e(TAG,"isSuccess == false");
			return false;
		}
		return true;
	}

	/**
	 * (已验证)
	 * 写double类型内容到指定路径的xls文件的指定行列的地方,如果不存在，则先创建
	 * @param num double类型内容
	 * @param xlsName 文件名
	 * @param column 第几列
	 * @param row 第几行
	 * 2014-7-8 上午9:18:35
	 * @author MoSQ
	 */
	public boolean write(String sheetName, int column, int row, double num){
		Log.e(TAG,"write(String sheetName,int column,int row,double num)");
		
		if (isEmptyString(sheetName)) {
			Log.e(TAG,"isEmptyString(sheet)");
			return false;
		}

		if (column < 0 || row < 0) {
			Log.e(TAG,"column < 0 || row < 0 column="+column + ",row="+row);
			return false;
		}
		//double类型的单元格
		jxl.write.Number number = new jxl.write.Number(column, row, num);
		boolean isSuccess = writeToCell(sheetName,number);
		if (isSuccess == false) {
			Log.e(TAG,"isSuccess == false");
			return false;
		}
		return true;
	}

	/**
	 * (已验证)
	 * 写boolean类型内容到指定路径的xls文件的指定行列的地方,如果不存在，则先创建
	 * @param bool boolean类型内容
	 * @param xlsName 文件名
	 * @param column 第几列
	 * @param row 第几行
	 * 2014-7-8 上午9:18:35
	 * @author MoSQ
	 */
	public boolean write(String sheetName,int column,int row,boolean bool){
		Log.i(TAG,"write(String sheetName,int column,int row,boolean bool)");
		if (isEmptyString(sheetName)) {
			Log.e(TAG,"isEmptyString(sheet)");
			return false;
		}

		if (column < 0 || row < 0) {
			Log.e(TAG,"column < 0 || row < 0 column="+column + ",row="+row);
			return false;
		}

		//boolean类型的单元格
		jxl.write.Boolean boolean1 = new jxl.write.Boolean(column, row, bool);

		boolean isSuccess = writeToCell(sheetName, boolean1);
		if (isSuccess == false) {
			Log.e(TAG,"isSuccess == false");
			return false;
		}
		return true;
	}

	/**
	 * （已验证）
	 * 将list写入名为sheet的表
	 * @param sheet 表名
	 * @param list 列表
	 * @return
	 * 2014-7-16 下午3:58:21
	 * @author MoSQ
	 */
	public <T> boolean write(String sheet, List<T> list){
		Log.e(TAG,"write(String sheet, int column, int row, List<T> list)");
		Log.e(TAG,"-------------------写数据到Excel表-------------------");
		if (isEmptyString(sheet)) {
			Log.e(TAG,"isEmptyString(sheet)");
			return false;
		}

		if (list == null) {
			Log.e(TAG,"list == null");
			return false;
		}
		if (list.size() == 0) {
			Log.e(TAG,"list.size() == 0");
			return false;
		}
		Log.e(TAG,"表名为："+sheet);

		int rows = list.size();//行
		Log.i(TAG,"list.size == rows=="+list.size());

		String columnName = null;//列名
		String value = null;//值

		for (int i = 0; i < rows; i++) {
			Object object2 = list.get(i);
			if (isBasicType(object2)) {
				Log.e(TAG,"i = "+i+",是基本类型数据，需要保存到非数据类型对象才能写入。");
				continue;
			}
			Class<?> class1 = list.get(i).getClass();
			// getFields()获得某个类的所有的公共（public）的字段，包括父类。 
			// getDeclaredFields()获得某个类的所有申明的字段，即包括public、private和proteced，
			// 但是不包括父类的申明字段。 
			// 得到属性数组
			Field fields[] = class1.getDeclaredFields();
			
			if (fields == null ) {
				Log.i(TAG,"fields == "+fields);
				continue;
			}

			int columns = fields.length;//列
			if (fields.length <= 0) {
				Log.i(TAG,"columns == "+columns);
				continue;
			}

			for (int j = 0; j < columns; j++) {
				Field field = fields[j];

				field.setAccessible(true); //设置些属性是可以访问的

				try {
					Object objectValue = field.get(object2);
					if (objectValue == null) {
						continue;	
					} 

					//第一行，存放列名
					if (i == 0) {
						columnName = field.getName();
						if (columnName != null) {
							write(sheet, j, i,columnName);//将列名写入xls
						}
					}
//					else {
//					}
					value = String.valueOf(objectValue);
					write(sheet, j, i+1,value);//将内容写入xls
					columnName = null;
					value = null;//一定要还原

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		Log.e(TAG,"-------------------写完-------------------");
		return true;
	}

	/**
	 * 将cell 写入 sheetName表
	 * cell 支持 数字（double float int long short） Boolean String
	 * @param sheetName
	 * @param cell
	 * @return
	 * 2014-7-22 下午2:03:53
	 * @author MoSQ
	 */
	private boolean writeToCell(String sheetName,WritableCell cell){
		Log.e(TAG,"writeToCell(String sheetName,WritableCell cell)");
		if (isEmptyString(sheetName)) {
			Log.e(TAG,"isEmptyString(sheet)");
			return false;
		}
		if (cell == null) {
			Log.i(TAG,"cell == null");
			return false;
		}

		WritableSheet  sheet = null;
		sheet = getSheet(sheetName);//获得表格
		
		try {
			if (sheet == null) {
				sheet = createSheet(sheetName);
			}
			if (sheet != null) {
				sheet.addCell(cell);
			}else {
				Log.i(TAG,"sheet == null");
				return false;
			}
		} catch (RowsExceededException e) {
			try {
				if (writableWorkbook != null) {
					writableWorkbook.close();
					return false;
				}
			} catch (WriteException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (WriteException e) {
			try {
				if (writableWorkbook != null) {
					writableWorkbook.close();
					return false;
				}
			} catch (WriteException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 关闭,一个对象只能关闭一次。
	 * 读写完数据后关闭
	 * 当写完数据后一定要关闭，否则数据写入无效
	 * 一定要关闭,并且在全部操作完成后再关闭；如果关闭多次，则报错；
	 * 如果还没有完全操作完就关闭，则后面的操作很可能无效。
	 * 2014-7-9 下午2:43:43
	 * @author MoSQ
	 */
	public void close(){
		if (writableWorkbook != null) {
			try {
				writableWorkbook.write();
				writableWorkbook.close();//必须关闭
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (workbook != null) {
			workbook.close();
		}
	}


	/**
	 * (已验证)
	 * 创建表
	 * @param path 路径（包括excel文件名；如果不存在，则创建,要以.xls结尾，如 "sheet1.xls"）
	 * @param sheetNames 表名,可以是多个
	 * @return 返回创建好的表
	 * 2014-7-9 下午2:20:32
	 * @author MoSQ
	 */
	public WritableSheet[] createSheet(String...sheetNames){
		Log.i(TAG,"createSheet(String path,String sheetName)");
		if ( sheetNames == null ) {
			Log.e(TAG,"sheetName == null");
			return null;
		}
		if (sheetNames.length == 0) {
			Log.e(TAG,"sheetNames.length == 0");
			return null;
		}
		
//		if (writableWorkbook == null) {
//			openWrite();
//		}

		if (writableWorkbook == null) {
			Log.e(TAG,"writableWorkbook == null");
			return null;
		}
		int length = sheetNames.length;
		WritableSheet sheet[] = new WritableSheet[length];
		//返回创建好的表
		for (int i = 0; i < length; i++) {
			if (isEmptyString(sheetNames[i])) {
				Log.e(TAG,"isEmptyString(sheetNames["+i+"])");
				continue;
			}
			//如果只是空格，也不应该当表格的名字
			if (isEmptyString(sheetNames[i].trim())) {
				Log.e(TAG,"isEmptyString(sheetName["+i+"].trim())");
				continue;
			}
			sheet[i] = writableWorkbook.createSheet(sheetNames[i], i);//表名，第几个表
		}

		return sheet;
	}

	/**
	 * (已验证)
	 * 创建表
	 * @param sheetName 表名
	 * @param i 第几个表
	 * @return 返回创建好的表
	 * 2014-7-9 下午2:20:32
	 * @author MoSQ
	 */
	public WritableSheet createSheet(String sheetName){
		Log.i(TAG,"createSheet(String path,String sheetName)");
		if (isEmptyString(sheetName)) {
			Log.e(TAG,"isEmptyString(sheetName)");
			return null;
		}
		
		//如果只是空格，也不应该当表格的名字
		if (isEmptyString(sheetName.trim())) {
			Log.e(TAG,"isEmptyString(sheetName.trim())");
			return null;
		}

		if (writableWorkbook == null) {
			Log.e(TAG,"writableWorkbook == null");
			return null;
		}
		WritableSheet sheet = null;
		//返回创建好的表
		sheet = writableWorkbook.createSheet(sheetName, writableWorkbook.getNumberOfSheets()+1);//表名，第几个表(在最后面创建表)
		
		return sheet;
	}

	/**
	 * (已验证)
	 * 删除表格
	 * 本来就已经存在的表格是删除不掉的，只有自己创建的才能删除
	 * @param sheetName 表名
	 * 2014-7-16 下午4:00:40
	 * @author MoSQ
	 */
	public boolean removeSheet(String sheetName){
		Log.i(TAG,"removeSheet(String sheetName)");

		if (isEmptyString(sheetName)) {
			Log.e(TAG,"isEmptyString(sheetName)");
			return false;
		}
		
		if (isEmptyString(sheetName.trim())) {
			Log.e(TAG,"isEmptyString(sheetName)");
			return false;
		}
		
//		if (writableWorkbook == null) {
//			openWrite();//如果为空，则先创建writableWorkbook对象
//		}

		if (writableWorkbook == null) {
			Log.e(TAG,"writableWorkbook == null");
			return false;
		}

		//获得下标
		int index = getIndexFromName(sheetName);

		if (index == -1) {
			Log.e(TAG,"index == -1");
			return false;
		}

		Log.e(TAG,"index == "+index);
		
		writableWorkbook.removeSheet(index);

		WritableSheet sheet = writableWorkbook.getSheet(sheetName);
		if (sheet != null) {
			Log.e(TAG,"sheet != null，没有删除成功");
			return false;
		}
		return true;
	}

	/**
	 * （已验证）
	 * 获得表格的数量
	 * @return
	 * 2014-7-16 下午3:59:09
	 * @author MoSQ
	 */
	public int getNumberOfSheets(){
		if (writableWorkbook == null) {
			Log.e(TAG, "writableWorkbook == null");
			return -1;
		}
		return writableWorkbook.getNumberOfSheets();
	}

	/**
	 * （已验证）
	 * 获得表格
	 * @param sheetName 表名
	 * @return 返回表格对象
	 * 2014-7-16 下午4:07:13
	 * @author MoSQ
	 */
	public WritableSheet getSheet(String sheetName){
		Log.i(TAG, "getSheet(String path,String sheetName)");

		if (isEmptyString(sheetName)) {
			Log.e(TAG, "isEmptyString(sheetName)");
			return null;
		}
		
		if (isEmptyString(sheetName.trim())) {
			Log.e(TAG, "isEmptyString(sheetName.trim())");
			return null;
		}

//		if (writableWorkbook == null) {
//			openWrite();
//			return null;
//		}
		
		if (writableWorkbook == null) {
			Log.e(TAG, "writableWorkbook == null");			
			return null;
		}

		WritableSheet sheet = null;

		sheet =  writableWorkbook.getSheet(sheetName);
		if(sheet == null){
			Log.e(TAG, "sheet == null");
			return null;
		}
		
		SheetSettings settings = sheet.getSettings();

		return sheet;
	}


	/**
	 * （已验证）
	 * 通过sheetName得到sheetIndex索引
	 * @param sheetName
	 * @return
	 * 2014-7-21 上午10:32:07
	 * @author MoSQ
	 */
	public int getIndexFromName(String sheetName) {
		Log.e(TAG,"getIndexFromName(String sheetName) ");

		if (isEmptyString(sheetName)) {
			Log.e(TAG, "isEmptyString(sheetName)");
			return -1;
		}
		
		if (isEmptyString(sheetName.trim())) {
			Log.e(TAG, "isEmptyString(sheetName.trim())");
			return -1;
		}

		if (writableWorkbook == null) {
			Log.e(TAG,"writableWorkbook == null");
			return -1;
		}

		String name[] = writableWorkbook.getSheetNames();

		if (name == null) {
			Log.e(TAG,"name == null");
			return -1;
		}

		for (int i = 0; i < name.length; i++) {
			if (name[i] != null && sheetName.equals(name[i])) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * （已验证）
	 * 通过sheetIndex索引得到sheetName名称
	 * @param sheetIndex
	 * @return
	 * 2014-7-21 上午10:31:58
	 * @author MoSQ
	 */
	public String getNameFromIndex(int sheetIndex) {
		Log.e(TAG,"getNameFromIndex(int sheetIndex) ");

		if (sheetIndex < 0) {
			Log.e(TAG,"sheetIndex < 0");
			return null;
		}

		if (writableWorkbook == null) {
			Log.e(TAG,"writableWorkbook == null");
			return null;
		}
		
		if (sheetIndex >= writableWorkbook.getNumberOfSheets()) {
			Log.e(TAG,"sheetIndex >= writableWorkbook.getNumberOfSheets()");
			return null;
		}

		WritableSheet sheet = writableWorkbook.getSheet(sheetIndex);
		if (sheet == null) {
			Log.e(TAG,"sheet == null");
			return null;
		}

		String sheetName = sheet.getName();

		return sheetName;
	}

	/**
	 * 重命名 (未完)
	 * 2014-7-17 下午5:24:18
	 * @author MoSQ
	 */
	public boolean renameSheet(String oldName,String newName){
		if (oldName == null) {
			Log.e(TAG, "oldName == null");
			return false;
		}

		if (newName == null) {
			Log.e(TAG, "newName == null");
			return false;
		}

		if (writableWorkbook == null) {
			Log.e(TAG, "writableWorkbook == null");
			return false;
		}
//		Sheet sheet = writableWorkbook.getSheet(oldName);
//		SheetSettings settings = sheet.getSettings();

		return true;
	}
	
	/**
	 * 判断是不是基本数据类型
	 * @param object
	 * @return
	 * 2014-7-24 上午10:58:49
	 * @author MoSQ
	 */
	private boolean isBasicType(Object object) {
		if (object == null) {
			return false;
		}
		if (object instanceof Integer) {
			return true;
		} else if (object instanceof Long) {
			return true;
		}else if (object instanceof Short) {
			return true;
		}else if (object instanceof String) {
			return true;
		} else if (object instanceof Character) {
			return true;
		}else if (object instanceof Double) {
			return true;
		} else if (object instanceof Float) {
			return true;
		} else if (object instanceof Boolean) {
			return true;
		}

		return false;
	}
	
	/**
	 * 判断是不是.xls类型（Excel）
	 * 
	 * @param file
	 * @return
	 * 2014-7-29 下午2:53:21
	 * @author MoSQ
	 */
	private boolean isXlsType(File file){
		Log.e(TAG, "isXlsType(File file)");
		if (file == null) {
			Log.e(TAG, "file == null");
			return false;
		}
		
		byte b[] = new byte[100];
		 InputStream is;
		try {
			is = new FileInputStream(file);
			is.read(b);  
			is.close();
			String filetypeHex = String.valueOf(getFileHexString(b));    
			
//			Log.e(TAG, "filetypeHex=="+filetypeHex);
			
			//包括word excel
			if (filetypeHex.toUpperCase().startsWith("D0CF11E0")) {    
				Log.e(TAG, "是word  或是  Excel");
				//还不能区别是word 还是 Excel
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}    
		return false;    
	}
	   
	/**
	 * 得到十六进制字符串
	 * @param b
	 * @return
	 * 2014-7-29 下午3:22:57
	 * @author MoSQ
	 */
	private String getFileHexString(byte[] b)    
	{    
		StringBuilder stringBuilder = new StringBuilder();    
		if (b == null || b.length <= 0)    
		{    
			return null;    
		}    
		for (int i = 0; i < b.length; i++)    
		{    
			int v = b[i] & 0xFF;    
			String hv = Integer.toHexString(v);    
			if (hv.length() < 2)    
			{    
				stringBuilder.append(0);    
			}    
			stringBuilder.append(hv);    
		}    
		return stringBuilder.toString();    
	}


	/**
	 * 打印表格(测试通过)
	 * @param sheetName  表名
	 * 2014-7-16 下午4:12:41
	 * @author MoSQ
	 */
	public void printSheet(String sheetName){
		Log.e(TAG, "---------printSheet--打印表格-------------");
		
		if (sheetName == null) {
			Log.e(TAG, "sheetName == null");
			printEnd();
			return;
		}

		if (workbook == null) {
			openRead();
		}
		
		if (workbook == null) {
			Log.e(TAG,"workbook == null");
			printEnd();
			return ;
		}

		Log.e(TAG, "表名为："+sheetName);

		Sheet sheet = workbook.getSheet(sheetName);
		if (sheet == null) {
			Log.e(TAG,"表不存在 sheet == null");
			printEnd();
			return;
		}

		int columns = sheet.getColumns();//列数
		int rows = sheet.getRows();//行数


		if (columns <= 0 || rows <= 0) {
			Log.e(TAG,"columns == "+columns+",rows == "+rows);
			printEnd();
			return ;
		}
		Log.i(TAG,"columns == "+columns+",rows == "+rows);

		Cell cell = null;
		StringBuffer buffer = null;
		String contents = null;
		for (int i = 0; i < rows; i++) {
			buffer = new StringBuffer();
			buffer.append(i+"__");
			for (int j = 0; j < columns; j++) {
				cell = sheet.getCell(j, i);
				if (cell == null) {
					Log.e(TAG,"cell == null");
					continue;
				}

				contents = cell.getContents();
				if (contents == null) {
					contents = " ";
				}
				buffer.append(cell.getContents()+"__");

			}
			Log.e(TAG, buffer.toString());//打印
		}
		printEnd();
	}
	
	/**
	 * (已验证)
	 * @param list 打印列表，列表保存的是非基本类型,
	 * 不能是基本类型（以下几种不行：int long short float double String char boolean）
	 * 2014-7-23 下午6:16:55
	 * @author MoSQ
	 */
	public void printList(List<?> list){
		Log.e(TAG, "--------printList---------打印list----------------");
		if(list == null){
			Log.e(TAG, "list == null");
			printEnd();
			return ;
		}
		if (list.size() == 0) {
			Log.e(TAG, "list.size() == 0");
			printEnd();
			return;
		}
		int size = list.size();
		Log.e(TAG, "list.size() == "+size);
		StringBuffer buffer = null;
		Object value = null;
		Object object = null;
		for (int i = 0; i < size; i++) {
			object = list.get(i);
			if (object == null) {
				continue;
			}
			
			if (isBasicType(object)) {
				Log.e(TAG, "基本类型数据不能打印");
				continue;
			}
			
			Class<?> clazz = object.getClass();
			if (clazz == null) {
				Log.e(TAG, "i="+i+",clazz == null");
				continue;
			}
			
			Field []fields = clazz.getDeclaredFields();
			if (fields == null) {
				Log.e(TAG, "fields == null");
				continue;
			}
			
			buffer = new StringBuffer();
			buffer.append(i+"__");
			
			int length = fields.length;
			for (int j = 0; j < length; j++) {
				if (fields[j] == null) {
					continue;
				}
				fields[j].setAccessible(true); //设置属性是可以访问的
//				if (i == 0) {
//					value = fields[j].getName();
//				}else {
					try {
						value = fields[j].get(object);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
//				}
				if (value == null) {
					value = "  ";
				}
				buffer.append("__"+value.toString());
				value = null;
			}
			Log.i(TAG, buffer.toString()+"\n");
		}
		
		printEnd();
	}
	
	private void printEnd(){
		Log.e(TAG, "------------------------结束打印---------------------");
	}
}

