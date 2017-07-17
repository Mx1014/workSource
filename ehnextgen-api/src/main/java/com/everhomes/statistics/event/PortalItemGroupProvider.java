// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface PortalItemGroupProvider {

	void createPortalItemGroup(PortalItemGroup portalItemGroup);

	void updatePortalItemGroup(PortalItemGroup portalItemGroup);

	PortalItemGroup findPortalItemGroupById(Long id);

	// List<PortalItemGroup> listPortalItemGroup();

}