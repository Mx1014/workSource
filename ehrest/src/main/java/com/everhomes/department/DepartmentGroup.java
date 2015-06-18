// @formatter:off
package com.everhomes.department;

/**
 * <ul>
 * <li>MANAGER: 机构管理员</li>
 * </ul>
 */
public enum DepartmentGroup {
	MANAGER("manager");
    
    private String code;
    private DepartmentGroup(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static DepartmentGroup fromCode(String code) {
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
