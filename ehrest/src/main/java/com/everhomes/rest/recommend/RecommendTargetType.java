package com.everhomes.rest.recommend;

public enum RecommendTargetType {
    CITY(3l), COMMUNITY(2l), FAMILY(1l), USER(0l);
    
    private long code;
    private RecommendTargetType(long code) {
        this.code = code;
    }
    
    public Long getCode() {
        return this.code;
    }
    
    public static RecommendTargetType fromCode(long code) {
        for(RecommendTargetType t : RecommendTargetType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
