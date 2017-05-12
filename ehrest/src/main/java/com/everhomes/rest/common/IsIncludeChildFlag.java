// @formatter:off
package com.everhomes.rest.common;

/**
 * <ul>
 * <li>NO(0): 非全部</li>
 * <li>YES(1): 全部</li>
 * </ul>
 */
public enum IsIncludeChildFlag {

    NO((byte)0), YES((byte)1);

    private byte code;

    private IsIncludeChildFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static IsIncludeChildFlag fromCode(Byte code) {
        if(code == null)
            return null;
        IsIncludeChildFlag[] values = IsIncludeChildFlag.values();
        for(IsIncludeChildFlag value : values){
            if(value.getCode() == code.byteValue())
                return value;
        }
        
        return null;
    }
}
