package com.everhomes.rest.payment;

/**
 * <ul>
 * <li>INACTIVE(0): 无效</li>
 * <li>WATING(1): wating</li>
 * <li>ACTIVE(2): 有效</li>
 * </ul>
 */
public enum PaymentCardStatus {
	INACTIVE((byte)0), WATING((byte)1),ACTIVE((byte)2);
        
    private Byte code;
    private PaymentCardStatus(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static PaymentCardStatus fromCode(Byte code) {
        if(code != null) {
        	PaymentCardStatus[] values = PaymentCardStatus.values();
            for(PaymentCardStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
