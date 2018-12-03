package com.echoss.svc.common.util;

import java.text.MessageFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class PropUtil implements MessageSourceAware {

	private static MessageSource msg;

	public void setMessageSource (MessageSource msg) {
		PropUtil.msg = msg;
	}

	/**
	 * 시스템 속성을 얻기위한 함수
	 * 호출 내용은 기존 메시지와 동일하나 기능적으로 구분하기 위해서 추가한 메소드 이다. 
	 */
	public static String prop(String key) {
		return PropUtil.msg(key, getDefaultLocale());
	}
	public static String prop(String key, HttpServletRequest request) {
		return PropUtil.msg(key, request);
	}

	public static String prop(String key, Locale locale) {
		return PropUtil.msg(key, locale);
	}

	public static String propFormat(String key, HttpServletRequest request, Object...objects) {
		return MessageFormat.format(PropUtil.msg(key, request), objects);
	}

	public static String propFormat(String key, Locale locale, Object...objects) {
		return MessageFormat.format(PropUtil.msg(key, locale),objects);
	}

	/**
	 * 메지시를 리턴한다.
	 */
	 public static String msg(String key, HttpServletRequest request){
		 return msg.getMessage(key, null, getLocale(request));
	 }

	 public static String msg(String key, Locale locale){
		 return msg.getMessage(key, null, locale);
	 }
	 
	 
	 /**
	  * 기본 로케일을 리턴한다. 기본은 한글이다. 
	  */
	 public static Locale getDefaultLocale() {
		 return LocaleContextHolder.getLocale();
	 }

	 public static Locale getLocale(HttpServletRequest request) {
		 Locale locale = null;
		 HttpSession session = request.getSession(); 
		 locale = (Locale)session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);

		 if (locale == null ) {
			 locale = getDefaultLocale();
		 }
		 return locale;
	 }
}