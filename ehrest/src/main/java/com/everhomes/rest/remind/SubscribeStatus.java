package com.everhomes.rest.remind;

/**
 * <ul>
 * <li>UN_SUBSCRIBE((byte) 0): 未添加</li>
 * <li>SUBSCRIBE((byte) 1): 已添加</li>
 * </ul>
 */
public enum SubscribeStatus {
    UN_SUBSCRIBE((byte) 0), SUBSCRIBE((byte) 1);

    private byte code;

    SubscribeStatus(Byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static SubscribeStatus fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        SubscribeStatus[] values = SubscribeStatus.values();
        for (SubscribeStatus value : values) {
            if (value.code == code.byteValue()) {
                return value;
            }
        }
        return null;
    }
}
