// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 *     <li>SMALL((byte) 0): 小</li>
 *     <li>MEDIUM((byte) 1): 中</li>
 *     <li>LARGE((byte) 2): 大</li>
 * </ul>
 */
public enum TitleSize {
    SMALL((byte) 0), MEDIUM((byte) 1), LARGE((byte) 2);

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
