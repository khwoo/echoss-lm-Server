/********************************************************* 
  @프로그램명   :  ParameterException.java
  @TRIN ID      :  
  @프로그램목적 :  클라이언트의 요청이 부적절할때 발생시키는 예외
  @적용일시     :  2015.12.04
  @히스토리관리 :
      수정일         변경자       내용
   ------------------------------------------------------
      2015-12-04     공통팀       최초생성
**********************************************************/
package com.echoss.svc.common.exception;

import org.springframework.core.NestedRuntimeException;

public class BadRequestException extends NestedRuntimeException {

	// 생성자.
	// msg 파라미터에는 부적절한 파라미터 정보 등이 전달된다.
	public BadRequestException(String msg) {
		super(msg);
	}

}
