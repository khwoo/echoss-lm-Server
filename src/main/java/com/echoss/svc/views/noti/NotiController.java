package com.echoss.svc.views.noti;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.echoss.svc.common.exception.BadRequestException;
import com.echoss.svc.common.resolver.RequestParams;
import com.echoss.svc.common.util.PropUtil;
import com.echoss.svc.common.util.TAData;

import java.io.Writer;


/**
 * <PRE>
 * <b>FileName</b>		: AdController.java
 * <b>Project</b>		: Echoss-Wechat-Admin
 * <b>프로그램 설명</b>
 * 
 * <b>작성자</b>			: CHEOLMIN
 * <b>작성일</b>			: 2017. 4. 12.
 * <b>변경이력</b>
 * 이름		: 일자			: 변경내용
 * ------------------------------------------------------
 * CHEOLMIN	: 2017. 4. 12.	: 신규개발
 * </PRE>
 */
@Controller
@RequestMapping({"/"})
public class NotiController {

	private static final Logger logger = LoggerFactory.getLogger(NotiController.class);
	
	@Resource(name="noti/Service")
	private NotiService service;
	
	/**
	 * 프린터 상태 Noti
	 * @param paramMap
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/noti/print", method = RequestMethod.POST)
	public String selBanner(@RequestParams TAData params, Model model) throws Exception {
		String kind = params.getString("kind");
		
		String statusMsg = "";
		try{
			statusMsg = PropUtil.prop("o2o.print.msg." + params.getString("status"));
		}catch(Exception e){
			logger.error("메시지 찾는 중 오류발생!!", e);
		}
		
		if(StringUtils.equals("printer", kind)){
			params.set("printer_status", params.getString("status"));
			params.set("status_text", statusMsg);
			params.set("printer_code", params.getString("pid"));
			
			service.updateStorePrinter(params);
		}
		else if(StringUtils.equals("ticket", kind)){
			String[] pidArray = StringUtils.split(params.getString("pid"), "-");
			if(pidArray == null || pidArray.length != 2){
				throw new BadRequestException("Invalid pid");
			}
			
			params.set("status_text", "[" + params.getString("status") + "]" + statusMsg);
			if(StringUtils.equals("0000", params.getString("status")) || StringUtils.equals("0200", params.getString("status")) || StringUtils.equals("0250", params.getString("status"))){
				params.set("print_state", "C");
				params.set("print_state_his", "C");
			}
			else{
				params.set("print_state", "A");
				params.set("print_state_his", "R");
			}
			
			params.set("order_num", pidArray[0]);
			params.set("print_type", pidArray[1]);
			
			service.updateStorePrintOrder(params);
		}
		
		return "jsonView";
	}

	/**
	*
	* 결재 noti 정보
	* 주문상태 수정 [ 결제완료 ]
	*
	*
	* 2018/10/18
	*
	*/
	@RequestMapping(value="/noti/wechatpay" , method = RequestMethod.POST)
	public void wechatpay(@RequestParams TAData params , Model model , HttpServletRequest request , HttpServletResponse response) throws Exception{
		logger.info("=======================결제========================");
		Writer writer = response.getWriter();
		service.wechatpay( params );
		writer.write("OK");
		writer.flush();
	}
	
}
