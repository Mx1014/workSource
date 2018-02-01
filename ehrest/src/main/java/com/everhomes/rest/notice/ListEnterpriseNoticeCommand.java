package com.everhomes.rest.notice;

import com.everhomes.util.StringHelper;

/**
 * <p>获取发送给我的公告列表</p>
 * <ul>
 * <li>organizationId : 公司ID</li>
 * <li>pageOffset : 查询开始页码</li>
 * <li>pageSize :每页最大获取记录数</li>
 * </ul>
 */
public class ListEnterpriseNoticeCommand {
    private Long organizationId;
    private Integer pageOffset;
    private Integer pageSize;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
