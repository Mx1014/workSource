package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>DAY((byte) 0): 每日</li>
 * <li>WEEK((byte) 1): 每周</li>
 * <li>MONTH((byte) 2): 每月</li>
 * </ul>
 */
public enum WorkReportType {
    DAY((byte) 0), WEEK((byte) 1), MONTH((byte) 2);

    private byte code;

    private WorkReportType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static WorkReportType fromCode(Byte code) {
        if (code != null) {
            WorkReportType[] values = WorkReportType.values();
            for (WorkReportType value : values) {
                if (code.byteValue() == value.code)
                    return value;
            }
        }
        return null;
    }
}
