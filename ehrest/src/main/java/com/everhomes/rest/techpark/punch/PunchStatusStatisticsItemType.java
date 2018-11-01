package com.everhomes.rest.techpark.punch;

/**
 * <p>出勤统计的类型</p>
 * <ul>
 * <li>UN_ARRIVED((byte) 1): 未到（未出勤）</li>
 * <li>BELATE((byte) 2): 迟到</li>
 * <li>LEAVE_EARLY((byte) 3): 早退</li>
 * <li>NORMAL((byte) 4): 正常</li>
 * <li>REST((byte) 5): 休息</li>
 * <li>ABSENT((byte) 6): 旷工</li>
 * <li>FORGOT_PUNCH((byte) 7): 缺卡（忘记打卡）</li>
 * <li>CHECKING((byte) 8): 核算中（等价于未到）</li>
 * <li>SHOULD_ARRIVE((byte) 9): 应到</li>
 * <li>ARRIVED((byte) 10): 已到</li>
 * <li>GO_OUT((byte) 11): 外出打卡</li>
 * </ul>
 */
public enum PunchStatusStatisticsItemType {
    UN_ARRIVED((byte) 1, "次"), BELATE((byte) 2, "次"), LEAVE_EARLY((byte) 3, "次"), NORMAL((byte) 4, "次"), REST((byte) 5, "天"), ABSENT((byte) 6, "天"),
    FORGOT_PUNCH((byte) 7, "次"), CHECKING((byte) 8, "次"), SHOULD_ARRIVE((byte) 9, "次"), ARRIVED((byte) 10, "次"), GO_OUT((byte) 11, "天");
    private byte code;
    private String unit;

    PunchStatusStatisticsItemType(byte code, String unit) {
        this.code = code;
        this.unit = unit;
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

    @Override
    public String toString() {
        switch (this) {
            case GO_OUT:
                return "外出";
            case UN_ARRIVED:
                return "未到";
            case BELATE:
                return "迟到";
            case LEAVE_EARLY:
                return "早退";
            case NORMAL:
                return "正常";
            case REST:
                return "休息";
            case ABSENT:
                return "旷工";
            case FORGOT_PUNCH:
                return "缺卡";
            case CHECKING:
                return "核算中";
            case SHOULD_ARRIVE:
                return "应到";
            case ARRIVED:
                return "已到";
            default:
                return "";
        }
    }

    public String getUnit() {
        return this.unit;
    }

}
