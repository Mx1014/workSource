package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>NO((byte)0): 不提醒</li>
 * <li>YES((byte)1): 提醒</li>
 * </ul>
 */
public enum WorkReportAuthorMsgType {

    NO((byte) 0), YES((byte) 1);

    private byte code;

    private WorkReportAuthorMsgType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static WorkReportAuthorMsgType fromCode(Byte code) {
        if (code == null)
            return null;
        WorkReportAuthorMsgType[] values = WorkReportAuthorMsgType.values();
        for (WorkReportAuthorMsgType value : values) {
            if (value.getCode() == code)
                return value;
        }

        return null;
    }
}
