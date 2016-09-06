package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>approvalUserList: 人员列表，参考{@link com.everhomes.rest.approval.ApprovalUserDTO}</li>
 * </ul>
 */
public class ListApprovalUserResponse {
	private Long nextPageAnchor;
	@ItemType(ApprovalUserDTO.class)
	private List<ApprovalUserDTO> approvalUserList;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ApprovalUserDTO> getApprovalUserList() {
		return approvalUserList;
	}

	public void setApprovalUserList(List<ApprovalUserDTO> approvalUserList) {
		this.approvalUserList = approvalUserList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
