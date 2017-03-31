package com.everhomes.techpark.expansion;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequests;

public class EnterpriseOpRequest extends EhEnterpriseOpRequests{
	
	private String sourceName;
	
	private Long LeaseIssuerId;

	public Long getLeaseIssuerId() {
		return LeaseIssuerId;
	}

	public void setLeaseIssuerId(Long leaseIssuerId) {
		LeaseIssuerId = leaseIssuerId;
	}

	public String getSourceName() {
		return sourceName;
	}



	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = -7588874833773327925L;

	
}
