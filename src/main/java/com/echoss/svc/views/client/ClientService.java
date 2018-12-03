package com.echoss.svc.views.client;

import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
//import org.apache.jasper.tagplugins.jstl.core.If;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.echoss.svc.common.dao.CommonDao;
import com.echoss.svc.common.exception.ComBizException;
import com.echoss.svc.common.util.TAData;
import com.mysql.fabric.xmlrpc.base.Param;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.SimpleDate;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;


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
@Service("client/Service")
public class ClientService {

	private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

	@Resource(name="commonDao")
	private CommonDao dao;

	/**
	 * CMS Contents 조회
	 * @param param
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(readOnly=true)
	public String selectCmsContents(TAData param) throws ComBizException {
		try{
			TAData cmsTagInfo = dao.select("client.selectCmsInfo", param);
			List<TAData> cmsTagDetailListInfo = new ArrayList<>();
			String cmsContents = "";
			if(cmsTagInfo == null || cmsTagInfo.isEmpty()){
				logger.error("CMS Tag 정보가 존재하지않습니다.");
				throw new ComBizException("ER90");
			}
			
			if(StringUtils.equals("Y", cmsTagInfo.getString("USE_YN"))){
				
				cmsTagDetailListInfo = dao.selectList("client.selectCmsDetailListInfo",param);
				
				for (TAData item : cmsTagDetailListInfo) {
					cmsContents += item.getString("CMS_CTNT");
				}
					
			}

			 
			return cmsContents;
		} catch (ComBizException e) {
			throw e;
		} catch (Exception e) {
			logger.error("CMS Contents 조회 중 DB 오류 발생!", e);
			throw new ComBizException("ER99");
		}
	}

	/**
	 * Contets 화면 생성
	 * @param template
	 * @param db_Model
	 * @return
	 * @throws ComBizException
	 */
	public String processCmsContents(String template, Map<String, Object> db_Model) throws ComBizException {
		try{
			db_Model.put("filePath", "http://61.35.35.203:45730");
			String contents = "";

			Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

			StringTemplateLoader stringLoader = new StringTemplateLoader();
			stringLoader.putTemplate("contentsTemplate", template);

			cfg.setTemplateLoader(stringLoader);
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

			Template contentsTemplate = cfg.getTemplate("contentsTemplate");

			Writer writer = new StringWriter();

			try {
				contentsTemplate.process(db_Model, writer);
			} catch (TemplateException e) {
				logger.error("CMS Contents 화면 생성 중 오류발생!", e);
//				throw new ComBizException("ER99");
			}

			contents = writer.toString();

			return contents;
		} catch (ComBizException e) {
			throw e;
		} catch (Exception e) {
			logger.error("CMS Contents 조회 중 DB 오류 발생!", e);
			throw new ComBizException("ER99");
		}
	}

