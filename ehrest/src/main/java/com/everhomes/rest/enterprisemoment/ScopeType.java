package com.everhomes.rest.enterprisemoment;

/**
 * <ul>
 * <li>ORGANIZATION: ORGANIZATION 公司</li>
 * <li>MEMBERDETAIL: MEMBERDETAIL 员工</li>
 * </ul>
 */
public enum ScopeType {
    ORGANIZATION("ORGANIZATION"),MEMBERDETAIL("MEMBERDETAIL");
    private String code;

    ScopeType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ScopeType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (ScopeType status : ScopeType.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
