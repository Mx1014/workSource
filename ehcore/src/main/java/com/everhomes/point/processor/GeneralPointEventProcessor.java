package com.everhomes.point.processor;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.point.*;
import com.everhomes.point.limit.PointRuleLimitDataVO;
import com.everhomes.point.limit.PointRuleLimitTypeVO;
import com.everhomes.rest.point.PointActionType;
import com.everhomes.rest.point.PointArithmeticType;
import com.everhomes.rest.point.PointOperatorType;
import com.everhomes.rest.point.PointRuleLimitType;
import com.everhomes.rest.user.UserInfo;
import com.everhomes.user.UserService;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class GeneralPointEventProcessor implements IGeneralPointEventProcessor {

    @Autowired
    protected UserService userService;

    @Autowired
    protected PointLogProvider pointLogProvider;

    @Autowired
    protected PointRuleProvider pointRuleProvider;

    @Autowired
    protected PointRuleToEventMappingProvider pointRuleToEventMappingProvider;

    @Override
    public String[] init() {
        SystemEvent[] values = SystemEvent.values();
        String[] events = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            events[i] = values[i].getCode();
        }
        return events;
    }

    @Override
    public PointEventProcessResult execute(LocalEvent localEvent, PointRule rule, PointSystem pointSystem, PointRuleCategory pointRuleCategory) {
        boolean isValidEvent = isValidEvent(localEvent);
        if (!isValidEvent) {
            return null;
        }

        boolean passLimit = doLimit(localEvent, rule, pointSystem);
        if (!passLimit) {
            return null;
        }

        Long targetUid = localEvent.getContext().getUid();
        Integer namespaceId = localEvent.getContext().getNamespaceId();

        PointLog pointLog = new PointLog();
        pointLog.setArithmeticType(rule.getArithmeticType());
        pointLog.setOperatorType(PointOperatorType.SYSTEM.getCode());
        pointLog.setTargetUid(targetUid);
        pointLog.setNamespaceId(namespaceId);
        pointLog.setSystemId(pointSystem.getId());
        pointLog.setEventName(localEvent.getEventName());
        pointLog.setEntityType(localEvent.getEntityType());
        pointLog.setEntityId(localEvent.getEntityId());
        pointLog.setDescription(rule.getDescription());

        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(targetUid);
        pointLog.setTargetPhone(userInfo.getPhones().get(0));
        pointLog.setTargetName(userInfo.getNickName());

        Long points = getPoints(localEvent, pointSystem, rule);
        pointLog.setPoints(Math.abs(points));

        pointLog.setRuleId(rule.getId());
        pointLog.setRuleName(rule.getDisplayName());
        pointLog.setCategoryId(rule.getCategoryId());
        pointLog.setCategoryName(pointRuleCategory.getDisplayName());
        pointLog.setEventHappenTime(localEvent.getCreateTime());

        if (rule.getBindingRuleId() != null && rule.getBindingRuleId() != 0) {
            PointLog bindingPointLog = pointLogProvider.findByRuleIdAndEntity(namespaceId, pointSystem.getId(), targetUid, rule.getBindingRuleId(),
                    localEvent.getEntityType(), localEvent.getEntityId());
            if (bindingPointLog == null) {
                return null;
            }
        }
        processPointLog(pointLog, localEvent, rule, pointSystem, pointRuleCategory);

        return new PointEventProcessResult(points, pointLog);
    }

    protected void processPointLog(PointLog pointLog, LocalEvent localEvent, PointRule rule, PointSystem pointSystem, PointRuleCategory pointRuleCategory) {

    }

    protected Long getPoints(LocalEvent localEvent, PointSystem pointSystem, PointRule rule) {
        Long points = rule.getPoints();
        if (PointArithmeticType.fromCode(rule.getArithmeticType()) == PointArithmeticType.SUBTRACT) {
            points = -rule.getPoints();
        }
        return points;
    }

    @Override
    public List<PointResultAction> getResultActions(List<PointAction> pointActions, LocalEvent localEvent, PointRule rule, PointSystem pointSystem, PointRuleCategory category) {
        List<PointResultAction> resultActions = new ArrayList<>();
        for (PointAction pointAction : pointActions) {
            PointActionType actionType = PointActionType.fromCode(pointAction.getActionType());
            switch (actionType) {
                case MESSAGE:
                    resultActions.add(new PointResultMessageAction(pointAction, pointSystem, rule, localEvent));
                    break;
            }
        }
        return resultActions;
    }

    @Override
    public boolean isContinue(BasePointEventProcessor eventProcessor) {
        return this.equals(eventProcessor);
    }

    @Override
    public PointEventGroup getEventGroup(Map<String, PointEventGroup> eventNameToPointEventGroupMap, LocalEvent localEvent, String subscriptionPath) {
        String[] split = getEventName(localEvent).split("\\.");
        for (int i = split.length; i >= 0; i--) {
            String[] tokens = new String[i];
            System.arraycopy(split, 0, tokens, 0, i);
            String name = StringUtils.join(tokens, ".");

            PointEventGroup eventGroup = eventNameToPointEventGroupMap.get(name);
            if (eventGroup != null) {
                return eventGroup;
            }
        }
        return eventNameToPointEventGroupMap.get(subscriptionPath);
    }

    protected String getEventName(LocalEvent localEvent) {
        return localEvent.getEventName();
    }

    @Override
    public List<PointRule> getPointRules(LocalEvent localEvent) {
        String[] split = getEventName(localEvent).split("\\.");
        for (int i = split.length; i >= 0; i--) {
            String[] tokens = new String[i];
            System.arraycopy(split, 0, tokens, 0, i);
            String name = StringUtils.join(tokens, ".");

            List<PointRuleToEventMapping> mappings = pointRuleToEventMappingProvider.listByEventName(name);
            if (mappings != null && mappings.size() > 0) {
                List<Long> ruleIds = mappings.stream().map(PointRuleToEventMapping::getRuleId).collect(Collectors.toList());
                return pointRuleProvider.listPointRuleByIds(ruleIds);
            }
        }
        return new ArrayList<>();
    }

    protected boolean isValidEvent(LocalEvent localEvent) {
        if (localEvent.getContext() != null && localEvent.getContext().getUid() != null) {
            UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(localEvent.getContext().getUid());
            if (userInfo != null && userInfo.getPhones() != null && userInfo.getPhones().size() > 0) {
                return true;
            }
        }
        return false;
    }

    protected boolean doLimit(LocalEvent localEvent, PointRule rule, PointSystem pointSystem) {
        PointRuleLimitTypeVO limitTypeVO = (PointRuleLimitTypeVO) StringHelper.fromJsonString(rule.getLimitType(), PointRuleLimitTypeVO.class);
        PointRuleLimitType limitType = PointRuleLimitType.fromCode(limitTypeVO.getType());

        Long targetUid = localEvent.getContext().getUid();
        Integer namespaceId = localEvent.getContext().getNamespaceId();

        if (limitType != null) {
            switch (limitType) {
                case TIMES_PER_DAY: {
                    LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    Integer count = pointLogProvider.countPointLog(
                            namespaceId, pointSystem.getId(), targetUid, rule.getId(), Timestamp.valueOf(dateTime), null);
                    PointRuleLimitDataVO limitData = (PointRuleLimitDataVO) StringHelper.fromJsonString(rule.getLimitData(), PointRuleLimitDataVO.class);
                    if (count >= limitData.getTimes()) {
                        return false;
                    }
                    break;
                }
                case TIMES: {
                    Integer count = pointLogProvider.countPointLog(
                            namespaceId, pointSystem.getId(), targetUid, rule.getId(), null, null);
                    PointRuleLimitDataVO limitData = (PointRuleLimitDataVO) StringHelper.fromJsonString(rule.getLimitData(), PointRuleLimitDataVO.class);
                    if (count >= limitData.getTimes()) {
                        return false;
                    }
                    break;
                }
            }
        }
        return true;
    }
}
