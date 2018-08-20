// @formatter:off
package com.everhomes.rest.approval;

/**
 * <ul>
 * <li>HOUR("HOUR"): 小时</li>
 * <li>DAY("DAY"): 天</li>
 * </ul>
 */
public enum ApprovalCategoryTimeUnit {
    HOUR("HOUR"), DAY("DAY");

    private String code;

    ApprovalCategoryTimeUnit(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ApprovalCategoryTimeUnit fromCode(String code) {
        if (code != null) {
            for (ApprovalCategoryTimeUnit unit : ApprovalCategoryTimeUnit.values()) {
                if (unit.getCode().equalsIgnoreCase(code)) {
                    return unit;
                }
            }
        }
        return null;
    }
}
