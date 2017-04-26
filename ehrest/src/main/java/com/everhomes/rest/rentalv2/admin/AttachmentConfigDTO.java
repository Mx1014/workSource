package com.everhomes.rest.rentalv2.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * 预约需要提交的信息
 * <li>id: id</li>
 * <li>attachmentType: 类型，参考
 * {@link com.everhomes.rest.rentalv2.admin.AttachmentType}</li>
 * <li>mustOptions: 是否必选，1是0否</li>
 * <li>goodItems: 物资列表  attachmentType: GOOD_ITEM(4)时使用  {@link com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO}</li>
 * <li>recommendUsers: 推荐员列表  attachmentType: RECOMMEND_USER(5)时使用 {@link com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO}</li>
 * <li>content: 内容</li>
 * <li>userName: 推荐员姓名</li>
 * <li>mobile: 推荐员手机号</li>
 * <li>iconUri: 推荐员头像uri</li>
 * <li>iconUrl: 推荐员头像url</li>
 * </ul>
 */
public class AttachmentConfigDTO {

	private Long id;
	private Byte attachmentType;
	private Byte mustOptions;

	private String content;
	@ItemType(AttachmentConfigDTO.class)
	private List<AttachmentConfigDTO> goodItems;
	@ItemType(AttachmentConfigDTO.class)
	private List<AttachmentConfigDTO> recommendUsers;

	private String userName;
	private String mobile;
	private String iconUri;
	private String iconUrl;


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public List<AttachmentConfigDTO> getGoodItems() {
		return goodItems;
	}

	public void setGoodItems(List<AttachmentConfigDTO> goodItems) {
		this.goodItems = goodItems;
	}

	public List<AttachmentConfigDTO> getRecommendUsers() {
		return recommendUsers;
	}

	public void setRecommendUsers(List<AttachmentConfigDTO> recommendUsers) {
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

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
