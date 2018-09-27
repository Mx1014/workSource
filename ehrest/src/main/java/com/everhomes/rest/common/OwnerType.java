// @formatter:off
package com.everhomes.rest.common;

/**
 * <ul>
 * <li>COMMUNITY(1): 项目</li>
 * <li>ORGANIZATION(4): 公司</li>
 * </ul>
 */
public enum OwnerType {
    COMMUNITY((byte)1), ORGANIZATION((byte)4);

    private byte code;

    private OwnerType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static OwnerType fromCode(Byte code) {
        if(code == null)
            return null;
        OwnerType[] values = OwnerType.values();
        for(OwnerType value : values){
            if(value.getCode() == code.byteValue())
                return value;
        }

        return null;
    }
}
