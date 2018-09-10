package com.everhomes.rest.developer_account_info;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>bundleIds: 关联应用(必填)</li>
 * <li>teamId: 关联开发者帐号(必填)</li>
 * <li>authkeyId: authkey_id(必填)</li>
 * </ul>
 * @author huanglm 20180606
 *
 */
public class DeveloperAccountInfoCommand {

	/**
	 * 关联应用
	 */
	@NotNull
    private String bundleIds;
    /**
     * 关联开发者帐号
     */
	@NotNull
    private String teamId;
    /**
     * authkey_id
     */
	@NotNull
    private String authkeyId;
    
	public String getBundleIds() {
		return bundleIds;
	}
	public void setBundleIds(String bundleIds) {
		this.bundleIds = bundleIds;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getAuthkeyId() {
		return authkeyId;
	}
	public void setAuthkeyId(String authkeyId) {
		this.authkeyId = authkeyId;
	}
  
}

