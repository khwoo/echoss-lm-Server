package com.echoss.svc.common.tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FormatDateTag Tag Library
 *
 * 날짜 변환
 *
 * ex)
 * <kbl:formatDate inregex="yyyyMMdd" outregex="yyyy-MM-dd" value="20071012"/>
 *
 * <b>Attributes</b>
 * - inregex        : 입력 포멧
 * - outregex       : 출력 포멧
 * - value          : 입력 값
 *
 */
public class FormatDateTag extends TagSupport {
	private static final Logger logger = LoggerFactory.getLogger(FormatDateTag.class);
	
	private static final long serialVersionUID = 1L;
	private final static String UNDEFINED = "00000000"; 
	
	/**
     * 입력 포멧
     */
    private String infmt = "yyyyMMdd";
    /**
     * 출력 포멧
     */
    private String outfmt = "yyyy-MM-dd";
    /**
     * 입력값
     */
    private String value = "";
    
	public int doEndTag() throws JspException {
		try
		{
			ExpressionEvaluatorManager.evaluate("value", value, String.class, this, pageContext);
			
			JspWriter out = this.pageContext.getOut();

			String contents = generateFormat();

			out.println(contents);
			
			return EVAL_PAGE;
		}
		catch (IOException e) {
			throw new JspException();
		}
	}

    /**
     * select tag 부분 처리
     * @return
     */
    private String generateFormat(){
        StringBuilder selectSb = new StringBuilder();
        
        SimpleDateFormat sdf = null;
        try {
            if( UNDEFINED.equals( value ) || "".equals( value ) ){
            	selectSb.append( "" );
            }
            else{
            	if(StringUtils.isBlank(infmt) || StringUtils.equals("yyyyMMdd", infmt)){
            		if(value.length() == 4){
            			infmt = "yyyy";
            			outfmt = "yyyy";
            		}
            		else if(value.length() == 6){
            			infmt = "yyyyMM";
            			outfmt = "yyyy-MM";
            		}
            		else if(value.length() == 8){
            			infmt = "yyyyMMdd";
            			outfmt = "yyyy-MM-dd";
            		}
            	}
        		
                sdf = new SimpleDateFormat(infmt);
                Date d = null;
                if(this.value.equals("today")){
                	d = new Date();
                }else{
                	d = sdf.parse(value);
                }
                sdf.applyPattern(outfmt);
                
                selectSb.append(sdf.format(d));
            }
        }
        catch (Exception e) {
            logger.error("Date Formatting Error", e);
        }
        
        return selectSb.toString();
    }

	public void setInfmt(String infmt) {
		this.infmt = infmt;
	}

	public void setOutfmt(String outfmt) {
		this.outfmt = outfmt;
	}

	public void setValue(String value) {
		this.value = StringUtils.trim(value);
	}
}
