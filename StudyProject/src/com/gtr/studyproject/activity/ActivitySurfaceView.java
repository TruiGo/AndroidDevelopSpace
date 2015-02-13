package com.gtr.studyproject.activity;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ActivitySurfaceView extends Activity {
	private Camera camera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_surfaceview);

	}

	@Override
	protected void onResume() {
		super.onResume();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.layout_surface_surfaceview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		camera = Camera.open();
		try {
			camera.setPreviewDisplay(surfaceHolder);
			Parameters ps = camera.getParameters();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (camera != null) {
			camera.release();
			camera = null;
		}

	}

}
