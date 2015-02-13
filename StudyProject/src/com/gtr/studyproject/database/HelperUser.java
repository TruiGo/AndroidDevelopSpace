package com.gtr.studyproject.database;

import com.myself.common.Mylog;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HelperUser {
	private static final String DATABASE_NAME = "studyproject.db"; // 数据库文件名
	private static final int VERSION = 1;

	private DataBaseUser database;
	private SQLiteDatabase db;
	
	private Context context;

	public HelperUser(Context context) {
		this.context = context;
		database = new DataBaseUser(context, DATABASE_NAME, null, VERSION);
		db = database.getWritableDatabase(); // reading and writing
	}
	
	/**
	 * 查询所有用户信息
	 */
	public void queryAllUser(){
		Cursor cursor = db.query(DataBaseUser.TABLE_ANME, null, null, null, null, null, null);
		int indexName = cursor.getColumnIndex(DataBaseUser.USER_ANME);
		int indexPassword = cursor.getColumnIndex(DataBaseUser.USER_PASSWORD);
		int indexId = cursor.getColumnIndex(DataBaseUser.USER_ID);
		int indexAccount = cursor.getColumnIndex(DataBaseUser.USER_SIGN_ACCOUNT);
		while(cursor.moveToNext()){
			String name = cursor.getString(indexName);
			String password = cursor.getString(indexPassword);
			String id = cursor.getString(indexId);
			int account = cursor.getInt(indexAccount);
			Mylog.info("name: "+name+" password: "+password+" id: "+id+" account: "+account);
		}
	}
}
