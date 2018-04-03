// @formatter:off
package com.everhomes.rest.messaging;

/**
 * <ul>
 *     <li>GUILD((byte) 1): 行业协会</li>
 * </ul>
 */
public enum DetailType {
    GUILD((byte) 1);

    private byte code;

    private DetailType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static DetailType fromCode(Byte code) {
        if (code != null) {
            DetailType[] values = DetailType.values();
            for (DetailType value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
