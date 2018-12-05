// @formatter:off
package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>sceneAppId: 支付场景id</li>
 * <li>pageOffset: 页码，默认为1</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListEnterprisePaymentSceneEmployeeLimitCommand {
    private Long organizationId;
    private Long sceneAppId;
    private Integer pageOffset;
    private Integer pageSize;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getSceneAppId() {
        return sceneAppId;
    }

    public void setSceneAppId(Long sceneAppId) {
        this.sceneAppId = sceneAppId;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
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
