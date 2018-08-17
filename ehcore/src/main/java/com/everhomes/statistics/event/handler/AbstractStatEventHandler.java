// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.rest.statistics.event.StatEventCommonStatus;
import com.everhomes.rest.statistics.event.StatEventParamType;
import com.everhomes.statistics.event.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xq.tian on 2017/8/7.
 */
abstract class AbstractStatEventHandler implements StatEventHandler {

    static final String PORTAL_ON_BOTTOM_NAVIGATION_CLICK = "portal_on_bottom_navigation_click";
    static final String PORTAL_ON_NAVIGATION_CLICK = "portal_on_navigation_click";
    static final String LAUNCHPAD_ON_BANNER_CLICK = "launchpad_on_banner_click";
    static final String LAUNCHPAD_ON_LAUNCH_PAD_ITEM_CLICK = "launchpad_on_launch_pad_item_click";
    static final String LAUNCHPAD_ON_BULLETIN_CLICK = "launchpad_on_bulletin_click";
    static final String LAUNCHPAD_ON_OPPUSH_BIZ_ITEM_CLICK = "launchpad_on_oppush_biz_item_click";
    static final String LAUNCHPAD_ON_OPPUSH_ACTIVITY_ITEM_CLICK = "launchpad_on_oppush_activity_item_click";
    static final String LAUNCHPAD_ON_OPPUSH_SERVICE_ALLIANCE_ITEM_CLICK = "launchpad_on_oppush_service_alliance_item_click";
    static final String LAUNCHPAD_ON_NEWS_ITEM_CLICK = "launchpad_on_news_item_click";
    static final String LAUNCHPAD_ON_NEWS_FLASH_ITEM_CLICK = "launchpad_on_news_flash_item_click";

    @Autowired
    private StatEventParamProvider statEventParamProvider;

    @Override
    public List<StatEventParamLog> processEventParamLogs(StatEventLog log, Map<String, String> param) {
        List<StatEventParamLog> paramLogs = new ArrayList<>();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            StatEventParam statEventParam = statEventParamProvider.findStatEventParam(log.getEventName(), entry.getKey());
            if (statEventParam != null) {
                StatEventParamLog paramLog = new StatEventParamLog();
                paramLog.setStatus(StatEventCommonStatus.ACTIVE.getCode());
                paramLog.setSessionId(log.getSessionId());
                paramLog.setNamespaceId(log.getNamespaceId());
                paramLog.setEventType(log.getEventType());
                paramLog.setEventName(log.getEventName());
                paramLog.setUid(log.getUid());
                paramLog.setEventLogId(log.getId());
                paramLog.setParamKey(entry.getKey());
                paramLog.setEventVersion(log.getEventVersion());
                paramLog.setUploadTime(log.getUploadTime());
                if (statEventParam.getParamType() == StatEventParamType.NUMBER.getCode()) {
                    paramLog.setNumberValue(Integer.valueOf(entry.getValue()));
                } else {
                    paramLog.setStringValue(entry.getValue());
                }
                paramLogs.add(paramLog);
            }
        }
        return paramLogs;
    }
}
