// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 *     <li>NONE((byte)0): NONE</li>
 *     <li>LEFT((byte)1): LEFT</li>
 *     <li>CENTER((byte)2): CENTER</li>
 * </ul>
 */
public enum TitleFlag {
    NONE((byte) 0), LEFT((byte) 1), CENTER((byte) 2);

    private byte code;

    private TitleFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static TitleFlag fromCode(Byte code) {
        if (null != code) {
            for (TitleFlag value : TitleFlag.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
