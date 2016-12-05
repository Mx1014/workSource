// formatter:off
package com.everhomes.rest.version;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id: 记录id</li>
 * <li>realmId: 版本id</li>
 * <li>realm: 版本</li>
 * <li>description: 版本名称</li>
 * <li>minVersion: 版本下限</li>
 * <li>maxVersion: 版本上限</li>
 * <li>targetVersion: 目标版本</li>
 * <li>forceUpgrade: 是否强制升级，0不强制1强制</li>
 * <li>urlId: url的记录id</li>
 * <li>downloadUrl: 下载链接</li>
 * <li>appName: app名称</li>
 * <li>publishTime: 发布时间</li>
 * <li>upgradeDescription: 升级描述</li>
 * </ul>
 */
public class VersionInfoDTO {
	private Long id;
	private Long realmId;
	private String realm;
	private String description;
	private String minVersion;
	private String maxVersion;
	private String targetVersion;
	private Byte forceUpgrade;
	private Long urlId;
	private String appName;
	private Timestamp publishTime;
	private String downloadUrl;
	private String upgradeDescription;
	private String iconUrl;

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Timestamp getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	public Long getUrlId() {
		return urlId;
	}

	public void setUrlId(Long urlId) {
		this.urlId = urlId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getMinVersion() {
		return minVersion;
	}

	public void setMinVersion(String minVersion) {
		this.minVersion = minVersion;
	}

	public String getMaxVersion() {
		return maxVersion;
	}

	public void setMaxVersion(String maxVersion) {
		this.maxVersion = maxVersion;
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

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpgradeDescription() {
		return upgradeDescription;
	}

	public void setUpgradeDescription(String upgradeDescription) {
		this.upgradeDescription = upgradeDescription;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
