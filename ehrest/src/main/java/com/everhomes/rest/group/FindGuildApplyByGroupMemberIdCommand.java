// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>groupMemberId: groupMemberId</li>
 * </ul>
 */
public class FindGuildApplyByGroupMemberIdCommand {

	Long groupMemberId;

	public Long getGroupMemberId() {
		return groupMemberId;
	}

	public void setGroupMemberId(Long groupMemberId) {
		this.groupMemberId = groupMemberId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
