//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billId:账单id</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:每页数量</li>
 *</ul>
 */
public class ListSettledBillItemsCommand {
    private Long billId;
    private Long pageAnchor;
    private Integer pageSize;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

    public ListSettledBillItemsCommand() {

    }
}
