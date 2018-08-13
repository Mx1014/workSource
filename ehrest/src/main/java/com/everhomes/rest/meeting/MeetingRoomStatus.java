package com.everhomes.rest.meeting;


/**
 * <ul>
 * <li>DELETED((byte) 0): 删除状态</li>
 * <li>CLOSED((byte) 1): 关闭</li>
 * <li>OPENING((byte) 2): 开启</li>
 * </ul>
 */
public enum MeetingRoomStatus {
    DELETED((byte) 0), CLOSED((byte) 1), OPENING((byte) 2);

    private byte code;

    MeetingRoomStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static MeetingRoomStatus fromCode(Byte code) {
        if (code != null) {
            MeetingRoomStatus[] values = MeetingRoomStatus.values();
            for (MeetingRoomStatus value : values) {
                if (code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}
