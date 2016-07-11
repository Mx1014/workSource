package com.everhomes.family;

import com.everhomes.server.schema.tables.pojos.EhFamilyBillingAccounts;
import com.everhomes.util.StringHelper;

public class FamilyBillingAccount extends EhFamilyBillingAccounts{

	private static final long serialVersionUID = 2118268321537522740L;
	
	public FamilyBillingAccount(){}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	
	

}
