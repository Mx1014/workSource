package com.everhomes.rest.techpark.punch;

/**
 * <p>考勤申请统计的类型</p>
 * <ul>
 * <li>ASK_FOR_LEAVE((byte) 1): 请假统计类型</li>
 * <li>GO_OUT((byte) 2): 外出统计类型</li>
 * <li>BUSINESS_TRIP((byte) 3): 出差统计类型</li>
 * <li>OVERTIME((byte) 4): 加班统计类型</li>
 * <li>PUNCH_EXCEPTION((byte) 5): 打卡异常统计类型</li>
 * </ul>
 */
public enum PunchExceptionRequestStatisticsItemType {
    ASK_FOR_LEAVE((byte) 1), GO_OUT((byte) 2), BUSINESS_TRIP((byte) 3), OVERTIME((byte) 4), PUNCH_EXCEPTION((byte) 5);

    private byte code;

    PunchExceptionRequestStatisticsItemType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PunchExceptionRequestStatisticsItemType fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        for (PunchExceptionRequestStatisticsItemType type : PunchExceptionRequestStatisticsItemType.values()) {
            if (type.code == code.byteValue()) {
                return type;
            }
        }
        return null;
    }
}
