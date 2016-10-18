// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>0: 未回访</li>
 * <li>1: 已回访</li>
 * </ul>
 */
public enum PmTaskRevisitStatus {
	NOT((byte)0), REVISITED((byte)1);
    
    private byte code;
    private PmTaskRevisitStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmTaskRevisitStatus fromCode(Byte code) {
        if(code != null) {
            PmTaskRevisitStatus[] values = PmTaskRevisitStatus.values();
            for(PmTaskRevisitStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}