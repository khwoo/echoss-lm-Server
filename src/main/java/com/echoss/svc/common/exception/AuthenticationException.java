/********************************************************* 
  @프로그램명   :  AuthenticationException.java
  @TRIN ID      :  
  @프로그램목적 :  클라이언트가 인증되지 않은 상태일때 발생되는 예외.
  @적용일시     :  2015.12.04
  @히스토리관리 :
      수정일         변경자       내용
   ------------------------------------------------------
      2015-12-04     공통팀       최초생성
**********************************************************/
package com.echoss.svc.common.exception;

import org.springframework.core.NestedRuntimeException;

public class AuthenticationException extends NestedRuntimeException {

	// 생성자.
	// msg 파라미터에는 상세 내용이 전달된다.
	public AuthenticationException(String msg) {
		super(msg);
	}

}
