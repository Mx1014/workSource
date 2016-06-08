// @formatter:off
package com.everhomes.rest.pmsy;

/**
 * <ul>物业缴费订单状态
 * <li>INACTIVE(0): 无效</li>
 * <li>UNPAID(1): 未支付</li>
 * <li>PAID(2): 已支付</li>
 * </ul>
 */
public enum PmsyOrderStatus {
    INACTIVE((byte)0), UNPAID((byte)1), PAID((byte)2),FAIL((byte)3),SUCCESS((byte)4);
    
    private byte code;
    
    private PmsyOrderStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmsyOrderStatus fromCode(Byte code) {
        if(code != null) {
            PmsyOrderStatus[] values = PmsyOrderStatus.values();
            for(PmsyOrderStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
