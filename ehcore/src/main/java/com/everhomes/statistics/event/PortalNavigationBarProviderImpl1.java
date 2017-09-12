package com.everhomes.statistics.event;

import com.everhomes.rest.statistics.event.StatEventPortalConfigType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xq.tian on 2017/8/19.
 */
@Component("PortalNavigationBarProvider-Config")
public class PortalNavigationBarProviderImpl1 implements PortalNavigationBarProvider {

    @Autowired
    private StatEventPortalConfigProvider statEventPortalConfigProvider;

    @Override
    public void createPortalNavigationBar(PortalNavigationBar portalNavigationBar) {
        throw new RuntimeException("createPortalNavigationBar not supported");
    }

    @Override
    public void updatePortalNavigationBar(PortalNavigationBar portalNavigationBar) {
        throw new RuntimeException("updatePortalNavigationBar not supported");
    }

    @Override
    public PortalNavigationBar findPortalNavigationBarById(Long id) {
        throw new RuntimeException("findPortalNavigationBarById not supported");
    }

    @Override
    public List<PortalNavigationBar> listPortalNavigationBar() {
        throw new RuntimeException("listPortalNavigationBar not supported");
    }

    @Override
    public List<PortalNavigationBar> listPortalNavigationBarByStatus(byte status) {
        List<PortalNavigationBar> navigationBars = new ArrayList<>();

        List<StatEventPortalConfig> portalConfigs = statEventPortalConfigProvider.listStatEventPortalConfigs(StatEventPortalConfigType.BOTTOM_NAVIGATION.getCode(), status);
        for (StatEventPortalConfig config : portalConfigs) {
            PortalNavigationBar bar = new PortalNavigationBar();
            bar.setId(config.getId());
            bar.setNamespaceId(config.getNamespaceId());
            bar.setLabel(config.getDisplayName());
            bar.setDescription(config.getDescription());

            navigationBars.add(bar);
        }
        return navigationBars;
    }
}
