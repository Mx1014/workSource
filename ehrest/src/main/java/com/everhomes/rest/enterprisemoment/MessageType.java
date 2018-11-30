package com.everhomes.rest.enterprisemoment;

/**
 * <ul>
 * <li>DO_FAVOURITE((byte) 1): 点赞</li>
 * <li>DO_COMMENT((byte) 2): 评论</li>
 * </ul>
 */
public enum MessageType {
    DO_FAVOURITE((byte) 1), DO_COMMENT((byte) 2);

    private byte code;

    MessageType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static MessageType fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        for (MessageType type : MessageType.values()) {
            if (type.code == code.byteValue()) {
                return type;
            }
        }
        return null;
    }
}
