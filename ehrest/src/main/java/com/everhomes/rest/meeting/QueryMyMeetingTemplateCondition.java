package com.everhomes.rest.meeting;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>organizationId: 总公司ID</li>
 * <li>userId: 所属人的用户id</li>
 * <li>offset: 偏移量</li>
 * <li>pageSize: 每页返回记录数</li>
 * </ul>
 */
public class QueryMyMeetingTemplateCondition {
    private Integer namespaceId;
    private Long organizationId;
    private Long userId;
    private Integer offset;
    private Integer pageSize;

    public QueryMyMeetingTemplateCondition() {

    }

    public QueryMyMeetingTemplateCondition(Integer namespaceId, Long organizationId, Long userId, Integer offset, Integer pageSize) {
        this.namespaceId = namespaceId;
        this.organizationId = organizationId;
        this.userId = userId;
        this.offset = offset;
        this.pageSize = pageSize;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
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
