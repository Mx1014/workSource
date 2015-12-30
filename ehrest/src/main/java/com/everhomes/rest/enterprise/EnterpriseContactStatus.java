package com.everhomes.rest.enterprise;

/**
 * <ul>
 * <li>INACTIVE(0): 已失效</li>
 * <li>WAITING_FOR_APPROVAL(1): 待通过</li>
 * <li>ACTIVE(2): 有效</li>
 * </ul>
 */
public enum EnterpriseContactStatus {
	INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), WAITING_FOR_ACCEPTANCE((byte)2), ACTIVE((byte)3);
    
    private byte code;
    private EnterpriseContactStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EnterpriseContactStatus fromCode(Byte code) {
        if(code != null) {
            for(EnterpriseContactStatus value : EnterpriseContactStatus.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
