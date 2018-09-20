package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>CURRENT((byte)0): 当日(本周,本月)</li>
 * <li>NEXT((byte)1):  次日(下周,下月)</li>
 * </ul>
 */
public enum ReportTimeType {

    CURRENT((byte) 0), NEXT((byte) 1);

    private byte code;

    private ReportTimeType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ReportTimeType fromCode(Byte code) {
        if (code == null)
            return null;
        ReportTimeType[] values = ReportTimeType.values();
        for (ReportTimeType value : values) {
            if (value.getCode() == code)
                return value;
        }

        return null;
    }
}
