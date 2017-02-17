// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * </ul>
 */
public enum PmTaskCheckPrivilegeFlag {
	NOT((byte)0), CHECKED((byte)1);

    private byte code;
    private PmTaskCheckPrivilegeFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmTaskCheckPrivilegeFlag fromCode(Byte code) {
        if(code != null) {
            PmTaskCheckPrivilegeFlag[] values = PmTaskCheckPrivilegeFlag.values();
            for(PmTaskCheckPrivilegeFlag value : values) {
                if(value.code == code) {
                    return value;
                }
            }
        }
        
        return null;
    }
}