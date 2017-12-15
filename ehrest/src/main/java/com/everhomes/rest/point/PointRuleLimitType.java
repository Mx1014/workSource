package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>TIMES_PER_DAY((byte) 1): 每天多少次</li>
 *     <li>TIMES((byte) 2): 总次数限制 </li>
 * </ul>
 */
public enum PointRuleLimitType {

    TIMES_PER_DAY((byte) 1),
    TIMES((byte) 2),;

    private Byte code;

    PointRuleLimitType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static PointRuleLimitType fromCode(Byte code) {
        if (code != null) {
            for (PointRuleLimitType type : PointRuleLimitType.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
