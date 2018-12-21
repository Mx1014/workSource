// @formatter:off
package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 * <p>参数:</p>
 * <ul>
 * <li>organizationId: 公司id，必填</li>
 * <li>appId: 应用id，必填</li>
 * <li>tagId: 标签id</li>
 * <li>pageAnchor:  锚点</li>
 * <li>pageSize: 单页数量</li>
 * </ul>
 */
public class ListMomentsCommand {
    private Long organizationId;
    private Long appId;
    private Long tagId;
    private Long pageAnchor;
    private Integer pageSize;

    public ListMomentsCommand() {

    }

    public ListMomentsCommand(Long organizationId, Long tagId, Long pageAnchor, Integer pageSize) {
        this.organizationId = organizationId;
        this.tagId = tagId;
        this.pageAnchor = pageAnchor;
        this.pageSize = pageSize;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getTag() {
        return tagId;
    }

    public void setTag(Long tagId) {
        this.tagId = tagId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
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
