package com.everhomes.user;

import com.everhomes.server.schema.tables.pojos.EhStatActiveUsers;
import com.everhomes.util.StringHelper;

public class StatActiveUser extends EhStatActiveUsers {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6497910330464633309L;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
