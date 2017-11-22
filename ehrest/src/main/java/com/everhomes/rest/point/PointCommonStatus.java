package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>INACTIVE((byte) 0): INACTIVE</li>
 *     <li>ENABLED((byte) 1): ENABLED</li>
 *     <li>DISABLED((byte) 2): DISABLED</li>
 * </ul>
 */
public enum PointCommonStatus {

    INACTIVE((byte) 0),
    ENABLED((byte) 1),
    DISABLED((byte) 2);

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
