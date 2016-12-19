package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhServiceModulePrivileges;
import com.everhomes.util.StringHelper;

public class ServiceModulePrivilege extends EhServiceModulePrivileges {

	private static final long serialVersionUID = -1852518988310908484L;

	public ServiceModulePrivilege() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
