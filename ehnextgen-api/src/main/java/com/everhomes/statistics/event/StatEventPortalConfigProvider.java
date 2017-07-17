// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface StatEventPortalConfigProvider {

	void createStatEventPortalConfig(StatEventPortalConfig statEventPortalConfig);

	void updateStatEventPortalConfig(StatEventPortalConfig statEventPortalConfig);

	StatEventPortalConfig findStatEventPortalConfigById(Long id);

	// List<StatEventPortalConfig> listStatEventPortalConfig();

}