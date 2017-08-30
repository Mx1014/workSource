package com.everhomes.rest.general_approval;

/**
 * <p>表单模板类型</p>
 * <ul>
 * <li>ARCHIVES("archives"): 人事档案表单</li>
 * </ul>
 */
public enum GeneralFormModuleType {

    ARCHIVES("archives");

    private String code;

    private GeneralFormModuleType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static GeneralFormModuleType fromCode(String code) {
        GeneralFormModuleType[] values = GeneralFormModuleType.values();
        for (GeneralFormModuleType value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
