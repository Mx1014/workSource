// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>是否对帖点赞标记
 * <li>INACTIVE: 已删除</li>
 * <li>WAITING_FOR_CONFIRMATION: 待确认</li>
 * <li>ACTIVE: 正常</li>
 * </ul>
 */
public enum PostStatus {
    INACTIVE((byte)0), WAITING_FOR_CONFIRMATION((byte)1), ACTIVE((byte)2);
    
    private byte code;
    
    private PostStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PostStatus fromCode(Byte code) {
        if(code != null) {
            PostStatus[] values = PostStatus.values();
            for(PostStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
