// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.activity.Activity;
import com.everhomes.activity.ActivityProivider;
import com.everhomes.rest.common.EntityType;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.statistics.event.StatEventParam;
import com.everhomes.statistics.event.StatEventStatistic;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动运营点击
 * Created by xq.tian on 2017/8/7.
 */
@Component
public class LaunchpadOnOppushActivityItemClickEventHandler extends AbstractStatEventPortalItemGroupHandler {

    @Autowired
    private ActivityProivider activityProivider;

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_OPPUSH_ACTIVITY_ITEM_CLICK;
    }

    @Override
    protected String getItemGroup(String key, Long layoutId) {
        return "OPPushActivity";
    }

    @Override
    protected Widget getWidget() {
        return Widget.OPPUSH;
    }

    @Override
    protected StatEventStatistic getEventStat(Map<String, String> paramsToValueMap) {
        StatEventStatistic eventStat = new StatEventStatistic();

        Long topicId = Long.valueOf(paramsToValueMap.get("topicId"));
        Activity activity = activityProivider.findSnapshotByPostId(topicId);
        if (activity == null) {
            return null;
        }
        eventStat.setOwnerType(EntityType.ACTIVITY.getCode());
        eventStat.setOwnerId(activity.getId());

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("activityId", activity.getId());
        paramMap.put("activitySubject", activity.getSubject());

        eventStat.setParam(StringHelper.toJsonString(paramMap));
        return eventStat;
    }

    @Override
    protected StatEventParam getIdentifierParam(List<StatEventParam> params) {
        for (StatEventParam param : params) {
            if (param.getParamKey().equals("topicId")) {
                return param;
            }
        }
        return null;
    }
}
