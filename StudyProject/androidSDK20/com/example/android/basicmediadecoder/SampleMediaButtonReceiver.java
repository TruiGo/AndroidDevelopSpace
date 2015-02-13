package com.example.android.basicmediadecoder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

/**
 * Broadcast receiver for handling ACTION_MEDIA_BUTTON.
 * 
 * This is needed to create the RemoteControlClient for controlling remote route volume in lock screen. It routes media key events back to main app activity MainActivity.
 */
public class SampleMediaButtonReceiver extends BroadcastReceiver {
	private static final String TAG = "SampleMediaButtonReceiver";
	private static MediaRouterSample mActivity;

	public static void setActivity(MediaRouterSample activity) {
		mActivity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (mActivity != null && Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
			mActivity.handleMediaKey((KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT));
		}
	}
}
