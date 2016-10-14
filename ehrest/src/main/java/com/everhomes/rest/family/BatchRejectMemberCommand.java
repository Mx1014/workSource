package com.everhomes.rest.family;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul> 
 * <li>members: 批量审批拒绝的members{@link com.everhomes.rest.family.RejectMemberCommand}</li>
 * </ul>
 */
public class BatchRejectMemberCommand {
	
	@ItemType(RejectMemberCommand.class)
	private List<RejectMemberCommand> members;

	public List<RejectMemberCommand> getMembers() {
		return members;
	}

	public void setMembers(List<RejectMemberCommand> members) {
		this.members = members;
	}
 
	
}
