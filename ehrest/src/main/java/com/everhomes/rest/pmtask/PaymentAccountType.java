package com.everhomes.rest.pmtask;

public enum PaymentAccountType {
    User((byte)0,"Users"),Organization((byte)1,"EhOrganizations");

    private byte code;
    private String name;

    PaymentAccountType(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public byte getCode() {
        return this.code;
    }

    public static Byte getCode(String name){
        if(name != null) {
            PaymentAccountType[] values = PaymentAccountType.values();
            for(PaymentAccountType value : values) {
                if(value.name == name) {
                    return value.code;
                }
            }
        }
        return null;
    }

    public static String fromCode(Byte code) {
        if(code != null) {
            PaymentAccountType[] values = PaymentAccountType.values();
            for(PaymentAccountType value : values) {
                if(value.code == code) {
                    return value.name;
                }
            }
        }
        return null;
    }
}
