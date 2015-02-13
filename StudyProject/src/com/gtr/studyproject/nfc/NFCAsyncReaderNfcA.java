package com.gtr.studyproject.nfc;

import java.io.IOException;

import android.nfc.Tag;
import android.nfc.tech.NfcA;

import com.xiaotian.framework.common.Mylog;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name NFCAsyncReaderNDEF
 * @description NfcA 类型读取器
 * @date 2014-5-13
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class NFCAsyncReaderNfcA extends NFCAsyncReader {

	@Override
	protected String doInBackground(Tag... params) {
		Tag tag = params[0];
		NfcA nfca = NfcA.get(tag); // 从TAG获取NfcA对象
		if (nfca == null) {
			// 本Tag 不包含 NDEF协议头
			return null;
		}
		String id = ByteArrayToHexString(tag.getId());
		String atqa = ByteArrayToHexString(nfca.getAtqa());
		int transceiveLength = nfca.getMaxTransceiveLength();
		int sak = nfca.getSak();
		// 获取NFC硬件配置信息
		Mylog.info("硬件 id=" + id);
		Mylog.info("硬件 ATQA/SENS 数据=" + atqa);
		Mylog.info("硬件 SAK/SEL_RES 字节=" + sak);
		Mylog.info("硬件一次最大指令长度(byte)MaxTransceiveLength=" + transceiveLength);
		//
		byte[] commands;
		byte[] readedData;
		try {
			nfca.connect(); // 建立连接
			nfca.setTimeout(5 * 1000);// 连接超时
			
			// byte[]类型的指令集
			//commands = new byte[] { (byte) 0xAA, 00, (byte) 0x01, (byte) 0x86, (byte) 0x87, (byte) 0xBB };
			//readedData = nfca.transceive(commands); // 发送执行指令[指令有限制长度]

			//Mylog.info(ByteArrayToHexString(readedData));
			//Mylog.info(nfca.isConnected());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				nfca.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		Mylog.info("读取内容完成 Text=" + result);
	}
}
