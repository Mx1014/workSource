// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>免费物品状态
 * <li>CLOSE: 已关闭</li>
 * <li>OPEN: 待交易</li>
 * </ul>
 */
public enum FreeStuffStatus {
    CLOSE((byte)0), OPEN((byte)1);
    
    private byte code;
    
    private FreeStuffStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static FreeStuffStatus fromCode(Byte code) {
        if(code != null) {
            FreeStuffStatus[] values = FreeStuffStatus.values();
            for(FreeStuffStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
