package com.gtr.studyproject.nfc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;

import com.xiaotian.framework.common.Mylog;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name UtilNFC
 * @description NFC Util
 * @date 2014-5-12
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class UtilNFC {
	public static final String MIME_TEXT_PLAIN = "text/plain";
	Activity activity;
	Intent intent;

	public UtilNFC(Activity activity) {
		this.activity = activity;
	}

	// 处理检测到Intent包含NFC的数据
	public void handlerIntent(Intent intent) {
		Mylog.info("UtilNFC handlerIntent Called!");
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			// 检测到的NFC触发类型是 NDEF的类型
			Mylog.info("UtilNFC Action ACTION_NDEF_DISCOVERED!");
			readTechInformation((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)); // 取得可序列化的参数
		} else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			// 检测到的NFC触发类型是 TAG的类型
			Mylog.info("UtilNFC Action ACTION_TAG_DISCOVERED!");
			readTechInformation((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)); // 取得可序列化的参数
		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			// 检测到的NFC触发类型是 TECH的类型
			Mylog.info("UtilNFC Action ACTION_TECH_DISCOVERED!");
			readTechInformation((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)); // 取得可序列化的参数
		}
	}

	// 设置侦听适配:: 前台调度[控制系统前台NFC发现的调度器,不让弹出选择发现NFC选择对话框]
	public void setupForegroundDispatch(NfcAdapter adapterNFC) {
		Mylog.info("setupForegroundDispatch");
		// 创建Intent,该Intent的目标是当前打开的Activity,当执行该Intent是触发Activity的onNewIntent方法,并传入当前配置的Intent
		intent = new Intent(activity.getApplicationContext(), activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// 创建挂起Intent,暂时不执行Intent,当发现NFC硬件时执行Intent
		PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
		// 创建Intent 过滤器(选择器)
		IntentFilter[] filters = new IntentFilter[1];
		// 创建NFC过滤选择器的可选择硬件类型::Tech的类名[类别]
		String[][] techLists = new String[][] { { IsoDep.class.getName() }, { NfcA.class.getName() },
				{ NfcB.class.getName() }, { NfcF.class.getName() }, { NfcV.class.getName() }, { Ndef.class.getName() },
				{ NdefFormatable.class.getName() }, { MifareClassic.class.getName() },
				{ MifareUltralight.class.getName() } };
		// 加载声明的过滤器
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
		filters[0].addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			e.printStackTrace();
		}
		// 开启PendingIntent的前台侦听,当检测NFC并符合绑定的过滤条件时触发挂起的Intent[onStop->onNewIntent->OnResume]
		adapterNFC.enableForegroundDispatch(activity, pendingIntent, filters, techLists);
	}

	// 取消侦听适配
	public void stoppForegroundDispatch(NfcAdapter adapterNFC) {
		Mylog.info("stoppForegroundDispatch");
		adapterNFC.disableForegroundDispatch(activity);
	}

	public void readTechInformation(Tag nfcExtraTag) {
		String[] techList = nfcExtraTag.getTechList(); // 获取支持的技术列表
		for (String tech : techList) {
			Mylog.info("Found Tag Tech :" + tech);
			if (IsoDep.class.getName().equals(tech)) {
				// IsoDep 硬件

			} else if (NfcA.class.getName().equals(tech)) {
				// NfcA 硬件
				new NFCAsyncReaderNfcA().execute(nfcExtraTag);

			} else if (NfcB.class.getName().equals(tech)) {
				// NfcB 硬件

			} else if (Ndef.class.getName().equals(tech)) {
				// Ndef 硬件
				new NFCAsyncReaderNdef().execute(nfcExtraTag);
				break;

			} else if (NfcF.class.getName().equals(tech)) {
				// NfcF 硬件

			} else if (NfcV.class.getName().equals(tech)) {
				// NfcV 硬件

			} else if (NdefFormatable.class.getName().equals(tech)) {
				// NdefFormatable 硬件

			} else if (MifareClassic.class.getName().equals(tech)) {
				// MifareClassic 硬件

			} else if (MifareUltralight.class.getName().equals(tech)) {
				// MifareUltralight 硬件

			}
		}
	}
}
