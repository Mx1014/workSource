package com.everhomes.recommend;

/**
 * <ul>
 * <li>COMMUNITY_USER: 小区用户</li>
 * <li>CONTACT_USER: 手机联系人</li>
 * </ul>
 */
public enum RecommendUserSourceType {
    COMMUNITY_USER(0), CONTACT_USER(1);
    
    private int code;
    private RecommendUserSourceType(int code) {
        this.code = code;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public static RecommendUserSourceType fromCode(int code) {
        for(RecommendUserSourceType t : RecommendUserSourceType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
