package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>0: no</li>
 *  <li>1: yes</li>
 * </ul>
 * Created by ying.xiong on 2017/5/17.
 */
public enum DeliveryFlag {
    NO((byte)0), YES((byte)1);
    private byte code;

    private DeliveryFlag(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static DeliveryFlag fromStatus(byte code) {
        for(DeliveryFlag v : DeliveryFlag.values()) {
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
