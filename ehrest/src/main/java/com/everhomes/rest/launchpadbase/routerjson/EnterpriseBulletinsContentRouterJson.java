package com.everhomes.rest.launchpadbase.routerjson;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>bulletinId : 公告ID</li>
 * <li>bulletinTitle : 公告标题</li>
 * <li>organizationId: 总公司ID</li>
 * </ul>
 */
public class EnterpriseBulletinsContentRouterJson {

    private Long bulletinId;
    private String bulletinTitle;
    private Long organizationId;

    public Long getBulletinId() {
        return bulletinId;
    }

    public void setBulletinId(Long bulletinId) {
        this.bulletinId = bulletinId;
    }

    public String getBulletinTitle() {
        return bulletinTitle;
    }

    public void setBulletinTitle(String bulletinTitle) {
        this.bulletinTitle = bulletinTitle;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
