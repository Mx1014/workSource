package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>PLUS((byte) 1): 增加</li>
 *     <li>MINUS((byte) 2): 减少</li>
 * </ul>
 */
public enum PointOperateType {

    PLUS((byte) 1),
    MINUS((byte) 2);

    private Byte code;

    PointOperateType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static PointOperateType fromCode(Byte code) {
        if (code != null) {
            for (PointOperateType type : PointOperateType.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
