package com.everhomes.techpark.rental;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>enterpriseCommunityId：园区id</li>
 * <li>siteType：场所类型</li>
 * <li>rentalSiteId：场所id</li>
 * <li>itemName：商品名称</li>
 * <li>itemPrice：商品价格</li>
 * <li>counts：商品数量</li> 
 * </ul>
 */
public class AddRentalSiteItemsCommand {
	private Long enterpriseCommunityId;
	private String siteType;
	private Long rentalSiteId;
	private String itemName;
	private Integer itemPrice;
	private Integer counts;
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Integer getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Integer getCounts() {
		return counts;
	}
	public void setCounts(Integer counts) {
		this.counts = counts;
	}
	public Long getEnterpriseCommunityId() {
		return enterpriseCommunityId;
	}
	public void setEnterpriseCommunityId(Long enterpriseCommunityId) {
		this.enterpriseCommunityId = enterpriseCommunityId;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
}
