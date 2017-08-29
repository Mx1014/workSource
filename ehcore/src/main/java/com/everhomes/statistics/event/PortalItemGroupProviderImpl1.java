package com.everhomes.statistics.event;

import com.everhomes.launchpad.LaunchPadLayout;
import com.everhomes.launchpad.LaunchPadProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.server.schema.tables.pojos.EhPortalItemGroups;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.zip.CRC32;

/**
 * Created by xq.tian on 2017/8/10.
 */
@Repository("PortalItemGroupProvider-LaunchPad")
public class PortalItemGroupProviderImpl1 implements PortalItemGroupProvider {

    @Autowired
    private LaunchPadProvider launchPadProvider;

    @Autowired
    private LocaleStringService localeStringService;

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
            pig.setLabel(getGroupName(group));
            pig.setDescription(getGroupName(group));
            pig.setLayoutId(layoutId);
            pig.setWidget(group.widget);
            pig.setName(itemGroup);
            pig.setStatus(status);
            pig.setDefaultOrder(group.defaultOrder);

            portalItemGroupList.add(pig);
        }
        portalItemGroupList.sort(Comparator.comparingInt(EhPortalItemGroups::getDefaultOrder));
        return portalItemGroupList;
    }

    private String getGroupName(LayoutItemGroup group) {
        if (group.groupName == null || group.groupName.isEmpty()) {
            int code = 0;
            Widget widget = Widget.fromCode(group.widget);
            if (widget != null) {
                switch (widget) {
                    case BANNERS:
                        // group.groupName = "Banner";
                        code = StatEventLocalStringCode.WIDGET_NAME_BANNER;
                        break;
                    case BULLETINS:
                        // group.groupName = "公告栏";
                        code = StatEventLocalStringCode.WIDGET_NAME_BULLETINS;
                        break;
                    case NEWS:
                    case NEWS_FLASH:
                        // group.groupName = "新闻快讯";
                        code = StatEventLocalStringCode.WIDGET_NAME_NEWS_AND_NEWS_FLASH;
                        break;
                    case OPPUSH:
                        String itemGroup = group.instanceConfig.get("itemGroup");
                        switch (itemGroup) {
                            case "OPPushBiz":
                                // group.groupName = "电商运营";
                                code = StatEventLocalStringCode.WIDGET_NAME_OPPUSH_BIZ;
                                break;
                            case "OPPushActivity":
                                // group.groupName = "活动运营";
                                code = StatEventLocalStringCode.WIDGET_NAME_OPPUSH_ACTIVITY;
                                break;
                            case "Gallery":
                                // group.groupName = "服务联盟运营";
                                code = StatEventLocalStringCode.WIDGET_NAME_OPPUSH_SERVICE_ALLIANCE;
                                break;
                        }
                        break;
                    case NAVIGATOR:
                        // group.groupName = "业务应用组";
                        code = StatEventLocalStringCode.WIDGET_NAME_NAVIGATOR;
                        break;
                    case TAB:
                        // group.groupName = "标签栏";
                        code = StatEventLocalStringCode.WIDGET_NAME_TAB;
                        break;
                }

            }
            group.groupName = localeStringService.getLocalizedString(StatEventLocalStringCode.SCOPE, String.valueOf(code), Locale.CHINA.toString(), "");
        }
        return group.groupName;
    }

    public static class LayoutItemGroup {
        String groupName;
        String widget;
        Integer defaultOrder;
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
                pig.setLabel(getGroupName(group));
                pig.setDescription(getGroupName(group));
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
