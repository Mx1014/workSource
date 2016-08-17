package com.everhomes.rest.namespace;

/**
 * <ul>
 * <li>name：命名空间名称</li>
 * <li>communityType：小区类型，参考
 * {@link com.everhomes.rest.namespace.NamespaceCommunityType}</li>
 * <li>realm: realm</li>
 * <li>description: 描述</li>
 * <li>namespaceId: 域空间id</li>
 * <li>versionRange: 版本上下界，比如[1.0.0,2.0.0)</li>
 * <li>targetVersion: 目标版本</li>
 * <li>forceUpgrade: 是否强制升级</li>
 * <li>appAgreementsUrl: app.agreements.url</li>
 * <li>homeUrl: home.url</li>
 * </ul>
 */
public class ConfigNamespaceCommand {
	private String name;
	private String communityType;
	private String androidRealm;
	private String iosRealm;
	private String description;
	private String versionRange;
	private String targetVersion;
	private Byte forceUpgrade;
	private String appAgreementsUrl;
	private String homeUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommunityType() {
		return communityType;
	}

	public void setCommunityType(String communityType) {
		this.communityType = communityType;
	}

	public String getAndroidRealm() {
		return androidRealm;
	}

	public void setAndroidRealm(String androidRealm) {
		this.androidRealm = androidRealm;
	}

	public String getIosRealm() {
		return iosRealm;
	}

	public void setIosRealm(String iosRealm) {
		this.iosRealm = iosRealm;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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

	public String getAppAgreementsUrl() {
		return appAgreementsUrl;
	}

	public void setAppAgreementsUrl(String appAgreementsUrl) {
		this.appAgreementsUrl = appAgreementsUrl;
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}

}
