// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>1: 小区家庭门牌地址</li>
 * <li>2: 园区公司地址</li>
 * </ul>
 */
public enum PmTaskAddressType {
	FAMILY((byte)1), ORGANIZATION((byte)2);
    
    private byte code;
    PmTaskAddressType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmTaskAddressType fromCode(Byte code) {
        if(code != null) {
            PmTaskAddressType[] values = PmTaskAddressType.values();
            for(PmTaskAddressType value : values) {
                if(value.code == code) {
                    return value;
                }
            }
        }
        
        return null;
    }
}