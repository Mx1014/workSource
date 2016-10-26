package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>communityId: 小区id</li>
 *     <li>billCategoryId: 表记分类项目id</li>
 *     <li>serviceCategoryId: 表记分类id</li>
 *     <li>dateStr: 时间</li>
 *     <li>statBills: 统计项目id列表 {@link com.everhomes.rest.energy.EnergyStatBill}</li>
 *     <li>meterType: 表记类型 {@link com.everhomes.rest.energy.EnergyMeterType}</li>
 * </ul>
 */
public class EnergyStatCommand {

    private Long organizationId;
    private Long communityId;
    private Long billCategoryId;
    private Long serviceCategoryId;
    private String dateStr;
    @ItemType(Byte.class)
    private List<Byte> statBills;
    private Byte meterType;

    public Long getOrganizationId() {
        return organizationId;
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

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public List<Byte> getStatBills() {
        return statBills;
    }

    public void setStatBills(List<Byte> statBills) {
        this.statBills = statBills;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
