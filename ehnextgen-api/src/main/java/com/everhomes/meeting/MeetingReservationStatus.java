package com.everhomes.meeting;


/**
 * <ul>
 * <li>DELETED((byte) 0): 删除状态</li>
 * <li>TIME_LOCK((byte)1): 时间锁定状态</li>
 * <li>CANCELED((byte) 2): 取消</li>
 * <li>NORMAL((byte)3): 正常状态</li>
 * </ul>
 */
public enum MeetingReservationStatus {
    DELETED((byte) 0), TIME_LOCK((byte) 1), CANCELED((byte) 2), NORMAL((byte) 3);

    private byte code;

    MeetingReservationStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static MeetingReservationStatus fromCode(Byte code) {
        if (code != null) {
            MeetingReservationStatus[] values = MeetingReservationStatus.values();
            for (MeetingReservationStatus value : values) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}
