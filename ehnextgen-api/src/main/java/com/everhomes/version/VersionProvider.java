package com.everhomes.version;

import java.util.List;

import com.everhomes.rest.version.VersionInfoDTO;
import com.everhomes.util.Version;

public interface VersionProvider {
    void createVersionRealm(VersionRealm realm);
    void updateVersionRealm(VersionRealm realm);
    void deleteVersionRealm(VersionRealm realm);
    void deleteVersionRealmById(long id);
    VersionRealm findVersionRealmById(long id);
    VersionRealm findVersionRealmByName(String realmName);
    List<VersionRealm> listVersionRealm();
    
    void createVersionUpgradeRule(VersionUpgradeRule rule);
    void updateVersionUpgradeRule(VersionUpgradeRule rule);
    void deleteVersionUpgradeRule(VersionUpgradeRule rule);
    void deleteVersionUpgradeRuleById(long id);
    VersionUpgradeRule findVersionUpgradeRuleById(long id);
    VersionUpgradeRule matchVersionUpgradeRule(long realmId, Version version);
    List<VersionUpgradeRule> listVersionUpgradeRules(long realmId);
    
    void createVersionedContent(VersionedContent content);
    void updateVersionedContent(VersionedContent content);
    void deleteVersionedContent(VersionedContent content);
    void deleteVersionedContentById(long id);
    VersionedContent findVersionedContentById(long id);
    VersionedContent matchVersionedContent(long realmId, Version version);
    List<VersionedContent> listVersionedContent(long realmId);

    void createVersionUrl(VersionUrl versionUrl);
    void updateVersionUrl(VersionUrl versionUrl);
    void deleteVersionUrl(VersionUrl versionUrl);
    void deleteVersionUrlById(long id);
    VersionUrl findVersionUrlById(long id);
    VersionUrl findVersionUrlByVersion(String realmName, String targetVersion);
	List<VersionInfoDTO> listVersionInfo(Long pageAnchor, int pageSize);
	String findAppNameByRealm(Long realmId);
	String findIconUrlByRealm(Long realmId);
	String findDownloadUrlByRealm(Long id);
}
