// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.launchpad.LaunchPadItem;
import com.everhomes.launchpad.LaunchPadProvider;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.server.schema.tables.EhLaunchPadItems;
import com.everhomes.server.schema.tables.pojos.EhPortalItems;
import com.everhomes.statistics.event.*;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

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

    @Autowired
    private LaunchPadProvider launchPadProvider;

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_LAUNCH_PAD_ITEM_CLICK;
    }

    @Override
    protected Widget getWidget() {
        return Widget.NAVIGATOR;
    }

    @Override
    protected String getItemGroup(Map<String, String> paramsToValueMap) {
        // return paramsToValueMap.get("itemGroup");
        Long launchPadItemId = Long.valueOf(paramsToValueMap.get("id"));
        PortalLaunchPadMapping mapping = portalLaunchPadMappingProvider.findPortalLaunchPadMapping(EhPortalItems.class.getSimpleName(), launchPadItemId);
        PortalItem portalItem = portalItemProvider.findPortalItemById(mapping.getPortalContentId());
        if (portalItem == null) {
            return null;
        }
        return portalItem.getGroupName();
    }

    @Override
    protected StatEventStatistic getEventStat(Map<String, String> paramsToValueMap) {
        // Long namespaceId = Long.valueOf(paramsToValueMap.get("namespaceId"));
        // Long scopeId = Long.valueOf(paramsToValueMap.get("scopeId"));
        // Long scopeCode = Long.valueOf(paramsToValueMap.get("scopeCode"));
        //
        // String sceneType = String.valueOf(paramsToValueMap.get("sceneType"));
        // String itemLocation = String.valueOf(paramsToValueMap.get("itemLocation"));
        // String itemGroup = String.valueOf(paramsToValueMap.get("itemGroup"));
        // String itemName = String.valueOf(paramsToValueMap.get("itemName"));


        Long launchPadItemId = Long.valueOf(paramsToValueMap.get("id"));
        PortalLaunchPadMapping mapping = portalLaunchPadMappingProvider.findPortalLaunchPadMapping(EhPortalItems.class.getSimpleName(), launchPadItemId);
        PortalItem portalItem = portalItemProvider.findPortalItemById(mapping.getPortalContentId());
        // PortalItem portalItem = portalItemProvider.findPortalItemByIdentifier(namespaceId, scopeCode, scopeId, sceneType,
        //         itemLocation, itemGroup, itemName);
        if (portalItem == null) {
            return null;
        }

        LaunchPadItem item = portalItem.getLaunchPadItem();
        String identifier = String.format("%s:%s:%s:%s:%s:%s:%s", item.getNamespaceId(), item.getScopeCode(), item.getScopeId(),
                item.getItemLocation(), item.getSceneType(), item.getItemGroup(), item.getItemName());

        CRC32 crc32 = new CRC32();
        crc32.update(identifier.getBytes(Charset.forName("UTF-8")));

        StatEventStatistic eventStat = new StatEventStatistic();
        eventStat.setOwnerType(EhLaunchPadItems.class.getSimpleName());
        eventStat.setOwnerId(crc32.getValue());

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("launchPadItemName", portalItem.getLabel());

        eventStat.setParam(StringHelper.toJsonString(paramMap));
        return eventStat;
    }

    /*@Override
    public List<StatEventParamLog> processEventParamLogs(StatEventLog log, Map<String, String> param) {
        List<StatEventParamLog> paramLogs = new ArrayList<>();

        long id = Long.parseLong(param.get("id"));
        LaunchPadItem lpt = launchPadProvider.findLaunchPadItemById(id);

        if (lpt != null) {
            StatEventParamLog paramLog = newSimpleParamLog(log);
            paramLog.setParamKey("namespaceId");
            paramLog.setStringValue(String.valueOf(lpt.getNamespaceId()));
            paramLogs.add(paramLog);

            paramLog = newSimpleParamLog(log);
            paramLog.setParamKey("scopeCode");
            paramLog.setStringValue(String.valueOf(lpt.getScopeCode()));
            paramLogs.add(paramLog);

            paramLog = newSimpleParamLog(log);
            paramLog.setParamKey("scopeId");
            paramLog.setStringValue(String.valueOf(lpt.getScopeId()));
            paramLogs.add(paramLog);

            paramLog = newSimpleParamLog(log);
            paramLog.setParamKey("sceneType");
            paramLog.setStringValue(String.valueOf(lpt.getScaleType()));
            paramLogs.add(paramLog);

            paramLog = newSimpleParamLog(log);
            paramLog.setParamKey("itemLocation");
            paramLog.setStringValue(String.valueOf(lpt.getItemLocation()));
            paramLogs.add(paramLog);

            paramLog = newSimpleParamLog(log);
            paramLog.setParamKey("itemGroup");
            paramLog.setStringValue(String.valueOf(lpt.getItemGroup()));
            paramLogs.add(paramLog);

            paramLog = newSimpleParamLog(log);
            paramLog.setParamKey("itemName");
            paramLog.setStringValue(String.valueOf(lpt.getItemName()));
            paramLogs.add(paramLog);

            paramLog = newSimpleParamLog(log);
            paramLog.setParamKey("layoutId");
            paramLog.setStringValue(param.get("layoutId"));
            paramLogs.add(paramLog);
        }
        return paramLogs;
    }*/

    /*@Override
    protected List<StatEventParam> getGroupByParams(List<StatEventParam> params) {
        List<StatEventParam> paramList = new ArrayList<>();
        StatEventParam p = params.get(0);

        StatEventParam param = newSimpleParam(p);
        param.setParamKey("namespaceId");
        paramList.add(param);

        param = newSimpleParam(p);
        param.setParamKey("scopeCode");
        paramList.add(param);

        param = newSimpleParam(p);
        param.setParamKey("scopeId");
        paramList.add(param);

        param = newSimpleParam(p);
        param.setParamKey("sceneType");
        paramList.add(param);

        param = newSimpleParam(p);
        param.setParamKey("itemLocation");
        paramList.add(param);

        param = newSimpleParam(p);
        param.setParamKey("itemGroup");
        paramList.add(param);

        param = newSimpleParam(p);
        param.setParamKey("itemName");
        paramList.add(param);

        param = newSimpleParam(p);
        param.setParamKey("layoutId");
        paramList.add(param);

        return paramList;
    }*/

    /*private StatEventParamLog newSimpleParamLog(StatEventLog log) {
        StatEventParamLog paramLog = new StatEventParamLog();
        paramLog.setStatus(StatEventCommonStatus.ACTIVE.getCode());
        paramLog.setSessionId(log.getSessionId());
        paramLog.setNamespaceId(log.getNamespaceId());
        paramLog.setEventType(log.getEventType());
        paramLog.setEventName(log.getEventName());
        paramLog.setUid(log.getUid());
        paramLog.setEventLogId(log.getId());
        paramLog.setEventVersion(log.getEventVersion());
        paramLog.setUploadTime(log.getUploadTime());
        return paramLog;
    }

    private StatEventParam newSimpleParam(StatEventParam param) {
        return ConvertHelper.convert(param, StatEventParam.class);
    }*/
}