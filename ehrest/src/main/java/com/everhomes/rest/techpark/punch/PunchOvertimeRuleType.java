package com.everhomes.rest.techpark.punch;

import org.apache.commons.lang.StringUtils;

/**
 * <ul>
 * <li>WORKDAY: 工作日加班</li>
 * <li>HOLIDAY: 休息日、节假日加班</li>
 * </ul>
 */
public enum PunchOvertimeRuleType {
    WORKDAY("WORKDAY"), HOLIDAY("HOLIDAY");

    private String code;

    PunchOvertimeRuleType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static PunchOvertimeRuleType fromCode(String code) {
        if (!StringUtils.isBlank(code)) {
            PunchOvertimeRuleType[] values = PunchOvertimeRuleType.values();
            for (PunchOvertimeRuleType value : values) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
        }
        return null;
    }

}
