package com.everhomes.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.techpark.punch.PunchExceptionRequestDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>rentalSiteId：场所id</li>
 * <li>enterpriseCommunityId：园区id</li>
 * <li>siteType：场所类型</li>
 * <li>rentalDate：预定日期</li>
 * <li>startTime：开始时间</li>
 * <li>endTime：结束时间</li>
 * <li>rentalSiteRuleIds：预定场所规则ID列表 json字符串</li>
 * <li>invoiceFlag：要不要发票，0 要 1 不要 参考
 * {@link com.everhomes.techpark.rental.InvoiceFlag}</li>
 * <li>rentalcount：预定场所数量</li>
 * <li>rentalItems：预定商品的json字符串</li>
 * </ul>
 */
public class AddRentalBillCommand {
	private Long rentalSiteId;
	private Long enterpriseCommunityId;
	private String siteType;
	private Long rentalDate;
	private Long startTime;
	private Long endTime;
	@ItemType(Long.class)
	private List<Long> rentalSiteRuleIds;
	private Byte invoiceFlag;
	private Double rentalcount;
	@ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> rentalItems;

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
 
	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}

	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
	}

	public Double getRentalcount() {
		return rentalcount;
	}

	public void setRentalcount(Double rentalcount) {
		this.rentalcount = rentalcount;
	}

	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}

	public List<Long> getRentalSiteRuleIds() {
		return rentalSiteRuleIds;
	}

	public void setRentalSiteRuleIds(List<Long> rentalSiteRuleIds) {
		this.rentalSiteRuleIds = rentalSiteRuleIds;
	}

	public List<SiteItemDTO> getRentalItems() {
		return rentalItems;
	}

	public void setRentalItems(List<SiteItemDTO> rentalItems) {
		this.rentalItems = rentalItems;
	}
 
 
 
}
