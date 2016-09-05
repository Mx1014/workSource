// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 参数：
 * <li>sceneToken: 场景</li>
 * <li>approvalType: 审批类型，参考{@link com.everhomes.rest.approval.ApprovalType}</li>
 * <li>categoryId: 具体类型</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListApprovalRequestBySceneCommand {
	private String sceneToken;
	private Byte approvalType;
	private Long categoryId;
	private Long pageAnchor;
	private Integer pageSize;

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

	public String getSceneToken() {
		return sceneToken;
	}

	public void setSceneToken(String sceneToken) {
		this.sceneToken = sceneToken;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
