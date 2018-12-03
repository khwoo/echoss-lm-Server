/********************************************************* 
  @프로그램명   :  ComBizException.java
  @TRIN ID      :  
  @프로그램목적 :  DAO 에서의 예외처리 기본 핸들러
  @적용일시     :  2008.06.15
  @히스토리관리 :
      수정일         변경자       내용
   ------------------------------------------------------

**********************************************************/

package com.echoss.svc.common.exception;

public class ComBizException extends CommonException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1L;

	/**
	 * 생성자
	 * @param String
	 * @return void
	 */
    public ComBizException(String errorCode) {
    	super(errorCode);
    }
    
    /**
     * 생성자
     * @param errorCode Biz클래스에서 던져주는 message코드
     * @param message Biz클래스에서 던져주는 message내용
     */
    public ComBizException(String errorCode, String message) {
    	super(errorCode, message);
    }

	/**
	 * 생성자
	 * @param String		Biz클래서에서 던져주는 message코드(message코드 관련정보는 환경설정됨)
	 * @param Throwable
	 * @return void
	 */
    public ComBizException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }
    
	/**
	 * 생성자
	 * @param String		Biz클래서에서 던져주는 message코드(message코드 관련정보는 환경설정됨)
	 * @param args
	 * @param Throwable
	 * @return void
	 */
    public ComBizException(String errorCode, Object... args) {
        super(errorCode, null, args );
    }
    
    /**
     * <b>프로그램 설명</b>
     * 	생성자
     * @param errorCode Biz클래서에서 던져주는 message코드(message코드 관련정보는 환경설정됨)
     * @param trxCd
     * @param args
     */
    public ComBizException(String errorCode, String trxCd, Object... args) {
        super(errorCode, trxCd, null, args );
    }
    
	/**
	 * 생성자
	 * @param String		Biz클래서에서 던져주는 message코드(message코드 관련정보는 환경설정됨)
	 * @param args
	 * @param Throwable
	 * @return void
	 */
    public ComBizException(String errorCode, Throwable cause, Object... args) {
        super(errorCode, cause, args );
    }
    
}