	/**
	 * cmsid별 DB Data 조회
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	public TAData getModelData(TAData params) throws ComBizException {
		if(!StringUtils.isBlank(params.getString("lastID"))){
			params.set("lastID", params.getInt("lastID"));
		}
		//page view

		
		// Select Main Model Data
		if(StringUtils.equals("CM001", params.getString("tid"))){
			
			List<String> searchTag = params.getList("searchTagCd");
			
			String sb = "";
			
			for (String item : searchTag) {
				
				sb += String.format("%s,", item);
				
			}
			
			sb = StringUtils.substring(sb, 0,sb.length() - 1);
			
			params.set("AREA_CD", params.getString("searchAreaCd"));
			
			params.set("TAG_CD", sb.toString());
			
			insertSttsPageViewInfo(params);
			
			return selectMainData(params);
			
		}
		// Select Merchant Detail
		if(StringUtils.equals("CM003", params.getString("tid"))){
			
			//mchtCd
			params.set("MCHT_CD", params.getString("mchtCd"));
			
			insertSttsPageViewInfo(params);
			
			return selectDetailData(params);
		}
		// Select Tour Data
		if(StringUtils.equals("CM020", params.getString("tid"))){
			//searchAreaCd
			params.set("AREA_CD", params.getString("searchAreaCd"));
			
			insertSttsPageViewInfo(params);
			
			return selectTourData(params);
		}
		// Select Tour Detail Data
		if(StringUtils.equals("CM021", params.getString("tid"))){
			
			//tourCd
			params.set("TOUR_CD", params.getString("tourCd"));
			
			insertSttsPageViewInfo(params);
			
			return selectTourDetailData(params);
		}
		// Select Tour Detail more Data
		if(StringUtils.equals("CM022", params.getString("tid"))){
			
			//tourCd                   |TU20170002                                  
			//tourMctCd                |V00A038B003P00001
			
			params.set("TOUR_CD", params.getString("tourCd"));
			params.set("MCHT_CD", params.getString("tourMctCd"));
			
			insertSttsPageViewInfo(params);
			
			return selectTourDetailmoreData(params);
		}
		// Select My Page Data
		if(StringUtils.equals("CM030", params.getString("tid"))){
			
			//params.set("mst", params.getString("my_tag"));
			
			insertSttsPageViewInfo(params);
			
			return selectMyPageData(params);
		}
		// Select promotion Data
		if(StringUtils.equals("CM040", params.getString("tid"))){
			
			insertSttsPageViewInfo(params);
			
			return selectPromotionData(params);
		}
		// Select promotion detail Data
		if(StringUtils.equals("CM041", params.getString("tid"))){
			
			params.set("PRMT_CD", params.getString("promotionCd"));
			
			insertSttsPageViewInfo(params);
			
			return selectPromotionDetailData(params);
		}
		// Select Banr Data
		if(StringUtils.equals("CM050", params.getString("tid"))){
			
			params.set("BANR_CD", params.getString("bnrCd"));
			
			// 페이지 view
			insertSttsPageViewInfo(params);
			
			//배너 click
			registSttsBannerClickInfo(params);
			
			return selectBanrDetailData(params);
		}
		// Select 
		if(StringUtils.equals("CM060", params.getString("tid"))){
			
			params.set("SRTH_NM", params.getString("searchNm"));
			
			insertSttsPageViewInfo(params);
			
			return selectSearchData(params);
			
		}
		// Select
		if(StringUtils.equals("CM061", params.getString("tid"))){
			params.set("SRTH_NM", params.getString("searchNm"));
			return selectSearchTopData(params);
			
		}
		// 매장 다음 목록을 조회한다.
		else if(StringUtils.equals("CM000011", params.getString("tid"))){
			List<TAData> branchList = dao.selectList("client.selectBranchList", params);
			List<Map<String, Object>> branchListMap = branchList.stream().map(data -> data.toMap()).collect(Collectors.toList());
			params.set("shopSmallLists", branchListMap);
			return params;
		}
		// 매장 상세 정보를 조회한다.
		else if(StringUtils.equals("CM000023", params.getString("tid")) || StringUtils.equals("CM100023", params.getString("tid"))){
			// Page View 통계 정보 등록
			//insertSttsPageViewInfo(params, params.getString("tid"), params.getString("brId"), "", "", "", "");
			
			return selectDetailData(params);
		}
		// 사용자 북마크 정보를 조회한다.
		else if(StringUtils.equals("CM000013", params.getString("tid"))){
			// Page View 통계 정보 등록
			//insertSttsPageViewInfo(params, params.getString("tid"), "", "", "", "", "");
			
			return selectUserBookmarkInfo(params);
		}
		// 이벤트 정보를 조회한다.
		else if(StringUtils.equals("CM000018", params.getString("tid"))){
			// Page View 통계 정보 등록
			//insertSttsPageViewInfo(params, params.getString("tid"), "", "", "", "", "");
			
			// Main Top 배너 목록을 조회한다.
			params.set("area_id", "");
			List<TAData> bnrList = selectBannerList(params, "E001");
			List<Map<String, Object>> bnrListMap = bnrList.stream().map(data -> data.toMap()).collect(Collectors.toList());
			params.set("adeventlists", bnrListMap);
			
			return params;
		}
		// 매장 길묻기 정보를 조회한다.
		else if(StringUtils.equals("CM000036", params.getString("tid"))){
			// Page View 통계 정보 등록
			//insertSttsPageViewInfo(params, params.getString("tid"), params.getString("brId"), "", "", "", "");
			
			params.set("shopDetail", dao.select("client.selectBranchInfo", params));
			return params;
		}
		// 지도 정보를 조회한다.
		else if(StringUtils.equals("CM000040", params.getString("tid"))){
			// Page View 통계 정보 등록
			//insertSttsPageViewInfo(params, params.getString("tid"), params.getString("brId"), "", "", "", "");
			
			params.set("shopDetail", dao.select("client.selectBranchInfo", params));
			return params;
		}
		// 매장 검색 목록을 조회한다.
		else if(StringUtils.equals("CM000044", params.getString("tid"))){
			// Page View 통계 정보 등록
			//insertSttsPageViewInfo(params, params.getString("tid"), "", "", "", "", "");
			
			return params;
		}
		
		return null;
	}

	/**
	 * 사용자 즐겨찾기 정보를 등록한다.
	 * @param params
	 * @throws ComBizException
	 */
	public void registCustFavorite(TAData params) throws ComBizException {
		try{
			// 등록
			if(StringUtils.equals("1", params.getString("fvrDivCd"))){
				dao.insert("client.insertCustFavorite", params);
			}
			// 삭제
			else if(StringUtils.equals("9", params.getString("fvrDivCd"))){
				dao.insert("client.deleteCustFavorite", params);
			}
		} catch (Exception e) {
			logger.error("고객정보 등록 중 DB 오류 발생!", e);
			throw new ComBizException("ER99");
		}
	}
	
