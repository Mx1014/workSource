package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * Created by zhengsiting on 2017/8/3.
 * 0:删除标志位
 * 1:正常跳转
 * 2:审批
 */
public enum SignalEnum {
    DELETE((byte)0),NORMAL((byte)1),APPROVAL((byte)2);

    private Byte code;
    private SignalEnum(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static SignalEnum fromCode(Byte code) {
        for(SignalEnum v : SignalEnum.values()){
            if(v.getCode().equals(code))
                return v;
        }
        return null;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
