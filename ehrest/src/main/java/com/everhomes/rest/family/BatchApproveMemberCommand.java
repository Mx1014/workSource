// @formatter:off
package com.everhomes.rest.family;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>members: 每一个审批用户的信息{@link com.everhomes.rest.family.ApproveMemberCommand} </li> 
 * </ul>
 */
public class BatchApproveMemberCommand {
	 
    @ItemType(ApproveMemberCommand.class)
    private List<ApproveMemberCommand> members;
     
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<ApproveMemberCommand> getMembers() {
		return members;
	}

	public void setMembers(List<ApproveMemberCommand> members) {
		this.members = members;
	}
	 
}
