package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0: none, 1: notify group, 2: pay group</li>
 * </ul>
 * Created by ying.xiong on 2018/1/4.
 */
public enum ContractParamGroupType {
    NONE((byte)0), NOTIFY_GROUP((byte)1), PAY_GROUP((byte)2);

    private byte code;

    private ContractParamGroupType(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ContractParamGroupType fromStatus(Byte code) {
        if(code != null) {
            for(ContractParamGroupType v : ContractParamGroupType.values()) {
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
