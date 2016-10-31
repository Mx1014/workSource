// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * </ul>
 */
public enum PmTaskTargetStatus {
	INACTIVE((byte)0), ACTIVE((byte)2);
    
    private byte code;
    private PmTaskTargetStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmTaskTargetStatus fromCode(Byte code) {
        if(code != null) {
            PmTaskTargetStatus[] values = PmTaskTargetStatus.values();
            for(PmTaskTargetStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}