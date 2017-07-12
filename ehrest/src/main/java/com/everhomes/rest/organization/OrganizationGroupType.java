package com.everhomes.rest.organization;

/**
 * <p>组织类型</p>
 * <ul>
 * <li>ENTERPRISE("ENTERPRISE"): 公司</li>
 * <li>DEPARTMENT("DEPARTMENT"): 部门</li>
 * <li>GROUP("GROUP"): 普通群组</li>
 * <li>JOB_POSITION("JOB_POSITION"): 岗位群组</li>
 * <li>JOB_LEVEL("JOB_LEVEL"): 职级群组</li>
 * <li>MANAGER("MANAGER"): 经理群组</li>
 * </ul>
 */
public enum OrganizationGroupType {

	ENTERPRISE("ENTERPRISE"), DEPARTMENT("DEPARTMENT"), GROUP("GROUP"),JOB_POSITION("JOB_POSITION"), JOB_LEVEL("JOB_LEVEL"), MANAGER("MANAGER"), DIRECT_UNDER_ENTERPRISE("DIRECT_UNDER_ENTERPRISE");
	
	private String code;
    private OrganizationGroupType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationGroupType fromCode(String code) {
    	OrganizationGroupType[] values = OrganizationGroupType.values();
        for(OrganizationGroupType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
