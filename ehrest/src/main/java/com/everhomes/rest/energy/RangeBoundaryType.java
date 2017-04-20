package com.everhomes.rest.energy;

/**
 * Created by ying.xiong on 2017/3/20.
 */
public enum RangeBoundaryType {
    LESS("<"), LESS_AND_EQUAL("<="), GREATER(">"), GREATER_AND_EQUAL(">=");

    private String code;

    public String getCode() {
        return code;
    }

    RangeBoundaryType(String code) {
        this.code = code;
    }

    public static RangeBoundaryType fromCode(String code) {
        for (RangeBoundaryType type : RangeBoundaryType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
