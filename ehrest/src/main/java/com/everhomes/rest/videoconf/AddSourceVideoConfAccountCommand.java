package com.everhomes.rest.videoconf;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>sourceAccount: 源账号</li>
 *  <li>password: 源账号密码</li>
 *  <li>accountCategory: 会议类型对应在ConfAccountCategories中的id</li>
 *  <li>validDate: 有效期</li>
 * </ul>
 *
 */
public class AddSourceVideoConfAccountCommand {
	
	private String sourceAccount;
	
	private String password;
	
	private Long accountCategory;
	
	private Long validDate;

	public String getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getAccountCategory() {
		return accountCategory;
	}

	public void setAccountCategory(Long accountCategory) {
		this.accountCategory = accountCategory;
	}

	public Long getValidDate() {
		return validDate;
	}

	public void setValidDate(Long validDate) {
		this.validDate = validDate;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
