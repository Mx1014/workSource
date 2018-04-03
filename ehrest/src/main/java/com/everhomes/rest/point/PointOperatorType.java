package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>SYSTEM((byte) 1): SYSTEM</li>
 *     <li>MANUALLY((byte) 2): MANUALLY</li>
 * </ul>
 */
public enum PointOperatorType {

    SYSTEM((byte) 1),
    MANUALLY((byte) 2);

    private Byte code;

    PointOperatorType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static PointOperatorType fromCode(Byte code) {
        if (code != null) {
            for (PointOperatorType type : PointOperatorType.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
