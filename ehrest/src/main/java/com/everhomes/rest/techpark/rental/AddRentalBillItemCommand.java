package com.everhomes.rest.techpark.rental;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchExceptionRequestDTO;
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
 * {@link com.everhomes.rest.techpark.rental.InvoiceFlag}</li>
 * <li>rentalcount：预定场所数量</li>
 * <li>rentalItems：预定商品</li>
 * <li>rentalAttanchments：预定附件，如车牌号，文字信息等</li>
 * <li>attachmentType：附件类型 0：字符串 1：邮件 2：文件
 * {@link com.everhomes.rest.techpark.rental.AttachmentType}</li>
 * </ul>
 */
public class AddRentalBillItemCommand {
	@NotNull
	private Long rentalSiteId;
	private Long communityId;
	private String ownerType;
	private Long ownerId;
	@NotNull
	private Byte invoiceFlag;
	@NotNull
	private String siteType;
	@NotNull
	private Long rentalBillId; 
	@ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> rentalItems;
	@ItemType(String.class)
	private List<String> rentalAttachments;
	private Byte attachmentType;

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

	 
 
	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	 
	public Long getRentalBillId() {
		return rentalBillId;
	}

	public void setRentalBillId(Long rentalBillId) {
		this.rentalBillId = rentalBillId;
	}

	public List<SiteItemDTO> getRentalItems() {
		return rentalItems;
	}

	public void setRentalItems(List<SiteItemDTO> rentalItems) {
		this.rentalItems = rentalItems;
	}

	public Byte getInvoiceFlag() {
		return invoiceFlag;
	}

	public void setInvoiceFlag(Byte invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}

	public List<String> getRentalAttachments() {
		return rentalAttachments;
	}

	public void setRentalAttachments(List<String> rentalAttachments) {
		this.rentalAttachments = rentalAttachments;
	}

	public Byte getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(Byte attachmentType) {
		this.attachmentType = attachmentType;
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

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
 
 
 
 
}
