package com.tools.excel;

import java.io.File;
import java.util.List;

import android.content.Context;

import com.tools.sqlite.SQLiteInfo;
import com.tools.sqlite.SQLiteManager;
import com.tools.util.Log;

/**
 * xls 和 Sqlite 转换
 * 如：将xls导入Sqlite；将Sqlite导出为xls
 * 2014-7-7 下午4:06:22
 * @author MoSQ
 * 
 * 
 * 使用：
 * 1 将数据库表转化为xls表
 * XlsSqlConvert xlsSqlConvert = new XlsSqlConvert(ui);
 * xlsSqlConvert.SqlConvertXls(UserInfo.class, "aaa.xls", Environment.getExternalStorageDirectory()+"/sqlXls");
 * 其中：UserInfo.class，为数据库表名的.class 对象；aaa.xls为要导出的xls表；
 * Environment.getExternalStorageDirectory()+"/sqlXls"为导出路径
 * 
 * 
 */
public class JExcelConvert {
	public static final String TAG = JExcelConvert.class.getSimpleName();
	Context context;
	private static SQLiteManager sqlite;
	
	/**
	 * context, SQLiteRam.getName(), SQLiteRam.getVersion()
	 * @param context 上下文
	 * @param file 数据库文件
	 * @param version 版本号
	 * 2014-7-22 下午3:08:22
	 * @author MoSQ
	 */
	private static void openDB(Context context,File file, int version){
		if (context == null) {
			Log.e(TAG, "context == null");
			return;
		}
		if(file == null){
			Log.e(TAG, "file == null");
			return;
		}
		if (version <= 0) {
			Log.e(TAG, "version <= 0");
			return;
		}
		
		SQLiteInfo sqliteInfo = new SQLiteInfo(file,version);
		SqliteExcelHelper sqliteHelper = new SqliteExcelHelper(context, sqliteInfo.getDBName(), sqliteInfo.getVersion());
		
		sqlite = new SQLiteManager(context, sqliteInfo, sqliteHelper);
		boolean isOpen = sqlite.open();
		Log.e(TAG, "isOpen == "+isOpen);
		
	}
	
	/**
	 * 将db文件的tableName表写入xlsFile文件的sheet表
	 * @param dbFile 数据库文件
	 * @param dbVersion 数据库版本号 要大于0，且要大于等于当前版本（如果不清楚当前版本的话，就填一个大于当前版本的号码）
	 * @param tableName 数据库表，必须是一个类的类名.class
	 * @param xlsFile Excel文件
	 * @param sheet Excel里的表
	 *  @return boolean true:转换成功；false:转换失败
	 * 2014-7-22 下午2:53:38
	 * @author MoSQ
	 */
	public static <T>  boolean Sqlite2Excel(Context context,File dbFile, int dbVersion, Class<T> tableName, File xlsFile, Class<T> sheet) {
		Log.i(TAG,"SqlConvertExcel(Context context,File dbFile, int dbVersion, Class<T> tableName, File xlsFile, Class<T> sheet)");
		if (sheet == null) {
			Log.e(TAG, "sheet == null");
			return false;
		}
		String sheetName = sheet.getSimpleName();

		return Sqlite2Excel(context,dbFile,dbVersion,tableName,xlsFile,sheetName);
	}
	
