package com.everhomes.dbsync;

public enum NJoinType {
    NO_JOIN("NO_JOIN"), INNER_JOIN("INNER_JOIN"), LEFT_OUTER_JOIN("LEFT_OUTER_JOIN"), RIGHT_OUTER_JOIN("RIGHT_OUTER_JOIN");
    
    private String code;
    private NJoinType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static NJoinType fromCode(String code) {
        NJoinType[] values = NJoinType.values();
        for(NJoinType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
