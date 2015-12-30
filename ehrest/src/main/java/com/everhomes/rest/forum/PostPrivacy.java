// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>帖子是否公开标记
 * <li>PUBLIC(0): 公开</li>
 * <li>PRIVATE(1): 不公开</li>
 * </ul>
 */
public enum PostPrivacy {

    PUBLIC((byte)0), PRIVATE((byte)1);
    
    private byte code;
    
    private PostPrivacy(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PostPrivacy fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return PUBLIC;
            
        case 1 :
            return PRIVATE;
            
        default :
            break;
        }
        
        return null;
    }
}
