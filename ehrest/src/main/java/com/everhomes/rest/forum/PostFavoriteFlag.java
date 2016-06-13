// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>是否对帖收藏标记
 * <li>NONE(0): 未收藏</li>
 * <li>FAVORITE(1): 已收藏</li>
 * </ul>
 */
public enum PostFavoriteFlag {
    NONE((byte)0), FAVORITE((byte)1);
    
    private byte code;
    
    private PostFavoriteFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PostFavoriteFlag fromCode(Byte code) {
        if(code != null) {
            PostFavoriteFlag[] values = PostFavoriteFlag.values();
            for(PostFavoriteFlag value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
