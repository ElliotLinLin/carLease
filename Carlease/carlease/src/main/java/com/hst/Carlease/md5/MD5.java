package com.hst.Carlease.md5;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.impl.auth.UnsupportedDigestAlgorithmException;

import android.util.Log;


/**
 * MD5加密
 * @author HL
 *
 */
public class MD5 {
	
	private static final String LOG_TAG = "MD5";
	private static final String ALGORITHM = "MD5";
	private static char sHexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	private static MessageDigest sDigest;

	static {
		try {
			sDigest = MessageDigest.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			Log.e(LOG_TAG, "Get MD5 Digest failed.");
			throw new UnsupportedDigestAlgorithmException(ALGORITHM, e);
		}
	}

	 public static String hexString(byte[] source) {
			if (source == null || source.length <= 0) {
				return "";
			}

			final int size = source.length;
			final char str[] = new char[size * 2];
			int index = 0;
			byte b;
			for (int i = 0; i < size; i++) {
				b = source[i];
				str[index++] = sHexDigits[b >>> 4 & 0xf];
//				str[index++] = sHexDigits[b >> 4];
				str[index++] = sHexDigits[b & 0xf];
			}
			return new String(str);
		}
	 final public static String encode(String source,String key) {
		 	String res=source+key;
		 	
			byte[] btyes;
			
				btyes = res.getBytes();
				sDigest.update(btyes);
				byte[] encodedBytes = sDigest.digest();
				return hexString(encodedBytes);
			
				
			
		
			
		}
	 /*
		 * 转换为md5十六位加密
		 */
		public static String Md5(String plainText) {
			  String result = null;
			  try {
			   MessageDigest md = MessageDigest.getInstance("MD5");
			   md.update(plainText.getBytes());
			   byte b[] = md.digest();
			   int i;
			   StringBuffer buf = new StringBuffer("");
			   for (int offset = 0; offset < b.length; offset++) {
			    i = b[offset];
			    if (i < 0)
			     i += 256;
			    if (i < 16)
			     buf.append("0");
			    buf.append(Integer.toHexString(i));
			   }
			  
			    result = buf.toString().substring(8, 24); //md5 16bit
			   result = buf.toString().substring(8, 24);
			   System.out.println("mdt 16bit: " + buf.toString().substring(8, 24));
			   System.out.println("md5 32bit: " + buf.toString() );
			  } catch (NoSuchAlgorithmException e) {
			   e.printStackTrace();
			  }
			  return result;
			}

}
