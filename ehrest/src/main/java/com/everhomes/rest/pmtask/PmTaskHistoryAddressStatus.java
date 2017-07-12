// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * </ul>
 */
public enum PmTaskHistoryAddressStatus {
	INACTIVE((byte)0), ACTIVE((byte)2);

    private byte code;
    private PmTaskHistoryAddressStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmTaskHistoryAddressStatus fromCode(Byte code) {
        if(code != null) {
            PmTaskHistoryAddressStatus[] values = PmTaskHistoryAddressStatus.values();
            for(PmTaskHistoryAddressStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}