package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>INITIALIZATION: 1 合同初始化</li>
 *     <li>EXEMPTION: ２　合同免批</li>
 *     <li>COPYCONTRACT: 3　合同复制</li>
 * </ul>
 * Created by djm on 2018/11/12.
 */
public enum ContractOperateStatus {
	INITIALIZATION((byte)1), EXEMPTION((byte)2), COPYCONTRACT((byte)3);

    private byte code;

    private ContractOperateStatus(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ContractOperateStatus fromStatus(Byte code) {
        if(code != null) {
            for(ContractOperateStatus v : ContractOperateStatus.values()) {
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
