package com.everhomes.point;

import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.point.ListPointRulesCommand;
import com.everhomes.rest.point.PointEventLogStatus;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateUtils;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/12/5.
 */
@Component
public class PointLocalBusSubscriber implements LocalBusSubscriber, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointLocalBusSubscriber.class);

    @Value("${core.server.id:}")
    private String serverId;

    @Value("#{T(java.util.Arrays).asList(${core.server.list:})}")
    private List<String> serverIdList;

    private final ReentrantLock lock = new ReentrantLock();

    private final Map<String, PointEventGroup> eventNameToPointEventGroupMap = new HashMap<>();
    private final Map<PointEventGroup, CopyOnWriteArrayList<PointEventLog>> pointEventGroupCache = new HashMap<>();

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(
            10, new CustomizableThreadFactory("PointLocalBusSubscriber-"));

    @Autowired
    private PointRuleCategoryProvider pointRuleCategoryProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private PointRuleProvider pointRuleProvider;

    @Autowired
    private PointEventLogProvider pointEventLogProvider;

    @Autowired
    private PointRuleToEventMappingProvider pointRuleToEventMappingProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null
                && serverId != null && serverId.trim().length() > 0
                && serverIdList != null && serverIdList.size() > 0) {
            registerPointRuleCategory();
            initPointRuleCategoryQueue();
            initScheduledExecutorService();
        }
    }

    private void initScheduledExecutorService() {
        scheduledExecutorService.schedule(this::persistAllEventLog, 60, TimeUnit.SECONDS);
    }

    private void persistAllEventLog() {
        try {
            lock.lock();
            pointEventGroupCache.keySet().forEach(this::persistGroupEventLog);
        } catch (Exception e) {
            LOGGER.error("Point persist group event log error", e);
        } finally {
            lock.unlock();
            scheduledExecutorService.schedule(this::persistAllEventLog, 60, TimeUnit.SECONDS);
        }
    }

    private void persistGroupEventLog(PointEventGroup eventGroup) {
        if (eventGroup.tryLock()) {
            int random = (int) (Math.random() * 5);
            if (random == 4) {
                LOGGER.info("Start to persist point event log");
            }

            CopyOnWriteArrayList<PointEventLog> eventList = pointEventGroupCache.get(eventGroup);
            List<PointEventLog> removeLogs = new ArrayList<>();
            try {
                if (eventList.size() > 0) {
                    removeLogs.addAll(eventList);
                    eventList.removeAll(removeLogs);
                }
            } finally {
                eventGroup.unlock();
            }
            if (random == 4) {
                LOGGER.info("Persist point event log size {}", removeLogs.size());
            }
            pointEventLogProvider.createPointEventLogsWithId(removeLogs);
        }
    }

    void initPointRuleCategoryQueue() {
        try {
            lock.lock();
            List<PointRuleCategory> pointRuleCategoryList = pointRuleCategoryProvider.listPointRuleCategories();
            // 事件队列
            for (PointRuleCategory category : pointRuleCategoryList) {
                PointEventGroup eventGroup = new PointEventGroup(category);
                pointEventGroupCache.put(eventGroup, new CopyOnWriteArrayList<>());

                ListPointRulesCommand cmd = new ListPointRulesCommand();
                cmd.setCategoryId(category.getId());
                cmd.setSystemId(PointConstant.CONFIG_POINT_SYSTEM_ID);

                List<PointRule> pointRules = pointRuleProvider.listPointRules(cmd, -1, new ListingLocator());
                for (PointRule rule : pointRules) {
                    List<PointRuleToEventMapping> mappings = pointRuleToEventMappingProvider.listByPointRule(PointConstant.CONFIG_POINT_SYSTEM_ID, rule.getId());
                    // 事件监听器
                    for (PointRuleToEventMapping mapping : mappings) {
                        eventNameToPointEventGroupMap.put(mapping.getEventName(), eventGroup);
                        LocalEventBus.subscribe(mapping.getEventName(), this);
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private void registerPointRuleCategory() {
        // 真正的全局锁，不用再细粒度
        coordinationProvider.getNamedLock(CoordinationLocks.POINT_UPDATE_RULE_CATEGORY_SERVER_ID.getCode()).enter(() -> {
            List<PointRuleCategory> allPointRuleCategoryList = pointRuleCategoryProvider.listPointRuleCategories();

            // 已经注册的服务器id列表
            List<String> registeredServers = allPointRuleCategoryList.stream()
                    .map(PointRuleCategory::getServerId)
                    .filter(r -> !r.equals("default"))
                    .distinct()
                    .collect(Collectors.toList());

            // 如果已经注册过了那就不用再注册了
            if (registeredServers.contains(serverId)) {
                return null;
            }

            // 还没有被注册的ruleCategory
            List<Long> defaultRuleCategoryIds = allPointRuleCategoryList.stream()
                    .filter(r -> "default".equals(r.getServerId()))
                    .map(PointRuleCategory::getId)
                    .distinct()
                    .collect(Collectors.toList());

            // 我是最后一个启动的server
            if (registeredServers.size() == serverIdList.size() - 1) {
                pointRuleCategoryProvider.registerDefaultPointRuleCategory(null, serverId);
            }
            // 我不是最后一个
            else {
                int count = allPointRuleCategoryList.size() / serverIdList.size();
                List<Long> ids = defaultRuleCategoryIds.subList(0, count);
                pointRuleCategoryProvider.registerDefaultPointRuleCategory(ids, serverId);
            }

            List<PointRuleCategory> categories = pointRuleCategoryProvider.listPointRuleCategoriesByServerId(serverId);
            List<String> cateList = categories.stream().map(PointRuleCategory::getDisplayName).collect(Collectors.toList());
            LOGGER.info("This server id {}, registered point rule category list {}", serverId, cateList.toString());
            return null;
        });
    }

    @Override
    public Action onLocalBusMessage(Object sender, String subject, Object args, String subscriptionPath) {
        Integer currentNamespaceId = UserContext.getCurrentNamespaceId();
        Long currentUserId = UserContext.currentUserId();

        LocalEvent localEvent = (LocalEvent) args;

        Integer namespaceId = localEvent.getContext() != null
                && localEvent.getContext().getNamespaceId() != null
                ? localEvent.getContext().getNamespaceId() : currentNamespaceId;

        Long creatorUid = localEvent.getContext() != null
                && localEvent.getContext().getUid() != null
                ? localEvent.getContext().getUid() : currentUserId;

        PointEventLog log = new PointEventLog();
        log.setId(pointEventLogProvider.getNextEventLogId());
        log.setNamespaceId(namespaceId);
        log.setEventName(localEvent.getEventName());

        log.setCreateTime(DateUtils.currentTimestamp());
        log.setCreatorUid(creatorUid);
        log.setStatus(PointEventLogStatus.WAITING_FOR_PROCESS.getCode());
        log.setEventJson(StringHelper.toJsonString(localEvent));

        PointEventGroup eventGroup = eventNameToPointEventGroupMap.get(subscriptionPath);
        pointEventGroupCache.computeIfPresent(eventGroup, (group, pointEventLogs) -> {
            log.setCategoryId(eventGroup.getCategory().getId());
            pointEventLogs.add(log);

            if (pointEventLogs.size() >= 1000) {
                scheduledExecutorService.schedule(() -> this.persistGroupEventLog(eventGroup), 0, TimeUnit.SECONDS);
            }
            return pointEventLogs;
        });

        // 同步事件
        // boolean sync = TrueOrFalseFlag.fromCode(localEvent.getSyncFlag()) == TrueOrFalseFlag.TRUE;
        // if (sync) {
        //     this.persistGroupEventLog(eventGroup);
        //     // List<PointRuleCategory> categories = new ArrayList<>();
        //     // categories.add(eventGroup.getCategory());
        //     // eventLogProcessor.doProcessGroup(categories);
        // }
        return Action.none;
    }
}
