package com.everhomes.acl;

import com.everhomes.server.schema.tables.pojos.EhWebMenuPrivileges;
import com.everhomes.util.StringHelper;

public class WebMenuPrivilege extends EhWebMenuPrivileges {
	
	private static final long serialVersionUID = -1852518988310908484L;

	public WebMenuPrivilege() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
