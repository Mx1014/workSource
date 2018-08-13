package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0收款合同 ,1付款合同</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public enum ContractPaymentType {
    RECEIVE((byte)0), PAY((byte)1);

    private byte code;

    private ContractPaymentType(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ContractPaymentType fromStatus(Byte code) {
        if(code != null) {
            for(ContractPaymentType v : ContractPaymentType.values()) {
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