	/**
	 * place 즐겨찾기 삭제.
	 * @param params
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	public void placeFavorite(TAData params) throws ComBizException{
		
		try{
				
			dao.delete("client.deletetUserPlaceFavorite", params);
			
		}catch(Exception e){
			
			logger.error("즐겨찾기 삭제 에러입니다 .",e);
			throw new ComBizException("ER99");
		}
		
	}
	/**
	 * tour 즐겨찾기 삭제.
	 * @param params
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	public void myTourFavorite(TAData params) throws ComBizException{
		
		try{
				
			dao.delete("client.deleteUserTourFavorite", params);
			
		}catch(Exception e){
			
			logger.error("즐겨찾기 삭제 에러입니다 .",e);
			throw new ComBizException("ER99");
		}
		
	}
	/**
	 * 유저 즐겨 찾기 건수 조회
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	public int myTourFavoriteCnt(TAData params) throws ComBizException{
		
		int count = 0;
		
		try{
			
			count = dao.selectInt("client.selectUserMyTourFavoriteCnt",params);
			
		}
		catch (Exception e) {
			logger.error("즐겨찾기 건수 조회 에러 입니다 .", e);
		}
		
		return count;
	}
	/**
	 * 유저 즐겨 찾기 건수 조회
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	public int placeFavoriteCnt(TAData params) throws ComBizException{
		
		int count = 0;
		
		try{
			
			count = dao.selectInt("client.selectUserPlaceFavoriteCnt",params);
			
		}
		catch (Exception e) {
			logger.error("즐겨찾기 건수 조회 에러 입니다 .", e);
		}
		
		return count;
	}
	/**
	 * 사용자 즐겨찾기 정보를 수정.
	 * @param params
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	public void tourFavorite(TAData params) throws ComBizException{
		
		int count = 0;
		
		try{
			
			//유저 tour 정보 조회
			count = dao.selectInt("client.selectUserTourFavoriteCnt",params);
			
			if(count > 0 ){
				
				//유저 tour 삭제 
				dao.delete("client.deleteUserTourFavorite", params);
				
			}else{
				
				//유저 tour 등록 
				dao.update("client.insertUserTourFavorite", params);
			}
			
		}catch (Exception e) {
				
			logger.error("tour 즐겨촻기 수정 에러 !",e);
			throw new ComBizException("ER99");
		}
		
	}
	
	/**
	 * 매장 검색 Contents에 들어갈 Data 조회
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	public String selectSearchBranchList(TAData params) throws ComBizException {
		try{
			if(StringUtils.isBlank(params.getString("search_val"))){
				return "";
			}
			
			// Page View 통계 정보 등록
			//insertSttsPageViewInfo(params, "CM000044", "", "", "", "", params.getString("search_val"));
			
			// 사용자 북마크 목록을 조회한다.
			List<TAData> searchBranchList = dao.selectList("client.selectSearchBranchList", params);
			
			String searchresultHtml = "";
			
			if(searchBranchList.size() == 0){
				searchresultHtml += "<li style='text-align: center;'>搜索的内容不存在。</li>";
			}
			else{
				for(TAData searchBranchInfo : searchBranchList){
					String onstyle = "";
					if(StringUtils.equals(params.getString("search_val"), searchBranchInfo.getString("CN_BR_NM"))){
						onstyle="on";
					}
					searchresultHtml += "<li class=\"cursor_style "+onstyle+"\" onclick=\"javascript:goDetail('"+searchBranchInfo.getString("CONN_URL")+"');\"><p>"+searchBranchInfo.getString("CN_BR_NM")+"</p></li>";
				}
			}
			
			return searchresultHtml;
		} catch (ComBizException e) {
			throw e;
		} catch (Exception e) {
			logger.error("CMS Data 조회 중 DB 오류 발생!", e);
			throw new ComBizException("ER99");
		}
	}

	/**
	 * Main Contents에 들어갈 Data 조회
	 * @param tid
	 * @param paramMap
	 * @param wechat_account_id
	 * @return
	 */
	@Transactional(rollbackFor=ComBizException.class)
	private TAData selectMainData(TAData params) throws ComBizException {
		try{
			params.setNullToInitialize(true);
			
			// 고객 정보를 등록한다.
			dao.insert("insertUserInfo", params);
			
			// 지역명을 조회한다.
			params.set("searchAreaNm", "");
			if(!StringUtils.isBlank(params.getString("searchAreaCd"))){
				TAData areaInfo = dao.select("selectSearchAreaNm", params);
				params.set("searchAreaNm", areaInfo.getString("AREA_NM"));
			}
			
			List<String> paramTagList = params.getList("searchTagCd");
			
			// TAG 목록을 조회한다.
			List<TAData> tagList = dao.selectList("client.selectMerchantTagList", params);
			List<Map<String, Object>> tagListMap = tagList.stream().map(data -> data.toMap()).collect(Collectors.toList());
			
			// 체크여부 확인
			if(paramTagList != null && paramTagList.size() > 0){
				for(String paramTag : paramTagList){
					for(Map<String, Object> tag : tagListMap){
						if(StringUtils.equals(paramTag, (String)tag.get("TAG_CD"))){
							tag.put("ON_YN", "Y");
						}
					}
				}
			}
			
			// 가맹점 목록 총 건수 조회
			int totCnt = dao.selectInt("client.countMerchantList", params);
			// 가맹점 목록을 조회한다.
			List<TAData> merchantList = dao.selectList("client.selectMerchantList", params);
			
			List<Map<String, Object>> merchantListMap = merchantList.stream().map(data -> data.toMap()).collect(Collectors.toList());
			
			List<Map<String, Object>> topBannerListMap = selectBannerList(params, "HMT").stream().map(data -> data.toMap()).collect(Collectors.toList());
			List<Map<String, Object>> bottomBannerListMap = selectBannerList(params, "HMB").stream().map(data -> data.toMap()).collect(Collectors.toList());
			
			params.set("merchantList", merchantListMap);
			params.set("merchantCount", totCnt);
			params.set("topBannerList", topBannerListMap);
			params.set("bottomBannerList", bottomBannerListMap);
			
			params.set("tagList", tagListMap);

		} catch (ComBizException e) {
			throw e;
		} catch (Exception e) {
			logger.error("CMS Data 조회 중 DB 오류 발생!", e);
			throw new ComBizException("ER99");
		}

		return params;
	}

	
	/**
	 * 가맹점 상세 Contents에 들어갈 Data 조회
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	private TAData selectDetailData(TAData params) throws ComBizException {
		try{
			// 매장 상세 정보를 조회한다.
			TAData mchtInfo = dao.select("client.selectMerchantInfo", params);
			
			// 매장 이미지 목록을 조회한다.
			List<TAData> mchtImgList = dao.selectList("client.selectMerchantImgList", params);
			List<Map<String, Object>> mchtImgListMap = mchtImgList.stream().map(data -> data.toMap()).collect(Collectors.toList());
			params.set("mchtImgCnt",	mchtImgListMap.size());
			
			params.set("imgLists",	mchtImgListMap);
			params.set("mchtDetail",	mchtInfo);
			
		} catch (ComBizException e) {
			throw e;
		} catch (Exception e) {
			logger.error("CMS Data 조회 중 DB 오류 발생!", e);
			throw new ComBizException("ER99");
		}
		
		return params;
	}
	/**
	 * tour 정보 조회
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	private TAData selectTourData(TAData params) throws ComBizException{
		try{
		
		List<String> tourCd = new ArrayList<>();
		String tourCdStr = "";
		List<Map<String, Object>> tour_mct_list_maps = null;
		List<Map<String, Object>> tour_list_maps = null;
		List<TAData> tour_list = new ArrayList<>();
		List<TAData> tour_mct_Info_list = new ArrayList<>();
		params.set("searchAreaNm", "");
		
		if(!StringUtils.isBlank(params.getString("searchAreaCd"))){
			
			TAData areaInfo = dao.select("selectSearchAreaNm", params);
			
			params.set("searchAreaNm", areaInfo.getString("AREA_NM"));
		}
		//tour mct 조회
		List<TAData> tour_mct_list = dao.selectList("client.selectTourMctList",params);
		
		for (TAData item : tour_mct_list) {
			//tourCd 목록
			if(StringUtils.indexOf(tourCdStr, item.getString("TOUR_CD")) == - 1){	
				tourCd.add(item.getString("TOUR_CD"));
				tourCdStr += item.getString("TOUR_CD")+",";
			}
		}
		
		//tourCd 조회
		
		params.set("TourCd", tourCd);
		
		if(tour_mct_list.size() > 0 ){
			
			tour_list = dao.selectList("client.selectTourList",params);
			
			tour_mct_Info_list = dao.selectList("client.selectTourMctInfoList",params);
			
		}
		
		tour_list_maps = tour_list.stream().map(data->data.toMap()).collect(Collectors.toList());
		
		tour_mct_list_maps = tour_mct_Info_list.stream().map(data->data.toMap()).collect(Collectors.toList());
		
		List<Map<String, Object>> tourMainBanrList = selectBannerList(params, "TMT").stream().map(data -> data.toMap()).collect(Collectors.toList());
		
		params.set("tourList"		, tour_list_maps);
		params.set("tourMctList"	, tour_mct_list_maps);
		params.set("tourbanrList", tourMainBanrList);
		
		}catch(ComBizException e){
			throw e;
		}catch(Exception e){
			logger.error("tour data 조회 중 DB 오류 발생 !" ,e);
			throw new ComBizException("ER99");
		}
		return params;
		
	}
	
	/**
	 * tour detail 정보 조회
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	private TAData selectTourDetailData(TAData params) throws ComBizException{
		
		TAData tour = null;
		List<TAData> tourMctList = null;
		List<TAData> tourBanrList = new ArrayList<>();
		List<Map<String, Object>> tourBanrListMaps = new ArrayList<>();
		List<Map<String, Object>> tourMctListMaps = new ArrayList<>();
		try{
			
			//tourCd
			//tour 조회
			tour = dao.select("client.selectTourInfo",params);
			
			if(tour != null){
				
				//tour mct 조회
				tourMctList = dao.selectList("client.selectTourDetailMctInfoList",params);
				
				tourMctListMaps = tourMctList.stream().map(data->data.toMap()).collect(Collectors.toList());
			}
			
			tourBanrListMaps = selectBannerList(params, "TDB").stream().map(data -> data.toMap()).collect(Collectors.toList());
			
			params.set("tour",tour);
			params.set("tourMctList", tourMctListMaps);			
			params.set("tourBanrList", tourBanrListMaps);
		}
		catch(ComBizException e){
			throw e;
		}
		catch (Exception e) {
			logger.error("tour detail data 조회 중 DB 오류 발생 !",e);
			throw new ComBizException("ER99");
		}
		
		return params;
		
	}
	/**
	 * tour detail more 정보 조회
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	private TAData selectTourDetailmoreData(TAData params) throws ComBizException{
		

		TAData tourMchtInfo = null;
		List<TAData> tourMchtImageList = new ArrayList<TAData>();
		List<TAData> tourMchtList = new ArrayList<TAData>();
		try{
			
			tourMchtInfo = dao.select("client.selectTourDetailMoreInfo",params);
			
			tourMchtImageList = dao.selectList("client.selectTourDetailMoreImageListInfo",params);
			
			tourMchtList = dao.selectList("client.selectTourDetailListInfo",params);
			
			params.set("tourMchtInfo"	,tourMchtInfo);
			
			params.set("tourMchtImage"	,tourMchtImageList);
			
			params.set("tourMchtList"	,tourMchtList);
			
		}catch(Exception e){
			
			logger.error("tour selectTourDetailmoreData 에러 입니다 .",e);
			
			throw new ComBizException("ER99"); 
			
		}
		
		return params;
	}
	@Transactional(rollbackFor=ComBizException.class)
	private TAData selectSearchTopData(TAData params) throws ComBizException{
		
		return params;
	}
	/**
	 * search data
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	private TAData selectSearchData(TAData params) throws ComBizException{
		
		List<TAData> searchMchList = new ArrayList<>();
		List<Map<String, Object>> searchMchListMap = new ArrayList<Map<String,Object>>();
		
		try{
			
			//searchNm
			//if(!StringUtils.equals("", params.getString("searchNm"))){
			if(!StringUtils.isBlank(params.getString("searchNm"))){
				searchMchList = dao.selectList("client.selectSearchMchList",params);
				
				searchMchListMap = searchMchList.stream().map(data->data.toMap()).collect(Collectors.toList());
				
			}
			
			params.set("searchMchList", searchMchListMap);
			
		}
		catch(ComBizException e){
			
			logger.error("검색 페이지 에러 입니다.",e);
			
		}
		
		return params;
		
	}
	/**
	 * banr detail data
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	private TAData selectBanrDetailData(TAData params) throws ComBizException{
		
		TAData banr = new TAData();
		List<TAData> banrImgList = new ArrayList<TAData>();
		List<Map<String, Object>> banrImgListMap = new ArrayList<Map<String, Object>>();
		
		try{
			
			banr = dao.select("client.selectBanrDetailInfo",params);
			
			banrImgList = dao.selectList("client.selectBanrDetailImageListInfo",params);
			
			banrImgListMap = banrImgList.stream().map(data->data.toMap()).collect(Collectors.toList());
			
			params.set("banr", banr.toMap());
			
			params.set("banrImgList", banrImgListMap);
			
		}catch (Exception e) {
			logger.error("배너 상세 페이지 에러 입니다.",e);
			throw new ComBizException("ER99");
		}
		return params;
		
	}
	/**
	 * promotion detail data
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	private TAData selectPromotionDetailData(TAData params) throws ComBizException{
		
		try{
			
			List<Map<String, Object>> bottomBannerListMap = selectBannerList(params, "PDB").stream().map(data -> data.toMap()).collect(Collectors.toList());
			params.set("proDetailBnarList",bottomBannerListMap );
			
		}catch (Exception e) {
			logger.error("promotion detail 정보 조회 에러 입니다.",e);
		}
		return params;
	}
	/**
	 * promotion page data
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	private TAData selectPromotionData(TAData params) throws ComBizException{
		
		
		List<TAData> promotionList = new ArrayList<>();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy.MM.dd");
		try{
			
			promotionList = dao.selectList("client.selectProMotionListInfo",params);
		
			for (TAData item : promotionList) {
				
				item.set("ST_DT", dateFormat2.format(dateFormat.parse(item.getString("ST_DT"))));
				item.set("EN_DT", dateFormat2.format(dateFormat.parse(item.getString("EN_DT"))));
			}
			List<Map<String, Object>> bottomBannerListMap = selectBannerList(params, "PMT").stream().map(data -> data.toMap()).collect(Collectors.toList());
			params.set("promotionList", promotionList);
			params.set("promotionbanrList", bottomBannerListMap);
			
		}
		catch (Exception e) {
			logger.error("promotion 정보 조회 에러 입니다 .",e);
			throw new ComBizException("ER99");
		}
		return params;
	}
	/**
	 * my page data
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	private TAData selectMyPageData(TAData params) throws ComBizException{
		
		List<TAData> tourList = new ArrayList<TAData>();
		List<TAData> tourMctList = new ArrayList<TAData>();
		List<TAData> placeList = new ArrayList<TAData>();
		List<TAData> placeTagList = new ArrayList<TAData>();
		List<String> TourCd = new ArrayList<String>();
		List<String> MctCd = new ArrayList<String>();
		List<Map<String, Object>> tourListMaps = new ArrayList<>();
		List<Map<String, Object>> tourMctListMaps = new ArrayList<>();
		List<Map<String, Object>> placeListMaps = new ArrayList<>();
		List<Map<String, Object>> placeTagListMaps = new ArrayList<>();
		try{
			
			if(StringUtils.equals("tour", params.getString("my_tag"))){
			
				tourList = dao.selectList("client.selectMyPageTourListInfo",params);
				
				for (TAData item : tourList) {
					
					TourCd.add(item.getString("TOUR_CD"));
					
				}
				
				params.set("TourCd", TourCd);
				
				if(TourCd.size() > 0 ){
					
					tourMctList = dao.selectList("client.selectMyPageTourMchtListInfo",params);
					tourMctListMaps = tourMctList.stream().map(data->data.toMap()).collect(Collectors.toList());
				}
				
				tourListMaps = tourList.stream().map(data->data.toMap()).collect(Collectors.toList());
				
			}else{
			
				placeList = dao.selectList("client.selectMyPagePlaceListInfo",params);
				
				////////////////////////////////
				
				for (TAData item : placeList) {
					
					MctCd.add(item.getString("MCHT_CD"));
					
				}
				
				params.set("MctCd", MctCd);
				
				if(MctCd.size() > 0 ){
				
					placeTagList = dao.selectList("client.selectMyPagePlaceTagListInfo",params);
					placeTagListMaps = placeTagList.stream().map(data->data.toMap()).collect(Collectors.toList());
				}
				
				placeListMaps = placeList.stream().map(data->data.toMap()).collect(Collectors.toList());
				
			}
			params.set("tourList"		, tourListMaps);
			params.set("tourMctList"	, tourMctListMaps);
			params.set("placeList"		, placeListMaps);
			params.set("placeTagList"	, placeTagListMaps);
			
		}catch(Exception e){
			logger.error("my page data 에러 입니다 .",e);
			throw new ComBizException("ER99");
		}
		
		return params;
	}
	/**
	 * 사용자 북마크 Contents에 들어갈 Data 조회
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	private TAData selectUserBookmarkInfo(TAData params) throws ComBizException {
		try{
			// 사용자 북마크 목록을 조회한다.
			List<TAData> bookmarkList = dao.selectList("client.selectUserBookmarkList", params);
			List<Map<String, Object>> bookmarkListMap = bookmarkList.stream().map(data -> data.toMap()).collect(Collectors.toList());
			
			params.set("shopSmallLists",	bookmarkListMap);
			params.set("total_count",		bookmarkListMap.size());
			
		} catch (ComBizException e) {
			throw e;
		} catch (Exception e) {
			logger.error("CMS Data 조회 중 DB 오류 발생!", e);
			throw new ComBizException("ER99");
		}
		
		return params;
	}
	
	/**
	 * 배너목록을 조회한다.
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	private List<TAData> selectBannerList(TAData params, String adBanrTyp) throws ComBizException {
		List<TAData> bnrList = null;
		try{
			params.set("adBanrTyp", adBanrTyp);
			bnrList = dao.selectList("client.selectBannerList", params);
			
			// id가 admin 이면 통계를 쌓지않는다(미리보기)
			if(!StringUtils.equals("admin", params.getString("id"))){
				// 조회된 배너의 View 수를 증가한다.
				for(TAData bnrInfo : bnrList){
					params.set("BanrId", bnrInfo.getString("BANR_CD"));
					dao.insert("client.insertSttsBannerViewInfo", params);
				}
			}

		} catch (ComBizException e) {
			throw e;
		} catch (Exception e) {
			logger.error("CMS Data 조회 중 DB 오류 발생!", e);
			throw new ComBizException("ER99");
		}
		
		return bnrList;
	}
	
	/**
	 * Page View 통계정보를 등록한다.
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	public void insertSttsPageViewInfo(TAData params) throws ComBizException {
		// id가 admin 이면 통계를 쌓지않는다(미리보기)
		if(StringUtils.equals("admin", params.getString("id"))){
			return;
		}
		
		try{
			TAData dbParam = new TAData();
			dbParam.set("USER_ID"	, params.getString("userId"));
			dbParam.set("CMS_ID"	,params.getString("tid"));
			
			dbParam.set("AREA_CD", 	params.getString("AREA_CD"));
			dbParam.set("MCHT_CD", 	params.getString("MCHT_CD"));
			dbParam.set("BANR_CD", 	params.getString("BANR_CD"));
			dbParam.set("TAG_CD", 	params.getString("TAG_CD"));
			dbParam.set("TOUR_CD", 	params.getString("TOUR_CD"));
			dbParam.set("PRMT_CD", 	params.getString("PRMT_CD"));
			
			dbParam.set("SRTH_NM"	, params.getString("searchNm"));
			
			dao.insert("client.insertSttsPageViewInfo", dbParam);
			
			// 매장 ID가 존재하면 매장 접속 건수 증가
//			if(!StringUtils.isBlank(brId)){
//				dao.update("client.updateBrInfoForConnCnt", dbParam);
//			}
		} catch (ComBizException e) {
			throw e;
		} catch (Exception e) {
			logger.error("CMS Data 조회 중 DB 오류 발생!", e);
			throw new ComBizException("ER99");
		}
	}
	
	/**
	 * 배너 Click 통계정보를 등록한다.
	 * @param params
	 * @return
	 * @throws ComBizException
	 */
	@Transactional(rollbackFor=ComBizException.class)
	public void registSttsBannerClickInfo(TAData params) throws ComBizException {
		// id가 admin 이면 통계를 쌓지않는다(미리보기)
		if(StringUtils.equals("admin", params.getString("id"))){
			return;
		}

		try{
			if(StringUtils.isBlank(params.getString("bnrCd"))){
				params.set("bnrCd", "");
			}
			dao.insert("client.insertSttsBannerClickInfo", params);
		} catch (ComBizException e) {
			throw e;
		} catch (Exception e) {
			logger.error("CMS Data 조회 중 DB 오류 발생!", e);
			throw new ComBizException("ER99");
		}
	}
}
