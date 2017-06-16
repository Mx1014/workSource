package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.RentalGoodItem;
import com.everhomes.rest.rentalv2.RentalRecommendUser;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * 预约需要提交的信息
 * <li>id: id</li>
 * <li>attachmentType: 类型，参考 {@link com.everhomes.rest.rentalv2.admin.AttachmentType}</li>
 * <li>mustOptions: 是否必选，1是0否</li>
 * <li>goodItems: 物资列表  attachmentType: GOOD_ITEM(4)时使用  {@link com.everhomes.rest.rentalv2.RentalGoodItem}</li>
 * <li>recommendUsers: 推荐员列表  attachmentType: RECOMMEND_USER(5)时使用 {@link com.everhomes.rest.rentalv2.RentalRecommendUser}</li>
 * <li>content: 内容</li>
 * </ul>
 */
public class AttachmentConfigDTO {

	private Long id;
	private Byte attachmentType;
	private Byte mustOptions;

	private String content;
	@ItemType(RentalGoodItem.class)
	private List<RentalGoodItem> goodItems;
	@ItemType(RentalRecommendUser.class)
	private List<RentalRecommendUser> recommendUsers;

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

	public Byte getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(Byte attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Byte getMustOptions() {
		return mustOptions;
	}

	public void setMustOptions(Byte mustOptions) {
		this.mustOptions = mustOptions;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
