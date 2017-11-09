package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>1: 调租; 2: 免租</li>
 * </ul>
 * Created by ying.xiong on 2017/10/10.
 */
public enum ChangeType {
    ADJUST((byte)1), FREE((byte)2);
    private byte code;

    private ChangeType(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ChangeType fromStatus(byte code) {
        for(ChangeType v : ChangeType.values()) {
            if(v.getCode() == code)
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
