package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 *  <li>userId：用户id</li>
 *  <li>enterpriseId: 企业id </li>
 * </ul>
 *
 */
public class VerifyVideoConfAccountCommand {
	
	private Long userId;

	private Long enterpriseId;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
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
