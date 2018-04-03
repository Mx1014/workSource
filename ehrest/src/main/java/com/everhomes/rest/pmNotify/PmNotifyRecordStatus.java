package com.everhomes.rest.pmNotify;

/**
 * <ul>
 *     <li>0: invalid, 1: waiting for send out, 2: already sended</li>
 * </ul>
 * Created by ying.xiong on 2017/9/12.
 */
public enum PmNotifyRecordStatus {
    INVAILD((byte)0), WAITING_FOR_SEND_OUT((byte)1), ALREADY_SENDED((byte)2);

    private byte code;

    private PmNotifyRecordStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PmNotifyRecordStatus fromCode(Byte code) {
        if(code != null) {
            PmNotifyRecordStatus[] values = PmNotifyRecordStatus.values();
            for(PmNotifyRecordStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
