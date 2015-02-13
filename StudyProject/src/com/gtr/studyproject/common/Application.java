package com.gtr.studyproject.common;

import android.content.Context;

public class Application extends android.app.Application {
	public static Context context;
	public static String packageName;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		context = getApplicationContext();
		packageName = getPackageName();
		
	}

}
