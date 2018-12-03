package com.echoss.svc.common.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

public class SelectTimeTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	
	private static final String inputClass = "input1";
	
	private String name = "";			// select에 설정된 id 및 name 값
	private String style = "";		// select에 설정될 CSS 스타일
	private String css = "";		// select에 설정될 CSS 스타일
	private String onChange = "";		// onChange이벤트가 발생되면 처리될 스크립트
	private String selected = "";		// select에서 이미 선택되어 출력될 항목 Value
    
    private HttpServletRequest request;
    
    private String  time_hour = "";
    private String  time_minute = "";
    
	public int doEndTag() throws JspException {
		try
		{
			ExpressionEvaluatorManager.evaluate("selected", selected, String.class, this, pageContext);
			
			JspWriter out = this.pageContext.getOut();
			request = (HttpServletRequest)this.pageContext.getRequest();
			
			String contents = generateSelect();

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
    private String generateSelect(){
        StringBuilder selectSb = new StringBuilder();
        
        selectSb.append( "<select name=\"")
		        .append( this.name + "_hor" )
		        .append( "\" id=\"")
		        .append( this.name + "_hor" )
		        .append( "\"");
		if( this.onChange != null && this.onChange.length() != 0 ){
		    selectSb.append( " onchange=\"")
		            .append( this.onChange )
		            .append( "\"");
		}
		
		if( this.css != null && this.css.length() != 0 ){
            selectSb.append( " class=\"")
                    .append( this.css )
                    .append( "\"");
        }
        
        if( this.style != null && this.style.length() != 0 ){
            selectSb.append( " style=\"")
                    .append( this.style )
                    .append( "\"");
        }
        
        
		selectSb.append( ">\n");
		
		selectSb.append( generateOptionHours() );
		
		selectSb.append( "</select> : ");

		selectSb.append( "<select name=\"")
		        .append( this.name + "_min" )
		        .append( "\" id=\"")
		        .append( this.name + "_min" )
		        .append( "\"");
		if( this.onChange != null && this.onChange.length() != 0 ){
		    selectSb.append( " onchange=\"")
		            .append( this.onChange )
		            .append( "\"");
		}
		
		if( this.style != null && this.style.length() != 0 ){
		    selectSb.append( " style=\"")
		            .append( this.style )
		            .append( "\"");
		}
		selectSb.append( ">\n");
		
		selectSb.append( generateOptionMinute() );
		
		selectSb.append( "</select>\n");

        return selectSb.toString();
    }
    
    /**
     * 시간 option 부분 처리
     * @return
     */
    private String generateOptionHours(){
        StringBuilder optionSb = new StringBuilder();

        List<String> emailHostList = getHoursList();
        Iterator<String> iter = emailHostList.iterator();

        while( iter.hasNext() ){
            String number = iter.next();
            optionSb.append( "<option value=\"")
                    .append(number)    
                    .append( "\"")
                    .append( time_hour.equals(number) ? " selected" : "" )
                    .append( ">")
            		.append(number)    
            		.append( "</option>\n");
        }

        return optionSb.toString();
    }
    
    /**
     * 분 option 부분 처리
     * @return
     */
    private String generateOptionMinute(){
        StringBuilder optionSb = new StringBuilder();

        List<String> emailHostList = getMinuteList();
        Iterator<String> iter = emailHostList.iterator();

        while( iter.hasNext() ){
            String number = iter.next();
            optionSb.append( "<option value=\"")
                    .append(number)    
                    .append( "\"")
                    .append( time_minute.equals(number) ? " selected" : "" )
                    .append( ">")
            		.append(number)    
            		.append( "</option>\n");
        }

        return optionSb.toString();
    }

    /**
     * 시간 정보
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<String> getHoursList(){
        List<String> hoursList = new ArrayList();
        hoursList.add("00");
        hoursList.add("01");
        hoursList.add("02");
        hoursList.add("03");
        hoursList.add("04");
        hoursList.add("05");
        hoursList.add("06");
        hoursList.add("07");
        hoursList.add("08");
        hoursList.add("09");
        hoursList.add("10");
        hoursList.add("11");
        hoursList.add("12");
        hoursList.add("13");
        hoursList.add("14");
        hoursList.add("15"); 
        hoursList.add("16");
        hoursList.add("17");
        hoursList.add("18");
        hoursList.add("19");
        hoursList.add("20");
        hoursList.add("21");
        hoursList.add("22");
        hoursList.add("23");
        
        return hoursList;
    }

    /**
     * 분 정보
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<String> getMinuteList(){
        List<String> minuteList = new ArrayList();
        minuteList.add("00");
        minuteList.add("01");
        minuteList.add("02");
        minuteList.add("03");
        minuteList.add("04");
        minuteList.add("05");
        minuteList.add("06");
        minuteList.add("07");
        minuteList.add("08");
        minuteList.add("09");
        minuteList.add("10");
        minuteList.add("11");
        minuteList.add("12");
        minuteList.add("13");
        minuteList.add("14");
        minuteList.add("15"); 
        minuteList.add("16");
        minuteList.add("17");
        minuteList.add("18");
        minuteList.add("19");
        minuteList.add("20");
        minuteList.add("21");
        minuteList.add("22");
        minuteList.add("23");
        minuteList.add("24");
        minuteList.add("25");
        minuteList.add("26");
        minuteList.add("27");
        minuteList.add("28");
        minuteList.add("29");
        minuteList.add("30");
        minuteList.add("31");
        minuteList.add("32");
        minuteList.add("33");
        minuteList.add("34");
        minuteList.add("35");
        minuteList.add("36");
        minuteList.add("37");
        minuteList.add("38");
        minuteList.add("39");
        minuteList.add("40");
        minuteList.add("41");
        minuteList.add("42");
        minuteList.add("43");
        minuteList.add("44");
        minuteList.add("45");
        minuteList.add("46");
        minuteList.add("47");
        minuteList.add("48");
        minuteList.add("49");
        minuteList.add("50");
        minuteList.add("51");
        minuteList.add("52");
        minuteList.add("53");
        minuteList.add("54");
        minuteList.add("55");
        minuteList.add("56");
        minuteList.add("57");
        minuteList.add("58");
        minuteList.add("59");
        return minuteList;
    }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getOnChange() {
		return onChange;
	}

	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
		
		if(selected.length() > 1){
			this.time_hour = StringUtils.substring(selected, 0, 2);
		}
		if(selected.length() > 3){
			this.time_minute = StringUtils.substring(selected, 2, 4);
		}
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

}