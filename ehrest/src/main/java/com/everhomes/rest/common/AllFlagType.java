// @formatter:off
package com.everhomes.rest.common;

/**
 * <ul>
 * <li>NO(0): 非全部</li>
 * <li>YES(1): 全部</li>
 * </ul>
 */
public enum AllFlagType {

    NO((byte)0), YES((byte)1);

    private byte code;

    private AllFlagType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static AllFlagType fromCode(Byte code) {
        if(code == null)
            return null;
        AllFlagType[] values = AllFlagType.values();
        for(AllFlagType value : values){
            if(value.getCode() == code.byteValue())
                return value;
        }
        
        return null;
    }
}
