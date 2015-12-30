// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>是否是推荐帖
 * <li>NONE: 非推荐</li>
 * <li>ASSIGNED: 已推荐</li>
 * </ul>
 */
public enum PostAssignedFlag {

    NONE((byte)0), ASSIGNED((byte)1);
    
    private byte code;
    
    private PostAssignedFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PostAssignedFlag fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return NONE;
            
        case 1 :
            return ASSIGNED;
            
        default :
            break;
        }
        
        return null;
    }
}
