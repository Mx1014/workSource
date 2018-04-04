package com.everhomes.notice;

/**
 * <ul>
 * <li>INVALID : 0</li>
 * <li>VALID : 1</li>
 * </ul>
 */
public enum EnterpriseNoticeReceiverStatus {
    INVALID((byte) 0), VALID((byte) 1);
    private byte code;

    private EnterpriseNoticeReceiverStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static EnterpriseNoticeReceiverStatus fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        EnterpriseNoticeReceiverStatus[] values = EnterpriseNoticeReceiverStatus.values();
        for (EnterpriseNoticeReceiverStatus flag : values) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
