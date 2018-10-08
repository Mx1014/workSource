package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

public enum CustomerLevelType {
    FIRST_CUSTOMER((byte)3),POTENTIAL_CUSTOMER((byte)4),INTENTIONAL_CUSTOMER((byte)5),
    REGISTERED_CUSTOMER((byte)6),LOSS_CUSTOMER((byte)7);

    private byte code;

    private CustomerLevelType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static CustomerLevelType fromCode(byte code) {
        for (CustomerLevelType v : CustomerLevelType.values()) {
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
