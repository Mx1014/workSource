package com.everhomes.rest.remind;


/**
 * <p>分享人类型,目前只分享给个人</p>
 * <ul>
 * <li>MEMBER_DETAIL("MEMBER_DETAIL"): 个人/li>
 * </ul>
 */
public enum ShareMemberSourceType {
    MEMBER_DETAIL("MEMBER_DETAIL");
    private String code;

    ShareMemberSourceType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static ShareMemberSourceType fromCode(String code) {
        if (code == null) {
            return null;
        }
        ShareMemberSourceType[] values = ShareMemberSourceType.values();
        for (ShareMemberSourceType value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}