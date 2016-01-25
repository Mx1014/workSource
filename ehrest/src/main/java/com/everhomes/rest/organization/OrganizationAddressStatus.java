package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>INACTIVE(0): 已失效</li>
 * <li>WAITING_FOR_APPROVAL(1): 待通过</li>
 * <li>ACTIVE(2): 有效</li>
 * <li>ACTIVE(2): 有效</li>
 * </ul>
 */
public enum OrganizationAddressStatus {
	INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), ACTIVE((byte)2);
    
    private byte code;
    private OrganizationAddressStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OrganizationAddressStatus fromCode(Byte code) {
        if(code != null) {
            for(OrganizationAddressStatus value : OrganizationAddressStatus.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
