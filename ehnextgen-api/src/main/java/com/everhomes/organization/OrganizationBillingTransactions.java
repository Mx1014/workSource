package com.everhomes.organization;

import com.everhomes.server.schema.tables.pojos.EhOrganizationBillingTransactions;
import com.everhomes.util.StringHelper;

public class OrganizationBillingTransactions extends EhOrganizationBillingTransactions{

	private static final long serialVersionUID = -6781540542343535059L;
	
	public OrganizationBillingTransactions(){
		
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
