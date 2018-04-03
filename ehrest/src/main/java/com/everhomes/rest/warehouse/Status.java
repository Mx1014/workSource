package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0: inactive</li>
 *     <li>1: waiting for confirmation</li>
 *     <li>2: active</li>
 * </ul>
 * Created by ying.xiong on 2017/5/12.
 */
public enum Status {
    INACTIVE((byte)0), WAITING_FOR_CONFIRMATION((byte)1), ACTIVE((byte)2);

    private byte code;

    private Status(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static Status fromCode(Byte code) {
        if(code != null) {
            Status[] values = Status.values();
            for(Status value : values) {
                if(value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
