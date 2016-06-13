package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * 
 * enterpriseId: 企业id
 *
 */
public class VerifyPurchaseAuthorityCommand {

	private Long enterpriseId;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
