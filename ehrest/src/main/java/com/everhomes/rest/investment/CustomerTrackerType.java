package com.everhomes.rest.investment;

import com.everhomes.util.StringHelper;

public enum CustomerTrackerType {

    INVITED_TRACKER((byte)0), ENTERPRISE_TRACKER((byte)1);

    private byte code;

    private CustomerTrackerType(byte code) {
        this.code = code;
    }


    public byte getCode() {
        return code;
    }

    public static CustomerTrackerType fromCode(byte code) {
        for (CustomerTrackerType v : CustomerTrackerType.values()) {
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
