package com.everhomes.rest.energy;

/**
 * Created by ying.xiong on 2018/1/6.
 */
public enum SyncOfflineTaskStatus {

    CREATED((byte)1), EXECUTING((byte)2), FINISH((byte)3), EXCEPTION((byte)4);

    private byte code;
    private SyncOfflineTaskStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static SyncOfflineTaskStatus fromCode(Byte code) {
        if(code != null) {
            for(SyncOfflineTaskStatus value : SyncOfflineTaskStatus.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
