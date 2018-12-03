package com.echoss.svc.views.client;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.echoss.svc.common.exception.BadRequestException;
import com.echoss.svc.common.resolver.RequestParams;
import com.echoss.svc.common.util.DateUtil;
import com.echoss.svc.common.util.TAData;


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
public class ClientController {

	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	@Resource(name="client/Service")
	private ClientService service;
	
	private String imgPath = "http://61.35.35.203:45730/resources/images/img_menu";
	
	// ==================================================================================================== //
	// 											배너관리 Controller												//
	// ==================================================================================================== //
	
	/**
	 * Wifi 화면 조회
	 * @param reqParam
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/page/{cmsid}", method = RequestMethod.POST)
	public String wifiView(@PathVariable String cmsid, @RequestParams TAData params, ModelMap model) throws Exception {
		if(StringUtils.isBlank(cmsid)){
			throw new BadRequestException("cmsid is empty.");
		}
		
		TAData userInfo = (TAData)params.get("userInfo");
		logger.debug("" + userInfo);
		if(userInfo != null && !userInfo.isEmpty()){
			params.set("userId",		userInfo.getString("openId"));
			params.set("nickname",		userInfo.getString("nickName"));
			params.set("sex",			userInfo.getString("gender"));
			params.set("lang",			userInfo.getString("language"));
			params.set("country",		"");
			params.set("prov",			userInfo.getString("province"));
			params.set("city",			userInfo.getString("city"));
			params.set("profileImgUrl",	userInfo.getString("avatarUrl"));
		}
		
		// cmsid에 해당하는 Contents 정보 조회
		params.set("tid", cmsid);
		String contents = service.selectCmsContents(params);
		
		// cmsid에 해당하는 DB data 조회
//		service.getModelData(params);
		
		// 고객 화면 생성
		contents = service.processCmsContents(contents, params.toMap());
		
		model.addAttribute("contents", contents);
		
		model.addAttribute("tid", cmsid);
		return cmsid;
	}
	
	/**
	 * 주문 Cart 화면 조회
	 * @param reqParam
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/page/cart", method = RequestMethod.POST)
	public String cartView(@RequestParams TAData params, ModelMap model) throws Exception {
		String cmsId = "CM004";
		// cmsid에 해당하는 Contents 정보 조회
		params.set("tid", cmsId);
		String contents = service.selectCmsContents(params);
		
		logger.debug(">>>>>" + params.get("cartData").getClass());
		
		logger.debug(">>>>>" + ((List)params.get("cartData")).size());
		
		List<TAData> cartData = (List<TAData>)params.get("cartData");
		
		// Cart 화면 생성
		params.set("cartSize", cartData.size());
		if(cartData != null && cartData.size() > 0){
			for(TAData data : cartData){
				data.set("amtInfo", data.getInt("value") * data.getInt("amt"));
				data.set("prdtNm", getPrdtNm(data.getString("key")));
				data.set("prdtImg", imgPath + data.getString("key") + ".jpg");
			}
		}
		
		contents = service.processCmsContents(contents, params.toMap());
		
		model.addAttribute("contents", contents);
		
		return cmsId;
	}
	
	/**
	 * 주문 Cart 화면 조회
	 * @param reqParam
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/page/pay", method = RequestMethod.POST)
	public String payView(@RequestParams TAData params, ModelMap model) throws Exception {
		String cmsId = "CM005";
		// cmsid에 해당하는 Contents 정보 조회
		params.set("tid", cmsId);
		String contents = service.selectCmsContents(params);
		
		logger.debug(">>>>>" + params.get("cartData").getClass());
		
		logger.debug(">>>>>" + ((List)params.get("cartData")).size());
		
		List<TAData> cartData = (List<TAData>)params.get("cartData");
		
		// Cart 화면 생성
		params.set("cartSize", cartData.size());
		int totCnt = 0;
		if(cartData != null && cartData.size() > 0){
			for(TAData data : cartData){
				data.set("amtInfo", data.getInt("value") * data.getInt("amt"));
				data.set("prdtNm", getPrdtNm(data.getString("key")));
				data.set("prdtImg", imgPath + data.getString("key") + ".jpg");
				
				totCnt += data.getInt("value") * data.getInt("amt");
			}
		}
		
		params.set("totPayAmt", totCnt);
		params.set("payDt", DateUtil.formatDate(DateUtil.getCurrentDate(), "-"));
		params.set("payTm", DateUtil.formatTime(DateUtil.getCurrentTime(), ":"));
		contents = service.processCmsContents(contents, params.toMap());
		
		model.addAttribute("contents", contents);
		
		return cmsId;
	}
	
	/**
	 * 주문 Cart 화면 조회
	 * @param reqParam
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/page/pay_comp", method = RequestMethod.POST)
	public String pay_compView(@RequestParams TAData params, ModelMap model) throws Exception {
		String cmsId = "CM006";
		// cmsid에 해당하는 Contents 정보 조회
		params.set("tid", cmsId);
		String contents = service.selectCmsContents(params);
		
		logger.debug(">>>>>" + params.get("cartData").getClass());
		
		logger.debug(">>>>>" + ((List)params.get("cartData")).size());
		
		List<TAData> cartData = (List<TAData>)params.get("cartData");
		
		// Cart 화면 생성
		params.set("cartSize", cartData.size());
		int totCnt = 0;
		if(cartData != null && cartData.size() > 0){
			for(TAData data : cartData){
				data.set("amtInfo", data.getInt("value") * data.getInt("amt"));
				data.set("prdtNm", getPrdtNm(data.getString("key")));
				data.set("prdtImg", imgPath + data.getString("key") + ".jpg");
				
				totCnt += data.getInt("value") * data.getInt("amt");
			}
		}
		
		params.set("totPayAmt", totCnt);
		params.set("payDt", DateUtil.formatDate(DateUtil.getCurrentDate(), "-"));
		params.set("payTm", DateUtil.formatTime(DateUtil.getCurrentTime(), ":"));
		contents = service.processCmsContents(contents, params.toMap());
		
		model.addAttribute("contents", contents);
		
		return cmsId;
	}
	
	
	/**
	 * 매장 즐겨찾기
	 * @param paramMap
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/favorite", method = RequestMethod.POST)
	public String favorite(@RequestParams TAData params, Model model) throws Exception {
		TAData userInfo = (TAData)params.get("userInfo");
		if(userInfo != null && !userInfo.isEmpty()){
			params.set("userId",		userInfo.getString("openId"));
		}
		
		service.registCustFavorite(params);
		
		return "jsonView";
	}
	
	/**
	 * 매장 검색
	 * @param paramMap
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/shopsearch", method = RequestMethod.POST)
	public String shopsearch(@RequestParams TAData params, Model model) throws Exception {
		
		model.addAttribute("searchresult", service.selectSearchBranchList(params));
		
		return "jsonView";
	}
	
	
	/**
	 * 고객 배너 클릭 정보 등록
	 * @param paramMap
	 * @param model
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/bannerclick", method = RequestMethod.POST)
	public String bannerclick(@RequestParams TAData params, Model model) throws Exception {
		service.registSttsBannerClickInfo(params);
		
		return "jsonView";
	}
	
	private String getPrdtNm(String key){
		String prdtNm = "";
		if(StringUtils.equals("001", key)) prdtNm = "비빔밥";
		else if(StringUtils.equals("002", key)) prdtNm = "육개장";
		else if(StringUtils.equals("003", key)) prdtNm = "갈비탕";
		else if(StringUtils.equals("004", key)) prdtNm = "부대찌개";
		else if(StringUtils.equals("005", key)) prdtNm = "순두부찌개";
		else if(StringUtils.equals("006", key)) prdtNm = "참치찌개";
		else if(StringUtils.equals("007", key)) prdtNm = "꽃게된장찌개";
		else if(StringUtils.equals("008", key)) prdtNm = "일본가정식 규동";
		else if(StringUtils.equals("009", key)) prdtNm = "오징어덮밥";
		else if(StringUtils.equals("010", key)) prdtNm = "제육덮밥";
		else if(StringUtils.equals("011", key)) prdtNm = "라면";
		else if(StringUtils.equals("012", key)) prdtNm = "치즈라면";
		else if(StringUtils.equals("013", key)) prdtNm = "쫄면";
		else if(StringUtils.equals("014", key)) prdtNm = "우동";
		else if(StringUtils.equals("015", key)) prdtNm = "물냉면";
		else if(StringUtils.equals("016", key)) prdtNm = "열무국수";
		else if(StringUtils.equals("017", key)) prdtNm = "비빔국수";
		else if(StringUtils.equals("018", key)) prdtNm = "냉모밀";
		else if(StringUtils.equals("019", key)) prdtNm = "따바오";
		else if(StringUtils.equals("020", key)) prdtNm = "쩡짜오";
		
		return prdtNm;
	}
	
}
