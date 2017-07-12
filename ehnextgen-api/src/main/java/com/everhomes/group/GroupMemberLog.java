// @formatter:off
package com.everhomes.group;

import com.everhomes.server.schema.tables.pojos.EhGroupMemberLogs;
import com.everhomes.util.StringHelper;

public class GroupMemberLog extends EhGroupMemberLogs {
	
	private static final long serialVersionUID = -2318994059276241958L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}