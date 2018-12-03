package com.echoss.svc.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendUtils {

	private static final Logger logger = LoggerFactory.getLogger(SendUtils.class);

	public static void sendEmail(String emlAdr, Map<String, String> params, TAData sendInfo) {

		try {
			String sender = sendInfo.getString("SEND_MAIL");
			String subject = getContents(sendInfo.getString("SUBJECT"), params);
			String contents = getContents(sendInfo.getString("CONTENTS"), params);

			SESHandler sesHandler = new SESHandler();
			sesHandler.connect(sendInfo.getString("ACCESS_KEY"), sendInfo.getString("SECRET_KEY"));

			if(StringUtils.equals(sendInfo.getString("DIV_CD"), "H")) {
				sesHandler.sendHtml(sender, subject, contents, emlAdr);
			}
			else {
				sesHandler.sendText(sender, subject, contents, emlAdr);
			}

			if(logger.isInfoEnabled()) {
				logger.info("이메일 발송 완료. sender=[" + sender + "], subject=[" + subject + "], contents=[" + contents + "]");
			}
		} catch(Exception e) {
			logger.error("이메일 발송 처리 중 오류가 발생하였습니다. ", e);
		}
	}

	public static void sendMessage(String ctn, Map<String, String> params, TAData sendInfo) {

		try {
			String sender = sendInfo.getString("SEND_MAIL");	// 발송번호
			String service = sendInfo.getString("SUBJECT");	// 발송서비스
			String ip = sendInfo.getString("ACCESS_KEY");	// 발송서버 IP
			int port = sendInfo.getInt("SECRET_KEY");	// 발송서버 Port

			String contents = getContents(sendInfo.getString("CONTENTS"), params);

			MSGManager mm = new MSGManager(service, "SMS", ip, port);
			mm.send(""+System.currentTimeMillis(), ctn, sender, contents);

			if(logger.isInfoEnabled()) {
				logger.info("메세지 발송 완료. ctn=[" + ctn + "], contents=[" + contents + "]");
			}
		} catch(Exception e) {
			logger.error("메세지 발송 처리 중 오류가 발생하였습니다. ", e);
		}
	}

	private static String getContents(String templateCtnt, Map<String, String> params) {

		String contents = templateCtnt;

		if(params == null || params.isEmpty()) return contents;

		// get parameters
		int paramsCount = StringUtils.countMatches(contents, "${");
		if(paramsCount > 0) {
			List<String> paramNames = new ArrayList<String>();
			int idx = 0;
			for(int i = 0 ; i < paramsCount ; i++) {
				int p1 = contents.substring(idx).indexOf("${");
				int p2 = contents.substring(idx).indexOf("}");
				String paramName = contents.substring(idx + p1 + 2, idx + p2);

				idx += p2 + 1;

				paramNames.add(paramName);
			}

			for(String paramName : paramNames) {
				String paramValue = params.get(paramName);
				contents = StringUtils.replace(contents, "${" + paramName + "}", paramValue);
			}
		}

		return contents;
	}
}
