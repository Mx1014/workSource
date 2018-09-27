package com.everhomes.personal_center;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface PersonalCenterSettingProvider {

	Long createPersonalCenterSetting(PersonalCenterSetting obj);

	void updatePersonalCenterSetting(PersonalCenterSetting obj);

	void deletePersonalCenterSetting(PersonalCenterSetting obj);

	PersonalCenterSetting getPersonalCenterSettingById(Long id);

	List<PersonalCenterSetting> queryPersonalCenterSettings(
			ListingLocator locator, int count,
			ListingQueryBuilderCallback queryBuilderCallback);
	List<PersonalCenterSetting> queryActivePersonalCenterSettings(Integer namespaceId);
	List<PersonalCenterSetting> queryDefaultPersonalCenterSettings();
	List<PersonalCenterSetting> queryPersonalCenterSettingsByNamespaceIdAndVersion(Integer namespaceId, Integer version);

	int countPersonalCenterSettingVersion(Integer namespaceId, Timestamp dayStartTime, Timestamp dayEndTime);

	List<Integer> getVersionList(Integer namespaceId);
}
