package com.gtr.studyproject.activity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;

import com.gtr.studyproject.activity.util.SystemUiHider;
import com.gtr.studyproject.nfc.UtilNFC;
import com.xiaotian.framework.activity.BaseActivity;
import com.xiaotian.framework.common.Mylog;
import com.xiaotian.frameworkxt.android.util.UtilVibrator;

/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class ActivityNFC extends BaseActivity {
	// Near Field Communication[NFC Data Exchange Format]近场通信[包含镭射识别]
	// radio frequency identification (RFID)镭射识别
	NfcAdapter adapterNFC;
	NdefMessage[] msgs;
	Intent intent;
	UtilNFC utilNFC;
	UtilVibrator vibrator;

	public ActivityNFC() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
		// 获取 NFC 硬件适配器
		NfcManager manager = (NfcManager) getSystemService(NFC_SERVICE);
		adapterNFC = manager.getDefaultAdapter();
		// adapterNFC = NfcAdapter.getDefaultAdapter(this);
		if (adapterNFC == null) {
			// 不支持 NFC
			toast("This Device does't support NFC.");
			return;
		}
		if (!adapterNFC.isEnabled()) {
			toast("This Device NFC is disabled.");
		} else {
			toast("NFC 设备检查正确,开始NFC之旅吧!");
		}
		// NFC 处理实体
		utilNFC = new UtilNFC(this);
		utilNFC.handlerIntent(getIntent());
		vibrator = UtilVibrator.getInstance(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Mylog.info("On Resume Call Back!");
		intent = getIntent();
		Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
		Mylog.info("Open NFCStyle NFC_EXTRA_TAG:");
		Mylog.infoClassField(tag);

		// 模式进入的Intent[系统检测到硬件时选择打开本应用::同时传入检测到的NFC事件信息]
		// 判断是否以ACTION_NDEF_DISCOVERED
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			Mylog.info("Action is ACTION_NDEF_DISCOVERED");
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			}
		} else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
			Mylog.info("Action is ACTION_TAG_DISCOVERED");

		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
			Mylog.info("Action is ACTION_TAG_DISCOVERED");

		}
		// 开始侦听NFC设备[NFC设备侦听必须在主线程]
		utilNFC.setupForegroundDispatch(adapterNFC);
	}

	@Override
	protected void onPause() {
		// 页面暂停
		utilNFC.stoppForegroundDispatch(adapterNFC); // 停止侦听

		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 执行挂起Pending Intent时该函数被触发
		Mylog.info("onNewIntent");
		vibrator.vibrate(200l);
		utilNFC.handlerIntent(intent);
	}
}
