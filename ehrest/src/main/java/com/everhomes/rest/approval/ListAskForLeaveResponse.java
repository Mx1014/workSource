// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 返回值：
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>askForLeaveList: 请假申请列表，参考{@link com.everhomes.rest.approval.AskForLeaveDTO}</li>
 * </ul>
 */
public class ListAskForLeaveResponse {
	private Long nextPageAnchor;
	@ItemType(AskForLeaveDTO.class)
	private List<AskForLeaveDTO> askForLeaveList;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AskForLeaveDTO> getAskForLeaveList() {
		return askForLeaveList;
	}

	public void setAskForLeaveList(List<AskForLeaveDTO> askForLeaveList) {
		this.askForLeaveList = askForLeaveList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
