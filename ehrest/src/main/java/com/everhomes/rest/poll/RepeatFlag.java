// @formatter:off
package com.everhomes.rest.poll;

/**
 * <ul>
 *     <li>NO((byte)0): NO</li>
 *     <li>YES((byte)1): YES</li>
 * </ul>
 */
public enum RepeatFlag {
    NO((byte) 0), YES((byte) 1);

    private byte code;

    private RepeatFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static RepeatFlag fromCode(Byte code) {
        RepeatFlag[] values = RepeatFlag.values();
        for (RepeatFlag value : values) {
            if (Byte.valueOf(value.code).equals(code)) {
                return value;
            }
        }

        return null;
    }
}
