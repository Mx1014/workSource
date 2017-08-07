// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>
 * <li>REAL: 实际帖</li>
 * <li>CLONE: 克隆帖</li>
 * <li>NORMAL: 普通帖</li>
 * </ul>
 */
public enum PostCloneFlag {
    REAL((byte)0), CLONE((byte)1), NORMAL((byte)2);

    private Byte code;
    private PostCloneFlag(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static PostCloneFlag fromCode(Byte code) {
        if(code != null) {
        	PostCloneFlag[] values = PostCloneFlag.values();
            for(PostCloneFlag value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return NORMAL;
    }
}
