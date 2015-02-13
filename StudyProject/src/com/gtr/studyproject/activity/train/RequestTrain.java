package com.gtr.studyproject.activity.train;

import java.io.ByteArrayOutputStream;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.xiaotian.framework.net.HttpsServerConnector;
import com.xiaotian.frameworkxt.android.common.Mylog;
import com.xiaotian.frameworkxt.net.HttpAction;
import com.xiaotian.frameworkxt.net.HttpNetworkException;
import com.xiaotian.frameworkxt.net.HttpParam;
import com.xiaotian.frameworkxt.net.HttpServer;

@HttpServer(serverName = "https://kyfw.12306.cn")
public class RequestTrain extends HttpsServerConnector {

	@HttpAction(action = "otn/login/init#", method = "GET")
	public String initSessionParams() throws HttpNetworkException {
		return sendAnnotatedRequest();
	}

	// 邮箱登录
	@HttpAction(action = "otn/login//loginAysnSuggest", method = HttpAction.METHOD_POST)
	public HttpResponse login(String name, String password, String randCode) throws HttpNetworkException {
		List<HttpParam> params = new ArrayList<HttpParam>();
		params.add(new HttpParam("loginUserDTO.user_name", name));
		params.add(new HttpParam("userDTO.password", password));
		params.add(new HttpParam("randCode", randCode));
		params.add(new HttpParam("randCode_validate", ""));
		params.add(new HttpParam("myversion", "undefined"));
		params.add(new HttpParam("Njg4ODY0", "M2YwYjM1N2YwMGFmNTU2Ng=="));
		addCookie(new HttpCookie("current_captcha_type", "C"));
		initAnnotatedURLConnection();
		return HttpResponse.parseResponse(sendAnnotatedRequest(params));
	}

	// 获取验证码
	public ByteArrayOutputStream getPassCodeNew(String module, String randType) throws HttpNetworkException {
		// randType:登录状态下的验证码传入”randp“，非登录传入”sjrand“
		String fileUrl = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=%1$s&rand=%2$s&%3$f";
		try {
			return getFile(String.format(fileUrl, module, randType, Math.random()));
		} catch (HttpNetworkException e) {
			throw e;
		}
	}

	// 校验验证码
	@HttpAction(action = "otn/passcodeNew/checkRandCodeAnsyn", method = HttpAction.METHOD_POST)
	public HttpResponse checkPassCode(String randType, String randCode) throws HttpNetworkException {
		List<HttpParam> params = new ArrayList<HttpParam>();
		params.add(new HttpParam("rand", randType));
		params.add(new HttpParam("randCode", randCode));
		params.add(new HttpParam("randCode_validate", ""));
		initAnnotatedURLConnection();
		return HttpResponse.parseResponse(sendAnnotatedRequest(params));
	}

	// 获取余票

	// 获取我的联系人

	//
	@Override
	public HttpsURLConnection initAnnotatedURLConnection() throws HttpNetworkException {
		HttpsURLConnection connection = super.initAnnotatedURLConnection();
		connection.addRequestProperty("Accept-Language", "zh-cn");
		connection.addRequestProperty("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/600.2.5 (KHTML, like Gecko) Version/7.1.2 Safari/537.85.11");
		Mylog.info("Current Cookies:", getCookies());
		return connection;
	}
}