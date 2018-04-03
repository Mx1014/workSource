// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>1: 执行人员</li>
 * <li>2: 维修人员</li>
 * </ul>
 */
public enum PmTaskOperateType {
	ALL((byte)0), EXECUTOR((byte)1), REPAIR((byte)2);
    
    private byte code;
    private PmTaskOperateType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmTaskOperateType fromCode(Byte code) {
        if(code != null) {
            PmTaskOperateType[] values = PmTaskOperateType.values();
            for(PmTaskOperateType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}