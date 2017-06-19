package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>communityId: 小区id</li>
 *     <li>keyword: 关键字</li>
 *     <li>meterNumber: 表记号码</li>
 *     <li>operatorName: 抄表人</li>
 *     <li>meterId: 表记id</li>
 *     <li>billCategoryId: 表记项目id</li>
 *     <li>serviceCategoryId: 表记性质id</li>
 *     <li>meterType: 表记类型 {@link com.everhomes.rest.energy.EnergyMeterType}</li>
 *     <li>startTime: 开始时间</li>
 *     <li>endTime: 结束时间</li>
 *     <li>pageAnchor: 下页锚点</li>
 *     <li>pageSize: 每页数量</li>
 * </ul>
 */
public class SearchEnergyMeterReadingLogsCommand {

    @NotNull private Long organizationId;
    private Long communityId;
    private String keyword;
    private String meterNumber;
    private String operatorName;
    private Long meterId;
    private Long billCategoryId;
    private Long serviceCategoryId;
    private Byte meterType;

    private Long startTime;
    private Long endTime;

    private Long pageAnchor;
    private Integer pageSize;

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

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
