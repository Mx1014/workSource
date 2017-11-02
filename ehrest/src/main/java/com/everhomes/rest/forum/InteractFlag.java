// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>
 *     <li>UNSUPPORT((byte)0): UNSUPPORT</li>
 *     <li>SUPPORT((byte)1): SUPPORT</li>
 * </ul>
 */
public enum InteractFlag {
    UNSUPPORT((byte) 0), SUPPORT((byte) 1);

    private Byte code;

    private InteractFlag(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static InteractFlag fromCode(Byte code) {
        if (code != null) {
            InteractFlag[] values = InteractFlag.values();
            for (InteractFlag value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }
        return SUPPORT;
    }
}
