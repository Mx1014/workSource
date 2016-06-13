// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>失物招领状态
 * <li>CLOSE: 已关闭</li>
 * <li>OPEN: 待认领</li>
 * </ul>
 */
public enum LostAndFoundStatus {
    CLOSE((byte)0), OPEN((byte)1);
    
    private byte code;
    
    private LostAndFoundStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static LostAndFoundStatus fromCode(Byte code) {
        if(code != null) {
            LostAndFoundStatus[] values = LostAndFoundStatus.values();
            for(LostAndFoundStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
