// @formatter:off
package com.everhomes.rest.community.admin;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>namespaceName：命名空间名称</li>
 * <li>communityType：小区类型，参考 {@link com.everhomes.rest.namespace.NamespaceCommunityType}</li>
 * <li>androidRealm: androidRealm</li>
 * <li>iosRealm: iosRealm</li>
 * <li>versionDescription: 描述</li>
 * <li>versionRange: 版本上下界，比如[1.0.0,2.0.0)</li>
 * <li>targetVersion: 目标版本</li>
 * <li>forceUpgrade: 是否强制升级</li>
 * <li>smsTemplates: 短信模板列表</li>
 * <li>appAgreementsUrl: app.agreements.url</li>
 * <li>homeUrl: home.url</li>
 * <li>postTypes: 帖子类型列表</li>
 * </ul>
 */
public class CommunityImportBaseConfigCommand {
	private String namespaceName;
	private String communityType;
	private String androidRealm;
	private String iosRealm;
	private String versionDescription;
	private String versionRange;
	private String targetVersion;
	private Byte forceUpgrade;
	@ItemType(SmsTemplate.class)
	private List<SmsTemplate> smsTemplates;
	private String appAgreementsUrl;
	private String homeUrl;
	@ItemType(String.class)
	private List<String> postTypes;

	public String getNamespaceName() {
		return namespaceName;
	}

	public void setNamespaceName(String namespaceName) {
		this.namespaceName = namespaceName;
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

	public String getVersionDescription() {
		return versionDescription;
	}

	public void setVersionDescription(String versionDescription) {
		this.versionDescription = versionDescription;
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

	public List<SmsTemplate> getSmsTemplates() {
		return smsTemplates;
	}

	public void setSmsTemplates(List<SmsTemplate> smsTemplates) {
		this.smsTemplates = smsTemplates;
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

	public List<String> getPostTypes() {
		return postTypes;
	}

	public void setPostTypes(List<String> postTypes) {
		this.postTypes = postTypes;
	}

}
