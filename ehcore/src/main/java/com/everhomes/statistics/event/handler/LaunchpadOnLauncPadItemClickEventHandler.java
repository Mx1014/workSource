// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.rest.launchpad.Widget;
import com.everhomes.server.schema.tables.EhLaunchPadItems;
import com.everhomes.server.schema.tables.pojos.EhPortalItems;
import com.everhomes.statistics.event.*;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * launchPadItem点击
 * Created by xq.tian on 2017/8/7.
 */
@Component
public class LaunchpadOnLauncPadItemClickEventHandler extends AbstractStatEventPortalItemGroupHandler {

    @Autowired
    @Qualifier("PortalLaunchPadMappingProvider-LaunchPad")
    private PortalLaunchPadMappingProvider portalLaunchPadMappingProvider;

    @Autowired
    @Qualifier("PortalItemProvider-LaunchPad")
    private PortalItemProvider portalItemProvider;

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_LAUNCH_PAD_ITEM_CLICK;
    }

    @Override
    protected String getItemGroup(Map<String, String> paramsToValueMap) {
        Long launchPadItemId = Long.valueOf(paramsToValueMap.get("id"));
        PortalLaunchPadMapping mapping = portalLaunchPadMappingProvider.findPortalLaunchPadMapping(EhPortalItems.class.getSimpleName(), launchPadItemId);
        PortalItem portalItem = portalItemProvider.findPortalItemById(mapping.getPortalContentId());
        if (portalItem == null) {
            return null;
        }
        return portalItem.getGroupName();
    }

    @Override
    protected Widget getWidget() {
        return Widget.NAVIGATOR;
    }

    @Override
    protected StatEventStatistic getEventStat(Map<String, String> paramsToValueMap) {
        Long launchPadItemId = Long.valueOf(paramsToValueMap.get("id"));
        PortalLaunchPadMapping mapping = portalLaunchPadMappingProvider.findPortalLaunchPadMapping(EhPortalItems.class.getSimpleName(), launchPadItemId);
        PortalItem portalItem = portalItemProvider.findPortalItemById(mapping.getPortalContentId());

        StatEventStatistic eventStat = new StatEventStatistic();
        eventStat.setOwnerType(EhLaunchPadItems.class.getSimpleName());
        eventStat.setOwnerId(launchPadItemId);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("launchPadItemName", portalItem.getLabel());

        eventStat.setParam(StringHelper.toJsonString(paramMap));
        return eventStat;
    }
}
