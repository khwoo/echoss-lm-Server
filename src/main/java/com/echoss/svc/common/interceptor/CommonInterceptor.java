package com.echoss.svc.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.internal.org.bouncycastle.asn1.ocsp.ResponseData;
import com.echoss.svc.common.util.JwtUtil;
import com.echoss.svc.common.util.PropUtil;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.io.PrintWriter;
import java.util.Map;

@Controller("commonInterceptor")
public class CommonInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);

	private long currentTimeMillis;
	
    @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

	    response.setCharacterEncoding("utf-8");

    	currentTimeMillis = System.currentTimeMillis();
    	String reqUrl = new String(request.getRequestURL());


    	String token = request.getHeader("X-token");

    	if(StringUtils.isBlank(token)){

		    JSONObject jsonObject = new JSONObject();
		    jsonObject.put("resCd" , "ER101");
		    jsonObject.put("resMsg", PropUtil.prop("ER101") );
		    responseMessage( response , jsonObject );
		    return false;

	    }

	    Map<String,Object> login = JwtUtil.unsign( token , Map.class );

    	logger.debug(login + "");


    	logger.info("메시지가 수신되었습니다. " + reqUrl);
    	
    	return super.preHandle(request, response, handler);
	}

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
    		ModelAndView modelAndView) throws Exception {
    	
    	// TODO Auto-generated method stub
    	super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    		throws Exception {
    	
    	long processTimeMillis = System.currentTimeMillis() - currentTimeMillis;
    	logger.info("메시지가 송신되었습니다. 처리시간[" + processTimeMillis + "]");
    	
    	// TODO Auto-generated method stub
    	super.afterCompletion(request, response, handler, ex);
    }


	private void responseMessage( HttpServletResponse response , JSONObject jsonObject  ) throws Exception {

		PrintWriter printWriter = response.getWriter();
		response.setContentType("application/json; charset=utf-8");
		String json = JSONObject.fromObject(jsonObject).toString();
		printWriter.print(json);
		printWriter.flush();
		printWriter.close();

	}
}
