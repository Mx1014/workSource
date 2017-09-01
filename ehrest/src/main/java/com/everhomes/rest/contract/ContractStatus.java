package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>INACTIVE: 0</li>
 *     <li>WAITING_FOR_LAUNCH: 1 待发起</li>
 *     <li>ACTIVE: ２　正常合同</li>
 *     <li>WAITING_FOR_APPROVAL: 3 审批中</li>
 *     <li>APPROVE_QUALITIED: 4 审批通过</li>
 *     <li>APPROVE_NOT_QUALITIED: 5 审批不通过</li>
 *     <li>EXPIRING: 6 即将到期</li>
 *     <li>EXPIRED: 7 已过期</li>
 *     <li>HISTORY: 8 历史合同</li>
 *     <li>INVALID: 9 作废合同</li>
 *     <li>DENUNCIATION: 10 退约合同</li>
 *     <li>DRAFT: 11 草稿</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public enum ContractStatus {
    INACTIVE((byte)0), WAITING_FOR_LAUNCH((byte)1), ACTIVE((byte)2), WAITING_FOR_APPROVAL((byte)3),
    APPROVE_QUALITIED((byte)4), APPROVE_NOT_QUALITIED((byte)5), EXPIRING((byte)6),
    EXPIRED((byte)7), HISTORY((byte)8), INVALID((byte)9), DENUNCIATION((byte)10), DRAFT((byte)11);

    private byte code;

    private ContractStatus(byte code){
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ContractStatus fromStatus(Byte code) {
        if(code != null) {
            for(ContractStatus v : ContractStatus.values()) {
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
