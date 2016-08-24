package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>categoryId: 子类型ID</li>
 * <li>address: 服务地点</li>
 * <li>content: 内容</li>
 * <li>attachments: 图片路径列表</li>
 * </ul>
 */
public class CreateTaskCommand {
	private String ownerType;
    private Long ownerId;
	private Long categoryId;
	private String address;
	private String content;
	private String nickName;
	private String mobile;
	
	@ItemType(AttachmentDescriptor.class)
	private List<AttachmentDescriptor> attachments;
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
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<AttachmentDescriptor> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<AttachmentDescriptor> attachments) {
		this.attachments = attachments;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
