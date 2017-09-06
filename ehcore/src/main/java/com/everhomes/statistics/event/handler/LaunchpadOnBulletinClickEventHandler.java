// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.rest.launchpad.Widget;
import com.everhomes.statistics.event.StatEventStatistic;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 公告栏点击
 * Created by xq.tian on 2017/8/7.
 */
@Component
public class LaunchpadOnBulletinClickEventHandler extends AbstractStatEventPortalItemGroupHandler {

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_BULLETIN_CLICK;
    }

    @Override
    protected String getItemGroup(Map<String, String> paramsToValueMap) {
        return "Default";
    }

    @Override
    protected Widget getWidget() {
        return Widget.BULLETINS;
    }

    @Override
    protected StatEventStatistic getEventStat(Map<String, String> paramsToValueMap) {
        StatEventStatistic eventStat = new StatEventStatistic();
        eventStat.setOwnerType("EhBulletins");
        eventStat.setOwnerId(0L);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bulletinsName", "公告栏");

        eventStat.setParam(StringHelper.toJsonString(paramMap));
        return eventStat;
    }
}
