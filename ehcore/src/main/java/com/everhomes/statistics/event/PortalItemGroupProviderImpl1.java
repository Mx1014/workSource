package com.everhomes.statistics.event;

import com.everhomes.launchpad.LaunchPadLayout;
import com.everhomes.launchpad.LaunchPadProvider;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

/**
 * Created by xq.tian on 2017/8/10.
 */
@Repository("PortalItemGroupProvider-LaunchPad")
public class PortalItemGroupProviderImpl1 implements PortalItemGroupProvider {

    @Autowired
    private LaunchPadProvider launchPadProvider;

    @Override
    public List<PortalItemGroup> listPortalItemGroupByStatus(Long layoutId, byte status) {
        LaunchPadLayout layout = launchPadProvider.findLaunchPadLayoutById(layoutId);

        String layoutJson = layout.getLayoutJson();

        LayoutJson layoutJsonObj = (LayoutJson) StringHelper.fromJsonString(layoutJson, LayoutJson.class);

        CRC32 crc32 = new CRC32();
        List<PortalItemGroup> portalItemGroupList = new ArrayList<>();
        for (LayoutItemGroup group : layoutJsonObj.groups) {
            PortalItemGroup pig = new PortalItemGroup();
            String itemGroup = group.instanceConfig.get("itemGroup");
            // 给每个itemGroup生成一个唯一的id
            String itemGroupIdentifier = layout.getNamespaceId() + layout.getName() + layout.getSceneType() + layout.getScopeCode() + layout.getScopeId() + group.widget + itemGroup;
            crc32.update(itemGroupIdentifier.getBytes());
            pig.setId(crc32.getValue());
            crc32.reset();
            pig.setNamespaceId(layout.getNamespaceId());
            pig.setLabel(group.groupName);
            pig.setDescription(group.groupName);
            pig.setLayoutId(layoutId);
            pig.setWidget(group.widget);
            pig.setName(itemGroup);
            pig.setStatus(status);

            portalItemGroupList.add(pig);
        }
        return portalItemGroupList;
    }

    public static class LayoutItemGroup {
        String groupName;
        String widget;
        Map<String, String> instanceConfig;
    }

    public static class LayoutJson {
        String layoutName;
        String displayName;
        List<LayoutItemGroup> groups;
    }

    @Override
    public PortalItemGroup findPortalItemGroup(Long layoutId, String widgetName, String itemGroup) {
        LaunchPadLayout layout = launchPadProvider.findLaunchPadLayoutById(layoutId);

        String layoutJson = layout.getLayoutJson();

        LayoutJson layoutJsonObj = (LayoutJson) StringHelper.fromJsonString(layoutJson, LayoutJson.class);

        CRC32 crc32 = new CRC32();
        for (LayoutItemGroup group : layoutJsonObj.groups) {
            if (group.widget.equals(widgetName) && group.instanceConfig.get("itemGroup").equals(itemGroup)) {
                PortalItemGroup pig = new PortalItemGroup();
                // 给每个itemGroup生成一个唯一的id
                String itemGroupIdentifier = layout.getNamespaceId() + layout.getName() + layout.getSceneType() + layout.getScopeCode() + layout.getScopeId() + group.widget + itemGroup;
                crc32.update(itemGroupIdentifier.getBytes());
                pig.setId(crc32.getValue());
                crc32.reset();
                pig.setNamespaceId(layout.getNamespaceId());
                pig.setLabel(group.groupName);
                pig.setDescription(group.groupName);
                pig.setLayoutId(layoutId);
                pig.setWidget(group.widget);
                pig.setName(itemGroup);
                return pig;
            }
        }
        return null;
    }

    @Override
    public void createPortalItemGroup(PortalItemGroup portalItemGroup) {
        throw new RuntimeException("createPortalLayout not supported");
    }

    @Override
    public void updatePortalItemGroup(PortalItemGroup portalItemGroup) {
        throw new RuntimeException("createPortalLayout not supported");
    }

    @Override
    public PortalItemGroup findPortalItemGroupById(Long id) {
        throw new RuntimeException("createPortalLayout not supported");
    }
}
