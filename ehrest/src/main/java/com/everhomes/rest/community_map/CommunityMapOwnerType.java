// @formatter:off
package com.everhomes.rest.community_map;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>COMMUNITY("community"): 小区</li>
 * </ul>
 */
public enum CommunityMapOwnerType {
    COMMUNITY("community");

    private String code;
    private CommunityMapOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static CommunityMapOwnerType fromCode(String code) {
        if(code != null) {
            CommunityMapOwnerType[] values = CommunityMapOwnerType.values();
            for(CommunityMapOwnerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
