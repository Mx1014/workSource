// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>是否有帖子状态
 * <li>NONE(0): 无新帖子</li>
 * <li>NEW_POST(1): 有新帖子</li>
 * </ul>
 */
public enum CheckUserPostStatus {
    NONE((byte)0), NEW_POST((byte)1);
    
    private byte code;
    
    private CheckUserPostStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static CheckUserPostStatus fromCode(Byte code) {
        if(code != null) {
            CheckUserPostStatus[] values = CheckUserPostStatus.values();
            for(CheckUserPostStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
