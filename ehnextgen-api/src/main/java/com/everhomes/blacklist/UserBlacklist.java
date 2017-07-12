package com.everhomes.blacklist;

import com.everhomes.server.schema.tables.pojos.EhUserBlacklists;
import com.everhomes.server.schema.tables.pojos.EhWebMenus;
import com.everhomes.util.StringHelper;

public class UserBlacklist extends EhUserBlacklists {

	private static final long serialVersionUID = -1852518988310908484L;

	public UserBlacklist() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
