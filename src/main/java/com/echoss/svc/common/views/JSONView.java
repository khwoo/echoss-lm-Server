package com.echoss.svc.common.views;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class JSONView extends MappingJackson2JsonView {
	private static final Logger logger = LoggerFactory.getLogger(JSONView.class);

	private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String REQUEST_HEADER_ORIGIN = "Origin";

	@Override
	protected Object filterModel(Map<String, Object> model) {
		Object result = super.filterModel(model);
		if(!(result instanceof Map)) {
			return result;
		}

		@SuppressWarnings("rawtypes")
		Map map = (Map)result;
		if(map.size() == 1) {
			return map.values().toArray()[0];
		}

		return map;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		response.setContentType("application/json; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setCharacterEncoding("UTF-8");

		// cross domain을 허용하게끔 header를 설정한다.(모바일웹 거래도 허용할 수 있도록)
		String origin = request.getHeader(REQUEST_HEADER_ORIGIN);
		if(StringUtils.length(origin) > 0) {
			response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
			response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
		}

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("resCd", "0000");
		params.put("resMsg", "Okay");
		params.put("result", model);
		
		ObjectMapper om = new ObjectMapper();
		String jsonStr = om.writeValueAsString(params);

		if(logger.isDebugEnabled()) {
			logger.debug("응답메시지 : [" + jsonStr + "]");
		}

		response.getWriter().write(jsonStr);
	}
}

