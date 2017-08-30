// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>
 *     <li>PUBLISH((byte) 0): 已发布</li>
 *     <li>ALL((byte) 1): 全部</li>
 *     <li>TEMPORARY((byte) 2): 暂存</li>
 * </ul>
 */
public enum NeedTemporaryType {
    PUBLISH((byte) 0), ALL((byte) 1), TEMPORARY((byte) 2);

    private Byte code;

    private NeedTemporaryType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static NeedTemporaryType fromCode(Byte code) {
        if (code != null) {
            NeedTemporaryType[] values = NeedTemporaryType.values();
            for (NeedTemporaryType value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
