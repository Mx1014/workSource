package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>CURRENT((byte)0): 当日(本周,本月)</li>
 * <li>NEXT((byte)1):  次日(下周,下月)</li>
 * </ul>
 */
public enum WorkReportTimeType {

    CURRENT((byte) 0), NEXT((byte) 1);

    private byte code;

    private WorkReportTimeType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static WorkReportTimeType fromCode(Byte code) {
        if (code == null)
            return null;
        WorkReportTimeType[] values = WorkReportTimeType.values();
        for (WorkReportTimeType value : values) {
            if (value.getCode() == code)
                return value;
        }

        return null;
    }
}
