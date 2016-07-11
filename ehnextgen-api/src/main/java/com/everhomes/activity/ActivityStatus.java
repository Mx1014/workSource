package com.everhomes.activity;

public enum ActivityStatus {
    UNKWON(0),UN_SIGNUP(1), SIGNUP(2), CHECKEINED(3), CONFIRMED(4);
    private Integer code;

    ActivityStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static ActivityStatus fromCode(Integer code) {
        for (ActivityStatus status : ActivityStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        return UN_SIGNUP;
    }

    public static ActivityStatus fromStringCode(String code) {
        for (ActivityStatus status : ActivityStatus.values()) {
            if (status.name().equalsIgnoreCase(code)) {
                return status;
            }
        }
        return UN_SIGNUP;
    }
}
