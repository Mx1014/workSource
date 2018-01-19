package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>UNREAD((byte) 0): 未读</li>
 * <li>READ((byte) 1): 已读</li>
 * </ul>
 */
public enum WorkReportReadStatus {

    UNREAD((byte) 0), READ((byte) 1);

    private byte code;

    private WorkReportReadStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static WorkReportReadStatus fromCode(Byte code) {
        if (code != null) {
            WorkReportReadStatus[] values = WorkReportReadStatus.values();
            for (WorkReportReadStatus value : values) {
                if (code.byteValue() == value.code)
                    return value;
            }
        }
        return null;
    }
}
