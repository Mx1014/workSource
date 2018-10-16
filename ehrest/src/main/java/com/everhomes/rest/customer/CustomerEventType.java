package com.everhomes.rest.customer;

import com.everhomes.util.StringHelper;

public enum CustomerEventType {
    CUSTOMER((byte) 0, "租客管理"), APP((byte) 1, "APP端"), PC((byte) 2, "企业信息");
    private byte code;
    private String value;

    CustomerEventType(byte code, String value) {
        this.code = code;
        this.value = value;
    }

    public static CustomerEventType fromCode(byte code) {
        for (CustomerEventType type : CustomerEventType.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    public byte getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
