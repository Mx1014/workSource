package com.everhomes.rest.module.security;

/**
 * <ul>
 * <li>NO_SECURITY((byte) 1): 未设置安全密码</li>
 * <li>SECURITY_SETTED((byte)2): 已设置安全密码</li>
 * </ul>
 */
public enum ServiceModuleSecurityStatus {
    NO_SECURITY((byte) 1), SECURITY_SETTED((byte) 2);

    private byte code;

    ServiceModuleSecurityStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ServiceModuleSecurityStatus fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        for (ServiceModuleSecurityStatus status : ServiceModuleSecurityStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
