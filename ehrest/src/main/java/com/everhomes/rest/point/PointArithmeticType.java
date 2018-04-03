package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>ADD((byte) 1): 加</li>
 *     <li>SUBTRACT((byte) 2): 减</li>
 * </ul>
 */
public enum PointArithmeticType {

    ADD((byte) 1),
    SUBTRACT((byte) 2);

    private Byte code;

    PointArithmeticType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static PointArithmeticType fromCode(Byte code) {
        if (code != null) {
            for (PointArithmeticType type : PointArithmeticType.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
