package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>
 *  <li>0: inactivee</li>
 *  <li>1: active</li>
 * </ul>
 * Created by ying.xiong on 2017/4/10.
 */
public enum QRCodeFlag {
    INACTIVE((byte)0), ACTIVE((byte)1);

    private byte code;

    private QRCodeFlag(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static QRCodeFlag fromStatus(byte code) {
        for(QRCodeFlag v : QRCodeFlag.values()) {
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
