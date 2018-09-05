package com.everhomes.rest.investment;

import com.everhomes.rest.general_approval.GeneralFormStatus;
import com.everhomes.util.StringHelper;

public enum InvestmentEnterpriseType {
    INVESTMENT_ENTERPRISE((byte)0),ENTEPRIRSE_CUSTOMER((byte)1);

    private byte code;

    private InvestmentEnterpriseType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static InvestmentEnterpriseType fromCode(byte code) {
        for (InvestmentEnterpriseType v : InvestmentEnterpriseType.values()) {
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
