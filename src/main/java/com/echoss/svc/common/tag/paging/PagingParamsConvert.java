package com.echoss.svc.common.tag.paging;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.echoss.svc.common.util.TAData;

public class PagingParamsConvert {
	private static final Logger logger = LoggerFactory.getLogger(PagingParamsConvert.class);
	
	private PaginationInfo pageInfo;

	private int pageIndex = 1;
	private int pageUnit = 10;
	private int pageSize = 10;

	public PagingParamsConvert(){
		this.pageInfo = new PaginationInfo();
	}
	
	public PagingParamsConvert(int pageUnit){
		this.pageInfo = new PaginationInfo();
		this.pageUnit = pageUnit;
	}

	public TAData convertPagingParams(TAData reqParams){
		if (StringUtils.isBlank(reqParams.getString("pageIndex"))) {
			reqParams.set("pageIndex", pageIndex);
		}
		if (StringUtils.isBlank(reqParams.getString("pageUnit"))) {
			reqParams.set("pageUnit", pageUnit);
		}
		if (StringUtils.isBlank(reqParams.getString("pageSize"))) {
			reqParams.set("pageSize", pageSize);
		}
		
		pageInfo.setCurrentPageNo(reqParams.getInt("pageIndex"));
		pageInfo.setRecordCountPerPage(reqParams.getInt("pageUnit"));
		pageInfo.setPageSize(reqParams.getInt("pageSize"));
		
		reqParams.set("firstIndex", pageInfo.getFirstRecordIndex());
		reqParams.set("lastIndex", pageInfo.getLastRecordIndex());
		reqParams.set("recordCountPerPage", pageInfo.getRecordCountPerPage());	

		logger.debug("================ PagingParams ========================");
		logger.debug("" + reqParams);
		
		return reqParams;
	}

	public PaginationInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PaginationInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public int getPageUnit() {
		return pageUnit;
	}

	public void setPageUnit(int pageUnit) {
		this.pageUnit = pageUnit;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
