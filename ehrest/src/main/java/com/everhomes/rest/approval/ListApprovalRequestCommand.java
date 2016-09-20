// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数：
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.approval.ApprovalOwnerType}</li>
 * <li>ownerId: 所属者ID</li>
 * <li>approvalType: 审批类型，参考{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>categoryId: 审批类别id</li>
 * <li>fromDate: 搜索开始日期</li>
 * <li>endDate: 搜索结束日期</li>
 * <li>nickName: 姓名</li>
 * <li>queryType: 查询类型，1待审批，2已审批</li>
 * <li>pageSize: 每页大小</li>
 * <li>pageAnchor: 锚点</li>
 * </ul>
 */
public class ListApprovalRequestCommand {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Byte approvalType;
	private Long categoryId;
	private Long fromDate;
	private Long endDate;
	private String nickName;
	private Byte queryType;
	private Integer pageSize;
	private Long pageAnchor;

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

	public Byte getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(Byte approvalType) {
		this.approvalType = approvalType;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getFromDate() {
		return fromDate;
	}

	public void setFromDate(Long fromDate) {
		this.fromDate = fromDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Byte getQueryType() {
		return queryType;
	}

	public void setQueryType(Byte queryType) {
		this.queryType = queryType;
	}

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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
