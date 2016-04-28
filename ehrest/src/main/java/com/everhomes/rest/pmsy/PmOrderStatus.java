// @formatter:off
package com.everhomes.rest.pmsy;

/**
 * <ul>停车充值订单状态
 * <li>INACTIVE(0): 无效</li>
 * <li>UNPAID(1): 未支付</li>
 * <li>PAID(2): 已支付</li>
 * </ul>
 */
public enum PmOrderStatus {
    INACTIVE((byte)0), UNPAID((byte)1), PAID((byte)2);
    
    private byte code;
    
    private PmOrderStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmOrderStatus fromCode(Byte code) {
        if(code != null) {
            PmOrderStatus[] values = PmOrderStatus.values();
            for(PmOrderStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
