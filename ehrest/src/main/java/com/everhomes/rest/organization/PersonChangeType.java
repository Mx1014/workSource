package com.everhomes.rest.organization;


/**
 * <ul>
 * <li>ENTRY: 入职</li>
 * <li>POSITIVE: 转正</li>
 * <li>DEPCHANGE: 部门变动</li>
 * <li>POICHANGE: 职位变动</li>
 * <li>LEVCHANGE: 职级变动</li>
 * <li>LEAVE: 离职</li>
 * </ul>
 */
public enum PersonChangeType {
    ENTRY("0"), POSITIVE("1"), DEPCHANGE("2"),POICHANGE("3"),LEVCHANGE("4"), LEAVE("5");

    private String code;
    private PersonChangeType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static PersonChangeType fromCode(String code) {
        if(code != null) {
            PersonChangeType[] values = PersonChangeType.values();
            for(PersonChangeType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
