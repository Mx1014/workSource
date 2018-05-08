package com.everhomes.rest.customer;

/**
 * Created by ying.xiong on 2018/4/9.
 */
public enum SyncResultViewedFlag {
    NOT_VIEWED((byte)0), VIEWED((byte)1);

    private byte code;
    private SyncResultViewedFlag(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static SyncResultViewedFlag fromCode(Byte code) {
        if(code != null) {
            for(SyncResultViewedFlag value : SyncResultViewedFlag.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
