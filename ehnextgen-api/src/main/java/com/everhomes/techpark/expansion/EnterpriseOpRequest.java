package com.everhomes.techpark.expansion;

import com.everhomes.server.schema.tables.pojos.EhEnterpriseOpRequests;

public class EnterpriseOpRequest extends EhEnterpriseOpRequests{

	private Long LeaseIssuerId;

	public Long getLeaseIssuerId() {
		return LeaseIssuerId;
	}

	public void setLeaseIssuerId(Long leaseIssuerId) {
		LeaseIssuerId = leaseIssuerId;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7588874833773327925L;

}
