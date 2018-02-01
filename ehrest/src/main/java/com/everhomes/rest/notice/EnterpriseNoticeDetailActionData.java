package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>bulletinId : 公告ID</li>
 * <li>showType:是否预览，请查看{@link EnterpriseNoticeShowType}</li>
 * </ul>
 */
public class EnterpriseNoticeDetailActionData {
    private Long bulletinId;
    private Byte showType;

    public Long getBulletinId() {
        return bulletinId;
    }

    public void setBulletinId(Long bulletinId) {
        this.bulletinId = bulletinId;
    }

    public Byte getShowType() {
        return showType;
    }

    public void setShowType(Byte showType) {
        this.showType = showType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
