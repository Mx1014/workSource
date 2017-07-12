package com.everhomes.rest.rentalv2.admin;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 添加默认规则
 * <li>ownerType: 所有者类型 参考
 * {@link com.everhomes.rest.rentalv2.RentalOwnerType}</li>
 * <li>ownerId: 园区id</li>
 * <li>resourceTypeId: 图标id</li> 
 * <li>timeIntervals: 开放时段</li>
 * <li>beginDate: 开放日期始</li>
 * <li>endDate: 开放日期终</li>
 * <li>openWeekday: 开放日期，从周日到周六是0123456，开放哪天就在数组传哪天</li>
 * <li>closeDates: 关闭日期</li> 
 * </ul>
 */
public class UpdateDefaultDateRuleAdminCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Long resourceTypeId; 
	private Long beginDate;
	private Long endDate;

	@ItemType(Integer.class)
	private List<Integer> openWeekday;
	@ItemType(Long.class)
	private List<Long> closeDates; 

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public Long getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Long beginDate) {
		this.beginDate = beginDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public List<Integer> getOpenWeekday() {
		return openWeekday;
	}

	public void setOpenWeekday(List<Integer> openWeekday) {
		this.openWeekday = openWeekday;
	}

	public List<Long> getCloseDates() {
		return closeDates;
	}

	public void setCloseDates(List<Long> closeDates) {
		this.closeDates = closeDates;
	} 
}
