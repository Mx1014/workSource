package com.everhomes.visitorsys.jsst;

public enum JsstVisitorType {

    FRIEND((byte)0,"亲朋好友"),
    DELIVER((byte)1,"外卖"),
    HOUSEKEEPING((byte)2,"家政"),
    OTHERS((byte)3,"更多");

    private Byte code;
    private String type;

    JsstVisitorType(Byte code, String type) {
        this.code = code;
        this.type = type;
    }

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static JsstVisitorType fromCode(Byte code) {
        if (code != null) {
            for (JsstVisitorType visitorType : JsstVisitorType.values()) {
                if (visitorType.getCode() == code.byteValue()) {
                    return visitorType;
                }
            }
        }
        return null;
    }

}
