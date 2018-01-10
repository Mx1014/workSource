//@formatter:off
package com.everhomes.rest.warehouse;

/**
 * Created by Wentian Wang on 2018/1/10.
 */

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:页大小</li>
 * <li>supplierId:供应商id</li>
 *</ul>
 */
public class FindMaterialLogsBySupplierCommand {
    private Long pageAnchor;
    private Integer pageSize;
    private Long supplierId;

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

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
}
