// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parentId: 区域父id,查省列表传0，查市列表，parentid为省id</li>
 * <li>nextPageAnchor: 下一页锚点</li>
 * <li>pageSize: 页大小</li>
 * </ul>
 */
public class ListRegionsCommand {
    private Long parentId;
    private Long nextPageAnchor;
    private Integer pageSize;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}



