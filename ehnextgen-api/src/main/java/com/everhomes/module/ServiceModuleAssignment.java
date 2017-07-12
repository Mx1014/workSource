package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleAssignments;
import com.everhomes.util.StringHelper;

public class ServiceModuleAssignment extends EhServiceModuleAssignments {

	private static final long serialVersionUID = -1852518988310908484L;

	public ServiceModuleAssignment() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
