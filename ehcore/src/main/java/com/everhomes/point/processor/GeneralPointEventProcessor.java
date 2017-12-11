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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xq.tian on 2017/12/7.
 */
@Component
public class GeneralPointEventProcessor implements PointEventProcessor {

    @Autowired
    protected UserService userService;

    @Autowired
    protected PointLogProvider pointLogProvider;

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
    public PointEventProcessResult execute(PointEventLog log, PointRule rule, PointSystem pointSystem, PointRuleCategory pointRuleCategory) {
        LocalEvent localEvent = (LocalEvent) StringHelper.fromJsonString(log.getEventJson(), LocalEvent.class);

        boolean isValidEvent = isValidEvent(localEvent);
        if (!isValidEvent) {
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
        pointLog.setEventName(log.getEventName());
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

        boolean passLimit = doLimit(localEvent, rule, pointSystem);
        if (!passLimit) {
            return null;
        }

        if (rule.getBindingEventName() != null && rule.getBindingEventName().trim().length() > 0) {
            PointLog bindingPointLog = pointLogProvider.findByUidAndEntity(namespaceId, uid,
                    rule.getBindingEventName(), localEvent.getEntityType(), localEvent.getEntityId());
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
    public List<PointResultAction> getResultActions(List<PointAction> pointActions, PointEventLog log, PointRule rule, PointSystem pointSystem, PointRuleCategory category) {
        List<PointResultAction> resultActions = new ArrayList<>();
        for (PointAction pointAction : pointActions) {
            PointActionType actionType = PointActionType.fromCode(pointAction.getActionType());
            switch (actionType) {
                case MESSAGE:
                    resultActions.add(new PointResultMessageAction(pointAction, pointSystem, rule, log));
                    break;
            }
        }
        return resultActions;
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

        switch (limitType) {
            case TIMES_PER_DAY: {
                long milli = Clock.systemUTC().instant().toEpochMilli();
                Integer count = pointLogProvider.countPointLog(
                        namespaceId, pointSystem.getId(), uid, rule.getEventName(), milli);
                PointRuleLimitData limitData = (PointRuleLimitData) StringHelper.fromJsonString(rule.getLimitData(), PointRuleLimitData.class);
                if (count >= limitData.getTimes()) {
                    return false;
                }
                break;
            }
            case TIMES: {
                Integer count = pointLogProvider.countPointLog(
                        namespaceId, pointSystem.getId(), uid, rule.getEventName(), null);
                PointRuleLimitData limitData = (PointRuleLimitData) StringHelper.fromJsonString(rule.getLimitData(), PointRuleLimitData.class);
                if (count >= limitData.getTimes()) {
                    return false;
                }
                break;
            }
        }
        return true;
    }
}
