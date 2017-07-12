// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 所属者，参考{@link com.everhomes.rest.group.GroupCategoryOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>categoryName: 分类名称</li>
 * </ul>
 */
public class CreateGroupCategoryCommand {

	private Integer namespaceId;

	private String ownerType;

	private Long ownerId;

	private String categoryName;

	public CreateGroupCategoryCommand() {

	}

	public CreateGroupCategoryCommand(Integer namespaceId, String ownerType, Long ownerId, String categoryName) {
		super();
		this.namespaceId = namespaceId;
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.categoryName = categoryName;
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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
