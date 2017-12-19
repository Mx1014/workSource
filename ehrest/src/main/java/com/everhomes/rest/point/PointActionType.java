package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>MESSAGE("MESSAGE"): MESSAGE</li>
 * </ul>
 */
public enum PointActionType {

    MESSAGE("MESSAGE");

    private String code;

    PointActionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static PointActionType fromCode(String code) {
        if (code != null) {
            for (PointActionType type : PointActionType.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
