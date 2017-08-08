package com.everhomes.rest.community_approve;

import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/7/19.
 */
public enum CommunityApproveStatus {
    DELETED((byte)0),INVALID((byte)1),RUNNING((byte)2);

    private byte code;

    private CommunityApproveStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static CommunityApproveStatus fromCode(byte code) {
        for (CommunityApproveStatus v : CommunityApproveStatus.values()) {
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
