/*
 * @(#)DateUtil.java	1.0	2012. 11. 01
 * 
 * Copyright (c) 2009 TA Networks
 * All rights reserved.
 */
package com.echoss.svc.common.util;

import java.util.Random;

/**
 * <PRE>
 * <b>FileName</b>    : NumberUtil.java
 * <b>프로그램 설명</b>
 * 	 숫자 Format, 계산 등 처리를 위한 메소드 모음
 * <b>작성자</b>      : wblee
 * <b>작성일</b>      : 2012. 11. 7.
 * <b>변경이력</b>
 *                    이름     : 일자          : 근거자료   : 변경내용
 *                   ------------------------------------------------------
 *                    wblee : 2012. 11. 7. :            : 신규 개발.
 * </PRE>
 */
public class NumberUtil {

	/**
	 * 랜덤숫자를 생성한다.
	 * @param length
	 * @return
	 */
	public static String createRandomNumber(int length) {
		Random rd = new Random();
		
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < length; i++) {
			sb.append(rd.nextInt(10));
		}
		
		return sb.toString();
	}
}
