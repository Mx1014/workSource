// @formatter:off
package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>MANAGER: 机构管理员</li>
 * <li>CUSTOMER_SERVICE: 客服</li>
 * <li>MAINTENANCE: 维修员</li>
 * <li>CLEANER: 保洁员</li>
 * <li>SECURITY: 保安</li>
 * </ul>
 */
public enum OrganizationGroup {
	MANAGER("manager"), CUSTOMER_SERVICE("customer service"), MAINTENANCE("maintenance"), CLEANER("cleaner"), SECURITY("security");
    
    private String code;
    private OrganizationGroup(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationGroup fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(MANAGER.getCode())) {
        	return MANAGER;
        }

//        if(code.equalsIgnoreCase(GANC.getCode())) {
//        	return GANC;
//        }
//
//        if(code.equalsIgnoreCase(GAPS.getCode())) {
//        	return GAPS;
//        }

        return null;
    }
}
