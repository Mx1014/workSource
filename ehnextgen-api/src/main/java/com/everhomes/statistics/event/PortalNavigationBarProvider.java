// @formatter:off
package com.everhomes.statistics.event;

import java.util.List;

public interface PortalNavigationBarProvider {

	void createPortalNavigationBar(PortalNavigationBar portalNavigationBar);

	void updatePortalNavigationBar(PortalNavigationBar portalNavigationBar);

	PortalNavigationBar findPortalNavigationBarById(Long id);

    List<PortalNavigationBar> listPortalNavigationBar();

    List<PortalNavigationBar> listPortalNavigationBarByStatus(byte status);
}