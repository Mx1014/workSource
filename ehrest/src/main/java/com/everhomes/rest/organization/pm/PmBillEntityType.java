// @formatter:off
package com.everhomes.rest.organization.pm;

/**
 * <ul>
 * <li>ADDRESS: 地址</li>
 * <li>COMPANY: 公司</li>
 * </ul>
 */
public enum PmBillEntityType {
    ADDRESS("address"), COMPANY("company");
    
    private String code;
    private PmBillEntityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmBillEntityType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(ADDRESS.getCode())) {
        	return ADDRESS;
        }

        if(code.equalsIgnoreCase(COMPANY.getCode())) {
        	return COMPANY;
        }

        return null;
    }
}
