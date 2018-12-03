package com.echoss.svc.views.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.echoss.svc.common.exception.ComBizException;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.commons.lang.StringUtils;
import org.codehaus.janino.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.echoss.svc.common.resolver.RequestParams;
import com.echoss.svc.common.util.TAData;
import sun.awt.ModalityListener;


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
public class ApiController {

	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	@Resource(name="api/Service")
	private ApiService service;

	/**
	*
	* 로그인
	*
	* 2018/11/19
	*
	*/
	@RequestMapping( value="/api/login", method = RequestMethod.POST )
	public String Login(@RequestParams TAData params , Model model ) throws Exception{

		logger.debug(params + "");

		String token = service.checkLoginInfo(params);
		model.addAttribute("resCd" , "0000");
		model.addAttribute("token" , token );

		return "jsonView";
	}

	/**
	*
	* 유저 정보 조회
	*
	* 2018/11/20
	*
	*/
	@RequestMapping( value="/api/user_info" , method = RequestMethod.POST)
	public String user_info(@RequestParams TAData params , Model model , HttpServletResponse response){
		logger.debug(params +"");
		model.addAttribute("resCd" , "0000");
		model.addAttribute("info" , "321");
		return "jsonView";

	}

}
