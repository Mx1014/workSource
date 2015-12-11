package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>namespaceId: 命名空间 </li>
 *  <li>enterpriseId: 企业id</li>
 *  <li>lockStatus: 锁定状态 0: 解锁    1: 锁定</li>
 * </ul>
 *
 */
public class EnterpriseLockStatusCommand {
	
	private Integer namespaceId;
	
	private Long enterpriseId;
	
	private Byte lockStatus;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

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
