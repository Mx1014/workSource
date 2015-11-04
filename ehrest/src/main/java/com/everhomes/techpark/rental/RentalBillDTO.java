package com.everhomes.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 订单DTO
 * <li>rentalBillId：订单id</li>
 * <li>enterpriseCommunityId：园区id</li>
 * <li>siteType：场所类型</li>
 * <li>siteName：场所名称</li>
 * <li>buildingName：楼栋名称</li>
 * <li>address：位置</li>
 * <li>spec：用户设置座位数等</li>
 * <li>companyName：场所隶属的公司</li>
 * <li>contactName：负责人id</li>
 * <li>contactPhonenum：电话号码</li>
 * <li>startTime：开始时间</li>
 * <li>endTime：结束时间</li> 
 * <li>reserveTime:预订时间</li> 
 * <li>payStartTime:支付开始时间</li> 
 * <li>payTime：支付时间</li> 
 * <li>cancelTime：取消时间</li> 
 * <li>payDeadLineTime：最后支付时间</li> 
 * <li>sitePrice：场所总价</li>
 * <li>totalPrice：全部总价</li>
 * <li>reservePrice：订金</li>
 * <li>paidPrice：已付金额</li>
 * <li>unPayPrice：未付金额</li>
 * <li>invoiceFlag：要不要发票，0 要 1 不要 参考{@link com.everhomes.techpark.rental.InvoiceFlag}</li>  
 * <li>status：订单状态  0待付订金1已付定金2已付清 3待付全款 4已取消 参考{@link com.everhomes.techpark.rental.SiteBillStatus}</li>  
 * <li>siteItems：场所商品</li>
 * </ul>
 */
public class RentalBillDTO {
	private Long rentalBillId;
	private Long enterpriseCommunityId;
	private String siteType;
	private String siteName;
	private String buildingName;
	private String address;
	private String spec;
	private String companyName;
	private String contactName;
	private String contactPhonenum;
	private Long startTime;
	private Long endTime;
	private Long reserveTime;
	private Long payStartTime;
	private Long payTime;
	private Long cancelTime;
	private Long payDeadLineTime;
	private Double sitePrice;
	private Double totalPrice;
	private Double reservePrice;
	private Double paidPrice;
	private Double unPayPrice;
	private Byte invoiceFlag;
	private Byte status;
	@ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> siteItems; 
	
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
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

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}
 

	public String getContactPhonenum() {
		return contactPhonenum;
	}

	public void setContactPhonenum(String contactPhonenum) {
		this.contactPhonenum = contactPhonenum;
	}

	public List<SiteItemDTO> getSiteItems() {
		return siteItems;
	}

	public void setSiteItems(List<SiteItemDTO> siteItems) {
		this.siteItems = siteItems;
	}
 
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}
 
	public Long getPayDeadLineTime() {
		return payDeadLineTime;
	}

	public void setPayDeadLineTime(Long payDeadLineTime) {
		this.payDeadLineTime = payDeadLineTime;
	}

	public Long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public Long getPayTime() {
		return payTime;
	}

	public void setPayTime(Long payTime) {
		this.payTime = payTime;
	}

	public Long getPayStartTime() {
		return payStartTime;
	}

	public void setPayStartTime(Long payStartTime) {
		this.payStartTime = payStartTime;
	}

	public Long getReserveTime() {
		return reserveTime;
	}

	public void setReserveTime(Long reserveTime) {
		this.reserveTime = reserveTime;
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

	public Long getRentalBillId() {
		return rentalBillId;
	}

	public void setRentalBillId(Long rentalBillId) {
		this.rentalBillId = rentalBillId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Double getSitePrice() {
		return sitePrice;
	}

	public void setSitePrice(Double sitePrice) {
		this.sitePrice = sitePrice;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getReservePrice() {
		return reservePrice;
	}

	public void setReservePrice(Double reservePrice) {
		this.reservePrice = reservePrice;
	}

	public Double getPaidPrice() {
		return paidPrice;
	}

	public void setPaidPrice(Double paidPrice) {
		this.paidPrice = paidPrice;
	}

	public Double getUnPayPrice() {
		return unPayPrice;
	}

	public void setUnPayPrice(Double unPayPrice) {
		this.unPayPrice = unPayPrice;
	}
}
