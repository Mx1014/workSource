// @formatter:off
package com.everhomes.rest.common;

/**
 * <ul>
 * <li>NO(0): 非全部</li>
 * <li>YES(1): 全部</li>
 * </ul>
 */
public enum IncludeChildFlagType {

    NO((byte)0), YES((byte)1);

    private byte code;

    private IncludeChildFlagType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static IncludeChildFlagType fromCode(Byte code) {
        if(code == null)
            return null;
        IncludeChildFlagType[] values = IncludeChildFlagType.values();
        for(IncludeChildFlagType value : values){
            if(value.getCode() == code.byteValue())
                return value;
        }
        
        return null;
    }
}
