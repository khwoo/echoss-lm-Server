package com.echoss.svc.views.noti;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.echoss.svc.common.dao.CommonDao;
import com.echoss.svc.common.exception.ComBizException;
import com.echoss.svc.common.util.TAData;


/**
 * <PRE>
 * <b>FileName</b>		: NotiService.java
 * <b>Project</b>		: Echoss-Wechat-Admin
 * <b>프로그램 설명</b>
 * 
 * <b>작성자</b>			: CHEOLMIN
 * <b>작성일</b>			: 2017. 4. 12.
 * <b>변경이력</b>
 * 이름		: 일자			: 변경내용
 * ------------------------------------------------------
 * CHEOLMIN	: 2017. 4. 12.	: 신규 개발
 * </PRE>
 */
@Service("noti/Service")
public class NotiService {

	private static final Logger logger = LoggerFactory.getLogger(NotiService.class);

	@Resource(name="commonDao")
	private CommonDao dao;
	
	/**
	 * 프린터 상태변경
	 * @param params
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	public void updateStorePrinter(TAData params) throws ComBizException{
		try{
			dao.update("noti.updateStorePrinter", params);
		}catch(Exception e){
			logger.error("가맹점 프린터 상태 변경 중 오류 발생", e);
			throw new ComBizException("ER99");
		}
	}
	
	/**
	 * 주문 프린트 상태변경
	 * @param params
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	public void updateStorePrintOrder(TAData params) throws ComBizException{
		try{
			// 주문 정보 조회
			params.set("orderNum", params.getString("order_num"));
			TAData ordrInfo = dao.select("api.selectOrderInfoForComp", params);
			if(ordrInfo == null || ordrInfo.isEmpty()){
				logger.error("주문정보가 존재하지 않습니다.[" + params.getString("orderNum") + "]");
				throw new ComBizException("ER05");
			}
			
			params.set("printer_code", ordrInfo.getString("printer_code"));
			
			// 주문 프린트 정보 변경
			dao.update("noti.updateOrderInfo", params);
			
			// 주문 프린트 내역 등록
			params.set("store_code", ordrInfo.getString("store_code"));
			dao.insert("noti.insertOrderPrintHis", params);
		}catch(Exception e){
			logger.error("가맹점 주문 프린트 상태 변경 중 오류 발생", e);
			throw new ComBizException("ER99");
		}
	}

	/**
	*
	* 결제 noti
	*
	* 2018/10/18
	*
	*/
	@Transactional(rollbackFor = {ComBizException.class})
	public void wechatpay( TAData params ){

//			-------------------------------[TAData]-------------------------------
//					KEY           |                    VALUE
//			----------------------------------------------------------------------
//			Amt                      |2
//			AuthDate                 |180918190633
//			Moid                     |1537265182327
//			MID                      |YST000056m
//			targetUrl                |http://61.35.35.203:45490/noti/platform/we..
//			WXPayEdiNo               |YST000056m20011809181906226310
//			WXPayAppDate             |20180918
//			TID                      |YST000056m20011809181906226310
//			AuthCode                 |4200000194201809187988949001
//			ResultMsg                |?��???������ ??��?��?��?��?.
//			StateCd                  |0
//			ResultCode               |0000
//			WXPayAppTime             |180633
//			----------------------------------------------------------------------

		if(StringUtils.equals("0000" , params.getString("ResultCode")) && StringUtils.equals("0" , params.getString("StateCd"))){

			//주문상태 조회  [ 선처리 ]
			params.set("order_num" , params.getString("Moid") );
			TAData orderInfo = dao.select("noti.selectOrderInfo" , params );

			if(orderInfo != null ){

				//주문상태 수정  [ 결제완료 ]
				params.set("order_state" , "B");
				params.set("nice_pay_no" , params.getString("WXPayEdiNo"));
				dao.update("noti.updateOrderStateInfo" , params );

			}else{

				logger.error("결제 선처리 조회 정보 없습니다.");
				logger.info(params + "");

			}

		}



	}
}
