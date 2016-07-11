package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationOwners;
import com.everhomes.util.StringHelper;

public class OrganizationOwners extends EhOrganizationOwners{

	private static final long serialVersionUID = -7443424881891300179L;
	
	public OrganizationOwners(){
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
}
