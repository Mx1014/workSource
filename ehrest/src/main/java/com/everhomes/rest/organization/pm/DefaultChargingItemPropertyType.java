package com.everhomes.rest.organization.pm;

/**
 * <ul>
 *     <li>0: community; 1: building; 2: apartment</li>
 * </ul>
 * Created by ying.xiong on 2017/10/27.
 */
public enum DefaultChargingItemPropertyType {
    COMMUNITY((byte)0), BUILDING((byte)1), APARTMENT((byte)2);

    private byte code;

    private DefaultChargingItemPropertyType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static DefaultChargingItemPropertyType fromCode(Byte code) {
        if(code != null){
            for (DefaultChargingItemPropertyType flag : DefaultChargingItemPropertyType.values()) {
                if (flag.getCode() == code.byteValue()) {
                    return flag;
                }
            }
        }

        return null;
    }
}
