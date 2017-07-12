// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalLaunchPadMappingProvider {

	void createPortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping);

	void updatePortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping);

	PortalLaunchPadMapping findPortalLaunchPadMappingById(Long id);

	List<PortalLaunchPadMapping> listPortalLaunchPadMapping(String contentType, Long portalContentId, Long launchPadContentId);

}