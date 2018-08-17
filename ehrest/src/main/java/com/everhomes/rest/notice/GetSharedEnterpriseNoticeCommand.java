package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

/**
 * <p>获取企业公告详情信息</p>
 * <ul>
 * <li>noticeToken : 企业公告token</li>
 * </ul>
 */
public class GetSharedEnterpriseNoticeCommand {
    private String noticeToken;

    public String getNoticeToken() {
        return noticeToken;
    }

    public void setNoticeToken(String noticeToken) {
        this.noticeToken = noticeToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
