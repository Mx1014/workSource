package com.everhomes.rest.enterpriseApproval;

public enum ApprovalFilterType {

    APPROVAL("APPROVAL"), GROUP("GROUP");

    private String code;
     ApprovalFilterType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ApprovalFilterType fromCode(String code) {
        if(code != null) {
            ApprovalFilterType[] values = ApprovalFilterType.values();
            for(ApprovalFilterType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
