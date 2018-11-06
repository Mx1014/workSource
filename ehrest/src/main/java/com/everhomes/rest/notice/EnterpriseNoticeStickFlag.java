package com.everhomes.rest.notice;

/**
 * <ul>
 * <li>UN_STICK : 0 - 不置顶</li>
 * <li>STICK : 1 - 置顶</li>
 * </ul>
 */
public enum EnterpriseNoticeStickFlag {
    UN_STICK((byte) 0), STICK((byte) 1);
    private byte code;

    private EnterpriseNoticeStickFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static EnterpriseNoticeStickFlag fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        EnterpriseNoticeStickFlag[] values = EnterpriseNoticeStickFlag.values();
        for (EnterpriseNoticeStickFlag flag : values) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
