package com.echoss.svc.common.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Controller("CommonLoginInterceptor")
public class CommonLoginInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(CommonLoginInterceptor.class);

	private long currentTimeMillis;
	
    @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
    	currentTimeMillis = System.currentTimeMillis();

    	String reqUrl = new String(request.getRequestURL());
    	logger.info("메시지가 수신되었습니다. " + reqUrl);
    	
    	HttpSession session = request.getSession();
    	
    	if(session.getAttribute("sessionAdmin") == null && !StringUtils.contains(reqUrl, "setChangeLocale")) {
    		if(logger.isDebugEnabled()) {
    			logger.debug("로그인된 세션정보가 없습니다. 로그인 화면으로 이동합니다.");
    		}
    		
    		if(StringUtils.equals(request.getHeader("AJAX"), "true")) response.sendError(901);
    		else {
				ServletContext context = request.getSession().getServletContext();
				context.getRequestDispatcher("/login").forward(request, response);
    		}

    		return false;
    	}

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
