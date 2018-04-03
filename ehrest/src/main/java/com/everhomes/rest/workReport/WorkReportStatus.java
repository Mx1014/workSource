package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>INVALID((byte) 0): 无效的</li>
 * <li>VALID((byte) 1): 有效的</li>
 * <li>RUNNING((byte) 2): 启用的</li>
 * </ul>
 */
public enum WorkReportStatus {
    INVALID((byte) 0), VALID((byte) 1), RUNNING((byte) 2);

    private byte code;

    private WorkReportStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static WorkReportStatus fromCode(Byte code) {
        if (code != null) {
            WorkReportStatus[] values = WorkReportStatus.values();
            for (WorkReportStatus value : values) {
                if (code.byteValue() == value.code)
                    return value;
            }
        }
        return null;
    }
}
