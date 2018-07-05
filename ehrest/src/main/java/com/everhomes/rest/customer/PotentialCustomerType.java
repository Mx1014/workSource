package com.everhomes.rest.customer;

/**
 * Created by Rui.Jia  2018/6/29 15 :04
 */

public enum PotentialCustomerType {
    ACTIVITY((byte) 0, "ACTIVITY"), SERVICE_ALLIANCE((byte) 1, "SERVICE_ALLIANCE");
    private byte code;
    private String value;

    PotentialCustomerType(byte code, String value) {
        this.code = code;
        this.value = value;
    }

    public byte getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static PotentialCustomerType fromCode(byte code) {
        for (PotentialCustomerType customerType : PotentialCustomerType.values()) {
            if (customerType.getCode() == code) {
                return customerType;
            }
        }
        return null;
    }
}
