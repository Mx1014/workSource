package com.everhomes.activity;

public enum YzbDeviceState {
    UN_READY((byte)0), LIVING((byte)1), READY((byte)2), INVALID((byte)3);
    private Byte code;

    YzbDeviceState(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static YzbDeviceState fromCode(Byte code) {
        for (YzbDeviceState status : YzbDeviceState.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return UN_READY;
    }
}
