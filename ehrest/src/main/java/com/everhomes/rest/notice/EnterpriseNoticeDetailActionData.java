package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>bulletinId : 公告ID</li>
 * </ul>
 */
public class EnterpriseNoticeDetailActionData {
    private Long bulletinId;

    public Long getBulletinId() {
        return bulletinId;
    }

    public void setBulletinId(Long bulletinId) {
        this.bulletinId = bulletinId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
