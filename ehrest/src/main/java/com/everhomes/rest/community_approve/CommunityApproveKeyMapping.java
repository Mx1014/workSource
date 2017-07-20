package com.everhomes.rest.community_approve;

import com.everhomes.util.StringHelper;

/**
 * Created by zhengsiting on 2017/7/20.
 */
public enum CommunityApproveKeyMapping {
    APPROVE_NAME("审批名称"),CREATE_TIME("申请时间");

    private String code;

    private CommunityApproveKeyMapping(String code){this.code = code;}

    public String getCode() {
        return code;
    }

    public static CommunityApproveKeyMapping fromCode(String code) {
        for (CommunityApproveKeyMapping v : CommunityApproveKeyMapping.values()) {
            if (v.getCode() == code)
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
