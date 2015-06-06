// @formatter:off
package com.everhomes.user;

public enum UserLikeType {
    NONE((byte)0), DISLIKE((byte)1), LIKE((byte)2);
    
    private byte code;
    private UserLikeType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
   
    public static UserLikeType fromCode(Byte code) {
        UserLikeType[] values = UserLikeType.values();
        for(UserLikeType value : values) {
            if(value == UserLikeType.fromCode(code)) {
                return value;
            }
        }
        
        return null;
    }
}
