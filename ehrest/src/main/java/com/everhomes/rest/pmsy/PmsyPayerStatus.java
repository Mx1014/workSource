package com.everhomes.rest.pmsy;

/**
 * <ul>停车充值订单状态
 * <li>INACTIVE(0): 无效</li>
 * <li>WAITING(1): waiting</li>
 * <li>ACTIVE(2): 有效</li>
 * </ul>
 */
public enum PmsyPayerStatus {
	INACTIVE((byte)0), WAITING((byte)1), ACTIVE((byte)2);
    
    private byte code;
    
    private PmsyPayerStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmsyPayerStatus fromCode(Byte code) {
        if(code != null) {
        	PmsyPayerStatus[] values = PmsyPayerStatus.values();
            for(PmsyPayerStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
