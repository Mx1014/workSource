// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 *     <li>SMALL((byte) 1): 小</li>
 *     <li>MEDIUM((byte) 2): 中</li>
 *     <li>LARGE((byte) 3): 大</li>
 * </ul>
 */
public enum TitleSize {
    SMALL((byte) 1), MEDIUM((byte) 2), LARGE((byte) 3);

    private byte code;

    private TitleSize(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static TitleSize fromCode(Byte code) {
        if (null != code) {
            for (TitleSize value : TitleSize.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
