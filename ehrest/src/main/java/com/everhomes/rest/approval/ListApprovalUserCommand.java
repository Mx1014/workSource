// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 参数：
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.approval.ApprovalOwnerType}</li>
 * <li>ownerId: 所属者ID</li>
 * <li>flowId: 审批流程ID</li>
 * <li>level: 级别</li>
 * <li>departmentId: 部门ID</li>
 * <li>keyword: 姓名关键字</li>
 * <li>pageSize: 每页大小</li>
 * <li>pageAnchor: 锚点</li>
 * </ul>
 */
public class ListApprovalUserCommand {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long flowId;
	private Byte level;
	private Long departmentId;
	private String keyword;
	private Integer pageSize;
	private Long pageAnchor;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public Byte getLevel() {
		return level;
	}

	public void setLevel(Byte level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
