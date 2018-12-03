/********************************************************* 
  @프로그램명   :  CommonException.java
  @TRIN ID      :  
  @프로그램목적 :  Action 및 기타 Layer 에서의 예외처리 기본 핸들러
  @적용일시     :  2008.06.15
  @히스토리관리 :
      수정일         변경자       내용
   ------------------------------------------------------
      2008-06-15     공통팀       최초생성
      2008-10-02     박성철       message 필드 추가
      2012-11-12 	 김치권       spring messageSource 방식으로 수정 
**********************************************************/
package com.echoss.svc.common.exception;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.NestedRuntimeException;

import com.echoss.svc.common.util.PropUtil;


public class CommonPlatformException extends NestedRuntimeException {
	
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -8733329168932640573L;
    
    private String errorCode;
    private String trxCd;
    
	public String getErrorCode() {
		return errorCode;
	}

	public String getTrxCd() {
		return trxCd;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
     * 생성자.
     * 
     * @param message
     * @return void
     */
    public CommonPlatformException(String errorCode) {
    	super(errorCode);
    	this.errorCode = errorCode;
    }
    
    /**
     * 생성자.
     * 
     * @param errorCode
     * @param message
     */
    public CommonPlatformException(String errorCode, String message) {
    	super(errorCode);
    	this.errorCode = errorCode;
    	this.message = message;
    }

    /**
     * 생성자.
     * 
     * @param message
     * @param cause
     * @return void
     */
    public CommonPlatformException(String errorCode, Throwable cause) {
        super( errorCode, cause);
    	this.errorCode = errorCode;
    }


    /**
     * 생성자.
     * 
     * @param message
     * @param cause
     * @param args Localized 메시지 생성을 위한 변수
     * @return void
     */
    public CommonPlatformException(String errorCode, Throwable cause, Object... args) {
		super( errorCode, cause );
    	this.errorCode = errorCode;
		this.args = args;
	}
    
    /**
     * <b>프로그램 설명</b>
     * 	생성자
     * @param errorCode Biz클래서에서 던져주는 message코드(message코드 관련정보는 환경설정됨)
     * @param trxCd
     * @param args
     */
    public CommonPlatformException(String errorCode, String trxCd, Throwable cause, Object... args) {
		super( errorCode, cause );
    	this.errorCode = errorCode;
    	this.trxCd = trxCd;
		this.args = args;
	}
    
    private String message = null;
    private Object[] args = null;
    
    /*
     * 메시지 획득 
     */
    @Override
	public String getMessage() {
		if(message == null) {
			message = PropUtil.propFormat(getErrorCode(), LocaleContextHolder.getLocale(), args);
			if(message == null) {
				message = super.getMessage();
			}				
	    }
		return message;
    }
	
    /**
     * 에러에 대한 정보에 대한 readable한 포맷을 리턴한다.
     * 
     * @param
     * @return String
     */
    public String toString() {

        StringBuilder sb = new StringBuilder("[trxCd : ")
        		.append(getTrxCd()).append("]# [errorCode : ")
                .append(getErrorCode()).append("]# [errorMessage : ")
                .append(getMessage()).append("]# [exception : ")
                .append((getCause() == null ? "" : getCause().toString())).append("]");
        return sb.toString();
    }


}
