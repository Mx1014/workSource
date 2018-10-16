package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>NO((byte)0): 不提醒</li>
 * <li>YES((byte)1): 提醒</li>
 * </ul>
 */
public enum ReportAuthorMsgType {

    NO((byte) 0), YES((byte) 1);

    private byte code;

    private ReportAuthorMsgType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ReportAuthorMsgType fromCode(Byte code) {
        if (code == null)
            return null;
        ReportAuthorMsgType[] values = ReportAuthorMsgType.values();
        for (ReportAuthorMsgType value : values) {
            if (value.getCode() == code)
                return value;
        }

        return null;
    }
}
