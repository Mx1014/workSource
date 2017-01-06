//@formatter:off
package com.everhomes.rest.pmkexing;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
 *     <li>billStatus: 账单状态{@link com.everhomes.rest.pmkexing.PmKeXingBillStatus}</li>
 *     <li>pageSize: 每页显示条数</li>
 *     <li>pageOffset: 页码(一碑提供的接口使用页码分页方式)</li>
 * </ul>
 */
public class ListPmKeXingBillsCommand {

    private Long organizationId;
    private Byte billStatus;
    private Integer pageSize;
    private Integer pageOffset;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Byte getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
