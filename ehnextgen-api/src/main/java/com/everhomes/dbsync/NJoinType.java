package com.everhomes.dbsync;

public enum NJoinType {
    NO_JOIN("no_join"), INNER_JOIN("inner_join"), LEFT_OUTER_JOIN("left_outer_join"), RIGHT_OUTER_JOIN("righ_outer_join");
    
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
