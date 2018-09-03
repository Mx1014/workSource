package com.everhomes.rest.investment;

import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.util.StringHelper;

public enum InvestmentEnterpriseStatus {

    INVALID((byte)0),CONFIG((byte)1),RUNNING((byte)2);

    private byte code;

    private InvestmentEnterpriseStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static GeneralFormStatus fromCode(byte code) {
        for (GeneralFormStatus v : GeneralFormStatus.values()) {
            if (v.getCode() == code)
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
