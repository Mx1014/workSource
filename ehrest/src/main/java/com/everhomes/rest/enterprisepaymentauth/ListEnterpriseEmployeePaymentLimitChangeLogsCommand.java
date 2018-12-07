// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>参数:
 * <li>organizationId: 公司id</li>
 * <li>detailId: 员工detailId</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 一页数量</li>
 * </ul>
 */
public class ListEnterpriseEmployeePaymentLimitChangeLogsCommand {
    private Long organizationId;
    private Long detailId;
    private Integer pageOffset;
    private Integer pageSize;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
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
