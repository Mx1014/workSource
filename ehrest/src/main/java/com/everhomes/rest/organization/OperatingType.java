package com.everhomes.rest.organization;

public enum OperatingType {
    ADD("add"), UPDATE("update"), DELETE("delete");

    private String code;
    private OperatingType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static OperatingType fromCode(String code) {
        if(code != null) {
            OperatingType[] values = OperatingType.values();
            for(OperatingType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}

