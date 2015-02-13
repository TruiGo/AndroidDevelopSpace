package com.example.android.storageclient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.TextView;

import com.example.android.common.activities.SampleActivityBase;
import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;
import com.gtr.studyproject.activity.R;

/**
 * A simple launcher activity containing a summary sample description and a few action bar buttons.
 */
public class StorageClientSample extends SampleActivityBase {

	public static final String TAG = "MainActivity";

	public static final String FRAGTAG = "StorageClientFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_templeate_logwrapper);

		if (getSupportFragmentManager().findFragmentByTag(FRAGTAG) == null) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			StorageClientFragment fragment = new StorageClientFragment();
			transaction.add(fragment, FRAGTAG);
			transaction.commit();
		}
		TextView tv = (TextView) findViewById(R.id.sample_output);
		tv.setText("Using the OPEN_DOCUMENT intent, a client app can access a list of Document Providers on the device, and choose a file from any of them. \n\nTo demonstrate this, click the button below to open up the Storage Access Framework interface, and choose an image on your device.  It will be displayed in this app.");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_log_wrapper, menu);
		return true;
	}

	/** Create a chain of targets that will receive log data */
	@Override
	public void initializeLogging() {
		// Wraps Android's native log framework.
		LogWrapper logWrapper = new LogWrapper();
		// Using Log, front-end to the logging chain, emulates android.util.log method signatures.
		Log.setLogNode(logWrapper);

		// Filter strips out everything except the message text.
		MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
		logWrapper.setNext(msgFilter);

		// On screen logging via a fragment with a TextView.
		LogFragment logFragment = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
		msgFilter.setNext(logFragment.getLogView());
		logFragment.getLogView().setTextAppearance(this, R.style.Log);
		logFragment.getLogView().setBackgroundColor(Color.WHITE);

		Log.i(TAG, "Ready");
	}
}
