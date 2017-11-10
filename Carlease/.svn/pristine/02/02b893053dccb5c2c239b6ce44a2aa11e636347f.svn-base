package com.tools.util.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.tools.util.Log;

/**
 * 
 * 压缩类有几种
 * 
 * CheckedInputStream：InputStream派生类，可得到输入流的校验和Checksum，用于校验数据的完整性。
 * 
 * DeflaterInputStream：压缩类的基类。
 * InflaterInputStream：解压缩类的基类。
 * 
 * ZipOutputStream：DeflaterOutputStream的一个子类，把数据压缩成Zip文件格式。
 * ZipInputStream：InflaterInputStream的一个子类，能解压缩Zip格式的数据。
 * 
 * GZIPOutputStream：DeflaterOutputStream的一个子类，把数据压缩成GZip文件格式。
 * GZIPInputStream：InflaterInputStream的一个子类，能解压缩Zip格式的数据。
 * 
 * 
 * zip是将文件打包为zip格式的压缩文件
 * gzip是将文件打包为tar.gz格式的压缩文件
 * 
 * 都是一种文件压缩格式，不过GZIP用在HTTP协议上是一种用来改进WEB应用程序性能的技术。
 * GZIP用在HTTP协议上是一种用来改进WEB应用程序性能的技术，将网页内容压缩后再传输。
 * 
 * @author LMC
 *
 */
public class GZIP {

	private static final String TAG = GZIP.class.getSimpleName();

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 * 
	 * @param src
	 * @return
	 */
	protected static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 压缩
	 * 
	 * @param bytes 字节数组
	 * @return
	 */
	public static byte[] compress(byte[] bytes) {
		Log.d(TAG, "compress()");
		if (bytes == null) {
			return null;
		}

		// 创建一个新的 byte 数组输出流
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			// 使用默认缓冲区大小创建新的输出流
			GZIPOutputStream gzip = new GZIPOutputStream(output);
			// 将 b.length 个字节写入此输出流
			gzip.write( bytes );
			gzip.flush();
			gzip.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		// 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
		return output.toByteArray();
	}

	/**
	 * 压缩
	 * 
	 * @param bytes 字节数组
	 * @param bytesCharset 字节数组的字符集
	 * @param destCharset 将转换成目标字符集
	 * @return
	 */
	public static String compress(byte[] bytes, String bytesCharset, String destCharset) {
		Log.d(TAG, "compress()");
		// 压缩
		byte[] buffer = compress( bytes );
		// 将字节数组转成指定字符集的字节数组
		byte[] tmp = com.tools.os.Charset.convert(buffer, bytesCharset, destCharset);
		// 将字节数组转成对应的字符集字符串
		return com.tools.os.Charset.bytes2String(tmp, destCharset);
	}

	/**
	 * 解压
	 * 
	 * @param bytes 字节数组
	 * @return
	 */
	public static byte[] unCompress(byte[] bytes) {
		Log.d(TAG, "unCompress()");
		if (bytes == null) {
			return null;
		}

		// 创建一个新的 byte 数组输出流
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		// 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
		ByteArrayInputStream input = new ByteArrayInputStream( bytes );
		// 使用默认缓冲区大小创建新的输入流

		try {
			GZIPInputStream gzip = new GZIPInputStream(input);
			byte[] buffer = new byte[ 256 ];
			int n = 0;
			while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
				// 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
				output.write(buffer, 0, n);
				output.flush();
			}
			gzip.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
		return output.toByteArray();
	}

	/**
	 * 解压
	 * 
	 * @param bytes 字节数组
	 * @param bytesCharset 字节数组的字符集
	 * @param destCharset 将转换成目标字符集
	 * @return
	 */
	public static String unCompress(byte[] bytes, String bytesCharset, String destCharset) {
		Log.d(TAG, "unCompress()");
		// 解压
		byte[] buffer = unCompress( bytes );
		// 将字节数组转成指定字符集的字节数组
		byte[] tmp = com.tools.os.Charset.convert(buffer, bytesCharset, destCharset);
		// 将字节数组转成对应的字符集字符串
		return com.tools.os.Charset.bytes2String(tmp, destCharset);
	}

}