package com.everhomes.rest.videoconf;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>confAccounts: 会议账号总数</li>
 *  <li>validConfAccount: 会议有效账号总数</li>
 *  <li>newConfAccount: 新增会议账号数</li>
 *  <li>confType: 账号会议类型</li>
 * </ul>
 *
 */
public class VideoConfAccountStatisticsDTO {
	
	private Long confAccounts;
	
	private Long validConfAccount;
	
	private Long newConfAccount;
	
	private String confType;

	public Long getConfAccounts() {
		return confAccounts;
	}

	public void setConfAccounts(Long confAccounts) {
		this.confAccounts = confAccounts;
	}

	public Long getValidConfAccount() {
		return validConfAccount;
	}

	public void setValidConfAccount(Long validConfAccount) {
		this.validConfAccount = validConfAccount;
	}

	public Long getNewConfAccount() {
		return newConfAccount;
	}

	public void setNewConfAccount(Long newConfAccount) {
		this.newConfAccount = newConfAccount;
	}

	public String getConfType() {
		return confType;
	}

	public void setConfType(String confType) {
		this.confType = confType;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
