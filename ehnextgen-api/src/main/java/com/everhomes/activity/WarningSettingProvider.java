// @formatter:off
package com.everhomes.activity;

import java.util.List;

public interface WarningSettingProvider {

	void createWarningSetting(WarningSetting warningSetting);

	void updateWarningSetting(WarningSetting warningSetting);

	WarningSetting findWarningSettingById(Long id);

	List<WarningSetting> listWarningSetting();

	WarningSetting findWarningSettingByNamespaceAndType(Integer namespaceId, Long categoryId, String type);

}