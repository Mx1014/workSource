package com.everhomes.rest.energy;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>communityId: 小区id</li>
 *     <li>billCategoryIds: 表记分类项目ids</li>
 *     <li>serviceCategoryIds: 性质-表记分类ids</li>
 *     <li>statDate: 查询时间</li>
 *     <li>statBills: 统计项目id列表 {@link com.everhomes.rest.energy.EnergyStatBill}</li>
 *     <li>meterType: 表记类型 {@link com.everhomes.rest.energy.EnergyMeterType}</li>
 * </ul>
 */
public class EnergyStatCommand {

    private Long organizationId;
    private Long communityId;
    @ItemType(Long.class)
    private List<Long> billCategoryIds;
    @ItemType(Long.class)
    private List<Long> serviceCategoryIds;
    private Long statDate;
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


	public List<Long> getBillCategoryIds() {
		return billCategoryIds;
	}


	public void setBillCategoryIds(List<Long> billCategoryIds) {
		this.billCategoryIds = billCategoryIds;
	}


	public List<Long> getServiceCategoryIds() {
		return serviceCategoryIds;
	}


	public void setServiceCategoryIds(List<Long> serviceCategoryIds) {
		this.serviceCategoryIds = serviceCategoryIds;
	}


	public Long getStatDate() {
		return statDate;
	}


	public void setStatDate(Long statDate) {
		this.statDate = statDate;
	}


	public List<Byte> getStatBills() {
		return statBills;
	}


	public void setStatBills(List<Byte> statBills) {
		this.statBills = statBills;
	}


	public Byte getMeterType() {
		return meterType;
	}


	public void setMeterType(Byte meterType) {
		this.meterType = meterType;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
