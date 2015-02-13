package com.example.android.basicmultitouch;

import android.app.Activity;
import android.os.Bundle;

import com.gtr.studyproject.activity.R;

/**
 * This is an example of keeping track of individual touches across multiple {@link android.view.MotionEvent}s.
 * <p>
 * This is illustrated by a View ({@link TouchDisplayView}) that responds to touch events and draws coloured circles for each pointer, stores the last positions of this pointer and
 * draws them. This example shows the relationship between MotionEvent indices, pointer identifiers and actions.
 * 
 * @see android.view.MotionEvent
 */
public class BasicMultitouchSample extends Activity {
	TouchDisplayView mView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_basic_multitouch_sample);
	}

}
