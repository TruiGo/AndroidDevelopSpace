package com.myself.common;

import android.util.Log;

public class Mylog {
	public static void info(String message) {
		Mylog.info(DataCommon.TAG, message);
	}

	public static void info(Object message) {
		Mylog.info(DataCommon.TAG, message);
	}
	public static void info(Object[] message, Boolean...newLine) {
		if( newLine.length > 0 && newLine[0] )
			Mylog.info( DataCommon.TAG, message , true );
		else
			Mylog.info( DataCommon.TAG, message , false );
	}

	// Process
	public static void info(String TAG, String message) {
		Log.i(TAG, message == null ? "null" : message);
	}
	
	public static void info(String TAG, Object message) {
		Log.i(TAG, message == null ? "null" : message.toString());
	}

	public static void info(String TAG, String name, String value) {
		Log.i(TAG, name + " : " + value);
	}
	public static void info(String TAG, String name, Object value) {
		Log.i(TAG, name + " : " + (value instanceof String ? (String) value : value.toString()) );
	}

	public static void info(String TAG, Object[] messages, Boolean newLine) {
		StringBuilder sb = null;
		if (messages == null)
			Log.i(TAG, "null");
		else {
			if(newLine){
				Log.i(TAG, "{");
				for (Object message : messages) {
					Log.i(TAG, "\t"+ message instanceof String? (String) message : message.toString());
				}
				Log.i(TAG, "}");
			}else{
				sb = messages.length < 1 ? null : ((sb = new StringBuilder()) != null ? sb.append("[") : null);
				for (Object message : messages) {
					sb.append(message.toString());
					sb.append( messages[messages.length - 1].equals(message) ? "" : "," );
				}
				sb = sb != null ? sb.append("]") : null;
				Log.i(TAG, sb == null ? "null" : sb.toString());
			}
		}

	}
	
	public static void infoShortNewLine(String data){
		int start = 0;
		int length = 40;
		int end = data.length()>length? length : data.length();
		Mylog.info("{");
		Mylog.info("Length: "+data.length());
		while(start < data.length()-1){
			Mylog.info(start+" : "+data.substring(start, end));
			start = end;
			end = (end+length)> data.length()? data.length():(end+length);
		}
		Mylog.info("}");
	}
	
	public static void info_(Object...data){
		 
	}
}
