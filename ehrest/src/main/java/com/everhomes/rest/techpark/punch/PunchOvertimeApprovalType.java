package com.everhomes.rest.techpark.punch;
 

/**
 * <ul>
 * <li>PUNCH_AND_APPROVAL : 1、需审批，时长以打卡为准，但不能超过审批单时长</li>
 * <li>APPROVAL : 2、 需审批，时长以审批单为准</li>
 * <li>PUNCH : 3、 无需审批，时长以打卡为准</li>
 * </ul>
 */
public enum PunchOvertimeApprovalType {
    PUNCH_AND_APPROVAL((byte) 1), APPROVAL((byte) 2),PUNCH((byte) 3);

    private byte code;

    PunchOvertimeApprovalType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PunchOvertimeApprovalType fromCode(Byte code) {
        if (null != code) {
            PunchOvertimeApprovalType[] values = PunchOvertimeApprovalType.values();
            for (PunchOvertimeApprovalType value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }
        return null;
    }

}
