package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/4/19.
 */
public enum AdminFlag {
    NO((byte)0), YES((byte)1);

    private byte code;

    private AdminFlag(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static AdminFlag fromStatus(byte code) {
        for(AdminFlag v : AdminFlag.values()) {
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
