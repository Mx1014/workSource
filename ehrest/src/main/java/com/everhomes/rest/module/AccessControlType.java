// @formatter:off
package com.everhomes.rest.module;

/**
 * <ul>
 *     <li>ALL((byte)0): 全部可见</li>
 *     <li>LOGON((byte)1): 登录可见</li>
 *     <li>AUTH((byte)2): 认证可见</li>
 * </ul>
 */
public enum AccessControlType {
    ALL((byte) 0), LOGON((byte) 1), AUTH((byte) 2);

    private byte code;

    private AccessControlType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static AccessControlType fromCode(Byte code) {
        if (code == null) {
            return null;
        }

        for (AccessControlType type : AccessControlType.values()) {
            if (type.getCode() == code.byteValue()) {
                return type;
            }
        }

        return null;

    }
}
