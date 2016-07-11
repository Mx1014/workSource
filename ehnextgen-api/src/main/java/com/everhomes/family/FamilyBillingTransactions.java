package com.everhomes.family;

import com.everhomes.server.schema.tables.pojos.EhFamilyBillingTransactions;
import com.everhomes.util.StringHelper;

public class FamilyBillingTransactions extends EhFamilyBillingTransactions{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FamilyBillingTransactions(){}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	};
	
	


	
	
	

}
