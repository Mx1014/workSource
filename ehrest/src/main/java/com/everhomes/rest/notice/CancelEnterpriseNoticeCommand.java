package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

/**
 * <p>撤销公告的请求</p>
 * <ul>
 * <li>organizationId : 操作人所属部门ID</li>
 * <li>id : 需要撤销的公告ID</li>
 * </ul>
 */
public class CancelEnterpriseNoticeCommand {
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
