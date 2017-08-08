package com.everhomes.community_approve;

/**
 * Created by zhengsiting on 2017/7/27.
 */
public enum CommunityApproveTranEnum {
    USER_NAME("姓名"),USER_PHONE("联系电话");

    private String code;

    private CommunityApproveTranEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static CommunityApproveTranEnum fromCode(String code) {
        if (code != null && !code.isEmpty()) {
            for (CommunityApproveTranEnum expressActionEnum : CommunityApproveTranEnum.values()) {
                if (expressActionEnum.getCode().equals(code)) {
                    return expressActionEnum;
                }
            }
        }
        return null;
    }
}
