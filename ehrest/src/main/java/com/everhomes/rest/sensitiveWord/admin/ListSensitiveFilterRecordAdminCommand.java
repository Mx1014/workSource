// @formatter:off
package com.everhomes.rest.sensitiveWord.admin;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>communityId: 项目(园区/小区)ID</li>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: 每一页的数量</li>
 * </ul>
 */
public class ListSensitiveFilterRecordAdminCommand {

    @NotNull
    private Integer namespaceId;

    private Long communityId;

    private Long pageAnchor;

    private Integer pageSize;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
