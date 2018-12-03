package com.echoss.svc.views.api;

import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.echoss.svc.common.util.*;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.echoss.svc.common.dao.CommonDao;
import com.echoss.svc.common.exception.ComBizException;


/**
 * <PRE>
 * <b>FileName</b>		: AdService.java
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
@Service("api/Service")
public class ApiService {

	private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

	@Resource(name="commonDao")
	private CommonDao dao;

	@Resource(name="config")
	private Properties prop;

	/**
	*
	* 로그인 정보 체크
	*
	* 2018/11/20
	*
	*/
	@Transactional(readOnly = true)
	public String checkLoginInfo( TAData params ) throws Exception{

		params.set("password" , EncUtils.getEncrypt(params.getString("username"), AESUtil.encrypt(params.getString("password"))));

		TAData user_info = dao.select("api.selectCheckUserInfo" , params );

		if(user_info == null ){
			logger.error("로그인 정보가 올바르지 않습니다.");
			throw new ComBizException("ER100");
		}

		Map<String,Object> login = new HashMap<>();
		login.put("username" , params.getString("username"));
		login.put("password" , params.getString("password"));
		Long stamp = (Long)(2L*24L*60L*60L*1000L);
//		Long stamp = (Long)(0L);
		String token = JwtUtil.sign( login , stamp );

		return token;

	}



}
