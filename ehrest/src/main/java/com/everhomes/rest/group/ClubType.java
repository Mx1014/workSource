// @formatter:off
package com.everhomes.rest.group;

/**
 * <ul>
 *     <li>NORMAL((byte) 0): NORMAL</li>
 *     <li>GUILD((byte) 1): GUILD</li>
 * </ul>
 */
public enum ClubType {
    NORMAL((byte) 0), GUILD((byte) 1);

    private byte code;

    private ClubType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ClubType fromCode(Byte code) {
        if (code != null) {
            ClubType[] values = ClubType.values();
            for (ClubType value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return NORMAL;
    }
}
