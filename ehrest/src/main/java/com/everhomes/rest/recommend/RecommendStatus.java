package com.everhomes.rest.recommend;

public enum RecommendStatus {
    TIMEOUT(2), IGNORE(1), OK(0);
    
    private int code;
    private RecommendStatus(int code) {
        this.code = code;
    }
    
    public Integer getCode() {
        return this.code;
    }
    
    public static RecommendStatus fromCode(long code) {
        for(RecommendStatus t : RecommendStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
