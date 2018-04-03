// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface PortalLayoutProvider {

	void createPortalLayout(PortalLayout portalLayout);

	void updatePortalLayout(PortalLayout portalLayout);

	PortalLayout findPortalLayoutById(Long id);

    List<PortalLayout> listPortalLayoutByStatus(byte status);

    // List<PortalLayout> listPortalLayout();

}