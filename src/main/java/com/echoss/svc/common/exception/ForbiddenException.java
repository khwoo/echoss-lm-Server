/********************************************************* 
  @프로그램명   :  ForbiddenException.java
  @TRIN ID      :  
  @프로그램목적 :  호출이 적절하지 않은 경우 발생시키는 예외
  @적용일시     :  2015.12.04
  @히스토리관리 :
      수정일         변경자       내용
   ------------------------------------------------------
      2015-12-04     공통팀       최초생성
**********************************************************/
package com.echoss.svc.common.exception;

import org.springframework.core.NestedRuntimeException;

public class ForbiddenException extends NestedRuntimeException {

	// 생성자.
	// msg 파라미터에는 오류 내용이 전달된다.
	public ForbiddenException(String msg) {
		super(msg);
	}

}
