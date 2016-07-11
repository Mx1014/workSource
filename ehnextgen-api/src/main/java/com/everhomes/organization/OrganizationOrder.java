package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationOrders;
import com.everhomes.util.StringHelper;

public class OrganizationOrder extends EhOrganizationOrders{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrganizationOrder(){}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
}
