// @formatter:off
package com.everhomes.visitorsys;

import java.util.List;

public interface VisitorSysConfigurationProvider {

	void createVisitorSysConfiguration(VisitorSysConfiguration visitorSysConfiguration);

	void updateVisitorSysConfiguration(VisitorSysConfiguration visitorSysConfiguration);

	VisitorSysConfiguration findVisitorSysConfigurationById(Long id);

	List<VisitorSysConfiguration> listVisitorSysConfiguration();

	VisitorSysConfiguration findVisitorSysConfigurationByOwner(Integer namespaceId, String ownerType, Long ownerId);

    VisitorSysConfiguration findVisitorSysConfigurationByOwnerToken(String ownerToken);
}