package com.gtr.studyproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class ActivityFramelayout extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Window window = getWindow();
		window.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		setContentView(R.layout.layout_framelayout);
	}

}
