package com.everhomes.rest.rentalv2;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
*<li>id： id</li>
 * <li>billId：订单id</li>
 * <li>attachmentType: 类型，参考{@link com.everhomes.rest.rentalv2.admin.AttachmentType}</li>
 * <li>content：内容</li> 
 * <li>resourceUrl：类型为附件时这里有url</li> 
 * <li>resourceName：类型为附件时这里有附件名称</li> 
 * <li>resourceSize：类型为附件时这里有附件大小</li> 
 * <li>goodItems: 物资列表  attachmentType: GOOD_ITEM(4)时使用  {@link com.everhomes.rest.rentalv2.RentalGoodItem}</li>
 * <li>recommendUsers: 推荐员列表  attachmentType: RECOMMEND_USER(5)时使用 {@link com.everhomes.rest.rentalv2.RentalRecommendUser}</li>
 * </ul>
 */
public class BillAttachmentDTO {
	private Long id;
	private Long billId;
	private Byte attachmentType ;
	private String content;
	private String resourceUrl;
	private String resourceName;
	private Integer resourceSize;

	@ItemType(RentalGoodItem.class)
	private List<RentalGoodItem> goodItems;
	@ItemType(RentalRecommendUser.class)
	private List<RentalRecommendUser> recommendUsers;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<RentalGoodItem> getGoodItems() {
		return goodItems;
	}

	public void setGoodItems(List<RentalGoodItem> goodItems) {
		this.goodItems = goodItems;
	}

	public List<RentalRecommendUser> getRecommendUsers() {
		return recommendUsers;
	}

	public void setRecommendUsers(List<RentalRecommendUser> recommendUsers) {
		this.recommendUsers = recommendUsers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(Byte attachmentType) {
		this.attachmentType = attachmentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Integer getResourceSize() {
		return resourceSize;
	}

	public void setResourceSize(Integer resourceSize) {
		this.resourceSize = resourceSize;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

}
