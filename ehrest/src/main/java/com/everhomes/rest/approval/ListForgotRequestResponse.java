// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>forgotRequestList: 忘打卡申列表，参考{@link com.everhomes.rest.approval.ForgotRequestDTO}</li>
 * </ul>
 */
public class ListForgotRequestResponse {
	private Long nextPageAnchor;
	@ItemType(ForgotRequestDTO.class)
	private List<ForgotRequestDTO> forgotRequestList;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ForgotRequestDTO> getForgotRequestList() {
		return forgotRequestList;
	}

	public void setForgotRequestList(List<ForgotRequestDTO> forgotRequestList) {
		this.forgotRequestList = forgotRequestList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
