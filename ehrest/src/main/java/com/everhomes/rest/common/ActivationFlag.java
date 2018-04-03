// @formatter:off
package com.everhomes.rest.common;

/**
 * <ul>
 * <li>NO(0): 未激活</li>
 * <li>YES(1): 激活</li>
 * </ul>
 */
public enum ActivationFlag {

    NO((byte)0), YES((byte)1);

    private byte code;

    private ActivationFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static ActivationFlag fromCode(Byte code) {
        if(code == null)
            return null;
        ActivationFlag[] values = ActivationFlag.values();
        for(ActivationFlag value : values){
            if(value.getCode() == code.byteValue())
                return value;
        }
        
        return null;
    }
}
