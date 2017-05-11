package com.everhomes.rest.organization;

/**
 * <p>入住节点范围</p>
 * <ul>
 * <li>CURRENT("current"): 当前节点</li>
 * <li>CURRENT_CHILD("current_child"): 当前节点以及子节点</li>
 * <li>CURRENT_LEVEL_CHILD("current_level_child"): 当前节点、同级节以及子节点</li>
 * </ul>
 */
public enum OrganizationCommunityScopeType {

    CURRENT("current"), CURRENT_CHILD("current_child"), CURRENT_LEVEL_CHILD("current_level_child");

	private String code;

    private OrganizationCommunityScopeType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationCommunityScopeType fromCode(String code) {
    	OrganizationCommunityScopeType[] values = OrganizationCommunityScopeType.values();
        for(OrganizationCommunityScopeType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
