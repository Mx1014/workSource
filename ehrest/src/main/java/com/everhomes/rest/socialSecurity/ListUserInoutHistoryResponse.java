// @formatter:off
package com.everhomes.rest.socialSecurity;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>userInoutHistory: 增减员记录</li>
 * </ul>
 */
public class ListUserInoutHistoryResponse {

	@ItemType(UserInoutHistoryDTO.class)
	private List<UserInoutHistoryDTO> userInoutHistory;

	public ListUserInoutHistoryResponse() {

	}

	public ListUserInoutHistoryResponse(List<UserInoutHistoryDTO> userInoutHistory) {
		super();
		this.userInoutHistory = userInoutHistory;
	}

	public List<UserInoutHistoryDTO> getUserInoutHistory() {
		return userInoutHistory;
	}

	public void setUserInoutHistory(List<UserInoutHistoryDTO> userInoutHistory) {
		this.userInoutHistory = userInoutHistory;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
