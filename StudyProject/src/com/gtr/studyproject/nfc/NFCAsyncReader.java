package com.gtr.studyproject.nfc;

import java.io.UnsupportedEncodingException;

import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.os.AsyncTask;

import com.xiaotian.framework.common.Mylog;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name NFCAsyncReaderNDEF
 * @description NFC 数据读取器
 * @date 2014-5-13
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public abstract class NFCAsyncReader extends AsyncTask<Tag, Void, String> {

	@Override
	protected void onPostExecute(String result) {
		Mylog.info("读取内容完成 Text=" + result);
	}

	// bit_7 defines encoding
	// bit_6 reserved for future use, must be 0
	// bit_5..0 length of IANA Language code
	protected String readText(NdefRecord ndefRecord) throws UnsupportedEncodingException {
		// 获取内容Text的二进制码
		byte[] payLoad = ndefRecord.getPayload();
		// 获取内容Text的编码::128位
		String textEncoding = ((payLoad[0] & 128) == 0) ? "UTF-8" : "UTF-16";
		// 获取语言Language码长度
		int languageCodeLength = payLoad[0] & 0063;
		// 获取语言编码
		String languageCode = new String(payLoad, 1, languageCodeLength, "US-ASCII");
		// 获取内容Text
		String text = new String(payLoad, languageCodeLength + 1, payLoad.length - languageCodeLength - 1, textEncoding);
		Mylog.info(languageCode + ":" + text);
		return text;
	}

	/**
	 * Utility class to convert a byte array to a hexadecimal string.
	 * 
	 * @param bytes
	 *            Bytes to convert
	 * @return String, containing hexadecimal representation.
	 */
	public static String ByteArrayToHexString(byte[] bytes) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Utility class to convert a hexadecimal string to a byte string.
	 * <p>
	 * Behavior with input strings containing non-hexadecimal characters is
	 * undefined.
	 * 
	 * @param s
	 *            String containing hexadecimal characters to convert
	 * @return Byte array generated from input
	 */
	public static byte[] HexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
