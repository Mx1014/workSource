// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalLayoutProvider {

	void createPortalLayout(PortalLayout portalLayout);

	void updatePortalLayout(PortalLayout portalLayout);

	PortalLayout findPortalLayoutById(Long id);

	List<PortalLayout> listPortalLayout(Integer namespaceId);

}