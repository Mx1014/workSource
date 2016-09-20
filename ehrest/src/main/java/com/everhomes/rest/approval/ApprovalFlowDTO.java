// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.approval.ApprovalOwnerType}</li>
 * <li>ownerId: 所属类型id</li>
 * <li>name: 审批流程名称</li>
 * <li>levelList: 审批级别列表，参考{@link com.everhomes.rest.approval.ApprovalFlowLevelDTO}</li>
 * </ul>
 */
public class ApprovalFlowDTO {
	private Long id;
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private String name;
	@ItemType(ApprovalFlowLevelDTO.class)
	private List<ApprovalFlowLevelDTO> levelList;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ApprovalFlowLevelDTO> getLevelList() {
		return levelList;
	}

	public void setLevelList(List<ApprovalFlowLevelDTO> levelList) {
		this.levelList = levelList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
