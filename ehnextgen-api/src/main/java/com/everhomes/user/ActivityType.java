package com.everhomes.user;

public enum ActivityType {
    UNKNOW((byte) 0), LOGON((byte) 1), LOGOFF((byte) 2);
    private Byte code;

    ActivityType(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return this.code;
    }

    public static ActivityType fromString(String val) {
        for (ActivityType activity : ActivityType.values()) {
            if (activity.name().equalsIgnoreCase(val)) {
                return activity;
            }

        }

        for (ActivityType activity : ActivityType.values()) {
            if (activity.getCode().toString().equals(val)) {
                return activity;
            }
        }

        return UNKNOW;
    }

}