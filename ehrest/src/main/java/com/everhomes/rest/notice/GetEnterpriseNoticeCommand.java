package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

/**
 * <p>获取企业公告详情信息</p>
 * <ul>
 * <li>organizationId : 操作人所属部门ID</li>
 * <li>id : 要获取的公告ID</li>
 * </ul>
 */
public class GetEnterpriseNoticeCommand {
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
