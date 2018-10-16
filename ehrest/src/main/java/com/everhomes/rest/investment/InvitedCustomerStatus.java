package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

public enum InvitedCustomerStatus {

    INVALID((byte)0),CONFIG((byte)1),RUNNING((byte)2);

    private byte code;

    private InvitedCustomerStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static InvitedCustomerStatus fromCode(byte code) {
        for (InvitedCustomerStatus v : InvitedCustomerStatus.values()) {
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
