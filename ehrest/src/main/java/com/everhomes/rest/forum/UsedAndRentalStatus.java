// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>二手与租售状态
 * <li>CLOSE: 已关闭</li>
 * <li>OPEN: 待交易</li>
 * </ul>
 */
public enum UsedAndRentalStatus {
    CLOSE((byte)0), OPEN((byte)1);
    
    private byte code;
    
    private UsedAndRentalStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static UsedAndRentalStatus fromCode(Byte code) {
        if(code != null) {
            UsedAndRentalStatus[] values = UsedAndRentalStatus.values();
            for(UsedAndRentalStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
