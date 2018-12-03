package com.echoss.svc.common.tag.paging;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class PaginationTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private PaginationInfo paginationInfo;
	private String jsFunction;
	
	public int doEndTag() throws JspException {
		try
		{
			JspWriter out = this.pageContext.getOut();

			PaginationRenderer paginationRenderer = new PaginationRenderer();
			
			if(this.paginationInfo.getPageSize() == 0){
				return SKIP_PAGE;
			}
			
			String contents = paginationRenderer.renderPagination(this.paginationInfo, this.jsFunction);

			out.println(contents);
			
			return EVAL_PAGE;
		}
		catch (IOException e) {
			throw new JspException();
		}
	}

	public void setJsFunction(String jsFunction) {
		this.jsFunction = jsFunction;
	}

	public void setPaginationInfo(PaginationInfo paginationInfo) {
		this.paginationInfo = paginationInfo;
	}
}