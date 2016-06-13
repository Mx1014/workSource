package com.everhomes.rest.organization;

/**
 * <p>任务类别</p>
 * <ul>
 * <li>PUBLIC_AREA: 公共区域事件</li>
 * <li>PRIVATE_OWNER: 业主私人事件</li>
 * </ul>
 */
public enum OrganizationTaskCategory {
	PUBLIC_AREA("PUBLIC_AREA"), PRIVATE_OWNER("PRIVATE_OWNER");
    
    private String code;
    private OrganizationTaskCategory(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationTaskCategory fromCode(String code) {
    	OrganizationTaskCategory[] values = OrganizationTaskCategory.values();
        for(OrganizationTaskCategory value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}