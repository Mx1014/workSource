package com.everhomes.point.processor;

import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.SystemEvent;
import com.everhomes.point.*;
import com.everhomes.point.limit.PointRuleLimitData;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class GeneralPointEventProcessor implements PointEventProcessor {

    @Autowired
    protected UserService userService;

    @Autowired
    protected PointLogProvider pointLogProvider;

    @Autowired
    private PointRuleProvider pointRuleProvider;

    @Autowired
    private PointRuleToEventMappingProvider pointRuleToEventMappingProvider;

    @Override
    public String[] init() {
        SystemEvent[] values = SystemEvent.values();
        String[] events = new String[values.length - 1];
        for (int i = 0; i < events.length; i++) {
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

        Long uid = localEvent.getContext().getUid();
        Integer namespaceId = localEvent.getContext().getNamespaceId();

        PointLog pointLog = new PointLog();
        pointLog.setArithmeticType(rule.getArithmeticType());
        pointLog.setOperatorType(PointOperatorType.SYSTEM.getCode());
        pointLog.setTargetUid(uid);
        pointLog.setNamespaceId(namespaceId);
        pointLog.setSystemId(pointSystem.getId());
        pointLog.setEventName(localEvent.getEventName());
        pointLog.setEntityType(localEvent.getEntityType());
        pointLog.setEntityId(localEvent.getEntityId());
        pointLog.setDescription(rule.getDescription());

        UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(uid);
        pointLog.setTargetPhone(userInfo.getPhones().get(0));
        pointLog.setTargetName(userInfo.getNickName());
        pointLog.setPoints(rule.getPoints());
        pointLog.setRuleId(rule.getId());
        pointLog.setRuleName(rule.getDisplayName());
        pointLog.setCategoryId(rule.getCategoryId());
        pointLog.setCategoryName(pointRuleCategory.getDisplayName());

        Long points = rule.getPoints();

        if (rule.getBindingRuleId() != null && rule.getBindingRuleId() != 0) {
            PointLog bindingPointLog = pointLogProvider.findByRuleIdAndEntity(namespaceId, uid, rule.getBindingRuleId(),
                    localEvent.getEntityType(), localEvent.getEntityId());
            if (bindingPointLog != null) {
                if (PointArithmeticType.fromCode(rule.getArithmeticType()) == PointArithmeticType.SUBTRACT) {
                    points = -rule.getPoints();
                }
            } else {
                return null;
            }
        }
        return new PointEventProcessResult(points, pointLog);
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
    public List<PointRule> getPointRules(PointSystem pointSystem, LocalEvent localEvent) {
        String[] split = localEvent.getEventName().split("\\.");
        for (int i = split.length; i >= 0; i--) {
            String[] tokens = new String[i];
            System.arraycopy(split, 0, tokens, 0, i);
            String eventName = StringUtils.join(tokens, ".");

            List<PointRuleToEventMapping> mappings = pointRuleToEventMappingProvider.listByEventName(
                    localEvent.getContext().getNamespaceId(), pointSystem.getId(), eventName);
            if (mappings != null && mappings.size() > 0) {
                List<Long> ruleIds = mappings.stream().map(PointRuleToEventMapping::getRuleId).collect(Collectors.toList());
                return pointRuleProvider.listPointRuleByIds(ruleIds);
            }
        }
        return new ArrayList<>();
    }

    private boolean isValidEvent(LocalEvent localEvent) {
        if (localEvent.getContext() != null && localEvent.getContext().getUid() != null) {
            UserInfo userInfo = userService.getUserSnapshotInfoWithPhone(localEvent.getContext().getUid());
            if (userInfo != null && userInfo.getPhones() != null && userInfo.getPhones().size() > 0) {
                return true;
            }
        }
        return false;
    }

    private boolean doLimit(LocalEvent localEvent, PointRule rule, PointSystem pointSystem) {
        PointRuleLimitType limitType = PointRuleLimitType.fromCode(rule.getLimitType());

        Long uid = localEvent.getContext().getUid();
        Integer namespaceId = localEvent.getContext().getNamespaceId();

        List<PointRuleToEventMapping> mappings = pointRuleToEventMappingProvider.listByPointRule(pointSystem.getId(), rule.getId());

        switch (limitType) {
            case TIMES_PER_DAY: {
                LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                for (PointRuleToEventMapping mapping : mappings) {
                    Integer count = pointLogProvider.countPointLog(
                            namespaceId, pointSystem.getId(), uid, mapping.getEventName(), dateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
                    PointRuleLimitData limitData = (PointRuleLimitData) StringHelper.fromJsonString(rule.getLimitData(), PointRuleLimitData.class);
                    if (count >= limitData.getTimes()) {
                        return false;
                    }
                }
                break;
            }
            case TIMES: {
                for (PointRuleToEventMapping mapping : mappings) {
                    Integer count = pointLogProvider.countPointLog(
                            namespaceId, pointSystem.getId(), uid, mapping.getEventName(), null);
                    PointRuleLimitData limitData = (PointRuleLimitData) StringHelper.fromJsonString(rule.getLimitData(), PointRuleLimitData.class);
                    if (count >= limitData.getTimes()) {
                        return false;
                    }
                }
                break;
            }
        }
        return true;
    }
}
