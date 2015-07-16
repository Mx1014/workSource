// @formatter:off
package com.everhomes.organization;

import com.everhomes.category.CategoryConstants;

/**
 * <ul>
 * <li>NOTICE: 物业公告-3091</li>
 * <li>ADVISE: 建议-3092</li>
 * <li>HELP: 求助-3093</li>
 * <li>REPAIR: 维修-3094</li>
 * </ul>
 */
public enum OrganizationTaskType {
	NOTICE("notice"), ADVISE("advise"), HELP("help"), REPAIR("repair");
    
    private String code;
    private OrganizationTaskType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationTaskType fromCode(long code) {
    	if(code == CategoryConstants.CATEGORY_ID_GA_ADVISE) {
    		return ADVISE;
    	}
        
        if(code == CategoryConstants.CATEGORY_ID_GA_NOTICE) {
        	return NOTICE;
        }

        if(code == CategoryConstants.CATEGORY_ID_GA_HELP) {
        	return HELP;
        }

        if(code == CategoryConstants.CATEGORY_ID_GA_REPAIR) {
        	return REPAIR;
        }

        return null;
    }

}
