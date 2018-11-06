package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

/**
 * <p>取消置顶公告的请求</p>
 * <ul>
 * <li>organizationId : 总公司ID</li>
 * <li>id : 取消置顶的公告ID</li>
 * </ul>
 */
public class UnStickyEnterpriseNoticeCommand {
    private Long organizationId;
    private Long id;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
