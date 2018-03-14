// @formatter:off
package com.everhomes.portal;

import java.util.List;

public interface PortalLaunchPadMappingProvider {

	void createPortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping);

    void createPortalLaunchPadMappings(List<PortalLaunchPadMapping> portalLaunchPadMappings);

    void updatePortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping);

    void deleteByVersionId(Long versionId);

    PortalLaunchPadMapping findPortalLaunchPadMappingById(Long id);

	List<PortalLaunchPadMapping> listPortalLaunchPadMapping(String contentType, Long portalContentId, Long launchPadContentId);

	void deletePortalLaunchPadMapping(Long id);
}