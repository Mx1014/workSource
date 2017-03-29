package com.everhomes.rest.rentalv2.admin;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 给资源增加单元格
 * <li>rentalSiteId: 资源id</li> 
 * <li>beginDate: 开放日期始</li>
 * <li>endDate: 开放日期终</li>
 * <li>openWeekday: 开放日期，从周日到周六是0123456，开放哪天就在数组传哪天List< Integer></li>
 * <li>closeDates: 关闭日期</li>
 * </ul>
 */
public class UpdateRentalDateCommand {
	@NotNull
	private Long rentalSiteId; 
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
	public Long getRentalSiteId() {
		return rentalSiteId;
	}
	public void setRentalSiteId(Long rentalSiteId) {
		this.rentalSiteId = rentalSiteId;
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
