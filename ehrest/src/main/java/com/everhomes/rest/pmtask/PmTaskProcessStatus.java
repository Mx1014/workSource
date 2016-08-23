// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>0: 未处理</li>
 * <li>1: 已处理</li>
 * </ul>
 */
public enum PmTaskProcessStatus {
	UNPROCESSED((byte)0), PROCESSED((byte)1), USER((byte)2);
    
    private byte code;
    private PmTaskProcessStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmTaskProcessStatus fromCode(Byte code) {
        if(code != null) {
            PmTaskProcessStatus[] values = PmTaskProcessStatus.values();
            for(PmTaskProcessStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}