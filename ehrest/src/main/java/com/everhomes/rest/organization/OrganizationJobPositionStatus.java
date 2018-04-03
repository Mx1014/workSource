package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>INACTIVE(0): 已失效</li>
 * <li>WAITING_FOR_APPROVAL(1): 待通过</li>
 * <li>ACTIVE(2): 有效</li>
 * </ul>
 */
public enum OrganizationJobPositionStatus {
	INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), ACTIVE((byte)2);
    
    private byte code;
    private OrganizationJobPositionStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OrganizationJobPositionStatus fromCode(Byte code) {
        if(code != null) {
            for(OrganizationJobPositionStatus value : OrganizationJobPositionStatus.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
