package com.everhomes.rest.organization;

/**
 * <p>删除机构范围类型</p>
 * <ul>
 * <li>CURRENT_NOTE("current_note"): 当前节点</li>
 * <li>CHILD_ENTERPRISE("current_enterprise"): 当前公司</li>
 * <li>ALL_NOTE("all_note"): 全部节点</li>
 * </ul>
 */
public enum DeleteOrganizationContactScopeType {

    CURRENT_NOTE("current_note"), CHILD_ENTERPRISE("current_enterprise"), ALL_NOTE("all_note");

	private String code;

    private DeleteOrganizationContactScopeType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static DeleteOrganizationContactScopeType fromCode(String code) {
    	DeleteOrganizationContactScopeType[] values = DeleteOrganizationContactScopeType.values();
        for(DeleteOrganizationContactScopeType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
