package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 主键id</li>
 * <li>enterpriseId: 企业id</li>
 * <li>accountId: 账号id</li>
 * <li>userId: 用户id</li>
 * <li>contactId: 用户在企业通讯录中的id</li>
 * </ul>
 */
public class AssignVideoConfAccountCommand {
	
	private Long id;
	
	private Long enterpriseId;
	
	private Long accountId;
	
	private Long userId;
	
	private Long contactId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
