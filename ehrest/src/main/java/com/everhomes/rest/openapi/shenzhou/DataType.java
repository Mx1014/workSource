package com.everhomes.rest.openapi.shenzhou;

/**
 * Created by ying.xiong on 2017/8/8.
 */
public enum DataType {
    COMMUNITY((byte)1),BUILDING((byte)2),APARTMENT((byte)3),ENTERPRISE((byte)4),INDIVIDUAL((byte)5),
    APARTMENT_LIVING_STATUS((byte)6);

    private Byte code;

    private DataType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static DataType fromCode(Byte code) {
        if (code != null) {
            for (DataType type : DataType.values()) {
                if (type.getCode().byteValue() == code.byteValue()) {
                    return type;
                }
            }
        }
        return null;
    }
}
