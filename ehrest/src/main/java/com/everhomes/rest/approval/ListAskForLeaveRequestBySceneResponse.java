// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 返回值：
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>askForLeaveRequestList: 请假列表，参考{@link com.everhomes.rest.approval.AskForLeaveRequestDTO}</li>
 * </ul>
 */
public class ListAskForLeaveRequestBySceneResponse {
	private Long nextPageAnchor;
	private List<AskForLeaveRequestDTO> askForLeaveRequestList;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AskForLeaveRequestDTO> getAskForLeaveRequestList() {
		return askForLeaveRequestList;
	}

	public void setAskForLeaveRequestList(List<AskForLeaveRequestDTO> askForLeaveRequestList) {
		this.askForLeaveRequestList = askForLeaveRequestList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
