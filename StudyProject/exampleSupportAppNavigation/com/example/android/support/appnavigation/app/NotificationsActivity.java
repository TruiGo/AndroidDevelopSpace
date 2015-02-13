/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.support.appnavigation.app;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;

import com.gtr.studyproject.activity.R;

public class NotificationsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifications);

		ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onPostDirect(View v) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setTicker("Direct Notification");
		builder.setSmallIcon(android.R.drawable.stat_notify_chat);
		builder.setContentTitle("Direct Notification");
		builder.setContentText("This will open the content viewer");
		builder.setAutoCancel(true);
		// TaskStackBuilder
		TaskStackBuilder taskStackBuilder = TaskStackBuilder.from(this);
		taskStackBuilder.addParentStack(ContentViewActivity.class);
		// Intent
		Intent intent = new Intent(this, ContentViewActivity.class);
		intent.putExtra(ContentViewActivity.EXTRA_TEXT, "From Notification");
		taskStackBuilder.addNextIntent(intent);
		// PendingIntent
		PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, 0);
		builder.setContentIntent(pendingIntent);
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.notify("direct_tag", R.id.direct_notification, builder.getNotification());
	}

	public void onPostInterstitial(View v) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setTicker("Interstitial Notification");
		builder.setSmallIcon(android.R.drawable.stat_notify_chat);
		builder.setContentTitle("Interstitial Notification");
		builder.setContentText("This will show a detail page");
		builder.setAutoCancel(true);
		Intent intent = new Intent(this, InterstitialMessageActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		// Pending Intent
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		//
		builder.setContentIntent(pendingIntent);
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.notify("interstitial_tag", R.id.interstitial_notification, builder.getNotification());
	}
}
