package com.everhomes.rest.pmNotify;

/**
 * <ul>
 *     <li>0: 一次提醒</li>
 *     <li>1: 周期循环提醒</li>
 * </ul>
 * Created by ying.xiong on 2017/9/11.
 */
public enum PmNotifyRepeatType {
    ONCE((byte)0), REPEAT((byte)1);

    private byte code;

    private PmNotifyRepeatType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PmNotifyRepeatType fromCode(Byte code) {
        if(code != null) {
            PmNotifyRepeatType[] values = PmNotifyRepeatType.values();
            for(PmNotifyRepeatType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
