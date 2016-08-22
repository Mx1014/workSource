// @formatter:off
package com.everhomes.rest.version;

/**
 * 
 * <ul>
 * <li>realmId: 版本id</li>
 * <li>realm: 版本</li>
 * <li>description: 版本名称</li>
 * </ul>
 */
public class VersionRealmDTO {
	private Long realmId;
	private String realm;
	private String description;

	public Long getRealmId() {
		return realmId;
	}

	public void setRealmId(Long realmId) {
		this.realmId = realmId;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
