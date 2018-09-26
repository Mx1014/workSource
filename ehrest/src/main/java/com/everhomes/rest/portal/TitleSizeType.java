// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 *     <li>SMALL((byte) 1): 小</li>
 *     <li>MUDIUM((byte) 2): 中</li>
 *     <li>LARGE((byte) 3): 大</li>
 * </ul>
 */
public enum TitleSizeType {
    SMALL((byte) 1), MUDIUM((byte) 2), LARGE((byte) 3);

    private byte code;

    private TitleSizeType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static TitleSizeType fromCode(Byte code) {
        if (null != code) {
            for (TitleSizeType value : TitleSizeType.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
