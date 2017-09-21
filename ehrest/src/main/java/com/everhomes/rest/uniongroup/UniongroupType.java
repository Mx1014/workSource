package com.everhomes.rest.uniongroup;

public enum UniongroupType {
    SALARYGROUP("SALARYGROUP"), PUNCHGROUP("PUNCHGROUP");
    private String code;

    private UniongroupType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static UniongroupType fromCode(String code) {
        UniongroupType[] values = UniongroupType.values();
        for (UniongroupType value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
