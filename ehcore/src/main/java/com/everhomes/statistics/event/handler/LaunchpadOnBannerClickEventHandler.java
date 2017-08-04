// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.banner.Banner;
import com.everhomes.banner.BannerProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.rest.statistics.event.StatEventPortalStatType;
import com.everhomes.rest.statistics.event.StatEventStatTimeInterval;
import com.everhomes.server.schema.tables.EhBanners;
import com.everhomes.server.schema.tables.EhPortalItemGroups;
import com.everhomes.server.schema.tables.EhPortalLayouts;
import com.everhomes.statistics.event.*;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Banner点击
 * Created by xq.tian on 2017/8/7.
 */
@Component(AbstractStatEventHandler.EVENT_HANDLER_PREFIX + AbstractStatEventHandler.LAUNCHPAD_ON_BANNER_CLICK)
public class LaunchpadOnBannerClickEventHandler extends AbstractStatEventHandler {

    @Autowired
    private StatEventPortalStatisticProvider statEventPortalStatisticProvider;

    @Autowired
    private StatEventParamProvider statEventParamProvider;

    @Autowired
    private StatEventParamLogProvider statEventParamLogProvider;

    @Autowired
    @Qualifier("PortalLaunchPadMappingProvider-LaunchPad")
    private PortalLaunchPadMappingProvider portalLaunchPadMappingProvider;

    @Autowired
    @Qualifier("PortalItemGroupProvider-LaunchPad")
    private PortalItemGroupProvider portalItemGroupProvider;

    @Autowired
    private BannerProvider bannerProvider;

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_BANNER_CLICK;
    }

    @Override
    public List<StatEventStatistic> process(Namespace namespace, StatEvent statEvent, LocalDate statDate) {
        Timestamp minTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MIN));
        Timestamp maxTime = Timestamp.valueOf(LocalDateTime.of(statDate, LocalTime.MAX));
        Date date = Date.valueOf(statDate);

        List<StatEventParam> params = statEventParamProvider.listParam(statEvent.getEventName(), statEvent.getEventVersion());

        StatEventParam bannerIdParam = new StatEventParam();
        StatEventParam layoutIdParam = new StatEventParam();
        for (StatEventParam p : params) {
            if (p.getParamKey().equals("id")) {
                bannerIdParam = p;
            } else if (p.getParamKey().equals("layoutId")) {
                layoutIdParam = p;
            }
        }

        List<Banner> banners = bannerProvider.listBannersByNamespace(namespace.getId());

        Map<Long, List<Banner>> idToBannerMap = banners.stream().collect(Collectors.groupingBy(Banner::getId));

        List<StatEventParamLog> eventParamLogs = statEventParamLogProvider.listEventParamLog(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), minTime, maxTime);

        Map<Long, List<StatEventParamLog>> eventLogIdToParamLogListMap = eventParamLogs.stream().collect(Collectors.groupingBy(StatEventParamLog::getEventLogId));

        // 从paramLog组装bannerId与layoutId映射的map
        Map<Long, Long> bannerIdToLayoutIdMap = new HashMap<>();
        for (Map.Entry<Long, List<StatEventParamLog>> entry : eventLogIdToParamLogListMap.entrySet()) {
            List<StatEventParamLog> value = entry.getValue();
            Long bannerId = null;
            Long layoutId = null;
            for (StatEventParamLog log : value) {
                if (log.getParamKey().equals(bannerIdParam.getParamKey())) {
                    bannerId = Long.valueOf(log.getStringValue());
                } else if (log.getParamKey().equals(layoutIdParam.getParamKey())) {
                    layoutId = Long.valueOf(log.getStringValue());
                }
            }
            bannerIdToLayoutIdMap.putIfAbsent(bannerId, layoutId);
        }

        // 每个参数发生的次数 map: {参数值:次数}
        Map<String, Integer> bannerIdParamValueToCountMap = statEventParamLogProvider.countParamTotalCount(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), bannerIdParam.getParamKey(), minTime, maxTime);

        // 独立session数map: {参数值:次数}
        Map<String, Integer> countDistinctSessionMap = statEventParamLogProvider.countDistinctSession(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), bannerIdParam.getParamKey(), minTime, maxTime);

        // 独立用户数 map: {参数值:次数}
        Map<String, Integer> countDistinctUidMap = statEventParamLogProvider.countDistinctUid(
                namespace.getId(), statEvent.getEventName(), statEvent.getEventVersion(), bannerIdParam.getParamKey(), minTime, maxTime);

        List<StatEventStatistic> statList = new ArrayList<>();
        // map: {参数值:次数}
        for (Map.Entry<String, Integer> entry : bannerIdParamValueToCountMap.entrySet()) {

            Long bannerId = Long.valueOf(entry.getKey());
            Long layoutId = bannerIdToLayoutIdMap.get(bannerId);

            List<Banner> bannerList = idToBannerMap.get(bannerId);
            if (bannerList == null || bannerList.isEmpty()) {
                continue;
            }
            Banner banner = bannerList.get(0);

            // 去映射表里拿到门户配置的layoutId => 去portalItemGroup里拿到portalItemGroup => 去portalStatistic里拿到统计记录的id => 保存数据
            PortalLaunchPadMapping mapping = portalLaunchPadMappingProvider.findPortalLaunchPadMapping(EhPortalLayouts.class.getSimpleName(), layoutId);

            PortalItemGroup portalItemGroup = portalItemGroupProvider.findPortalItemGroup(mapping.getPortalContentId(), Widget.BANNERS.getCode(), banner.getBannerGroup());
            if (portalItemGroup == null) {
                continue;
            }

            StatEventPortalStatistic portalStat = statEventPortalStatisticProvider.findStatEventPortalStatistic(
                    namespace.getId(), StatEventPortalStatType.PORTAL_ITEM_GROUP.getCode(), EhPortalItemGroups.class.getSimpleName(),
                    portalItemGroup.getId(), date);
            if (portalStat == null) {
                continue;
            }

            StatEventStatistic eventStat = new StatEventStatistic();

            eventStat.setStatDate(date);
            eventStat.setNamespaceId(namespace.getId());

            eventStat.setTotalCount(entry.getValue().longValue());

            Integer completedSessions = countDistinctSessionMap.get(entry.getKey());
            eventStat.setCompletedSessions(completedSessions.longValue());

            Integer uniqueUsers = countDistinctUidMap.get(entry.getKey());
            eventStat.setUniqueUsers(uniqueUsers.longValue());

            eventStat.setEventName(statEvent.getEventName());
            eventStat.setEventPortalStatId(portalStat.getId());
            eventStat.setEventDisplayName(statEvent.getEventDisplayName());

            eventStat.setEventType(statEvent.getEventType());
            eventStat.setEventVersion(statEvent.getEventVersion());
            eventStat.setTimeInterval(StatEventStatTimeInterval.DAILY.getCode());

            eventStat.setOwnerType(EhBanners.class.getSimpleName());
            eventStat.setOwnerId(bannerId);

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("bannerId", bannerId);
            paramMap.put("bannerName", banner.getName());

            eventStat.setParam(StringHelper.toJsonString(paramMap));

            statList.add(eventStat);
        }
        return statList;
    }
}
