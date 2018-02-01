package com.everhomes.notice;

/**
 * <ul>
 * <li>INVALID : 0</li>
 * <li>VALID : 1</li>
 * </ul>
 */
public enum EnterpriseNoticeAttachmentStatus {
    INVALID((byte) 0), VALID((byte) 1);
    private byte code;

    private EnterpriseNoticeAttachmentStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static EnterpriseNoticeAttachmentStatus fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        EnterpriseNoticeAttachmentStatus[] values = EnterpriseNoticeAttachmentStatus.values();
        for (EnterpriseNoticeAttachmentStatus flag : values) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
