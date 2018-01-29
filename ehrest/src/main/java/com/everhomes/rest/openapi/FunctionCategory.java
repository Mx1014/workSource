package com.everhomes.rest.openapi;

public enum FunctionCategory {

    GUILD("GUILD"), CLUB("CLUB");
    private String code;

    private FunctionCategory(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public FunctionCategory fromCode(String code) {
        for (FunctionCategory v : FunctionCategory.values()) {
            if (v.getCode().equals(code))
                return v;
        }
        return null;
    }

}
