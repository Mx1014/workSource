// @formatter: off
package com.everhomes.rest.version;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数
 * <li>realm: realm</li>
 * <li>description: 描述</li>
 * <li>namespaceId: 域空间id</li>
 * <li>versionRange: 版本上下界，比如[1.0.0,2.0.0)</li>
 * <li>targetVersion: 目标版本</li>
 * <li>forceUpgrade: 是否强制升级</li>
 * </ul>
 */
public class CreateVersionRealmCommand {
	private String realm;
	private String description;
	private Integer namespaceId;
	private String versionRange;
	private String targetVersion;
	private Byte forceUpgrade;

	public String getVersionRange() {
		return versionRange;
	}

	public void setVersionRange(String versionRange) {
		this.versionRange = versionRange;
	}

	public String getTargetVersion() {
		return targetVersion;
	}

	public void setTargetVersion(String targetVersion) {
		this.targetVersion = targetVersion;
	}

	public Byte getForceUpgrade() {
		return forceUpgrade;
	}

	public void setForceUpgrade(Byte forceUpgrade) {
		this.forceUpgrade = forceUpgrade;
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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
