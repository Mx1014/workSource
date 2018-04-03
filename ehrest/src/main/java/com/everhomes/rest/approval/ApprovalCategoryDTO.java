// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>id: id</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.approval.ApprovalOwnerType}</li>
 * <li>ownerId: 所属者ID</li>
 * <li>approvalType: 审批类型，参考{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>categoryName: 类别名称</li>
 * </ul>
 */
public class ApprovalCategoryDTO {
	private Long id;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Byte approvalType;
	private String categoryName;
	
	public ApprovalCategoryDTO(){}
	
	public ApprovalCategoryDTO(Long id , Byte approvalType,String categoryName){
		this.id = id;
		this.approvalType = approvalType;
		this.categoryName = categoryName;
	}
	public Byte getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(Byte approvalType) {
		this.approvalType = approvalType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
