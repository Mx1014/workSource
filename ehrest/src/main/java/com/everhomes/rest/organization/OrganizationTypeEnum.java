package com.everhomes.rest.organization;

public enum OrganizationTypeEnum {

    ENTERPRISE("ENTERPRISE"),
    PM("PM"),
    SERVICE("SERVICE");

    private String code;

    OrganizationTypeEnum(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static OrganizationGroupType fromCode(String code) {
        OrganizationGroupType[] values = OrganizationGroupType.values();
        for(OrganizationGroupType value : values) {
            if(value.getCode().equals(code)) {
                return value;
            }
        }

        return null;
    }
}
