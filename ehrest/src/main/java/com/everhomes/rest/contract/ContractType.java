package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>0:新签合同、1:续约合同、2:变更合同</li>
 * </ul>
 * Created by ying.xiong on 2017/8/3.
 */
public enum ContractType {
    NEW((byte)0,"新签合同"), RENEW((byte)1,"续约合同"), CHANGE((byte)2,"变更合同"), DENUNCIATION((byte)3,"退约合同");

    private byte code;
    private String description;

    ContractType(byte code, String description) {
        this.code = code;
        this.description = description;
    }

    public byte getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ContractType fromStatus(Byte code) {
        if(code != null) {
            for(ContractType v : ContractType.values()) {
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
