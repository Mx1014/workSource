package com.everhomes.point;

import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.util.ConvertHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.util.*;
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

    private static int SCHEDULE_DURATION_SECONDS = 60;

    private final static Random random = new Random();

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

    @Autowired
    private PointEventLogScheduler pointEventLogScheduler;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // if (event.getApplicationContext().getParent() == null
        //         && serverId != null && serverId.trim().length() > 0
        //         && serverIdList != null && serverIdList.size() > 0) {
        //     registerPointRuleCategory();
        //     initPointRuleCategoryQueue();
        //     initScheduledExecutorService();
        //     initVMShutdownHook();
        // }
    }

    private void initScheduledExecutorService() {
        scheduledExecutorService.schedule(() -> this.persistAllEventLog(true), scheduleDurationSeconds(), TimeUnit.SECONDS);
    }

    private int scheduleDurationSeconds() {
        try {
            SCHEDULE_DURATION_SECONDS =
                    configurationProvider.getIntValue(
                            "point.persistLog.durationSeconds",
                            SCHEDULE_DURATION_SECONDS);
        } catch (Exception e) {
            // ignore
        }
        return SCHEDULE_DURATION_SECONDS;
    }

    private void initVMShutdownHook() {
        // 系统终止hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Point event log persist VM shutdown hook are triggered.");
            persistAllEventLog(false);
        }, "PointEventLogShutdownHookThread"));
    }

    private void persistAllEventLog(boolean next) {
        try {
            pointEventGroupCache.keySet().forEach(this::persistGroupEventLog);
        } catch (Exception e) {
            LOGGER.error("Point persist group event log error", e);
        } finally {
            if (next) {
                scheduledExecutorService.schedule(() -> this.persistAllEventLog(true), scheduleDurationSeconds(), TimeUnit.SECONDS);
            }
        }
    }

    private void persistGroupEventLog(PointEventGroup eventGroup) {
        if (eventGroup.tryLock()) {
            int i = random.nextInt(100);
            if (i == 1) {
                LOGGER.info("Start to persist point event log");
            }

            List<PointEventLog> removeLogs = new ArrayList<>();
            try {
                CopyOnWriteArrayList<PointEventLog> eventList = pointEventGroupCache.get(eventGroup);
                if (eventList.size() > 0) {
                    removeLogs.addAll(eventList);
                    eventList.removeAll(removeLogs);
                }
            } finally {
                eventGroup.unlock();
            }
            if (i == 1) {
                LOGGER.info("Persist point event log size {}", removeLogs.size());
            }
            pointEventLogProvider.createPointEventLogsWithId(removeLogs);
        }
    }

    void initPointRuleCategoryQueue() {
        lock.lock();
        try {
            List<PointRuleCategory> pointRuleCategoryList = pointRuleCategoryProvider.listPointRuleCategories();
            // 事件队列
            for (PointRuleCategory category : pointRuleCategoryList) {
                PointEventGroup eventGroup = new PointEventGroup(category);
                pointEventGroupCache.put(eventGroup, new CopyOnWriteArrayList<>());

                List<PointRule> pointRules = pointRuleProvider.listPointRuleByCategoryId(category.getId());
                for (PointRule rule : pointRules) {
                    List<PointRuleToEventMapping> mappings = pointRuleToEventMappingProvider.listByPointRule(rule.getId());
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
        /*LocalEvent localEvent = (LocalEvent) args;

        PointEventProcessorHolder processorHolder = pointEventLogScheduler.getPointEventProcessor(localEvent.getEventName());
        PointEventProcessorHolder processorHolder1 = pointEventLogScheduler.getPointEventProcessor(subscriptionPath);

        // 是否允许树形调用
        if (!processorHolder.isContinue(processorHolder1)) {
            return Action.none;
        }

        Integer currentNamespaceId = UserContext.getCurrentNamespaceId();
        Long currentUserId = UserContext.currentUserId();

        Integer namespaceId = localEvent.getContext() != null
                && localEvent.getContext().getNamespaceId() != null
                ? localEvent.getContext().getNamespaceId() : currentNamespaceId;

        Long creatorUid = localEvent.getContext() != null
                && localEvent.getContext().getUid() != null
                ? localEvent.getContext().getUid() : currentUserId;

        PointEventLog log = new PointEventLog();
        log.setNamespaceId(namespaceId);
        log.setEventName(localEvent.getEventName());
        log.setSubscriptionPath(subscriptionPath);

        log.setCreateTime(DateUtils.currentTimestamp());
        log.setCreatorUid(creatorUid);
        log.setStatus(PointEventLogStatus.WAITING_FOR_PROCESS.getCode());
        log.setEventJson(StringHelper.toJsonString(localEvent));

        // 同步事件
        if (Objects.equals(localEvent.getSyncFlag(), TrueOrFalseFlag.TRUE.getCode())) {
            doSyncEvent(processorHolder, localEvent, log);
            return Action.none;
        }

        processorHolder.doProcess(processor -> {
            PointEventGroup eventGroup = processor.getEventGroup(eventNameToPointEventGroupMap, localEvent, subscriptionPath);
            pointEventGroupCache.computeIfPresent(eventGroup, (group, pointEventLogs) -> {
                // copy
                PointEventLog eventLog = ConvertHelper.convert(log, PointEventLog.class);
                eventLog.setId(pointEventLogProvider.getNextEventLogId());
                eventLog.setCategoryId(eventGroup.getCategory().getId());
                pointEventLogs.add(eventLog);

                if (pointEventLogs.size() >= 1000) {
                    scheduledExecutorService.schedule(() -> this.persistGroupEventLog(eventGroup), 0, TimeUnit.SECONDS);
                }
                return pointEventLogs;
            });
        });*/
        return Action.none;
    }

    private void doSyncEvent(PointEventProcessorHolder processorHolder, LocalEvent localEvent, PointEventLog log) {
        processorHolder.doProcess(processor -> {
            List<PointRule> pointRules = processor.getPointRules(localEvent);

            if (pointRules.size() > 0) {
                // copy
                PointEventLog eventLog = ConvertHelper.convert(log, PointEventLog.class);
                eventLog.setId(pointEventLogProvider.getNextEventLogId());
                eventLog.setCategoryId(pointRules.get(0).getCategoryId());
                pointEventLogProvider.createPointEventLogsWithId(Collections.singletonList(eventLog));

                List<Long> cateIds = pointRules.stream().map(PointRule::getCategoryId).distinct().collect(Collectors.toList());
                for (Long cateId : cateIds) {
                    PointRuleCategory category = pointRuleCategoryProvider.findById(cateId);
                    pointEventLogScheduler.doProcessGroup(category, eventLog.getNamespaceId());
                }
            }
        });
    }
}