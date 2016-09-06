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
 * <li>absenceRequestList: 请假申请列表，参考{@link com.everhomes.rest.approval.AbsenceRequestDTO}</li>
 * </ul>
 */
public class ListAbsenceRequestResponse {
	private Long nextPageAnchor;
	@ItemType(AbsenceRequestDTO.class)
	private List<AbsenceRequestDTO> absenceRequestList;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<AbsenceRequestDTO> getAbsenceRequestList() {
		return absenceRequestList;
	}

	public void setAbsenceRequestList(List<AbsenceRequestDTO> absenceRequestList) {
		this.absenceRequestList = absenceRequestList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
