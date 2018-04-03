package com.everhomes.rest.pmNotify;

/**
 * <ul>
 *     <li>0: 任务开始前</li>
 *     <li>1: 任务过期前</li>
 *     <li>2: 任务过期后</li>
 * </ul>
 * Created by ying.xiong on 2017/9/11.
 */
public enum PmNotifyType {
    BEFORE_START((byte)0), BEFORE_DELAY((byte)1), AFTER_DELAY((byte)2);

    private byte code;

    private PmNotifyType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PmNotifyType fromCode(Byte code) {
        if(code != null) {
            PmNotifyType[] values = PmNotifyType.values();
            for(PmNotifyType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
