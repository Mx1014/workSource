// @formatter:off
package com.everhomes.rest.launchpadbase;

/**
 * <ul>
 *     <li>WORK((byte) 1): 工作台</li>
 *     <li>SQUARE((byte) 2): 广场</li>
 *     <li>MESSAGE((byte) 3): 消息</li>
 *     <li>ME((byte) 4): 我的</li>
 *     <li>CONTAINER((byte) 5): 容器</li>
 * </ul>
 */
public enum IndexType {
    WORK((byte) 1), SQUARE((byte) 2), MESSAGE((byte) 3), ME((byte) 4), CONTAINER((byte) 5);

    private byte code;

    private IndexType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static IndexType fromCode(Byte code) {
        if (code != null) {
            IndexType[] values = IndexType.values();
            for (IndexType value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
