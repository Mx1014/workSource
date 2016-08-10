package com.everhomes.statistics.transaction;

import com.everhomes.server.schema.tables.pojos.EhStatServiceSettlementResults;

public class StatServiceSettlementResult extends EhStatServiceSettlementResults {
	
	private String resourceName;
	
	

	public String getResourceName() {
		return resourceName;
	}



	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = -2021154083746586959L;

}
