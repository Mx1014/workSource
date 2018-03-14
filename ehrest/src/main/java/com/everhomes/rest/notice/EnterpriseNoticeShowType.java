package com.everhomes.rest.notice;

/**
 * <ul>
 * <li>SHOW : 0 - 正常显示</li>
 * <li>PREVIEW : 1 - 预览</li>
 * </ul>
 */
public enum EnterpriseNoticeShowType {
    SHOW((byte) 0), PREVIEW((byte) 1);
    private byte code;

    private EnterpriseNoticeShowType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static EnterpriseNoticeShowType fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        EnterpriseNoticeShowType[] values = EnterpriseNoticeShowType.values();
        for (EnterpriseNoticeShowType flag : values) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
