package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>serviceCategoryId: 服务类型ID</li>
 * <li>childCategoryId: 子类型ID</li>
 * <li>address: 服务地点</li>
 * <li>content: 内容</li>
 * <li>attachments: 图片列表</li>
 * </ul>
 */
public class CreateNewTaskCommand {
	private String ownerType;
    private Long ownerId;
	private Long serviceCategoryId;
	private Long childCategoryId;
	private String address;
	private String content;
	private List<String> attachments;
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
	public Long getServiceCategoryId() {
		return serviceCategoryId;
	}
	public void setServiceCategoryId(Long serviceCategoryId) {
		this.serviceCategoryId = serviceCategoryId;
	}
	
	public Long getChildCategoryId() {
		return childCategoryId;
	}
	public void setChildCategoryId(Long childCategoryId) {
		this.childCategoryId = childCategoryId;
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
	public List<String> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<String> attachments) {
		this.attachments = attachments;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
