package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>rentalSiteId：场所id</li>
 * <li>rentalDate：预定日期</li>
 * <li>startTime：开始时间</li>
 * <li>endTime：结束时间</li> 
 * <li>rentalcounts：预定场所数量</li> 
 * <li>rentalItems：预定商品的json字符串</li> 
 * </ul>
 */
public class AddRentalBillCommand {
	private Long rentalSiteId;
	private Long rentalDate;
	private Long startTime;
	private Long endTime;
	private Double rentalcounts;
	private String rentalItems;
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
	public Long getRentalDate() {
		return rentalDate;
	}
	public void setRentalDate(Long rentalDate) {
		this.rentalDate = rentalDate;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Double getRentalcounts() {
		return rentalcounts;
	}
	public void setRentalcounts(Double rentalcounts) {
		this.rentalcounts = rentalcounts;
	}
	public String getRentalItems() {
		return rentalItems;
	}
	public void setRentalItems(String rentalItems) {
		this.rentalItems = rentalItems;
	}
}
