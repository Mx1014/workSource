package com.everhomes.rest.share;

/**
 * <ul>
 *     <li>POST("POST"): 帖子</li>
 *     <li>ACTIVITY("ACTIVITY"): 活动</li>
 * </ul>
 */
public enum ShareType {

    POST("POST"),
    ACTIVITY("ACTIVITY"),
    ;

    private String code;

    ShareType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ShareType fromCode(String code) {
        if (code != null) {
            for (ShareType type : ShareType.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
