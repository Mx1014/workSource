package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>TITLE_ERROE("title_error"): 模板错误</li>
 * </ul>
 */
public enum ImportFileErrorType {
    TITLE_ERROE("title_error");

    private String code;

    private ImportFileErrorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ImportFileErrorType fromCode(String code) {
        ImportFileErrorType[] values = ImportFileErrorType.values();
        for (ImportFileErrorType value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
