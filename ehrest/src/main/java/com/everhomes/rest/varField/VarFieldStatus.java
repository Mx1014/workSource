package com.everhomes.rest.varField;

import com.everhomes.util.StringHelper;

/**
 * Created by ying.xiong on 2017/9/20.
 */
public enum VarFieldStatus {
    INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), ACTIVE((byte)2), CUSTOMIZATION((byte)3);

    private byte code;

    private VarFieldStatus(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static VarFieldStatus fromStatus(Byte code) {
        if(code != null) {
            for(VarFieldStatus v : VarFieldStatus.values()) {
                if(v.getCode() == code)
                    return v;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
