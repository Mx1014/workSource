// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 参数：
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.approval.ApprovalOwnerType}</li>
 * <li>ownerId: 所属者ID</li>
 * <li>requestIdList: 申请ID列表</li>
 * <li>reason: 驳回理由</li>
 * </ul>
 */
public class RejectRequestCommand {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	@ItemType(Long.class)
	private List<Long> requestIdList;
	private String reason;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public List<Long> getRequestIdList() {
		return requestIdList;
	}

	public void setRequestIdList(List<Long> requestIdList) {
		this.requestIdList = requestIdList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
