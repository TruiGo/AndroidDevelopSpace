package com.gtr.studyproject.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaotian.framework.activity.BaseFragmentActivity;
import com.xiaotian.framework.common.Mylog;
import com.xiaotian.framework.widget.VUMeter;
import com.xiaotian.frameworkxt.android.util.UtilEnvironment;
import com.xiaotian.frameworkxt.util.UtilDateTime;
import com.xiaotian.frameworkxt.util.UtilFile;

/**
 * 
 * @version 1.0.0
 * @author mac
 * @name ActivityRadioRecord
 * @description Radio Record
 * @date 2014-10-10
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityRadioRecord extends BaseFragmentActivity {
	List<RecordItem> listData = new ArrayList<RecordItem>();
	String currentRadioFileName;
	MediaRecorder mediaRecorder;
	MediaPlayer mediaPlayer;
	BaseAdapter adapter;
	ListView listView;
	File currentRadioFile;
	RecordButton recordButton;
	boolean recording;
	File externalFile = UtilEnvironment.getExternalStorageDirectory();
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (!recording) return;
			recordButton.setText(recordButton.pressedText);
			recordButton.append(String.valueOf(msg.arg1));
			recordButton.append(" S");
			sendMessageDelayed(obtainMessage(-1, msg.arg1 + 1, -1), 1000);
		}
	};
	DialogFragmentVUMeter dialogVUMeter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radio_record);
		recordButton = (RecordButton) findViewById(R.id.id_0);
		listView = (ListView) findViewById(R.id.id_1);
		recordButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					recordButton.setText(recordButton.pressedText);
					startRecording();
					recording = true;
					handler.sendMessage(handler.obtainMessage());
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					recording = false;
					recordButton.setText(recordButton.normalText);
					stopRecording();
					break;
				}
				return true;
			}
		});
		adapter = new BaseAdapter() {
			UtilDateTime utilDataTime = new UtilDateTime();

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return listData.get(position);
			}

			@Override
			public int getCount() {
				return listData.size();
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					LinearLayout linearLayout = new LinearLayout(ActivityRadioRecord.this);
					linearLayout.setOrientation(LinearLayout.HORIZONTAL);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
					params.weight = 1;
					params.setMargins(0, 50, 0, 0);
					TextView textTitle = new TextView(ActivityRadioRecord.this);
					textTitle.setId(R.id.id_0);
					textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					linearLayout.addView(textTitle, params);
					params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					textTitle = new TextView(ActivityRadioRecord.this);
					textTitle.setId(R.id.id_1);
					textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					linearLayout.addView(textTitle, params);
					LinearLayout root = new LinearLayout(ActivityRadioRecord.this);
					root.setOrientation(LinearLayout.VERTICAL);
					params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					root.addView(linearLayout, params);
					textTitle = new TextView(ActivityRadioRecord.this);
					textTitle.setId(R.id.id_2);
					textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
					params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 0, 0, 50);
					root.addView(textTitle, params);
					convertView = root;
				}
				RecordItem item = (RecordItem) getItem(position);
				TextView textTitle = (TextView) convertView.findViewById(R.id.id_0);
				textTitle.setText(formatDate(item.date));
				textTitle = (TextView) convertView.findViewById(R.id.id_1);
				textTitle.setText(formatSize(item.size));
				textTitle = (TextView) convertView.findViewById(R.id.id_2);
				textTitle.setText(item.path);
				return convertView;
			}

			String formatDate(Long date) {
				return utilDataTime.formatDate("%1$tY%<tm%<td%<tH%<tM%<tS%<tL", date);
			}

			String formatSize(Long size) {
				if (size < 1024 * 1024) {
					return String.format(Locale.CHINA, "%1$d KB", size / 1024);
				} else {
					return String.format(Locale.CHINA, "%1$.1f MB", size / 1024 / 1024.0);
				}
			}
		};
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RecordItem item = listData.get(position);
				currentRadioFileName = item.path;
				onPlay(true);
			}
		});
	}

	void onRecord(boolean start) {
		if (start) {
			startRecording();
		} else {
			stopRecording();
		}
	}

	void startRecording() {
		currentRadioFile = new File(externalFile, UtilFile.getInstance().randomFileName("3pg", 20));
		mediaRecorder = new MediaRecorder();
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setOutputFile(currentRadioFile.getAbsolutePath());
		mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
			@Override
			public void onError(MediaRecorder mr, int what, int extra) {
				Mylog.info("mediaRecorder onError");
			}
		});
		mediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
			@Override
			public void onInfo(MediaRecorder mr, int what, int extra) {
				Mylog.info("mediaRecorder onInfo");
			}
		});
		try {
			mediaRecorder.prepare();
		} catch (IOException e) {
			e.printStackTrace();
			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
			Mylog.info("mediaRecorder.prepare() failed.");
		}
		try {
			mediaRecorder.start();
			showVUMeter();
		} catch (RuntimeException exception) {
			mediaRecorder.reset();
			mediaRecorder.release();
			mediaRecorder = null;
		}
	}

	void stopRecording() {
		if (mediaRecorder == null) return;
		hideVUMeter();
		mediaRecorder.stop();
		mediaRecorder.release();
		mediaRecorder = null;
		if (currentRadioFile == null) return;
		if (currentRadioFile.getTotalSpace() < 3 * 1024) {
			toast("录音时间太短了...");
			currentRadioFile.deleteOnExit();
			return;
		}
		RecordItem item = new RecordItem();
		item.path = currentRadioFile.getAbsolutePath();
		item.size = currentRadioFile.length();
		item.date = currentRadioFile.lastModified();
		listData.add(item);
		adapter.notifyDataSetChanged();
	}

	void showVUMeter() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prefra = getSupportFragmentManager().findFragmentByTag(DialogFragmentVUMeter.TAG);
		if (prefra != null) ft.remove(prefra);
		ft.addToBackStack(null);
		dialogVUMeter = new DialogFragmentVUMeter();
		dialogVUMeter.show(ft, DialogFragmentVUMeter.TAG);
	}

	void hideVUMeter() {
		if (dialogVUMeter != null) dialogVUMeter.dismiss();
	}

	void onPlay(boolean start) {
		if (start) {
			startPlaying();
		} else {
			stopPlaying();
		}
	}

	void startPlaying() {
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(currentRadioFileName);
			mediaPlayer.setLooping(false);
			mediaPlayer.prepare();
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					toast("播放完毕.");
				}
			});
			mediaPlayer.setOnErrorListener(new OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					toast("音频播放遇到错误.");
					return false;
				}
			});
			mediaPlayer.setOnInfoListener(new OnInfoListener() {
				@Override
				public boolean onInfo(MediaPlayer mp, int what, int extra) {
					toast("播放提示: what=" + what + " extra=" + extra);
					return false;
				}
			});
		} catch (IOException e) {
			Mylog.info("mediaPlayer.prepare() failed.");
		}
	}

	void stopPlaying() {
		mediaPlayer.release();
		mediaPlayer = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mediaRecorder != null) {
			mediaRecorder.release();
			mediaRecorder = null;
		}
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	class RecordItem {
		Long date;
		Long size;
		String path;
	}

	public static class RecordButton extends Button {
		String normalText;
		String pressedText;

		public RecordButton(Context context, AttributeSet attrs) {
			super(context, attrs);
			normalText = attrs.getAttributeValue("http://schemas.framework.xiaotian.com/android", "normalText");
			pressedText = attrs.getAttributeValue("http://schemas.framework.xiaotian.com/android", "pressedText");
			if (normalText == null) normalText = "Press Record";
			if (pressedText == null) pressedText = "Recording...";
			setText(normalText);
		}
	}

	class DialogFragmentVUMeter extends DialogFragment {
		public static final String TAG = "DialogFragmentVUMeter";
		VUMeter vuMeter;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setStyle(STYLE_NO_FRAME, R.style.style_dialog_theme_xiaotian);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.dialog_activity_radiorecord_vumeter, container, false);
			vuMeter = (VUMeter) v.findViewById(R.id.id_0);
			vuMeter.setRecorder(mediaRecorder);
			return v;
		}
	}
}
