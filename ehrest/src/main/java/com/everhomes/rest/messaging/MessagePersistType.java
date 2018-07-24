package com.everhomes.rest.messaging;

/**
 * <ul>
 *     <li>DISCARD((byte) 0): 丢弃</li>
 *     <li>DB((byte) 1): 数据库</li>
 *     <li>LOG((byte) 2): 日志文件</li>
 * </ul>
 */
public enum MessagePersistType {

    DISCARD((byte) 0), DB((byte) 1), LOG((byte) 2);

    private Byte code;

    MessagePersistType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static MessagePersistType fromCode(Byte code) {
        if (code != null) {
            MessagePersistType[] values = MessagePersistType.values();
            for (MessagePersistType value : values) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}
