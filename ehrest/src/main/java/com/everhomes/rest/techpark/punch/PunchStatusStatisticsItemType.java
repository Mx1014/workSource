package com.everhomes.rest.techpark.punch;

/**
 * <p>出勤统计的类型</p>
 * <ul>
 * <li>UNKNOWN((byte) 0): 核算中</li>
 * <li>UN_ATTENDANCE((byte) 1): 未到（未出勤）</li>
 * <li>BELATE((byte) 2): 迟到</li>
 * <li>LEAVE_EARLY((byte) 3): 早退</li>
 * <li>NORMAL((byte) 4): 正常</li>
 * <li>REST((byte) 5): 休息</li>
 * <li>ABSENT((byte) 6): 旷工</li>
 * <li>FORGOT_PUNCH((byte) 7): 缺卡（忘记打卡）</li>
 * </ul>
 */
public enum PunchStatusStatisticsItemType {
    UNKNOWN((byte) 0), UN_ATTENDANCE((byte) 1), BELATE((byte) 2), LEAVE_EARLY((byte) 3), NORMAL((byte) 4), REST((byte) 5), ABSENT((byte) 6), FORGOT_PUNCH((byte) 7);
    private byte code;

    PunchStatusStatisticsItemType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PunchStatusStatisticsItemType fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        for (PunchStatusStatisticsItemType type : PunchStatusStatisticsItemType.values()) {
            if (type.code == code.byteValue()) {
                return type;
            }
        }
        return null;
    }
}
