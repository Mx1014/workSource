package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

public enum InvitedCustomerType {
    INVITED_CUSTOMER((byte)0),ENTEPRIRSE_CUSTOMER((byte)1);

    private byte code;

    private InvitedCustomerType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static InvitedCustomerType fromCode(byte code) {
        for (InvitedCustomerType v : InvitedCustomerType.values()) {
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
