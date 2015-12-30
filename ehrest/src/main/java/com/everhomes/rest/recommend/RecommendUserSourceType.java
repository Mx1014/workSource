package com.everhomes.rest.recommend;

/**
 * <ul>
 * <li>COMMUNITY_USER: 小区用户</li>
 * <li>CONTACT_USER: 手机联系人</li>
 * </ul>
 */
public enum RecommendUserSourceType {
    COMMUNITY_USER(0l), CONTACT_USER(1l);
    
    private long code;
    private RecommendUserSourceType(long code) {
        this.code = code;
    }
    
    public Long getCode() {
        return this.code;
    }
    
    public static RecommendUserSourceType fromCode(long code) {
        for(RecommendUserSourceType t : RecommendUserSourceType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
