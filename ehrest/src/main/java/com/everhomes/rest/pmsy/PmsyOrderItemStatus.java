// @formatter:off
package com.everhomes.rest.pmsy;

/**
 * <ul>停车充值订单状态
 * <li>FAIL(0): 失败</li>
 * <li>SUCCESS(1): 成功</li>
 * </ul>
 */
public enum PmsyOrderItemStatus {
    FAIL((byte)0), SUCCESS((byte)1);
    
    private byte code;
    
    private PmsyOrderItemStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmsyOrderItemStatus fromCode(Byte code) {
        if(code != null) {
            PmsyOrderItemStatus[] values = PmsyOrderItemStatus.values();
            for(PmsyOrderItemStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
