package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0:新签合同、1:续约合同、2:变更合同</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public enum ContractType {
    NEW((byte)0), RENEW((byte)1), CHANGE((byte)2);

    private byte code;

    private ContractType(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ContractType fromStatus(byte code) {
        for(ContractType v : ContractType.values()) {
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
