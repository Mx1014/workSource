package com.everhomes.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>accountId: 账号id</li>
 *  <li>validDate: 有效期</li>
 *  <li>accountCategoryId: 会议账号销售规则id</li>
 * </ul>
 *
 */
public class UpdateVideoConfAccountCommand {
	
	private Long accountId;
	
	private Long validDate;
	
	private Long accountCategoryId;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getValidDate() {
		return validDate;
	}

	public void setValidDate(Long validDate) {
		this.validDate = validDate;
	}
	
	public Long getAccountCategoryId() {
		return accountCategoryId;
	}

	public void setAccountCategoryId(Long accountCategoryId) {
		this.accountCategoryId = accountCategoryId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
