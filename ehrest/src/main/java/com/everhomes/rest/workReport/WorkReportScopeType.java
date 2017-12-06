package com.everhomes.rest.workReport;

/**
 * <ul>
 * <li>DEPARTMENT("department"): 部门</li>
 * <li>POSITION("position"): 岗位</li>
 * <li>USER("user"): 用户</li>
 * </ul>
 */
public enum WorkReportScopeType {

    DEPARTMENT("department"), POSITION("position"), USER("user");

    private String code;

    private WorkReportScopeType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static WorkReportScopeType fromCode(String code) {
        if (code != null) {
            WorkReportScopeType[] values = WorkReportScopeType.values();
            for (WorkReportScopeType value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}