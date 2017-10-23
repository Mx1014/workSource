// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>
 *     <li>DEFAULT((byte)0): DEFAULT</li>
 *     <li>TOP((byte)1): TOP</li>
 * </ul>
 */
public enum StickFlag {
    DEFAULT((byte) 0), TOP((byte) 1);

    private Byte code;

    private StickFlag(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static StickFlag fromCode(Byte code) {
        if (code != null) {
            StickFlag[] values = StickFlag.values();
            for (StickFlag value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
