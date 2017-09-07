package com.everhomes.rest.openapi.shenzhou;

/**
 * Created by ying.xiong on 2017/8/12.
 */
public enum SyncFlag {
    ALL((byte)1),PART((byte)0);

    private Byte code;

    private SyncFlag(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static SyncFlag fromCode(Byte code) {
        if (code != null) {
            for (SyncFlag type : SyncFlag.values()) {
                if (type.getCode().byteValue() == code.byteValue()) {
                    return type;
                }
            }
        }
        return null;
    }
}
