// @formatter:off
package com.everhomes.rest.category;

import com.everhomes.rest.category.CategoryConstants;

/**
 * <ul>
 * <li>NOTICE: 物业公告-3091</li>
 * <li>ADVISE: 建议-3092</li>
 * <li>HELP: 求助-3093</li>
 * <li>REPAIR: 维修-3094</li>
 * <li>HOTLINE: 热线</li>
 * <li>PAYMENT: 缴费</li>
 * </ul>
 */
public enum CategoryType {
	NOTICE("NOTICE"), ADVISE("ADVISE"), HELP("HELP"), REPAIR("REPAIR"), HOTLINE("HOTLINE"), PAYMENT("PAYMENT") ;
    
    private String code;
    private CategoryType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static CategoryType fromCode(long code) {
    	if(code == CategoryConstants.CATEGORY_ID_COMPLAINT_ADVICE) {
    		return ADVISE;
    	}
        
        if(code == CategoryConstants.CATEGORY_ID_NOTICE) {
        	return NOTICE;
        }

        if(code == CategoryConstants.CATEGORY_ID_CONSULT_APPEAL) {
        	return HELP;
        }

        if(code == CategoryConstants.CATEGORY_ID_REPAIRS) {
        	return REPAIR;
        }

        return null;
    }

}
