package com.example.android.basicgesturedetect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.android.common.logger.LogFragment;
import com.gtr.studyproject.activity.R;

public class BasicGestureDetectFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View gestureView = getActivity().findViewById(R.id.sample_output);
		gestureView.setClickable(true);
		gestureView.setFocusable(true);

		// First create the GestureListener that will include all our callbacks.
		// Then create the GestureDetector, which takes that listener as an argument.
		GestureDetector.SimpleOnGestureListener gestureListener = new GestureListener();
		final GestureDetector gd = new GestureDetector(getActivity(), gestureListener);

		/*
		 * For the view where gestures will occur, create an onTouchListener that sends all motion events to the gesture detector. When the gesture detector actually detects an
		 * event, it will use the callbacks you created in the SimpleOnGestureListener to alert your application.
		 */

		gestureView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				gd.onTouchEvent(motionEvent);
				return false;
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.sample_action) {
			clearLog();
		}
		return true;
	}

	public void clearLog() {
		LogFragment logFragment = ((LogFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.log_fragment));
		logFragment.getLogView().setText("");
	}
}
