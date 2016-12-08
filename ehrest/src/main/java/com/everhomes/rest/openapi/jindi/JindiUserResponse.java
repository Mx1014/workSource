// @formatter:off
package com.everhomes.rest.openapi.jindi;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>userList: 用户列表</li>
 * <li>nextPageAnchor: 下页锚点</li>
 * </ul>
 */
public class JindiUserResponse {
	@ItemType(JindiUserDTO.class)
	private List<JindiUserDTO> userList;
	private Long nextPageAnchor;
	private Byte hasMore;

	public Byte getHasMore() {
		return hasMore;
	}

	public void setHasMore(Byte hasMore) {
		this.hasMore = hasMore;
	}

	public List<JindiUserDTO> getUserList() {
		return userList;
	}

	public void setUserList(List<JindiUserDTO> userList) {
		this.userList = userList;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
