package com.everhomes.activity;

public enum CheckInStatus {
    CHECKIN((byte) 1), UN_CHECKIN((byte) 0);
    private byte code;

    CheckInStatus(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static CheckInStatus fromCode(Byte code) {
        for (CheckInStatus flag : CheckInStatus.values()) {
            if (flag.code == code) {
                return flag;
            }
        }
        return UN_CHECKIN;
    }

}
