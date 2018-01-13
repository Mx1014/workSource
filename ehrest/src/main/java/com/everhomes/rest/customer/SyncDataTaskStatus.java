package com.everhomes.rest.customer;

/**
 * Created by ying.xiong on 2018/1/13.
 */
public enum SyncDataTaskStatus {
    CREATED((byte)1), EXECUTING((byte)2), FINISH((byte)3), EXCEPTION((byte)4);

    private byte code;
    private SyncDataTaskStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static SyncDataTaskStatus fromCode(Byte code) {
        if(code != null) {
            for(SyncDataTaskStatus value : SyncDataTaskStatus.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
