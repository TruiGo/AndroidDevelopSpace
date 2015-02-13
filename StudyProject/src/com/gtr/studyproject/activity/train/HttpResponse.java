package com.gtr.studyproject.activity.train;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.xiaotian.frameworkxt.android.common.Mylog;
import com.xiaotian.frameworkxt.serializer.json.JSONEntity;

@JSONEntity
public class HttpResponse {
	String validateMessagesShowId;
	Boolean status;
	Integer httpStatus;
	JSONObject jsonObjectData;
	JSONArray jsonArrayMessage;
	JSONObject jsonObjectValidateMessage;
	Data data = new Data();

	public static HttpResponse parseResponse(String jsonString) {
		HttpResponse hr = new HttpResponse();
		if (jsonString == null) {
			hr.status = false;
			hr.data.result = "0";
			hr.data.msg = "网络无数据返回";
		}
		JSONTokener tokener = new JSONTokener(jsonString);
		Object data = null;
		try {
			do {
				data = tokener.nextValue();
				if (data == null) continue;
				if (data instanceof String) {
					Mylog.info("String data");
				} else if (data instanceof Integer) {
					Mylog.info("Integer data");
				} else if (data instanceof Boolean) {
					Mylog.info("Boolean data");
				} else if (data instanceof Long) {
					Mylog.info("Long data");
				} else if (data instanceof Double) {
					Mylog.info("Double data");
				} else if (data instanceof JSONObject) {
					JSONObject json = (JSONObject) data;
					hr = new HttpResponse();
					if (json.has("validateMessagesShowId")) hr.validateMessagesShowId = json.getString("validateMessagesShowId");
					if (json.has("status")) hr.status = json.getBoolean("status");
					if (json.has("httpstatus")) hr.httpStatus = json.getInt("httpstatus");
					try {
						hr.jsonObjectData = json.getJSONObject("data");
						if (hr.jsonObjectData.has("result")) hr.data.result = hr.jsonObjectData.getString("result");
						if (hr.jsonObjectData.has("msg")) hr.data.result = hr.jsonObjectData.getString("msg");
					} catch (Exception ignore) {}
					try {
						hr.jsonObjectValidateMessage = json.getJSONObject("validateMessages");
					} catch (Exception ignore) {}
					try {
						hr.jsonArrayMessage = json.getJSONArray("messages");
					} catch (Exception ignore) {}
				} else if (data instanceof JSONArray) {
					Mylog.info("JSONArray data");
				}
			} while (data == JSONObject.NULL);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return hr;
	}

	class Data {
		String result;
		String msg;
	}
}