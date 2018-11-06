// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>
 *     <li>NATIVE((byte) 0): 原生处理方式</li>
 *     <li>OUTSIDE_URL((byte) 1): 外部链接</li>
 *     <li>INSIDE_URL((byte) 2): 内部链接</li>
 *     <li>OFFLINE_PACKAGE((byte) 3): 离线包</li>
 * </ul>
 */
public enum ClientHandlerType {

    NATIVE((byte) 0), OUTSIDE_URL((byte) 1), INSIDE_URL((byte) 2), OFFLINE_PACKAGE((byte) 3);

    private Byte code;

    private ClientHandlerType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static ClientHandlerType fromCode(Byte code) {
        if (null != code) {
            for (ClientHandlerType value : ClientHandlerType.values()) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
