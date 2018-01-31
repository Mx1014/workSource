package com.everhomes.rest.notice;

/**
 * <ul>
 * <li>PUBLIC : 0</li>
 * <li>PRIVATE : 1</li>
 * </ul>
 */
public enum EnterpriseNoticeSecretFlag {
    PUBLIC((byte) 0), PRIVATE((byte) 1);
    private byte code;

    private EnterpriseNoticeSecretFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static EnterpriseNoticeSecretFlag fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        EnterpriseNoticeSecretFlag[] values = EnterpriseNoticeSecretFlag.values();
        for (EnterpriseNoticeSecretFlag flag : values) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
