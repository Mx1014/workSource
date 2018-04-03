package com.everhomes.module;

import com.everhomes.server.schema.tables.pojos.EhServiceModuleAssignmentRelations;
import com.everhomes.util.StringHelper;

public class ServiceModuleAssignmentRelation extends EhServiceModuleAssignmentRelations {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7504193619444671830L;

	public ServiceModuleAssignmentRelation() {

	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
