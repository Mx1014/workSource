package com.everhomes.rest.decoration;

public enum DecorationRequestStatus {
    APPLY((byte)1,"装修申请"),
    FILE_APPROVAL((byte)2,"资料审核"),
    PAYMENT((byte)3,"缴费"),
    CONSTRACT((byte)3,"进场施工"),
    CHECK((byte)4,"验收"),
    REFOUND((byte)5,"押金退回"),
    COMPLETE((byte)6,"完成");

    private byte code;
    private String describe;
    private DecorationRequestStatus(byte code,String describe) {
        this.code = code;
        this.describe = describe;
    }
    public byte getCode() {
        return this.code;
    }

    public static DecorationRequestStatus fromCode(byte code) {
        for(DecorationRequestStatus t : DecorationRequestStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }

    public String getDescribe() {
        return describe;
    }
}
