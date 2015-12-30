package com.everhomes.rest.enterprise;

/**
 * <ul>
 * <li>INACTIVE(0): 已失效</li>
 * <li>WAITING_FOR_APPROVAL(1): 待通过</li>
 * <li>ACTIVE(2): 有效</li>
 * </ul>
 */
public enum EnterpriseAddressStatus {
	INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), ACTIVE((byte)2);
    
    private byte code;
    private EnterpriseAddressStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static EnterpriseAddressStatus fromCode(Byte code) {
        if(code != null) {
            for(EnterpriseAddressStatus value : EnterpriseAddressStatus.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
