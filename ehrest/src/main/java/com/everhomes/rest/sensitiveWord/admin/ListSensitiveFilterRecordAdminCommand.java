// @formatter:off
package com.everhomes.rest.sensitiveWord.admin;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>namespaceId: 域空间ID</li>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: 每一页的数量</li>
 * </ul>
 */
public class ListSensitiveFilterRecordAdminCommand {

    @NotNull
    private Long namespaceId;

    private Long pageAnchor;

    private Integer pageSize;

    public Long getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Long namespaceId) {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
