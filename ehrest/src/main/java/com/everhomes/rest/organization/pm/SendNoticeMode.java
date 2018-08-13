package com.everhomes.rest.organization.pm;

/**
 * <ul>
 *     <li>NAMESPACE("NAMESPACE"): 域空间</li>
 *     <li>MOBILE("MOBILE"): 手机号</li>
 *     <li>COMMUNITY("COMMUNITY"): 项目</li>
 * </ul>
 */
public enum SendNoticeMode {

    NAMESPACE("NAMESPACE"),
    MOBILE("MOBILE"),
    COMMUNITY("COMMUNITY"),
    ;

    private String code;

    SendNoticeMode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static SendNoticeMode fromCode(String code) {
        if (code != null) {
            for (SendNoticeMode mode : SendNoticeMode.values()) {
                if (mode.code.equals(code)) {
                    return mode;
                }
            }
        }
        return null;
    }
}
