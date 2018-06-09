package com.everhomes.rest.rentalv2;

public enum OrderRecordType {
    RENEW((byte)1),OWNINGFEE((byte)2),NORMAL((byte)1),REFUND((byte)3);

    private byte code;
    private OrderRecordType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static OrderRecordType fromCode(byte code) {
        for(OrderRecordType t : OrderRecordType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        return null;
    }
}
