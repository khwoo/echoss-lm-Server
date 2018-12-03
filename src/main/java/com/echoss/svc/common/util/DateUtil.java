/*
 * @(#)DateUtil.java	1.0	2009. 08. 30.
 * 
 * Copyright (c) 2009 TA Networks
 * All rights reserved.
 */
package com.echoss.svc.common.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 공통 Utility
 * Class 설명 : 배치업무에서 사용되는 공통 유틸리티 메소드 정의
 * @version 1.0
 * @since   2009. 03. 13
 * @author  DH.KANG
 */
public class DateUtil {

	public static String formatDateTime(String format) {
		return DateFormatUtils.format(System.currentTimeMillis(), format);
	}
	
	/**
	 * 일자 Format
	 * @param date
	 * @param delimiter
	 * @return
	 */
	public static String formatDate(String date, String delimiter) {
		if(StringUtils.isBlank(date) || date.length() != 8){
			return date;
		}
		
		return StringUtils.substring(date, 0, 4) + delimiter + StringUtils.substring(date, 4, 6) + delimiter + StringUtils.substring(date, 6);
	}
	
	/**
	 * 시간 Format
	 * @param time
	 * @param delimiter
	 * @return
	 */
	public static String formatTime(String time, String delimiter) {
		if(StringUtils.isBlank(time) || time.length() != 6){
			return time;
		}
		
		return StringUtils.substring(time, 0, 2) + delimiter + StringUtils.substring(time, 2, 4) + delimiter + StringUtils.substring(time, 4);
	}
	
	/**
	 * 현재 일자와 시간을 반환한다. (17자리 : yyyyMMddHHmmssSSS)
	 * @return String
	 */
	public static String getCurrentDateTime17() {
		return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
	}

	/**
	 * 현재 일자와 시간을 반환한다. (16자리 : yyyyMMddHHmmssSS)
	 * @return String
	 */
	public static String getCurrentDateTime16() {
		String dateTime = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
		return dateTime.substring(0, 16);
	}

	/**
	 * 현재 일자와 시간을 반환한다. (14자리 : yyyyMMddHHmmss)
	 * @return String
	 */
	public static String getCurrentDateTime() {
		return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss");
	}

	/**
	 * 현재 일자와 시간을 반환한다. (12자리 : yyyyMMddHHmm)
	 * @return String
	 */
	public static String getCurrentDateTime12() {
		return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmm");
	}

	/**
	 * 현재일자를 반환한다. (8자리 : yyyyMMdd)
	 * @return String
	 */
	public static String getCurrentDate() {
		return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd"); 
	}

	/**
	 * 현재일자를 반환한다. (6자리 : yyMMdd)
	 * @return String
	 */
	public static String getCurrentDate6() {
		return DateFormatUtils.format(System.currentTimeMillis(), "yyMMdd"); 
	}

	/**
	 * 현재시간을 반환한다. (6자리 : HHmmss)
	 * @return String
	 */
	public static String getCurrentTime() {
		return DateFormatUtils.format(System.currentTimeMillis(), "HHmmss");
	}

    /**
     * 월,일에 현제 년도를 추가 반환한다. (8자리 : yyyyMMdd)
     * @param mmdd 월일
     * @return String
     */
    public static String getCalYear( String mmdd ) {
        
    	String systemDate = getCurrentDate();
    	return systemDate.substring(0, 4) + mmdd;
    }
    
	/**
	 * 오늘을 기준으로 파라메터로 입력한 값 이후의 일자를 반환한다. (8자리 : yyyyMMdd)
	 * @param day 합산할 일
	 * @return String
	 */
	public static String getDate(int day) {
		GregorianCalendar calandar = new GregorianCalendar();
		calandar.add(Calendar.DAY_OF_YEAR, day);
		return DateFormatUtils.format(calandar.getTime(), "yyyyMMdd");
	}
	
	/**
	 * 입력한 일자를 기준으로 파라메터로 입력한 값 이후의 일자를 반환한다. (8자리 : yyyyMMdd)
	 * @param date 일자, day 합산할 일
	 * @return String
	 */
	public static String addDay(String date, int day) {
		GregorianCalendar calandar = new GregorianCalendar();
		calandar.set(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.toString().substring(4,6))-1, Integer.parseInt(date.toString().substring(6,8)));
		
