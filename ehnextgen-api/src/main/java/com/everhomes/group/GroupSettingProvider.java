// @formatter:off
package com.everhomes.group;

import java.util.List;

public interface GroupSettingProvider {

	void createGroupSetting(GroupSetting groupSetting);

	void updateGroupSetting(GroupSetting groupSetting);

	GroupSetting findGroupSettingById(Long id);

	List<GroupSetting> listGroupSetting();

	GroupSetting findGroupSettingByNamespaceId(Integer namespaceId);

}