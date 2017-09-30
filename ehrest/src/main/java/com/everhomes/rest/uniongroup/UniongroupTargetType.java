package com.everhomes.rest.uniongroup;


/**
 * <p>组目标类型</p>
 * <ul>
 * <li>MEMBERDETAIL("MEMBERDETAIL"): 个人/li>
 * <li>ORGANIZATION("ORGANIZATION"): 组织</li>
 * </ul>
 */
public enum UniongroupTargetType {
    MEMBERDETAIL("MEMBERDETAIL"), ORGANIZATION("ORGANIZATION");
    private String code;

    private UniongroupTargetType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static UniongroupTargetType fromCode(String code) {
        UniongroupTargetType[] values = UniongroupTargetType.values();
        for (UniongroupTargetType value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}