package com.everhomes.rest.organization;

/**
 * <p>查询机构通讯录的过滤范围</p>
 * <ul>
 * <li>CURRENT("current"): 本节点人员</li>
 * <li>CHILD_ENTERPRISE("child_enterprise"): 子公司人员</li>
 * <li>CHILD_DEPARTMENT("child_department"): 子部门人员</li>
 * <li>CHILD_GROUP("child_group"): 子项目组人员</li>
 * </ul>
 */
public enum FilterOrganizationContactScopeType {

    CURRENT("current"), CHILD_ENTERPRISE("child_enterprise"), CHILD_DEPARTMENT("child_department"), CHILD_GROUP("child_group");

	private String code;

    private FilterOrganizationContactScopeType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FilterOrganizationContactScopeType fromCode(String code) {
    	FilterOrganizationContactScopeType[] values = FilterOrganizationContactScopeType.values();
        for(FilterOrganizationContactScopeType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
