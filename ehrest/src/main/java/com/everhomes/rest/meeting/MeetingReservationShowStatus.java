package com.everhomes.rest.meeting;


/**
 * <ul>
 * <li>EDIT((byte) 0): 编辑状态</li>
 * <li>STARTING((byte) 1): 进行中</li>
 * <li>COMING_SOON((byte) 2): 未开始</li>
 * <li>COMPLETED((byte) 3): 已结束</li>
 * <li>CANCELED((byte) 4): 已取消</li>
 * <li>BE_OCCUPIED((byte) 5): 已被预订</li>
 * <li>BE_LOCK_UP((byte) 6): 锁定中</li>
 * </ul>
 */
public enum MeetingReservationShowStatus {
    EDIT((byte) 0), STARTING((byte) 1), COMING_SOON((byte) 2), COMPLETED((byte) 3), CANCELED((byte) 4), BE_OCCUPIED((byte) 5), BE_LOCK_UP((byte) 6);

    private byte code;

    MeetingReservationShowStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static MeetingReservationShowStatus fromCode(Byte code) {
        if (code != null) {
            MeetingReservationShowStatus[] values = MeetingReservationShowStatus.values();
            for (MeetingReservationShowStatus value : values) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }

    public String toString() {
        switch (this) {
            case STARTING:
                return "进行中";
            case COMING_SOON:
                return "未开始";
            case COMPLETED:
                return "已结束";
            case CANCELED:
                return "已取消";
            case BE_OCCUPIED:
                return "已被预订";
            case BE_LOCK_UP:
                return "锁定中";
            default:
                return "";
        }
    }

}
