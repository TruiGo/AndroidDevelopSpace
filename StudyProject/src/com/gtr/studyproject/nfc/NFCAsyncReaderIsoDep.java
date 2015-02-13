package com.gtr.studyproject.nfc;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;

import com.xiaotian.framework.common.Mylog;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name NFCAsyncReaderNDEF
 * @description IsoDep 类型读取器
 * @date 2014-5-13
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class NFCAsyncReaderIsoDep extends NFCAsyncReader {

	@Override
	protected String doInBackground(Tag... params) {
		Tag tag = params[0];
		Ndef ndef = Ndef.get(tag);
		if (ndef == null) {
			// 本Tag 不包含 NDEF协议头
			return null;
		}
		NdefMessage ndefMessage = ndef.getCachedNdefMessage();
		NdefRecord[] records = ndefMessage.getRecords();
		for (NdefRecord ndefRecord : records) {
			// TNF
			if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN // TNF-short
					&& Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT) // RTD-Byte[]
			) {
				try {
					return readText(ndefRecord);
				} catch (UnsupportedEncodingException e) {
					// 不支持的语言编码
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		Mylog.info("读取内容完成 Text=" + result);
	}
}
