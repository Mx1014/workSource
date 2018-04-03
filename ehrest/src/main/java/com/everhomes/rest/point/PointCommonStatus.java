package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>INACTIVE((byte) 0): INACTIVE</li>
 *     <li>DISABLED((byte) 1): DISABLED</li>
 *     <li>ENABLED((byte) 2): ENABLED</li>
 * </ul>
 */
public enum PointCommonStatus {

    INACTIVE((byte) 0),
    DISABLED((byte) 1),
    ENABLED((byte) 2);

    private Byte code;

    PointCommonStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static PointCommonStatus fromCode(Byte code) {
        if (code != null) {
            for (PointCommonStatus type : PointCommonStatus.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
