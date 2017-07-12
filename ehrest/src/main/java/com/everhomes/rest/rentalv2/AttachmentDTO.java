package com.everhomes.rest.rentalv2;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * 预约需要提交的信息
 * <li>attachmentType: 类型，参考
 * {@link com.everhomes.rest.rentalv2.admin.AttachmentType}</li>
 * <li>content: 内容-文本</li>
 * <li>recommendUsers：List<Long> 推荐员列表id</li>
 * <li>goodItems：List<Long> 物资列表id</li>
 * </ul>
 */
public class AttachmentDTO {
	private Byte attachmentType;
	private String content;

	@ItemType(Long.class)
	private List<Long> recommendUsers;
	@ItemType(Long.class)
	private List<Long> goodItems;

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
