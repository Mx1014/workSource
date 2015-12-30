// @formatter:off
package com.everhomes.rest.business;

/**
 * <ul>
 * <li>NONE: 未知</li>
 * <li>ZUOLIN: 左邻商家</li>
 * <li>THIRDPART: 第三方商家</li>
 * </ul>
 */
public enum BusinessTargetType {
    NONE((byte)0), ZUOLIN((byte)1), THIRDPART((byte)2);
    
    private byte code;
    
    private BusinessTargetType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static BusinessTargetType fromCode(Byte code) {
        if(code == null)
            return null;
        BusinessTargetType[] values = BusinessTargetType.values();
        for(BusinessTargetType value : values){
            if(value.getCode() == code.byteValue())
                return value;
        }
        
        return null;
    }
}