		calandar.add(Calendar.DAY_OF_YEAR, day);
		return DateFormatUtils.format(calandar.getTime(), "yyyyMMdd");
	}
	
	/**
	 * 입력기준일로 부터 월을 계산하여 계산된 일자를 반환한다. (8자리 : yyyyMMdd)
	 * @param month 합산할 월
	 * @return String
	 */
	public static String addMonth(String date, int month) {
		GregorianCalendar calandar = new GregorianCalendar();

		calandar.set(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.toString().substring(4,6))-1, Integer.parseInt(date.toString().substring(6,8)));
		calandar.add(Calendar.MONTH, month);
		return DateFormatUtils.format(calandar.getTime(), "yyyyMMdd");
	}
	
	/**
	 * 입력기준일로 부터 년을 계산하여 계산된 일자를 반환한다. (8자리 : yyyyMMdd)
	 * @param year 합산할 년
	 * @return String
	 */
	public static String addYear(String date, int year) {
		GregorianCalendar calandar = new GregorianCalendar();

		calandar.set(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.toString().substring(4,6))-1, Integer.parseInt(date.toString().substring(6,8)));
		calandar.add(Calendar.YEAR, year);
		return DateFormatUtils.format(calandar.getTime(), "yyyyMMdd");
	}
	
	/**
	 * 입력한 일자를 기준으로 파라메터로 입력한 값 이후의 시간을 반환한다. (8자리 : HHmm)
	 * @param date 일자, minute 합산할 분
	 * @return String
	 */
	public static String addMinute(String time, int minute) {
		GregorianCalendar calandar = new GregorianCalendar();
		calandar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
		calandar.set(Calendar.MINUTE, Integer.parseInt(time.substring(2)));
		
		calandar.add(Calendar.MINUTE, minute);
		return DateFormatUtils.format(calandar.getTime(), "HHmm");
	}
	
	/**
	 * 입력한 두 날짜의 차이를 반환한다. (8자리 : yyyyMMdd)
	 * @param day 날짜 차이일
	 * @return String
	 */
	public static int getDayDiff(String stDate, String enDate) {
		GregorianCalendar stCalandar = new GregorianCalendar();
		GregorianCalendar enCalandar = new GregorianCalendar();
		stCalandar.set(Integer.parseInt(stDate.substring(0,4)), Integer.parseInt(stDate.toString().substring(4,6))-1, Integer.parseInt(stDate.toString().substring(6,8)));
		enCalandar.set(Integer.parseInt(enDate.substring(0,4)), Integer.parseInt(enDate.toString().substring(4,6))-1, Integer.parseInt(enDate.toString().substring(6,8)));
		
		long diffSec = (enCalandar.getTimeInMillis() - stCalandar.getTimeInMillis())/1000;
		int difDay = (int)diffSec/(60*60*24);
		
		return difDay;
	}
	
	/**
	 * <PRE>
	 * <b>프로그램 설명</b>
	 * 	입력한 두 날짜의 시간차이를 반환한다.(second)
	 * </PRE>
	 * @param stDatetime
	 * @param enDatetime
	 * @return
	 */
	public static long getDatetimeDiff(String stDatetime, String enDatetime) {
		GregorianCalendar stCalandar = new GregorianCalendar();
		GregorianCalendar enCalandar = new GregorianCalendar();
		stCalandar.set(Integer.parseInt(stDatetime.substring(0,4)), Integer.parseInt(stDatetime.toString().substring(4,6))-1, Integer.parseInt(stDatetime.toString().substring(6,8)), Integer.parseInt(stDatetime.toString().substring(8,10)), Integer.parseInt(stDatetime.toString().substring(10,12)), Integer.parseInt(stDatetime.toString().substring(12,14)));
		enCalandar.set(Integer.parseInt(enDatetime.substring(0,4)), Integer.parseInt(enDatetime.toString().substring(4,6))-1, Integer.parseInt(enDatetime.toString().substring(6,8)), Integer.parseInt(enDatetime.toString().substring(8,10)), Integer.parseInt(enDatetime.toString().substring(10,12)), Integer.parseInt(enDatetime.toString().substring(12,14)));
		
		long diffSec = (enCalandar.getTimeInMillis() - stCalandar.getTimeInMillis())/(1000);
		
		return diffSec;
	}
	
	/**
	 * 현재일 기준으로 지난달을 반환한다. (8자리 : yyyyMMdd)
	 * @param 
	 * @return String
	 */
	public static String getBeforeMonth() {
		GregorianCalendar calandar = new GregorianCalendar();
		
		calandar.add(Calendar.MONTH, -1);
		return DateFormatUtils.format(calandar.getTime(), "yyyyMMdd");
	}
	
	/**
	 * 현재일 기준으로 지난달을 반환한다. (8자리 : yyyyMM)
	 * @param date 일자
	 * @return String
	 */
	public static String getBeforeMonth(String date) {
		GregorianCalendar calandar = new GregorianCalendar();
		calandar.set(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.toString().substring(4,6))-1, Integer.parseInt("01"));
		
		calandar.add(Calendar.MONTH, -1);
		return DateFormatUtils.format(calandar.getTime(), "yyyyMM");
	}
	
	/**
	 * 입력한 일자를 기준으로 하루 전 일자를 반환한다. (8자리 : yyyyMMdd)
	 * @param date 일자
	 * @return String
	 */
	public static String getBeforeDay(String date) {
		GregorianCalendar calandar = new GregorianCalendar();
		calandar.set(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.toString().substring(4,6))-1, Integer.parseInt(date.toString().substring(6,8)));
		
		calandar.add(Calendar.DATE, -1);
		return DateFormatUtils.format(calandar.getTime(), "yyyyMMdd");
	}
}
