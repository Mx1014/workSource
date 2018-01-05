package com.everhomes.point;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.mail.MailHandler;
import com.everhomes.namespace.Namespace;
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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * Created by xq.tian on 2017/12/6.
 */
@Component
public class PointEventLogScheduler implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointEventLogScheduler.class);

    private static final int SCHEDULE_DURATION_SECONDS = 60;

    private final static Random random = new Random();

    @Value("${core.server.id:}")
    private String serverId;

    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(
            1, new CustomizableThreadFactory("PointEventLogSchedule-"));

    @Autowired(required = false)
    private List<IPointEventProcessor> eventProcessors;

    @Autowired(required = false)
    private List<IGeneralPointEventProcessor> generalProcessors;

    private final Map<String, BasePointEventProcessor> processorMap = new HashMap<>();

    @Autowired
    private ConfigurationProvider configurationProvider;

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

    @Autowired
    private PointLocalBusSubscriber pointLocalBusSubscriber;

    @Autowired
    private PointRuleConfigProvider pointRuleConfigProvider;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null
                && serverId != null && serverId.trim().length() > 0) {
            initEventProcessor();
            initScheduledTask();
            initRestartSubscriber();
        }
    }

    private void initEventProcessor() {
        BiConsumer<String, BasePointEventProcessor> putCon = processorMap::put;

        // 通用处理器
        if (generalProcessors != null) {
            // 通用处理器需要单独订阅
            BiConsumer<String, BasePointEventProcessor> andThen = putCon.andThen(
                    (event, processor) -> LocalEventBus.subscribe(event, pointLocalBusSubscriber)
            );
            initProcessor(generalProcessors, andThen);
        }

        // 具体处理器
        if (eventProcessors != null) {
            initProcessor(eventProcessors, putCon);
        } else {
            LOGGER.warn("There is no event processor found.");
        }
    }

    private void initProcessor(List<? extends BasePointEventProcessor> processors, BiConsumer<String, BasePointEventProcessor> consumer) {
        for (BasePointEventProcessor processor : processors) {
            try {
                String[] events = processor.init();
                for (String event : events) {
                    consumer.accept(event, processor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initScheduledTask() {
        scheduledExecutorService.schedule(this::doProcessAll, SCHEDULE_DURATION_SECONDS, TimeUnit.SECONDS);
    }

    private void initRestartSubscriber() {
        LocalEventBus.subscribe("PointEventLogSchedulerRestart", (sender, subject, args, subscriptionPath) -> {
            if (scheduledExecutorService.isTerminated()) {
                scheduledExecutorService = Executors.newScheduledThreadPool(
                        1, new CustomizableThreadFactory("PointEventLogSchedule-"));
                initScheduledTask();
            }
            return LocalBusSubscriber.Action.none;
        });
    }

    private void doProcessAll() {
        try {
            int i = random.nextInt(100);
            long start = System.currentTimeMillis();
            if (i == 1) {
                LOGGER.info("Start to process point event log");
            }

            doProcessGroups();
            scheduledExecutorService.schedule(this::doProcessAll, SCHEDULE_DURATION_SECONDS, TimeUnit.SECONDS);

            if (i == 1) {
                long end = System.currentTimeMillis();
                LOGGER.info("End process point event log, elapsed {}s", (end - start) / 1000);
            }
        } catch (Exception e) {
            scheduledExecutorService.shutdown();

            LOGGER.error("Process point event log error.", e);
            sendExceptionMail(e);
        }
    }

    private void sendExceptionMail(Exception e) {
        // ------------------------------------------------------------------------
        String xqt = "xq.tian@zuolin.com";
        String home = configurationProvider.getValue(ConfigConstants.HOME_URL, "");
        String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
        MailHandler handler = PlatformContext.getComponent(handlerName);
        String account = configurationProvider.getValue(0, "mail.smtp.account", "zuolin@zuolin.com");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintStream stream = new PrintStream(out)) {
            e.printStackTrace(stream);
            String message = out.toString("UTF-8");
            handler.sendMail(0, account, xqt, "PointEventLogSchedule error (" + home + ")", message);
        } catch (Exception ignored) { }
        // ------------------------------------------------------------------------
    }

    private void doProcessGroups() {
        List<PointRuleCategory> categories = pointRuleCategoryProvider.listPointRuleCategoriesByServerId(serverId);
        for (PointRuleCategory category : categories) {
            try {
                doProcessGroup(category, null);
            } catch (Throwable t) {
                LOGGER.error("Point event log scheduler do process group error, categoryId = " + category.getId(), t);
                throw new RuntimeException("DoProcessGroup categoryId = " + category.getId(), t);
            }
        }
    }

    void doProcessGroup(PointRuleCategory category, Integer namespaceId) {
        coordinationProvider.getNamedLock(CoordinationLocks.POINT_CATEGORY_SCHEDULE.getCode() + category.getId()).tryEnter(() -> {
            ListingLocator locator = new ListingLocator();
            do {
                List<PointEventLog> pointEventLogs = pointEventLogProvider.listEventLog(namespaceId, category.getId(),
                        PointEventLogStatus.WAITING_FOR_PROCESS.getCode(), 1000, locator);

                for (PointEventLog log : pointEventLogs) {
                    final List<PointResultAction> actions = new ArrayList<>();
                    dbProvider.execute(s -> {
                        Map<PointScoreLockAndCacheKey, Long> pointKeyToScoreMap = new HashMap<>();
                        // 设置状态为处理中
                        log.setStatus(PointEventLogStatus.PROCESSING.getCode());
                        pointEventLogProvider.updatePointEventLog(log);

                        List<PointSystem> enabledPointSystems = pointSystemProvider.getEnabledPointSystems(log.getNamespaceId());
                        for (PointSystem pointSystem : enabledPointSystems) {
                            LocalEvent localEvent = (LocalEvent) StringHelper.fromJsonString(log.getEventJson(), LocalEvent.class);

                            BasePointEventProcessor processor = getPointEventProcessor(log.getEventName());
                            if (processor == null) {
                                continue;
                            }

                            List<PointRule> pointRules = processor.getPointRules(localEvent);
                            for (PointRule rule : pointRules) {
                                // rule config
                                PointRuleConfig ruleConfig = pointRuleConfigProvider.findByRuleIdAndSystemId(
                                        pointSystem.getId(), rule.getId());

                                if (ruleConfig != null) {
                                    rule.setStatus(ruleConfig.getStatus());
                                    rule.setLimitData(ruleConfig.getLimitData());
                                    rule.setLimitType(ruleConfig.getLimitType());
                                    rule.setPoints(ruleConfig.getPoints());
                                    rule.setDescription(ruleConfig.getDescription());
                                }

                                PointCommonStatus status = PointCommonStatus.fromCode(rule.getStatus());
                                if (status == PointCommonStatus.DISABLED) {
                                    continue;
                                }
                                PointEventProcessResult result = processor.execute(localEvent, rule, pointSystem, category);
                                if (result == null) {
                                    continue;
                                }

                                List<PointAction> pointActions = pointActionProvider.listByOwner(
                                        Namespace.DEFAULT_NAMESPACE, EhPointRules.class.getSimpleName(), rule.getId());
                                List<PointResultAction> resultActions = processor.getResultActions(
                                        pointActions, localEvent, rule, pointSystem, category);

                                if (resultActions != null && resultActions.size() > 0) {
                                    actions.addAll(resultActions);
                                }

                                pointKeyToScoreMap.compute(getKey(pointSystem.getNamespaceId(), pointSystem.getId(),
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

                        // 设置状态为已完成
                        log.setStatus(PointEventLogStatus.PROCESSED.getCode());
                        pointEventLogProvider.updatePointEventLog(log);
                        persistPointScore(pointKeyToScoreMap);
                        return true;
                    });
                    actions.stream().filter(Objects::nonNull).forEach(r -> {
                        try {
                            r.doAction();
                        } catch (Exception e) {
                            LOGGER.error("Point action doAction error, action = " + r.toString(), e);
                        }
                    });
                }
            } while (locator.getAnchor() != null);
        });
    }

    public BasePointEventProcessor getPointEventProcessor(String eventName) {
        String[] split = eventName.split("\\.");
        for (int i = split.length; i >= 0; i--) {
            String[] tokens = new String[i];
            System.arraycopy(split, 0, tokens, 0, i);
            String name = StringUtils.join(tokens, ".");

            BasePointEventProcessor processor = processorMap.get(name);
            if (processor != null) {
                return processor;
            }
        }
        return null;
    }

    private void persistPointScore(Map<PointScoreLockAndCacheKey, Long> pointKeyToScoreMap) {
        pointKeyToScoreMap.forEach((k, v) -> {
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
