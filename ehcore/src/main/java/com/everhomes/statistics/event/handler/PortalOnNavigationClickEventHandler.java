// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.rest.statistics.event.StatEventPortalConfigType;
import com.everhomes.rest.statistics.event.StatEventPortalStatType;
import com.everhomes.statistics.event.StatEventParam;
import com.everhomes.statistics.event.StatEventPortalStatistic;
import com.everhomes.statistics.event.StatEventStatistic;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 顶部工具栏
 * Created by xq.tian on 2017/8/7.
 */
@Component
public class PortalOnNavigationClickEventHandler extends AbstractStatEventPortalConfigHandler {

    @Override
    public String getEventName() {
        return PORTAL_ON_NAVIGATION_CLICK;
    }

    @Override
    protected StatEventPortalStatType getStatType() {
        return StatEventPortalStatType.TOP_NAVIGATION;
    }

    @Override
    protected StatEventPortalConfigType getConfigType() {
        return StatEventPortalConfigType.TOP_NAVIGATION;
    }

    @Override
    protected StatEventStatistic getEventStat(String identifierParamsValue, StatEventPortalStatistic portalStat) {
        StatEventStatistic eventStat = new StatEventStatistic();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("topNavigationName", portalStat.getDisplayName());

        eventStat.setParam(StringHelper.toJsonString(paramMap));
        return eventStat;
    }

    protected StatEventParam getIdentifierParam(List<StatEventParam> params) {
        for (StatEventParam p : params) {
            if (p.getParamKey().equals("identifier")) {
                return p;
            }
        }
        return null;
    }
}
