package com.everhomes.rest.notice;

/**
 * <ul>
 * <li>DELETED : 0</li>
 * <li>DRAFT : 1</li>
 * <li>ACTIVE : 2</li>
 * <li>INACTIVE : 3</li>
 * </ul>
 */
public enum EnterpriseNoticeStatus {
    DELETED((byte) 0), DRAFT((byte) 1), ACTIVE((byte) 2), INACTIVE((byte) 3);
    private byte code;

    private EnterpriseNoticeStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static EnterpriseNoticeStatus fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        EnterpriseNoticeStatus[] values = EnterpriseNoticeStatus.values();
        for (EnterpriseNoticeStatus status : values) {
            if (status.code == code.byteValue()) {
                return status;
            }
        }
        return null;
    }
}
