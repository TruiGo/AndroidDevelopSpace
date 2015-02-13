package com.gtr.studyproject.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.NameValuePair;
import org.xml.sax.InputSource;

import android.view.inputmethod.InputBinding;

import com.myself.common.Mylog;

public class HttpRequest {
	private static final String DEFAULT_HOST = "http://125cn.net";
	private String host;
	private URL url;
	private URLConnection urlConnection;
	private HttpURLConnection httpUrlConnection;
	private OutputStream outputStream;
	private InputStream inputStream;

	/**
	 * 
	 * @param host
	 *            ÍøÂç·ÃÎÊ¶Ë¿ÚµØÖ·
	 */
	public HttpRequest(String... host) {
		if (host != null && host.length > 0 && host[0] != null)
			this.host = host[0];
	}

	public OutputStream requestGETMethod(String path, List<NameValuePair> params) {
		OutputStream os = null;
		StringBuilder sb = new StringBuilder("?");
		try {
			for (NameValuePair param : params) {
				String string = param.getName() + " : " + param.getValue();
				Mylog.info(string);

				sb.append(param.getName());
				sb.append("=");
				sb.append(param.getValue());
				if (param != params.get(params.size() - 1))
					sb.append("&");
			}

			url = new URL(host != null ? host + path : DEFAULT_HOST + path);
			urlConnection = url.openConnection();
			httpUrlConnection = (HttpURLConnection) urlConnection;
			httpUrlConnection.setReadTimeout(30 * 1000);
			httpUrlConnection.setConnectTimeout(30 * 10000);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setRequestMethod("GET");
			httpUrlConnection.setRequestProperty("connection", "keep-alive");
			httpUrlConnection.setRequestProperty("Charsert", "UTF-8");
			httpUrlConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=utf-8");
			httpUrlConnection.setRequestProperty("Content-Length",
					Integer.toString(sb.toString().trim().getBytes().length));

			outputStream = httpUrlConnection.getOutputStream();
			outputStream.write(sb.toString().getBytes());
			outputStream.flush();

			if (httpUrlConnection.getResponseCode() != 200)
				throw new RuntimeException("Net work error to connecting.");
			inputStream = httpUrlConnection.getInputStream();
			String contentLength = httpUrlConnection
					.getRequestProperty("content-length");
			Mylog.info("contentLength: " + contentLength);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] bbuf = new byte[1024];
			int hasRead = -1;
			while ((hasRead = inputStream.read(bbuf)) != -1) {
				baos.write(bbuf, 0, hasRead);
			}
			Mylog.info(baos.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (outputStream != null)
					outputStream.close();
				if (inputStream != null)
					inputStream.close();
				if (httpUrlConnection != null)
					httpUrlConnection.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return os;
	}
}
