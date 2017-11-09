
package com.everhomes.rest.energy;

import com.everhomes.rest.energy.util.EnumType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>communityId: 小区id</li>
 *     <li>addressId: 门牌id</li>
 *     <li>buildingId: 楼栋id</li>
 *     <li>keyword: 关键字</li>
 *     <li>meterNumber: 表记号码</li>
 *     <li>billCategoryId: 表记项目id</li>
 *     <li>serviceCategoryId: 表记性质id</li>
 *     <li>meterType: 表记类型 {@link com.everhomes.rest.energy.EnergyMeterType}</li>
 *     <li>status: 表记状态 {@link com.everhomes.rest.energy.EnergyMeterStatus}</li>
 *     <li>pageAnchor: 下页锚点</li>
 *     <li>pageSize: 每页数量</li>
 *     <li>assignFlag: 是否关联计划 0 未关联计划； 1 已关联</li>
 * </ul>
 */
public class SearchEnergyMeterCommand {

    @NotNull private Long organizationId;
    private Long communityId;
    private String keyword;
    private String meterNumber;
    private Long billCategoryId;
    private Long serviceCategoryId;
    @EnumType(value = EnergyMeterType.class, nullValue = true)
    private Byte meterType;
    private Long addressId;
    private Long buildingId;
    private Long status;
    private Long pageAnchor;
    private Integer pageSize;
    private Integer namespaceId;
    private Byte assignFlag;

    public Byte getAssignFlag() {
        return assignFlag;
    }

    public void setAssignFlag(Byte assignFlag) {
        this.assignFlag = assignFlag;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Long getOrganizationId() {
        return organizationId;
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

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getBillCategoryId() {
        return billCategoryId;
    }

    public void setBillCategoryId(Long billCategoryId) {
        this.billCategoryId = billCategoryId;
    }

    public Long getServiceCategoryId() {
        return serviceCategoryId;
    }

    public void setServiceCategoryId(Long serviceCategoryId) {
        this.serviceCategoryId = serviceCategoryId;
    }

    public Byte getMeterType() {
        return meterType;
    }

    public void setMeterType(Byte meterType) {
        this.meterType = meterType;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

