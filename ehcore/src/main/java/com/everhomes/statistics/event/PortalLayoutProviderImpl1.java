package com.everhomes.statistics.event;

import com.everhomes.launchpad.LaunchPadProvider;
import com.everhomes.rest.launchpad.LaunchPadLayoutDTO;
import com.everhomes.rest.launchpad.LaunchPadLayoutStatus;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/8/10.
 */
@Repository("PortalLayoutProvider-LaunchPad")
public class PortalLayoutProviderImpl1 implements PortalLayoutProvider {

    @Autowired
    private LaunchPadProvider launchPadProvider;

    @Override
    public List<PortalLayout> listPortalLayoutByStatus(byte status) {
        List<LaunchPadLayoutDTO> layoutDTOList = launchPadProvider.listLaunchPadLayoutByKeyword(10000, 0, null);
        layoutDTOList = layoutDTOList.stream()
                .filter(r -> LaunchPadLayoutStatus.fromCode(r.getStatus()) == LaunchPadLayoutStatus.ACTIVE)
                .collect(Collectors.toList());

        List<PortalLayout> portalLayoutList = new ArrayList<>();
        for (LaunchPadLayoutDTO layout : layoutDTOList) {
            PortalLayout portalLayout = new PortalLayout();

            portalLayout.setNamespaceId(layout.getNamespaceId());

            Map<String, String> map = (Map<String, String>) StringHelper.fromJsonString(layout.getLayoutJson(), HashMap.class);

            portalLayout.setName(layout.getName());
            portalLayout.setLabel(map.get("displayName"));
            portalLayout.setDescription(layout.getName());

            portalLayout.setId(layout.getId());
            portalLayout.setStatus(status);

            portalLayoutList.add(portalLayout);
        }
        return portalLayoutList;
    }

    @Override
    public void createPortalLayout(PortalLayout portalLayout) {
        throw new RuntimeException("createPortalLayout not supported");
    }

    @Override
    public void updatePortalLayout(PortalLayout portalLayout) {
        throw new RuntimeException("updatePortalLayout not supported");
    }

    @Override
    public PortalLayout findPortalLayoutById(Long id) {
        throw new RuntimeException("findPortalLayoutById not supported");
    }
}