	/**
	 * 将db文件的tableName表写入xlsFile文件的sheet表
	 * @param context
	 * @param dbFile 数据库文件
	 * @param dbVersion 要大于0，且要大于等于当前版本（如果不清楚当前版本的话，就填一个大于当前版本的号码）
	 * @param tableName  表名，必须是一个类的类名：包名+类名
	 * @param xlsFile excel文件
	 * @param sheet excel 要转换的表名
	 * @return boolean true:转换成功；false:转换失败
	 * 2014-7-22 下午5:03:01
	 * @author MoSQ
	 */
	public static  <T> boolean Sqlite2Excel(Context context,File dbFile, int dbVersion, Class<T> tableName, File xlsFile, String sheet) {
		Log.i(TAG,"SqlConvertExcel(Context context,File dbFile, int dbVersion, String tableName, File xlsFile, String sheet)");
		if (context == null) {
			Log.e(TAG, "context == null");
			return false;
		}
		if (dbFile == null) {
			Log.e(TAG, "dbFile == null");
			return false;
		}
		if (dbVersion <= 0) {
			Log.e(TAG, "dbVersion <= 0");
			return false;
		}
		if (tableName == null) {
			Log.e(TAG, "tableName == null");
			return false;
		}
		if (xlsFile == null) {
			Log.e(TAG, "xlsFile == null");
			return false;
		}
		if (sheet == null) {
			Log.e(TAG, "sheet == null");
			return false;
		}
		
		openDB(context,dbFile,dbVersion);
		
		List<?> list = sqlite.queryAll(tableName);
		
		if (list == null) {
			Log.i(TAG, "list == null");
			return false;
		}
		if (list.size() == 0) {
			Log.i(TAG, "list.size() == 0");
			return false;
		}
		
		String path = xlsFile.getAbsolutePath();
		
		//写入sheetName表
		list2Excel( list, path, sheet);
		return true;
	}
		

	
	/**
	 * 将指定列表写入Excel指定名sheetName表格
	 * @param list 将要写进sheetName的列表
	 * @param path 文件的绝对路径
	 * @param sheetName 表名
	 * 2014-7-22 下午4:06:33
	 * @author MoSQ
	 */
	private static void list2Excel(List<?> list,String path,String sheetName){
		Log.i(TAG, "listToExcel(List<?> list,String path,String sheetName)");
		if (list == null) {
			Log.e(TAG, "list == null");
			return;
		}
		if (list.size() == 0) {
			Log.i(TAG,"list.size() == 0");
			return;
		}
		if (path == null) {
			Log.e(TAG, "path == null");
			return;
		}
		if (sheetName == null) {
			Log.e(TAG, "sheetName == null");
			return;
		}
		
		JExcel jExcel = new JExcel(path);
		boolean isopenSuccess = jExcel.openWrite();
		if (isopenSuccess == false) {
			Log.i(TAG,"isopenSuccess == false");
			return;
		}
		
		jExcel.printList(list);//打印
		
		boolean isSuccuss = jExcel.write(sheetName,list);
		if (isSuccuss) {
			Log.i(TAG,"写入Excel表成功！");
		}

		jExcel.close();//一定要关闭
	}

	
	/**
	 * excel表导入数据库表
	 * @param context
	 * @param xlsPath excel文件名，包括路径
	 * @param sheetName  表名
	 * @param sqlFile 数据库文件，可以是存在的，也可以是不存在的
	 * @param version 数据库版本号，至少要大于0
	 * @param tableName 数据库表名（.class的形式表示）
	 * @return boolean  true:转换成功；false:转换失败
	 * 2014-7-22 下午5:53:12
	 * @author MoSQ
	 */
	public static <T> boolean excel2Sqlite(Context context,String xlsPath,String sheetName,File sqlFile,int version,Class<T> tableName){
		Log.i(TAG,"excelConvertSql(Context context,String xlsName,String sheetName,File sqlFile,int version,Class<T> tableName)");
		if (context == null) {
			Log.i(TAG,"context == null");
			return false;
		}
		if (xlsPath == null) {
			Log.i(TAG,"xlsPath == null");
			return false;
		}
		if (sheetName == null) {
			Log.i(TAG,"sheetName == null");
			return false;
		}
		if (sqlFile == null) {
			Log.i(TAG,"sqlFile == null");
			return false;
		}
		if (version <= 0) {
			Log.i(TAG,"version <= 0");
			return false;
		}
		if (tableName == null) {
			Log.i(TAG,"tableName == null");
			return false;
		}
		
		JExcel jExcel = new JExcel(xlsPath);
		if (!jExcel.openRead()) {
			Log.i(TAG, "!jExcel.openRead()");
			return false;
		}
		List<T> list = jExcel.read(sheetName, tableName);
		
		if (list == null) {
			Log.i(TAG, "list == null");
			return false;
		}
		if (list.size() == 0) {
			Log.i(TAG, "list.size() == 0");
			return false;
		}
		
		jExcel.printList(list);
		
		list.remove(0);//第一行为列名
		jExcel.printList(list);
		
		openDB(context, sqlFile, version);
		sqlite.createTable(tableName);//先创建表
		int size = sqlite.insert(list);
		Log.i(TAG, "size ===="+size);
		sqlite.close();
		return true;
	}
	
	/**
	 * tableName 为全名（包名+类名）
	 */
	private static Class<?> stringToClass(String tableName){
		if (tableName == null) {
			return null;
		}
		
		Class<?> clazz = null;
		try {
			clazz = Class.forName(tableName);
		} catch (ClassNotFoundException e) {
			Log.i(TAG, "是否把类名写全了？包名+加类名");
			e.printStackTrace();
		}
		if (clazz == null) {
			Log.i(TAG, "clazz == null");
			return null;
		}
		
		return clazz;
	}


}
