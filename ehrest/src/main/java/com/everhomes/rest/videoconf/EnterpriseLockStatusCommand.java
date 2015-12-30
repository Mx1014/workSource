package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>enterpriseId: 企业id</li>
 *  <li>lockStatus: 锁定状态 1: 解锁    2: 锁定</li>
 * </ul>
 *
 */
public class EnterpriseLockStatusCommand {
	
	private Long enterpriseId;
	
	private Byte lockStatus;

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
	public Byte getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Byte lockStatus) {
		this.lockStatus = lockStatus;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
