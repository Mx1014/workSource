package com.everhomes.rest.quality;

import com.everhomes.rest.community.CommunityType;


/**
 * <ul>
 * <li>ENTERPRISE: 公司</li>
 * <li>USER: 用户</li>
 * <li>GROUP: 机构成员</li>
 * <li>PM: 物业</li>
 * <li>DEPARTMENT: 部门</li>
 * <li>COMMUNITY: 小区</li>
 * </ul>
 */
public enum OwnerType {

	ENTERPRISE("enterprise"), USER("user"), GROUP("group"), PM("pm"), DEPARTMENT("department"), COMMUNITY("community");
	
	private String code;
    private OwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OwnerType fromCode(String code) {
    	if(code != null) {
    		OwnerType[] values = OwnerType.values();
            for(OwnerType value : values) {
                if(value.getCode().equalsIgnoreCase(code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
