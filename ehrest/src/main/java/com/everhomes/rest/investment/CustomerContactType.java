package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

public enum CustomerContactType {
    CUSTOMER_CONTACT((byte)0), CHANNEL_CONTACT((byte)1);

    private byte code;

    private CustomerContactType(byte code) {
        this.code = code;
    }


    public byte getCode() {
        return code;
    }

    public static CustomerContactType fromCode(byte code) {
        for (CustomerContactType v : CustomerContactType.values()) {
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
