package com.everhomes.rest.ui.user;

/**
 * <ul>
 * <li>YES((byte)1): 是管理员</li>
 * <li>NO((byte)0): 不是管理员</li>
 * </ul>
 */
public enum ContactAdminFlag {

    YES((byte) 1), NO((byte) 0);

    private byte code;

    private ContactAdminFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ContactAdminFlag fromCode(byte code) {
        for (ContactAdminFlag t : ContactAdminFlag.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }
}
