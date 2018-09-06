package com.everhomes.rest.techpark.punch;

public enum PunchDayType {
    WORKDAY((byte) 1), RESTDAY((byte) 2), HOLIDAY((byte) 3);

    private byte code;

    PunchDayType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PunchDayType fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        for (PunchDayType type : PunchDayType.values()) {
            if (type.code == code.byteValue()) {
                return type;
            }
        }
        return null;
    }
}
