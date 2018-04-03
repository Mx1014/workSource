// @formatter:off
package com.everhomes.activity;

public interface RosterOrderSettingProvider {

	void createRosterOrderSetting(RosterOrderSetting rosterOrderSetting);

	void updateRosterOrderSetting(RosterOrderSetting rosterOrderSetting);

	RosterOrderSetting findRosterOrderSettingById(Long id);

	//List<RosterOrderSetting> listRosterOrderSetting();

	RosterOrderSetting findRosterOrderSettingByNamespace(Integer namespaceId, Long categoryId);

}