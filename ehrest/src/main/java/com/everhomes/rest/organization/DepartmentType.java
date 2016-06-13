package com.everhomes.rest.organization;

/**
 * <p>部门类型</p>
 * <ul>
 * <li>TWG: Temporary Working Group园区合作商内部组织架构类型，临时工作组</li>
 * <li>ORGS: Organizational Structure园区合作商内部组织架构类型，内部正常组织架构</li>
 * </ul>
 */
public enum DepartmentType {

	TWG("TWG"), ORGS("ORGS");
	
	private String code;
    private DepartmentType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static DepartmentType fromCode(String code) {
    	DepartmentType[] values = DepartmentType.values();
        for(DepartmentType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
