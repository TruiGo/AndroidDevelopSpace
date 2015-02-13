package com.gtr.studyproject.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.xiaotian.framework.activity.BaseActivity;
import com.xiaotian.framework.widget.gesture.imageview.GestureImageView;
import com.xiaotian.frameworkxt.net.HttpServerConnector;

/**
 * @version 1.0.0
 * @author XiaoTian
 * @name ActivityGesture
 * @description Gesture Image
 * @date 2014-9-29
 * @link gtrstudio@qq.com
 * @copyright Copyright © 2010-2014 小天天 Studio, All Rights Reserved.
 */
public class ActivityGesture extends BaseActivity {
	GestureImageView gestureImageView;
	HttpServerConnector hsc = new HttpServerConnector();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture);
		gestureImageView = (GestureImageView) findViewById(R.id.GestureImageView);
		gestureImageView.setImageResource(R.drawable.ic_action_play);
		executeAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			@Override
			protected Boolean doInBackground(Void... params) {
				return hsc.downloadUrlToStream("http://www.iyi8.com/uploadfile/2014/0429/20140429115244966.jpg", outputStream);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (!result) return;
				// out -> in
				InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
				gestureImageView.setImageDrawable(drawable);
			}
		});
	}
}
