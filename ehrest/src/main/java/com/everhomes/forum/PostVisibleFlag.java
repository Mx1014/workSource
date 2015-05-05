// @formatter:off
package com.everhomes.forum;

/**
 * <p>帖子可见性范围：</p>
 * <ul>
 * <li>ALL: 所有人可见</li>
 * <li>COMMUNITY_ONLY: 仅本小区可见</li>
 * </ul>
 *
 */
public enum PostVisibleFlag {
    ALL((byte)0), COMMUNITY_ONLY((byte)1); 
    
    private byte code;
    
    private PostVisibleFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PostVisibleFlag fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return ALL;
            
        case 1 :
            return COMMUNITY_ONLY;
            
        default :
            assert(false);
            break;
        }
        
        return null;
    }
}
