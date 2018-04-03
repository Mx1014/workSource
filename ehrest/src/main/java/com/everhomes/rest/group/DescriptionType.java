// @formatter:off
package com.everhomes.rest.group;

/**
 * <ul>
 *     <li>TEXT((byte)0): TEXT</li>
 *     <li>RICH_TEXT((byte)1): RICH_TEXT</li>
 * </ul>
 */
public enum DescriptionType {
    TEXT((byte) 0), RICH_TEXT((byte) 1);

    private byte code;

    private DescriptionType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static DescriptionType fromCode(Byte code) {
        if (code != null) {
            DescriptionType[] values = DescriptionType.values();
            for (DescriptionType value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return TEXT;
    }
}
