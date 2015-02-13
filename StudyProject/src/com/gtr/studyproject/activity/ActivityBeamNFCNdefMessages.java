package com.gtr.studyproject.activity;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.framework.common.Mylog;

// Android Beam Event[扫描近常触发]
// 注册为NFC TAG Scanned[配置过滤Intent]
public class ActivityBeamNFCNdefMessages extends BaseFragmentActivity {
	NfcAdapter mNfcAdapter;
	ImageView mImageView;
	TextView mTextView;
	EditText mEditText;

	// NFC NDEFMessage消息通信
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beam_nfc_ndef_message);
		//
		mTextView = (TextView) findViewById(R.id.id_1);
		mEditText = (EditText) findViewById(R.id.id_2);
		mImageView = (ImageView) findViewById(R.id.id_3);
		//
		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
		if (mNfcAdapter == null) {
			toast("抱歉 本机器的 NFC 硬件不支持.");
			finish();
			return;
		}
		// 发送消息的NFC适配器[当触发是回调]
		mNfcAdapter.setNdefPushMessageCallback(new CreateNdefMessageCallback() {
			@Override
			public NdefMessage createNdefMessage(NfcEvent event) {
				// bitmap->byte array
				Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_geo);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				// ByteArray Record [发送指定NDEFMessage的MIMEType,这个消息会触发指定的MIMEType过滤的Activity] : 程序/程序名
				// MIMEType:发送指定程序接收的消息过滤器(application/ : 调用应用程序打开)
				byte[] mimeBytes = "application/com.gtr.studyproject.activity".getBytes(Charset.forName("US-ASCII")); // MIMEType,ByteArray
				NdefRecord byteArrayRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], byteArray);
				// Application Record[Android Application Record (AAR)创建一个APPRecord参数发送到触发源]
				// 目的:当MimeType中声明的程序找不到时,会自动打开程序下载页去下载本应用程序(packageName)
				NdefRecord appRecord = NdefRecord.createApplicationRecord("com.gtr.studyproject.activity");
				String editTextString = mEditText.getText().toString().trim();
				// Text Record
				NdefRecord editTextRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], editTextString
						.getBytes(Charset.forName("UTF-8")));
				// 创建 NDEF Message
				NdefMessage msg = new NdefMessage(new NdefRecord[] { byteArrayRecord, appRecord, editTextRecord });
				//
				Mylog.info("通过近场发送 NDEFMessage 成功...");
				return msg;
			}
		}, this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			// 是由NFC触发进入的 Activity Intent
			Intent intent = getIntent();
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			// only one message sent during the beam
			NdefMessage msg = (NdefMessage) rawMsgs[0]; // 第1个消息
			// record 0 contains the MIME type, record 1 is the AAR
			byte[] bytes = msg.getRecords()[0].getPayload(); // 0:MIME+Image Byte,1:AAR Record,2:EdtiText
			Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			mImageView.setImageBitmap(bmp);
			// 读取AppRecord
			NdefRecord appRecord = msg.getRecords()[1];
			toast("绑定的包名:" + new String(appRecord.getPayload())); // 传入自动下载的packageName
			// 读取字符串
			NdefRecord editRecord = msg.getRecords()[2];
			String edtiString = new String(editRecord.getPayload(), Charset.forName("UTF-8"));
			mEditText.setText(edtiString);
			//
			toast("接收NDEFMessage成功 图片+文本 ...");
			Mylog.info("接收NDEFMessage成功...");
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		setIntent(intent);
	}

	protected NdefRecord getNdefRecordFromUri(Uri uri) {
		return new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI, new byte[0], uri.toString().getBytes());
	}

	protected NdefRecord getEmptyNdefRecord() {
		byte[] empty = new byte[0];
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, empty, empty, empty);
	}

	/**
	 * Creates an NDEF message with a single text record, with language code "en" and the given text, encoded using UTF-8.
	 */
	protected NdefRecord getNdefRecordFromTextEN(String text) {
		byte[] textBytes = text.getBytes(Charset.forName("UTF-8"));
		byte[] textBytesEN = new byte[textBytes.length + 3];
		textBytesEN[0] = 0x002;
		textBytesEN[1] = 'e';
		textBytesEN[2] = 'n';
		System.arraycopy(textBytes, 0, textBytesEN, 3, textBytes.length); // textBytes从0开始拷贝到textBytesEN的3开始,长度为length
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], textBytesEN);
	}

	/**
	 * Creates an NDEF message with a single text record, with the given text content (UTF-8 encoded) and language code.
	 */
	public static NdefMessage fromText(String text, String languageCode) {
		try {
			int languageCodeLength = languageCode.length(); // 国家码
			int textLength = text.length();
			byte[] textPayload = new byte[textLength + 1 + languageCodeLength];
			textPayload[0] = (byte) (0x3F & languageCodeLength);
			System.arraycopy(languageCode.getBytes(), 0, textPayload, 1, languageCodeLength);
			System.arraycopy(text.getBytes(), 0, textPayload, 1 + languageCodeLength, textLength);
			NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], textPayload);
			NdefRecord[] records = new NdefRecord[] { record };
			return new NdefMessage(records);
		} catch (NoClassDefFoundError e) {
			return null;
		}
	}

	NdefRecord createBluetoothAlternateCarrierRecord(boolean activating) {
		byte[] payload = new byte[4];
		payload[0] = (byte) (activating ? 1 : 0);
		payload[1] = 1;
		payload[2] = 'b';
		payload[3] = 0;
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_ALTERNATIVE_CARRIER, null, payload);
	}

}
