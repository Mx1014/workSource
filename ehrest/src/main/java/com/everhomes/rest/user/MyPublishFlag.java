// @formatter:off
package com.everhomes.rest.user;

/**
 * <ul>
 *     <li>HIDE((byte)0): 隐藏</li>
 *     <li>DISPLAY((byte)1): 展示</li>
 * </ul>
 */
public enum MyPublishFlag {
    HIDE((byte) 0), DISPLAY((byte) 1);

    private byte code;

    private MyPublishFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static MyPublishFlag fromCode(Byte code) {
        if (code != null) {
            MyPublishFlag[] values = MyPublishFlag.values();
            for (MyPublishFlag value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return DISPLAY;
    }
}
