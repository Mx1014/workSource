// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>approvalStatus: 审核状态，参考{@link com.everhomes.rest.group.ApprovalStatus}</li>
 * <li>privateFlag: 公有私有圈标记，参考{@link com.everhomes.rest.organization.PrivateFlag}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListGroupsByApprovalStatusCommand {

	private Byte approvalStatus;

	private Byte privateFlag;

	private Long pageAnchor;

	private Integer pageSize;

	public ListGroupsByApprovalStatusCommand() {

	}

	public ListGroupsByApprovalStatusCommand(Byte approvalStatus, Byte privateFlag, Long pageAnchor, Integer pageSize) {
		super();
		this.approvalStatus = approvalStatus;
		this.privateFlag = privateFlag;
		this.pageAnchor = pageAnchor;
		this.pageSize = pageSize;
	}

	public Byte getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Byte approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Byte getPrivateFlag() {
		return privateFlag;
	}

	public void setPrivateFlag(Byte privateFlag) {
		this.privateFlag = privateFlag;
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
