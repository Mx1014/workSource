// @formatter:off
package com.everhomes.department;

/**
 * <ul>
 * <li>GARC: 业委，Government Agency - Resident Committee</li>
 * <li>GANC: 居委，Government Agency - Neighbor Committee</li>
 * <li>GAPS: 公安，Government Agency - Police Station</li>
 * </ul>
 */
public enum DepartmentGroup {
	GARC("garc"), GANC("ganc"), GAPS("gaps");
    
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
        
        if(code.equalsIgnoreCase(GARC.getCode())) {
        	return GARC;
        }

        if(code.equalsIgnoreCase(GANC.getCode())) {
        	return GANC;
        }

        if(code.equalsIgnoreCase(GAPS.getCode())) {
        	return GAPS;
        }

        return null;
    }
}
