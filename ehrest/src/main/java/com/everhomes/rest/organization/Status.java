package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>1: inactive</li>
 *     <li>2: active</li>
 * </ul>
 */
public enum Status {
    INACTIVE((byte)1), ACTIVE((byte)2);

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
