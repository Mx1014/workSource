package com.everhomes.rest.oauth2;

public enum ControlTargetOption {
    ALL_COMMUNITY("0"),SPECIFIC_COMMUNITIES("1"),CURRENT_ORGANIZATION("2"), CURRENT_DEPARTMENT("3"), SPECIFIC_orgs("4");

    private String code;
    private ControlTargetOption(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ControlTargetOption fromCode(String code) {
        if (code != null) {
            for (ControlTargetOption controlTargetOption : ControlTargetOption.values()) {
                if (controlTargetOption.code.equalsIgnoreCase(code)) {
                    return controlTargetOption;
                }
            }
        }

        return null;
    }
}
