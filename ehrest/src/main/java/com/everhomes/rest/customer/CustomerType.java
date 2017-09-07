package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>'0: enterprise; 1: individual</li>
 * </ul>
 * Created by ying.xiong on 2017/8/5.
 */
public enum CustomerType {
    ENTERPRISE((byte)0), INDIVIDUAL((byte)1);

    private byte code;

    private CustomerType(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static CustomerType fromStatus(byte code) {
        for(CustomerType v : CustomerType.values()) {
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
