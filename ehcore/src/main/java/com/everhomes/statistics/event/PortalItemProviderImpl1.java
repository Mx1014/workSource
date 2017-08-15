package com.everhomes.statistics.event;

import com.everhomes.launchpad.LaunchPadItem;
import com.everhomes.launchpad.LaunchPadProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xq.tian on 2017/8/12.
 */
@Repository("PortalItemProvider-LaunchPad")
public class PortalItemProviderImpl1 implements PortalItemProvider {

    @Autowired
    private LaunchPadProvider launchPadProvider;

    @Override
    public List<PortalItem> listPortalItem(Integer namespaceId, byte status) {
        List<LaunchPadItem> launchPadItemList = launchPadProvider.listLaunchPadItemsByNamespaceId(namespaceId);
        List<PortalItem> portalItemList = new ArrayList<>();
        for (LaunchPadItem launchPadItem : launchPadItemList) {
            PortalItem portalItem = new PortalItem();
            portalItem.setLabel(launchPadItem.getItemLabel());
            portalItem.setName(launchPadItem.getItemName());
            portalItem.setId(launchPadItem.getId());
            portalItem.setNamespaceId(namespaceId);
            portalItem.setActionType(String.valueOf(launchPadItem.getActionType()));
            portalItem.setActionData(launchPadItem.getActionData());
            portalItem.setGroupName(launchPadItem.getItemGroup());
            portalItemList.add(portalItem);
        }
        return portalItemList;
    }

    @Override
    public void createPortalItem(PortalItem portalItem) {
        throw new RuntimeException("createPortalItem not supported");
    }

    @Override
    public void updatePortalItem(PortalItem portalItem) {
        throw new RuntimeException("updatePortalItem not supported");
    }

    @Override
    public PortalItem findPortalItemById(Long id) {
        LaunchPadItem launchPadItem = launchPadProvider.findLaunchPadItemById(id);
        if (launchPadItem != null) {
            PortalItem portalItem = new PortalItem();
            portalItem.setLabel(launchPadItem.getItemLabel());
            portalItem.setName(launchPadItem.getItemName());
            portalItem.setId(launchPadItem.getId());
            portalItem.setNamespaceId(launchPadItem.getNamespaceId());
            portalItem.setActionType(String.valueOf(launchPadItem.getActionType()));
            portalItem.setActionData(launchPadItem.getActionData());
            portalItem.setGroupName(launchPadItem.getItemGroup());
            return portalItem;
        }
        return null;
    }
}
