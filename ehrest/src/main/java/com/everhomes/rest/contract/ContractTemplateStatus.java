package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>INACTIVE: 0 未激活状态</li>
 *     <li>ACTIVE: ２　激活状态</li>
 * </ul>
 * Created by jm.ding on 2018/6/28.
 */
public enum ContractTemplateStatus {
    INACTIVE((byte)0),  ACTIVE((byte)2);

    private byte code;

    private ContractTemplateStatus(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ContractTemplateStatus fromStatus(Byte code) {
        if(code != null) {
            for(ContractTemplateStatus v : ContractTemplateStatus.values()) {
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
