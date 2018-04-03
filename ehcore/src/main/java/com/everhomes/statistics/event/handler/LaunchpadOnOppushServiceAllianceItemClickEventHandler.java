// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.statistics.event.StatEventStatistic;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务联盟运营点击
 * Created by xq.tian on 2017/8/7.
 */
@Component
public class LaunchpadOnOppushServiceAllianceItemClickEventHandler extends AbstractStatEventPortalItemGroupHandler {

    @Autowired
    private YellowPageProvider yellowPageProvider;

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_OPPUSH_SERVICE_ALLIANCE_ITEM_CLICK;
    }

    @Override
    protected String getItemGroup(Map<String, String> paramsToValueMap) {
        return "Gallery";
    }

    @Override
    protected Widget getWidget() {
        return Widget.OPPUSH;
    }

    @Override
    protected StatEventStatistic getEventStat(Map<String, String> paramsToValueMap) {
        StatEventStatistic eventStat = new StatEventStatistic();

        Long serviceAllianceId = Long.valueOf(paramsToValueMap.get("id"));

        ServiceAlliances serviceAlliances = yellowPageProvider.findServiceAllianceById(serviceAllianceId, null, null);
        if (serviceAlliances == null) {
            return null;
        }
        eventStat.setOwnerType(EntityType.SERVICE_ALLIANCES.getCode());
        eventStat.setOwnerId(serviceAllianceId);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("serviceAllianceId", serviceAllianceId);
        paramMap.put("serviceAllianceName", serviceAlliances.getName());

        eventStat.setParam(StringHelper.toJsonString(paramMap));
        return eventStat;
    }
}
