package com.everhomes.rest.techpark.punch.admin;

public enum PunchTargetType {
    PUNCHGROUP("PUNCHGROUP"),USER("USER_DETAIL");
    private String code;

    private PunchTargetType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static PunchTargetType fromCode(String code) {
        PunchTargetType[] values = PunchTargetType.values();
        for (PunchTargetType value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
