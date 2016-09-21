package com.everhomes.rest.widget;


/**
 * <ul>
 * <li>DATE: 日期</li>
 * <li>DATETIME: 时间日期</li>
 * <li>TIME: 时间</li> 
 * </ul>
 */
public enum TimeWidgetStyle {

	DATE("date"), DATETIME("datetime"), TIME("time") ;
	
	private String code;
    private TimeWidgetStyle(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static TimeWidgetStyle fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(DATE.getCode())) {
        	return DATE;
        }

        if(code.equalsIgnoreCase(DATETIME.getCode())) {
        	return DATETIME;
        }
        
        if(code.equalsIgnoreCase(TIME.getCode())) {
        	return TIME;
        }
         
        return null;
    }
}
