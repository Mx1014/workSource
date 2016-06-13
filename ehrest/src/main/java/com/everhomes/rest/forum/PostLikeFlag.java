// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>是否对帖点赞标记
 * <li>NONE: 未点赞</li>
 * <li>LIKE: 已点赞</li>
 * </ul>
 */
public enum PostLikeFlag {
    NONE((byte)0), LIKE((byte)1);
    
    private byte code;
    
    private PostLikeFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PostLikeFlag fromCode(Byte code) {
        if(code != null) {
            PostLikeFlag[] values = PostLikeFlag.values();
            for(PostLikeFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
