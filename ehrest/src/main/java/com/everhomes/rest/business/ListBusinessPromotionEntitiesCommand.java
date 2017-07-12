// @formatter:off
package com.everhomes.rest.business;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>pageSize: 显示数量</li>
 *     <li>pageAnchor: 下页锚点</li>
 * </ul>
 */
public class ListBusinessPromotionEntitiesCommand {

    private Integer pageSize;
    private Long pageAnchor;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
