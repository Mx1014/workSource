package com.everhomes.rest.recommend;

public enum RecommendSourceType {
    USER(0l), GROUP(1l), BANNER(2l);
    
    private long code;
    private RecommendSourceType(long code) {
        this.code = code;
    }
    
    public Long getCode() {
        return this.code;
    }
    
    public static RecommendSourceType fromCode(long code) {
        for(RecommendSourceType t : RecommendSourceType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
