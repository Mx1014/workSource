// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>forgetToPunchList: 忘打卡申列表，参考{@link com.everhomes.rest.approval.ForgetToPunchDTO}</li>
 * </ul>
 */
public class ListForgetToPunchResponse {
	private Long nextPageAnchor;
	@ItemType(ForgetToPunchDTO.class)
	private List<ForgetToPunchDTO> forgetToPunchList;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ForgetToPunchDTO> getForgetToPunchList() {
		return forgetToPunchList;
	}

	public void setForgetToPunchList(List<ForgetToPunchDTO> forgetToPunchList) {
		this.forgetToPunchList = forgetToPunchList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
