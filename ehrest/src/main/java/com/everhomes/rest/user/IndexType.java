// @formatter:off
package com.everhomes.rest.user;

/**
 * <ul>
 *     <li>work((byte) 1): 工作台</li>
 *     <li>square((byte) 2): 广场</li>
 *     <li>message((byte) 3): 消息</li>
 *     <li>me((byte) 4): 我</li>
 * </ul>
 */
public enum IndexType {
    work((byte) 1), square((byte) 2), message((byte) 3), me((byte) 4);

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
