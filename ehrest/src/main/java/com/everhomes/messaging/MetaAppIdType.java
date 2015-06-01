package com.everhomes.messaging;

public enum MetaAppIdType {
    TEXT(1l), IMAGE(2l), AUDIO(3l), VIDEO(4l), LINK(5l);
    
    private long code;
    private MetaAppIdType(long code) {
        this.code = code;
    }
    
    public Long getCode() {
        return this.code;
    }
    
    public static MetaAppIdType fromCode(long code) {
        for(MetaAppIdType t : MetaAppIdType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
