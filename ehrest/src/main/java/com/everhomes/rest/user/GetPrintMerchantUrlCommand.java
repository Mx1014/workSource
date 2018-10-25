package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;


/**

 *<ul>
 *<li>enterpriseId:公司ID</li>
 * </ul>
 */
public class GetPrintMerchantUrlCommand {
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
