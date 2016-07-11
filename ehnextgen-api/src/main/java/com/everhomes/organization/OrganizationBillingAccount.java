package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationBillingAccounts;
import com.everhomes.util.StringHelper;

public class OrganizationBillingAccount extends EhOrganizationBillingAccounts{

	private static final long serialVersionUID = -5798375159547655367L;
	
	public OrganizationBillingAccount(){}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	

}
