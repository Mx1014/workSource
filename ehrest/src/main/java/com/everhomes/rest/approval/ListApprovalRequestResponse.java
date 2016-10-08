// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>listJson: 申请列表，请假申请参考{@link com.everhomes.rest.approval.AbsenceRequestDTO}，异常申请参考{@link com.everhomes.rest.approval.ExceptionRequestDTO}</li>
 * </ul>
 */
public class ListApprovalRequestResponse {
	private Long nextPageAnchor;
	private String listJson;

	public ListApprovalRequestResponse() {
		super();
	}

	public ListApprovalRequestResponse(Long nextPageAnchor, String listJson) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.listJson = listJson;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public String getListJson() {
		return listJson;
	}

	public void setListJson(String listJson) {
		this.listJson = listJson;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
