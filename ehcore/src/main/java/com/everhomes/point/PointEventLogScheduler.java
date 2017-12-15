package com.everhomes.point;

import com.everhomes.bus.LocalEvent;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.point.PointCommonStatus;
import com.everhomes.rest.point.PointEventLogStatus;
import com.everhomes.server.schema.tables.pojos.EhPointRules;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/12/6.
 */
@Component
public class PointEventLogScheduler implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointEventLogScheduler.class);

    @Value("${core.server.id:}")
    private String serverId;

    private final transient ReentrantLock lock = new ReentrantLock();

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(
            4, new CustomizableThreadFactory("PointEventLogSchedule-"));

    @Autowired(required = false)
    private List<PointEventProcessor> eventProcessors;

    private final Map<String, PointEventProcessor> processorMap = new HashMap<>();

    @Autowired
    private PointEventLogProvider pointEventLogProvider;

    @Autowired
    private PointRuleCategoryProvider pointRuleCategoryProvider;

    @Autowired
    private PointLogProvider pointLogProvider;

    @Autowired
    private PointScoreProvider pointScoreProvider;

    @Autowired
    private PointSystemProvider pointSystemProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private PointActionProvider pointActionProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null && serverId != null && serverId.trim().length() > 0) {
            initEventProcessor();
            initScheduledTask();
            initVMShutdownHook();
        }
    }

    private void initEventProcessor() {
        if (eventProcessors != null) {
            for (PointEventProcessor processor : eventProcessors) {
                try {
                    String[] events = processor.init();
                    for (String event : events) {
                        processorMap.put(event, processor);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            LOGGER.warn("There is no event processor found.");
        }
    }

    void initScheduledTask() {
        scheduledExecutorService.schedule(this::doProcessAll, 60, TimeUnit.SECONDS);
    }

    private void initVMShutdownHook() {
        // 系统终止hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Point event log scheduler VM shutdown hook are triggered.");
            this.doProcessAll();
        }, "PointEventLogShutdownHookThread"));
    }

    private void doProcessAll() {
        lock.lock();
        try {
            int random = (int) (Math.random() * 10);
            long start = System.currentTimeMillis();
            if (random == 5) {
                LOGGER.info("Start to process point event log");
            }

            doProcessGroups();
            scheduledExecutorService.schedule(this::doProcessAll, 60, TimeUnit.SECONDS);

            if (random == 5) {
                long end = System.currentTimeMillis();
                LOGGER.info("End process point event log, elapsed {}s", (end - start) / 1000);
            }
        } catch (Exception e) {
            LOGGER.error("Process point event log error.", e);
        } finally {
            lock.unlock();
        }
    }

    private void doProcessGroups() {
        List<PointRuleCategory> categories = pointRuleCategoryProvider.listPointRuleCategoriesByServerId(serverId);
        for (PointRuleCategory category : categories) {
            try {
                doProcessGroup(category);
            } catch (Throwable t) {
                LOGGER.error("Point event log scheduler do process group error, categoryId = ;" + category.getId(), t);
                throw t;
            }
        }
    }

    private void doProcessGroup(PointRuleCategory category) {
        List<PointSystem> enabledPointSystems = pointSystemProvider.getEnabledPointSystems(null);
        for (PointSystem pointSystem : enabledPointSystems) {
            ListingLocator locator = new ListingLocator();
            do {
                Map<PointScoreLockAndCacheKey, Long> userIdToPointsScoreMap = new HashMap<>();

                List<PointEventLog> pointEventLogs = pointEventLogProvider.listEventLog(category.getId(),
                        PointEventLogStatus.WAITING_FOR_PROCESS.getCode(), 1000, locator);
                List<Long> idList = pointEventLogs.stream().map(PointEventLog::getId).collect(Collectors.toList());

                final List<PointResultAction> actions = new ArrayList<>();
                dbProvider.execute(s -> {
                    pointEventLogProvider.updatePointEventLogStatus(idList, PointEventLogStatus.PROCESSING.getCode());

                    for (PointEventLog log : pointEventLogs) {
                        LocalEvent localEvent = (LocalEvent) StringHelper.fromJsonString(
                                log.getEventJson(), LocalEvent.class);

                        PointEventProcessor processor = getPointEventProcessor(log.getEventName());
                        if (processor == null) {
                            continue;
                        }

                        List<PointRule> pointRules = processor.getPointRules(pointSystem, localEvent);
                        for (PointRule rule : pointRules) {
                            PointCommonStatus status = PointCommonStatus.fromCode(rule.getStatus());
                            if (status == PointCommonStatus.DISABLED) {
                                continue;
                            }
                            PointEventProcessResult result = processor.execute(localEvent, rule, pointSystem, category);
                            if (result == null) {
                                continue;
                            }

                            List<PointAction> pointActions = pointActionProvider.listByOwner(pointSystem.getNamespaceId(), pointSystem.getId(), EhPointRules.class.getSimpleName(), rule.getId());
                            List<PointResultAction> resultActions = processor.getResultActions(pointActions, localEvent, rule, pointSystem, category);
                            if (resultActions != null && resultActions.size() > 0) {
                                actions.addAll(resultActions);
                            }

                            userIdToPointsScoreMap.compute(getKey(pointSystem.getNamespaceId(), pointSystem.getId(),
                                    localEvent.getContext().getUid()), (uid, score) -> {
                                if (score == null) {
                                    score = result.getPoints();
                                } else {
                                    score += result.getPoints();
                                }
                                return score;
                            });
                            pointLogProvider.createPointLog(result.getLog());
                        }
                    }
                    persistPointScore(userIdToPointsScoreMap);
                    pointEventLogProvider.updatePointEventLogStatus(idList, PointEventLogStatus.PROCESSED.getCode());
                    return true;
                });

                actions.stream().filter(Objects::nonNull).forEach(r -> {
                    try {
                        r.doAction();
                    } catch (Exception e) {
                        LOGGER.error("Point action doAction error, action = " + r.toString(), e);
                    }
                });
            } while (locator.getAnchor() != null);
        }
    }

    private PointEventProcessor getPointEventProcessor(String eventName) {
        String[] split = eventName.split("\\.");
        for (int i = split.length; i >= 0; i--) {
            String[] tokens = new String[i];
            System.arraycopy(split, 0, tokens, 0, i);
            String name = StringUtils.join(tokens, ".");

            PointEventProcessor processor = processorMap.get(name);
            if (processor != null) {
                return processor;
            }
        }
        return null;
    }

    private void persistPointScore(Map<PointScoreLockAndCacheKey, Long> userIdToPointsScoreMap) {
        userIdToPointsScoreMap.forEach((k, v) -> {
            coordinationProvider.getNamedLock(
                    CoordinationLocks.POINT_UPDATE_POINT_SCORE.getCode() + k.toString()).enter(() -> {
                PointScore pointScore = pointScoreProvider.findUserPointScore(
                        k.namespaceId, k.systemId, k.uid, PointScore.class);
                if (pointScore == null) {
                    pointScore = new PointScore();
                    pointScore.setScore(v);
                    pointScore.setUserId(k.uid);
                    pointScore.setSystemId(k.systemId);
                    pointScore.setNamespaceId(k.namespaceId);
                    pointScoreProvider.createPointScore(pointScore);
                } else {
                    pointScore.setScore(pointScore.getScore() + v);
                    pointScoreProvider.updatePointScore(pointScore);
                }
                return true;
            });
        });
    }

    private PointScoreLockAndCacheKey getKey(Integer namespaceId, Long sysId, Long uid) {
        return new PointScoreLockAndCacheKey(namespaceId, sysId, uid);
    }
}
