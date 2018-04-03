package com.everhomes.statistics.event;

import org.springframework.stereotype.Repository;

/**
 * Created by xq.tian on 2017/8/11.
 */
@Repository("PortalLaunchPadMappingProvider-LaunchPad")
public class PortalLaunchPadMappingProviderImpl1 implements PortalLaunchPadMappingProvider {

    @Override
    public void createPortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping) {
        throw new RuntimeException("createPortalLaunchPadMapping not supported");
    }

    @Override
    public void updatePortalLaunchPadMapping(PortalLaunchPadMapping portalLaunchPadMapping) {
        throw new RuntimeException("updatePortalLaunchPadMapping not supported");
    }

    @Override
    public PortalLaunchPadMapping findById(Long id) {
        throw new RuntimeException("findById not supported");
    }

    @Override
    public PortalLaunchPadMapping findPortalLaunchPadMapping(String contentType, Long launchPadContentId) {
        PortalLaunchPadMapping mapping = new PortalLaunchPadMapping();
        mapping.setPortalContentId(launchPadContentId);
        return mapping;
    }
}
