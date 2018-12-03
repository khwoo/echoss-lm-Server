package com.echoss.svc.common.exception;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ControllerAdvice
@EnableWebMvc
public class ExceptionHandlingController {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingController.class);
	
	@Resource(name="messageSource")
	MessageSource message;

	// Convert a predefined exception to an HTTP Status code
	@ResponseStatus(value=HttpStatus.CONFLICT, reason="Data integrity violation")// 409
	@ExceptionHandler(DataIntegrityViolationException.class)
	public void conflict() {
		// Nothing to do
		logger.error("conflict");
	}

	@ExceptionHandler({CommonException.class})
	public ModelAndView handelBusinessError(HttpServletRequest request,
			HttpServletResponse response, Object handler, CommonException exception) {
		logger.error("CommonException Request: " + request.getRequestURL() + " raised " + exception);
		logger.error(printStackTraceToString(exception));

		String errCd = exception.getErrorCode();
		String errMsg = exception.getMessage();

		if(errMsg == null) {
			try {
				errMsg = message.getMessage(errCd, null, LocaleContextHolder.getLocale());
			} catch(NoSuchMessageException e) {
				errMsg = errCd;
			}
		}

		String viewName = "errorPage";
		if (StringUtils.equals("true", request.getHeader("AJAX"))) {
			viewName = "exceptionView";
		}

		ModelAndView mav = new ModelAndView(viewName);

		mav.addObject("resCd", errCd);
		mav.addObject("resMsg", errMsg);
//		mav.addObject("httpResponseCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		return mav;
	}

	@ExceptionHandler({CommonPlatformException.class})
	public ModelAndView handelBusinessError(HttpServletRequest request,
			HttpServletResponse response, Object handler, CommonPlatformException exception) {
		logger.error("CommonBizException Request: " + request.getRequestURL() + " raised " + exception);
		logger.error(printStackTraceToString(exception));

		String errCd = exception.getErrorCode();
		String errMsg = exception.getMessage();

		if(errMsg == null) {
			try {
				errMsg = message.getMessage(errCd, null, LocaleContextHolder.getLocale());
			} catch(NoSuchMessageException e) {
				errMsg = errCd;
			}
		}

		String viewName = "errorPage";
		if (StringUtils.equals("true", request.getHeader("AJAX"))) {
			viewName = "exceptionView";
		}

		ModelAndView mav = new ModelAndView(viewName);

		mav.addObject("exception", "CommonPlatformException");
		mav.addObject("result", "failed");
		mav.addObject("message", errMsg);
		mav.addObject("httpResponseCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		return mav;
	}
	
	@ExceptionHandler({RuntimeException.class})
	public ModelAndView handleRuntimeError(HttpServletRequest request, Exception exception) {
		logger.error("RuntimeException Request: " + request.getRequestURL() + " raised " + exception);
		logger.error(printStackTraceToString(exception));

		String errCd = "ER99";
		String errMsg = errCd;
		try {
			errMsg = message.getMessage(errCd, null, LocaleContextHolder.getLocale());
		} catch(NoSuchMessageException e) {
		}

		logger.error("errCd : " + errCd + " errMsg : " + errMsg);

		String viewName = "errorPage";
		if (StringUtils.equals("true", request.getHeader("AJAX"))) {
			viewName = "exceptionView";
		}

		ModelAndView mav = new ModelAndView(viewName);

		mav.addObject("resCd", errCd);
		mav.addObject("resMsg", errMsg);
//		mav.addObject("httpResponseCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		return mav;
	}

	@ExceptionHandler({AuthenticationException.class})
	public ModelAndView handleAuthenticationError(HttpServletRequest request, AuthenticationException exception) {
		logger.error("AuthenticationException Request: " + request.getRequestURL() + " raised " + exception);
		logger.error(printStackTraceToString(exception));

		String errCd = "ER92";
		String errMsg = errCd;
		try {
			errMsg = message.getMessage(errCd, null, LocaleContextHolder.getLocale());
		} catch(NoSuchMessageException e) {
		}

		String viewName = "errorPage";
		if (request.getHeader("Accept").toLowerCase().indexOf("json") > -1) {
			viewName = "exceptionView";
		}

		ModelAndView mav = new ModelAndView(viewName);

		mav.addObject("resCd", errCd);
		mav.addObject("resMsg", errMsg);
		mav.addObject("result", exception.getMessage());
		mav.addObject("httpResponseCode", HttpServletResponse.SC_UNAUTHORIZED);

		return mav;
	}

	@ExceptionHandler({BadRequestException.class})
	public ModelAndView handleParameterError(HttpServletRequest request, BadRequestException exception) {
		logger.error("BadRequestException Request: " + request.getRequestURL() + " raised " + exception);
		logger.error(printStackTraceToString(exception));

		String errCd = "ER93";
		String errMsg = errCd;
		try {
			errMsg = message.getMessage(errCd, null, LocaleContextHolder.getLocale());
		} catch(NoSuchMessageException e) {
		}

		String viewName = "errorPage";
		if (request.getHeader("Accept").toLowerCase().indexOf("json") > -1) {
			viewName = "exceptionView";
		}

		ModelAndView mav = new ModelAndView(viewName);

		mav.addObject("resCd", errCd);
		mav.addObject("resMsg", errMsg);
		mav.addObject("result", exception.getMessage());
		mav.addObject("httpResponseCode", HttpServletResponse.SC_BAD_REQUEST);

		return mav;
	}

	@ExceptionHandler({Exception.class})
	public ModelAndView handleError(HttpServletRequest request, Exception exception) {

		logger.error("Exception Request: " + request.getRequestURL() + " raised " + exception);
		logger.error(printStackTraceToString(exception));

		String errCd = "ER99";
		String errMsg = errCd;
		try {
			errMsg = message.getMessage(errCd, null, LocaleContextHolder.getLocale());
		} catch(NoSuchMessageException e) {
		}

		String viewName = "errorPage";
		if (request.getHeader("Accept").toLowerCase().indexOf("json") > -1) {
			viewName = "exceptionView";
		}

		ModelAndView mav = new ModelAndView(viewName);

		mav.addObject("resCd", errCd);
		mav.addObject("resMsg", errMsg);
//		mav.addObject("httpResponseCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		return mav;
	}
	
	public String printStackTraceToString(Throwable e) {
		StringBuffer sb = new StringBuffer();

		try {
			sb.append(e.toString());
			sb.append("\n");
			StackTraceElement element[] = e.getStackTrace();
			for (int idx = 0; idx < element.length; idx++) {
				sb.append("\tat ");
				sb.append(element[idx].toString());
				sb.append("\n");
			}
		} catch (Exception ex) {
			return e.toString();
		}
		return sb.toString();
	}
}
