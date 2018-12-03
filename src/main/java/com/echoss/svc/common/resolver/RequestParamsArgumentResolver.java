package com.echoss.svc.common.resolver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.echoss.svc.common.exception.BadRequestException;
import com.echoss.svc.common.exception.ComBizException;
import com.echoss.svc.common.util.TAData;

import net.sf.json.JSONObject;

public class RequestParamsArgumentResolver implements HandlerMethodArgumentResolver {

	private static final Logger logger = LoggerFactory.getLogger(RequestParamsArgumentResolver.class);

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return true;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest nativeRequest = (HttpServletRequest) webRequest.getNativeRequest();

		return getParams(nativeRequest);
	}

	@SuppressWarnings("unchecked")
	public TAData getParams(HttpServletRequest request) throws UnsupportedEncodingException {
		String reqUri = request.getRequestURI();
    	String method = request.getMethod();
    	String contentType = request.getHeader("Content-Type");
 
    	if(logger.isInfoEnabled()) {
    		logger.info("The message has been received. [" + method + "][" + reqUri +"][" + contentType +"]");
    	}
 
    	// Contents Body와 queryString을 읽어들여 TAData 객체를 생성한다.
		TAData params = null;
 
		try {
			if(StringUtils.indexOfIgnoreCase(StringUtils.trimToEmpty(contentType), "Application/x-www-form-urlencoded") > -1) {
				params = new TAData();
				Enumeration<String> ee = request.getParameterNames();
				while(ee.hasMoreElements()) {
					String key = ee.nextElement();

					String[] values = request.getParameterValues(key);
					if(values != null && values.length == 1) {
						String value = values[0];
						if(value == null) value = "";
						params.set(key, value);
					}
					else if(values != null && values.length > 1) {
						List<String> valueList = new ArrayList<String>();
						for(String v : values) {
							String value = v;
							if(value == null) value = "";
							valueList.add(value);
						}
						params.set(key, valueList);
					}
				}
			}
			else if(StringUtils.indexOfIgnoreCase(StringUtils.trimToEmpty(contentType), "Application/json") > -1) {
				InputStream in = request.getInputStream();
				byte[] receiveData = receiveData(in);
 
				if(receiveData == null || receiveData.length == 0) {
					logger.error("Invalid Parameter. parameter is missing.");
					throw new ComBizException("P001");
				}
 
				String receiveString = new String(receiveData);
				receiveString = StringUtils.replace(URLDecoder.decode(receiveString, "UTF-8"), ":null", ":\"\"");;
 
				if (logger.isDebugEnabled()) {
					logger.debug("received data : [" + receiveString + "]");
				}
 
				JSONObject requestJson = null;
				try {
					requestJson = JSONObject.fromObject(receiveString);
				} catch(Exception e) {
					logger.error("Invalid Parameter. Parameter is not JSON format");
					throw new ComBizException("P002");
				}
 
				params = TAData.jsonToTAData(requestJson);
			}
			else {
				params = new TAData();
				Enumeration<String> ee = request.getParameterNames();
				while(ee.hasMoreElements()) {
					String key = ee.nextElement();

					String[] values = request.getParameterValues(key);
					if(values != null && values.length == 1) {
						String value = values[0];
						if(value == null) value = "";
						params.set(key, value);
					}
					else if(values != null && values.length > 1) {
						List<String> valueList = new ArrayList<String>();
						for(String v : values) {
							String value = v;
							if(value == null) value = "";
							valueList.add(value);
						}
						params.set(key, valueList);
					}
				}
			}
		} catch (IOException e1) {
			logger.error("", e1);
		}
 
		String queryString = request.getQueryString();
		
		if (queryString != null) {
			if(params == null) params = new TAData(false);
 
			StringTokenizer token1 = new StringTokenizer(queryString, "&");
 
			while (token1.hasMoreTokens()) {
				String param = token1.nextToken();
 
				StringTokenizer token2 = new StringTokenizer(param, "=");
 
				String key = null;
				String value = null;
				int i = 0;
				while (token2.hasMoreTokens()) {
					switch(i) {
					case 0:
						key = token2.nextToken();
						break;
 
					case 1:
						value = token2.nextToken();
						break;
					}
 
					i++;
				}
 
				if(value != null) {
					if(value.equals("null")) {
						value = "";
					}
					else {
						try {
							value = new String(value.getBytes("8859_1"), "UTF_8");
						} catch (UnsupportedEncodingException e) {
						}
					}
				}
 
				params.set(key, value != null ? URLDecoder.decode(value, "UTF-8") : "");
			}
		}
 
		TAData userInfo = (TAData)params.get("userInfo");
		if(userInfo != null && !userInfo.isEmpty()){
			params.set("userId",		userInfo.getString("openId"));
			params.set("userId",		userInfo.getString("openId"));
			params.set("nickName",		userInfo.getString("nickName").replaceAll("[^\\u0000-\\uFFFF]", ""));
			String gender = "";
			if(StringUtils.equals("1", userInfo.getString("gender"))) gender = "M";
			else if(StringUtils.equals("2", userInfo.getString("gender"))) gender = "F";
			else gender = "N";
			params.set("gender",		gender);
			params.set("country",		userInfo.getString("country"));
			params.set("province",		userInfo.getString("province"));
			params.set("city",		userInfo.getString("city"));
		}
		
		params.toMap().remove("userInfo");
		if(logger.isInfoEnabled()) {
			logger.info("Request Parameters : " + params);
		}
		
		// 필수파라미터를 체크한다.
		checkMandatoryFields(request, params);
				
		return params;
	}

    /**
     * @param in
     * @return
     * @throws Exception
     */
    private byte[] receiveData(InputStream in) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int readByte = 0;
        try {
            while (true) {
                readByte = in.read();
                if (readByte < 1) {
                    break;
                }
                baos.write(readByte);
            }
        } catch (IOException e) {
            logger.error("Receive socket close event!", e);
            return null;
        }

        return baos.toByteArray();
    }

	/**
	 * 입력 파라미터의 유효성을 검사한다.
	 * @param request 입력 파라미터
	 * @param trxCd 거래코드
	 */
    private void checkMandatoryFields(HttpServletRequest request, TAData params) {

    	String reqUri = request.getRequestURI();
    	String method = request.getMethod();
    	String contextPath = StringUtils.trimToEmpty(request.getContextPath());

		logger.debug("contextPath : " + contextPath);

    	String uri = "";
    	if(StringUtils.isEmpty(contextPath)) {
    		uri = reqUri;
    	}
    	else {
    		uri = reqUri.substring(contextPath.length(), reqUri.length());
    	}

		String[] fields = mandatoryFields.get(method + uri);

		if(fields == null || fields.length == 0) {
			return;
		}

		for(String fieldName : fields) {
			if(!params.containsKey(fieldName)) {
				logger.error(fieldName + " is required.");
				throw new BadRequestException(fieldName + " is required.");
			}

			String value = params.getString(fieldName);

			if(value == null || value.trim().equals("")) {
				logger.error(fieldName + " is empty.");
				throw new BadRequestException(fieldName + " is empty.");
			}
		}
	}

	private static Map<String, String[]> mandatoryFields = null;

	/**
	 * 필수항목 리스트를 생성하여 Map에 등록
	 * @param trxCd
	 * @param strs
	 */
	private static void makeMandatoryField(String method, String uri, String... strs) {

		String[] fields = new String[strs.length];
		for(int i = 0 ; i < strs.length ; i++) {
			fields[i] = strs[i];
		}

		mandatoryFields.put(method + uri, fields);
	}

	static {
		mandatoryFields = new HashMap<String, String[]>();
		makeMandatoryField("GET",	"/page/CM000001",	"acntCd", "id");
		makeMandatoryField("GET",	"/page/CM000023",	"acntCd", "id", "brId");
		makeMandatoryField("GET",	"/page/CM100023",	"acntCd", "id", "brId");
		makeMandatoryField("GET",	"/page/CM000013",	"acntCd", "id");
		makeMandatoryField("GET",	"/page/CM000018",	"acntCd", "id");
		makeMandatoryField("POST",	"/userinfo",		"acntCd", "id");
		makeMandatoryField("POST",	"/shopbookmark",	"brId", "id");
	}
}
