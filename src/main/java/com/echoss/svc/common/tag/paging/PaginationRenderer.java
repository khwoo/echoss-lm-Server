package com.echoss.svc.common.tag.paging;

import java.text.MessageFormat;

public class PaginationRenderer {
	public String firstPageLabel;
	public String previousPageLabel;
	public String currentPageLabel;
	public String otherPageLabel;
	public String nextPageLabel;
	public String lastPageLabel;

	public PaginationRenderer()
	{
		this.firstPageLabel = "<a href=\"javascript:{0}({1});\" class=\"first\"></a>";
		this.previousPageLabel = "<a href=\"javascript:{0}({1});\" class=\"pre\"></a><span class=\"paging_num\">";
		this.currentPageLabel = "<a href=\"javascript:{0}({1})\" class=\"on\">{2}</a>";
		this.otherPageLabel = "<a href=\"javascript:{0}({1})\">{2}</a>";
		this.nextPageLabel = "</span><a href=\"javascript:{0}({1});\" class=\"next\"></a>";
		this.lastPageLabel = "<a href=\"javascript:{0}({1});\" class=\"last\"></a>";
	}

	public String renderPagination(PaginationInfo paginationInfo, String jsFunction)
	{
		StringBuffer strBuff = new StringBuffer();

		int firstPageNo = paginationInfo.getFirstPageNo();
		int firstPageNoOnPageList = paginationInfo.getFirstPageNoOnPageList();
		int totalPageCount = paginationInfo.getTotalPageCount();
		int pageSize = paginationInfo.getPageSize();
		int lastPageNoOnPageList = paginationInfo.getLastPageNoOnPageList();
		int currentPageNo = paginationInfo.getCurrentPageNo();
		int lastPageNo = paginationInfo.getLastPageNo();

		if (firstPageNoOnPageList > pageSize) {
			strBuff.append(MessageFormat.format(this.firstPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNo) }));
			strBuff.append(MessageFormat.format(this.previousPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNoOnPageList - 1) }));
		} else {
			strBuff.append(MessageFormat.format(this.firstPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNo) }));
			strBuff.append(MessageFormat.format(this.previousPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNo) }));
		}

		for (int i = firstPageNoOnPageList; i <= lastPageNoOnPageList; ++i) {
			if (i == currentPageNo)
				strBuff.append(MessageFormat.format(this.currentPageLabel, new Object[] { jsFunction, Integer.toString(i), Integer.toString(i) }));
			else {
				strBuff.append(MessageFormat.format(this.otherPageLabel, new Object[] { jsFunction, Integer.toString(i), Integer.toString(i) }));
			}
		}

		if (lastPageNoOnPageList < totalPageCount) {
			strBuff.append(MessageFormat.format(this.nextPageLabel, new Object[] { jsFunction, Integer.toString(firstPageNoOnPageList + pageSize) }));
			strBuff.append(MessageFormat.format(this.lastPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
		} else {
			strBuff.append(MessageFormat.format(this.nextPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
			strBuff.append(MessageFormat.format(this.lastPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
		}
		
		return strBuff.toString();
	}
}
