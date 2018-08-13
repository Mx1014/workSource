package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>IMMEDIATELY((byte) 0): 即时</li>
 * <li>SUMMARY((byte) 1): 汇总</li>
 * </ul>
 */
public enum ReportReceiverMsgType {

    IMMEDIATELY((byte) 0), SUMMARY((byte) 1);

    private byte code;

    private ReportReceiverMsgType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ReportReceiverMsgType fromCode(Byte code) {
        if (code == null)
            return null;
        ReportReceiverMsgType[] values = ReportReceiverMsgType.values();
        for (ReportReceiverMsgType value : values) {
            if (value.getCode() == code)
                return value;
        }

        return null;
    }
}
