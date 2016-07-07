// @formatter:off
package com.everhomes.rest.payment;

/**
 * <ul>停车充值订单状态
 * <li>INACTIVE(0): 无效</li>
 * <li>UNPAID(1): 未支付</li>
 * <li>PAID(2): 已支付</li>
 * </ul>
 */
public enum CardOrderStatus {
    INACTIVE((byte)0), UNPAID((byte)1), PAID((byte)2);
    
    private byte code;
    
    private CardOrderStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static CardOrderStatus fromCode(Byte code) {
        if(code != null) {
            CardOrderStatus[] values = CardOrderStatus.values();
            for(CardOrderStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
