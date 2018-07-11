package com.everhomes.rest.enterpriseApproval;

public enum EnterpriseApprovalGroupAttribute {
    DEFAULT("DEFAULT"), CUSTOMIZE("CUSTOMIZE");

    private String code;
    private EnterpriseApprovalGroupAttribute(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static EnterpriseApprovalGroupAttribute fromCode(String code) {
        if(code != null) {
            EnterpriseApprovalGroupAttribute[] values = EnterpriseApprovalGroupAttribute.values();
            for(EnterpriseApprovalGroupAttribute value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
