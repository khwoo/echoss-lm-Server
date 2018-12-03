package com.echoss.svc.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Controller("commonViewInterceptor")
public class CommonViewInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(CommonViewInterceptor.class);

	private long currentTimeMillis;
	
    @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
    	currentTimeMillis = System.currentTimeMillis();

    	String reqUrl = new String(request.getRequestURL());
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
}
