// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 所属者，参考{@link com.everhomes.rest.group.GroupCategoryOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>categoryId: 分类id</li>
 * </ul>
 */
public class DeleteGroupCategoryCommand {

	private Integer namespaceId;

	private String ownerType;

	private Long ownerId;

	private Long categoryId;

	public DeleteGroupCategoryCommand() {

	}

	public DeleteGroupCategoryCommand(Integer namespaceId, String ownerType, Long ownerId, Long categoryId) {
		super();
		this.namespaceId = namespaceId;
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.categoryId = categoryId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
