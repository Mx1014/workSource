// @formatter:off
package com.everhomes.statistics.event;

public interface PortalLaunchPadMappingProvider {

    void createPortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping);

    void updatePortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping);

    PortalLaunchPadMapping findById(Long id);

    PortalLaunchPadMapping findPortalLaunchPadMapping(String contentType, Long launchPadContentId);
}