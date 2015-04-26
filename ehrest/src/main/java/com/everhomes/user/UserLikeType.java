// @formatter:off
package com.everhomes.user;

public enum UserLikeType {
    DISLIKE((byte)0), LIKE((byte)1);
    
    private byte code;
    private UserLikeType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
   
    public static UserLikeType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return DISLIKE;
            
        case 1 :
            return LIKE;
            
        default :
            break;
        }
        
        return null;
    }
}
