package com.everhomes.rest.rentalv2;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.PunchExceptionRequestDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>rentalSiteId：场所id</li>   
 * <li>rentalBillId：订单id</li>
 * <li>rentalItems：List<SiteItemDTO> 商品列表</li>
 * <li>rentalAttachments：List<AttachmentDTO> 附件列表</li>
 * <li>recommendUsers：List<Long> 推荐员列表id</li>
 * <li>goodItems：List<Long> 物资列表id</li>
 * </ul>
 */
public class AddRentalBillItemCommand {
//	@NotNull
//	private Long rentalSiteId;   
	@NotNull
	private Long rentalBillId; 
	@ItemType(SiteItemDTO.class)
	private List<SiteItemDTO> rentalItems;
	@ItemType(AttachmentDTO.class)
	private List<AttachmentDTO> rentalAttachments;

	@ItemType(Long.class)
	private List<Long> recommendUsers;
	@ItemType(Long.class)
	private List<Long> goodItems;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

//	public Long getRentalSiteId() {
//		return rentalSiteId;
//	}
//
//	public void setRentalSiteId(Long rentalSiteId) {
//		this.rentalSiteId = rentalSiteId;
//	}

	  
	 
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
 

	public List<AttachmentDTO> getRentalAttachments() {
		return rentalAttachments;
	}

	public void setRentalAttachments(List<AttachmentDTO> rentalAttachments) {
		this.rentalAttachments = rentalAttachments;
	}


	public List<Long> getRecommendUsers() {
		return recommendUsers;
	}

	public void setRecommendUsers(List<Long> recommendUsers) {
		this.recommendUsers = recommendUsers;
	}

	public List<Long> getGoodItems() {
		return goodItems;
	}

	public void setGoodItems(List<Long> goodItems) {
		this.goodItems = goodItems;
	}
}
