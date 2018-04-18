// @formatter:off
package com.everhomes.rest.launchpadbase;

/**
 * <ul>
 *     <li>LAYOUT((byte) 1): 门户</li>
 *     <li>APPLICATION((byte) 2): 应用</li>
 *     <li>MESSAGE((byte) 3): 消息</li>
 *     <li>ME((byte) 4): 我的</li>
 * </ul>
 */
public enum LayoutType {
    LAYOUT((byte) 1), APPLICATION((byte) 2), MESSAGE((byte) 3), ME((byte) 4);

    private byte code;

    private LayoutType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static LayoutType fromCode(Byte code) {
        if (code != null) {
            LayoutType[] values = LayoutType.values();
            for (LayoutType value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
