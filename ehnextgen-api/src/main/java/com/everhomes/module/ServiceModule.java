package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhServiceModules;
import com.everhomes.util.StringHelper;

public class ServiceModule extends EhServiceModules {

	private static final long serialVersionUID = -1852518988310908484L;

	public ServiceModule() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
