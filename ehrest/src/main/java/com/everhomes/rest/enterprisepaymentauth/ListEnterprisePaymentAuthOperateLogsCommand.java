// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>pageOffset: 页码(根据上页的返回值</li>
 * <li>pageSize: 一页数量</li>
 * </ul>
 */
public class ListEnterprisePaymentAuthOperateLogsCommand {
    private Long organizationId;
    private Integer pageOffset;
    private Integer pageSize;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }
}
